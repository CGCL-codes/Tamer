package module;

public class MessageDeliveryException extends Exception {

    MessageDeliveryException(Throwable cause) {
        super(cause);
    }

    MessageDeliveryException(Module mod, Message msg, Throwable cause) {
        super("mod" + mod + ",msg=" + msg, cause);
    }

    MessageDeliveryException(Module mod, Message msg, String str) {
        super("mod" + mod + ",msg=" + msg + ": " + str);
    }

    MessageDeliveryException(String message, Throwable cause) {
        super(message, cause);
    }
}
