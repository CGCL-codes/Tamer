package net.sourceforge.cvsgrab.web;

import net.sourceforge.cvsgrab.AbstractTestCase;
import net.sourceforge.cvsgrab.CVSGrab;
import net.sourceforge.cvsgrab.CvsWebInterface;
import net.sourceforge.cvsgrab.RemoteDirectory;
import net.sourceforge.cvsgrab.RemoteFile;
import net.sourceforge.cvsgrab.RemoteRepository;
import org.w3c.dom.Document;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author <a href="mailto:ludovicc@users.sourceforge.net">Ludovic Claude</a>
 * @version $Revision: 1.10 $ $Date: 2006/07/20 02:08:59 $
 * @cvsgrab.created on 12 oct. 2003
 */
public class ViewCvs0_8InterfaceTest extends AbstractTestCase {

    private ViewCvs0_8Interface _interface;

    private CVSGrab _grabber;

    /**
     * Constructor for ViewCvs0_8InterfaceTest
     * @param testName
     */
    public ViewCvs0_8InterfaceTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        super.setUp();
        _grabber = new CVSGrab();
        _interface = new ViewCvs0_8Interface(_grabber);
    }

    public void testValidateAndDetect() throws Exception {
        List errors = new ArrayList();
        Document doc = getDocument("src/test/html_docs/view_cvs_0_8.html");
        _grabber.getWebOptions().setRootUrl("http://cvs.repository.org/viewcvs.py/");
        _grabber.getWebOptions().setPackagePath("test");
        CvsWebInterface.registerDocument("http://cvs.repository.org/viewcvs.py/test/", doc);
        _interface.validate(errors);
        assertTrue(errors.isEmpty());
        assertEquals("ViewCVS 0.8", _interface.getType());
    }

    public void testGetFiles() throws Exception {
        Document doc = getDocument("src/test/html_docs/view_cvs_0_8.html");
        int i = 0;
        RemoteFile[] files = _interface.getFiles(doc);
        assertEquals("AntRunner.properties", files[i].getName());
        assertEquals("1.1.1.1", files[i++].getVersion());
        assertEquals("CVSGrab.html", files[i].getName());
        assertEquals("1.1.1.1", files[i++].getVersion());
        assertEquals("CVSGrab.jpx", files[i].getName());
        assertEquals("1.2", files[i++].getVersion());
        assertEquals("HotSAX.library", files[i].getName());
        assertEquals("1.1", files[i++].getVersion());
        assertEquals("License.txt", files[i].getName());
        assertEquals("1.1.1.1", files[i++].getVersion());
        assertEquals("build.bat", files[i].getName());
        assertEquals("1.1.1.1", files[i++].getVersion());
        assertEquals("build.xml", files[i].getName());
        assertEquals("1.4", files[i++].getVersion());
        assertEquals("jCVS.library", files[i].getName());
        assertEquals("1.1", files[i++].getVersion());
        assertEquals("Expected no more files", i, files.length);
    }

    public void testGetDirectories() throws Exception {
        Document doc = getDocument("src/test/html_docs/view_cvs_0_8.html");
        int i = 0;
        String[] directories = _interface.getDirectories(doc);
        assertEquals("doc", directories[i++]);
        assertEquals("etc", directories[i++]);
        assertEquals("lib", directories[i++]);
        assertEquals("src", directories[i++]);
        assertEquals("web", directories[i++]);
        assertEquals("Expected no more directories", i, directories.length);
    }

    /**
     * Fix for bug #853915
     */
    public void testStrangeUrls() {
        RemoteRepository repository = new RemoteRepository("http://cvs.sourceforge.net/viewcvs.py/", null);
        RemoteDirectory dir = new RemoteDirectory(repository, "avantgarde/AvantGarde/src/st/fr/cageauxtrolls/avantgarde/gestion/partie/", "partie");
        RemoteFile file = new RemoteFile("RestrictionsArmée.java", "1.1");
        file.setDirectory(dir);
        String fileUrl = _interface.getDownloadUrl(file);
        assertEquals("http://cvs.sourceforge.net/viewcvs.py/*checkout*/avantgarde/AvantGarde/src/st/fr/cageauxtrolls/avantgarde/gestion/partie/RestrictionsArm%E9e.java?rev=1.1", fileUrl);
    }

    public void testGuessWebProperties() {
        Properties webProperties = _interface.guessWebProperties("http://cvs.sourceforge.net/viewcvs.py/cvsgrab/cvsgrab/");
        assertEquals("http://cvs.sourceforge.net/viewcvs.py/", webProperties.get(CVSGrab.ROOT_URL_OPTION));
        assertEquals("cvsgrab/cvsgrab/", webProperties.get(CVSGrab.PACKAGE_PATH_OPTION));
        assertNull(webProperties.get(CVSGrab.TAG_OPTION));
        assertNull(webProperties.get(CVSGrab.QUERY_PARAMS_OPTION));
        webProperties = _interface.guessWebProperties("http://cvs.sourceforge.net/viewcvs.py/cvsgrab/cvsgrab/?only_with_tag=RELEASE_2_0_3");
        assertEquals("http://cvs.sourceforge.net/viewcvs.py/", webProperties.get(CVSGrab.ROOT_URL_OPTION));
        assertEquals("cvsgrab/cvsgrab/", webProperties.get(CVSGrab.PACKAGE_PATH_OPTION));
        assertEquals("RELEASE_2_0_3", webProperties.get(CVSGrab.TAG_OPTION));
        assertNull(webProperties.get(CVSGrab.QUERY_PARAMS_OPTION));
    }
}
