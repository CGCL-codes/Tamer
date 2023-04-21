package cn.myapps.core.report.crossreport.runtime.analyzer;

import java.io.Serializable;
import java.util.Comparator;

/**
 * The comparator for the analyse data key.
 * 
 */
public class AnalyseDataKeyComparator implements Comparator, Serializable {

    private static final long serialVersionUID = 2073917551730063109L;

    public int compare(Object arg0, Object arg1) {
        if (arg0 instanceof AnalyseDataKeyPair && arg1 instanceof AnalyseDataKeyPair && arg0 != null && arg1 != null) {
            return ((AnalyseDataKeyPair) arg0).compareTo((AnalyseDataKeyPair) arg1);
        }
        return 0;
    }
}
