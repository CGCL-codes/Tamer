package org.tmapi.index.core.test;

import org.tmapi.core.TMAPIException;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicName;
import org.tmapi.index.core.TopicNamesIndex;

public class TopicNamesIndexTest extends IndexTest {

    public TopicNamesIndexTest(String name) {
        super(name, org.tmapi.index.core.TopicNamesIndex.class);
    }

    public void testgetTopicNamesByValue() {
        try {
            TopicNamesIndex oi = (TopicNamesIndex) m_topicMap.getHelperObject(org.tmapi.index.core.TopicNamesIndex.class);
            Topic t = m_topicMap.createTopic();
            oi.open();
            assertEquals("There cannot be a Topic with this Value in TopicMap", 0, oi.getTopicNamesByValue("tmapiTestTopicName").size());
            TopicName oc = t.createTopicName("tmapiTestTopicName", null);
            oi.reindex();
            assertEquals("There should be a Topic with this one Value in TopicMap", 1, oi.getTopicNamesByValue("tmapiTestTopicName").size());
            assertEquals("The TopicName is not the right one", oc, oi.getTopicNamesByValue("tmapiTestTopicName").iterator().next());
            oc.setValue(null);
            oi.reindex();
            assertEquals("There cannot be a an TopicName with this Value in TopicMap after removal", 0, oi.getTopicNamesByValue("tmapiTestTopicName").size());
        } catch (TMAPIException ex) {
            ex.printStackTrace();
            fail("Unexpected exception: " + ex.toString());
        }
    }

    public void testgetTopicNameTypes() {
        try {
            boolean xtm_1_1 = m_tmSystem.getFeature("http://tmapi.org/features/model/xtm1.1");
            TopicNamesIndex tnIdx = (TopicNamesIndex) m_topicMap.getHelperObject(org.tmapi.index.core.TopicNamesIndex.class);
            Topic t = m_topicMap.createTopic();
            TopicName tn = t.createTopicName("foo", null);
            Topic type = m_topicMap.createTopic();
            tnIdx.open();
            assertEquals("There cannot be a TopicNameType in TopicMap", 0, tnIdx.getTopicNameTypes().size());
            if (!xtm_1_1) {
                return;
            }
            tn.setType(type);
            tnIdx.reindex();
            assertEquals("There should be one TopicNameType in TopicMap", 1, tnIdx.getTopicNameTypes().size());
            assertEquals("The TopicNameType is not the right one", type, tnIdx.getTopicNameTypes().iterator().next());
            tn.setType(null);
            tnIdx.reindex();
            assertEquals("There cannot be a TopicNameType in TopicMap after removal", 0, tnIdx.getTopicNameTypes().size());
        } catch (TMAPIException ex) {
            fail("Unexpected exception: " + ex.toString());
        }
    }

    public void testgetTopicNamesByType() {
        try {
            boolean xtm_1_1 = m_tmSystem.getFeature("http://tmapi.org/features/model/xtm1.1");
            TopicNamesIndex tnIdx = (TopicNamesIndex) m_topicMap.getHelperObject(org.tmapi.index.core.TopicNamesIndex.class);
            Topic t = m_topicMap.createTopic();
            TopicName tn = t.createTopicName("foo", null);
            Topic type = m_topicMap.createTopic();
            tnIdx.open();
            assertEquals("There cannot be a TopicName with this type in TopicMap", 0, tnIdx.getTopicNamesByType(type).size());
            if (!xtm_1_1) {
                return;
            }
            tn.setType(type);
            tnIdx.reindex();
            assertEquals("There should be a Topic with this one type in TopicMap", 1, tnIdx.getTopicNamesByType(type).size());
            assertEquals("The TopicName is not the right one", tn, tnIdx.getTopicNamesByType(type).iterator().next());
            tn.setType(null);
            tnIdx.reindex();
            assertEquals("There cannot be a TopicName with this type in TopicMap after removal", 0, tnIdx.getTopicNamesByType(type).size());
            assertEquals("The should be a TopicName with no type after setting type to null", 1, tnIdx.getTopicNamesByType(null).size());
            assertEquals("The TopicName is not the right one (querying for type == null)", tn, tnIdx.getTopicNamesByType(null).iterator().next());
        } catch (TMAPIException ex) {
            ex.printStackTrace();
            fail("Unexpected exception: " + ex.toString());
        }
    }
}
