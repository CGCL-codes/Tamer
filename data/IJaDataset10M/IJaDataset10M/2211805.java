package apollo.gui;

import java.util.*;
import java.awt.*;
import apollo.datamodel.*;
import apollo.gui.schemes.*;
import apollo.gui.event.*;
import apollo.util.*;

public class SortedFeatureTierManager extends FlexibleFeatureTierManager implements TypesChangedListener {

    public SortedFeatureTierManager() {
        super();
    }

    public SortedFeatureTierManager(DrawableFeatureSet fset) {
        super(fset);
    }

    protected void layoutTiers() {
        super.layoutTiers();
        long timestamp = System.currentTimeMillis();
        Vector sortedTiers = new Vector();
        int[] boundaries = findTypeBoundaries();
        for (int i = 0; i < boundaries.length - 1; i++) {
            FeatureTier tier_data = (FeatureTier) tiers.elementAt(boundaries[i]);
            String feature_type = tier_data.getType();
            TierProperty tp = getPropertyScheme().getTierProperty(feature_type);
            if (isVisible(feature_type)) {
                if (!(tp.isExpanded())) {
                    sortedTiers.addElement(tier_data);
                } else {
                    Vector oldTiers = new Vector();
                    for (int j = boundaries[i]; j < boundaries[i + 1]; j++) {
                        oldTiers.addElement(tiers.elementAt(j));
                    }
                    int[] startInds = new int[oldTiers.size()];
                    Vector newTiers = new Vector();
                    for (int j = 0; j < oldTiers.size(); j++) {
                        newTiers.addElement(new FeatureTier());
                    }
                    Vector overlap;
                    while ((overlap = getNextOverlap(oldTiers, startInds)).size() > 0) {
                        sortOverlap(newTiers, overlap);
                    }
                    for (int j = 0; j < newTiers.size(); j++) {
                        sortedTiers.addElement(newTiers.elementAt(j));
                    }
                }
            }
        }
        tiers = sortedTiers;
        System.out.println("Layout tiers in: " + (System.currentTimeMillis() - timestamp) + "ms");
        fireTierManagerEvent(TierManagerEvent.LAYOUT_CHANGED);
    }

    protected int[] findTypeBoundaries() {
        int[] tmpBoundaries = new int[getPropertyScheme().getAllTiers().size() + 2];
        int nBound = 0;
        if (tiers.size() > 0) {
            FeatureTier tier_data = (FeatureTier) tiers.elementAt(0);
            TierProperty lasttp = getPropertyScheme().getTierProperty(tier_data.getType());
            tmpBoundaries[nBound++] = 0;
            for (int i = 1; i < tiers.size(); i++) {
                tier_data = (FeatureTier) tiers.elementAt(i);
                TierProperty tp = getPropertyScheme().getTierProperty(tier_data.getType());
                if (tp != lasttp) {
                    lasttp = tp;
                    tmpBoundaries[nBound++] = i;
                }
            }
            tmpBoundaries[nBound++] = tiers.size();
        }
        int[] boundaries = new int[nBound];
        System.arraycopy(tmpBoundaries, 0, boundaries, 0, nBound);
        return boundaries;
    }

    protected Vector getNextOverlap(Vector typeTiers, int[] startInds) {
        Vector overlapFeatures = new Vector();
        boolean hadOverlap = true;
        Vector features = ((FeatureTier) typeTiers.elementAt(0)).getFeatures();
        long high = -1000000000;
        while (hadOverlap) {
            hadOverlap = false;
            for (int i = 0; i < typeTiers.size(); i++) {
                features = ((FeatureTier) typeTiers.elementAt(i)).getFeatures();
                if (startInds[i] < features.size()) {
                    SeqFeatureI sf = (SeqFeatureI) features.elementAt(startInds[i]);
                    if (high == -1000000000) high = sf.getHigh();
                    if (sf.getLow() <= high) {
                        hadOverlap = true;
                        overlapFeatures.addElement(sf);
                        startInds[i]++;
                        if (sf.getHigh() > high) {
                            high = sf.getHigh();
                        }
                    }
                }
            }
        }
        return overlapFeatures;
    }

    protected void sortOverlap(Vector newTiers, Vector overlap) {
        int nTier = newTiers.size();
        int nOverlapping = overlap.size();
        Object[] feats = new Object[overlap.size()];
        for (int i = 0; i < overlap.size(); i++) {
            feats[i] = overlap.elementAt(i);
        }
        double[] scores = new double[nOverlapping];
        for (int i = 0; i < nOverlapping; i++) {
            scores[i] = ((SeqFeatureI) feats[i]).getScore();
        }
        QuickSort.sort(scores, feats);
        for (int i = 0; i < nOverlapping / 2; i++) {
            Object tmp = feats[i];
            feats[i] = feats[nOverlapping - i - 1];
            feats[nOverlapping - i - 1] = tmp;
        }
        for (int i = 0; i < nOverlapping; i++) {
            if (i >= nTier) {
                nTier++;
                newTiers.addElement(new FeatureTier());
            }
            ((FeatureTier) newTiers.elementAt(i)).addFeature((DrawableSeqFeature) feats[i]);
        }
    }
}
