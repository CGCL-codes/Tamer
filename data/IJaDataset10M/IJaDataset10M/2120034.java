package test.openjmx.adaptor.http;

import junit.framework.TestCase;
import java.io.Reader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.IOException;
import java.net.URLConnection;
import java.net.URL;
import java.net.MalformedURLException;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import javax.management.NotificationBroadcaster;
import javax.management.NotificationBroadcasterSupport;
import javax.management.MBeanNotificationInfo;
import javax.management.Attribute;
import org.custommonkey.xmlunit.XMLTestCase;

/**
 * Test of HttpAdapter XML results
 */
public class HttpAdaptorXMLTest extends XMLTestCase {

    protected MBeanServer server;

    protected ObjectName httpName, test1Name, test2Name;

    protected String host = "localhost";

    protected int port = 8080;

    TestClass test1 = new TestClass("t1");

    TestClass test2 = new TestClass("t1");

    /**
	 * Construct the test case
	 */
    public HttpAdaptorXMLTest(String name) {
        super(name);
    }

    public static interface TestClassMBean {

        public String getStr();

        public Double getDouble();

        public boolean isTrue();

        public void setStr(String str);

        public Boolean aMethod(String string);

        public void anotherMethod(String string, int test);
    }

    public static class TestClass extends NotificationBroadcasterSupport implements TestClassMBean {

        private String str;

        public TestClass(String str) {
            this.str = str;
        }

        public String getStr() {
            return str;
        }

        public void setStr(String str) {
            this.str = str;
        }

        public Double getDouble() {
            return new Double(0);
        }

        public boolean isTrue() {
            return true;
        }

        public Boolean aMethod(String string) {
            return new Boolean(string.equals("true"));
        }

        public void anotherMethod(String string, int test) {
            this.str = string;
        }

        public MBeanNotificationInfo[] getNotificationInfo() {
            MBeanNotificationInfo[] notifications = new MBeanNotificationInfo[1];
            notifications[0] = new MBeanNotificationInfo(new String[] { "test1", "test2" }, "name", "test");
            return notifications;
        }
    }

