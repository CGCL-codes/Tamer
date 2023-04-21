package alesis.fusion.commonparameters;

import java.nio.ByteBuffer;

/**
 *
 * @author jam
 */
public class Tfpg extends TrackCommonParameter {

    private static final int length = 8;

    /**
     *
     * @param trackNo
     */
    public Tfpg(int trackNo) {
        buffer = ByteBuffer.allocate(length);
        setSignature("Tfpg");
    }

    /**
     *
     */
    public Tfpg() {
        this(0);
    }
}
