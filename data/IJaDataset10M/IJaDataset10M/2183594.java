package com.jedox.etl.core.source.filter;

import java.util.regex.Pattern;
import com.jedox.etl.core.component.ConfigurationException;

/**
 * Evaluates if an alphanumeric expression lies within a specific range  
 * @author Christian Schwarzinger. Mail: christian.schwarzinger@proclos.com
 *
 */
public class AlphaRangeEvaluator implements IEvaluator {

    private String startString;

    private String endString;

    private boolean includeStart = true;

    private boolean includeEnd = true;

    public AlphaRangeEvaluator(String definition) throws ConfigurationException {
        String regex = "(\\[|\\()(\\w|[\\.;+\\- ])*,(\\w|[\\.;+\\- ])*(\\]|\\))";
        Pattern rangePattern = Pattern.compile(regex);
        if (rangePattern.matcher(definition).matches()) {
            startString = definition.split(",")[0];
            String startMode = startString.substring(0, 1);
            startString = startString.substring(1);
            endString = definition.split(",")[1];
            String endMode = endString.substring(endString.length() - 1, endString.length());
            endString = endString.substring(0, endString.length() - 1);
            if (startMode.equals("(")) includeStart = false;
            if (endMode.equals(")")) includeEnd = false;
        } else throw new ConfigurationException("Alphanumeric range definition is not valid: " + definition);
    }

    private boolean evaluateStart(String value) {
        return (includeStart) ? (startString.compareToIgnoreCase(value) <= 0) : (startString.compareToIgnoreCase(value) < 0);
    }

    private boolean evaluateEnd(String value) {
        return (includeEnd) ? (endString.compareToIgnoreCase(value) >= 0) : (endString.compareToIgnoreCase(value) > 0);
    }

    public boolean evaluate(Object value) {
        return evaluateStart(value.toString()) && evaluateEnd(value.toString());
    }
}
