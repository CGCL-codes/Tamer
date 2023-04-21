package com.jawise.serviceadapter.test;

import java.rmi.RemoteException;
import javax.xml.rpc.ServiceException;
import junit.framework.TestCase;
import org.apache.log4j.Logger;
import com.jawise.serviceadapter.test.svc.soap.ArrayOfCar;
import com.jawise.serviceadapter.test.svc.soap.Car;
import com.jawise.serviceadapter.test.svc.soap.CarRentalServiceALocator;
import com.jawise.serviceadapter.test.svc.soap.CarRentalServiceAPortType;
import com.jawise.serviceadapter.test.svc.soap.cars.CarRentalServiceAClient;

public class TestSoapToNvpAdapter extends TestCase {

    protected TestFixture serviceAdapterTestFixture;

    private static final Logger logger = Logger.getLogger(TestNvpToSoapAdapter.class);

    private static final String SERVICE_URL = "http://localhost:8080/serviceadapter/process/adapter?userid=raj&service=CarRentalServiceA&messageconversion=listTosearchConverter";

    protected void setUp() throws Exception {
        super.setUp();
        serviceAdapterTestFixture = new TestFixture(this);
        serviceAdapterTestFixture.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        serviceAdapterTestFixture.tearDown();
        serviceAdapterTestFixture = null;
    }

    public void testSuccesfullCarRentalSoapCall() {
        try {
            CarRentalServiceALocator loc = new CarRentalServiceALocator();
            CarRentalServiceAPortType port = loc.getCarRentalServiceAHttpPort();
            Car[] listRentalCars = port.listRentalCars("Lodnon", "01/01/2009", "05/01/2009");
            assertTrue(listRentalCars != null);
            assertTrue("Mazda".equals(listRentalCars[0].getMake()));
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    public void testSuccesfullCarRentalAdapterCall() {
        try {
            CarRentalServiceALocator loc = new CarRentalServiceALocator();
            loc.setCarRentalServiceAHttpPortEndpointAddress(SERVICE_URL);
            CarRentalServiceAPortType port = loc.getCarRentalServiceAHttpPort();
            Car[] listRentalCars = port.listRentalCars("Lodnon", "01/01/2009", "05/01/2009");
            assertTrue(listRentalCars != null);
            assertTrue("Mazda".equals(listRentalCars[0].getMake()));
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }
}
