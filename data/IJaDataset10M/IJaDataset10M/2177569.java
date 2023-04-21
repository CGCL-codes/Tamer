package org.redwood.business.usermanagement.wswsg;

import java.rmi.RemoteException;
import java.util.Collection;
import org.redwood.business.usermanagement.website.*;
import org.redwood.business.usermanagement.websitegroup.*;

public interface WsWsg {

    public String getRw_id() throws RemoteException;

    public String getRw_webSiteID() throws RemoteException;

    public String getRw_webSiteGroupID() throws RemoteException;

    public WebSite getWebSite() throws RemoteException;

    public WebSiteGroup getWebSiteGroup() throws RemoteException;
}
