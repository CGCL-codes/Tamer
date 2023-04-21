package org.spantus.server.services.impl;

import java.awt.Point;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spantus.core.FrameValues;
import org.spantus.core.FrameVectorValues;
import org.spantus.core.IValues;
import org.spantus.core.beans.IValueHolder;
import org.spantus.core.beans.RecognitionResult;
import org.spantus.core.beans.RecognitionResultDetails;
import org.spantus.core.beans.SignalSegment;
import org.spantus.exception.ProcessingException;
import org.spantus.math.NumberUtils;
import org.spantus.math.dtw.DtwResult;
import org.spantus.math.dtw.DtwService;
import org.spantus.math.dtw.DtwServiceJavaMLImpl.JavaMLSearchWindow;
import org.spantus.math.services.MathServicesFactory;

public class CorpusServiceHelperImpl {

    private static final Logger LOG = LoggerFactory.getLogger(CorpusServiceHelperImpl.class);

    private DtwService dtwService;

    private Integer searchRadius;

    private JavaMLSearchWindow javaMLSearchWindow;

    private Set<String> includeFeatures;

    /**
	 * Update information with min max for each feature
	 * 
	 * @param feature
	 * @param value
	 * @param minimum
	 * @param maximum
	 */
    public void updateMinMax(String feature, Double value, Map<String, Double> minimum, Map<String, Double> maximum) {
        if (minimum.get(feature) == null) {
            minimum.put(feature, Double.MAX_VALUE);
        }
        if (maximum.get(feature) == null) {
            maximum.put(feature, -Double.MAX_VALUE);
        }
        if (minimum.get(feature).compareTo(value) > 0) {
            minimum.put(feature, value);
        }
        if (maximum.get(feature).compareTo(value) < 0) {
            maximum.put(feature, value);
        }
    }

    /**
	 * post process multi match result. normalize and ordering
	 * 
	 * @param results
	 * @return
	 */
    public <T extends RecognitionResult> List<T> postProcessResult(List<T> results, Map<String, Double> minimum, Map<String, Double> maximum) {
        for (RecognitionResult result : results) {
            Map<String, Double> normalizedScores = new HashMap<String, Double>();
            Double normalizedSum = 0D;
            for (Entry<String, Double> score : result.getScores().entrySet()) {
                Double normalizedScore = score.getValue();
                if (getIncludeFeatures() == null || getIncludeFeatures().isEmpty()) {
                    normalizedSum += normalizedScore;
                } else if (getIncludeFeatures().contains(score.getKey())) {
                    normalizedSum += normalizedScore;
                }
                normalizedScores.put(score.getKey(), normalizedScore);
            }
            result.setDistance(normalizedSum);
            result.setScores(normalizedScores);
        }
        Collections.sort(results, new Comparator<RecognitionResult>() {

            public int compare(RecognitionResult o1, RecognitionResult o2) {
                return NumberUtils.compare(o1.getDistance(), o2.getDistance());
            }
        });
        int maxElementSize = NumberUtils.min(20, results.size());
        LOG.debug("[postProcessResult] results after sort: {0}", results);
        return results.subList(0, maxElementSize);
    }

    /**
	 * 
	 * @param targetEntry
	 * @param sampleFeatureData
	 * @return
	 */
    public DtwResult findDtwResult(Entry<String, IValues> targetEntry, IValueHolder<?> sampleFeatureData) {
        DtwResult dtwResult = null;
        int targetDimention = targetEntry.getValue().getDimention();
        if (targetDimention == 1) {
            dtwResult = (DtwResult) getDtwService().calculateInfo((FrameValues) targetEntry.getValue(), (FrameValues) sampleFeatureData.getValues());
        } else {
            if (targetDimention != sampleFeatureData.getValues().getDimention()) {
                String msg = "Sample size not same " + targetEntry.getKey() + targetDimention + "!=" + sampleFeatureData.getValues().getDimention();
                LOG.error("[findMultipleMatch] " + msg);
                throw new ProcessingException(msg);
            }
            dtwResult = getDtwService().calculateInfoVector((FrameVectorValues) targetEntry.getValue(), (FrameVectorValues) sampleFeatureData.getValues());
        }
        return dtwResult;
    }

    /**
	 * 
	 * @param target
	 * @param sample
	 * @return
	 */
    protected RecognitionResult compare(String featureName, IValues targetValues, SignalSegment sample) {
        IValueHolder<?> fd = sample.findValueHolder(featureName);
        if (fd == null) {
            return null;
        }
        RecognitionResult result = new RecognitionResult();
        result.setInfo(sample);
        if (targetValues.getDimention() == 1) {
            if (((FrameValues) targetValues).size() < 2) {
                return null;
            }
            result.setDistance(getDtwService().calculateDistance((FrameValues) targetValues, (FrameValues) fd.getValues()));
        } else {
            if (((FrameVectorValues) targetValues).size() < 2) {
                return null;
            }
            result.setDistance(getDtwService().calculateDistanceVector((FrameVectorValues) targetValues, (FrameVectorValues) fd.getValues()));
        }
        return result;
    }

    /**
	 * 
	 * @return
	 */
    public RecognitionResultDetails createRecognitionResultDetail() {
        RecognitionResultDetails result = new RecognitionResultDetails();
        result.setPath(new HashMap<String, List<Point>>());
        result.setPath(new HashMap<String, List<Point>>());
        result.setScores(new HashMap<String, Double>());
        result.setTargetLegths(new HashMap<String, Double>());
        result.setSampleLegths(new HashMap<String, Double>());
        return result;
    }

    public DtwService getDtwService() {
        if (dtwService == null) {
            if (searchRadius == null || javaMLSearchWindow == null) {
                dtwService = MathServicesFactory.createDtwService();
            } else {
                dtwService = MathServicesFactory.createDtwService(searchRadius, javaMLSearchWindow);
            }
        }
        return dtwService;
    }

    public Integer getSearchRadius() {
        return searchRadius;
    }

    public void setSearchRadius(Integer searchRadius) {
        this.searchRadius = searchRadius;
    }

    public JavaMLSearchWindow getJavaMLSearchWindow() {
        return javaMLSearchWindow;
    }

    public void setJavaMLSearchWindow(JavaMLSearchWindow javaMLSearchWindow) {
        this.javaMLSearchWindow = javaMLSearchWindow;
    }

    public Set<String> getIncludeFeatures() {
        return includeFeatures;
    }

    public void setIncludeFeatures(Set<String> includeFeatures) {
        this.includeFeatures = includeFeatures;
    }

    public void setDtwService(DtwService dtwService) {
        this.dtwService = dtwService;
    }
}
