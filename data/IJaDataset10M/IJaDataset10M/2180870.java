package alesis.fusion.commonparameters;

import java.nio.ByteBuffer;

/**
 *
 * @author jam
 */
public class Tpvl extends TrackCommonParameter {

    private static final int length = 8;

    /**
     *
     * @param trackNo
     */
    public Tpvl(int trackNo) {
        buffer = ByteBuffer.allocate(length);
        setSignature("Tpvl");
    }

    /**
     *
     */
    public Tpvl() {
        this(0);
    }
}
