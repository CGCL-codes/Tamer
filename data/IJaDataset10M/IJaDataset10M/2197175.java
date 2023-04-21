package jaco.mp3.player.resources;

/**
 * The <code>DecoderException</code> represents the class of
 * errors that can occur when decoding MPEG audio. 
 * 
 * @author MDM
 */
public class DecoderException extends JavaLayerException implements DecoderErrors {

    private int errorcode = UNKNOWN_ERROR;

    public DecoderException(String msg, Throwable t) {
        super(msg, t);
    }

    public DecoderException(int errorcode, Throwable t) {
        this(getErrorString(errorcode), t);
        this.errorcode = errorcode;
    }

    public int getErrorCode() {
        return errorcode;
    }

    public static String getErrorString(int errorcode) {
        return "Decoder errorcode " + Integer.toHexString(errorcode);
    }
}
