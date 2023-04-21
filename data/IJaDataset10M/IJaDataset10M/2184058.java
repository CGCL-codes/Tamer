package yarfraw;

import java.io.File;
import junit.framework.TestCase;
import org.junit.Test;
import yarfraw.rss20.datamodel.Channel;
import yarfraw.rss20.datamodel.Item;
import yarfraw.rss20.io.Rss20Appender;
import yarfraw.rss20.io.Rss20Reader;
import yarfraw.rss20.io.Rss20Writer;
import yarfraw.rss20.utils.Rss20Utils;

/**
 * Some unit tests.
 * 
 * @author jliang
 *
 */
public class IOTest extends TestCase {

    @Test
    public void testBuilder() throws Exception {
        Channel c = BuilderTest.buildChannel();
        Rss20Writer w = new Rss20Writer(File.createTempFile("yarfraw", ".xml"));
        w.writeChannel(c);
    }

    @Test
    public void testRead() throws Exception {
        Rss20Reader r = new Rss20Reader(Thread.currentThread().getContextClassLoader().getResource("yarfraw/digg.xml").toURI());
        Channel c = r.readChannel();
        assertTrue("Title is not the same", "digg".equals(c.getTitle()));
        assertTrue("language is not the same", "en-us".equals(c.getLanguage().getLanguage().toLowerCase()));
        assertTrue("Link is not the same", "http://digg.com/".equals(c.getLink().toString()));
    }

    @Test
    public void testAppend() throws Exception {
        File f = new File(Thread.currentThread().getContextClassLoader().getResource("yarfraw/digg.xml").toURI());
        Rss20Appender a = new Rss20Appender(f);
        Item item = BuilderTest.buildChannel().getItems().get(0);
        a.addItem(item);
        Channel c = Rss20Utils.read(f);
        assertEquals(item, c.getItems().get(c.getItems().size() - 1));
        int oldSize = c.getItems().size();
        a.removeItem(oldSize - 1);
        c = Rss20Utils.read(f);
        assertEquals(oldSize - 1, c.getItems().size());
    }
}
