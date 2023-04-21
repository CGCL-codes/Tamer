package org.tm4j.topicmap.test;

import org.ozoneDB.UnexpectedException;
import org.tm4j.net.Locator;
import org.tm4j.net.LocatorFactory;
import org.tm4j.net.LocatorFactoryException;
import org.tm4j.net.LocatorResolutionException;
import org.tm4j.test.ConfigurableBackendTest;
import org.tm4j.topicmap.Association;
import org.tm4j.topicmap.BaseName;
import org.tm4j.topicmap.DuplicateObjectIDException;
import org.tm4j.topicmap.Member;
import org.tm4j.topicmap.Occurrence;
import org.tm4j.topicmap.Topic;
import org.tm4j.topicmap.TopicMap;
import org.tm4j.topicmap.TopicMapFactory;
import org.tm4j.topicmap.TopicMapObject;
import org.tm4j.topicmap.TopicMapProcessingException;
import org.tm4j.topicmap.TopicMapProvider;
import org.tm4j.topicmap.TopicMapRuntimeException;
import org.tm4j.topicmap.Variant;
import org.tm4j.topicmap.VariantName;
import org.tm4j.topicmap.utils.TopicMapWalker;
import org.tm4j.topicmap.utils.XTMWriter;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;
import java.beans.PropertyVetoException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class TopicMapTest extends ConfigurableBackendTest {

    TopicMap tm;

    TopicMapFactory m_factory;

    LocatorFactory m_locFactory;

    public TopicMapTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
        tm = createTopicMap();
        m_factory = tm.getFactory();
        m_locFactory = tm.getLocatorFactory();
    }

    public void testAddTopic() {
        System.out.println("testAddTopic()");
        int numTopics = tm.getTopicCount();
        Topic t;
        try {
            assertTrue("Expected " + String.valueOf(numTopics) + " topics. Found " + String.valueOf(tm.getTopicCount()) + " topics.", tm.getTopicCount() == numTopics);
            t = tm.createTopic("test-one");
            assertTrue("Expected topic count to increment from " + String.valueOf(numTopics) + " after createTopic()", tm.getTopicCount() == (numTopics + 1));
            Topic t2 = tm.createTopic("test-two");
            assertTrue(tm.getTopicCount() == (numTopics + 2));
            t.destroy();
            assertEquals("Unexpected count after topic removal.", 1, tm.getTopicCount());
            t2.destroy();
            assertEquals("Unexpected count after second topic removal.", 0, tm.getTopicCount());
        } catch (Exception ex) {
            fail("Caught unexpected exception" + ex.getMessage());
        }
        System.gc();
    }

    public void testDuplicateTopicException() {
        System.out.println("testDuplicateTopicException()");
        try {
            Topic t = tm.createTopic("duplicate");
        } catch (Exception e) {
            fail("Caught Exception while adding first duplicate topic: " + e.toString());
        }
        try {
            Topic s = tm.getTopicByID("duplicate");
            if (s != null) {
                Topic t = tm.createTopic("duplicate");
                fail("Successfully added duplicate topic 'duplicate'");
            } else {
                fail("Can't locate duplicate topic");
            }
        } catch (DuplicateObjectIDException e) {
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Caught unexpected exception: " + ex.toString());
        }
        try {
            Topic t = tm.createTopic("duplicate");
            assertTrue("Successfully created new duplicate topic in the topic map.", false);
        } catch (DuplicateObjectIDException e) {
        } catch (Exception otherExceptions) {
            fail("Caught unexpected exception: " + otherExceptions.getMessage());
        }
        System.gc();
    }

    public void testRemoveTopic() {
        System.out.println("testRemoveTopic()");
        try {
            int numTopics = tm.getTopicCount();
            Topic t = tm.createTopic("remove-me");
            Locator subject = m_locFactory.createLocator("URI", "urn:tm4j.org:remove-me");
            Locator subjectIndicator = m_locFactory.createLocator("URI", "urn:tm4j.org:subject-indicator:remove-me");
            t.setSubject(subject);
            t.addSubjectIndicator(subjectIndicator);
            assertEquals("Expected numTopics to increment after creation", numTopics + 1, tm.getTopicCount());
            t.destroy();
            assertEquals("Expected numTopics to decrement after remove", numTopics, tm.getTopicCount());
            Topic removed = tm.getTopicByID("remove-me");
            assertNull("Expected not to find remove-me by ID after deletion", removed);
            removed = tm.getTopicBySubject(subject);
            assertNull("Expected not to find remove-me by subject after deletion", removed);
            removed = tm.getTopicBySubjectIndicator(subjectIndicator);
            assertNull("Expected not to find remove-me by ID after deletion", removed);
            System.out.println("About to call createTopic");
            t = tm.createTopic("remove-me");
            System.out.println("Create topic returned.");
            removed = tm.getTopicByID("remove-me");
            assertNotNull("Expected to find remove-me by ID after recreated.", removed);
            t.setSubject(subject);
            t.addSubjectIndicator(subjectIndicator);
            removed = tm.getTopicBySubject(subject);
            assertNotNull("Expected to find remove-me by subject after recreated", removed);
            removed = tm.getTopicBySubjectIndicator(subjectIndicator);
            assertNotNull("Expected to find remove-me by subject indicator after recreated", removed);
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Unexpected exception: " + ex.toString());
        }
        System.gc();
    }

    public void testMergeBySubject() {
        System.out.println("testMergeBySubject()");
        int numTopics = tm.getTopicCount();
        try {
            Topic t = tm.createTopic("add-one");
            t.addSourceLocator(tm.getBaseLocator().resolveRelative("#add-one"));
            t.setSubject(m_locFactory.createLocator("URI", "urn:techquila.com:merge-me"));
            assertTrue("Topic count should be incremented", tm.getTopicCount() == (numTopics + 1));
            t = tm.createTopic("add-two");
            t.setSubject(m_locFactory.createLocator("URI", "urn:techquila.com:merge-me"));
            if (tm.getProperty(TopicMapProvider.OPT_STATIC_MERGE).booleanValue()) {
                assertTrue("Expected add-two to be merged with add-one", containsLocator(t.getSourceLocators(), tm.getBaseLocator().resolveRelative("#add-one")));
            } else {
                assertEquals("add-two should be merged with a single topic.", 1, t.getBaseTopic().getMergedTopics().size());
            }
        } catch (Exception ex) {
            fail("Unexpected exception: " + ex.toString());
        }
        System.gc();
    }

    public void testMergeByIndicator() {
        System.out.println("testMergeByIndicator()");
        try {
            int numTopics = tm.getTopicCount();
            Topic t = tm.createTopic("add-three");
            t.addSourceLocator(tm.getBaseLocator().resolveRelative("#add-three"));
            t.addSubjectIndicator(m_locFactory.createLocator("URI", "http://www.techquila.com/test#merge-me"));
            assertTrue("add-three: Topic count should be incremented", tm.getTopicCount() == (numTopics + 1));
            t = tm.createTopic("add-four");
            t.addSourceLocator(tm.getBaseLocator().resolveRelative("#add-four"));
            Locator si = m_locFactory.createLocator("URI", "http://www.techquila.com/test#dont-merge-this-one");
            t.addSubjectIndicator(si);
            assertNotNull("Could not find add-four by subject indicator: " + si.getAddress(), tm.getTopicBySubjectIndicator(si));
            si = m_locFactory.createLocator("URI", "http://www.techquila.com/test#merge-me");
            t.addSubjectIndicator(si);
            assertNotNull("Could not find add-four by subject indicator: " + si.getAddress(), tm.getTopicBySubjectIndicator(si));
            boolean staticMerge = tm.getProperty(TopicMapProvider.OPT_STATIC_MERGE).booleanValue();
            if (staticMerge) {
                assertTrue("add-four should be merged", t.getSourceLocators().size() > 1);
            } else {
                assertTrue("add-four: should be merged", !t.getBaseTopic().getMergedTopics().isEmpty());
            }
            Topic addFour = (Topic) tm.getObjectBySourceLocator(tm.getBaseLocator().resolveRelative("#add-four"));
            Topic addThree = (Topic) tm.getObjectBySourceLocator(tm.getBaseLocator().resolveRelative("#add-three"));
            assertNotNull(addFour);
            assertNotNull(addThree);
            Collection addFourMerged = addFour.getMergedTopics();
            Iterator it = addFourMerged.iterator();
            Topic addFourBase = addFour.getBaseTopic();
            if (staticMerge) {
                assertTrue("Expected add-three to be merged with add-four", containsLocator(addThree.getSourceLocators(), tm.getBaseLocator().resolveRelative("#add-four")));
            } else {
                assertTrue("Expected add-three to be in merged topics of add-four or add-four's base topic to be add-three", addFour.getMergedTopics().contains(addThree) || addFour.getBaseTopic().equals(addThree));
                assertTrue(addThree.getMergedTopics().contains(addFour) || addThree.getBaseTopic().equals(addFour));
            }
            t = tm.createTopic("add-five");
            t.addSourceLocator(tm.getBaseLocator().resolveRelative("#add-five"));
            t.addSubjectIndicator(m_locFactory.createLocator("URI", "http://www.techquila.com/test#dont-merge-this-one"));
            BaseName bn = t.createName(null, "this is a test");
            if (staticMerge) {
                assertTrue("add-five should have been merged", t.getSourceLocators().size() > 1);
            } else {
                assertTrue("add-five: Should be merged.", !t.getBaseTopic().getMergedTopics().isEmpty());
            }
            t = tm.getTopicByID("add-three");
            Collection names = t.getNames();
            assertTrue("Topic add-three should have a base name", names.size() == 1);
            t = tm.createTopic("add-six");
            t.addSourceLocator(tm.getBaseLocator().resolveRelative("#add-six"));
            bn = t.createName(null, "this is a test");
            if (staticMerge) {
                assertTrue("add-six should be merged", t.getSourceLocators().size() > 1);
            } else {
                assertTrue("add-six: Should be merged", !t.getBaseTopic().getMergedTopics().isEmpty());
            }
            Topic t2 = tm.createTopic("add-seven");
            t2.addSourceLocator(tm.getBaseLocator().resolveRelative("#add-seven"));
            bn = t2.createName(null, "this is a test", new Topic[] { t });
            if (staticMerge) {
                it = t2.getSourceLocators().iterator();
                StringBuffer buff = new StringBuffer();
                while (it.hasNext()) {
                    buff.append(((Locator) it.next()).getAddress());
                    buff.append(" ");
                }
                assertTrue("add-seven should not be merged [" + buff.toString() + "]", t2.getSourceLocators().size() == 1);
            } else {
                assertTrue("add-seven: Should not be merged", t2.getBaseTopic().getMergedTopics().isEmpty());
            }
            Topic t3 = tm.createTopic("add-eight");
            t3.addSourceLocator(tm.getBaseLocator().resolveRelative("#add-eight"));
            bn = t3.createName(null, "this is a test", new Topic[] { t });
            if (staticMerge) {
                assertTrue("add-eight should be merged with add-seven", containsLocator(t3.getSourceLocators(), tm.getBaseLocator().resolveRelative("#add-seven")));
            } else {
                assertTrue("add-eight: Should be merged", !t3.getBaseTopic().getMergedTopics().isEmpty());
                assertTrue("add-eight: Should have merged with add-seven", t3.getBaseTopic().getMergedTopics().contains(t2) || t3.getBaseTopic().equals(t2));
            }
        } catch (LocatorResolutionException ex) {
            fail("Unexpected LocatorResolutionException: " + ex.getMessage());
        } catch (TopicMapProcessingException ex) {
            fail("Unexpected TopicMapProcessingException: " + ex.getMessage());
        } catch (LocatorFactoryException ex) {
            fail("Unexpected exception: " + ex.toString());
        } catch (PropertyVetoException ex) {
            fail("Unexcpected PropertyVetoException: " + ex.toString());
        }
        System.gc();
    }

    public void testMergedTopicSubjectClash() {
        System.out.println("testMergedTopicSubjectClash()");
        Topic t;
        BaseName bn;
        try {
            t = tm.createTopic("add-nine");
            bn = t.createName(null, "merge");
            t.setSubject(m_locFactory.createLocator("URI", "urn:merge-ok"));
        } catch (Exception ex) {
            fail("Unexpected exception while adding add-nine: " + ex.toString());
        }
        try {
            t = tm.createTopic("add-ten");
            assertNotNull(t);
            Locator loc = m_locFactory.createLocator("URI", "urn:merge-clash");
            t.setSubject(loc);
            bn = t.createName(null, "merge");
            fail("Expected MergedTopicSubjectClash while adding add-ten");
        } catch (TopicMapRuntimeException e) {
            Throwable cause = e.getCause();
            System.out.println("Caught TMRuntimeException: " + e.toString());
            if (cause == null) {
                e.printStackTrace();
                fail("Expected runtime exception to be wrapping a MergedTopicSubjectClashException.");
            }
            assertTrue("Expected cause to be a MergedTopicSubjectClashException but got: " + cause.toString(), cause instanceof org.tm4j.topicmap.MergedTopicSubjectClashException);
        } catch (TopicMapProcessingException e2) {
            fail("Unexpected TopicMapProcessingException while adding add-ten" + e2.toString());
        } catch (LocatorFactoryException ex) {
            fail("Unexpected InvalidLocatorException while adding add-ten. " + ex.toString());
        } catch (PropertyVetoException ex) {
            fail("Unexcpected PropertyVetoException: " + ex.toString());
        } catch (UnexpectedException ex) {
            String msg = ex.getMessage();
            if (!(ex.getMessage().startsWith("org.tm4j.topicmap.MergedTopicSubjectClashException"))) {
                fail("Unexpected Exception: " + ex.toString());
            }
        }
        System.gc();
    }

    public void testMergeBySubjectIndicatorIsSourceLocator() throws Exception {
        TopicMap tm = createTopicMap();
        Locator loc = tm.getLocatorFactory().createLocator("URI", "http://www.tm4j.org/test.xtm#foo");
        Locator subjLoc = tm.getLocatorFactory().createLocator("URI", "http://www.tm4j.org/index.html");
        Topic t = tm.createTopic("foo");
        t.addSourceLocator(loc);
        Topic t2 = tm.createTopic("bar");
        t2.setSubject(subjLoc);
        t2.addSubjectIndicator(loc);
        assertEquals("Expected merged t2 to get t1's source locator", 1, t2.getSourceLocators().size());
        assertNotNull("Expected t1 to gain a subject address from the merge", t.getSubject());
        if (!tm.getProperty(TopicMapProvider.OPT_STATIC_MERGE).booleanValue()) {
            assertTrue("Expected t2 to get merged.", (!t2.getMergedTopics().isEmpty()) || (!t.getMergedTopics().isEmpty()));
        }
    }

    public void testRemoveMergedTopic() {
        System.out.println("testRemoveMergedTopic()");
        try {
            int topicCount = tm.getTopicCount();
            Topic t1 = tm.createTopic("remove-merged-1");
            t1.addSourceLocator(tm.getBaseLocator().resolveRelative("#remove-merged-1"));
            Topic t2 = tm.createTopic("remove-merged-2");
            t2.addSourceLocator(tm.getBaseLocator().resolveRelative("#remove-merged-2"));
            Locator loc = m_locFactory.createLocator("URI", "urn:tm4j:remove-merged");
            t1.setSubject(loc);
            t2.setSubject(loc);
            if (tm.getProperty(TopicMapProvider.OPT_STATIC_MERGE).booleanValue()) {
                assertTrue("Expected t1 and t2 to be merged", containsLocator(t1.getSourceLocators(), tm.getBaseLocator().resolveRelative("#remove-merged-2")) && containsLocator(t1.getSourceLocators(), tm.getBaseLocator().resolveRelative("#remove-merged-1")));
                t1.destroy();
                Topic t = (Topic) tm.getObjectBySourceLocator(tm.getBaseLocator().resolveRelative("#remove-merged-2"));
                assertNull("Expected remove-merged-2 to be destroyed after destroying statically merged topics.", t);
            } else {
                assertEquals("Expected t1 and t2 to be merged. ", 1, t1.getMergedTopics().size());
                assertTrue("Expected t1 to be in the merged topic set of t2", t2.getMergedTopics().contains(t1) || t2.getBaseTopic().equals(t1));
                assertTrue("Expected t2 to be in the merged topic set of t1", t1.getMergedTopics().contains(t2) || t1.getBaseTopic().equals(t2));
                t1.destroy();
                Topic t = tm.getTopicByID("remove-merged-2");
                assertNotNull("Expected to find remove-merged-2 after destroying remove-merged-1", t);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Unexpected exception: " + ex.toString());
        }
        System.gc();
    }

    public void testAssociation() {
        System.out.println("testAssociation()");
        try {
            Topic p1 = tm.createTopic("player-1");
            Topic p2 = tm.createTopic("player-2");
            Association assoc = tm.createAssociation("assoc1");
            Member m1 = assoc.createMember("member1");
            m1.addPlayer(p1);
            Member m2 = assoc.createMember("member2");
            m2.addPlayer(p2);
            assertTrue("Association should have 2 members. Got " + assoc.getMembers().size(), assoc.getMembers().size() == 2);
            assertTrue("p1 should play one role. Got " + p1.getRolesPlayed().size(), p1.getRolesPlayed().size() == 1);
            assertTrue("p2 should play one role. Got " + p2.getRolesPlayed().size(), p2.getRolesPlayed().size() == 1);
            TopicMapObject tmo = tm.getObjectByID("member1");
            assertTrue("m1 not found by ID.", tmo != null);
            assertTrue("Object found by ID member1 is not a Member", tmo instanceof Member);
            tmo = tm.getObjectByID("member2");
            assertTrue("m2 not found by ID.", tmo != null);
            assertTrue("Object found by ID member2 is not a Member", tmo instanceof Member);
        } catch (TopicMapProcessingException ex) {
            fail("Unexpected TopicMapProcessingException while testing association");
        } catch (PropertyVetoException ex) {
            fail("Unexpected PropertyVetoException: " + ex.toString());
        }
        System.gc();
    }

    public void testOccurs() {
        System.out.println("testOccurs()");
        try {
            Topic type = tm.createTopic("occurs-type");
            Topic holder = tm.createTopic("occurs-parent");
            Occurrence occ = holder.createOccurrence("occurs-1");
            occ.setType(type);
            Locator loc = m_locFactory.createLocator("URI", "http://www.techquila.com");
            occ.setDataLocator(loc);
            Occurrence occ2 = holder.createOccurrence("occurs-2");
            occ2.setType(type);
            occ2.setData("42");
            assertTrue("occ2 dataLocator should be null", occ2.getDataLocator() == null);
            assertTrue("occ data should be null", occ.getData() == null);
            assertTrue("occ dataLocator should be 'http://www.techquila.com'", occ.getDataLocator().getAddress().equals("http://www.techquila.com"));
            assertTrue("occ2 data should be 42", occ2.getData().equals("42"));
            Collection occurs = holder.getOccurrences();
            assertTrue(occurs.size() == 2);
            Iterator it = occurs.iterator();
            while (it.hasNext()) {
                Occurrence o = (Occurrence) it.next();
                assertTrue((o.getDataLocator() == null) || (o.getDataLocator().getAddress().equals("http://www.techquila.com")));
                assertTrue((o.getData() == null) || (o.getData().equals("42")));
            }
            TopicMapObject tmo = tm.getObjectByID("occurs-1");
            assertTrue("Can't find occurs-1", tmo != null);
            assertTrue("Expected object occurs-1 to be an occurrence.", tmo instanceof Occurrence);
            tmo = tm.getObjectByID("occurs-2");
            assertTrue("Can't find occurs-2", tmo != null);
            assertTrue("Expected object occurs-2 to be an occurrence.", tmo instanceof Occurrence);
        } catch (TopicMapProcessingException ex) {
            fail("Unexpected TopicMapProcessingException while testing occurrence");
        } catch (LocatorFactoryException ex) {
            fail("Unexpected InvalidLocatorException while testing occurrence");
        } catch (PropertyVetoException ex) {
            fail("Unexcpected PropertyVetoException: " + ex.toString());
        }
        System.gc();
    }

    public void testAssociationIndex() {
        System.out.println("testAssociationIndex()");
        try {
            Association assoc = tm.createAssociation("assoc-1");
            TopicMapObject tmo = tm.getObjectByID("assoc-1");
            assertTrue("Could not find association object.", tmo != null);
            assertTrue("Returned object not an Association", tmo instanceof Association);
            assoc.addSourceLocator(m_locFactory.createLocator("URI", "http://tm4j.org/test/topicmap.xtm#assoc-1-resource"));
            tmo = tm.getObjectBySourceLocator(m_locFactory.createLocator("URI", "http://tm4j.org/test/topicmap.xtm#assoc-1-resource"));
            assertTrue("Could not find association by resource locator", tmo != null);
            assertTrue("Returned object is not an Association.", tmo instanceof Association);
            try {
                assoc.setID("assoc-1-alt");
                fail("Was allowed to alter object ID.");
            } catch (TopicMapRuntimeException ex) {
            }
            tmo = tm.getObjectByID("assoc-1");
            assertTrue("Association object is no longer indexed under old id.", tmo != null);
            assertTrue("Returned object is not an Association.", tmo instanceof Association);
            tmo = tm.getObjectByID("assoc-1-alt");
            assertTrue("Association object is indexed under new id.", tmo == null);
        } catch (TopicMapProcessingException ex) {
            fail("Exception caught: " + ex.getMessage());
        } catch (LocatorFactoryException ex) {
            fail("Unexcpected InvalidLocatorException: " + ex.getMessage());
        } catch (PropertyVetoException ex) {
            fail("Unexcpected PropertyVetoException: " + ex.toString());
        }
        try {
            Association dup = tm.createAssociation("assoc-1");
            fail("Allowed to add duplicate object!");
        } catch (TopicMapProcessingException ex) {
            assertTrue("Expected DuplicateObjectIDException.", ex instanceof DuplicateObjectIDException);
        } catch (PropertyVetoException ex) {
            fail("Unexpected PropertyVetoException: " + ex.toString());
        }
        System.gc();
    }

    public void testSourceLocatorIndex() throws Exception {
        System.out.println("testSourceLocatorIndex()");
        TopicMap tm = getTopicMap("restest1");
        Locator base = tm.getBaseLocator();
        Locator loc = null;
        try {
            loc = base.resolveRelative("#topic.1");
            Topic t1 = (Topic) tm.getObjectBySourceLocator(loc);
            assertNotNull("topic.1 not found with resource locator address " + loc.getAddress(), t1);
            loc = base.resolveRelative("#bn.1");
            assertNotNull("bn.1 not found!", (BaseName) tm.getObjectBySourceLocator(loc));
            loc = base.resolveRelative("#bns.1");
            assertNull("bns.1 was found!", tm.getObjectBySourceLocator(loc));
            loc = base.resolveRelative("#var.1");
            assertNotNull("var.1 not found!", (Variant) tm.getObjectBySourceLocator(loc));
            loc = base.resolveRelative("#varname.1");
            assertNotNull("varname.1 not found!", (VariantName) tm.getObjectBySourceLocator(loc));
            loc = base.resolveRelative("#occ.1");
            assertNotNull("occ.1 not found!", (Occurrence) tm.getObjectBySourceLocator(loc));
            loc = base.resolveRelative("#topic.2");
            System.out.println("Looking for: " + loc.getAddress());
            Topic t2 = (Topic) tm.getObjectBySourceLocator(loc);
            assertNotNull("topic.2 not found!", t2);
            loc = base.resolveRelative("#topic.3");
            Topic t3 = (Topic) tm.getObjectBySourceLocator(loc);
            assertNotNull("topic.3 not found!", t3);
            loc = base.resolveRelative("#topic.4");
            Topic t4 = (Topic) tm.getObjectBySourceLocator(loc);
            assertNotNull("topic.4 not found!", t4);
            loc = base.resolveRelative("#assoc.1");
            Association a1 = (Association) tm.getObjectBySourceLocator(loc);
            assertNotNull("assoc.1 not found!", a1);
            loc = base.resolveRelative("#instof.1");
            assertNull("instof.1 was found!", tm.getObjectBySourceLocator(loc));
            loc = base.resolveRelative("#member.1");
            assertNotNull("member.1 not found!", (Member) tm.getObjectBySourceLocator(loc));
            loc = base.resolveRelative("#member.2");
            assertNotNull("member.2 not found!", (Member) tm.getObjectBySourceLocator(loc));
        } catch (ClassCastException ex) {
            fail("Unexpected object type from locator: " + loc.getAddress());
        } catch (LocatorResolutionException ex) {
            fail("Unexpected locator resolution exception: " + ex.toString());
        }
        System.gc();
    }

    public void testSetTopicMapSourceLocator() {
        try {
            TopicMap tm = createTopicMap();
            tm.addSourceLocator(tm.getBaseLocator().resolveRelative("#some_id"));
            Set resLocs = tm.getSourceLocators();
            assertEquals("Unexpected resourceLocator count for topic map.", 1, resLocs.size());
            boolean foundLoc = false;
            Iterator it = resLocs.iterator();
            while (it.hasNext() && !foundLoc) {
                Locator l = (Locator) it.next();
                if (l.getAddress().endsWith("#some_id")) {
                    foundLoc = true;
                }
            }
            assertTrue("Expected topic map to have one resourceLocator property with an address ending #some_id.", foundLoc);
            assertNotNull("Expected to be able to find topic map by its source locator.", tm.getObjectBySourceLocator(tm.getBaseLocator().resolveRelative("#some_id")));
            XTMWriter writer = new XTMWriter();
            TopicMapIDTester tmidTester = new TopicMapIDTester();
            writer.setContentHandler(tmidTester);
            TopicMapWalker walker = new TopicMapWalker();
            walker.setHandler(writer);
            walker.walk(tm);
            assertTrue("Expected id attribute value of updated topic map to be 'some_id'. Instead id is " + tmidTester.tmID, tmidTester.tmID.equals("some_id"));
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Unexpected exception when setting topic map ID property " + ex.toString());
        }
    }

    public void testDestroyTopicMap() {
        try {
            TopicMap tm = createTopicMap();
            Locator baseLoc = tm.getBaseLocator();
            assertNotNull("Expected a base locator to be created for the topic map.", baseLoc);
            Topic t = tm.createTopic(null);
            tm.destroy();
            TopicMap foundTM = getProvider().getTopicMap(baseLoc);
            assertNull("Expected not to find topic map with base locator of deleted map.", foundTM);
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Unexpected exception: " + ex.toString());
        }
        System.gc();
    }

    /**
     * Test to cover report of NPE when attempting to set a topic type
     * after destroying a topic.
     */
    public void testReplaceDestroyedClass() {
        try {
            TopicMap tm = createTopicMap();
            Topic topic_a = tm.createTopic("a");
            Topic topic_b = tm.createTopic("b");
            Topic topic_c = tm.createTopic("c");
            Topic topic_d = tm.createTopic("d");
            topic_a.addType(topic_b);
            topic_c.destroy();
            topic_a.addType(topic_d);
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Unexpected exception: " + ex.toString());
        }
    }
}

class TopicMapIDTester extends DefaultHandler {

    String tmID = null;

    public void startElement(String nsuri, String localname, String qname, Attributes attrs) {
        if (localname == "topicMap") {
            for (int i = 0; i < attrs.getLength(); i++) {
                if (attrs.getLocalName(i).equals("id")) {
                    tmID = attrs.getValue(i);
                }
            }
        }
    }
}
