package org.objectstyle.cayenne;

import java.util.Map;
import org.objectstyle.art.Artist;
import org.objectstyle.art.Painting;
import org.objectstyle.art.PaintingInfo;

public class CDOOne2OneDepTst extends CayenneDOTestBase {

    public void test2Null() throws Exception {
        Artist a1 = newArtist();
        Painting p1 = newPainting();
        p1.setToArtist(a1);
        ctxt.commitChanges();
        ctxt = createDataContext();
        Painting p2 = fetchPainting();
        assertNull(p2.getToPaintingInfo());
    }

    public void testReplaceNull() throws Exception {
        Artist a1 = newArtist();
        Painting p1 = newPainting();
        p1.setToArtist(a1);
        ctxt.commitChanges();
        ctxt = createDataContext();
        Painting p2 = fetchPainting();
        p2.setToPaintingInfo(null);
        assertNull(p2.getToPaintingInfo());
    }

    public void testNewAdd() throws Exception {
        Artist a1 = newArtist();
        PaintingInfo pi1 = newPaintingInfo();
        Painting p1 = newPainting();
        p1.setToArtist(a1);
        p1.setToPaintingInfo(pi1);
        assertSame(pi1, p1.getToPaintingInfo());
        assertSame(p1, pi1.getPainting());
        ctxt.commitChanges();
        ctxt = createDataContext();
        Painting p2 = fetchPainting();
        PaintingInfo pi2 = p2.getToPaintingInfo();
        assertNotNull(pi2);
        assertEquals(textReview, pi2.getTextReview());
    }

    public void testTakeObjectSnapshotDependentFault() throws Exception {
        Artist a1 = newArtist();
        PaintingInfo pi1 = newPaintingInfo();
        Painting p1 = newPainting();
        p1.setToArtist(a1);
        p1.setToPaintingInfo(pi1);
        ctxt.commitChanges();
        ctxt = createDataContext();
        Painting painting = fetchPainting();
        assertTrue(painting.readPropertyDirectly("toPaintingInfo") instanceof Fault);
        Map snapshot = ctxt.currentSnapshot(painting);
        assertEquals(paintingName, snapshot.get("PAINTING_TITLE"));
        assertTrue(painting.readPropertyDirectly("toPaintingInfo") instanceof Fault);
    }
}
