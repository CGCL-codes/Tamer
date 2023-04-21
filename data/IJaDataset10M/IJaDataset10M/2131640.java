package org.jcvi.assembly.slice.consensus;

import static org.jcvi.assembly.slice.consensus.ConsensusUtil.BASES_TO_CONSIDER;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.jcvi.assembly.slice.Slice;
import org.jcvi.assembly.slice.SliceElement;
import org.jcvi.glyph.nuc.NucleotideGlyph;
import org.jcvi.glyph.phredQuality.PhredQuality;

/**
 * Calculate Consensus for a slice using Bayes formula and the procedure from
 * <pre>
 * Churchill, G.A. and Waterman, M.S.
 * "The accuracy of DNA sequences: Estimating sequence quality."
 * Genomics 14, pp.89-98 (1992)
 * </pre>
 * @author dkatzel
 *
 *
 */
public abstract class AbstractChurchillWatermanConsensusCaller extends AbstractConsensusCaller {

    public AbstractChurchillWatermanConsensusCaller(PhredQuality highQualityThreshold) {
        super(highQualityThreshold);
    }

    protected abstract NucleotideGlyph getConsensus(ProbabilityStruct normalizedErrorProbabilityStruct, Slice slice);

    @Override
    public ConsensusResult callConsensusWithCoverage(Slice slice) {
        Map<NucleotideGlyph, Integer> qualityValueSumMap = generateQualityValueSumMap(slice);
        ProbabilityStruct normalizedErrorProbabilityStruct = generateNormalizedProbabilityStruct(qualityValueSumMap);
        NucleotideGlyph consensus = getConsensus(normalizedErrorProbabilityStruct, slice);
        return new DefaultConsensusResult(consensus, getErrorProbability(normalizedErrorProbabilityStruct, slice));
    }

    protected int getErrorProbability(ProbabilityStruct normalizedErrorProbabilityStruct, Slice slice) {
        double normalizedProbability = getProbabilityFor(normalizedErrorProbabilityStruct);
        if (normalizedProbability == 0.0D) {
            int sum = 0;
            for (SliceElement element : slice) {
                sum += element.getQuality().getNumber();
            }
            return sum;
        }
        return PhredQuality.convertErrorProbability(normalizedProbability);
    }

    protected double getProbabilityFor(ProbabilityStruct normalizedErrorProbabilityStruct) {
        Double lowest = Double.MAX_VALUE;
        for (Entry<NucleotideGlyph, Double> entry : normalizedErrorProbabilityStruct.entrySet()) {
            Double currentValue = entry.getValue();
            if (currentValue.compareTo(lowest) < 0) {
                lowest = currentValue;
            }
        }
        return lowest;
    }

    private ProbabilityStruct generateNormalizedProbabilityStruct(Map<NucleotideGlyph, Integer> qualityValueSumMap) {
        List<ProbabilityStruct> probabilityStructs = createProbabilityStructsForEachBase(qualityValueSumMap);
        ProbabilityStruct rawErrorProbabilityStruct = createRawErrorProbabilityStruct(probabilityStructs);
        return rawErrorProbabilityStruct.normalize();
    }

    private ProbabilityStruct createRawErrorProbabilityStruct(List<ProbabilityStruct> probabilityStructs) {
        Map<NucleotideGlyph, Double> rawErrorProbabilityMap = new EnumMap<NucleotideGlyph, Double>(NucleotideGlyph.class);
        for (NucleotideGlyph base : ConsensusUtil.BASES_TO_CONSIDER) {
            rawErrorProbabilityMap.put(base, calculateRawErrorProbabilityFor(base, probabilityStructs));
        }
        return new ProbabilityStruct(rawErrorProbabilityMap);
    }

    private List<ProbabilityStruct> createProbabilityStructsForEachBase(Map<NucleotideGlyph, Integer> qualityValueSumMap) {
        List<ProbabilityStruct> probabilityStructs = new ArrayList<ProbabilityStruct>();
        for (NucleotideGlyph base : ConsensusUtil.BASES_TO_CONSIDER) {
            probabilityStructs.add(new ProbabilityStruct(base, qualityValueSumMap.get(base)));
        }
        return probabilityStructs;
    }

    private double calculateRawErrorProbabilityFor(NucleotideGlyph base, List<ProbabilityStruct> probabilityStructs) {
        double result = 1D;
        for (ProbabilityStruct struct : probabilityStructs) {
            result *= struct.getProbabilityFor(base);
        }
        return result;
    }

    protected Set<NucleotideGlyph> getBasesUsedTowardsAmbiguity(ProbabilityStruct normalizedErrorProbabilityStruct, int baseCount) {
        double errorProbabilityOfAmbiguity;
        double sumOfProbabilitySuccess = 0D;
        Set<NucleotideGlyph> basesUsed = EnumSet.noneOf(NucleotideGlyph.class);
        List<NucleotideGlyph> basesToConsider = new ArrayList<NucleotideGlyph>(BASES_TO_CONSIDER);
        Collections.sort(basesToConsider, new LowestProbabilityComparator(normalizedErrorProbabilityStruct));
        do {
            NucleotideGlyph baseWithLowestErrorProbability = basesToConsider.remove(0);
            sumOfProbabilitySuccess += (1 - normalizedErrorProbabilityStruct.getProbabilityFor(baseWithLowestErrorProbability));
            basesUsed.add(baseWithLowestErrorProbability);
            errorProbabilityOfAmbiguity = 1 - sumOfProbabilitySuccess;
        } while (sumOfProbabilitySuccess < 1D && PhredQuality.convertErrorProbability(errorProbabilityOfAmbiguity) < getHighQualityThreshold().getNumber().intValue() && basesUsed.size() < baseCount);
        return basesUsed;
    }

    /**
     * Sorts {@link ProbabilityStruct} by comparing the 
     * probability of the given {@link NucleotideGlyph}.
     * @author dkatzel
     *
     *
     */
    private static class LowestProbabilityComparator implements Comparator<NucleotideGlyph> {

        private final ProbabilityStruct probabilityStruct;

        LowestProbabilityComparator(ProbabilityStruct probabilityStruct) {
            this.probabilityStruct = probabilityStruct;
        }

        @Override
        public int compare(NucleotideGlyph o1, NucleotideGlyph o2) {
            return probabilityStruct.getProbabilityFor(o1).compareTo(probabilityStruct.getProbabilityFor(o2));
        }
    }
}
