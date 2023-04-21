package ru.cos.sim.communication.messages;

/**
 * 
 * @author zroslaw
 */
public class AvailableMetersRequest extends AbstractMessage {

    @Override
    public final MessageType getMessageType() {
        return MessageType.REQUEST_AVAILABLE_METERS;
    }
}