    public void setUp() {
        try {
            server = MBeanServerFactory.createMBeanServer("Http");
            httpName = new ObjectName("Http:name=HttpAdaptor");
            test1Name = new ObjectName("Test:name=test1");
            test2Name = new ObjectName("Test:name=test2");
            server.createMBean("openjmx.adaptor.http.HttpAdaptor", httpName, null);
            String hostProperty = System.getProperty("test.http.host");
            if (hostProperty != null) {
                host = hostProperty;
            }
            String portProperty = System.getProperty("test.http.port");
            if (portProperty != null) {
                port = Integer.parseInt(portProperty);
            }
            server.setAttribute(httpName, new Attribute("Host", host));
            server.setAttribute(httpName, new Attribute("Port", new Integer(port)));
            server.registerMBean(test1, test1Name);
            server.registerMBean(test2, test2Name);
            server.invoke(httpName, "start", null, null);
            setIgnoreWhitespace(true);
            setControlParser("org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");
            setTestParser("org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void tearDown() {
        try {
            server.unregisterMBean(httpName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * Test the mbeans request
	 */
    public void testServer() throws Exception {
        Reader stream = getReader(host, port, "server");
        String controlMBean = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<Server>" + "<MBean name=\"JMImplementation:type=MBeanServerDelegate\"/>" + "<MBean name=\"Http:name=HttpAdaptor\"/>" + "<MBean name=\"Test:name=test2\"/>" + "<MBean name=\"Test:name=test1\"/>" + "</Server>";
        assertXMLEqual(new StringReader(controlMBean), stream);
        stream.close();
    }

    /**
	 * Test the mbeans request
	 */
    public void testServerAndFilters() throws Exception {
        Reader stream = getReader(host, port, "server?instanceof=test.openjmx.adaptor.http.HttpAdaptorXMLTest$TestClass");
        String controlMBean = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<Server>" + "<MBean name=\"Test:name=test2\"/>" + "<MBean name=\"Test:name=test1\"/>" + "</Server>";
        assertXMLEqual(new StringReader(controlMBean), stream);
        stream.close();
    }

    /**
	 * Test the mbeans delete
	 */
    public void testDelete() throws Exception {
        try {
            Reader stream = getReader(host, port, "delete?objectname=Test:name=test1");
            String controlMBean = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<MBeanOperation>" + "<Operation objectname=\"Test:name=test1\" operation=\"delete\" result=\"success\"/>" + "</MBeanOperation>";
            assertXMLEqual(new StringReader(controlMBean), stream);
            assertTrue(!server.isRegistered(test1Name));
            stream.close();
            stream = getReader(host, port, "delete?objectname=Test:name=test5");
            controlMBean = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<MBeanOperation>" + "<Operation errorMsg=\"MBean Test:name=test5 not registered\" objectname=\"Test:name=test5\" operation=\"delete\" result=\"error\"/>" + "</MBeanOperation>";
            assertXMLEqual(new StringReader(controlMBean), stream);
            stream.close();
        } finally {
            server.registerMBean(test1, test1Name);
        }
    }

    /**
	 * Test the operations invoke
	 */
    public void testInvoke() throws Exception {
        Reader stream = getReader(host, port, "invoke?objectname=Test:name=test1&operation=aMethod&type0=java.lang.String&value0=true");
        String controlMBean = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<MBeanOperation>" + "<Operation objectname=\"Test:name=test1\" operation=\"invoke\" result=\"success\" return=\"true\"/>" + "</MBeanOperation>";
        assertXMLEqual(new StringReader(controlMBean), stream);
        stream.close();
        stream = getReader(host, port, "invoke?objectname=Test:name=test1&operation=aMethod&type0=java.lang.String&value0=test");
        controlMBean = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<MBeanOperation>" + "<Operation objectname=\"Test:name=test1\" operation=\"invoke\" result=\"success\" return=\"false\"/>" + "</MBeanOperation>";
        assertXMLEqual(new StringReader(controlMBean), stream);
        stream.close();
    }

    /**
	 * Test the set attribute request
	 */
    public void testSetAttribute() throws Exception {
        try {
            Reader stream = getReader(host, port, "setattribute?objectname=Test:name=test1&attribute=Str&value=t2");
            String controlMBean = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<MBeanOperation>" + "<Operation objectname=\"Test:name=test1\" operation=\"setattribute\" result=\"success\"/>" + "</MBeanOperation>";
            assertXMLEqual(new StringReader(controlMBean), stream);
            assertEquals("t2", server.getAttribute(test1Name, "Str"));
            server.setAttribute(test1Name, new Attribute("Str", "t1"));
            stream.close();
            stream = getReader(host, port, "setattribute?attribute=Str&value=t2");
            controlMBean = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<MBeanOperation>" + "<Operation errorMsg=\"Incorrect parameters in the request\" operation=\"setattribute\" result=\"error\"/>" + "</MBeanOperation>";
            assertXMLEqual(new StringReader(controlMBean), stream);
            assertEquals("t1", server.getAttribute(test1Name, "Str"));
            stream = getReader(host, port, "setattribute?objectname=3&attribute=Str&value=t2");
            controlMBean = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<MBeanOperation>" + "<Operation errorMsg=\"Malformed object name\" objectname=\"3\" operation=\"setattribute\" result=\"error\"/>" + "</MBeanOperation>";
            assertXMLEqual(new StringReader(controlMBean), stream);
            assertEquals("t1", server.getAttribute(test1Name, "Str"));
            stream.close();
            stream = getReader(host, port, "setattribute?objectname=Test:name=test1&attribute=Number&value=t2");
            controlMBean = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<MBeanOperation>" + "<Operation errorMsg=\"Attribute Number not found\" objectname=\"Test:name=test1\" operation=\"setattribute\" result=\"error\"/>" + "</MBeanOperation>";
            assertXMLEqual(new StringReader(controlMBean), stream);
            assertEquals("t1", server.getAttribute(test1Name, "Str"));
            stream.close();
        } finally {
            server.setAttribute(test1Name, new Attribute("Str", "t1"));
        }
    }

    public void testServerByDomain() throws Exception {
        Reader stream = getReader(host, port, "serverbydomain");
        String controlMBean = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<Server>" + "<Domain name=\"Http\">" + "<MBean classname=\"openjmx.adaptor.http.HttpAdaptor\" description=\"HttpAdaptor MBean\" objectname=\"Http:name=HttpAdaptor\"/>" + "</Domain>" + "<Domain name=\"JMImplementation\">" + "<MBean classname=\"javax.management.MBeanServerDelegate\" description=\"\" objectname=\"JMImplementation:type=MBeanServerDelegate\"/>" + "</Domain>" + "<Domain name=\"Test\">" + "<MBean classname=\"test.openjmx.adaptor.http.HttpAdaptorXMLTest$TestClass\" description=\"\" objectname=\"Test:name=test2\"/>" + "<MBean classname=\"test.openjmx.adaptor.http.HttpAdaptorXMLTest$TestClass\" description=\"\" objectname=\"Test:name=test1\"/>" + "</Domain>" + "</Server>";
        assertXMLEqual(new StringReader(controlMBean), stream);
    }

    public void testServerByDomainAndFilters() throws Exception {
        Reader stream = getReader(host, port, "serverbydomain?instanceof=test.openjmx.adaptor.http.HttpAdaptorXMLTest$TestClass");
        String controlMBean = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<Server>" + "<Domain name=\"Http\"/>" + "<Domain name=\"JMImplementation\"/>" + "<Domain name=\"Test\">" + "<MBean classname=\"test.openjmx.adaptor.http.HttpAdaptorXMLTest$TestClass\" description=\"\" objectname=\"Test:name=test2\"/>" + "<MBean classname=\"test.openjmx.adaptor.http.HttpAdaptorXMLTest$TestClass\" description=\"\" objectname=\"Test:name=test1\"/>" + "</Domain>" + "</Server>";
        assertXMLEqual(new StringReader(controlMBean), stream);
        stream = getReader(host, port, "serverbydomain?querynames=*:*");
        controlMBean = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<Server>" + "<Domain name=\"Http\">" + "<MBean classname=\"openjmx.adaptor.http.HttpAdaptor\" description=\"HttpAdaptor MBean\" objectname=\"Http:name=HttpAdaptor\"/>" + "</Domain>" + "<Domain name=\"JMImplementation\">" + "<MBean classname=\"javax.management.MBeanServerDelegate\" description=\"\" objectname=\"JMImplementation:type=MBeanServerDelegate\"/>" + "</Domain>" + "<Domain name=\"Test\">" + "<MBean classname=\"test.openjmx.adaptor.http.HttpAdaptorXMLTest$TestClass\" description=\"\" objectname=\"Test:name=test2\"/>" + "<MBean classname=\"test.openjmx.adaptor.http.HttpAdaptorXMLTest$TestClass\" description=\"\" objectname=\"Test:name=test1\"/>" + "</Domain>" + "</Server>";
        stream = getReader(host, port, "serverbydomain?querynames=Test:*");
        controlMBean = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<Server>" + "<Domain name=\"Test\">" + "<MBean classname=\"test.openjmx.adaptor.http.HttpAdaptorXMLTest$TestClass\" description=\"\" objectname=\"Test:name=test2\"/>" + "<MBean classname=\"test.openjmx.adaptor.http.HttpAdaptorXMLTest$TestClass\" description=\"\" objectname=\"Test:name=test1\"/>" + "</Domain>" + "</Server>";
        assertXMLEqual(new StringReader(controlMBean), stream);
        stream = getReader(host, port, "serverbydomain?querynames=something");
        controlMBean = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<Server>" + "<Exception errorMsg=\"\"/>" + "</Server>";
    }

    public void testCreate() throws Exception {
        ObjectName name = new ObjectName("Http:name=create");
        try {
            Reader stream = getReader(host, port, "create?class=openjmx.adaptor.http.HttpAdaptor&objectname=" + name.toString());
            String controlMBean = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<MBeanOperation>" + "<Operation objectname=\"Http:name=create\" name=\"create\" result=\"success\"/>" + "</MBeanOperation>";
            assertXMLEqual(new StringReader(controlMBean), stream);
        } finally {
            server.unregisterMBean(name);
        }
    }

    /**
	 * Test the mbeans request
	 */
    public void testSingleMBean() throws Exception {
        Reader stream = getReader(host, port, "mbean?objectname=Test:name=test1");
        String controlMBean = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<MBean classname=\"test.openjmx.adaptor.http.HttpAdaptorXMLTest$TestClass\" description=\"\" objectname=\"Test:name=test1\">" + "<Attribute availability=\"RO\" description=\"\" name=\"Double\" type=\"java.lang.Double\" value=\"0.0\"/>" + "<Attribute availability=\"RW\" description=\"\" name=\"Str\" type=\"java.lang.String\" value=\"t1\"/>" + "<Attribute availability=\"RO\" description=\"\" name=\"True\" type=\"boolean\"  value=\"true\"/>" + "<Constructor description=\"\" name=\"test.openjmx.adaptor.http.HttpAdaptorXMLTest$TestClass\">" + "<Parameter description=\"\" id=\"0\" name=\"\" type=\"java.lang.String\"/>" + "</Constructor>" + "<Operation description=\"\" impact=\"unknown\" name=\"aMethod\" return=\"java.lang.Boolean\">" + "<Parameter description=\"\" id=\"0\" name=\"\" type=\"java.lang.String\">" + "</Parameter>" + "</Operation>" + "<Operation description=\"\" impact=\"unknown\" name=\"anotherMethod\" return=\"void\">" + "<Parameter description=\"\" id=\"0\" name=\"\" type=\"java.lang.String\"></Parameter>" + "<Parameter description=\"\" id=\"1\" name=\"\" type=\"int\"></Parameter>" + "</Operation>" + "<Notification description=\"test\" name=\"name\">" + "<Type name=\"test1\"></Type>" + "<Type name=\"test2\"></Type>" + "</Notification>" + "</MBean>";
        assertXMLEqual(new StringReader(controlMBean), stream);
    }

    public void testSingleMBeanAndFilters() throws Exception {
        Reader stream = getReader(host, port, "mbean?objectname=Test:name=test1&attributes=false");
        String controlMBean = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<MBean classname=\"test.openjmx.adaptor.http.HttpAdaptorXMLTest$TestClass\" description=\"\" objectname=\"Test:name=test1\">" + "<Constructor description=\"\" name=\"test.openjmx.adaptor.http.HttpAdaptorXMLTest$TestClass\">" + "<Parameter description=\"\" id=\"0\" name=\"\" type=\"java.lang.String\"/>" + "</Constructor>" + "<Operation description=\"\" impact=\"unknown\" name=\"aMethod\" return=\"java.lang.Boolean\">" + "<Parameter description=\"\" id=\"0\" name=\"\" type=\"java.lang.String\">" + "</Parameter>" + "</Operation>" + "<Operation description=\"\" impact=\"unknown\" name=\"anotherMethod\" return=\"void\">" + "<Parameter description=\"\" id=\"0\" name=\"\" type=\"java.lang.String\"></Parameter>" + "<Parameter description=\"\" id=\"1\" name=\"\" type=\"int\"></Parameter>" + "</Operation>" + "<Notification description=\"test\" name=\"name\">" + "<Type name=\"test1\"></Type>" + "<Type name=\"test2\"></Type>" + "</Notification>" + "</MBean>";
        assertXMLEqual(new StringReader(controlMBean), stream);
        stream.close();
        stream = getReader(host, port, "mbean?objectname=Test:name=test1&constructors=false");
        controlMBean = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<MBean classname=\"test.openjmx.adaptor.http.HttpAdaptorXMLTest$TestClass\" description=\"\" objectname=\"Test:name=test1\">" + "<Attribute availability=\"RO\" description=\"\" name=\"Double\" type=\"java.lang.Double\" value=\"0.0\"/>" + "<Attribute availability=\"RW\" description=\"\" name=\"Str\" type=\"java.lang.String\" value=\"t1\"/>" + "<Attribute availability=\"RO\" description=\"\" name=\"True\" type=\"boolean\"  value=\"true\"/>" + "<Operation description=\"\" impact=\"unknown\" name=\"aMethod\" return=\"java.lang.Boolean\">" + "<Parameter description=\"\" id=\"0\" name=\"\" type=\"java.lang.String\">" + "</Parameter>" + "</Operation>" + "<Operation description=\"\" impact=\"unknown\" name=\"anotherMethod\" return=\"void\">" + "<Parameter description=\"\" id=\"0\" name=\"\" type=\"java.lang.String\"></Parameter>" + "<Parameter description=\"\" id=\"1\" name=\"\" type=\"int\"></Parameter>" + "</Operation>" + "<Notification description=\"test\" name=\"name\">" + "<Type name=\"test1\"></Type>" + "<Type name=\"test2\"></Type>" + "</Notification>" + "</MBean>";
        assertXMLEqual(new StringReader(controlMBean), stream);
        stream.close();
        stream = getReader(host, port, "mbean?objectname=Test:name=test1&operations=false");
        controlMBean = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<MBean classname=\"test.openjmx.adaptor.http.HttpAdaptorXMLTest$TestClass\" description=\"\" objectname=\"Test:name=test1\">" + "<Attribute availability=\"RO\" description=\"\" name=\"Double\" type=\"java.lang.Double\" value=\"0.0\"/>" + "<Attribute availability=\"RW\" description=\"\" name=\"Str\" type=\"java.lang.String\" value=\"t1\"/>" + "<Attribute availability=\"RO\" description=\"\" name=\"True\" type=\"boolean\"  value=\"true\"/>" + "<Constructor description=\"\" name=\"test.openjmx.adaptor.http.HttpAdaptorXMLTest$TestClass\">" + "<Parameter description=\"\" id=\"0\" name=\"\" type=\"java.lang.String\"/>" + "</Constructor>" + "<Notification description=\"test\" name=\"name\">" + "<Type name=\"test1\"></Type>" + "<Type name=\"test2\"></Type>" + "</Notification>" + "</MBean>";
        assertXMLEqual(new StringReader(controlMBean), stream);
        stream.close();
        stream = getReader(host, port, "mbean?objectname=Test:name=test1&notifications=false");
        controlMBean = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<MBean classname=\"test.openjmx.adaptor.http.HttpAdaptorXMLTest$TestClass\" description=\"\" objectname=\"Test:name=test1\">" + "<Attribute availability=\"RO\" description=\"\" name=\"Double\" type=\"java.lang.Double\" value=\"0.0\"/>" + "<Attribute availability=\"RW\" description=\"\" name=\"Str\" type=\"java.lang.String\" value=\"t1\"/>" + "<Attribute availability=\"RO\" description=\"\" name=\"True\" type=\"boolean\"  value=\"true\"/>" + "<Constructor description=\"\" name=\"test.openjmx.adaptor.http.HttpAdaptorXMLTest$TestClass\">" + "<Parameter description=\"\" id=\"0\" name=\"\" type=\"java.lang.String\"/>" + "</Constructor>" + "<Operation description=\"\" impact=\"unknown\" name=\"aMethod\" return=\"java.lang.Boolean\">" + "<Parameter description=\"\" id=\"0\" name=\"\" type=\"java.lang.String\">" + "</Parameter>" + "</Operation>" + "<Operation description=\"\" impact=\"unknown\" name=\"anotherMethod\" return=\"void\">" + "<Parameter description=\"\" id=\"0\" name=\"\" type=\"java.lang.String\"></Parameter>" + "<Parameter description=\"\" id=\"1\" name=\"\" type=\"int\"></Parameter>" + "</Operation>" + "</MBean>";
        assertXMLEqual(new StringReader(controlMBean), stream);
        stream.close();
        stream = getReader(host, port, "mbean?objectname=Test:name=test1&notifications=false&attributes=false&operations=false&constructors=false");
        controlMBean = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<MBean classname=\"test.openjmx.adaptor.http.HttpAdaptorXMLTest$TestClass\" description=\"\" objectname=\"Test:name=test1\">" + "</MBean>";
        assertXMLEqual(new StringReader(controlMBean), stream);
    }

    public Reader getReader(String host, int port, String path) throws IOException, MalformedURLException {
        URL url = new URL("http://" + host + ":" + port + "/" + path);
        URLConnection connection = url.openConnection();
        return new InputStreamReader(connection.getInputStream());
    }
}
