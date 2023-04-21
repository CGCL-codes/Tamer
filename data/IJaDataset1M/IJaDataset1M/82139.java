package org.kablink.teaming.gwt.client.rpc.shared;

import java.util.ArrayList;
import org.kablink.teaming.gwt.client.admin.GwtAdminCategory;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * This class holds the response data for the "Get Admin Actions" rpc command
 * @author jwootton
 *
 */
public class AdminActionsRpcResponseData implements IsSerializable, VibeRpcResponseData {

    private ArrayList<GwtAdminCategory> m_adminActions;

    /**
	 * 
	 */
    public AdminActionsRpcResponseData() {
    }

    /**
	 * 
	 */
    public AdminActionsRpcResponseData(ArrayList<GwtAdminCategory> adminActions) {
        m_adminActions = adminActions;
    }

    /**
	 * 
	 */
    public ArrayList<GwtAdminCategory> getAdminActions() {
        return m_adminActions;
    }
}
