package server.protocol;

import static org.junit.Assert.*;
import org.junit.Test;
import server.services.protocol.InputMessageQueue;
import server.services.protocol.ProtocolMessage;

public class InputMessageQueueTest {

    @Test
    public void testIsEmpty() {
        InputMessageQueue emptyQueue = new InputMessageQueue();
        assertTrue(emptyQueue.isEmpty());
        emptyQueue.enqueue(new ProtocolMessage("foo"));
        assertFalse(emptyQueue.isEmpty());
        assertFalse(new InputMessageQueue(new ProtocolMessage("ayt")).isEmpty());
    }

    @Test
    public void testGetSize() {
        InputMessageQueue testQueue = new InputMessageQueue();
        assertEquals(0, testQueue.getSize());
        for (int i = 1; i <= 10; i++) {
            testQueue.enqueue(new ProtocolMessage("foo"));
            assertEquals(i, testQueue.getSize());
        }
        for (int i = 9; i >= 0; i--) {
            testQueue.dequeue();
            assertEquals(i, testQueue.getSize());
        }
    }

    @Test
    public void testEnqueue() {
        InputMessageQueue testQueue = new InputMessageQueue();
        testQueue.enqueue(new ProtocolMessage("1"));
        assertEquals(1, testQueue.getSize());
        for (int i = 2; i <= 10; i++) {
            testQueue.enqueue(new ProtocolMessage(String.valueOf(i)));
            assertEquals(i, testQueue.getSize());
        }
        for (int i = 9; i >= 0; i--) {
            assertEquals(String.valueOf(10 - i), testQueue.dequeue().getMessage());
            assertEquals(i, testQueue.getSize());
        }
        testQueue.enqueue(new ProtocolMessage("foo"));
        testQueue.enqueue(new ProtocolMessage("bar"));
        testQueue.dequeue();
        testQueue.enqueue(new ProtocolMessage("foo"));
        assertEquals("bar", testQueue.dequeue().getMessage());
        assertEquals("foo", testQueue.dequeue().getMessage());
    }

    @Test
    public void testDequeue() {
        InputMessageQueue testQueue = new InputMessageQueue();
        testQueue.enqueue(new ProtocolMessage("1"));
        assertEquals(1, testQueue.getSize());
        for (int i = 2; i <= 10; i++) {
            testQueue.enqueue(new ProtocolMessage(String.valueOf(i)));
            assertEquals(i, testQueue.getSize());
        }
        for (int i = 9; i >= 0; i--) {
            assertEquals(String.valueOf(10 - i), testQueue.dequeue().getMessage());
            assertEquals(i, testQueue.getSize());
        }
        testQueue.enqueue(new ProtocolMessage("foo"));
        testQueue.enqueue(new ProtocolMessage("bar"));
        testQueue.dequeue();
        testQueue.enqueue(new ProtocolMessage("foo"));
        assertEquals("bar", testQueue.dequeue().getMessage());
        assertEquals("foo", testQueue.dequeue().getMessage());
    }

    @Test
    public void testPriority() {
        InputMessageQueue testQueueNoPriority = new InputMessageQueue();
        assertEquals(1, testQueueNoPriority.getPriority());
        InputMessageQueue testQueue = new InputMessageQueue(InputMessageQueue.MAX_PRIORITY + 1);
        assertEquals(InputMessageQueue.MAX_PRIORITY, testQueue.getPriority());
        testQueue.setPriority(-10);
        assertEquals(0, testQueue.getPriority());
        for (int i = 0; i <= InputMessageQueue.MAX_PRIORITY; i++) {
            testQueue.setPriority(i);
            assertEquals(i, testQueue.getPriority());
        }
    }
}
