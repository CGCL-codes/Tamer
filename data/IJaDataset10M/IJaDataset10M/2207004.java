package org.tm4j.topicmap.test;

import org.tm4j.net.Locator;
import org.tm4j.net.LocatorFactoryException;
import org.tm4j.test.ConfigurableBackendTest;
import org.tm4j.topicmap.BaseName;
import org.tm4j.topicmap.Member;
import org.tm4j.topicmap.Topic;
import org.tm4j.topicmap.TopicMap;
import org.tm4j.topicmap.TopicMapProviderException;
import org.tm4j.topicmap.source.SerializedTopicMapSource;
import org.tm4j.topicmap.source.TopicMapSource;
import org.tm4j.topicmap.utils.LTMBuilder;
import org.tm4j.topicmap.utils.LTMParser;
import java.io.File;
import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

public class LTMParserTest extends ConfigurableBackendTest {

    public LTMParserTest(String name) {
        super(name);
    }

    protected TopicMap parseLTMFile(String tmKey, TopicMap tmBase) throws Exception {
        LTMBuilder builder = new LTMBuilder();
        Locator tmLoc = makeLocator(tmKey);
        TopicMapSource tmsrc = new SerializedTopicMapSource(new FileInputStream(getFile(tmKey)), tmLoc, builder);
        TopicMap ret = provider.addTopicMap(tmsrc, tmBase);
        return ret;
    }

    public void testLTMParse() {
        try {
            TopicMap tm = parseLTMFile("ltmparse1", null);
            Topic t1 = (Topic) tm.getObjectBySourceLocator(tm.getBaseLocator().resolveRelative("#topic1"));
            Topic t3 = (Topic) tm.getObjectBySourceLocator(tm.getBaseLocator().resolveRelative("#topic3"));
            Topic broader = (Topic) tm.getObjectBySourceLocator(tm.getBaseLocator().resolveRelative("#broader-term"));
            Topic narrower = (Topic) tm.getObjectBySourceLocator(tm.getBaseLocator().resolveRelative("#narrower-term"));
            Topic assocType = (Topic) tm.getObjectBySourceLocator(tm.getBaseLocator().resolveRelative("#broader-narrower"));
            assertNotNull("Could not find topic 'topic1'", t1);
            assertNotNull("Could not find topic 'topic3'", t3);
            assertNotNull("Could not find topic 'broader'", broader);
            assertNotNull("Could not find topic 'narrower'", narrower);
            assertNotNull("Could not find topic 'broader-narrower'", assocType);
            assertEquals("Expected a single type for topic3", 1, t3.getTypes().size());
            assertTrue("Expected topic1 as type of topic3", t3.getTypes().contains(t1));
            assertEquals("Expected topic1 to play one role", 1, t1.getRolesPlayed().size());
            Member t1Role = null;
            Iterator it = t1.getRolesPlayed().iterator();
            while ((t1Role == null) && (it.hasNext())) {
                Member m = (Member) it.next();
                if (m.getRoleSpec().equals(broader)) {
                    t1Role = m;
                }
            }
            assertNotNull("Could not find 'broader' role played by 'topic1'", t1Role);
            assertEquals("Expected 'topic1' to play 'broader' role in 'broader-narrower' association", assocType, t1Role.getParent().getType());
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Unexpected exception: " + ex.toString());
        }
    }

    public void testPreserveAssignedIDs() throws Exception {
        System.out.println("Test: preserve assigned ids");
        TopicMap tm = parseLTMFile("ltmparse1", null);
        assertNotNull(tm.getObjectByID("topic1"));
        assertNotNull(tm.getObjectByID("topic2"));
        assertNotNull(tm.getObjectByID("topic3"));
    }

