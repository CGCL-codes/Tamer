package com.volantis.mcs.ibm.ucp;

import com.ibm.ucp.IProvider;
import com.ibm.ucp.Profile;
import com.ibm.ucp.ProviderFactory;
import com.ibm.ucp.util.Environment;
import com.volantis.devrep.repository.accessors.EclipseDeviceRepository;
import com.volantis.mcs.objects.FileExtension;
import com.volantis.mcs.repository.xml.XMLRepository;
import com.volantis.synergetics.cornerstone.utilities.xml.jaxp.TransformerMetaFactory;
import com.volantis.synergetics.cornerstone.utilities.xml.jaxp.TestTransformerMetaFactory;
import com.volantis.synergetics.testtools.Executor;
import com.volantis.synergetics.testtools.HypersonicManager;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.testtools.io.IOUtils;
import org.jdom.input.DefaultJDOMFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.jdom.Namespace;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Test case for {@link MCSDeviceRepositoryProvider}.
 *
 * @todo this points out the need for more infrastructure for repository tests.
 * We could have a RepositoryExecutor interface with a
 *  - execute(RespositoryManagerContext)
 * method. Then we could have an abstract RepositoryManager class which has a
 *  - setInitialisor(RepositoryInitialisor)
 *  - doExecuteWith(RepositoryExecutor).
 * The RepositoryInitialisor interface would have a single method
 *  - doInitialisationWith(RepositoryManagerContext)
 * RepositoryManager would use the initialisor instance to initialise the
 * repository before using the executor to run the test when doExecuteWith was
 * called. Then we could have concrete subclasses XMLRepositoryManager and
 * JDBCRepositoryManager. JDBCRepositoryManager would add the
 *  - setCreator(JDBCRepositoryCreator)
 * method, where JDBCRepositoryCreator interface would have the method
 *  - doCreateWith(JDBCRepositoryManagerContext)
 * JDBCRepositoryManager would use the creator instance to create the database
 * tables it needs. XMLRepositoryManager doesn't need to create tables, but it
 * would need to call XMLRepository.write() after the initialisation stage.
 * After all that was done, we could write instances of RepositoryExecutor
 * which could implement repository tests which could run against either of
 * the repository types by executing them with different manager instances.
 */
public class MCSDeviceRepositoryProviderTestCase extends TestCaseAbstract {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * Used to create XSL transformers.
     */
    private static final TransformerMetaFactory transformerMetaFactory = new TestTransformerMetaFactory();

    private static final Namespace DEVICE_NAMESPACE = Namespace.getNamespace("", "http://www.volantis.com/xmlns/device-repository/device");

    private static final Namespace HIERARCHY_NAMESPACE = Namespace.getNamespace("", "http://www.volantis.com/xmlns/device-repository/device-hierarchy");

    /**
     * The name of an empty repository file which we can use for testing -
     * we need a valid repository to add stuff to.
     */
    private static final String EMPTY_REPOSITORY_FILE = "repository.zip";

    public void testDefaultConstructorFailure() throws Exception {
        Environment.init("/com/volantis/mcs/ibm/ucp/ucp-config.xml");
        try {
            new MCSDeviceRepositoryProvider("provider name");
            fail("default constructor should throw exception");
        } catch (UnsupportedOperationException e) {
        }
    }

    public void testNoParameters() throws Exception {
        Environment.init("/com/volantis/mcs/ibm/ucp/ucp-config.xml");
        Element parameters = createParameters(null);
        try {
            new MCSDeviceRepositoryProvider("provider name", parameters);
            fail("construction with no parameters tag should fail");
        } catch (RuntimeException e) {
        }
    }

    public void testEmptyParameters() throws Exception {
        Environment.init("/com/volantis/mcs/ibm/ucp/ucp-config.xml");
        Element parameters = createParameters(new HashMap());
        try {
            new MCSDeviceRepositoryProvider("provider name", parameters);
            fail("construction with empty parameters tag should fail");
        } catch (RuntimeException e) {
        }
    }

    public void testXmlDeviceNone() throws Exception {
        Environment.init("/com/volantis/mcs/ibm/ucp/ucp-config.xml");
        File deviceRepository = IOUtils.extractTempZipFromJarFile(getClass(), EMPTY_REPOSITORY_FILE, FileExtension.DEVICE_REPOSITORY.getExtension());
        HashMap params = new HashMap();
        params.put("repository-type", "xml");
        params.put("file", deviceRepository.getPath());
        Element parameters = createParameters(params);
        String providerName = "provider name";
        String deviceName = "uNkNoWn_dEvIcE";
        MCSDeviceRepositoryProvider provider = new MCSDeviceRepositoryProvider(providerName, parameters);
        assertEquals("", providerName, provider.getName());
        assertNull("", provider.getProfile(deviceName));
        assertNull("", provider.getProfileDescription(deviceName));
    }

