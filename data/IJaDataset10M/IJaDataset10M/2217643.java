package org.tmapi.core.test;

import java.util.Iterator;
import org.tmapi.core.Locator;
import org.tmapi.core.TMAPIException;
import org.tmapi.core.Topic;
import org.tmapi.core.Association;
import org.tmapi.core.TopicMap;
import org.tmapi.core.TopicName;
import org.tmapi.core.SubjectLocatorClashException;
import org.tmapi.core.ModelConstraintException;
import org.tmapi.core.FeatureNotSupportedException;
import org.tmapi.core.TopicMapSystemFactory;
import org.tmapi.core.TopicMapSystem;

/**
 *     Equality rule: Two topic items are equal if they have:
 *
 *      at least one equal locator item in their [subject identifiers] properties,
 *
 *      at least one equal locator item in their [source locators] properties,
 *
 *      equal locator items in their [subject locator] properties,
 *
 *      an equal locator in the [subject identifiers] property of the one topic item and the [source locators] property of the other, or
 *
 *      the same information item in their [reified] properties.
 *
 */
public class MergeInTest extends TMAPITest {

    public MergeInTest(String name) {
        super(name);
    }

    /** Tests if TopicName Merging is provided by this TMAPI Implementation
        by default */
    public void testBaseNameDefault() throws TMAPIException {
        boolean merge = m_tmSystem.getFeature("http://tmapi.org/features/merge/byTopicName");
        Topic t1 = m_topicMap.createTopic();
        TopicName bn1 = t1.createTopicName("testbn", null);
        TopicMap tm2 = createTopicMap("test2");
        Topic t2 = tm2.createTopic();
        TopicName bn2 = t2.createTopicName("testbn", null);
        TopicName bn22 = t2.createTopicName("testbn2", null);
        m_topicMap.mergeIn(tm2);
        if (merge) {
            assertEquals("Topic with same TopicNames are not merged", 1, m_topicMap.getTopics().size());
            assertEquals("TopicNames are not merged (added) or are not the union", 2, t1.getTopicNames().size());
        } else {
            assertEquals("Topics must not be merged if merge by name is off", 2, m_topicMap.getTopics().size());
        }
        int topicCounter = m_topicMap.getTopics().size();
        TopicMap tm3 = createTopicMap("test3");
        Topic t3 = tm3.createTopic();
        TopicName bn3 = t3.createTopicName("testbn500", null);
        m_topicMap.mergeIn(tm3);
        assertEquals("Topic with different TopicNames are not inserted", ++topicCounter, m_topicMap.getTopics().size());
    }

    /** Tests if TopicName Merging is provided by this TMAPI Implementation */
    public void testBaseNameForce() throws TMAPIException {
        boolean merge = true;
        TopicMapSystemFactory factory = TopicMapSystemFactory.newInstance();
        try {
            factory.setFeature("http://tmapi.org/features/merge/byTopicName", true);
        } catch (FeatureNotSupportedException e) {
            merge = false;
        }
        Iterator itprops = System.getProperties().keySet().iterator();
        while (itprops.hasNext()) {
            String key = (String) itprops.next();
            factory.setProperty(key, System.getProperty(key));
        }
        m_tmSystem = factory.newTopicMapSystem();
        removeAllMaps();
        m_topicMap = createTopicMap(m_baseLocAddress);
        m_baseLoc = m_topicMap.createLocator(m_baseLocAddress);
        Topic t1 = m_topicMap.createTopic();
        TopicName bn1 = t1.createTopicName("testbn", null);
        TopicMap tm2 = createTopicMap("test2");
        Topic t2 = tm2.createTopic();
        TopicName bn2 = t2.createTopicName("testbn", null);
        TopicName bn22 = t2.createTopicName("testbn2", null);
        m_topicMap.mergeIn(tm2);
        if (merge) {
            assertEquals("Topic with same TopicNames are not merged", 1, m_topicMap.getTopics().size());
            assertEquals("TopicNames are not merged (added) or are not the union", 2, t1.getTopicNames().size());
        } else {
            assertEquals("Topics must not be merged if merge by name is off", 2, m_topicMap.getTopics().size());
        }
        int topicCounter = m_topicMap.getTopics().size();
        TopicMap tm3 = createTopicMap("test3");
        Topic t3 = tm3.createTopic();
        TopicName bn3 = t3.createTopicName("testbn500", null);
        m_topicMap.mergeIn(tm3);
        assertEquals("Topic with different TopicNames are not inserted", ++topicCounter, m_topicMap.getTopics().size());
    }

    /** Test if matching is done when 2 Topics have the same SubjectIndicator */
    public void testsubjectIndicator() throws TMAPIException {
        Topic t1 = m_topicMap.createTopic();
        t1.addSubjectIdentifier(m_topicMap.createLocator("sa"));
        TopicMap tm = createTopicMap("test2");
        Topic t2 = tm.createTopic();
        t2.addSubjectIdentifier(tm.createLocator("sa4"));
        m_topicMap.mergeIn(tm);
        assertEquals("Topics with different SubjectIdentifiers are merged", 2, m_topicMap.getTopics().size());
        TopicMap tm3 = createTopicMap("test3");
        Topic t3 = tm3.createTopic();
        t3.addSubjectIdentifier(tm3.createLocator("sa"));
        t3.addSubjectIdentifier(tm3.createLocator("sa5"));
        m_topicMap.mergeIn(tm3);
        assertEquals("Topics with same SubjectIdentifier are not merged", 2, m_topicMap.getTopics().size());
        assertEquals("SubjectIdentifiers are not merged (added) or there is not the union of them", 2, t1.getSubjectIdentifiers().size());
    }

