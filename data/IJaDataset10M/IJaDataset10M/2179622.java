package net.sf.securejdms.server.document.entity;

import static org.junit.Assert.assertNotNull;
import java.io.InputStream;
import org.junit.Ignore;
import org.junit.Test;

public class PersistenceXMLTest {

    @Test
    @Ignore
    public void test() throws Exception {
        InputStream stream = this.getClass().getResourceAsStream("/META-INF/persistence.xml");
        assertNotNull("persistence.xml not found", stream);
        byte[] content = new byte[stream.available()];
        stream.read(content);
        System.out.println(new String(content));
    }
}
