package org.vardb.alignment;

import java.util.HashMap;
import java.util.Map;
import org.vardb.sequences.CSequenceFileParser;

public class CAlignmentResults {

    protected String alignment;

    protected String dendrogram;

    protected Map<String, String> map = new HashMap<String, String>();

    protected CConsensus consensus;

    public String getAlignment() {
        return this.alignment;
    }

    public void setAlignment(String alignment) {
        this.alignment = alignment;
    }

    public String getDendrogram() {
        return this.dendrogram;
    }

    public void setDendrogram(String dendrogram) {
        this.dendrogram = dendrogram;
    }

    public Map<String, String> getMap() {
        return this.map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }

    public CAlignmentResults() {
    }

    public CAlignmentResults(Map<String, String> sequences) {
        this.map = sequences;
        this.alignment = CSequenceFileParser.writeFasta(sequences);
    }

    public int getLength() {
        String sequence = this.map.values().iterator().next();
        return sequence.length();
    }

    public CConsensus getConsensus() {
        if (this.consensus == null) this.consensus = new CConsensus(this.map);
        return this.consensus;
    }

    public int getNumsequences() {
        return this.map.size();
    }

    public int getNumcolumns() {
        return getConsensus().getPositions().size();
    }
}
