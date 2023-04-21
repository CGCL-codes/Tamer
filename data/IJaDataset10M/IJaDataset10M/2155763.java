package com.wavechain.ale;

import java.util.List;
import org.apache.log4j.Logger;

public class ECFilterSpec {

    public static final String INCLUDE_PATTERNS = "includePatterns";

    public static final String EXCLUDE_PATTERNS = "excludePatterns";

    private List includePatterns;

    private List excludePatterns;

    Logger log = null;

    public ECFilterSpec(List includePatterns, List excludePatterns) {
        this.includePatterns = includePatterns;
        this.excludePatterns = excludePatterns;
        log = Logger.getLogger(this.getClass());
    }

    public List getIncludePatterns() {
        return includePatterns;
    }

    public void setIncludePatterns(List x) {
        includePatterns = x;
    }

    public void addIncludePattern(String x) {
        includePatterns.add(x);
        log.debug("added includePattern = " + x);
    }

    public void deleteIncludePattern(String x) {
        includePatterns.remove(x);
    }

    public List getExcludePatterns() {
        return excludePatterns;
    }

    public void setExcludePatterns(List x) {
        excludePatterns = x;
    }

    public void addExcludePattern(String x) {
        log.debug("added excludePattern = " + x);
        excludePatterns.add(x);
    }

    public void deleteExcludePattern(String x) {
        excludePatterns.remove(x);
    }
}
