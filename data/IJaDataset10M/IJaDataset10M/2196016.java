package org.tn5250j.tools.encoder;

/**
 * This class is an exception that is raised by Encode or one of it's
 * subclasses.  It may also be subclassed for exceptions thrown by subclasses
 * of Encode. It represents any problem encountered while encoding an image.
 * The message is used to state the type of error.
*/
public class EncoderException extends Exception {

    /**
    * Creates an exception with the given message.
    */
    public EncoderException(String msg) {
        super(msg);
    }
}
