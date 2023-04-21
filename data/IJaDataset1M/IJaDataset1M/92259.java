package subethaclipse.net.command.test;

import subethaclipse.net.command.CommandQueue;
import subethaclipse.net.command.ICommand;
import subethaclipse.net.command.NullCommand;
import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * @author Vaughan
 */
public class QueueTest extends TestCase {

    private CommandQueue queue;

    protected void setUp() throws Exception {
        super.setUp();
        queue = new CommandQueue();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testEmpty() {
        Assert.assertTrue("Empty queue is not empty", queue.isEmpty());
        Assert.assertEquals(0, queue.getLength());
    }

    public void testAdd() {
        ICommand command = new NullCommand();
        queue.add(command);
        Assert.assertTrue("Non empty queue is empty", !queue.isEmpty());
        Assert.assertEquals(1, queue.getLength());
    }

    public void testAddException() {
        ICommand command = null;
        try {
            queue.add(command);
            fail("Expected a NullPointerException to be thrown");
        } catch (NullPointerException e) {
        }
    }

    public void testFront() {
        try {
            ICommand command = new NullCommand();
            ICommand front = null;
            queue.add(command);
            front = queue.getFront();
            Assert.assertTrue("Front is not queued command", command == front);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    public void testMultiple() {
        try {
            ICommand firstCommand = new NullCommand();
            ICommand secondCommand = new NullCommand();
            ICommand front = null;
            firstCommand.initialiseSequenceNumber(1);
            secondCommand.initialiseSequenceNumber(2);
            queue.add(firstCommand);
            queue.add(secondCommand);
            Assert.assertEquals(2, queue.getLength());
            front = queue.getFront();
            Assert.assertTrue("Front is not queued command", firstCommand == front);
            queue.remove();
            Assert.assertEquals(1, queue.getLength());
            front = queue.getFront();
            Assert.assertTrue("Front is not queued command", secondCommand == front);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    public void testRemove() {
        try {
            ICommand firstCommand = new NullCommand();
            ICommand secondCommand = new NullCommand();
            ICommand front = null;
            firstCommand.initialiseSequenceNumber(1);
            secondCommand.initialiseSequenceNumber(2);
            queue.add(firstCommand);
            queue.add(secondCommand);
            front = queue.remove();
            Assert.assertTrue("Front is not queued command", firstCommand == front);
            front = queue.remove();
            Assert.assertTrue("Front is not queued command", secondCommand == front);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    public void testPriority() {
        try {
            ICommand firstCommand = new NullCommand();
            ICommand secondCommand = new NullCommand();
            ICommand front = null;
            firstCommand.initialiseSequenceNumber(1);
            secondCommand.initialiseSequenceNumber(2);
            queue.add(secondCommand);
            queue.add(firstCommand);
            front = queue.remove();
            Assert.assertTrue("Front is not highest priority queued command", firstCommand == front);
            front = queue.remove();
            Assert.assertTrue("Front is not queued command", secondCommand == front);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    public void testRepeatsDropped() {
        try {
            ICommand firstCommand = new NullCommand();
            ICommand firstSystemCommand = new NullCommand();
            ICommand secondCommand = new NullCommand();
            ICommand duplicateCommand = new NullCommand();
            ICommand secondSystemCommand = new NullCommand();
            ICommand front = null;
            firstCommand.initialiseSequenceNumber(1);
            firstSystemCommand.initialiseSequenceNumber(ICommand.SEQUENCE_SYSTEM);
            secondCommand.initialiseSequenceNumber(2);
            duplicateCommand.initialiseSequenceNumber(1);
            secondSystemCommand.initialiseSequenceNumber(ICommand.SEQUENCE_SYSTEM);
            queue.add(firstCommand);
            Assert.assertEquals(1, queue.getLength());
            queue.add(firstSystemCommand);
            Assert.assertEquals(2, queue.getLength());
            queue.add(secondCommand);
            Assert.assertEquals(3, queue.getLength());
            queue.add(duplicateCommand);
            Assert.assertEquals(3, queue.getLength());
            queue.add(secondSystemCommand);
            Assert.assertEquals(4, queue.getLength());
            front = queue.remove();
            Assert.assertTrue(front == firstSystemCommand);
            front = queue.remove();
            Assert.assertTrue(front == secondSystemCommand);
            front = queue.remove();
            Assert.assertTrue(front == firstCommand);
            front = queue.remove();
            Assert.assertTrue(front == secondCommand);
        } catch (Exception ex) {
            Assert.fail(ex.getMessage());
        }
    }
}
