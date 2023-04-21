package prequips.inclusionListBuilder.segments;

import java.util.ArrayList;
import java.util.Collections;
import prequips.inclusionListBuilder.features.InclusionListFeature;

public class StrictTimeAndSizeSegmenter extends AbstractInclusionListSegmenter {

    public StrictTimeAndSizeSegmenter() {
        super();
    }

    public int segmentList() {
        inclusionList.clearSegments();
        ArrayList<InclusionListFeature> l_featureList = inclusionList.getNonExcludedFeatureList();
        Collections.sort(l_featureList, new InclusionListFeature.PredictedRetentionTimeComparator(inclusionList));
        int l_currentSegmentId = 1;
        Segment l_currentSegment = new Segment(inclusionList, l_currentSegmentId++, getFirstSegmentStart(), getFirstSegmentEnd(), getSegmentOverlap(), 0f);
        inclusionList.addSegment(l_currentSegment);
        boolean l_isPastFirst = false;
        boolean l_isSegmentFull = false;
        for (int i = 0; i < l_featureList.size(); ++i) {
            InclusionListFeature l_feature = l_featureList.get(i);
            if (!l_isPastFirst) {
                if (l_feature.getPredictedRetentionTime(inclusionList) >= l_currentSegment.getActualEnd()) {
                    l_currentSegment = new Segment(inclusionList, l_currentSegmentId++, l_currentSegment.getActualEnd(), l_currentSegment.getActualEnd() + getSegmentLength(), getSegmentOverlap(), 0f);
                    l_currentSegment.setBeginFlankLength(getSegmentOverlap() - getSegmentDelay());
                    inclusionList.addSegment(l_currentSegment);
                    l_isPastFirst = true;
                }
                inclusionList.setSegment(l_feature, l_currentSegment);
                l_currentSegment.addFeature(l_feature);
            } else {
                if (l_currentSegment.getNumberOfFeatures() >= getMaxFeaturesPerSegment()) {
                    l_isSegmentFull = true;
                }
                if (l_currentSegment.getActualEnd() <= l_feature.getPredictedRetentionTime(inclusionList)) {
                    l_currentSegment = new Segment(inclusionList, l_currentSegmentId++, l_currentSegment.getActualEnd(), l_currentSegment.getActualEnd() + getSegmentLength(), getSegmentOverlap(), 0f);
                    l_currentSegment.setBeginFlankLength(getSegmentOverlap() - getSegmentDelay());
                    inclusionList.addSegment(l_currentSegment);
                    l_isSegmentFull = false;
                }
                if (!l_isSegmentFull) {
                    inclusionList.setSegment(l_feature, l_currentSegment);
                    l_currentSegment.addFeature(l_feature);
                }
            }
        }
        inclusionList.updateSegments();
        return inclusionList.getNumberOfSegments();
    }
}
