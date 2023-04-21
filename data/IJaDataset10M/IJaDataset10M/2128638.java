package com.ibm.wala.cast.js.ipa.callgraph.correlations.extraction;

import java.util.List;

/**
 * A region for the {@link ClosureExtractor} to extract.
 * 
 * @author mschaefer
 *
 */
public class ExtractionRegion {

    private int start, end;

    private final List<String> parameters;

    private final List<String> locals;

    public ExtractionRegion(int start, int end, List<String> parameters, List<String> locals) {
        super();
        this.start = start;
        this.end = end;
        this.parameters = parameters;
        this.locals = locals;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public List<String> getParameters() {
        return parameters;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public List<String> getLocals() {
        return locals;
    }
}
