package tags.jdbc;

import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import beans.jdbc.Query;

public class RowsTag extends BodyTagSupport {

    private static int UNASSIGNED = -1;

    private ResultSet rs;

    private boolean keepGoing;

    private String query;

    private int startRow = UNASSIGNED, endRow = UNASSIGNED;

    public void setQuery(String query) {
        this.query = query;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public void setEndRow(int endRow) {
        this.endRow = endRow;
    }

    public int doStartTag() throws JspException {
        rs = Query.getResult(pageContext, query);
        try {
            if (startRow == UNASSIGNED) {
                keepGoing = rs.next();
            } else {
                if (startRow < 1) startRow = 1;
                keepGoing = rs.absolute(startRow);
            }
        } catch (Exception ex) {
            throw new JspException(ex.getMessage());
        }
        if (keepGoing) return EVAL_BODY_TAG; else return SKIP_BODY;
    }

    public int doAfterBody() throws JspException {
        try {
            if (endRow == UNASSIGNED) {
                if (rs.isLast()) {
                    rs.beforeFirst();
                    writeBodyContent();
                    return SKIP_BODY;
                }
            } else {
                if (rs.getRow() == endRow || rs.isLast()) {
                    rs.beforeFirst();
                    writeBodyContent();
                    return SKIP_BODY;
                }
            }
            rs.next();
        } catch (java.sql.SQLException ex) {
            throw new JspException(ex.getMessage());
        }
        return EVAL_BODY_TAG;
    }

    public void writeBodyContent() throws JspException {
        try {
            bodyContent.writeOut(pageContext.getOut());
        } catch (Exception e) {
            throw new JspException(e.getMessage());
        }
    }
}
