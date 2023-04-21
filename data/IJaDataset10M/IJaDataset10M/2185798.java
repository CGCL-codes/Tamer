package com.road.model;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author Leopard Liu
 * 
 */
@Test(groups = { "unit" })
public class TestLocation {

    private Location location;

    private Passerby passerby;

    @BeforeMethod(groups = { "unit" })
    public void init() {
        location = new Location("Nanjing", "Zhongshan Road");
        passerby = new Passerby("tester", "test@r0ad.com", "aaaa");
    }

    public void testJoin() {
        location.join(passerby, new Date());
        assertTrue(location.isHeHere(passerby));
    }

    public void testLeave() {
        location.join(passerby, new Date());
        location.leave(passerby);
        assertFalse(location.isHeHere(passerby));
    }

    public void testAddPasserbyTrack() {
        Date joinTime = new Date();
        PasserbyTrack track = passerby.join(location, joinTime);
        location.addPasserbyTrack(track);
        assertTrue(location.getPasserbyTracks().contains(track));
    }

    public void testAddTopicTrack() {
        Topic topic = new Topic(passerby, "test topic", new Date());
        TopicTrack track = topic.join(location, new Date());
        location.addTopicTrack(track);
        assertTrue(location.getTopicTracks().contains(track));
    }

    public void testUniqueName() {
        assertEquals(location.getCity() + "-" + location.getName(), location.uniqueName());
    }

    public void testTopicList() {
        Topic topic1 = new Topic(passerby, "topic 1", new Date());
        TopicTrack track1 = topic1.join(location, new Date());
        location.addTopicTrack(track1);
        Topic topic2 = new Topic(passerby, "topic 2", new Date());
        TopicTrack track2 = topic2.join(location, new Date());
        location.addTopicTrack(track2);
        List<Topic> topicList = new ArrayList<Topic>();
        topicList.add(topic1);
        topicList.add(topic2);
        assertTrue(location.topicList().containsAll(topicList));
    }

    public void testCurrentPasserby() {
        Passerby passerby2 = new Passerby("tester2", "tester2@r0ad.com", "aaaa");
        location.join(passerby, new Date());
        location.join(passerby2, new Date());
        HashSet<Passerby> passerbySet = new HashSet<Passerby>();
        passerbySet.add(passerby);
        passerbySet.add(passerby2);
        assertTrue(location.currentPasserbys().containsAll(passerbySet));
    }

    public void testIsHeHere() {
        assertFalse(location.isHeHere(passerby));
        location.join(passerby, new Date());
        assertTrue(location.isHeHere(passerby));
    }
}
