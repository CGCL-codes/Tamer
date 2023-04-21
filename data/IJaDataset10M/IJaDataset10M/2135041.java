package org.kablink.teaming.gwt.client.rpc.shared;

import java.util.List;
import org.kablink.teaming.gwt.client.mainmenu.RecentPlaceInfo;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * This class holds the response data for the "get recent places" rpc command
 * @author jwootton
 *
 */
public class GetRecentPlacesRpcResponseData implements IsSerializable, VibeRpcResponseData {

    private List<RecentPlaceInfo> m_recentPlacesList;

    /**
	 * 
	 */
    public GetRecentPlacesRpcResponseData() {
    }

    /**
	 * 
	 */
    public GetRecentPlacesRpcResponseData(List<RecentPlaceInfo> recentPlacesList) {
        m_recentPlacesList = recentPlacesList;
    }

    /**
	 * 
	 */
    public List<RecentPlaceInfo> getRecentPlaces() {
        return m_recentPlacesList;
    }
}
