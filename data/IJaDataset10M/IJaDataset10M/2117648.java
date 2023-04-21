package org.benetech.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringEscapeUtils;

/**
 * A snippet of a file, with line numbers, and a marker in a specific place.
 * Returns the contents HTML escaped, so that they can be rendered within an
 * HTML page.
 * @author Reuben Firmin
 */
public final class PreviewContext {

    private Map<Integer, String> lineMap = new LinkedHashMap<Integer, String>();

    private Integer lineNumber;

    /**
     * Default constructor.
     */
    public PreviewContext() {
    }

    /**
     * Add a line to the preview content.
     * @param lineNumber The line number
     * @param line The line contents
     */
    public void addLine(final Integer lineNumber, final String line) {
        lineMap.put(lineNumber, StringEscapeUtils.escapeHtml(line));
    }

    /**
     * Set the content's marker.
     * @param lineNumber The line to mark
     * @param columnNumber The column to mark
     */
    public void setMarker(final Integer lineNumber, final Integer columnNumber) {
        this.lineNumber = lineNumber;
    }

    /**
     * Get lines before the marker.
     * @return Never null
     */
    public List<Map.Entry<Integer, String>> getLinesBefore() {
        final List<Map.Entry<Integer, String>> lines = new ArrayList<Map.Entry<Integer, String>>();
        for (Map.Entry<Integer, String> mapping : lineMap.entrySet()) {
            if (mapping.getKey() < lineNumber) {
                mapping.setValue(mapping.getValue());
                lines.add(mapping);
            }
        }
        return lines;
    }

    /**
     * Get lines after the marker.
     * @return Never null
     */
    public List<Map.Entry<Integer, String>> getLinesAfter() {
        final List<Map.Entry<Integer, String>> lines = new ArrayList<Map.Entry<Integer, String>>();
        for (Map.Entry<Integer, String> mapping : lineMap.entrySet()) {
            if (mapping.getKey() > lineNumber) {
                mapping.setValue(mapping.getValue());
                lines.add(mapping);
            }
        }
        return lines;
    }

    /**
     * Get the marked line.
     * @return Never null
     */
    public String getLine() {
        return lineMap.get(lineNumber);
    }

    /**
     * Get the line number of the marker line.
     * @return Never null
     */
    public Integer getLineNumber() {
        return lineNumber;
    }
}
