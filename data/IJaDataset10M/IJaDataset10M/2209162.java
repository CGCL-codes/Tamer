package org.newsml.toolkit.dom.unittests;

import java.io.IOException;
import junit.framework.TestCase;
import org.newsml.toolkit.Topic;
import org.newsml.toolkit.TopicSet;

public class TopicSetTest extends TestCase {

    public TopicSetTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
        sample = SampleFactory.createNewsML().getTopicSet(1);
    }

    public void testBase() {
        assertEquals(sample.getXMLName(), "TopicSet");
        assertNotNull(sample.getSession());
    }

    public void testIdentifiers() {
        assertEquals(sample.getDuid().toString(), "vocab.newsitemtype");
        assertEquals(sample.getEuid().toString(), "id1");
    }

    public void testFormalName() {
        assertEquals(sample.getName().toString(), "topicset.e");
    }

    public void testComment() {
        assertEquals(sample.getCommentCount(), 2);
        assertEquals(sample.getComment().length, 2);
        assertNotNull(sample.getComment(0));
        assertNotNull(sample.getComment(1));
        assertNull(sample.getComment(2));
    }

    public void testCatalog() {
        assertNotNull(sample.getCatalog());
    }

    public void testTopicSetRef() {
        assertEquals(sample.getTopicSetRefCount(), 2);
        assertEquals(sample.getTopicSetRef().length, 2);
        assertEquals(sample.getTopicSetRef(0).getRef().toString(), "#vocab.topictype");
        assertEquals(sample.getTopicSetRef(1).getRef().toString(), "http://foo.com/topicset.b");
        assertNull(sample.getTopicSetRef(2));
    }

    public void testTopic() {
        assertEquals(sample.getTopicCount(), 2);
        assertEquals(sample.getTopic().length, 2);
        assertNotNull(sample.getTopic(0));
        assertNotNull(sample.getTopic(1));
        assertNull(sample.getTopic(2));
    }

    public void testFindTopic() throws IOException {
        Topic topic;
        topic = sample.findTopic("other", null, false);
        assertNotNull(topic);
        topic = sample.findTopic("sample", "scheme.a", false);
        assertNotNull(topic);
        topic = sample.findTopic("name.c", "scheme.b", false);
        assertNotNull(topic);
        topic = sample.findTopic("name.d", null, false);
        assertNotNull(topic);
        topic = sample.findTopic("foo", "bar", false);
        assertNull(topic);
    }

    private TopicSet sample;
}
