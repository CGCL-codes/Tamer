package org.spantus.extractor.segments.offline;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;
import org.spantus.core.FrameValues;
import org.spantus.core.marker.Marker;
import org.spantus.core.marker.MarkerSet;
import org.spantus.core.marker.MarkerSetHolder;
import org.spantus.core.marker.service.IMarkerService;
import org.spantus.core.marker.service.MarkerServiceFactory;
import org.spantus.extractor.segments.ExtremeSegmentServiceImpl;
import org.spantus.extractor.segments.offline.ExtremeEntry.FeatureStates;
import org.spantus.logger.Logger;
import org.spantus.math.cluster.ClusterCollection;
import org.spantus.math.services.MathServicesFactory;

public class ExtremeClassifierServiceImpl {

    Logger log = Logger.getLogger(ExtremeClassifierServiceImpl.class);

    IMarkerService markerService;

    private ExtremeSegmentServiceImpl extremeSegmentService;

    public ExtremeClassifierServiceImpl() {
        markerService = MarkerServiceFactory.createMarkerService();
    }

    /**
	 * 
	 * @param extremes
	 * @return
	 */
    public ExtremeOfflineCtx calculateSegments(FrameValues values) {
        ExtremeOfflineCtx ctx = new ExtremeOfflineCtx();
        ctx.setValues(values);
        extractExtremes(ctx);
        extractSements(ctx);
        initialCleanup(ctx);
        extractMarkerSet(ctx);
        return ctx;
    }

    /**
	 * 
	 * @param extremeCtx
	 * @return
	 */
    public List<ExtremeSegment> extractSements(ExtremeOfflineCtx extremeCtx) {
        List<ExtremeSegment> segments = new LinkedList<ExtremeSegment>();
        ExtremeSequences sequence = extremeCtx.getSequence();
        for (ExtremeListIterator iter = sequence.extreamsListIterator(); iter.hasNext(); ) {
            ExtremeEntry entry = iter.next();
            if (iter.isCurrentMaxExtream()) {
                ExtremeSegment extremeSegment = new ExtremeSegment();
                extremeSegment.setPeakEntry(entry);
                extremeSegment.setStartEntry(entry.getPrevious());
                extremeSegment.setEndEntry(entry.getNext());
                segments.add(extremeSegment);
            }
        }
        extremeCtx.setSegments(segments);
        if (log.isDebugMode()) {
            log.debug("[extractSements]{1} segments: {0}", segments, segments.size());
        }
        return segments;
    }

    /**
	 * extract min/max from signal
	 * 
	 * @param values
	 * @return
	 */
    public Map<Integer, ExtremeEntry> extractExtremes(ExtremeOfflineCtx extremeCtx) {
        FrameValues values = extremeCtx.getValues();
        Map<Integer, ExtremeEntry> extremes1 = new TreeMap<Integer, ExtremeEntry>();
        ExtremeSequences sequence = new ExtremeSequences(extremes1.values(), values);
        extremeCtx.setSequence(sequence);
        ExtremeListIterator iterator = sequence.extreamsListIterator();
        if (values.size() == 0) {
            return sequence.toMap();
        }
        int index = 0;
        Double previous = null;
        FeatureStates maxState = FeatureStates.stable;
        FeatureStates minState = FeatureStates.stable;
        ListIterator<Double> listIter = values.listIterator();
        while (listIter.hasNext()) {
            Double value = (Double) listIter.next();
            if (previous == null) {
                previous = value;
                index++;
                continue;
            }
            if (value > previous) {
                log.debug("[extractExtremes]found 1st min on {0} value {1}", index - 1, previous);
                break;
            }
            previous = value;
            index++;
        }
        listIter.previous();
        index--;
        ExtremeEntry firstMinExtreamEntry = new ExtremeEntry(index, previous, FeatureStates.min);
        iterator.add(firstMinExtreamEntry);
        log.debug("[extractExtremes]adding min  {0} ", firstMinExtreamEntry.toString());
        while (listIter.hasNext()) {
            Double value = (Double) listIter.next();
            int entryIndex = index;
            if (value > previous) {
                maxState = FeatureStates.max;
            } else {
                if (FeatureStates.max.equals(maxState)) {
                    ExtremeEntry currentExtreamEntry = new ExtremeEntry(entryIndex, previous, maxState);
                    iterator.add(currentExtreamEntry);
                    log.debug("[extractExtremes]adding max  {0} ", currentExtreamEntry);
                }
                maxState = FeatureStates.decreasing;
            }
            if (value < previous) {
                minState = FeatureStates.min;
            } else {
                if (FeatureStates.min.equals(minState)) {
                    ExtremeEntry currentExtreamEntry = new ExtremeEntry(entryIndex, previous, minState);
                    log.debug("[extractExtremes]adding min  {0} ", currentExtreamEntry);
                    iterator.add(currentExtreamEntry);
                }
                minState = FeatureStates.increasing;
            }
            previous = value;
            index++;
        }
        if (iterator.isCurrentMaxExtream()) {
            ExtremeEntry lastMinExtreamEntry = new ExtremeEntry(index, previous, FeatureStates.min);
            iterator.add(lastMinExtreamEntry);
            log.debug("[extractExtremes]adding min  {0} ", lastMinExtreamEntry.toString());
        }
        return sequence.toMap();
    }

