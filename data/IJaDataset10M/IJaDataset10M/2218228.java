package com.liferay.portal.ejb;

import java.rmi.RemoteException;

/**
 * <a href="LayoutManagerSoap.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 * @version $Revision: 1.4 $
 *
 */
public class LayoutManagerSoap {

    public static com.liferay.portal.model.LayoutModel addGroupLayout(java.lang.String groupId, java.lang.String name, java.lang.String[] portletIds) throws RemoteException {
        try {
            com.liferay.portal.model.Layout returnValue = LayoutManagerUtil.addGroupLayout(groupId, name, portletIds);
            return returnValue;
        } catch (Exception e) {
            throw new RemoteException(e.getMessage());
        }
    }

    public static com.liferay.portal.model.LayoutModel addUserLayout(java.lang.String name, java.lang.String[] portletIds) throws RemoteException {
        try {
            com.liferay.portal.model.Layout returnValue = LayoutManagerUtil.addUserLayout(name, portletIds);
            return returnValue;
        } catch (Exception e) {
            throw new RemoteException(e.getMessage());
        }
    }

    public static com.liferay.portal.model.LayoutModel getLayout(com.liferay.portal.ejb.LayoutPK pk) throws RemoteException {
        try {
            com.liferay.portal.model.Layout returnValue = LayoutManagerUtil.getLayout(pk);
            return returnValue;
        } catch (Exception e) {
            throw new RemoteException(e.getMessage());
        }
    }

    public static void setDefaultLayout(com.liferay.portal.ejb.LayoutPK pk, boolean defaultLayout) throws RemoteException {
        try {
            LayoutManagerUtil.setDefaultLayout(pk, defaultLayout);
        } catch (Exception e) {
            throw new RemoteException(e.getMessage());
        }
    }

    public static com.liferay.portal.model.LayoutModel updateLayout(com.liferay.portal.ejb.LayoutPK pk, java.lang.String name, java.lang.String[] portletIds) throws RemoteException {
        try {
            com.liferay.portal.model.Layout returnValue = LayoutManagerUtil.updateLayout(pk, name, portletIds);
            return returnValue;
        } catch (Exception e) {
            throw new RemoteException(e.getMessage());
        }
    }

    public static com.liferay.portal.model.LayoutModel updateLayout(com.liferay.portal.ejb.LayoutPK pk, java.lang.String name, java.lang.String columnOrder, java.lang.String narrow1, java.lang.String narrow2, java.lang.String wide, java.lang.String stateMax, java.lang.String stateMin, java.lang.String modeEdit, java.lang.String modeHelp) throws RemoteException {
        try {
            com.liferay.portal.model.Layout returnValue = LayoutManagerUtil.updateLayout(pk, name, columnOrder, narrow1, narrow2, wide, stateMax, stateMin, modeEdit, modeHelp);
            return returnValue;
        } catch (Exception e) {
            throw new RemoteException(e.getMessage());
        }
    }

    public static boolean hasWrite(com.liferay.portal.ejb.LayoutPK pk) throws RemoteException {
        try {
            boolean returnValue = LayoutManagerUtil.hasWrite(pk);
            return returnValue;
        } catch (Exception e) {
            throw new RemoteException(e.getMessage());
        }
    }
}
