package org.ladybug.core.items;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.ladybug.TestDriver;
import org.ladybug.core.lifecycle.Sprint;

/**
 * @author Aurelian Pop
 */
public class SprintTest {

    private TestDriver driver;

    private Sprint sprint;

    private UserStory userStory1;

    private UserStory userStory2;

    @Before
    public void setUp() throws Exception {
        driver = new TestDriver();
        sprint = new Sprint("S1");
        userStory1 = driver.getRelease().get(TestDriver.FEATURE_NAME).get("Hunt marines");
        userStory2 = driver.getRelease().get(TestDriver.FEATURE_NAME).get("Hunt aliens");
    }

    @Test
    public void testUserStories() {
        Assert.assertEquals(0, sprint.size());
        sprint.add(userStory1);
        sprint.add(userStory1);
        Assert.assertEquals(1, sprint.size());
        sprint.add(userStory2);
        Assert.assertEquals(2, sprint.size());
        sprint.remove(userStory1);
        sprint.remove(userStory1);
        Assert.assertEquals(1, sprint.size());
    }

    @Test
    public void testLength() {
        Assert.assertEquals(0, sprint.getLengthInWeeks());
        sprint.setLengthInWeeks(2);
        Assert.assertEquals(2, sprint.getLengthInWeeks());
    }

    @Test
    public void testSprint() {
        Assert.assertNotNull(sprint);
        Assert.assertEquals("S1", sprint.getName());
    }
}
