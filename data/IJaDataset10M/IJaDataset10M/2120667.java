package net.sf.fb4j.tests;

import net.sf.fb4j.FacebookRuntimeException;
import net.sf.fb4j.model.FeedStory;
import org.junit.Test;

public class TestFeed extends SessionTestBase {

    @Test
    public void publishStory() throws Exception {
        FeedStory s = new FeedStory("<b>HELLO!</b>");
        s.setTitle("Test story title!");
        s.setBody("Welcome <fb:name uid=\"" + getLong("uid") + "\"/>");
        if (session.publishStory(s)) {
            log.debug("PUBLISHED!!!");
        } else {
            throw new FacebookRuntimeException("OOOOPS!");
        }
    }

    @Test
    public void publishAction() {
    }

    @Test
    public void publishActionWithTemplate() {
    }
}
