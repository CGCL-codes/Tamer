package org.spantus.exp.segment.exec.classification;

import java.util.List;

public class ExpCriteria {

    List<String> signalNames;

    List<String> noiseNames;

    String markerName;

    public List<String> getSignalNames() {
        return signalNames;
    }

    public void setSignalNames(List<String> signalNames) {
        this.signalNames = signalNames;
    }

    public List<String> getNoiseNames() {
        return noiseNames;
    }

    public void setNoiseNames(List<String> noiseNames) {
        this.noiseNames = noiseNames;
    }

    public String getMarkerName() {
        return markerName;
    }

    public void setMarkerName(String markerName) {
        this.markerName = markerName;
    }
}
