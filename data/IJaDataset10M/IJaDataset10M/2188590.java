package com.telstra.ess.test.junit;

import java.util.*;
import javax.naming.ConfigurationException;
import com.telstra.ess.*;
import com.telstra.ess.alarming.*;
import com.telstra.ess.alarming.event.*;
import com.telstra.ess.environment.*;
import com.telstra.ess.event.*;
import java.io.*;
import junit.framework.*;

/**
 * @author c957258
 */
public class EssAlarmInterfaceTestSuite extends TestCase {

    private class DummyEssEventListener implements EssEventListener {

        private boolean result = false;

        public DummyEssEventListener() {
            result = false;
        }

        public void processEssEvent(EssEvent ev) {
            result = true;
        }

        public boolean getResult() {
            return result;
        }

        public void reset() {
            result = false;
        }
    }

    public static void main(String[] args) {
        TestSuite suite = new TestSuite();
        suite.addTest(new EssAlarmInterfaceTestSuite("testRaiseAlarmsAndClear"));
        junit.textui.TestRunner.run(suite);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Constructor for EssReportingTestSuite.
     * @param arg0
     */
    public EssAlarmInterfaceTestSuite(String arg0) {
        super(arg0);
    }

    /**
     * Method to be used by JUnit to verify that an EssFactoryException is thrown when a
     * user tries to retrieve a ReportingEventManager instance with a null component.
     * Expected outcome: an EssFactoryException is thrown
     */
    public void testGetInstanceNullComponent() {
        AlarmManager am = null;
        EssComponent comp = null;
        try {
            am = AlarmManager.getInstance(comp);
            fail("Should not have retrieved an AlarmManager instance with a null component");
        } catch (EssManagerException efe) {
            assertTrue(true);
        } catch (Exception e) {
            fail("Unexpected exception " + e.getMessage());
        }
    }

    /**
     * Method to be used by JUnit to verify that an EssFactoryException is thrown when a
     * user tries to retrieve a ReportingEventManager instance with a null component name.
     * Expected outcome: an EssFactoryException is thrown     
     */
    public void testGetInstanceNullComponentName() {
        AlarmManager rem = null;
        EssComponent comp = null;
        try {
            comp = new EssComponent(null, "Some description");
        } catch (Exception e) {
            assertTrue(true);
        }
        try {
            rem = AlarmManager.getInstance(comp);
            assertNotNull(rem);
            fail("Should not have retrieved an AlarmManager instance with a null component name");
        } catch (EssManagerException efe) {
            assertTrue(true);
        } catch (Exception e) {
            fail("Unexpected exception " + e.getMessage());
        }
    }

    public void testLeaseExpiryBeforeExpiry() {
        EssComponent leaseTestComp = new EssComponent("EssAlarmingTestSuite");
        String leaseLengthStr = null;
        long leaseLength = 0;
        try {
            leaseLengthStr = ConfigurationContext.getConfigurationItem(leaseTestComp, "service.leaselength", null);
            if (leaseLengthStr != null) {
                leaseLength = Long.valueOf(leaseLengthStr).longValue() * 1000;
            } else {
                fail("Lease length not defined");
            }
        } catch (ConfigurationException e) {
            fail("Exception thrown while trying to determine lease details: " + e.getMessage());
        } catch (Exception e) {
            fail("Exception thrown while trying to determine lease details: " + e.getMessage());
        }
        double leaseFraction = new Long(leaseLength).doubleValue() / 10.0;
        long reRequestInterval = leaseLength - new Double(leaseFraction).longValue();
        AlarmManager am1 = null, am2 = null;
        EssComponent comp = new EssComponent("EssAlarmingTestSuite");
        try {
            am1 = AlarmManager.getInstance(comp);
            Thread.sleep(reRequestInterval);
            am2 = AlarmManager.getInstance(comp);
            assertSame("New alarm manager issued, supposed to be the same", am1, am2);
            return;
        } catch (EssManagerException efe) {
            fail("Could not get reporting event manager " + efe.getMessage());
        } catch (InterruptedException ie) {
            fail("InterruptedException caught: " + ie);
        }
    }

    /**
    * Method to be used by JUnit to test that the lease on the reporting event manager 
    * expires between calls to ReportingEventManager.getInstance if the calls violate the lease
    * time
    * Expected outcome: ReportingEventManager is a new instance (not the same as the initial request)    
    */
    public void testLeaseExpiryAfterExpiry() {
        EssComponent leaseTestComp = new EssComponent("EssAlarmingTestSuite");
        String leaseLengthStr = null;
        long leaseLength = 0;
        try {
            leaseLengthStr = ConfigurationContext.getConfigurationItem(leaseTestComp, "service.leaselength", null);
            if (leaseLengthStr != null) {
                leaseLength = Long.valueOf(leaseLengthStr).longValue() * 1000;
            } else {
                fail("Lease length not defined");
            }
        } catch (ConfigurationException e) {
            fail("Exception thrown while trying to determine lease details: " + e.getMessage());
        } catch (Exception e) {
            fail("Exception thrown while trying to determine lease details: " + e.getMessage());
        }
        double leaseFraction = new Long(leaseLength).doubleValue() / 10.0;
        long reRequestInterval = leaseLength + new Double(leaseFraction).longValue();
        AlarmManager am1 = null, am2 = null;
        try {
            am1 = AlarmManager.getInstance(leaseTestComp);
            Thread.sleep(reRequestInterval);
            am2 = AlarmManager.getInstance(leaseTestComp);
            assertNotSame("The same reporting event manager issued, supposed to be different", am1, am2);
            return;
        } catch (EssManagerException efe) {
            fail("Could not get reporting event manager " + efe.getMessage());
        } catch (InterruptedException ie) {
            fail("InterruptedException caught: " + ie);
        }
    }

    /**
     * Method to be used by JUnit to test that when the ReportingEventManager is used
     * to issue a reporting event, the event is detected by a listener
     * Expected outcome: a registered listener detects the reporting event
     */
    public void testAlarmEvent() {
        AlarmManager am = null;
        EssComponent comp = new EssComponent("EssAlarmingTestSuite");
        try {
            am = AlarmManager.getInstance(comp);
            Alarm a = new Alarm(1, Alarm.FATAL, "Fatal Alarm 1");
            DummyEssEventListener dummy = new DummyEssEventListener();
            am.getDispatcher().addEssEventListener(dummy);
            am.raise(a);
            boolean raised = dummy.getResult();
            assertTrue(raised);
        } catch (EssManagerException efe) {
            fail("EssFactoryException thrown from getInstance: " + efe.getMessage());
        } catch (Exception e) {
            fail("Unexpected exception");
        }
    }

    /**
     * Method used by JUnit to test that if multiple event listeners are registered
     * with the ReportingEventManager that all of them receive the reporting event
     * and act on them.
     * Expected outcome: All registered listeners report that they have detected
     * the event
     *
     */
    public void testReportEventMultipleListeners() {
        AlarmManager am = null;
        EssComponent comp = new EssComponent("EssAlarmingTestSuite");
        try {
            am = AlarmManager.getInstance(comp);
            Alarm a = new Alarm(1, Alarm.FATAL, "Fatal Alarm 1");
            DummyEssEventListener dummy1 = new DummyEssEventListener();
            DummyEssEventListener dummy2 = new DummyEssEventListener();
            ArrayList al = new ArrayList();
            al.add(dummy1);
            al.add(dummy2);
            if (al.isEmpty()) {
                fail("No listeners to watch - list was empty");
            }
            am.getDispatcher().addEssEventListener(dummy1);
            am.getDispatcher().addEssEventListener(dummy2);
            am.raise(a);
            Iterator i = al.iterator();
            while (i.hasNext()) {
                DummyEssEventListener d = (DummyEssEventListener) i.next();
                if (d.getResult() != true) {
                    fail("Reporting event not seen by a listener");
                }
            }
            assertTrue(true);
        } catch (EssManagerException efe) {
            fail("EssFactoryException thrown from getInstance: " + efe.getMessage());
        } catch (Exception e) {
            fail("Unexpected exception");
        }
    }

    public void testCustomerComponent() {
        AlarmManager am1 = null, am2 = null;
        EssComponent comp1 = new EssComponent("EssAlarmingTestSuite", "Some Desc", "Greg");
        EssComponent comp2 = new EssComponent("EssAlarmingTestSuite", "Other Desc", "Greg");
        try {
            am1 = AlarmManager.getInstance(comp1);
            am2 = AlarmManager.getInstance(comp2);
            assertSame("Alarm managers not the same for the same customer", am1, am2);
        } catch (EssManagerException efe) {
            fail("EssFactoryException thrown from getInstance: " + efe.getMessage());
        } catch (Exception e) {
            fail("Unexpected exception");
        }
    }

    public void testRaiseAlarmsAndClear() {
        try {
            EssComponent component = new EssComponent("EssAlarmingTestSuite", null, "CustomerXYZ");
            Alarm a1 = new Alarm(1, Alarm.FATAL, "This is a fatal alarm");
            Alarm a2 = new Alarm(2, Alarm.CRITICAL, "This is a critical alarm");
            Alarm a3 = new Alarm(3, Alarm.WARNING, "This is a warning alarm");
            AlarmManager.getInstance(component).raise(a1);
            Thread.sleep(7000);
            AlarmManager.getInstance(component).raise(a1);
            Thread.sleep(7000);
            AlarmManager.getInstance(component).raise(a1);
            Thread.sleep(7000);
            AlarmManager.getInstance(component).raise(a2);
            Thread.sleep(7000);
            AlarmManager.getInstance(component).raise(a2);
            Thread.sleep(7000);
            AlarmManager.getInstance(component).raise(a3);
            Thread.sleep(7000);
            AlarmManager.getInstance(component).clear(a1);
            Thread.sleep(7000);
            AlarmManager.getInstance(component).clear(a2);
            Thread.sleep(7000);
            AlarmManager.getInstance(component).raise(a3);
            Thread.sleep(7000);
            AlarmManager.getInstance(component).clear(a3);
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
}
