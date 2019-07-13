package com.adechinan.dev.sblocationapi;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/locations")
@RequiredArgsConstructor
public class LocationAPI {

    private final LocationRepository locationRepository;

    // add
    @PostMapping
    public Location storeLocation(@RequestBody LocationDTO locationDTO){

        GeoJsonPoint geoJsonPoint = new GeoJsonPoint(Double.valueOf(locationDTO.getLongitude()),
                Double.valueOf(locationDTO.getLatitude()));

        Location location = new Location();
        location.setGeoPoint(geoJsonPoint);

        return locationRepository.save(location);
    }

    // retrieve top nearest
    @GetMapping
    public Page<Location> getNearest(@RequestParam("lat") String latitude,
                                     @RequestParam("long") String longitude,
                                     @RequestParam("d") double distance, Pageable pageable){
        return this.locationRepository.findAllByGeoPointNear(
                new Point(Double.valueOf(longitude), Double.valueOf(latitude)),
                new Distance(distance, Metrics.KILOMETERS), pageable);
    }

}

/*


.param("page", "5")
        .param("size", "10")
        .param("sort", "id,desc")   // <-- no space after comma!
        .param("sort", "name,asc")) // <-- no space after comma!

 */
