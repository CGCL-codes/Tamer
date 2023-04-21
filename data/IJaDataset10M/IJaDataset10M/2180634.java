package org.fpse.forum.javaranch.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import junit.framework.TestCase;
import org.fpse.forum.Forum;
import org.fpse.forum.ForumConfiguration;
import org.fpse.forum.determinstic.DeterministicForumTopicArea;
import org.fpse.forum.determinstic.DetermisticForumQuestion;
import org.fpse.forum.javaranch.JavaRanchForumConfiguration;
import org.fpse.parser.HTMLParser;
import org.fpse.parser.UTF8InputStream;
import org.fpse.parser.javaranch.PostParser;
import org.fpse.topic.Post;
import org.jdom.Document;

public class TestJavaRanchPost extends TestCase {

    InputStream m_in;

    public void setUp() throws Exception {
        super.setUp();
        String name = getClass().getName();
        int idx = name.lastIndexOf('.');
        if (idx > 0) name = name.substring(0, idx);
        name = name.replace('.', '/');
        name = name + "/Post";
        m_in = getClass().getResourceAsStream(name);
        if (null == m_in) m_in = ClassLoader.getSystemResourceAsStream(name);
        if (null == m_in) throw new IllegalStateException("Could not locate Topic Area.");
        m_in = new UTF8InputStream(m_in, "ISO-8859-1");
    }

    public void testTopicAreas() throws Exception {
        ForumConfiguration forumConfig = new JavaRanchForumConfiguration("saloon.javaranch.com");
        Forum forum = new Forum("saloon.javaranch.com", "", forumConfig);
        DeterministicForumTopicArea ta = new DeterministicForumTopicArea(forum, "Java in General (advanced)", "34");
        ta.setDatabaseID(Long.valueOf(1));
        DetermisticForumQuestion q = new DetermisticForumQuestion("", ta, "JVM Crash", "Saidur Rahman");
        Document doc = HTMLParser.parse(m_in);
        Iterator<Post> iterator = new PostParser().parse(q, doc);
        int count = 0;
        while (iterator.hasNext()) {
            Post p = iterator.next();
            System.out.println(count + " " + p);
            System.out.println(count + " " + p.getText());
            count++;
        }
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        if (null != m_in) {
            try {
                m_in.close();
            } catch (IOException _) {
            }
        }
    }
}
