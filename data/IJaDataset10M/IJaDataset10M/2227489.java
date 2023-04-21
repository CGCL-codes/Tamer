package org.epo.jbpa.rmi.test;

import java.util.Properties;
import java.rmi.server.UID;
import org.apache.log4j.Logger;
import junit.framework.*;
import junit.textui.TestRunner;
import org.epo.utils.ConfigurationException;
import org.epoline.jsf.client.ServiceProvider;
import org.epoline.service.support.ServiceSupport;
import org.epoline.service.support.PropertyException;
import org.epo.jbpa.dl.JBpaServiceProxyInterface;

/**
 * @author INFOTEL Conseil
 * Junit class test for a Jini service
 */
public class JBpaServiceTest extends TestCase {

    public static int result = -1;

    private static Properties props;

    private static String className = "";

    static {
        try {
            className = JBpaTester.class.getName();
            props = ServiceSupport.getProperties(className);
            ServiceProvider.setContext(props);
        } catch (ConfigurationException cfgE) {
            System.exit(-1);
        } catch (PropertyException prpE) {
            System.exit(-1);
        } catch (Exception e) {
            System.exit(-1);
        }
    }

    /**
	 * Constructor for JBpaServiceTest.
	 * @param arg0
	 */
    public JBpaServiceTest() {
        super();
    }

    /**
	 * Constructor for JBpaServiceTest.
	 * @param arg0
	 * @deprecated
	 */
    public JBpaServiceTest(String arg0) {
        super(arg0);
    }

    public static void main(String[] args) {
        System.exit(JBpaServiceTest.result);
    }

    public void testJBpa() {
        try {
            JBpaServiceProxyInterface myProxy = (JBpaServiceProxyInterface) ServiceProvider.getInstance().getService(JBpaServiceProxyInterface.NAME, null);
            String myId = Thread.currentThread().getName();
            JBpaTester myTester = new JBpaTester();
            myTester.loadParameters(props);
            UID myUID = null;
            try {
                myUID = myProxy.openJBpaSession(myTester.getUserName(), myTester.getUserPwd());
            } catch (Exception e) {
            }
            String myReqNum = myTester.buildRequest(myProxy, myUID);
            byte[] appData = myTester.readFile(myTester.getAppFileName());
            myTester.managePermAppDoc(myProxy, appData, myId);
            myTester.manageTempAppDoc(myProxy, myUID, appData, myId, myReqNum);
            myTester.manageRequest(myProxy, myUID, myReqNum);
            myTester.manageRequestList(myProxy);
            myProxy.closeJBpaSession(myUID);
        } catch (Exception e) {
        }
        result = 0;
    }
}
