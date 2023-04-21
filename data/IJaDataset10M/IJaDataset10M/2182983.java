package org.processmining.framework.ui.slicker.launch;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * @author christian
 * 
 */
public class ActionFilter {

    protected Pattern searchPattern;

    protected boolean useMining;

    protected boolean useAnalysis;

    protected boolean useConversion;

    protected boolean useExport;

    public ActionFilter(String search, boolean mining, boolean analysis, boolean conversion, boolean export) {
        useMining = mining;
        useAnalysis = analysis;
        useConversion = conversion;
        useExport = export;
        search = search.trim().toLowerCase();
        search = search.replaceAll("(\\s)+", "(.*)");
        search = "(.*)" + search + "(.*)";
        try {
            searchPattern = Pattern.compile(search);
        } catch (PatternSyntaxException pse) {
            searchPattern = Pattern.compile("(.*)");
        }
    }

    public boolean filter(String name) {
        return searchPattern.matcher(name).matches();
    }

    public boolean useMining() {
        return useMining;
    }

    public boolean useAnalysis() {
        return useAnalysis;
    }

    public boolean useConversion() {
        return useConversion;
    }

    public boolean useExport() {
        return useExport;
    }
}
