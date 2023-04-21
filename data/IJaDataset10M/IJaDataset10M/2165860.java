package yarfraw.extension;

import java.io.File;
import junit.framework.TestCase;
import org.junit.Test;
import yarfraw.core.datamodel.ChannelFeed;
import yarfraw.core.datamodel.ItemEntry;
import yarfraw.generated.blogger.elements.BloggerExtension;
import yarfraw.io.FeedReader;
import yarfraw.io.FeedWriter;
import yarfraw.utils.extension.ExtensionUtils;

public class BloggerTest extends TestCase {

    @Test
    public void testBuild() throws Exception {
        BloggerExtension ext = new BloggerExtension();
        ext.setConvertLineBreaks(true);
        ext.setDraft(true);
        File f = new File("testTmpOutput/blogger.xml");
        FeedWriter w = new FeedWriter(f);
        ChannelFeed c = new ChannelFeed();
        c.setTitle("a title");
        c.getOtherElements().addAll(ExtensionUtils.toBloggerAtomElements(ext));
        ItemEntry i = new ItemEntry();
        i.getOtherElements().addAll(ExtensionUtils.toBloggerAtomElements(ext));
        c.addItem(i.setTitle("item title"));
        w.writeChannel(c);
        FeedReader r = new FeedReader(f);
        ChannelFeed c2 = r.readChannel();
        BloggerExtension ext2 = ExtensionUtils.extractBloggerExtension(c2.getOtherElements());
        assertEquals(ext.isConvertLineBreaks(), ext2.isConvertLineBreaks());
        assertEquals(ext.isDraft(), ext2.isDraft());
    }
}
