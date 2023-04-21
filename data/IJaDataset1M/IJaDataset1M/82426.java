package org.jcvi.glyph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.jcvi.glyph.nuc.NucleotideGlyph;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestRunLengthEncoder {

    String BasesAsString = "AAAAAAAAAAAATAAAAAAAAAAAATTTAAAAAAAAAAAAAAAAAAAAAAAATAAAAAAAAAAAAAA";

    List<NucleotideGlyph> list = NucleotideGlyph.getGlyphsFor(BasesAsString);

    List<RunLength<NucleotideGlyph>> expectedEncoding = Arrays.asList(new RunLength<NucleotideGlyph>(NucleotideGlyph.Adenine, 12), new RunLength<NucleotideGlyph>(NucleotideGlyph.Thymine, 1), new RunLength<NucleotideGlyph>(NucleotideGlyph.Adenine, 12), new RunLength<NucleotideGlyph>(NucleotideGlyph.Thymine, 3), new RunLength<NucleotideGlyph>(NucleotideGlyph.Adenine, 24), new RunLength<NucleotideGlyph>(NucleotideGlyph.Thymine, 1), new RunLength<NucleotideGlyph>(NucleotideGlyph.Adenine, 14));

    @Test
    public void encode() {
        List<RunLength<NucleotideGlyph>> actual = RunLengthEncoder.encode(list);
        assertEquals(expectedEncoding, actual);
    }

    @Test
    public void decode() {
        List<NucleotideGlyph> actual = RunLengthEncoder.decode(expectedEncoding);
        assertEquals(list, actual);
    }

    @Test
    public void enocdeEmptyList() {
        assertEquals(new ArrayList<RunLength<NucleotideGlyph>>(), RunLengthEncoder.encode(Collections.<NucleotideGlyph>emptyList()));
    }
}
