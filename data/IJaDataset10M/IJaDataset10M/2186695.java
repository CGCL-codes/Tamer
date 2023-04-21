package org.jcvi.glyph.aa;

import java.util.List;
import org.jcvi.glyph.EncodedGlyphs;

public class TestDefaultAminoAcidEncodedGlyphs extends AbstractTestAminoAcidEncodedGlyphs {

    @Override
    protected EncodedGlyphs<AminoAcid> encode(List<AminoAcid> aminoAcids) {
        return new DefaultAminoAcidEncodedGlyphs(aminoAcids);
    }
}
