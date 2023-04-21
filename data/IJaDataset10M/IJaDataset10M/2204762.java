package ch.epfl.lsr.adhoc.services.statistics;

import ch.epfl.lsr.adhoc.runtime.Message;

/**
 * This class is representing the internal message StatMessage. These messages
 * are never leaving the communication stack. They are only used to transport
 * information retrieved at the StatistcsLayer level; from there they are passed
 * via Dispatcher to the StatisticsService.
 *
 * @see Message
 * @see StatMessageFactory
 *
 * @author Reto Krummenacher
 */
public class StatMessage extends Message {

    /**
   * The type of the message entcountered, thus a field used for statistics
   */
    private char messageType;

    /**
   * The size in byte of the message entcountered
   */
    private int size;

    /**
   * The direction of a passing message.
   * <p>
   * false for (0)ut, true for (1)n.
   */
    private boolean inOut;

    public StatMessage(char type) {
        super(type);
    }

    public char getMessageType() {
        return messageType;
    }

    public int getSize() {
        return size;
    }

    public boolean getIO() {
        return inOut;
    }

    /**
   * This method sets the values of the message
   */
    public void setParameters(char type, int size, boolean inOut) {
        this.messageType = type;
        this.size = size;
        this.inOut = inOut;
    }

    public void prepareData() {
        addChar(messageType);
        addInt(size);
        addBoolean(inOut);
    }

    public void readData() {
        inOut = getBoolean();
        size = getInt();
        messageType = getChar();
    }
}
