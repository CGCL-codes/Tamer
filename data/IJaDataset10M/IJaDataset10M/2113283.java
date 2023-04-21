package net.sourceforge.basher.impl;

import java.util.ArrayList;
import junit.framework.TestCase;
import net.sourceforge.basher.tasks.ATestTask;

/**
 * @author Johan Lindquist
 */
public class TestTaskInfo extends TestCase {

    public void testEquals() {
        TaskInfo taskInfo1 = new TaskInfo();
        taskInfo1.setTask(new ATestTask());
        taskInfo1.setFollowers(new ArrayList());
        TaskInfo taskInfo2 = new TaskInfo();
        taskInfo2.setTask(new ATestTask());
        taskInfo2.setFollowers(new ArrayList());
        TaskInfo taskInfo3 = new TaskInfo();
        final ATestTask task = new ATestTask("hehe");
        taskInfo3.setTask(task);
        final ArrayList followers = new ArrayList();
        followers.add(new ATestTask("hehe"));
        taskInfo3.setFollowers(followers);
        TaskInfo taskInfo5 = new TaskInfo();
        taskInfo3.setTask(new ATestTask("hehe"));
        taskInfo3.setFollowers(taskInfo3.getFollowers());
        TaskInfo taskInfo4 = new TaskInfo();
        taskInfo4.setTask(taskInfo1.getTask());
        taskInfo3.setFollowers(taskInfo1.getFollowers());
        assertEquals("Bad equals", taskInfo1, taskInfo1);
        assertNotSame("Bad equals", taskInfo1, taskInfo2);
        assertFalse("Bad equals", taskInfo1.equals(taskInfo3));
        assertFalse("Bad equals", taskInfo3.equals(taskInfo5));
        assertTrue("Bad equals", taskInfo1.equals(taskInfo2));
        assertFalse("Bad equals", taskInfo1.equals("fdfd"));
        assertEquals("Bad equals", taskInfo1, taskInfo4);
    }

    public void testHashCode() {
        TaskInfo taskInfo1 = new TaskInfo();
        taskInfo1.setTask(new ATestTask());
        taskInfo1.setFollowers(new ArrayList());
        TaskInfo taskInfo2 = new TaskInfo();
        taskInfo2.setTask(new ATestTask());
        taskInfo2.setFollowers(new ArrayList());
        TaskInfo taskInfo3 = new TaskInfo();
        taskInfo3.setTask(new ATestTask("fred"));
        taskInfo3.setFollowers(new ArrayList());
        assertEquals("Bad hashcode", taskInfo1.hashCode(), taskInfo1.hashCode());
        assertTrue("Bad hashcode", taskInfo1.hashCode() == taskInfo2.hashCode());
        assertFalse("Bad hashcode", taskInfo1.hashCode() == taskInfo3.hashCode());
    }

    public void testAddFollower() {
        TaskInfo taskInfo1 = new TaskInfo();
        taskInfo1.setTask(new ATestTask());
        taskInfo1.setFollowers(new ArrayList());
        assertNotNull("No Followers", taskInfo1.getFollowers());
        assertEquals("Bad follower size", 0, taskInfo1.getFollowers().size());
        final ATestTask task = new ATestTask();
        taskInfo1.addFollower(task);
        assertEquals("Bad follower size", 1, taskInfo1.getFollowers().size());
        assertEquals("Bad following task", task, taskInfo1.getFollowers().get(0));
    }
}
