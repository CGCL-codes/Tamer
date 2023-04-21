package giftoapng;

import util.ByteBuilder;

/**
 * Frame-control-chunk: width&height of related image, offset, delay-time, dispose and blend-method
 * For more information look here: <a href="http://wiki.mozilla.org/APNG_Specification">APNG-specifcation</a> 
 * @author Alexander Sch&auml;ffer
 *
 */
public class ChunkfcTL extends Chunk {

    private int miSequenceNumber;

    private int miWidth;

    private int miHeight;

    private int miX_Offset;

    private int miY_Offset;

    private short msDelayNum;

    private short msDelayDen;

    private byte mbDisposeOp;

    private byte mbBlendOp;

    public static final short APNG_DEF_DELAY_DEN = 100;

    public static final byte APNG_BLEND_OP_SOURCE = 0;

    public static final byte APNG_BLEND_OP_OVER = 1;

    public ChunkfcTL(int isequenceNumer, int iwidth, int iheight, int ix_offset, int iy_offset, int idelaytime, int idisposalmethod, int iblendmethod) {
        mbaHeader = new Property("fcTL");
        miSequenceNumber = isequenceNumer;
        miWidth = iwidth;
        miHeight = iheight;
        miX_Offset = ix_offset;
        miY_Offset = iy_offset;
        msDelayNum = (short) idelaytime;
        msDelayDen = APNG_DEF_DELAY_DEN;
        mbDisposeOp = (byte) idisposalmethod;
        mbBlendOp = (byte) iblendmethod;
    }

    @Override
    protected byte[] getData() {
        ByteBuilder bb = new ByteBuilder();
        bb.append(miSequenceNumber);
        bb.append(miWidth);
        bb.append(miHeight);
        bb.append(miX_Offset);
        bb.append(miY_Offset);
        bb.append(msDelayNum);
        bb.append(msDelayDen);
        bb.appendSingle(mbDisposeOp);
        bb.appendSingle(mbBlendOp);
        return bb.getArray();
    }
}