    public void testMergeLTMFile() throws Exception {
        String tmKey = "ltm_mergemap_base";
        LTMBuilder builder = new LTMBuilder();
        Locator tmLoc = makeLocator(tmKey);
        TopicMap tmp = provider.getTopicMap(tmLoc);
        if (tmp != null) tmp.destroy();
        TopicMapSource tmsrc = new SerializedTopicMapSource(new FileInputStream(getFile(tmKey)), tmLoc, builder);
        TopicMap ret = provider.addTopicMap(tmsrc);
        Locator baseLoc = ret.getBaseLocator();
        Iterator it = ret.getMergeMapLocators().iterator();
        while (it.hasNext()) {
            Locator mmLoc = (Locator) it.next();
            Topic[] addedThemes = (Topic[]) ret.getMergeMapAddedThemes(mmLoc).toArray(new Topic[1]);
            provider.mergeTopicMap(ret, mmLoc, addedThemes);
            assertTrue("Expected base address to remain as " + baseLoc.getAddress() + " after merge, but it got changed to " + ret.getBaseLocator().getAddress(), ret.getBaseLocator().getAddress().equals(baseLoc.getAddress()));
        }
        assertTrue("Expected base address to remain as " + baseLoc.getAddress() + " after merge, but it got changed to " + ret.getBaseLocator().getAddress(), ret.getBaseLocator().getAddress().equals(baseLoc.getAddress()));
    }

    public void testMergeLTMFileWithDuplicateID() throws Exception {
        String tmKey = "ltm_mergemap_base_2";
        LTMBuilder builder = new LTMBuilder();
        Locator tmLoc = makeLocator(tmKey);
        TopicMap tmp = provider.getTopicMap(tmLoc);
        if (tmp != null) tmp.destroy();
        TopicMapSource tmsrc = new SerializedTopicMapSource(new FileInputStream(getFile(tmKey)), tmLoc, builder);
        TopicMap ret = provider.addTopicMap(tmsrc);
        Locator baseLoc = ret.getBaseLocator();
        Iterator it = ret.getMergeMapLocators().iterator();
        while (it.hasNext()) {
            Locator mmLoc = (Locator) it.next();
            Topic[] addedThemes = (Topic[]) ret.getMergeMapAddedThemes(mmLoc).toArray(new Topic[1]);
            provider.mergeTopicMap(ret, mmLoc, addedThemes);
            assertTrue("Expected base address to remain as " + baseLoc.getAddress() + " after merge, but it got changed to " + ret.getBaseLocator().getAddress(), ret.getBaseLocator().getAddress().equals(baseLoc.getAddress()));
        }
        assertTrue("Expected base address to remain as " + baseLoc.getAddress() + " after merge, but it got changed to " + ret.getBaseLocator().getAddress(), ret.getBaseLocator().getAddress().equals(baseLoc.getAddress()));
        assertEquals(2, ret.getTopicCount());
    }

    public void testRecursiveMerge() throws Exception {
        String tmKey = "ltm_recursive_merge";
        LTMBuilder builder = new LTMBuilder();
        Locator tmLoc = makeLocator(tmKey);
        TopicMap tmp = provider.getTopicMap(tmLoc);
        if (tmp != null) tmp.destroy();
        TopicMapSource tmsrc = new SerializedTopicMapSource(new FileInputStream(getFile(tmKey)), tmLoc, builder);
        TopicMap ret = provider.addTopicMap(tmsrc);
        assertEquals(2, ret.getTopicCount());
    }

    protected void tearDown() throws Exception {
        TopicMap tm = provider.getTopicMap(makeLocator("ltmparse1"));
        if (tm != null) {
            provider.removeTopicMap(tm);
        }
        tm = provider.getTopicMap(locatorFactory.createLocator("URI", "http://some.org/test"));
        if (tm != null) {
            provider.removeTopicMap(tm);
        }
        super.tearDown();
    }

    private Locator makeLocator(String tmKey) throws MalformedURLException, LocatorFactoryException {
        File f = getFile(tmKey);
        URL fileURL = f.toURL();
        Locator tmLoc = locatorFactory.createLocator("URI", fileURL.toString());
        return tmLoc;
    }

    private File getFile(String tmKey) {
        File f = new File(System.getProperty("testdir") + File.separatorChar + getProperty(tmKey));
        return f;
    }
}
