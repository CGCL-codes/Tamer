package org.tm4j.tologx.parser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tm4j.tologx.utils.VariableSet;
import org.tm4j.topicmap.TopicMapObject;

/**
 * 
 * @author Kal Ahmed (kal@techquila.com)
 */
public class ProjectionImpl implements Projection {

    private List m_columns;

    private List m_rows;

    private static final Log m_log = LogFactory.getLog(ProjectionImpl.class);

    public ProjectionImpl() {
        m_columns = new ArrayList();
        m_rows = new ArrayList();
    }

    public void addVariable(Variable var) {
        m_columns.add(var);
    }

    public void addCount(Variable var) {
        m_columns.add(new Count(var));
    }

    public void addVariableSet(VariableSet varSet) {
        if (m_columns.isEmpty()) {
            Iterator it = varSet.getColumns().iterator();
            while (it.hasNext()) {
                m_columns.add((Variable) it.next());
            }
        }
        int max = varSet.size();
        for (int i = 0; i < max; i++) {
            List srcRow = varSet.getRow(i);
            List newRow = new ArrayList();
            Iterator it = m_columns.iterator();
            while (it.hasNext()) {
                Object c = it.next();
                if (c instanceof Count) {
                    int ix = varSet.indexOf(((Count) c).getVariable());
                    Object srcVal = srcRow.get(ix);
                    if (((srcRow == null) || (srcRow.get(ix) instanceof Variable))) {
                        newRow.add(new Integer(0));
                    } else {
                        newRow.add(new Integer(1));
                    }
                } else {
                    int ix = varSet.indexOf((Variable) c);
                    Object srcVal = srcRow.get(ix);
                    if ((srcRow == null) || (srcVal instanceof Variable)) {
                        newRow.add(null);
                    } else {
                        newRow.add(srcVal);
                    }
                }
            }
            if (m_log.isDebugEnabled()) debugRow("Adding new row: ", newRow);
            boolean hasMatch = false;
            it = m_rows.iterator();
            while (!hasMatch && it.hasNext()) {
                List row = (List) it.next();
                if (m_log.isDebugEnabled()) debugRow("  Compare to ", row);
                hasMatch = compareRows(newRow, row);
                if (hasMatch) {
                    m_log.debug("New row matches existing row. Updating counts...");
                    for (int col = 0; col < row.size(); col++) {
                        if (m_columns.get(col) instanceof Count) {
                            int count = ((Integer) row.get(col)).intValue();
                            count += ((Integer) newRow.get(col)).intValue();
                            row.set(col, new Integer(count));
                        }
                    }
                }
            }
            if (!hasMatch) {
                m_log.debug("Adding new row to projection.");
                m_rows.add(newRow);
            }
        }
    }

    private boolean compareRows(List r1, List r2) {
        for (int col = 0; col < r1.size(); col++) {
            if (m_columns.get(col) instanceof Count) continue;
            Object v1 = r1.get(col);
            Object v2 = r2.get(col);
            if ((v1 == null && v2 != null) || (v2 == null && v1 != null)) {
                return false;
            } else if (v1 == null && v2 == null) {
                return true;
            } else if (!(r1.get(col).equals(r2.get(col)))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the columns in the projection as a list of either Variable or Count
     * instances. 
     * @return a list of column descriptors
     */
    public List getColumns() {
        return m_columns;
    }

    /**
     * Returns the data rows in the projection.
     * @return a List of List instances, one per row in the projected data.
     */
    public List getRows() {
        return m_rows;
    }

    public class Count {

        private Variable m_var;

        public Count(Variable var) {
            m_var = var;
        }

        public String getVarName() {
            return m_var.getName();
        }

        public Variable getVariable() {
            return m_var;
        }

        public String toString() {
            return "count($" + m_var.getName() + ")";
        }
    }

    private void debugRow(String msg, List row) {
        StringBuffer m = new StringBuffer();
        m.append(msg);
        Iterator it = row.iterator();
        while (it.hasNext()) {
            Object o = it.next();
            if (o instanceof TopicMapObject) {
                m.append(((TopicMapObject) o).getID());
            } else {
                m.append(String.valueOf(o));
            }
            if (it.hasNext()) {
                m.append(", ");
            }
        }
        m_log.debug(m.toString());
    }

    public String toString() {
        if (m_columns.isEmpty()) {
            return "select * from";
        }
        StringBuffer sb = new StringBuffer();
        sb.append("select");
        Iterator it = m_columns.iterator();
        while (it.hasNext()) {
            sb.append(" ");
            sb.append(it.next().toString());
            if (it.hasNext()) sb.append(",");
        }
        sb.append(" from");
        return sb.toString();
    }
}
