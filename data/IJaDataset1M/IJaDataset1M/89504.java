package br.ufsc.inf.guiga.media.codec.video.h264.vcl.mode;

import java.io.OutputStream;
import br.ufsc.inf.guiga.media.codec.video.h264.vcl.FrameBuffer;
import br.ufsc.inf.guiga.media.codec.video.h264.vcl.Macroblock;
import br.ufsc.inf.guiga.media.codec.video.h264.vcl.datatype.MacroblockType;
import br.ufsc.inf.guiga.media.parser.video.YUVFrameBuffer;
import br.ufsc.inf.guiga.media.util.io.H264EntropyOutputStream;

/**
 * This superclass provides common services to encoding modes subclasses.
 * 
 * @author Guilherme Ferreira <guiga@inf.ufsc.br>
 */
public abstract class AbstractEncodingMode implements EncodingMode {

    protected YUVFrameBuffer inputFrameBuffer;

    protected YUVFrameBuffer outputFrameBuffer;

    protected Macroblock macroblock;

    protected MacroblockType mbType;

    /**
     * @param macroblock the {@link Macroblock} to be encoded by this mode.
     * @param mbType subclass provide this field to identify which encoding mode it represents.
     */
    public AbstractEncodingMode(Macroblock macroblock, MacroblockType mbType) {
        this.macroblock = macroblock;
        this.mbType = mbType;
    }

    public void encode(YUVFrameBuffer inFrameBuffer, YUVFrameBuffer outFrameBuffer) {
        this.inputFrameBuffer = inFrameBuffer;
        this.outputFrameBuffer = outFrameBuffer;
        doEncode(inFrameBuffer, outFrameBuffer);
    }

    public int getDistortion() {
        int distortion = 0;
        int x = macroblock.getPixelX();
        int y = macroblock.getPixelY();
        int cx = macroblock.getPixelChromaX();
        int cy = macroblock.getPixelChromaY();
        for (int j = y; j < y + Macroblock.MB_HEIGHT; j++) {
            for (int i = x; i < x + Macroblock.MB_WIDTH; i++) distortion += Math.pow(inputFrameBuffer.getY8bit(i, j) - outputFrameBuffer.getY8bit(i, j), 2);
        }
        for (int j = cy; j < cy + Macroblock.MB_CHROMA_HEIGHT; j++) {
            for (int i = cx; i < cx + Macroblock.MB_CHROMA_WIDTH; i++) {
                distortion += Math.pow(inputFrameBuffer.getCb8bit(i, j) - outputFrameBuffer.getCb8bit(i, j), 2);
                distortion += Math.pow(inputFrameBuffer.getCr8bit(i, j) - outputFrameBuffer.getCr8bit(i, j), 2);
            }
        }
        return distortion;
    }

    public void write(H264EntropyOutputStream outStream) {
        doWrite(outStream);
    }

    public MacroblockType getMbType() {
        return mbType;
    }

    /**
     * @return the {@link Macroblock} which owns this encoding mode.
     */
    public Macroblock getMacroblock() {
        return macroblock;
    }

    /**
     * Subclasses must implement this method to encode the macroblock data.
     * <p>
     * <b>Important:</b>The coded data shall not be placed on the output buffer until the
     * {@link EncodingMode#reconstruct(YUVFrameBuffer)} method on this mode be called. The
     * output buffer is passed here to provide previously coded data from neighbours
     * macroblocks.
     * 
     * @param inFrameBuffer the {@link FrameBuffer} that contains the macroblock data to be
     *            encoded.
     * @param codedFrameBuffer the {@link FrameBuffer} with previously coded macroblocks data
     *            for prediction use only.
     */
    protected abstract void doEncode(YUVFrameBuffer inFrameBuffer, YUVFrameBuffer codedFrameBuffer);

    /**
     * Subclasses must implement this method in order to write their coded data into the
     * stream.
     * 
     * @param outStream the {@link OutputStream} where the macroblock encoded will be write.
     */
    protected abstract void doWrite(H264EntropyOutputStream outStream);
}
