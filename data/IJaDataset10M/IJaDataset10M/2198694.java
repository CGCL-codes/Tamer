package org.jcvi.sequence;

import static org.junit.Assert.*;
import java.nio.ShortBuffer;
import org.jcvi.glyph.DefaultEncodedGlyphs;
import org.jcvi.glyph.EncodedGlyphs;
import org.jcvi.glyph.num.DefaultShortGlyphCodec;
import org.jcvi.glyph.num.ShortGlyph;
import org.jcvi.glyph.num.ShortGlyphFactory;
import org.jcvi.sequence.Peaks;
import org.jcvi.testUtil.TestUtil;
import org.junit.Test;

public class TestPeaks {

    private static ShortGlyphFactory PEAKS_FACTORY = ShortGlyphFactory.getInstance();

    private static DefaultShortGlyphCodec PEAK_CODEC = DefaultShortGlyphCodec.getInstance();

    private short[] peaks = new short[] { 110, 120, 130, 140, 150, 160 };

    private short[] differentPeaks = new short[] { 30, 40, 50, 60, 70, 80, 90 };

    private Peaks sut = new Peaks(peaks);

    private EncodedGlyphs<ShortGlyph> encodedPeaks = new DefaultEncodedGlyphs<ShortGlyph>(PEAK_CODEC, PEAKS_FACTORY.getGlyphsFor(peaks));

    @Test
    public void constructor() {
        assertEquals(encodedPeaks, sut.getData());
    }

    @Test
    public void equalsSameRef() {
        TestUtil.assertEqualAndHashcodeSame(sut, sut);
    }

    @Test
    public void equalsSameValues() {
        Peaks sameValues = new Peaks(peaks);
        TestUtil.assertEqualAndHashcodeSame(sut, sameValues);
    }

    @Test
    public void notEqualsWrongClass() {
        assertFalse(sut.equals("not a Confidence"));
    }

    @Test
    public void notEqualsNull() {
        assertFalse(sut.equals(null));
    }

    @Test
    public void notEqualsDifferentValues() {
        Peaks differentValues = new Peaks(differentPeaks);
        TestUtil.assertNotEqualAndHashcodeDifferent(sut, differentValues);
    }

    @Test
    public void ShortBufferConstructor() {
        Peaks sameValues = new Peaks(ShortBuffer.wrap(peaks));
        TestUtil.assertEqualAndHashcodeSame(sut, sameValues);
    }

    @Test
    public void equalsShortBufferAtDifferentPositions() {
        final ShortBuffer buffer = ShortBuffer.wrap(peaks);
        Peaks sameValues = new Peaks(buffer);
        buffer.position(2);
        TestUtil.assertEqualAndHashcodeSame(sut, sameValues);
    }
}
