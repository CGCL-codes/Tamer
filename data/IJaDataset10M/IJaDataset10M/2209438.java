package org.dbe.demos.bookingservice.test;

import java.math.BigInteger;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.holders.BooleanHolder;
import javax.xml.rpc.holders.StringHolder;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.log4j.Logger;
import org.dbe.servent.ServentInfo;
import org.dbe.toolkit.pa.ProtocolAdapterException;
import org.dbe.toolkit.proxyframework.Workspace;
import org.sun.dbe.ClientHelper;

public class Testing {

    private static final Logger logger = Logger.getLogger(Testing.class.getName());

    private static int TEST_NUMBER = 1;

    private static int NUM_THREADS = 1;

    public static void main(String[] args) {
        try {
            Testing client = new Testing();
            System.out.println("Starting testing...");
            String result = client.test();
            System.out.println("result=" + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class Invoke extends Thread {

        private int called = 0, failed = 0, success = 0;

        private final Logger logger = Logger.getLogger(Invoke.class.getName());

        private Object[] dataObjects;

        private Class[] classObjects;

        private Boolean confirmation;

        private String name, hotelReservationNumber, flightReservationNumber, flightNumber, failureReason;

        private Random rnd;

        private int number = 1;

        private Workspace workspace;

        public Invoke(String name, Object[] dataObjects, Class[] classObjects, Workspace workspace) {
            super(name);
            this.name = name;
            this.dataObjects = dataObjects;
            this.classObjects = classObjects;
            this.workspace = workspace;
        }

        public void run() {
            for (int i = 0; i < TEST_NUMBER; i++) {
                try {
                    rnd = new Random(new Date().getTime() + number * 1000);
                    sleep(rnd.nextInt(NUM_THREADS) * 1000);
                    hotelReservationNumber = "";
                    flightReservationNumber = "";
                    flightNumber = "";
                    failureReason = "";
                    confirmation = new Boolean(false);
                    executeCall(i);
                } catch (InterruptedException e) {
                    logger.error(name + ": " + e);
                }
            }
            logger.info(name + ": called=" + called + " : success=" + success + " : failed=" + failed);
        }

        private void executeCall(int number) {
            try {
                logger.info(name + ": " + number + ". invoke()..");
                called++;
                Object result = workspace.invoke("makeMultiBooking", classObjects, dataObjects);
                if (result != null) {
                    confirmation = (Boolean) result;
                    if (confirmation.booleanValue()) {
                        success++;
                    } else {
                        failed++;
                    }
                } else {
                    confirmation = new Boolean(false);
                    failed++;
                }
                logger.info(name + ": invoke() returning: " + confirmation.booleanValue());
            } catch (ProtocolAdapterException e) {
            } catch (RemoteException e) {
                failed++;
                logger.error(name + ": have a failure: " + e);
            }
        }
    }

    public String test() throws Exception {
        Object[] dataObjects = setupObjects();
        Class[] classObjects = setupClasses();
        ClientHelper css = new ClientHelper(ServentInfo.getInstance().getPrivateURL());
        String[] entries = { "lotteBookingService-11" };
        List workspaces = (List) css.getProxies(null, entries);
        if (workspaces == null) {
            logger.error("Service Proxy is null");
            throw new Exception("Service Proxy is null");
        } else if (workspaces.size() < 1) {
            logger.error("have no entries for Service Proxies");
            throw new Exception("have no entries for Service Proxies");
        }
        Workspace workspace = (Workspace) workspaces.get(0);
        Invoke testThreads = new Invoke("Client1-", dataObjects, classObjects, workspace);
        testThreads.start();
        testThreads.join();
        return "finished testing...";
    }

    protected Class[] setupClasses() {
        Class classes[] = { java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.math.BigInteger.class, java.math.BigInteger.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, javax.xml.rpc.holders.BooleanHolder.class, javax.xml.rpc.holders.StringHolder.class, javax.xml.rpc.holders.StringHolder.class, javax.xml.rpc.holders.StringHolder.class, javax.xml.rpc.holders.StringHolder.class };
        return classes;
    }

    protected Object[] setupObjects() {
        String origin = "Dublin";
        String target = "London";
        String startDate = "01/08/2006";
        String endDate = "15/08/2006";
        String hotelRoomType = "1";
        BigInteger hotelNumOfAdults = new BigInteger("2");
        BigInteger hotelNumOfChild = new BigInteger("0");
        String referencePerson = "Lotte Nickel";
        String receiptType = "email";
        String sendFrom = "nickell@cs.tcd.ie";
        String emailAddress = "lotte.nickel@gmail.com";
        String emailSubject = "Automated Booking Service: Your Reference Numbers!";
        StringHolder hotelReservation = new StringHolder("hotel reservation number here");
        StringHolder flightReservation = new StringHolder("flight reservation number here");
        StringHolder flightNumber = new StringHolder("flight number here");
        StringHolder failureReason = new StringHolder("any failures");
        BooleanHolder response = new BooleanHolder();
        Object[] returnObject = new Object[] { origin, target, startDate, endDate, hotelRoomType, hotelNumOfAdults, hotelNumOfChild, referencePerson, receiptType, sendFrom, emailAddress, emailSubject, response, hotelReservation, flightReservation, flightNumber, failureReason };
        return returnObject;
    }

    protected Call createCall() throws Exception {
        logger.debug("createCall()");
        Service service = new Service();
        Call call = (Call) service.createCall();
        String urlString = "http://localhost:2728/active-bpel/services/Booking";
        call.setTargetEndpointAddress(new java.net.URL(urlString));
        call.setOperationName("makeMultiBooking");
        call.addParameter("origin", org.apache.axis.Constants.XSD_STRING, ParameterMode.IN);
        call.addParameter("target", org.apache.axis.Constants.XSD_STRING, ParameterMode.IN);
        call.addParameter("startDate", org.apache.axis.Constants.XSD_STRING, ParameterMode.IN);
        call.addParameter("endDate", org.apache.axis.Constants.XSD_STRING, ParameterMode.IN);
        call.addParameter("hotelRoomType", org.apache.axis.Constants.XSD_STRING, ParameterMode.IN);
        call.addParameter("hotelNumOfAdults", org.apache.axis.Constants.XSD_INTEGER, ParameterMode.IN);
        call.addParameter("hotelNumOfChild", org.apache.axis.Constants.XSD_INTEGER, ParameterMode.IN);
        call.addParameter("referencePerson", org.apache.axis.Constants.XSD_STRING, ParameterMode.IN);
        call.addParameter("receiptType", org.apache.axis.Constants.XSD_STRING, ParameterMode.IN);
        call.addParameter("sendFrom", org.apache.axis.Constants.XSD_STRING, ParameterMode.IN);
        call.addParameter("emailAddress", org.apache.axis.Constants.XSD_STRING, ParameterMode.IN);
        call.addParameter("emailSubject", org.apache.axis.Constants.XSD_STRING, ParameterMode.IN);
        call.addParameter("hotelReservationNumber", org.apache.axis.Constants.XSD_STRING, ParameterMode.OUT);
        call.addParameter("flightReservationNumber", org.apache.axis.Constants.XSD_STRING, ParameterMode.OUT);
        call.addParameter("flightNumber", org.apache.axis.Constants.XSD_STRING, ParameterMode.OUT);
        call.addParameter("failureReason", org.apache.axis.Constants.XSD_STRING, ParameterMode.OUT);
        call.setReturnType(org.apache.axis.Constants.XSD_BOOLEAN);
        return call;
    }
}
