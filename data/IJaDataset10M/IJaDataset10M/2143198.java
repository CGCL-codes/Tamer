package fr.lelouet.tools.container;

import java.util.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import fr.lelouet.tools.containers.BlockingContainer;

public class BlockingContainerTest {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(BlockingContainerTest.class);

    @Test
    public void testOneStart() {
        logger.trace("trace");
        final String correctString = "good string";
        final BlockingContainer<String> container = new BlockingContainer<String>();
        final List<String> obtainedVals = Collections.synchronizedList(new ArrayList<String>());
        Set<String> threadValues = new HashSet<String>();
        int nbThreads = 5;
        for (int i = 0; i < nbThreads; i++) {
            final String valName = "" + i;
            threadValues.add(valName);
            Runnable toRun = new Runnable() {

                @Override
                public void run() {
                    String val = container.get();
                    Assert.assertEquals(val, correctString);
                    obtainedVals.add(valName);
                }
            };
            new Thread(toRun).start();
        }
        Assert.assertEquals(obtainedVals.size(), 0);
        container.waitFor(nbThreads);
        long startTime = System.currentTimeMillis();
        container.set(correctString);
        long maxWaitingMS = 2000;
        while (obtainedVals.size() < nbThreads) {
            if (System.currentTimeMillis() - startTime > maxWaitingMS) {
                Assert.fail("not enought data received in " + maxWaitingMS + " ms");
            }
        }
        Assert.assertTrue(obtainedVals.containsAll(threadValues));
        Assert.assertTrue(threadValues.containsAll(obtainedVals));
    }

    @Test(dependsOnMethods = "testOneStart")
    public void testTwoStart() {
        final String correctString = "good string";
        final BlockingContainer<String> container = new BlockingContainer<String>();
        container.set("bad String");
        final List<String> obtainedVals = Collections.synchronizedList(new ArrayList<String>());
        Set<String> threadValues = new HashSet<String>();
        int nbThreads = 5;
        for (int i = 0; i < nbThreads; i++) {
            final String valName = "" + i;
            threadValues.add(valName);
            Runnable toRun = new Runnable() {

                @Override
                public void run() {
                    String val = container.get();
                    Assert.assertEquals(val, correctString);
                    obtainedVals.add(valName);
                }
            };
            new Thread(toRun).start();
        }
        Assert.assertEquals(obtainedVals.size(), 0);
        container.waitFor(nbThreads);
        long startTime = System.currentTimeMillis();
        container.set(correctString);
        long maxWaitingMS = 2000;
        while (obtainedVals.size() < nbThreads) {
            if (System.currentTimeMillis() - startTime > maxWaitingMS) {
                Assert.fail("not enought data received in " + maxWaitingMS + " ms");
            }
        }
        Assert.assertTrue(obtainedVals.containsAll(threadValues));
        Assert.assertTrue(threadValues.containsAll(obtainedVals));
    }
}
