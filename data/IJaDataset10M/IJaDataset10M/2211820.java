package org.jcvi.common.core.seq.fastx.fasta.qual;

import org.jcvi.common.core.seq.fastx.fasta.FastaRecordFactory;
import org.jcvi.common.core.symbol.qual.PhredQuality;
import org.jcvi.common.core.symbol.qual.QualitySequence;

/**
 * {@code NucleotideFastaRecordFactory} is an implementation 
 * of {@link FastaRecordFactory} that makes 
 * {@code FastaRecord<EncodedGlyphs<PhredQuality>>}s.
 * @author dkatzel
 *
 *
 */
interface QualityFastaRecordFactory extends FastaRecordFactory<PhredQuality, QualitySequence, QualitySequenceFastaRecord> {
}
