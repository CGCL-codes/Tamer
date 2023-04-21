package org.xiph.speex;

/**
 * Sideband Codec.
 * This class contains all the basic structures needed by the Sideband
 * encoder and decoder.
 * 
 * @author Marc Gimpel, Wimba S.A. (mgimpel@horizonwimba.com)
 * @version $Revision: 1.1 $
 */
public class SbCodec extends NbCodec {

    /** The Sideband Frame Size gives the size in bits of a Sideband frame for a given sideband submode. */
    public static final int[] SB_FRAME_SIZE = { 4, 36, 112, 192, 352, -1, -1, -1 };

    /** The Sideband Submodes gives the number of submodes possible for the Sideband codec. */
    public static final int SB_SUBMODES = 8;

    /** The Sideband Submodes Bits gives the number bits used to encode the Sideband Submode*/
    public static final int SB_SUBMODE_BITS = 3;

    /** Quadratic Mirror Filter Order */
    public static final int QMF_ORDER = 64;

    /** */
    protected int fullFrameSize;

    /** */
    protected float foldingGain;

    /** */
    protected float[] high;

    /** */
    protected float[] y0, y1;

    /** */
    protected float[] x0d;

    /** */
    protected float[] g0_mem, g1_mem;

    /**
   * Wideband initialisation
   */
    public void wbinit() {
        submodes = buildWbSubModes();
        submodeID = 3;
    }

    /**
   * Ultra-wideband initialisation
   */
    public void uwbinit() {
        submodes = buildUwbSubModes();
        submodeID = 1;
    }

    /**
   * Initialisation
   * @param frameSize
   * @param subframeSize
   * @param lpcSize
   * @param bufSize
   * @param foldingGain
   */
    protected void init(final int frameSize, final int subframeSize, final int lpcSize, final int bufSize, final float foldingGain) {
        super.init(frameSize, subframeSize, lpcSize, bufSize);
        this.fullFrameSize = 2 * frameSize;
        this.foldingGain = foldingGain;
        lag_factor = 0.002f;
        high = new float[fullFrameSize];
        y0 = new float[fullFrameSize];
        y1 = new float[fullFrameSize];
        x0d = new float[frameSize];
        g0_mem = new float[QMF_ORDER];
        g1_mem = new float[QMF_ORDER];
    }

    /**
   * Build wideband submodes.
   * @return the wideband submodes.
   */
    protected static SubMode[] buildWbSubModes() {
        HighLspQuant highLU = new HighLspQuant();
        SplitShapeSearch ssCbHighLbrSearch = new SplitShapeSearch(40, 10, 4, hexc_10_32_table, 5, 0);
        SplitShapeSearch ssCbHighSearch = new SplitShapeSearch(40, 8, 5, hexc_table, 7, 1);
        SubMode[] wbSubModes = new SubMode[SB_SUBMODES];
        wbSubModes[1] = new SubMode(0, 0, 1, 0, highLU, null, null, .75f, .75f, -1, 36);
        wbSubModes[2] = new SubMode(0, 0, 1, 0, highLU, null, ssCbHighLbrSearch, .85f, .6f, -1, 112);
        wbSubModes[3] = new SubMode(0, 0, 1, 0, highLU, null, ssCbHighSearch, .75f, .7f, -1, 192);
        wbSubModes[4] = new SubMode(0, 0, 1, 1, highLU, null, ssCbHighSearch, .75f, .75f, -1, 352);
        return wbSubModes;
    }

    /**
   * Build ultra-wideband submodes.
   * @return the ultra-wideband submodes.
   */
    protected static SubMode[] buildUwbSubModes() {
        HighLspQuant highLU = new HighLspQuant();
        SubMode[] uwbSubModes = new SubMode[SB_SUBMODES];
        uwbSubModes[1] = new SubMode(0, 0, 1, 0, highLU, null, null, .75f, .75f, -1, 2);
        return uwbSubModes;
    }

    /**
   * Returns the size of a frame (ex: 160 samples for a narrowband frame,
   * 320 for wideband and 640 for ultra-wideband).
   * @return the size of a frame (number of audio samples in a frame).
   */
    public int getFrameSize() {
        return fullFrameSize;
    }

    /**
   * Returns whether or not we are using Discontinuous Transmission encoding.
   * @return whether or not we are using Discontinuous Transmission encoding.
   */
    public boolean getDtx() {
        return dtx_enabled != 0;
    }

    /**
   * Returns the excitation array.
   * @return the excitation array.
   */
    public float[] getExc() {
        int i;
        float[] excTmp = new float[fullFrameSize];
        for (i = 0; i < frameSize; i++) excTmp[2 * i] = 2.0f * excBuf[excIdx + i];
        return excTmp;
    }

    /**
   * Returns the innovation array.
   * @return the innovation array.
   */
    public float[] getInnov() {
        return getExc();
    }
}
