package org.jcvi.fastX.fastq;

import org.jcvi.fastX.FastXRecord;
import org.jcvi.glyph.nuc.NucleotideEncodedGlyphs;
import org.jcvi.glyph.phredQuality.QualityEncodedGlyphs;

public interface FastQRecord extends FastXRecord<NucleotideEncodedGlyphs> {

    String getId();

    String getComment();

    NucleotideEncodedGlyphs getNucleotides();

    QualityEncodedGlyphs getQualities();
}
