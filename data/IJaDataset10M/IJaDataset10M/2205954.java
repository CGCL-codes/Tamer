package org.javasimon.callback.calltree;

import org.javasimon.SimonManager;
import org.javasimon.Split;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;

/**
 * Unit test for {@link CallTree} and {@link CallTreeNode}
 *
 * @author gquintana
 */
public class CallTreeTest {

    /**
	 * Logger
	 */
    private static final Logger LOGGER = LoggerFactory.getLogger(CallTreeTest.class);

    /**
	 * Simon name prefix
	 */
    private static final String NAME_PREFIX = "org.javasimon.test.";

    /**
	 * Call tree under test
	 */
    private CallTree callTree = new CallTree() {

        @Override
        public void onRootStopwatchStart(CallTreeNode rootNode, Split split) {
            CallTreeTest.this.rootTreeNode = rootNode;
        }
    };

    /**
	 * Root call tree node
	 */
    private CallTreeNode rootTreeNode;

    /**
	 * Start a stopwatch and append it call stack
	 */
    private Split startStopwatch(String name) {
        Split split = SimonManager.getStopwatch(NAME_PREFIX + name).start();
        callTree.onStopwatchStart(split);
        return split;
    }

    /**
	 * Stop a stopwatch and remove it from call stack
	 */
    private void stopStopwatch(Split split) {
        split.stop();
        callTree.onStopwatchStop(split);
    }

    /**
	 * Test call tree
	 */
    @Test
    public void testStopwatchStartStop() {
        SimonManager.clear();
        Split rootSplit = startStopwatch("root");
        Split child1Split = startStopwatch("child1");
        Split child11Split = startStopwatch("child1.m1");
        stopStopwatch(child11Split);
        Split child12Split = startStopwatch("child1.m2");
        stopStopwatch(child12Split);
        stopStopwatch(child1Split);
        Split child2Split = startStopwatch("child2");
        Split child21Split = startStopwatch("child2.loop");
        stopStopwatch(child21Split);
        Split child22Split = startStopwatch("child2.loop");
        stopStopwatch(child22Split);
        stopStopwatch(child2Split);
        stopStopwatch(rootSplit);
        assertEquals(rootTreeNode.getChildren().size(), 2);
        assertEquals(rootTreeNode.getCounter(), 1);
        CallTreeNode child1Node = rootTreeNode.getChild(NAME_PREFIX + "child1");
        assertEquals(child1Node.getChildren().size(), 2);
        assertEquals(child1Node.getCounter(), 1);
        CallTreeNode child11Node = child1Node.getChild(NAME_PREFIX + "child1.m1");
        assertEquals(child11Node.getCounter(), 1);
        CallTreeNode child12Node = child1Node.getChild(NAME_PREFIX + "child1.m2");
        assertEquals(child12Node.getCounter(), 1);
        CallTreeNode child2Node = rootTreeNode.getChild(NAME_PREFIX + "child2");
        assertEquals(child2Node.getChildren().size(), 1);
        CallTreeNode child21Node = child2Node.getChild(NAME_PREFIX + "child2.loop");
        assertEquals(child21Node.getCounter(), 2);
        LOGGER.debug(rootTreeNode.toString());
    }
}
