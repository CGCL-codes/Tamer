package net.sf.zcatalog.db.basex;

import java.io.*;
import net.sf.zcatalog.*;
import net.sf.zcatalog.fs.*;
import net.sf.zcatalog.xml.jaxb.Meta;
import org.custommonkey.xmlunit.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author lex
 */
public class MetaRecipientTest extends XMLTestCase {

    static LocalDb db = new LocalDb();

    public MetaRecipientTest() {
        MetaRecipient rcp = (MetaRecipient) db.newRecipient();
        assert (rcp.f != null);
        assert (rcp.mar != null);
        assert (rcp.w != null);
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        LocalDb db = new LocalDb();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     * Test of {@link MetaRecipient#pushParent(net.sf.zcatalog.xml.jaxb.Meta)
     * method.
     */
    @Test
    public void testNewParent() throws Exception {
        String res;
        Meta m = new Meta();
        MetaRecipient rcp = (MetaRecipient) db.newRecipient();
        int i = rcp.id, j = 100;
        assertTrue(i == 0);
        while (j-- > 0) rcp.pushParent(m);
        assertTrue(rcp.id == 100);
        rcp.w.close();
        res = MiscUtils.readText(rcp.f.getPath());
        i = j = 0;
        while ((i = res.indexOf(":Obj id=", i)) > 0) {
            i += 8;
            j++;
        }
        assertTrue(j == 100);
    }

    /**
     * Test of {@link MetaRecipient#popParent() method}.
     */
    @Test
    public void testCloseParent() throws Exception {
        String res;
        MetaRecipient rcp = (MetaRecipient) db.newRecipient();
        int i, j = 100;
        while (j-- > 0) rcp.popParent();
        rcp.w.close();
        res = MiscUtils.readText(rcp.f.getPath());
        i = j = 0;
        while ((i = res.indexOf("Obj>", i)) > 0) {
            i += 4;
            j++;
        }
        assertTrue(j == 100);
    }

    /**
     * Test of {@link MetaRecipient#addChild(net.sf.zcatalog.xml.jaxb.Meta) method.
     */
    @Test
    public void testNewChild() throws Exception {
        String res;
        MetaRecipient rcp = (MetaRecipient) db.newRecipient();
        int i, j = 100;
        while (j-- > 0) rcp.addChild(new Meta());
        rcp.w.close();
        i = j = 0;
        res = MiscUtils.readText(rcp.f.getPath());
        while ((i = res.indexOf("meta", i)) > 0) {
            i += 4;
            j++;
        }
        assertTrue(j == 100);
    }

    /**
     * Test of {@link MetaRecipient#commit(net.sf.zcatalog.ProgressObserver)}
     * method.
     */
    @Test
    public void testCommit() throws Exception {
        String res, out;
        MetaRecipient rcp = (MetaRecipient) db.newRecipient();
        File f = new File(Global.TEST_DIR.getPath() + File.separatorChar + "metarecipient_basex");
        Traverser trav = Traverser.getTraverser(f);
        trav.enumerate(new TraverserObserverStubDummy());
        trav.traverse(rcp, new TraverserProfile(), new TraverserObserverStubDummy());
        res = MiscUtils.readText(MetaRecipientTest.class, "metarecipient.xml");
        rcp.commit(new TraverserObserverStubDummy());
        out = MiscUtils.readText(rcp.f.getPath());
        assertXMLEqual(res, out);
    }
}