    /**
     * Test a single device with no values in a XML repository.
     * <p>
     * This should test basic device retrieval and null value handling for
     * the values we are adapting.
     */
    public void testXMLDeviceEmpty() throws Exception {
        HashMap deviceToProfile = new HashMap();
        deviceToProfile.put(new DeviceValue(), new ProfileValue());
        checkXMLDeviceToProfileAdaption(deviceToProfile);
    }

    /**
     * Test a set of devices with no values in an XML repository.
     * <p>
     * This should test the device name set handling methods.
     */
    public void testXMLDeviceEmptySet() throws Exception {
        HashMap deviceToProfile = new HashMap();
        deviceToProfile.put(new DeviceValue(), new ProfileValue());
        deviceToProfile.put(new DeviceValue(), new ProfileValue());
        deviceToProfile.put(new DeviceValue(), new ProfileValue());
        checkXMLDeviceToProfileAdaption(deviceToProfile);
    }

    /**
     * Test a single device, with values defined for those properties which
     * require adaption, in an XML repository.
     * <p>
     * This should test the adaption of straightforward properties.
     */
    public void testXMLDeviceFull() throws Exception {
        HashMap deviceToProfile = new HashMap();
        DeviceValue deviceValue = new DeviceValue();
        deviceValue.pixeldepth = "8";
        deviceValue.rendermode = "greyscale";
        deviceValue.modelnum = "Exploding Phone 101";
        deviceValue.pixelsx = "64";
        deviceValue.pixelsy = "32";
        deviceValue.charactersx = "10";
        deviceValue.charactersy = "4";
        deviceValue.mfg = "Acme";
        deviceValue.uaprofCcppAccept = "text/html, text/plain";
        deviceValue.brwsrname = "Acme WML Browser";
        deviceValue.brwsrvers = "2.01";
        deviceValue.uaprofWmlVersion = "1.1, 1.3";
        ProfileValue profileValue = new ProfileValue();
        profileValue.bitsPerPixel = "8";
        profileValue.colorCapable = "No";
        profileValue.model = "Exploding Phone 101";
        profileValue.screenSize = "64x32";
        profileValue.screenSizeChar = "10x4";
        profileValue.vendor = "Acme";
        profileValue.ccppAccept = "[text/html, text/plain]";
        profileValue.browserName = "Acme WML Browser";
        profileValue.browserVersion = "2.01";
        profileValue.wmlVersion = "[1.1, 1.3]";
        deviceToProfile.put(deviceValue, profileValue);
        checkXMLDeviceToProfileAdaption(deviceToProfile);
    }

    /**
     * Test the adaption of the rendermode=rgb property.
     */
    public void testXMLDeviceRenderModeRgb() throws Exception {
        HashMap deviceToProfile = new HashMap();
        DeviceValue deviceValue = new DeviceValue();
        deviceValue.rendermode = "rgb";
        deviceValue.pixeldepth = "2";
        ProfileValue profileValue = new ProfileValue();
        profileValue.colorCapable = "Yes";
        profileValue.bitsPerPixel = "2";
        deviceToProfile.put(deviceValue, profileValue);
        checkXMLDeviceToProfileAdaption(deviceToProfile);
    }

    /**
     * Test the adaption of the rendermode=pallette property.
     */
    public void testXMLDeviceRenderModePallette() throws Exception {
        HashMap deviceToProfile = new HashMap();
        DeviceValue deviceValue = new DeviceValue();
        deviceValue.rendermode = "pallette";
        deviceValue.pixeldepth = "2";
        ProfileValue profileValue = new ProfileValue();
        profileValue.colorCapable = "Yes";
        profileValue.bitsPerPixel = "2";
        deviceToProfile.put(deviceValue, profileValue);
        checkXMLDeviceToProfileAdaption(deviceToProfile);
    }

