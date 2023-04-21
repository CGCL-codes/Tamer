package com.objecteffects.clublist.geo;

import com.google.inject.Inject;
import com.google.appengine.api.datastore.GeoPt;
import com.objecteffects.clublist.domain.Club;
import com.objecteffects.clublist.service.ClubService;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Rusty Wright
 */
public final class ClubGeocoder {

    private final transient Logger log = LoggerFactory.getLogger(this.getClass());

    private final ClubService clubService;

    private final GeoCodeAddress geoCodeAddress;

    @Inject
    ClubGeocoder(final ClubService clubService, final GeoCodeAddress geoCodeAddress) {
        this.geoCodeAddress = geoCodeAddress;
        this.clubService = clubService;
    }

    /**
     * Geocode a club from its address. If geocoding fails then return
     * false whereupon the calling action bean will throw an exception
     * which signals to the task queue that this task needs to be
     * re-queued.
     * 
     * @param clubId
     * @return true if geocoding succeeded, or it can't be geocoded
     */
    public boolean geocodeClub(final long clubId) {
        if (clubId == 0) {
            this.log.warn("missing clubId");
            return (true);
        }
        Club club = null;
        try {
            club = this.clubService.find(Long.valueOf(clubId));
        } catch (final Exception e) {
            this.log.warn(ExceptionUtils.getFullStackTrace(e));
            return (true);
        }
        this.log.debug("club: {}", club);
        if (club == null) {
            return (true);
        }
        club.geocodeAttemptsInc();
        GeoPt location = null;
        try {
            if ((club.getState() != null) || (club.getZipcode() != null)) {
                location = this.geoCodeAddress.locationFromAddress(club);
            }
        } catch (final Exception ex) {
            this.log.error(ExceptionUtils.getStackTrace(ex));
        }
        this.log.debug("location: {}", location);
        club.setLocation(location);
        this.clubService.put(club);
        if (location == null) {
            return (false);
        }
        return (true);
    }
}
