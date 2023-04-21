package javarequirementstracer;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Helper class for creating XHTML.
 * 
 * @author Ronald Koster
 */
final class XhtmlBuilder {

    private static final String TABLE_START = "\n<table border='1' cellspacing='0' cellpadding='3'";

    private static final String TABLE_END = "\n</table>";

    private static final String ROW_START = "\n<tr>";

    private static final String ROW_END = "\n</tr>";

    private static final String HEADER_START = "\n<th>";

    private static final String HEADER_END = "</th>";

    private static final String COLUMN_START = "\n<td>";

    private static final String COLUMN_END = "</td>";

    private static final String PAR_START = "\n<p>\n";

    private static final String PAR_END = "\n</p>";

    private static final String BR = "\n<br/>";

    private static final String SPACE = " ";

    static final String NBSP = "&nbsp;";

    private static final String TAB = NBSP + NBSP + NBSP + NBSP;

    static final String SPAN_END = "</span>";

    private static final String STYLE = "\n<style type='text/css'>" + "\ndiv.percentgraph {" + "\n    background-color: #f02020;" + "\n    border: #808080 1px solid;" + "\n    height: 1.3em;" + "\n    magin: 0px;" + "\n    padding: 0px;" + "\n}" + "\ndiv.greenbar {" + "\n    background-color: #00f000;" + "\n    height: 1.3em;" + "\n    magin: 0px;" + "\n    padding: 0px;" + "\n}" + "\n</style>";

    private final StringBuilder bldr = new StringBuilder();

    private boolean parStarted = false;

    @Override
    public String toString() {
        return this.bldr.toString();
    }

    XhtmlBuilder append(Object obj) {
        this.bldr.append(obj);
        return this;
    }

    XhtmlBuilder append(AttributeId spanId, Object obj) {
        return spanStart(spanId).append(obj).spanEnd();
    }

    XhtmlBuilder start(String title) {
        append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
        return append("\n<html>\n<head>\n<title>").append(title).append("</title>").append(STYLE).append("\n</head>\n<body>").heading(1, title);
    }

    XhtmlBuilder end() {
        return append("\n\n</body>\n</html>");
    }

    XhtmlBuilder heading(int level, String title) {
        return append("\n\n<h").append(level).append(">").append(title).append("</h").append(level).append(">");
    }

    XhtmlBuilder parStart() {
        if (this.parStarted) {
            throw new IllegalStateException("Paragraph not ended yet.");
        }
        this.parStarted = true;
        return append(PAR_START);
    }

    XhtmlBuilder parEnd() {
        if (!this.parStarted) {
            throw new IllegalStateException("Paragraph not started yet.");
        }
        this.parStarted = false;
        return append(PAR_END);
    }

    XhtmlBuilder spanStart(AttributeId id) {
        this.bldr.append("<span");
        return attId(id);
    }

    XhtmlBuilder spanEnd() {
        this.bldr.append(SPAN_END);
        return this;
    }

    private XhtmlBuilder attId(AttributeId id) {
        this.bldr.append(" id='").append(id).append("'>");
        return this;
    }

    XhtmlBuilder br() {
        return append(BR);
    }

    XhtmlBuilder space() {
        return append(SPACE);
    }

    XhtmlBuilder nbsp() {
        return append(NBSP);
    }

    XhtmlBuilder tab() {
        return append(TAB);
    }

    XhtmlBuilder table(final AttributeId id, final Map<String, String> map, final String... headers) {
        List<Collection<String>> rows = new ArrayList<Collection<String>>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            List<String> row = new ArrayList<String>();
            row.add(entry.getKey());
            row.add(entry.getValue());
            rows.add(row);
        }
        return table(id, rows, headers);
    }

    XhtmlBuilder table(final AttributeId id, final Collection<Collection<String>> rows, final String... headers) {
        validateTrue("At least rows or headers must not be empty.", rows != null && rows.size() > 0 || headers != null && headers.length > 0);
        this.bldr.append(TABLE_START);
        attId(id);
        if (headers != null && headers.length > 0) {
            appendHeaders(headers);
        }
        if (rows != null) {
            appendRows(rows);
        }
        return append(TABLE_END);
    }

    private void appendHeaders(final String... headers) {
        append(ROW_START);
        for (String header : headers) {
            append(HEADER_START).append(header).append(HEADER_END);
        }
        append(ROW_END);
    }

    private void appendRows(final Collection<Collection<String>> rows) {
        for (Collection<String> row : rows) {
            append(ROW_START);
            for (String column : row) {
                append(COLUMN_START);
                append(column == null || column.length() == 0 ? NBSP : column);
                append(COLUMN_END);
            }
            append(ROW_END);
        }
    }

    void write(final File reportFile) {
        FileUtils.writeFile(reportFile, toString());
    }

    private void validateTrue(String msg, boolean condition) {
        if (!condition) {
            throw new IllegalArgumentException(msg);
        }
    }
}
