package com.vividsolutions.jts.noding;

import java.util.*;
import com.vividsolutions.jts.index.SpatialIndex;
import com.vividsolutions.jts.index.chain.*;
import com.vividsolutions.jts.index.quadtree.Quadtree;
import com.vividsolutions.jts.index.strtree.STRtree;
import com.vividsolutions.jts.noding.SegmentIntersector;
import com.vividsolutions.jts.noding.SegmentString;

/**
 * Intersects two sets of {@link SegmentStrings} using a index based
 * on {@link MonotoneChain}s and a {@link SpatialIndex}.
 *
 * @version 1.7
 */
public class MCIndexSegmentSetMutualIntersector extends SegmentSetMutualIntersector {

    private List monoChains = new ArrayList();

    private SpatialIndex index = new STRtree();

    private int indexCounter = 0;

    private int processCounter = 0;

    private int nOverlaps = 0;

    public MCIndexSegmentSetMutualIntersector() {
    }

    public List getMonotoneChains() {
        return monoChains;
    }

    public SpatialIndex getIndex() {
        return index;
    }

    public void setBaseSegments(Collection segStrings) {
        for (Iterator i = segStrings.iterator(); i.hasNext(); ) {
            addToIndex((SegmentString) i.next());
        }
    }

    private void addToIndex(SegmentString segStr) {
        List segChains = MonotoneChainBuilder.getChains(segStr.getCoordinates(), segStr);
        for (Iterator i = segChains.iterator(); i.hasNext(); ) {
            MonotoneChain mc = (MonotoneChain) i.next();
            mc.setId(indexCounter++);
            index.insert(mc.getEnvelope(), mc);
        }
    }

    public void process(Collection segStrings) {
        processCounter = indexCounter + 1;
        nOverlaps = 0;
        monoChains.clear();
        for (Iterator i = segStrings.iterator(); i.hasNext(); ) {
            addToMonoChains((SegmentString) i.next());
        }
        intersectChains();
    }

    private void intersectChains() {
        MonotoneChainOverlapAction overlapAction = new SegmentOverlapAction(segInt);
        for (Iterator i = monoChains.iterator(); i.hasNext(); ) {
            MonotoneChain queryChain = (MonotoneChain) i.next();
            List overlapChains = index.query(queryChain.getEnvelope());
            for (Iterator j = overlapChains.iterator(); j.hasNext(); ) {
                MonotoneChain testChain = (MonotoneChain) j.next();
                queryChain.computeOverlaps(testChain, overlapAction);
                nOverlaps++;
                if (segInt.isDone()) return;
            }
        }
    }

    private void addToMonoChains(SegmentString segStr) {
        List segChains = MonotoneChainBuilder.getChains(segStr.getCoordinates(), segStr);
        for (Iterator i = segChains.iterator(); i.hasNext(); ) {
            MonotoneChain mc = (MonotoneChain) i.next();
            mc.setId(processCounter++);
            monoChains.add(mc);
        }
    }

    public class SegmentOverlapAction extends MonotoneChainOverlapAction {

        private SegmentIntersector si = null;

        public SegmentOverlapAction(SegmentIntersector si) {
            this.si = si;
        }

        public void overlap(MonotoneChain mc1, int start1, MonotoneChain mc2, int start2) {
            SegmentString ss1 = (SegmentString) mc1.getContext();
            SegmentString ss2 = (SegmentString) mc2.getContext();
            si.processIntersections(ss1, start1, ss2, start2);
        }
    }
}
