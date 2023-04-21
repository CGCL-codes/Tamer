package net.sf.woko.search;

import net.sf.woko.util.testing.TestEntitySub;
import net.sf.woko.util.testing.WokoTestCase;
import org.compass.core.Compass;
import org.compass.core.CompassCallbackWithoutResult;
import org.compass.core.CompassException;
import org.compass.core.CompassHits;
import org.compass.core.CompassSession;
import org.compass.core.CompassTemplate;
import org.hibernate.Session;

public class CompassUtilTest extends WokoTestCase {

    private static String MSG = "TOF Tunning powered by woko !";

    public void testHighlight1() {
        String plain = "This is a test yeah";
        String highlightedExpected = "This is a <x>test</x> yeah";
        String highlighted = getCompassUtil().highlight("test", plain, null, null);
        System.out.println("expected=" + highlightedExpected);
        System.out.println("highlighted=" + highlighted);
        assertEquals("unexpected result, highlight failed", highlightedExpected, highlighted);
    }

    public void testHighlight2() {
        String plain = "This is a test yeah";
        String highlightedExpected = "This is a <y>test</y> yeah";
        String highlighted = getCompassUtil().highlight("test", plain, "testing", null);
        System.out.println("expected=" + highlightedExpected);
        System.out.println("highlighted=" + highlighted);
        assertEquals("unexpected result, highlight failed", highlightedExpected, highlighted);
    }

    public void testHighlight3() {
        String plain = "This is a test yeah";
        String highlightedExpected = "This is a test yeah";
        String highlighted = getCompassUtil().highlight("crap", plain, "testing", null);
        System.out.println("expected=" + highlightedExpected);
        System.out.println("highlighted=" + highlighted);
        assertEquals("unexpected result, highlight failed", highlightedExpected, highlighted);
    }

    public void testReindexObject() {
        TestEntitySub e;
        final Long id;
        CompassUtil compassUtil = getCompassUtil();
        final String newString = "reindexme";
        try {
            openSessionAndTx();
            e = new TestEntitySub();
            e.setTestString(MSG);
            Session s = getSession();
            s.save(e);
            s.flush();
            getPersistenceUtil().commit();
            id = e.getId();
            assertNotNull("id has not been set", id);
        } finally {
            closeSession();
        }
        try {
            openSessionAndTx();
            e = (TestEntitySub) getSession().get(TestEntitySub.class, id);
            assertNotNull(e);
            System.out.println(e.getId());
            e.setTestString(newString);
            compassUtil.reindex(e);
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("exception caught : " + ex);
        } finally {
            closeSession();
        }
        try {
            openSessionAndTx();
            Compass compass = getCompassUtil().getCompass();
            CompassTemplate compassTemplate = new CompassTemplate(compass);
            compassTemplate.execute(new CompassCallbackWithoutResult() {

                public void doInCompassWithoutResult(CompassSession compassSession) throws CompassException {
                    CompassHits hits = compassSession.find(newString);
                    assertEquals("we don't have exactly one hit", 1, hits.length());
                    TestEntitySub e2 = (TestEntitySub) hits.hit(0).getData();
                    assertEquals("unexpected entity ID", id, e2.getId());
                    assertEquals("unexpected property value", newString, e2.getTestString());
                }
            });
        } finally {
            closeSession();
        }
        try {
            openSessionAndTx();
            e = (TestEntitySub) getSession().load(TestEntitySub.class, id);
            assertNotNull(e);
            assertEquals("unexpected property value", MSG, e.getTestString());
        } finally {
            getSession().delete(e);
            closeSession();
        }
    }
}
