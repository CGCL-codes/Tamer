package com.wpjr.simulator.entity;

import com.wpjr.simulator.entity.Message;
import com.wpjr.simulator.entity.enums.MessageCode;
import com.wpjr.simulator.entity.enums.NodeState;
import com.wpjr.simulator.system.SimulationException;
import com.wpjr.simulator.system.TestNodeSystemAbstract;

public class TestMessage extends TestNodeSystemAbstract {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        try {
            system.startUp(true);
            scheduler.run();
        } catch (SimulationException e) {
            e.printStackTrace();
        }
        Message.restartIdGenerator();
        message = new Message(node.getId(), node2.getId(), MessageCode.GENERIC_DATA_MESSAGE);
    }

    public void testRestartIdGenerator() {
        assertEquals(0, message.getId());
        message = new Message(node, node2.getId(), MessageCode.GENERIC_DATA_MESSAGE);
        assertEquals(1, message.getId());
        Message.restartIdGenerator();
        message = new Message(node.getId(), node2.getId(), MessageCode.GENERIC_DATA_MESSAGE);
        assertEquals(message.getId(), 0);
    }

    private void sendMessage() throws SimulationException {
        assertEquals(NodeState.ONLINE, node.getState());
        assertTrue(node.getNeighbors().contains(node2.getId()));
        assertTrue(node2.getNeighbors().contains(node.getId()));
        assertEquals(0, scheduler.getActions().size());
        message = new Message(node, node2.getId(), MessageCode.GENERIC_DATA_MESSAGE);
        node.getProgram().send(message);
        scheduler.runAll();
    }

    public void testGetLastHop() throws SimulationException {
        int numMessagesQueue = node2.getProgram().getMessageQueue().size();
        sendMessage();
        assertEquals(numMessagesQueue + 1, node2.getProgram().getMessageQueue().size());
        Message m = node2.getProgram().getMessageQueue().peek();
        assertEquals(node2.getId(), m.getLastHop());
    }

    public void testAddProcessedMessageQueue() throws SimulationException {
        int numMessagesQueue = 0;
        Message m = null;
        for (int i = 0; i < 12; i++) {
            numMessagesQueue = node2.getProgram().getMessageQueue().size();
            sendMessage();
            if (numMessagesQueue == 10) {
                numMessagesQueue--;
            }
            assertEquals(numMessagesQueue + 1, node2.getProgram().getMessageQueue().size());
            m = node2.getProgram().getMessageQueue().peek();
            assertEquals(node2.getId(), m.getLastHop());
            assertTrue(node.getProgram().getMessageQueue().size() <= 10);
        }
    }

    public void testSetSource() throws SimulationException {
        sendMessage();
        assertEquals(node.getId(), message.getSource());
    }

    public void testGetSource() throws SimulationException {
        sendMessage();
        assertEquals(node.getId(), message.getSource());
    }

    public void testSetDestination() throws SimulationException {
        sendMessage();
        assertEquals(node2.getId(), message.getDestination());
    }

    public void testGetDestination() throws SimulationException {
        sendMessage();
        assertEquals(node2.getId(), message.getDestination());
    }
}
