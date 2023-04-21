package org.jpublish.util.uri;

import junit.framework.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Tests for the jpublish.org.util.InternalURIParserTest class.
 *
 * @author Anthony Eden
 */
public class InternalURIParserTest extends TestCase {

    private static final Log log = LogFactory.getLog(InternalURIParserTest.class);

    /**
     * A unit test for JUnit
     *
     * @throws Exception Description of the Exception
     */
    public void testTemplateURI() throws Exception {
        String uriString = "template://basic.html";
        InternalURIParser parser = InternalURIParser.getInstance();
        InternalURI uri = parser.parse(uriString);
        Assert.assertEquals(TemplateURI.class, uri.getClass());
        Assert.assertEquals("template", uri.getProtocol());
        Assert.assertEquals("basic.html", uri.getPath());
        Assert.assertEquals(uriString, uri.toURI());
    }

    /**
     * A unit test for JUnit
     *
     * @throws Exception Description of the Exception
     */
    public void testNamedTemplateURI() throws Exception {
        String uriString = "template:test://basic.html";
        InternalURIParser parser = InternalURIParser.getInstance();
        InternalURI uri = parser.parse(uriString);
        Assert.assertEquals(TemplateURI.class, uri.getClass());
        Assert.assertEquals("template", uri.getProtocol());
        Assert.assertEquals("test", ((TemplateURI) uri).getTemplateManagerName());
        Assert.assertEquals("basic.html", uri.getPath());
        Assert.assertEquals(uriString, uri.toURI());
    }

    /**
     * A unit test for JUnit
     *
     * @throws Exception Description of the Exception
     */
    public void testRepositoryURI() throws Exception {
        String uriString = "repository:fs_repository://index.html";
        InternalURIParser parser = InternalURIParser.getInstance();
        InternalURI uri = parser.parse(uriString);
        Assert.assertEquals(RepositoryURI.class, uri.getClass());
        Assert.assertEquals("repository", uri.getProtocol());
        Assert.assertEquals("index.html", uri.getPath());
        Assert.assertEquals("fs_repository", ((RepositoryURI) uri).getRepositoryName());
        Assert.assertEquals(uriString, uri.toURI());
    }

    /**
     * A unit test for JUnit
     *
     * @throws Exception Description of the Exception
     */
    public void testRepositoryURILongPath() throws Exception {
        String uriString = "repository:fs_repository://foo/bar/baz/index.html";
        InternalURIParser parser = InternalURIParser.getInstance();
        InternalURI uri = parser.parse(uriString);
        Assert.assertEquals(RepositoryURI.class, uri.getClass());
        Assert.assertEquals("repository", uri.getProtocol());
        Assert.assertEquals("foo/bar/baz/index.html", uri.getPath());
        Assert.assertEquals("fs_repository", ((RepositoryURI) uri).getRepositoryName());
        Assert.assertEquals(uriString, uri.toURI());
    }

    /**
     * A unit test for JUnit
     *
     * @throws Exception Description of the Exception
     */
    public void testRepositoryURIWithURL() throws Exception {
        String uriString = "repository:web_repository://" + "http://www.url.com/foo/bar/baz/index.html";
        InternalURIParser parser = InternalURIParser.getInstance();
        InternalURI uri = parser.parse(uriString);
        Assert.assertEquals(RepositoryURI.class, uri.getClass());
        Assert.assertEquals("repository", uri.getProtocol());
        Assert.assertEquals("http://www.url.com/foo/bar/baz/index.html", uri.getPath());
        Assert.assertEquals("web_repository", ((RepositoryURI) uri).getRepositoryName());
        Assert.assertEquals(uriString, uri.toURI());
    }
}
