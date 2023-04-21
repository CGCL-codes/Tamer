package org.ujac.print.ide.editors.xml.format;

/**
 * @author Phil Zoio
 */
public class DocTypeFormattingStrategy extends DefaultFormattingStrategy {

    public String format(String content, boolean isLineStart, String indentation, int[] positions) {
        return lineSeparator + content;
    }
}
