package de.ah7.imp.jdbc07;

import de.ah7.lib.db.DatabaseException;
import de.ah7.lib.db.sql.Column;
import de.ah7.lib.db.sql.SelectColumn;
import de.ah7.lib.db.sql.SelectStatement;
import de.ah7.lib.db.sql.SortColumn;
import de.ah7.lib.db.sql.Table;
import de.ah7.lib.db.sql.TableReference;
import de.ah7.lib.render.RenderException;
import de.ah7.lib.term.Term;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Andreas Huber <dev@ah7.de>
 */
public class SelectStatementBean implements SelectStatement {

    private static Log log = LogFactory.getLog(SelectStatementBean.class);

    /**
     */
    private List<SelectColumn> select = new ArrayList<SelectColumn>();

    public List<SelectColumn> getSelect() {
        return this.select;
    }

    /**
     */
    private List<TableReference> from = new ArrayList<TableReference>();

    public List<TableReference> getFrom() {
        return this.from;
    }

    /**
     */
    private Term<Boolean> where = null;

    public Term<Boolean> getWhere() {
        return this.where;
    }

    public void setWhere(Term<Boolean> where) {
        this.where = where;
    }

    /**
     */
    private List<Column> groupBy = new ArrayList<Column>();

    public List<Column> getGroupBy() {
        return this.groupBy;
    }

    /**
     */
    private List<SortColumn> orderBy = new ArrayList<SortColumn>();

    public List<SortColumn> getOrderBy() {
        return this.orderBy;
    }

    /**
     */
    private final Table mainTable;

    public Table getMainTable() {
        return this.mainTable;
    }

    public SelectStatementBean() {
        this.mainTable = null;
    }

    public SelectStatementBean(Table mainTable) {
        this.mainTable = mainTable;
        this.from.add(mainTable);
    }

    public PreparedStatement prepareStatement(Connection connection) throws DatabaseException {
        try {
            Query query = getQuery();
            PreparedStatement result = connection.prepareStatement(query.getQuery());
            Iterator it = query.getParams().iterator();
            int i = 1;
            while (it.hasNext()) {
                result.setObject(i++, it.next());
            }
            return result;
        } catch (RenderException ex) {
            throw new DatabaseException(ex);
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
    }

    private Query getQuery() throws RenderException {
        SQLRenderSet renderSet = new SQLRenderSet();
        StringBuffer buffer = renderSet.getBuffer();
        buffer.append("SELECT ");
        boolean first = true;
        Iterator<SelectColumn> colIt = getSelect().iterator();
        while (colIt.hasNext()) {
            if (!first) {
                buffer.append(", ");
            }
            colIt.next().render(renderSet);
            first = false;
        }
        if (first) {
            buffer.append("*");
        }
        buffer.append(" FROM ");
        first = true;
        Iterator<TableReference> tableIt = getFrom().iterator();
        while (tableIt.hasNext()) {
            if (!first) {
                buffer.append(", ");
            }
            tableIt.next().render(renderSet);
            first = false;
        }
        if (first) {
            throw new RenderException("can't render SELECT statement without tables");
        }
        if (getWhere() != null) {
            buffer.append(" WHERE ");
            getWhere().render(renderSet);
        }
        if ((getGroupBy() != null) && (getGroupBy().size() > 0)) {
            buffer.append(" GROUP BY ");
            first = true;
            Iterator<Column> groupIt = getGroupBy().iterator();
            while (groupIt.hasNext()) {
                if (!first) {
                    buffer.append(", ");
                }
                groupIt.next().render(renderSet);
                first = false;
            }
        }
        if ((getOrderBy() != null) && (getOrderBy().size() > 0)) {
            buffer.append(" ORDER BY ");
            first = true;
            Iterator<SortColumn> sortIt = getOrderBy().iterator();
            while (sortIt.hasNext()) {
                if (!first) {
                    buffer.append(", ");
                }
                sortIt.next().render(renderSet);
                first = false;
            }
        }
        log.info("executing sql: " + buffer.toString());
        return new Query(renderSet.getResult(), renderSet.getParams());
    }
}
