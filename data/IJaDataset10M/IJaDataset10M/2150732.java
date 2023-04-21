package org.jcvi.common.core.seq.read.trace.sanger.chromat.ztr.chunk;

import java.nio.ByteBuffer;
import org.jcvi.common.core.seq.read.trace.TraceDecoderException;
import org.jcvi.common.core.seq.read.trace.TraceEncoderException;
import org.jcvi.common.core.seq.read.trace.sanger.chromat.ztr.ZTRChromatogram;
import org.jcvi.common.core.seq.read.trace.sanger.chromat.ztr.ZTRChromatogramBuilder;
import org.jcvi.common.core.seq.read.trace.sanger.chromat.ztr.chunk.Chunk;
import org.jcvi.common.core.symbol.pos.SangerPeak;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

public class TestBPOSChunk {

    private static final short[] decodedPeaks = new short[] { 10, 20, 30, 41, 53, 60, 68 };

    Chunk sut = Chunk.POSITIONS;

    private static final byte[] encodedPositions;

    static {
        ByteBuffer buf = ByteBuffer.allocate(decodedPeaks.length * 4 + 4);
        buf.putInt(0);
        for (int i = 0; i < decodedPeaks.length; i++) {
            buf.putInt(decodedPeaks[i]);
        }
        encodedPositions = buf.array();
    }

    @Test
    public void valid() throws TraceDecoderException {
        ZTRChromatogramBuilder mockStruct = new ZTRChromatogramBuilder("id");
        sut.parseData(encodedPositions, mockStruct);
        assertArrayEquals(decodedPeaks, mockStruct.peaks());
    }

    @Test
    public void encode() throws TraceEncoderException {
        ZTRChromatogram chromatogram = createMock(ZTRChromatogram.class);
        expect(chromatogram.getPeaks()).andReturn(new SangerPeak(decodedPeaks));
        replay(chromatogram);
        byte[] actual = sut.encodeChunk(chromatogram);
        assertArrayEquals(encodedPositions, actual);
        verify(chromatogram);
    }
}
