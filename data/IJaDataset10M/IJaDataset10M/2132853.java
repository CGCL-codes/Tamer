package org.apache.harmony.luni.tests.java.net;

import java.io.IOException;
import java.net.ContentHandler;
import java.net.URL;
import java.net.URLConnection;
import junit.framework.TestCase;

public class ContentHandlerTest extends TestCase {

    /**
     * @tests java.net.ContentHandler#getContent(java.net.URLConnection,
     *        java.lang.Class[])
     */
    public void test_getContent() throws IOException {
        URLConnection conn = new URL("http://www.apache.org").openConnection();
        Class[] classes = { Foo.class, String.class };
        ContentHandler handler = new ContentHandlerImpl();
        ((ContentHandlerImpl) handler).setContent(new Foo());
        Object content = handler.getContent(conn, classes);
        assertEquals("Foo", ((Foo) content).getFoo());
        ((ContentHandlerImpl) handler).setContent(new FooSub());
        content = handler.getContent(conn, classes);
        assertEquals("FooSub", ((Foo) content).getFoo());
        Class[] classes2 = { FooSub.class, String.class };
        ((ContentHandlerImpl) handler).setContent(new Foo());
        content = handler.getContent(conn, classes2);
        assertNull(content);
    }
}

class ContentHandlerImpl extends ContentHandler {

    private Object content;

    @Override
    public Object getContent(URLConnection uConn) throws IOException {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }
}

class Foo {

    public String getFoo() {
        return "Foo";
    }
}

class FooSub extends Foo {

    public String getFoo() {
        return "FooSub";
    }
}
