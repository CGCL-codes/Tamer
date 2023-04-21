package com.adserversoft.flexfuse.server.api.service;

import com.adserversoft.flexfuse.server.api.AdPlace;
import com.adserversoft.flexfuse.server.api.ui.AdPlaceBookings;
import com.adserversoft.flexfuse.server.api.ui.ServerRequest;
import java.util.List;

/**
 * Author: Vitaly Sazanovich
 * Vitaly.Sazanovich@gmail.com
 */
public interface IAdPlaceManagementService {

    public List<AdPlace> getAdPlaces4Booking(ServerRequest sr) throws Exception;

    public AdPlaceBookings getAdPlaceBookings(ServerRequest sr, Integer adPlaceId) throws Exception;
}