    /**
     * Check the results of adapting a set of devices into Profiles.
     * <p>
     * This will take a mapping of device to profile values, create a set of
     * synthetic device names for each entry in the map, add the devices into
     * a temporary repository with the synthetic names.
     * <p>
     * It will then create a Provider against that temporary repository, and
     * attempt to retreive Profiles from the Provider using the set of
     * synthetic names, checking that the Profiles returned match the profile
     * values passed in the original map.
     *
     * @param deviceToProfile a map of DeviceValues to ProfileValues.
     * @throws Exception
     */
    public void checkXMLDeviceToProfileAdaption(Map deviceToProfile) throws Exception {
        Environment.init("/com/volantis/mcs/ibm/ucp/ucp-config.xml");
        File deviceRepository = IOUtils.extractTempZipFromJarFile(getClass(), EMPTY_REPOSITORY_FILE, FileExtension.DEVICE_REPOSITORY.getExtension());
        EclipseDeviceRepository accessor = new EclipseDeviceRepository(deviceRepository.getPath(), transformerMetaFactory, new DefaultJDOMFactory(), null);
        int i = 0;
        HashMap nameToProfile = new HashMap();
        for (Iterator keys = deviceToProfile.keySet().iterator(); keys.hasNext(); ) {
            DeviceValue deviceValue = (DeviceValue) keys.next();
            i += 1;
            String deviceName = "mytest-device-" + i;
            nameToProfile.put(deviceName, deviceToProfile.get(deviceValue));
            accessor.addHierarchyDeviceElement(deviceName, accessor.getRootDeviceName());
            HashMap deviceProps = new HashMap();
            deviceProps.put("pixeldepth", deviceValue.pixeldepth);
            deviceProps.put("rendermode", deviceValue.rendermode);
            deviceProps.put("modelnum", deviceValue.modelnum);
            deviceProps.put("pixelsx", deviceValue.pixelsx);
            deviceProps.put("pixelsy", deviceValue.pixelsy);
            deviceProps.put("charactersx", deviceValue.charactersx);
            deviceProps.put("charactersy", deviceValue.charactersy);
            deviceProps.put("mfg", deviceValue.mfg);
            deviceProps.put("UAProf.CcppAccept", deviceValue.uaprofCcppAccept);
            deviceProps.put("brwsrname", deviceValue.brwsrname);
            deviceProps.put("brwsrvers", deviceValue.brwsrvers);
            deviceProps.put("UAProf.WmlVersion", deviceValue.uaprofWmlVersion);
            org.jdom.Element deviceElement = createDeviceElement(deviceProps);
            Map devices = new HashMap(1);
            devices.put(deviceName, deviceElement);
            accessor.writeDeviceElements(devices);
            accessor.writeHierarchy();
        }
        accessor.saveRepositoryArchive();
        HashMap repositoryProps = new HashMap();
        repositoryProps.put(XMLRepository.DEVICE_REPOSITORY_PROPERTY, deviceRepository.getPath());
        HashMap params = new HashMap();
        params.put("repository-type", "xml");
        params.put("file", deviceRepository.getPath());
        Element parameters = createParameters(params);
        MCSDeviceRepositoryProvider provider = new MCSDeviceRepositoryProvider("provider", parameters);
        Set nameSet = nameToProfile.keySet();
        System.out.println("expecting: " + nameSet);
        System.out.println("and got  : " + provider.getProfileKeys());
        for (Iterator names = nameSet.iterator(); names.hasNext(); ) {
            String deviceName = (String) names.next();
            ProfileValue profileValue = (ProfileValue) nameToProfile.get(deviceName);
            Profile profile = provider.getProfile(deviceName);
            assertNotNull("cannot retrieve profile for empty device", profile);
            assertEquals("profile description should equal device name", deviceName, provider.getProfileDescription(deviceName));
            assertEquals("bitsPerPixel", profileValue.bitsPerPixel, profile.getPropertyString("BitsPerPixel"));
            assertEquals("colorCapable", profileValue.colorCapable, profile.getPropertyString("ColorCapable"));
            assertEquals("model", profileValue.model, profile.getPropertyString("Model"));
            assertEquals("screenSize", profileValue.screenSize, profile.getPropertyString("ScreenSize"));
            assertEquals("screenSizeChar", profileValue.screenSizeChar, profile.getPropertyString("ScreenSizeChar"));
            assertEquals("vendor", profileValue.vendor, profile.getPropertyString("Vendor"));
            assertEquals("ccppAccept", profileValue.ccppAccept, profile.getPropertyString("CcppAccept"));
            assertEquals("browserName", profileValue.browserName, profile.getPropertyString("BrowserName"));
            assertEquals("browserVersion", profileValue.browserVersion, profile.getPropertyString("BrowserVersion"));
            assertEquals("wmlVersion", profileValue.wmlVersion, profile.getPropertyString("WmlVersion"));
        }
        assertTrue("profile keys should contain all explicitly " + "created device names", provider.getProfileKeys().containsAll(nameSet));
    }

    /**
     * Create a device element with the specified policy child elements
     * @param policies A map of name value pairs to add to the device element
     * as policy elements
     * @return a device element with the specified policy child elements
     */
    private org.jdom.Element createDeviceElement(Map policies) {
        org.jdom.Element deviceElement = new org.jdom.Element("device", DEVICE_NAMESPACE);
        org.jdom.Element policiesElement = new org.jdom.Element("policies", DEVICE_NAMESPACE);
        deviceElement.addContent(policiesElement);
        Set keys = policies.keySet();
        for (Iterator iterator = keys.iterator(); iterator.hasNext(); ) {
            String name = (String) iterator.next();
            String value = (String) policies.get(name);
            if (name != null && value != null) {
                org.jdom.Element policy = new org.jdom.Element("policy", DEVICE_NAMESPACE);
                policy.setAttribute("name", name);
                policy.setAttribute("value", value);
                policiesElement.addContent(policy);
            }
        }
        return deviceElement;
    }

