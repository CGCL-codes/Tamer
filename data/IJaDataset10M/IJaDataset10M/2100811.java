package fr.esrf.TangoApi;

import fr.esrf.webapi.IDAOImplUtil;

public class DAOImplUtil implements IDAOImplUtil {

    public Object getDAOImpl(Object result) {
        if (result instanceof DeviceAttribute) {
            return ((DeviceAttribute) result).getDeviceattributeDAO();
        }
        if (result instanceof DeviceData) {
            return ((DeviceData) result).getDevicedataDAO();
        }
        if (result instanceof DeviceDataHistory) {
            return ((DeviceDataHistory) result).getDeviceedatahistoryDAO();
        }
        if (result instanceof DeviceProxy) {
            return ((DeviceProxy) result).getDeviceProxy();
        }
        if (result instanceof Database) {
            return ((Database) result).getDatabaseDAO();
        }
        if (result instanceof ApiUtil) {
            return ((ApiUtil) result).getApiUtilDAO();
        }
        if (result instanceof Connection) {
            return ((Connection) result).getIConnection();
        }
        return result;
    }
}
