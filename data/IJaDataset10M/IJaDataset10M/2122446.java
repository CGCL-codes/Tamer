package org.spantus.extract.segments.offline;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import org.spantus.core.FrameValues;
import org.spantus.core.marker.MarkerSet;

public class ExtremeOfflineCtx {

    private FrameValues values;

    private ExtremeSequences sequence;

    private List<ExtremeSegment> segments;

    private MarkerSet markerSet;

    public FrameValues getValues() {
        return values;
    }

    public void setValues(FrameValues values) {
        this.values = values;
    }

    public ExtremeSequences getSequence() {
        return sequence;
    }

    public void setSequence(ExtremeSequences sequence) {
        this.sequence = sequence;
    }

    public Map<Integer, ExtremeEntry> getExtremes() {
        return sequence.toMap();
    }

    public ListIterator<ExtremeSegment> getSegmentsIterator() {
        return ((LinkedList<ExtremeSegment>) segments).listIterator();
    }

    public List<ExtremeSegment> getSegments() {
        return segments;
    }

    public void setSegments(List<ExtremeSegment> segments) {
        this.segments = segments;
    }

    public MarkerSet getMarkerSet() {
        return markerSet;
    }

    public void setMarkerSet(MarkerSet markerSet) {
        this.markerSet = markerSet;
    }

    public Float getSampleRate() {
        return getValues().getSampleRate();
    }
}
