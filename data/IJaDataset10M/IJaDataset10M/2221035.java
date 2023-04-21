package info.decamps.m2settings.model;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import junit.framework.TestCase;
import info.decamps.m2settings.XMLBinder;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

/**
 * Test XML marshalling and unmarshalling.
 *
 * Warning: the root element of XMLBinder is *always* settings.
 *
 * @author regis
 *
 */
public class XMLBinderTest extends TestCase {

    private XMLBinder xmlbind;

    public static final String XML_PROLOG = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";

    public static final String XML_SETTINGS_BEGIN = "<settings xmlns=\"http://maven.apache.org/POM/4.0.0\">";

    public static final String XML_SETTINGS_END = "</settings>";

    public static final String XML_MIRROR = "<mirror><mirrorOf>central</mirrorOf><name>name</name><url>http://ftp.ggi-project.org/pub/packages/maven2</url><id>ggi-project.org</id></mirror>";

    public static final String XML_PROXY1 = "<proxy><active>true</active><protocol>http</protocol><username>user</username><password>secretpwd</password><port>8080</port><host>proxy.intra</host><nonProxyHosts>*.intra</nonProxyHosts><id>myproxy-id</id></proxy>";

    public static final String XML_PROXY2 = "<proxy><active>false</active><protocol>http</protocol><username>user2</username><password>pwd2</password><port>8568</port><host>http-proxy.mydomain</host><id>scnd</id></proxy>";

    public static final String XML_PROXY = XML_PROXY1 + XML_PROXY2;

    public static final String XML_SERVER = "<server><username>username</username><password>password</password><privateKey>privatekey</privateKey><passphrase>passphrase</passphrase><filePermissions>filepermissions</filePermissions><directoryPermissions>directorypermissions</directoryPermissions><configuration/><id>id</id></server>";

    @Override
    protected void setUp() throws Exception {
        xmlbind = new XMLBinder();
    }

    /**
     * Test transformation of Mirror bean into XML.
     * This is a simple test.
     * @throws IOException
     * @throws ValidationException
     * @throws MarshalException
     */
    public void testMarshallMirrors() throws MarshalException, ValidationException, IOException {
        Mirrors mirrors = generateMirrorsElement();
        String xmlSettings = XML_PROLOG + "<mirrors xmlns=\"http://maven.apache.org/POM/4.0.0\">" + XML_MIRROR + "</mirrors>";
        assertEquals(xmlSettings, mytrim(XMLBinder.toXMLString(mirrors)));
    }

    public static Mirrors generateMirrorsElement() {
        Mirror mirror = new Mirror();
        mirror.setMirrorOf("central");
        mirror.setName("name");
        mirror.setUrl("http://ftp.ggi-project.org/pub/packages/maven2");
        mirror.setId("ggi-project.org");
        Mirrors mirrors = new Mirrors();
        mirrors.addMirror(mirror);
        return mirrors;
    }

    /**
     * Test transformation of Proxy into XML.
     * Proxy has an integer element
     * @throws IOException
     * @throws ValidationException
     * @throws MarshalException
     */
    public void testMarshallProxies() throws MarshalException, ValidationException, IOException {
        Proxies proxies = generateProxiesElement();
        assertEquals(XML_PROLOG + "<proxies xmlns=\"http://maven.apache.org/POM/4.0.0\">" + XML_PROXY + "</proxies>", mytrim(XMLBinder.toXMLString(proxies)));
    }

    public static Proxies generateProxiesElement() {
        Proxies proxies = new Proxies();
        proxies.addProxy(generateProxy1());
        proxies.addProxy(generateProxy2());
        return proxies;
    }

    protected static Proxy generateProxy1() {
        Proxy proxy = new Proxy();
        proxy.setActive(true);
        proxy.setProtocol("http");
        proxy.setUsername("user");
        proxy.setPassword("secretpwd");
        proxy.setPort(8080);
        proxy.setNonProxyHosts("*.intra");
        proxy.setId("myproxy-id");
        proxy.setHost("proxy.intra");
        return proxy;
    }

    protected static Proxy generateProxy2() {
        Proxy proxy = new Proxy();
        proxy.setActive(false);
        proxy.setProtocol("http");
        proxy.setUsername("user2");
        proxy.setPassword("pwd2");
        proxy.setPort(8568);
        proxy.setId("scnd");
        proxy.setHost("http-proxy.mydomain");
        return proxy;
    }

    protected static String mytrim(String string) {
        String retval = string.replaceFirst(".*", "");
        retval = retval.replaceAll("(?m)^\\s+", "");
        retval = retval.replaceAll("\\n+", "");
        return XML_PROLOG + retval;
    }

    public void testMytrim() {
        String orig, expected;
        orig = XML_PROLOG + "<a>hello</a>\n<b>world</b>";
        expected = XML_PROLOG + "<a>hello</a><b>world</b>";
        assertEquals(expected, mytrim(orig));
        orig = XML_PROLOG + "\t   <a>hello</a>";
        expected = XML_PROLOG + "<a>hello</a>";
        assertEquals(expected, mytrim(orig));
        orig = XML_PROLOG + "\t<a>hello</a>\n\t<b>world</b>";
        expected = XML_PROLOG + "<a>hello</a><b>world</b>";
        assertEquals(expected, mytrim(orig));
    }
}
