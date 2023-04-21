package org.jcvi.assembly.slice.consensus;

import org.jcvi.assembly.slice.Slice;
import org.jcvi.glyph.nuc.NucleotideGlyph;
import org.jcvi.glyph.phredQuality.PhredQuality;
import static org.jcvi.glyph.nuc.NucleotideGlyph.*;

/**
 * <code>NoAmbiguityConsensusCaller</code>
 * will always return the non-ambiguous base
 * in the consensus with the lowest error probability.
 * @author dkatzel
 *
 *
 */
public class NoAmbiguityConsensusCaller extends AbstractChurchillWatermanConsensusCaller {

    public NoAmbiguityConsensusCaller(PhredQuality highQualityThreshold) {
        super(highQualityThreshold);
    }

    @Override
    protected NucleotideGlyph getConsensus(ProbabilityStruct normalizedErrorProbabilityStruct, Slice slice) {
        NucleotideGlyph result = Adenine;
        double lowestErrorProbability = normalizedErrorProbabilityStruct.getProbabilityFor(Adenine);
        if (normalizedErrorProbabilityStruct.getProbabilityFor(Cytosine).compareTo(lowestErrorProbability) < 0) {
            result = Cytosine;
            lowestErrorProbability = normalizedErrorProbabilityStruct.getProbabilityFor(Cytosine);
        }
        if (normalizedErrorProbabilityStruct.getProbabilityFor(Guanine).compareTo(lowestErrorProbability) < 0) {
            result = Guanine;
            lowestErrorProbability = normalizedErrorProbabilityStruct.getProbabilityFor(Guanine);
        }
        if (normalizedErrorProbabilityStruct.getProbabilityFor(Thymine).compareTo(lowestErrorProbability) < 0) {
            result = Thymine;
            lowestErrorProbability = normalizedErrorProbabilityStruct.getProbabilityFor(Thymine);
        }
        if (normalizedErrorProbabilityStruct.getProbabilityFor(Gap).compareTo(lowestErrorProbability) < 0) {
            result = Gap;
            lowestErrorProbability = normalizedErrorProbabilityStruct.getProbabilityFor(Gap);
        }
        return result;
    }
}
