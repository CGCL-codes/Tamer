package com.jswiff.swfrecords.tags;

import com.jswiff.io.OutputBitStream;
import java.io.IOException;

/**
 * <p>
 * This tag contains raw streaming sound data. The data format must be defined
 * in a preceding <code>SoundStreamHead</code> or
 * <code>SoundStreamHead2</code> tag. There may only be one
 * <code>SoundStreamBlock</code> tag per SWF frame.
 * </p>
 * 
 * <p>
 * Warning: you are responsible for obtaining technology licenses needed for
 * encoding and decoding sound data (see e.g. <a
 * href="http://mp3licensing.com">mp3licensing.com</a> for details on mp3
 * licensing).
 * </p>
 *
 * @see SoundStreamHead
 * @see SoundStreamHead2
 * @since SWF 1
 */
public final class SoundStreamBlock extends Tag {

    private byte[] streamSoundData;

    /**
	 * Creates a new SoundStreamBlock tag. Supply the sound data as byte array
	 *
	 * @param streamSoundData raw sound stream data
	 */
    public SoundStreamBlock(byte[] streamSoundData) {
        code = TagConstants.SOUND_STREAM_BLOCK;
        this.streamSoundData = streamSoundData;
    }

    SoundStreamBlock() {
    }

    /**
	 * Sets the contained sound stream data (as a byte array).
	 *
	 * @param streamSoundData raw sound stream data
	 */
    public void setStreamSoundData(byte[] streamSoundData) {
        this.streamSoundData = streamSoundData;
    }

    /**
	 * Returns the contained sound stream data as a byte array.
	 *
	 * @return raw sound stream data
	 */
    public byte[] getStreamSoundData() {
        return streamSoundData;
    }

    protected void writeData(OutputBitStream outStream) throws IOException {
        forceLongHeader = true;
        outStream.writeBytes(streamSoundData);
    }

    void setData(byte[] data) {
        streamSoundData = data;
    }
}
