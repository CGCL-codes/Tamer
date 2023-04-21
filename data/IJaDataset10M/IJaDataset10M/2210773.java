package org.jcvi.fastX.fasta.pos;

import java.io.File;
import org.jcvi.fastX.fasta.AbstractLargeFastaRecordIterator;
import org.jcvi.glyph.EncodedGlyphs;
import org.jcvi.glyph.num.ShortGlyph;

/**
 * @author dkatzel
 *
 *
 */
public class LargePositionFastaVisitor extends AbstractLargeFastaRecordIterator<EncodedGlyphs<ShortGlyph>, PositionFastaRecord<EncodedGlyphs<ShortGlyph>>> {

    /**
     * @param fastaFile
     * @param recordFactory
     */
    public LargePositionFastaVisitor(File fastaFile) {
        super(fastaFile, DefaultPositionFastaRecordFactory.getInstance());
    }
}
