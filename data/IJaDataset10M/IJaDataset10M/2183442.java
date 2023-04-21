package jdbframework.tags;

import jdbframework.tags.property.TagParameterProperty;
import jdbframework.tags.property.TagStoredprocProperty;
import jdbframework.tags.property.TagQueryProperty;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;
import java.util.*;
import java.sql.*;
import jdbframework.common.DBConnectionManagement;
import jdbframework.common.DataTypeMapping;
import jdbframework.common.GeneralSettings;
import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleResultSet;

public class StatementTag extends BodyTagSupport {

    private String property = null;

    private Vector paramFieldList = null;

    private TagQueryProperty Query = null;

    private TagStoredprocProperty Storedproc = null;

    public void setProperty(String property) {
        this.property = property;
    }

    public void addParamItem(TagParameterProperty paramFields) {
        if (paramFieldList == null) {
            paramFieldList = new Vector();
        }
        paramFieldList.add(paramFields);
    }

    public void addQuery(TagQueryProperty Query) {
        this.Query = Query;
    }

    public void addStoredproc(TagStoredprocProperty Storedproc) {
        this.Storedproc = Storedproc;
    }

    public int doStartTag() throws JspException {
        return EVAL_BODY_INCLUDE;
    }

    public int doEndTag() throws JspException {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        try {
            Connection conn = DBConnectionManagement.getConnection(request);
            if (conn == null) throw new JspException("Error: DB connection is null!");
            OracleCallableStatement stmtRowCount = null;
            OracleCallableStatement stmt;
            if (Query != null) {
                stmt = (OracleCallableStatement) conn.prepareCall(Query.getQuery());
                stmtRowCount = (OracleCallableStatement) conn.prepareCall("select count(*) qnt from (" + Query.getQuery() + ")");
            } else {
                stmt = (OracleCallableStatement) conn.prepareCall(Storedproc.getStoredproc());
            }
            TagParameterProperty Param;
            DataTypeMapping dtm = new DataTypeMapping();
            if (paramFieldList != null) {
                for (int i = 0; i < paramFieldList.size(); i++) {
                    Param = (TagParameterProperty) paramFieldList.get(i);
                    dtm.setParameterStatement(stmt, Param.getProperty(), Param.getDatatype(), Param.getValue());
                    if (stmtRowCount != null) {
                        dtm.setParameterStatement(stmtRowCount, Param.getProperty(), Param.getDatatype(), Param.getValue());
                    }
                }
            }
            if (Query != null) {
                OracleResultSet rset = (OracleResultSet) dtm.executeQueryStatement(stmt);
                request.setAttribute(this.property + "resultset", rset);
                OracleResultSet rsetRowCount = (OracleResultSet) dtm.executeQueryStatement(stmtRowCount);
                rsetRowCount.next();
                request.setAttribute(this.property + "rowcount", rsetRowCount.getString("qnt"));
                rsetRowCount.getStatement().close();
                rsetRowCount.close();
            } else {
                dtm.executeStatement(stmt);
                stmt.close();
            }
            if (paramFieldList != null) {
                if (request.getSession().getAttribute(this.property + GeneralSettings.TMP_PRE_PARAMETER_LIST) != null) {
                    Vector preParamList = (Vector) request.getSession().getAttribute(this.property + GeneralSettings.TMP_PRE_PARAMETER_LIST);
                    if (preParamList.equals(paramFieldList)) {
                        request.getSession().setAttribute(this.property + GeneralSettings.TMP_PRE_PARAMETER_STATUS, "OLD");
                    } else {
                        request.getSession().setAttribute(this.property + GeneralSettings.TMP_PRE_PARAMETER_STATUS, "NEW");
                    }
                } else {
                    request.getSession().setAttribute(this.property + GeneralSettings.TMP_PRE_PARAMETER_STATUS, "NEW");
                }
            } else request.getSession().setAttribute(this.property + GeneralSettings.TMP_PRE_PARAMETER_STATUS, "OLD");
            request.getSession().setAttribute(this.property + GeneralSettings.TMP_PRE_PARAMETER_LIST, paramFieldList);
        } catch (SQLException sqlExc) {
            throw new JspException(sqlExc.getMessage());
        } catch (java.text.ParseException pe) {
            throw new JspException(pe.getMessage());
        } finally {
            paramFieldList = null;
            Query = null;
            Storedproc = null;
        }
        return EVAL_PAGE;
    }
}
