package de.mse.mogwai.forms;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;
import org.apache.struts.taglib.html.FormTag;

public class TableLayoutTag extends StrutsBodyTagSupport {

    private Vector m_columns;

    private Vector m_rows;

    private String m_spacerGif;

    private String m_debug;

    private Vector m_cells;

    private class Rec {

        int m_column;

        int m_row;

        int m_width;

        int m_height;

        String m_content;

        public Rec(int column, int row, int width, int height, String content) {
            this.m_column = column;
            this.m_row = row;
            this.m_width = width;
            this.m_height = height;
            this.m_content = content;
        }

        public String toString() {
            return "C" + m_column + " R" + m_row + " W" + m_width + " H" + m_height;
        }
    }

    public void setCellContent(String column, String row, String width, String height, String value) {
        Rec rec = new Rec(Integer.parseInt(column), Integer.parseInt(row), Integer.parseInt(width), Integer.parseInt(height), value);
        this.m_cells.add(rec);
    }

    private Rec getCellAt(int row, int column) {
        for (Iterator it = this.m_cells.iterator(); it.hasNext(); ) {
            Rec rec = (Rec) it.next();
            if ((row >= rec.m_row) && (row < rec.m_row + rec.m_height) && (column >= rec.m_column) && (column < rec.m_column + rec.m_width)) {
                return rec;
            }
        }
        return null;
    }

    public void setPageContext(PageContext context) {
        super.setPageContext(context);
        this.m_spacerGif = "images/spacer.gif";
        this.m_cells = new Vector();
    }

    public int doEndTag() throws JspException {
        JspWriter out = this.pageContext.getOut();
        try {
            out.print("<table cellspacing=\"0\" cellpadding=\"0\"");
            if ("true".equals(this.m_debug)) out.print(" border=\"1\"");
            out.print(">");
            out.println("<tr>");
            for (int col = 0; col < this.m_columns.size(); col++) {
                String colDef = (String) this.m_columns.get(col);
                if (colDef.equals("*")) colDef = "100%";
                out.print("	<td style=\"width:" + colDef + "\" width=\"" + colDef + "\">");
                if (!colDef.equals("100%")) {
                    out.print("<img src=\"" + this.m_spacerGif + "\" border=\"0\" width=\"" + colDef + "\" height=\"1\">");
                }
                out.println("</td>");
            }
            out.println("</tr>");
            for (int row = 0; row < this.m_rows.size(); row++) {
                String rowDef = (String) this.m_rows.get(row);
                if (rowDef.equals("*")) rowDef = "100%";
                out.println("<tr height=\"" + rowDef + "\">");
                for (int col = 0; col < this.m_columns.size(); col++) {
                    String colDef = (String) this.m_columns.get(col);
                    Rec rec = this.getCellAt(row + 1, col + 1);
                    if (rec == null) {
                        out.println("	<td></td>");
                    } else {
                        if ((rec.m_column == col + 1) && (rec.m_row == row + 1)) {
                            out.print("	<td valign=\"top\" colspan=\"" + rec.m_width + "\" rowspan=\"" + rec.m_height + "\">");
                            out.println(rec.m_content.trim() + "</td>");
                        }
                    }
                }
                out.println("</tr>");
            }
            out.print("</table>");
        } catch (IOException e) {
            throw new JspException(e);
        }
        return EVAL_PAGE;
    }

    public void setColumns(String columns) {
        this.m_columns = new Vector();
        StringTokenizer st = new StringTokenizer(columns, ",");
        while (st.hasMoreTokens()) this.m_columns.add(st.nextToken());
    }

    public void setRows(String rows) {
        this.m_rows = new Vector();
        StringTokenizer st = new StringTokenizer(rows, ",");
        while (st.hasMoreTokens()) this.m_rows.add(st.nextToken());
    }

    public void setDebug(String debug) {
        this.m_debug = debug;
    }

    public void setSpacerGif(String name) {
        this.m_spacerGif = name;
    }
}
