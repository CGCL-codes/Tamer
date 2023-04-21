package net.sf.jjmpeg.codec.audio;

import java.io.IOException;
import net.sf.jjmpeg.bitstream.InputBitstream;
import net.sf.jjmpeg.bitstream.OutputBitstream;
import net.sf.jjmpeg.codec.CodecParameterSet;
import net.sf.jjmpeg.codec.InvalidCodecInitializationParameter;

/**
 * @author Derek Whitaker & Trenton Pack
 * @version
 *
 */
public class TestSoundCodec implements AudioCodec {

    @Override
    public void finalizeStream() {
    }

    @Override
    public int finalizeStream(OutputBitstream out) {
        return 0;
    }

    @Override
    public int initialize(InputBitstream in) throws IOException {
        return 0;
    }

    @Override
    public int initialize(OutputBitstream out, CodecParameterSet parameters) throws InvalidCodecInitializationParameter {
        return 0;
    }
}
