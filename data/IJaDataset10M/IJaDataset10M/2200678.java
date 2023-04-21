package de.jochenbrissier.backyard;

/**
 *will be thrown if a message not will send;
 */
public class SendFailException extends Exception {

    public SendFailException() {
        super("Message send Fails");
    }

    SendFailException(String Message) {
        super("Message send Fails: " + Message);
    }
}
