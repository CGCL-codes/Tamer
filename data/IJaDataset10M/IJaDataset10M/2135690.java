package org.mobicents.media.server.spi.dsp;

import org.mobicents.media.server.spi.format.Formats;
import org.mobicents.media.server.spi.memory.Frame;

/**
 * Digital signaling processor.
 * 
 * DSP transforms media from its original format to one of the specified
 * output format. Output formats are specified as array where order of the
 * formats defines format's priority. If frame has format matching to output
 * format the frame won't be changed.

 * @author kulikov
 */
public interface Processor {

    /**
     * Gets the list of supported codecs.
     * 
     * @return array of codecs
     */
    public Codec[] getCodecs();

    /**
     * Sets the list of supported output formats.
     * 
     * @param formats the list of formats sorted by format priority.
     */
    public void setFormats(Formats formats);

    /**
     * Transforms supplied frame if frame's format does not match to any
     * of the supported output formats and such transcoding is possible.
     *
     * @param frame the frame for transcoding
     * @return transcoded frame
     */
    public Frame process(Frame frame);
}
