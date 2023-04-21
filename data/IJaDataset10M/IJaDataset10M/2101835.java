package org.spantus.extractor.segments.online.cluster;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import net.sf.javaml.classification.Classifier;
import net.sf.javaml.classification.KNearestNeighbors;
import net.sf.javaml.clustering.Clusterer;
import net.sf.javaml.clustering.KMeans;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.SparseInstance;
import net.sf.javaml.distance.MinkowskiDistance;
import org.spantus.extractor.segments.offline.ExtremeSegment;
import org.spantus.extractor.segments.online.ExtremeSegmentsOnlineCtx;
import org.spantus.extractor.segments.online.SegmentFeatureData;
import org.spantus.logger.Logger;

public class ExtremeOnlineClusterServiceKnnImpl extends ExtremeOnlineClusterServiceSimpleImpl {

    protected Logger log = Logger.getLogger(ExtremeOnlineClusterServiceKnnImpl.class);

    @SuppressWarnings("unused")
    private Clusterer km = new KMeans(3);

    private Dataset dataSet;

    /**
	 * 
	 */
    @Override
    public String getClassName(ExtremeSegment segment, ExtremeSegmentsOnlineCtx ctx) {
        log.debug("[getClassName]+++");
        Double area = getExtremeSegmentService().getCalculatedArea(segment);
        Long length = getExtremeSegmentService().getCalculatedLength(segment);
        Integer peaks = segment.getPeakEntries().size();
        if (getDataSet().size() > 3) {
            Dataset normalized = getDataSet().copy();
            Clusterer kmCluser = new KMeans(3, 100, new MinkowskiDistance());
            Dataset[] clusters = kmCluser.cluster(normalized);
            Dataset testDataset = new DefaultDataset();
            TreeMap<Double, Integer> map = new TreeMap<Double, Integer>();
            int classNumIter = 0;
            for (Dataset cluster : clusters) {
                Instance insance = cluster.get(0);
                double sum = 0d;
                for (double dVal : insance.values()) {
                    sum += dVal;
                }
                map.put(sum, classNumIter++);
            }
            Map<Integer, Integer> remap = new HashMap<Integer, Integer>();
            int order = 0;
            for (Integer classNum : map.values()) {
                remap.put(classNum, order++);
            }
            int i = 0;
            for (Dataset cluster : clusters) {
                log.debug("[getClassName] cluster[{0}] class{1}:  {2}", i, remap.get(i), cluster);
                for (Instance instance : cluster) {
                    instance.setClassValue(remap.get(i).doubleValue());
                    testDataset.add(instance);
                }
                i++;
            }
            Instance tmpInstance = new SparseInstance(3);
            tmpInstance.put(0, length.doubleValue());
            tmpInstance.put(1, peaks.doubleValue());
            tmpInstance.put(2, area.doubleValue());
            tmpInstance.setClassValue(0D);
            Classifier knnClassifier = new KNearestNeighbors(3);
            knnClassifier.buildClassifier(testDataset);
            Double predictedClassValue = (Double) knnClassifier.classify(tmpInstance);
            log.debug("[getClassName] instance: {0}", tmpInstance);
            log.debug("[getClassName] predictedClassValue: {0}", predictedClassValue);
            log.debug("[getClassName]---");
            return "" + predictedClassValue.intValue();
        }
        log.debug("[getClassName]---");
        return "1";
    }

    /**
	 * 
	 */
    @Override
    public SegmentFeatureData learn(ExtremeSegment segment, ExtremeSegmentsOnlineCtx ctx) {
        SegmentFeatureData segmentFeature = super.learn(segment, ctx);
        Instance tmpInstance = new SparseInstance(3);
        tmpInstance.put(0, segmentFeature.getLength().doubleValue());
        tmpInstance.put(1, segmentFeature.getPeaks().doubleValue());
        tmpInstance.put(2, segmentFeature.getArea().doubleValue());
        tmpInstance.setClassValue(0D);
        getDataSet().add(tmpInstance);
        log.debug("[learn]  segmentFeature: {0}", segmentFeature);
        return segmentFeature;
    }

    public Dataset getDataSet() {
        if (dataSet == null) {
            dataSet = new DefaultDataset();
        }
        return dataSet;
    }
}
