package bgu.nlp.seg.duck.determine;

import bgu.nlp.seg.AmbiguityRandom;
import bgu.nlp.seg.vo.SegmentWordOptionCount;
import bgu.nlp.seg.vo.SegmentationResults;
import bgu.nlp.seg.vo.SegmentedWord;
import java.util.Set;

/**
 * @author Guz
 * 
 */
public class DeepThoughtRandom implements DeepThought {

    private static final Double dummy = new Double(123);

    public String getName() {
        return "DeepThoughtRandom";
    }

    public SegmentationResults choose(Set<SegmentWordOptionCount> segmentationCounted) {
        final AmbiguityRandom ambiguityRandom = new AmbiguityRandom();
        final SegmentationResults randomResults = new SegmentationResults(segmentationCounted.size()) {

            @Override
            public SegmentedWord getSegmetation() {
                final SegmentedWord segmetation = ambiguityRandom.getSegmetation(segmentationProbability);
                return segmetation;
            }
        };
        for (SegmentWordOptionCount option : segmentationCounted) {
            randomResults.put(option.getSuggesetedWordSegmentation(), dummy);
        }
        return randomResults;
    }
}
