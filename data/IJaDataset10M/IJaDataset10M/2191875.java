package com.jvantage.ce.presentation.html;

import org.apache.commons.lang.StringUtils;
import java.util.*;

/**
 *  Displays a panel of icons and/or text in a row, such as:
 *<PRE>
 *      +------+-------------------------------------+
 *      |  XX  |  XX  |  Show a record list          |
 *      +------+-------------------------------------+
 *</PRE>
 *
 *  Simply add content in the form of HTML text.
 *
 * @author  Administrator
 */
public class PanelDisplayer implements java.io.Serializable {

    private ArrayList contentList = null;

    private String delimiterString = null;

    /** Creates a new instance of PanelDisplayer */
    public PanelDisplayer() {
        contentList = new ArrayList();
    }

    /**
     *  Creates a new cell at the right-most position of the panel
     *  and sets its content.
     */
    public void addContent(String content) {
        if (StringUtils.isBlank(content)) {
            content = HTML.noBreakSpace();
        }
        contentList.add(content);
    }

    /**
     * Getter for property delimiterString.
     * @return Value of property delimiterString.
     */
    public java.lang.String getDelimiterString() {
        return delimiterString;
    }

    public String render() {
        StringBuffer content = new StringBuffer();
        content.append(HTML.tableBeg()).append(HTML.tableRowBeg());
        boolean delimiterIsDefined = StringUtils.isNotBlank(getDelimiterString());
        final String tdWidth = "";
        String cellContent = null;
        for (int i = 0; i < contentList.size(); i++) {
            if (delimiterIsDefined && (i > 0)) {
                content.append(HTML.tableData(getDelimiterString(), tdWidth));
            }
            cellContent = StringUtils.defaultString((String) contentList.get(i), HTML.noBreakSpace());
            content.append(HTML.tableData(cellContent, tdWidth));
        }
        content.append(HTML.tableRowEnd()).append(HTML.tableEnd());
        return content.toString();
    }

    /**
     *  Sets the content (argument) to the specified position
     *  in the panel.  The left-most cell is position zero.
     */
    public void setCellContent(int position, String content) {
        if (position < 0) {
            return;
        }
        if (StringUtils.isBlank(content)) {
            content = HTML.noBreakSpace();
        }
        if (contentList.size() < (position + 1)) {
            contentList.ensureCapacity(position + 1);
        }
        contentList.set(position, content);
    }

    /**
     * Setter for property delimiterString.  The delimiter string is
     * most likely a spacer image or series of non-breaking spaces
     * to push the cells apart.  This can better be accomplished using
     * CSS styles.
     *
     * @param delimiterString New value of property delimiterString.
     */
    public void setDelimiterString(java.lang.String delimiterString) {
        this.delimiterString = delimiterString;
    }
}
