package org.solol.mmseg.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.solol.mmseg.core.IChunk;
import org.solol.mmseg.core.IRule;

/**
 * @author solo L
 * 
 */
public class LSDMFOCWRule implements IRule {

    public final IChunk[] invoke(final IChunk[] chunks) {
        LSDMFOCWRuleComparator[] orderedChunks = new LSDMFOCWRuleComparator[chunks.length];
        for (int i = 0; i < chunks.length; i++) {
            orderedChunks[i] = new LSDMFOCWRuleComparator(chunks[i]);
        }
        Arrays.sort(orderedChunks);
        int index = 0;
        double degreeOfMorphemicFreedom = orderedChunks[index].getChunk().getDegreeOfMorphemicFreedom();
        List list = new ArrayList(1);
        list.add(orderedChunks[index].getChunk());
        index++;
        while (index < orderedChunks.length) {
            if (orderedChunks[index].getChunk().getDegreeOfMorphemicFreedom() == degreeOfMorphemicFreedom) {
                list.add(orderedChunks[index].getChunk());
            } else {
                break;
            }
            index++;
        }
        IChunk[] degreeOfMorphemicFreedomChunks = new IChunk[list.size()];
        list.toArray(degreeOfMorphemicFreedomChunks);
        return degreeOfMorphemicFreedomChunks;
    }

    static class LSDMFOCWRuleComparator implements Comparable {

        private IChunk chunk;

        public LSDMFOCWRuleComparator(IChunk chunk) {
            this.chunk = chunk;
        }

        public IChunk getChunk() {
            return chunk;
        }

        public int compareTo(Object obj) {
            IChunk another = ((LSDMFOCWRuleComparator) obj).getChunk();
            double temp = another.getDegreeOfMorphemicFreedom() - chunk.getDegreeOfMorphemicFreedom();
            if (temp > 0D) {
                return 1;
            } else if (temp < 0D) {
                return -1;
            } else {
                return 0;
            }
        }
    }
}
