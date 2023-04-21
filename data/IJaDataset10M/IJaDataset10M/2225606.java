package fr.esrf.TangoApi.factory;

import fr.esrf.Tango.factory.ITangoFactory;
import fr.esrf.TangoApi.*;

public class DefaultTangoFactoryImpl implements ITangoFactory {

    public DefaultTangoFactoryImpl() {
    }

    public IConnectionDAO getConnectionDAO() {
        return new ConnectionDAODefaultImpl();
    }

    public IDeviceProxyDAO getDeviceProxyDAO() {
        return new DeviceProxyDAODefaultImpl();
    }

    public ITacoTangoDeviceDAO getTacoTangoDeviceDAO() {
        return new TacoTangoDeviceDAODefaultImpl();
    }

    public IDatabaseDAO getDatabaseDAO() {
        try {
            return new DatabaseDAODefaultImpl();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public IDeviceAttributeDAO getDeviceAttributeDAO() {
        return new DeviceAttributeDAODefaultImpl();
    }

    public IDeviceAttribute_3DAO getDeviceAttribute_3DAO() {
        return new DeviceAttribute_3DAODefaultImpl();
    }

    public IDeviceDataDAO getDeviceDataDAO() {
        return new DeviceDataDAODefaultImpl();
    }

    public IDeviceDataHistoryDAO getDeviceDataHistoryDAO() {
        return new DeviceDataHistoryDAODefaultImpl();
    }

    public IApiUtilDAO getApiUtilDAO() {
        return new ApiUtilDAODefaultImpl();
    }

    public IIORDumpDAO getIORDumpDAO() {
        return new IORDumpDAODefaultImpl();
    }

    public String getFactoryName() {
        return "TANGORB Default";
    }
}
