package com.ail.coretest;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import com.ail.core.Core;
import com.ail.core.History;
import com.ail.core.Version;
import com.ail.core.VersionEffectiveDate;
import com.ail.core.configure.ConfigurationHandler;
import com.ail.core.configure.Group;
import com.ail.core.configure.server.ServerBean;
import com.ail.coretest.service.TestService;

/**
 * The core's factories support the concept of baseing one type on another - in effect
 * allowing type definition to extend one another. The tests here try exercise that
 * support.
 * @version $Revision$
 * @author $Author$
 * @state $State$
 * @date $Date$
 * @source $Source:
 *         /home/bob/CVSRepository/projects/core/test/com/ail/coretest/TestServiceInvocation.java,v $
 */
public class TestFactoryTypeMerging extends CoreUserTestCase {

    private static boolean oneTimeSetupDone = false;

    /**
     * Constructs a test case with the given name.
     */
    public TestFactoryTypeMerging(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(TestFactoryTypeMerging.class);
    }

    public static void main(String[] args) {
        TestRunner.run(suite());
    }

    /**
     * Sets up the fixture (run before every test). Get an instance of Core, and
     * delete the testnamespace from the config table.
     */
    protected void setUp() {
        setCore(new Core(this));
        if (!oneTimeSetupDone) {
            System.setProperty("org.xml.sax.parser", "org.apache.xerces.parsers.SAXParser");
            System.setProperty("java.protocol.handler.pkgs", "com.ail.core.urlhandler");
            System.setProperty("javax.xml.transform.TransformerFactory", "net.sf.saxon.TransformerFactoryImpl");
            tidyUpTestData();
            ConfigurationHandler.reset();
            new ServerBean().resetNamedConfiguration("all");
            new TestService().resetConfiguration();
            resetConfiguration();
            oneTimeSetupDone = true;
        }
        ConfigurationHandler.reset();
    }

    /**
     * Always select the latest configuration.
     * 
     * @return
     */
    public VersionEffectiveDate getVersionEffectiveDate() {
        return new VersionEffectiveDate();
    }

    protected void tearDown() {
    }

    /**
     * The Type class offers a merge() method which will merge values from a 'donor' object
     * into a 'subject'. The type factories use this method to implement the 'Extends' facility where one
     * type definition extends another. The javadocs {@link com.ail.core.Type#mergeWithDataFrom(com.ail.core.Type, Core) here}
     * describe what should happen during a merge. This test checks that functionality.
     * @throws Exception
     */
    public void testSimpleAttributeMerging() throws Exception {
        Version merged = (Version) getCore().newType("ExtendingType");
        assertEquals("H.G.Wells", merged.getAuthor());
        assertEquals("The loganberry's are sweet", merged.getComment());
        assertEquals("Peach and mint", merged.getSource());
        assertNotNull(merged.xpathGet("attribute[id='baseattr']"));
        assertNotNull(merged.xpathGet("attribute[id='subattr']"));
        assertEquals("string", merged.xpathGet("attribute[id='one']/format"));
        assertEquals("feet", merged.xpathGet("attribute[id='one']/unit"));
        assertEquals("overriden-two", merged.xpathGet("attribute[id='two']/value"));
        assertEquals("string,32", merged.xpathGet("attribute[id='two']/format"));
        assertNotNull(merged.xpathGet("attribute[id='root']"));
        assertNotNull(merged.xpathGet("attribute[id='root']/attribute[id='branch1']"));
        assertNotNull(merged.xpathGet("attribute[id='root']/attribute[id='branch2']"));
        assertNotNull(merged.xpathGet("attribute[id='root2']/attribute[id='branch3']"));
        assertEquals("string,32", merged.xpathGet("attribute[id='root']/attribute[id='branch1']/format"));
        assertEquals("ExtBranch", merged.xpathGet("attribute[id='root']/attribute[id='branch1']/value"));
        Group group = (Group) getCore().newType("ExtendingGroup");
        assertEquals("mygroup", group.getName());
        assertEquals("value 1", group.findParameter("param1").getValue());
        assertEquals("overriden value 2", group.findParameter("param2").getValue());
        assertEquals("value 3", group.findParameter("param3").getValue());
    }

    /**
     * Castor should support the use of &lt;xi:include&gt;. This test instantiates a type whose definition
     * uses xi:include to pull in external content.
     * @throws Exception
     */
    public void testXmlInclude() throws Exception {
        History h = (History) getCore().newType("Includer");
        assertTrue(getCore().toXML(h).toString().indexOf("bloody hell") != -1);
    }

    /**
     * xi:include doesn't support relative URIs out of the box. Without it, all URI's used to include
     * content into an XML document have to be absolute. This isn't convenient, especially when we want 
     * let users move products and their type around in CMS without having to edit the content of the
     * types which those products define. For this reason, extends the XML parser to support relative URLs.
     * This test builds an instance of a type which includes using a relative URI.
     * @throws Exception
     */
    public void testXmlIncludeRelativeUrl() throws Exception {
        History h = (History) getCore().newType("IncluderRelative");
        assertTrue(getCore().toXML(h).toString().indexOf("bloody hell") != -1);
    }
}
