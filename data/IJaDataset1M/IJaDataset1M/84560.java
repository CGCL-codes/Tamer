package com.google.code.atpfx;

import com.google.code.atpfx.data.Pair;
import com.google.code.atpfx.data.Pairs;
import com.google.code.db.HibernateUtil;
import junit.framework.TestCase;
import org.hibernate.*;

public class MyBasicTests extends TestCase {

    public void testConnectionToMarket() {
        AccountLoginDetails accountLoginDetails = new AccountLoginDetails();
        boolean hasConnected = false;
        IConnection myFXCMConnection = new FXCMConnection(accountLoginDetails.getUserName(), accountLoginDetails.getPassword(), accountLoginDetails.getAccountType());
        assertFalse("The connection has not yet been initiated", hasConnected);
        myFXCMConnection.open();
        hasConnected = myFXCMConnection.isOpened();
        myFXCMConnection.close();
        assertTrue("The connection could not be established", hasConnected);
    }

    public void testGetEurUsdDataFromMarket() {
        FXCMConnection myFXCMConnection = new FXCMConnection();
        myFXCMConnection.open();
        secondsToWait(20);
        myFXCMConnection.close();
    }

    public void testConnectionToDB() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        for (Pair p : Pairs.getPairs()) {
            System.out.println(p.getPairId() + " == " + p.getPairName());
        }
    }

    public void testSaveTick() {
        FXCMConnection myFXCMConnection = null;
        Session session = null;
        try {
            myFXCMConnection = new FXCMConnection();
            myFXCMConnection.open();
            for (int counter = 0; counter < 10; counter++) {
                secondsToWait(1);
                while (myFXCMConnection.getGenericMessagesReceived().size() > 0) {
                }
            }
        } finally {
            if (null != myFXCMConnection) {
                if (myFXCMConnection.isOpened()) {
                    myFXCMConnection.close();
                }
            }
        }
    }

    public void testFinalize() {
        HibernateUtil.getSessionFactory().close();
    }

    public void secondsToWait(int i) {
        try {
            Thread.sleep(1000 * i);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
}
