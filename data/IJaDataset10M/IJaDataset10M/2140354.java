package org.jrobin.graph;

import org.jrobin.core.RrdException;
import org.jrobin.core.Util;
import org.jrobin.data.DataProcessor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class PrintText extends CommentText {

    static final String UNIT_MARKER = "([^%]?)%(s|S)";

    static final Pattern UNIT_PATTERN = Pattern.compile(UNIT_MARKER);

    private final String srcName, consolFun;

    private final boolean includedInGraph;

    PrintText(String srcName, String consolFun, String text, boolean includedInGraph) {
        super(text);
        this.srcName = srcName;
        this.consolFun = consolFun;
        this.includedInGraph = includedInGraph;
    }

    boolean isPrint() {
        return !includedInGraph;
    }

    void resolveText(DataProcessor dproc, ValueScaler valueScaler) throws RrdException {
        super.resolveText(dproc, valueScaler);
        if (resolvedText != null) {
            double value = dproc.getAggregate(srcName, consolFun);
            Matcher matcher = UNIT_PATTERN.matcher(resolvedText);
            if (matcher.find()) {
                ValueScaler.Scaled scaled = valueScaler.scale(value, matcher.group(2).equals("s"));
                resolvedText = resolvedText.substring(0, matcher.start()) + matcher.group(1) + scaled.unit + resolvedText.substring(matcher.end());
                value = scaled.value;
            }
            resolvedText = Util.sprintf(resolvedText, value);
            trimIfGlue();
        }
    }
}
