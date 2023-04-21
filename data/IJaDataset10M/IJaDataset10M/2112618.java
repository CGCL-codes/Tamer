package com.wpjr.simulator.entity.program;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import com.wpjr.simulator.entity.Coordinates;
import com.wpjr.simulator.entity.Message;
import com.wpjr.simulator.entity.action.SendHelloMessageAction;
import com.wpjr.simulator.entity.enums.EnergyCost;
import com.wpjr.simulator.entity.enums.MessageCode;
import com.wpjr.simulator.entity.enums.NodeEvent;
import com.wpjr.simulator.entity.enums.NodeState;
import com.wpjr.simulator.entity.enums.NodeSystemEvent;
import com.wpjr.simulator.entity.node.Node;
import com.wpjr.simulator.main.NodeEventInterface;
import com.wpjr.simulator.screen.NSConsole;
import com.wpjr.simulator.system.NodeSystem;
import com.wpjr.simulator.system.Scheduler;
import com.wpjr.util.NodeSystemFactory;
import com.wpjr.util.Validate;

public abstract class ProgramAbstract implements Program {

    private static final int MESSAGE_QUEUE_SIZE = 10;

    protected static final NodeSystem nodeSystem = NodeSystem.getInstance();

    private static int nextId;

    private int id;

    protected Node node;

    protected Queue<Message> messageQueue;

    public ProgramAbstract() {
        id = nextId;
        nextId++;
        messageQueue = new ArrayBlockingQueue<Message>(MESSAGE_QUEUE_SIZE);
    }

    public ProgramAbstract(Node node2) {
        this();
        this.node = node2;
        node.addProgram(this);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Program ? ((ProgramAbstract) obj).getId() == id : false;
    }

    public int getId() {
        return id;
    }

    public void send(Message message) {
        nodeSystem.send(node, message);
        updateNodeStatistics(NodeEvent.NODE_MESSAGE_SENT);
        EnergyCost.TRANSMISSION.updateEnergyLevel(getNode().getId());
    }

    public final void shutDown() {
        if (!node.getState().equals(NodeState.DEAD)) {
            nodeSystem.getStatistics().update(NodeSystemEvent.SYSTEM_DEAD_NODE);
            nodeSystem.updateDeadNode(node);
            node.shutDown();
        }
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getClass().getSimpleName()).append(": ").append(getType());
        return buffer.toString();
    }

    /**
	 * Starts the software sending a hello message.
	 */
    public void bootStepOne() {
        EnergyCost.BOOT.updateEnergyLevel(getNode().getId());
        node.activateNode();
    }

    /**
	 * Starts the software sending a hello message.
	 */
    public void bootStepTwo() {
        Validate.isTrue(node.getState().isWorking(), "The node is not working!!");
        sendHelloMessage();
    }

    public void handleSuspiciousInfoResponseMessage(Message message) {
    }

    public void handleSuspiciousInfoRequestMessage(Message message) {
    }

    public void sendHelloMessage() {
        SendHelloMessageAction sma = new SendHelloMessageAction(node, Scheduler.getInstance().getCurrentTime() + 1);
        Scheduler.getInstance().schedule(sma);
    }

    public void sendHelloBackMessage(Message originMessage) {
        Message message = NodeSystemFactory.createHelloBackMessage(node, originMessage.getSource());
        send(message);
    }

    protected void addNeighbor(int id) {
        updateNodeStatistics(NodeEvent.NODE_NEW_NEIGHBOR);
        node.addNeighbor(id);
    }

    public boolean isNeighbor(int id) {
        return node.getNeighbors().contains(id);
    }

    /**
	 * @return the node
	 */
    public Node getNode() {
        return node;
    }

    protected void showMessageOnConsole(Message message) {
        int destination = 0;
        NSConsole.show(this.getClass().getSimpleName(), "Node " + node.getId() + " received a " + message.getType() + " message from node " + message.getSource());
    }

    /**
	 * @param message
	 */
    protected void processArrivingMessage(Message message) {
        showMessageOnConsole(message);
        processArrivingMessageAtDestination(message);
        message.setLastHop(node.getId());
    }

    private void processArrivingMessageAtDestination(Message message) {
        if (message.getDestination() == node.getId()) {
            NSConsole.show(this.getClass().getSimpleName(), "Message " + message + " arrived at the destination.");
            updateNodeStatistics(NodeEvent.NODE_MESSAGE_RECEIVED_AT_DESTINATION);
        }
    }

    protected void processHelloMessage(Message message) {
        addNeighbor(message.getSource());
        processArrivingMessage(message);
    }

    public boolean process(Message message) {
        if (message.isProcessed()) {
            NSConsole.show(node, "Message has already been processed: " + message);
        } else {
            if (message.getType().equals(MessageCode.HELLO.toString())) {
                NSConsole.show(node, message.toString());
                processHelloMessage(message);
            } else if (message.getType().equals(MessageCode.HELLO_BACK.toString())) {
            } else if (message.getType().equals(MessageCode.GENERIC_DATA_MESSAGE.toString())) {
                processArrivingMessage(message);
            } else if (message.getType().equals(MessageCode.SUSPICIOUS_INFO_REQUEST_MESSAGE.toString())) {
                handleSuspiciousInfoRequestMessage(message);
            } else if (message.getType().equals(MessageCode.SUSPICIOUS_INFO_RESPONSE_MESSAGE.toString())) {
                handleSuspiciousInfoResponseMessage(message);
            }
            message.setProcessed(true);
            addProcessedMessageQueue(message);
        }
        return true;
    }

    private void addProcessedMessageQueue(Message message) {
        if (messageQueue == null) {
            messageQueue = new ArrayBlockingQueue<Message>(MESSAGE_QUEUE_SIZE);
        }
        if (messageQueue.size() >= MESSAGE_QUEUE_SIZE) {
            messageQueue.poll();
        }
        messageQueue.add(message);
    }

    public void updateNodeStatistics(NodeEventInterface event) {
        event.notifyNodeEvent(node.getId());
    }

    public Queue<Message> getMessageQueue() {
        return messageQueue;
    }
}
