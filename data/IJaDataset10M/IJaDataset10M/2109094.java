package com.shieldsbetter.paramour.utilities;

import java.io.IOException;
import java.io.InputStream;
import javax.sound.sampled.AudioFormat;

/**
 *
 * @author hamptos
 */
public class PCMDataConverterStream extends InputStream {

    public PCMDataConverterStream(InputStream source, AudioFormat sourceFormat, AudioFormat destinationFormat) {
    }

    @Override
    public int read() throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