    public void testJDBCDeviceNone() throws Exception {
        Environment.init("/com/volantis/mcs/ibm/ucp/ucp-config.xml");
        final HypersonicManager hypersonicMgr = new HypersonicManager(HypersonicManager.IN_MEMORY_SOURCE);
        hypersonicMgr.useCleanupWith(new Executor() {

            public void execute() throws Exception {
                Connection connection = DriverManager.getConnection(hypersonicMgr.getUrl(), HypersonicManager.DEFAULT_USERNAME, HypersonicManager.DEFAULT_PASSWORD);
                Statement statement = connection.createStatement();
                String sql = "CREATE TABLE VMDEVICE_PATTERNS ( " + "PROJECT   VARCHAR (255)  NOT NULL," + "NAME      VARCHAR (20)  NOT NULL," + "PATTERN   VARCHAR (255)  NOT NULL," + "REVISION  NUMERIC (9)   DEFAULT 0" + " ) ";
                statement.execute(sql);
                statement.close();
                statement = connection.createStatement();
                sql = "CREATE TABLE VMPOLICY_VALUES ( " + "PROJECT   VARCHAR (255)  NOT NULL," + "NAME      VARCHAR (20)  NOT NULL," + "POLICY    VARCHAR (200)  NOT NULL," + "VALUE     VARCHAR (1024)," + "REVISION  NUMERIC (9)   DEFAULT 0" + " ) ";
                statement.execute(sql);
                statement.close();
                final HashMap params = new HashMap();
                params.put("repository-type", "jdbc");
                params.put("odbc-user", HypersonicManager.DEFAULT_USERNAME);
                params.put("odbc-password", HypersonicManager.DEFAULT_PASSWORD);
                params.put("odbc-source", hypersonicMgr.getSource());
                params.put("odbc-vendor", "hypersonic");
                params.put("project", "#DefaultProject");
                Element parameters = createParameters(params);
                MCSDeviceRepositoryProvider provider = new MCSDeviceRepositoryProvider("provider name", parameters);
                Profile profile = provider.getProfile("unknown device");
                assertNull(profile);
            }
        });
    }

    /**
     * Try creating our provider via the hardcoded UCP config file rather than
     * simulating it's creation manually.
     *
     * @todo later this cannot be done since the ucp-config.xml doesn't contain a reference to a valid and existing device repository
     */
    public void noTestFullInitialisationFromUcpConfig() throws Exception {
        Environment.init("/com/volantis/mcs/ibm/ucp/ucp-config.xml");
        IProvider provider = ProviderFactory.getProvider();
        assertTrue(provider instanceof MCSDeviceRepositoryProvider);
    }

    /**
     * Create a DOM tree containing parameters as would be passed into the
     * constructor by UCP. This has the same structure as the <parameters>
     * tree defined in the UCP XML config file.
     *
     * @param parameters
     * @return a DOM parameters element
     * @throws Exception
     */
    private Element createParameters(Map parameters) throws Exception {
        Element root = null;
        if (parameters != null) {
            String xml = "";
            xml += "<?xml version='1.0'?>";
            xml += "<parameters>";
            for (Iterator i = parameters.keySet().iterator(); i.hasNext(); ) {
                String name = (String) i.next();
                String value = (String) parameters.get(name);
                xml += "<param name='" + name + "' value='" + value + "'/>";
            }
            xml += "</parameters>";
            root = parse(xml);
        }
        return root;
    }

    /**
     * Parse a valid snippet of XML, returning the root element of a DOM tree.
     *
     * @param xml the xml snippet to parse.
     * @return the root element.
     * @throws Exception if there was a problem with the parse.
     */
    private Element parse(String xml) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(false);
        DocumentBuilder db = dbf.newDocumentBuilder();
        ByteArrayInputStream input = new ByteArrayInputStream(xml.getBytes());
        Document document = db.parse(input);
        return document.getDocumentElement();
    }

    /**
     * Stores the values we adapt into a Profile.
     */
    private static class ProfileValue {

        String bitsPerPixel;

        String colorCapable;

        String model;

        String screenSize;

        String screenSizeChar;

        String vendor;

        String ccppAccept;

        String browserName;

        String browserVersion;

        String wmlVersion;
    }

    /**
     * Stores the value we adapt out of a InternalDevice.
     */
    private static class DeviceValue {

        String pixeldepth;

        String rendermode;

        String modelnum;

        String pixelsx;

        String pixelsy;

        String charactersx;

        String charactersy;

        String mfg;

        String uaprofCcppAccept;

        String brwsrname;

        String brwsrvers;

        String uaprofWmlVersion;
    }
}
