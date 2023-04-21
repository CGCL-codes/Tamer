package ararat.sound;

/**
 * MessagePlayerException class
 * @author Arman Charif
 * Created on 14-Jun-2004
 */
public class MessagePlayerException extends Exception {

    public MessagePlayerException(String s) {
        super("\n\n\t" + s);
    }

    public MessagePlayerException() {
        super();
    }
}
