package com.road.model;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import java.util.Date;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author Leopard Liu
 * 
 */
@Test(groups = { "unit" })
public class TestPasserby {

    private Passerby passerby;

    @BeforeMethod(alwaysRun = true)
    public void init() {
        passerby = new Passerby("tester", "test@r0ad.com", "aaaa");
    }

    public void testJoin() {
        Location l = new Location("Nanjing", "Zhongshan Road");
        Date joinTime = new Date();
        PasserbyTrack track = passerby.join(l, joinTime);
        assertEquals(l, passerby.getCurrentLocation());
        assertEquals(new PasserbyTrack(passerby, l, joinTime), track);
    }

    public void testLeave() {
        Location l = new Location("Nanjing", "Zhongshan Road");
        passerby.join(l, new Date());
        passerby.leave();
        assertNull(passerby.getCurrentLocation());
    }

    public void testInitiateTopic() {
        Topic topic = new Topic(passerby, "test topic", new Date());
        passerby.initiateTopic(topic);
        assertTrue(passerby.getInitiatedTopics().contains(topic));
    }

    public void testAddPost() {
        Topic topic = new Topic(passerby, "test topic", new Date());
        Post post = new Post(passerby, "", topic, new Date());
        passerby.addPost(post);
        assertTrue(passerby.getPosts().contains(post));
    }
}