    /** Test if matching is done when 2 Topics have the same SubjectLocator */
    public void testsubjectLocator() throws TMAPIException {
        Topic t1 = m_topicMap.createTopic();
        t1.addSubjectLocator(m_topicMap.createLocator("sa"));
        TopicMap tm = createTopicMap("test2");
        Topic t2 = tm.createTopic();
        t2.addSubjectLocator(tm.createLocator("sa4"));
        m_topicMap.mergeIn(tm);
        assertEquals("Topics with different SubjectLocators are merged or not inserted", 2, m_topicMap.getTopics().size());
        TopicMap tm3 = createTopicMap("test3");
        Topic t3 = tm3.createTopic();
        t3.addSubjectLocator(tm3.createLocator("sa"));
        m_topicMap.mergeIn(tm3);
        assertEquals("Topics with same SubjectLocator are not merged", 2, m_topicMap.getTopics().size());
        assertEquals("Topics with same SubjectLocator are merged but now they have duplicated source locators", 1, t1.getSubjectLocators().size());
        Topic t4 = m_topicMap.createTopic();
        t4.createTopicName("testbn", null);
        t4.mergeIn(t1);
        assertEquals("Topics has to be merged and subject Locator has to be added", 1, t4.getSubjectLocators().size());
    }

    /** Test if matching is done when 2 Topics have the same SourceLocators */
    public void testsourceLocator() throws TMAPIException {
        Topic t1 = m_topicMap.createTopic();
        int t1SrcLocCount = t1.getSourceLocators().size();
        t1.addSourceLocator(m_topicMap.createLocator("sa"));
        TopicMap tm = createTopicMap("test2");
        Topic t2 = tm.createTopic();
        t2.addSourceLocator(tm.createLocator("sa4"));
        m_topicMap.mergeIn(tm);
        assertEquals("Topics with different SourceLocator are merged", 2, m_topicMap.getTopics().size());
        TopicMap tm3 = createTopicMap("test3");
        Topic t3 = tm3.createTopic();
        int t3SrcLocCount = t3.getSourceLocators().size();
        t3.addSourceLocator(tm3.createLocator("sa"));
        m_topicMap.mergeIn(tm3);
        assertEquals("Topics with same SourceLocator are not merged", 2, m_topicMap.getTopics().size());
        assertEquals("SourceLocator are not merged (added) or there is not the union of them", t1SrcLocCount + t3SrcLocCount + 1, t1.getSourceLocators().size());
    }

    /** Tests if one SubjectIdentifier matches to one SourceLocator */
    public void testSubjectIdentifierSourcelocators() throws TMAPIException {
        Topic t1 = m_topicMap.createTopic();
        int count = m_topicMap.getTopics().size();
        t1.addSourceLocator(m_topicMap.createLocator("http://www.tmapi.org/mergeInTest#foo"));
        TopicMap tm = createTopicMap("test4");
        Topic t2 = tm.createTopic();
        Locator loc = tm.createLocator("http://www.tmapi.org/subjectAdress");
        t2.addSubjectLocator(loc);
        t2.addSubjectIdentifier(tm.createLocator("http://www.tmapi.org/mergeInTest#foo"));
        m_topicMap.mergeIn(tm);
        assertEquals("Topics with same SourceLocator<->SubjectIdentifier (subjectIndicator) are not merged 1", count, m_topicMap.getTopics().size());
        assertEquals("Topics with same SourceLocator<->SubjectIdentifier (subjectIndicator) are not merged 2", loc, t1.getSubjectLocators().iterator().next());
    }

    public void testAssocRolesMerged() throws Exception {
        Topic t1 = m_topicMap.createTopic();
        Topic t2 = m_topicMap.createTopic();
        Topic t3 = m_topicMap.createTopic();
        Association a = m_topicMap.createAssociation();
        a.createAssociationRole(t2, null);
        a.createAssociationRole(t3, null);
        t1.mergeIn(t2);
        assertEquals("topic must now play a role after merging", 1, t1.getRolesPlayed().size());
    }

    public void testTypesMerged() throws Exception {
        Topic t1 = m_topicMap.createTopic();
        Topic t2 = m_topicMap.createTopic();
        Topic t3 = m_topicMap.createTopic();
        t2.addType(t3);
        t1.mergeIn(t2);
        assertTrue("topic must have a type now", t1.getTypes().contains(t3));
    }

    /**
     * Test if a subject locator clash is recognized by the topicmap processor.
     *
     * @todo This test should be run with two configurations:
     *       One with XTM 1.1 enabled and one with XTM 1.0 enabled.
     *       Currently this test is using the default model of the TM processor.
     */
    public void testSubjectLocatorClash() throws TMAPIException {
        boolean xtm_1_1 = false;
        try {
            xtm_1_1 = m_tmSystem.getFeature("http://tmapi.org/features/model/xtm1.1");
            Topic t1 = m_topicMap.createTopic();
            Topic t2 = m_topicMap.createTopic();
            t1.addSubjectLocator(m_topicMap.createLocator("subjectA"));
            t2.addSubjectLocator(m_topicMap.createLocator("subjectB"));
            try {
                t1.mergeIn(t2);
                if (!xtm_1_1) fail("XTM 1.1 not supported, yet SubjectLocatorClashException is not thrown.");
            } catch (ModelConstraintException e) {
                if (xtm_1_1) throw e;
            }
            if (xtm_1_1) assertEquals("Unexpected subject locator size after merging (XTM 1.1 model)", 2, t1.getSubjectLocators().size());
        } catch (SubjectLocatorClashException ex) {
            if (!xtm_1_1) {
                assertEquals("Unexpected topic count in SubjectLocatorClashException.getTopics()", 2, ex.getTopics().size());
            } else {
                fail("A topic map processor that is using the XTM 1.1 model MUST never throw a SubjectLocatorClashException");
            }
        }
    }
}