    /**
	 * 
	 * @param sequence
	 */
    public List<ExtremeSegment> initialCleanup(ExtremeOfflineCtx ctx) {
        if (log.isDebugMode()) {
            for (ExtremeSegment extremeSegment : ctx.getSegments()) {
                log.debug("[initialCleanup]before intial cleanup {0} ", extremeSegment);
            }
        }
        LinkedList<ExtremeSegment> segments = new LinkedList<ExtremeSegment>();
        ExtremeSegment previous = null;
        for (ListIterator<ExtremeSegment> iter = ctx.getSegmentsIterator(); iter.hasNext(); ) {
            ExtremeSegment entry = iter.next();
            log.debug("[initialCleanup]++++++++++++++++++++++++++++++");
            if (previous == null) {
                segments.add(entry);
                log.debug("[initialCleanup]reusing first:{0}; exists: {1} ", entry, segments.size());
            } else {
                if (getExtremeSegmentService().isIncrease(entry) && getExtremeSegmentService().isIncrease(previous)) {
                    log.debug("[initialCleanup]remove:{0}; exists: {1} ", previous, segments.size());
                    ExtremeSegment eliminated = segments.removeLast();
                    ExtremeSegment joined = join(eliminated, entry);
                    log.debug("[initialCleanup] /// inc:true; inc:true ");
                    segments.add(joined);
                    log.debug("[initialCleanup]joined:{0}; size: {1} ", joined, segments.size());
                } else if (getExtremeSegmentService().isDecrease(entry) && getExtremeSegmentService().isDecrease(previous)) {
                    log.debug("[initialCleanup]remove:{0}; size: {1} ", previous, segments.size());
                    ExtremeSegment eliminated = segments.removeLast();
                    ExtremeSegment joined = join(eliminated, entry);
                    log.debug("[initialCleanup] \\\\\\ dec:true; dec:true ");
                    segments.add(joined);
                    log.debug("[initialCleanup]joined:{0}; size: {1} ", joined, segments.size());
                } else {
                    segments.add(entry);
                    log.debug("[initialCleanup]reusing:{0}; size: {1} ", entry, segments.size());
                }
                log.debug("[initialCleanup]size {0} ", segments.size());
            }
            previous = entry;
        }
        ctx.setSegments(segments);
        if (log.isDebugMode()) {
            for (ExtremeSegment extremeSegment : segments) {
                log.debug("[initialCleanup]after intial cleanup {0} (anngle: {1})", extremeSegment, getExtremeSegmentService().angle(extremeSegment));
            }
        }
        return ctx.getSegments();
    }

    /**
	 * 
	 * @param s1
	 * @param s2
	 * @return
	 */
    public ExtremeSegment join(ExtremeSegment s1, ExtremeSegment s2) {
        ExtremeSegment joined = new ExtremeSegment();
        ExtremeSegment prior = s1;
        ExtremeSegment next = s2;
        if (s1.getEndEntry().getIndex().compareTo(s2.getEndEntry().getIndex()) > 0) {
            prior = s2;
            next = s1;
        }
        joined.setStartEntry(prior.getStartEntry());
        joined.setEndEntry(next.getEndEntry());
        if (s1.getPeakEntry().getValue().compareTo(s2.getPeakEntry().getValue()) > 0) {
            joined.setPeakEntry(s1.getPeakEntry());
        } else {
            joined.setPeakEntry(s2.getPeakEntry());
        }
        return joined;
    }

