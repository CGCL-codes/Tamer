package aigames.soccer.application;

import aigames.soccer.InvalidMoveException;
import aigames.soccer.Move;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import snifos.common.UserId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * This class describes message passed from communication module. It should be
 * compatible with AI Soccer protocol.
 * @version $Id: Message.java,v 1.9 2004/05/08 21:55:27 mwerla Exp $
 */
public class Message {

    /** HELLO message type */
    public static final int HELLO = 1;

    /** MOVE message type */
    public static final int MOVE = 2;

    /** PARAMS message type */
    public static final int PARAMS = 3;

    /** LOG message type */
    public static final int LOG = 4;

    /** GAME_OVER message type */
    public static final int GAME_OVER = 5;

    /** CONFIRMATION message type */
    public static final int CONFIRMATION = 6;

    /** UNKNOWN message type */
    public static final int UNKNOWN = -1;

    private Document message;

    private UserId userId;

    private int messageType = UNKNOWN;

    private int messageId = -1;

    private String messageIdAsString = "-1";

    private String lastValidationReason;

    private Date sendDate = null;

    private int resendCounter;

    private Move move;

    /**
     * Message constructor.
     * @param userId Id of user which sent the message.
     * @param message Sent message.
     */
    public Message(UserId userId, Document message) {
        this.userId = userId;
        this.message = message;
        String temp = message.valueOf("//message/@id");
        if (temp != null) {
            messageId = Integer.parseInt(temp);
            messageIdAsString = temp;
        }
        String type = message.valueOf("//message/@type");
        if ("confirmation".equals(type)) {
            messageType = CONFIRMATION;
        } else if ("move".equals(type)) {
            messageType = MOVE;
            List moves = new ArrayList();
            for (Iterator i = message.getRootElement().elementIterator("type"); i.hasNext(); ) {
                Element singleStep = (Element) i.next();
                moves.add(new Integer(Integer.parseInt(singleStep.getStringValue())));
            }
            try {
                move = new Move((Integer[]) moves.toArray(new Integer[0]));
            } catch (InvalidMoveException e) {
                Logger.getRootLogger().error(e.getMessage(), e);
            }
        } else if ("log".equals(type)) {
            messageType = LOG;
        } else if ("params".equals(type)) {
            messageType = PARAMS;
        } else if ("hello".equals(type)) {
            messageType = HELLO;
        } else if ("game-over".equals(type)) {
            messageType = GAME_OVER;
        }
    }

    /**
     * Returns validation result - can be used after calling
     * {@link Message#validate(Validator)
     * validate(Validator)} method.
     * @return Validation result.
     */
    public String getLastValidationReason() {
        return lastValidationReason;
    }

    /**
     * Returns unique message id.
     * @return Unique message id.
     */
    public int getMessageId() {
        return messageId;
    }

    /**
     * Return message type.
     * @return Message type.
     *
     */
    public int getMessageType() {
        return messageType;
    }

    /**
     * Returns Document with message body.
     * @return Document with message body.
     */
    public Document getMessage() {
        return message;
    }

    /**
     * Validates message with given validator.
     * @param validator Validator that should be used to validate message.
     * @return True if message is valid, otherwise false.
     */
    public boolean validate(Validator validator) {
        boolean validationResult = false;
        lastValidationReason = "Message is not compatible with protocol!";
        try {
            validationResult = validator.validate(message);
        } catch (Exception e) {
            lastValidationReason = e.getMessage();
        }
        return validationResult;
    }

    /**
     * Returns user id of a user which send the message.
     * @return User id.
     */
    public UserId getUserId() {
        return userId;
    }

    /**
     * Returns string representation of user id.
     * @return String representation of user id
     */
    public String getMessageIdAsString() {
        return messageIdAsString;
    }

    /**
     * Returns date when the message was sent.
     * @return Date when the message was sent.
     */
    public Date getSendDate() {
        return sendDate;
    }

    /**
     * Sets message send date.
     * @param sendDate The send date to set.
     */
    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    /**
     * Increases counter which shows how many times message was resend.
     */
    public void incResendCounter() {
        resendCounter++;
    }

    /**
     * Returns move. It should be used only if message type is MOVE.
     * @return Returns the move.
     */
    public Move getMove() {
        return move;
    }
}
