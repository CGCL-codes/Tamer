package com.endigi.ceedws.database;

import com.endigi.ceedws.database.domain.LocationCode;
import com.endigi.frame.base.genericdao.search.Search;
import com.endigi.frame.base.genericdao.search.SearchResult;

public interface LocationCodeService {

    public SearchResult<LocationCode> searchLocationCode(Search search);

    public LocationCode getLocationCodeById(String id);

    public void saveLocationCode(LocationCode code);

    public void updateLocationCode(LocationCode code);

    public void deleteLocationCodeById(String id);

    public LocationCode getLocationCodeByLocationName(String locationName, String parentName);
}
