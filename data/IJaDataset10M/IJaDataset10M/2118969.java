package net.sf.beezle.sushi.fs.webdav;

import net.sf.beezle.sushi.fs.Node;
import net.sf.beezle.sushi.fs.NodeReadOnlyTest;
import org.junit.Test;
import java.io.IOException;
import java.net.URI;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/** Accesses external hosts and might need proxy configuration => Full test */
public class WebdavReadOnlyFullTest extends NodeReadOnlyTest<WebdavNode> {

    static {
        WebdavFilesystem.wireLog(WORLD.guessProjectHome(WebdavNodeFullBase.class).getAbsolute() + "/target/webdav-readonly.log");
    }

    public void validateDeallocation() {
        assertEquals(0, work.getRoot().getAllocated());
    }

    @Test
    public void normal() throws Exception {
        WebdavNode node;
        node = (WebdavNode) WORLD.node("http://englishediting.de/index.html");
        assertTrue(node.isFile());
        assertTrue(node.exists());
        assertTrue(node.readString().length() > 1);
        assertEquals("//englishediting.de/", node.getRoot().getId());
        assertEquals("index.html", node.getPath());
        assertEquals("", node.getParent().getPath());
    }

    @Test
    public void eq() throws Exception {
        WebdavNode node;
        node = (WebdavNode) WORLD.node("http://englishediting.de/index.html?foo=1");
        assertEquals(node, WORLD.node("http://englishediting.de/index.html?foo=1"));
        assertFalse(node.equals(WORLD.node("http://englishediting.de/index.html?foo=2")));
    }

    @Test
    public void uriWithEmptyPath() throws Exception {
        Node node;
        node = WORLD.node("http://www.heise.de");
        assertTrue(node instanceof WebdavNode);
        assertEquals("http://www.heise.de:80/", node.getURI().toString());
        assertNotNull(node.readBytes());
    }

    @Test
    public void query() throws Exception {
        URI uri;
        WebdavNode node;
        String str;
        uri = URI.create("http://dict.tu-chemnitz.de:80/dings.cgi?lang=en&noframes=1&service=&query=foobarbaz&optword=1&optcase=1&opterrors=0&optpro=0&style=&dlink=self");
        node = (WebdavNode) WORLD.node(uri);
        assertEquals(uri, node.getURI());
        assertTrue(node.isFile());
        str = node.readString();
        assertTrue(str.contains("foobarbaz"));
    }

    @Test
    public void node() throws Exception {
        URI uri;
        WebdavNode node;
        uri = new URI("http://englishediting.de:80/index.html");
        node = (WebdavNode) WORLD.node(uri);
        assertEquals("index.html", node.getPath());
        assertEquals(uri, node.getURI());
    }

    @Override
    protected WebdavNode createWork() throws IOException {
        return (WebdavNode) WORLD.validNode("http://englishediting.de/foo");
    }
}
