package alesis.fusion.commonparameters;

import java.nio.ByteBuffer;

/**
 *
 * @author jam
 */
public class Tflg extends TrackCommonParameter {

    /**
     *
     */
    public static final int length = 8;

    /**
     *
     * @param trackNo
     */
    public Tflg(int trackNo) {
        buffer = ByteBuffer.allocate(length);
        setSignature("Tflg");
    }

    /**
     *
     */
    public Tflg() {
        this(0);
    }
}
