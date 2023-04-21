package transport.message;

import transport.InvalidMessageException;
import transport.Message;
import transport.MessageFactory;
import transport.ParserContext;

/**
 *
 * @author rem
 */
public class HearMessage extends Message {

    int time;

    String sender;

    String message;

    public HearMessage(ParserContext context) {
        super("hear");
        time = Integer.parseInt(Message.nextTerm(context));
        sender = Message.nextTerm(context.inc());
        message = Message.nextTerm(context.inc());
        if (context.next() != ')') {
            throw new InvalidMessageException("Missing ) in message: " + context);
        }
    }

    public int getTime() {
        return time;
    }

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "message " + time + " " + sender + " " + message;
    }
}
