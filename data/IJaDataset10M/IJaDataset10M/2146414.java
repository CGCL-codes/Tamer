package org.brainypdm.test.junit.datastore;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Random;
import junit.framework.JUnit4TestAdapter;
import org.brainypdm.dto.Host;
import org.brainypdm.dto.PerformanceData;
import org.brainypdm.dto.Service;
import org.brainypdm.dto.ServiceData;
import org.brainypdm.dto.ServiceStatus;
import org.brainypdm.exceptions.BaseException;
import org.brainypdm.modules.commons.utils.uom.UomAdministrator;
import org.brainypdm.modules.interfaces.DataStoreInterface;
import org.brainypdm.test.junit.BasicSuite;
import org.junit.Test;

/*******************************************************************************
 * 
 * @author Thomas Buffagni
 * 
 */
public class TestBasicServices extends BasicSuite {

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(TestBasicServices.class);
    }

    @Test
    public void ping() throws RemoteException, BaseException, NotBoundException {
        System.out.println(super.getDatastoreImpl().ping());
    }

    @Test
    public void insertPerformanceData() throws BaseException, RemoteException, NotBoundException {
        Host host = new Host();
        host.setName("test insertPerfData");
        host.setStoreable(true);
        host.setVirtual(true);
        Service service = new Service();
        service.setName("Service1");
        service.setStore(true);
        service.setStoreable(true);
        service.setVirtual(false);
        host.addService(service);
        ServiceStatus status = new ServiceStatus();
        status.setCheckDate(new Timestamp(System.currentTimeMillis()));
        status.setStatus(1);
        status.setVerificationTime(System.currentTimeMillis());
        service.addStatus(status);
        ServiceData sd = new ServiceData();
        sd.setName("test perfData");
        sd.setStoreable(true);
        sd.setVirtual(false);
        sd.setUom(UomAdministrator.getInstance().getUOM(1));
        PerformanceData pd = new PerformanceData();
        pd.setMaxValue(10);
        pd.setMinValue(0);
        pd.setValue(5);
        pd.setWarning(6);
        sd.addPerformanceData(pd);
        service.addServiceData(sd);
        boolean retValue = super.getDatastoreImpl().insertPerformanceData(host);
        System.out.println("insertPerformanceData=" + retValue);
    }

    @Test
    public void insertPerformanceDataHightLoad() throws BaseException, RemoteException, NotBoundException {
        DataStoreInterface ds = super.getDatastoreImpl();
        Random random = new Random();
        final long delay = 30000;
        final long cycles = 10000;
        long checkDate = System.currentTimeMillis() - delay * cycles;
        for (int i = 0; i < cycles; i++) {
            Host host = new Host();
            host.setName(String.valueOf(random.nextInt(1)));
            host.setStoreable(true);
            host.setVirtual(true);
            Service service = new Service();
            service.setName(host.getName());
            service.setStore(true);
            service.setStoreable(true);
            service.setVirtual(false);
            host.addService(service);
            ServiceStatus status = new ServiceStatus();
            checkDate = checkDate + delay;
            status.setCheckDate(new Timestamp(checkDate));
            status.setStatus(1);
            status.setVerificationTime(checkDate);
            service.addStatus(status);
            ServiceData sd = new ServiceData();
            sd.setName("test perfData");
            sd.setStoreable(true);
            sd.setVirtual(false);
            sd.setUom(UomAdministrator.getInstance().getUOM(1));
            PerformanceData pd = new PerformanceData();
            pd.setMaxValue(10);
            pd.setMinValue(0);
            pd.setValue(random.nextInt(40));
            pd.setWarning(6);
            sd.addPerformanceData(pd);
            service.addServiceData(sd);
            try {
                ds.insertPerformanceData(host);
            } catch (Exception e) {
            }
            if (i % 1000 == 0) {
                System.out.println("i=" + i);
            }
        }
    }
}
