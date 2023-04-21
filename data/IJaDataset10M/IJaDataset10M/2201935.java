package org.mobicents.media.server.impl.dsp.audio.g711.alaw;

import org.mobicents.media.Format;
import org.mobicents.media.server.spi.dsp.Codec;
import org.mobicents.media.server.spi.dsp.CodecFactory;

/**
 *
 * @author kulikov
 */
public class DecoderFactory implements CodecFactory {

    /**
     * (Non Java-doc.)
     * 
     * @see org.mobicents.media.server.spi.dsp.CodecFactory#getCodec() 
     */
    public Codec getCodec() {
        return new Decoder();
    }

    /**
     * (Non Java-doc)
     * 
     * @see org.mobicents.media.server.impl.jmf.dsp.Codec#getSupportedFormat().
     */
    public Format getSupportedInputFormat() {
        return Codec.PCMA;
    }

    /**
     * (Non Java-doc)
     * 
     * @see org.mobicents.media.server.impl.jmf.dsp.Codec#getSupportedFormat().
     */
    public Format getSupportedOutputFormat() {
        return Codec.LINEAR_AUDIO;
    }
}
