package org.jaudiotagger.audio.asf.io;

import org.jaudiotagger.audio.asf.data.*;
import org.jaudiotagger.audio.asf.util.Utils;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;

/**
 * Reads and interprets the data of the audio or video stream information chunk. <br>
 * 
 * @author Christian Laireiter
 */
public class StreamChunkReader implements ChunkReader {

    /**
     * The GUID this reader {@linkplain #getApplyingIds() applies to}
     */
    private static final GUID[] APPLYING = { GUID.GUID_STREAM };

    /**
     * Shouldn't be used for now.
     */
    protected StreamChunkReader() {
    }

    /**
     * {@inheritDoc}
     */
    public boolean canFail() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public GUID[] getApplyingIds() {
        return APPLYING.clone();
    }

    /**
     * {@inheritDoc}
     */
    public Chunk read(final GUID guid, final InputStream stream, final long chunkStart) throws IOException {
        StreamChunk result = null;
        final BigInteger chunkLength = Utils.readBig64(stream);
        final GUID streamTypeGUID = Utils.readGUID(stream);
        if (GUID.GUID_AUDIOSTREAM.equals(streamTypeGUID) || GUID.GUID_VIDEOSTREAM.equals(streamTypeGUID)) {
            final GUID errorConcealment = Utils.readGUID(stream);
            final long timeOffset = Utils.readUINT64(stream);
            final long typeSpecificDataSize = Utils.readUINT32(stream);
            final long streamSpecificDataSize = Utils.readUINT32(stream);
            final int mask = Utils.readUINT16(stream);
            final int streamNumber = mask & 127;
            final boolean contentEncrypted = (mask & 0x8000) != 0;
            stream.skip(4);
            long streamSpecificBytes;
            if (GUID.GUID_AUDIOSTREAM.equals(streamTypeGUID)) {
                final AudioStreamChunk audioStreamChunk = new AudioStreamChunk(chunkLength);
                result = audioStreamChunk;
                final long compressionFormat = Utils.readUINT16(stream);
                final long channelCount = Utils.readUINT16(stream);
                final long samplingRate = Utils.readUINT32(stream);
                final long avgBytesPerSec = Utils.readUINT32(stream);
                final long blockAlignment = Utils.readUINT16(stream);
                final int bitsPerSample = Utils.readUINT16(stream);
                final int codecSpecificDataSize = Utils.readUINT16(stream);
                final byte[] codecSpecificData = new byte[codecSpecificDataSize];
                stream.read(codecSpecificData);
                audioStreamChunk.setCompressionFormat(compressionFormat);
                audioStreamChunk.setChannelCount(channelCount);
                audioStreamChunk.setSamplingRate(samplingRate);
                audioStreamChunk.setAverageBytesPerSec(avgBytesPerSec);
                audioStreamChunk.setErrorConcealment(errorConcealment);
                audioStreamChunk.setBlockAlignment(blockAlignment);
                audioStreamChunk.setBitsPerSample(bitsPerSample);
                audioStreamChunk.setCodecData(codecSpecificData);
                streamSpecificBytes = 18 + codecSpecificData.length;
            } else {
                final VideoStreamChunk videoStreamChunk = new VideoStreamChunk(chunkLength);
                result = videoStreamChunk;
                final long pictureWidth = Utils.readUINT32(stream);
                final long pictureHeight = Utils.readUINT32(stream);
                stream.skip(1);
                stream.skip(2);
                stream.skip(16);
                final byte[] fourCC = new byte[4];
                stream.read(fourCC);
                videoStreamChunk.setPictureWidth(pictureWidth);
                videoStreamChunk.setPictureHeight(pictureHeight);
                videoStreamChunk.setCodecId(fourCC);
                streamSpecificBytes = 31;
            }
            result.setStreamNumber(streamNumber);
            result.setStreamSpecificDataSize(streamSpecificDataSize);
            result.setTypeSpecificDataSize(typeSpecificDataSize);
            result.setTimeOffset(timeOffset);
            result.setContentEncrypted(contentEncrypted);
            result.setPosition(chunkStart);
            stream.skip(chunkLength.longValue() - 24 - streamSpecificBytes - 54);
        }
        return result;
    }
}