    public boolean remove(List<ExtremeSegment> segments, ExtremeSegment removeTo) {
        return segments.remove(removeTo);
    }

    /**
	 * 
	 * @param vectors
	 * @param centers
	 */
    public void writeDebug(List<List<Double>> vectors, ClusterCollection centers) {
        for (Entry<Integer, List<Double>> entry : centers.entrySet()) {
            List<Double> list = entry.getValue();
            String seperator = "";
            StringBuilder sb = new StringBuilder();
            for (Double float1 : list) {
                sb.append(seperator).append(float1);
                seperator = ";";
            }
            log.debug("cluster {0} center: area, length:[{1}]", entry.getKey(), sb.toString());
        }
    }

    /**
	 * 
	 * 
	 * 
	 * 
	 */
    protected ClusterCollection calculateCenters(ExtremeSequences allExtriemesSequence) {
        List<List<Double>> vectors = new ArrayList<List<Double>>();
        for (ExtremeListIterator iter = allExtriemesSequence.extreamsListIterator(); iter.hasNext(); ) {
            iter.next();
            if (iter.isCurrentMaxExtream()) {
                List<Double> point = createLearnVector(iter.getPeakLength(), iter.getArea());
                vectors.add(point);
            }
        }
        if (vectors.size() == 0) {
            throw new IllegalArgumentException("No data found for clustering.");
        }
        ClusterCollection centers = MathServicesFactory.createKnnService().cluster(vectors, getClusterSize());
        writeDebug(vectors, centers);
        return centers;
    }

    protected List<Double> createLearnVector(Long length, Double area) {
        return createVector(length, area);
    }

    protected List<Double> createMatchVector(Long length, Double area) {
        return createVector(length, area);
    }

    /**
	 * 
	 * @param lenght
	 * @param area
	 * @return
	 */
    protected List<Double> createVector(Long length, Double area) {
        List<Double> vector = new ArrayList<Double>();
        vector.add(area);
        return vector;
    }

    /**
	 * 
	 * @param extriemesSequence
	 * @return
	 */
    public MarkerSet extractMarkerSet(ExtremeOfflineCtx ctx) {
        MarkerSet markerSet = new MarkerSet();
        ctx.setMarkerSet(markerSet);
        int i = 0;
        if (ctx.getSequence().size() < 6) {
            return markerSet;
        }
        ClusterCollection clusterCollection = calculateCenters(ctx.getSequence());
        Double sampleRate = ctx.getSampleRate();
        for (ExtremeListIterator iter = ctx.getSequence().extreamsListIterator(); iter.hasNext(); ) {
            ExtremeEntry entry = iter.next();
            if (iter.isPreviousMinExtream() && iter.isCurrentMaxExtream() && iter.isNextMinExtream()) {
                Integer clusterID = clusterCollection.matchClusterClass(createMatchVector(iter.getPeakLength(), iter.getArea()));
                if (clusterID == 0) {
                    continue;
                }
                Marker marker = createMarker(entry, sampleRate);
                marker.setLabel(MessageFormat.format("{0}:{1}", i, clusterID));
                markerSet.getMarkers().add(marker);
                i++;
            }
        }
        markerSet.setMarkerSetType(MarkerSetHolder.MarkerSetHolderEnum.phone.name());
        return markerSet;
    }

    /**
	 * 
	 * @param entry
	 * @param sampleRate
	 * @return
	 */
    protected Marker createMarker(ExtremeEntry entry, Double sampleRate) {
        Marker marker = new Marker();
        Integer startInSample = entry.getPrevious().getIndex();
        Integer endInSample = entry.getNext().getIndex();
        Long start = markerService.getTime(startInSample, sampleRate);
        Long end = markerService.getTime(endInSample, sampleRate);
        marker.setStart(start);
        marker.setEnd(end);
        marker.getExtractionData().setStartSampleNum(startInSample.longValue());
        marker.getExtractionData().setEndSampleNum(endInSample.longValue());
        return marker;
    }

    protected int getClusterSize() {
        return 3;
    }

    protected Double getMaxLength() {
        return .2D;
    }

    public ExtremeSegmentServiceImpl getExtremeSegmentService() {
        if (extremeSegmentService == null) {
            extremeSegmentService = new ExtremeSegmentServiceImpl();
        }
        return extremeSegmentService;
    }

    public void setExtremeSegmentService(ExtremeSegmentServiceImpl extremeSegmentService) {
        this.extremeSegmentService = extremeSegmentService;
    }
}
