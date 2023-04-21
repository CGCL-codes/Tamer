package org.tmapi.core.test;

import org.tmapi.core.*;

public class MergeDetectionTest extends TMAPITest {

    public MergeDetectionTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
        TopicMapSystemFactory factory = TopicMapSystemFactory.newInstance();
        java.util.Iterator itprops = System.getProperties().keySet().iterator();
        while (itprops.hasNext()) {
            String key = (String) itprops.next();
            factory.setProperty(key, System.getProperty(key));
        }
        factory.setFeature("http://tmapi.org/features/automerge", false);
        m_tmSystem = factory.newTopicMapSystem();
        removeAllMaps();
        m_topicMap = createTopicMap(m_baseLocAddress);
        m_baseLoc = m_topicMap.createLocator(m_baseLocAddress);
    }

    /** Test if matching is done when 2 Topics have the same SubjectIndicator */
    public void testsubjectIndicator() {
        Topic t1 = m_topicMap.createTopic();
        t1.addSubjectIdentifier(m_topicMap.createLocator("sa"));
        Topic t2 = m_topicMap.createTopic();
        try {
            t2.addSubjectIdentifier(m_topicMap.createLocator("sa"));
            fail("TopicsMustMergeException must be thrown cause topic with existing subject Identifier is created");
        } catch (TopicsMustMergeException ex) {
            assertEquals("TopicsMustMergeException.getUnmodifiedTopic() must return t1", t1, ex.getUnmodifiedTopic());
            assertEquals("TopicsMustMergeException.getModifiedTopic() must return t2", t2, ex.getModifiedTopic());
        }
    }

    /** Test if matching is done when 2 Topics have the same SubjectLocator */
    public void testsubjectLocator() {
        Topic t1 = m_topicMap.createTopic();
        t1.addSubjectLocator(m_topicMap.createLocator("sa"));
        Topic t2 = m_topicMap.createTopic();
        try {
            t2.addSubjectLocator(m_topicMap.createLocator("sa"));
            fail("TopicsMustMergeException must be thrown cause topic with existing subject locator is created");
        } catch (TopicsMustMergeException ex) {
            assertEquals("TopicsMustMergeException.getUnmodifiedTopic() must return t1", t1, ex.getUnmodifiedTopic());
            assertEquals("TopicsMustMergeException.getModifiedTopic() must return t2", t2, ex.getModifiedTopic());
        }
    }

    /** Test if matching is done when 2 Topics have the same SourceLocator */
    public void testTopicsourceLocator() throws Exception {
        Topic t1 = m_topicMap.createTopic();
        t1.addSourceLocator(m_topicMap.createLocator("sa"));
        Topic t2 = m_topicMap.createTopic();
        try {
            t2.addSourceLocator(m_topicMap.createLocator("sa"));
            fail("TopicsMustMergeException must be thrown cause topic with existing source locator is created");
        } catch (TopicsMustMergeException ex) {
            assertEquals("TopicsMustMergeException.getUnmodifiedTopic() must return first Topic", t1, ex.getUnmodifiedTopic());
            assertEquals("TopicsMustMergeException.getModifiedTopic() must return second Topic", t2, ex.getModifiedTopic());
        }
    }

    /** Test if matching is done when 2 Topics have the same SourceLocator */
    public void testDuplicatesourceLocator() throws Exception {
        Topic t1 = m_topicMap.createTopic();
        t1.addSourceLocator(m_topicMap.createLocator("sa"));
        TopicName tn = t1.createTopicName("test", null);
        try {
            tn.addSourceLocator(m_topicMap.createLocator("sa"));
            fail("DuplicateSourceLocatorException must be thrown cause TopicMapObject with existing source locator is created");
        } catch (DuplicateSourceLocatorException ex) {
            assertEquals("DuplicateSourceLocatorException.getUnmodifiedTopicMapObject() must return t1", t1, ex.getUnmodifiedTopicMapObject());
            assertEquals("DuplicateSourceLocatorException.getModifiedTopicMapObject() must return tn", tn, ex.getModifiedTopicMapObject());
        }
    }
}
