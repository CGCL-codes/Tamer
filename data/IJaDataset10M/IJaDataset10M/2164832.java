package org.zamia.instgraph;

import java.util.ArrayList;

/**
 * result of IGContainer:resolve(id)
 * 
 * @author Guenter Bartsch
 * 
 */
public class IGResolveResult {

    private final ArrayList<IGItem> fResults;

    private boolean fContainsSubPrograms = false;

    public IGResolveResult() {
        this(new ArrayList<IGItem>());
    }

    public IGResolveResult(ArrayList<IGItem> aResults) {
        fResults = aResults;
    }

    public int getNumResults() {
        return fResults.size();
    }

    public IGItem getResult(int aIdx) {
        return fResults.get(aIdx);
    }

    public void addItem(IGItem aItem) {
        fResults.add(aItem);
        if (aItem instanceof IGSubProgram) fContainsSubPrograms = true;
    }

    public boolean isContainsSubPrograms() {
        return fContainsSubPrograms;
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder("IGResolveResult(nResults=" + fResults.size());
        buf.append(")");
        return buf.toString();
    }

    public boolean isEmpty() {
        return fResults.isEmpty();
    }
}
