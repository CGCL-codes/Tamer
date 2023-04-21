package org.progeeks.extract.filter;

import java.util.*;
import org.progeeks.util.log.Log;
import org.progeeks.extract.*;

/**
 *  Constructs a set of Map records from a grid, ie:
 *  a list of lists.  The first x number of rows can be
 *  used to specify the 'column' names or they can be
 *  specified directly.  null column names can inherit
 *  from the first rows.
 *
 *  @version   $Revision: 4315 $
 *  @author    Paul Speed
 */
public class TableFilter extends AbstractFilter implements CollectionCapable {

    static Log log = Log.getLog();

    private List<String> columnNames;

    private int headerRows = 1;

    private boolean ignoreOutliers = true;

    public TableFilter() {
    }

    public void setColumnNames(List<String> names) {
        if (this.columnNames == names) return;
        this.columnNames = new ArrayList<String>(names);
    }

    public List<String> getColumnNames() {
        if (columnNames == null) return Collections.emptyList();
        return columnNames;
    }

    public void setHeaderRows(int c) {
        this.headerRows = c;
    }

    public int getHeaderRows() {
        return headerRows;
    }

    public void setIgnoreOutOfBounds(boolean b) {
        this.ignoreOutliers = b;
    }

    public boolean getIgnoreOutOfBounds() {
        return ignoreOutliers;
    }

    protected String value(Object o) {
        if (o instanceof DataElement) o = ((DataElement) o).getValue();
        return String.valueOf(o);
    }

    protected List composeTable(List list) {
        if (log.isTraceEnabled()) log.trace("composeTable(" + list + ")");
        if (list == null || list.size() == 0) return null;
        List results = new ArrayList();
        List localRows = new ArrayList();
        for (Object o : list) {
            if (log.isTraceEnabled()) log.trace("  checking child:" + o);
            if (o instanceof DataElement) o = ((DataElement) o).getValue();
            if (!(o instanceof List)) return null;
            List sub = composeTable((List) o);
            if (sub == null) {
                if (log.isTraceEnabled()) log.trace("   adding child row:" + o);
                localRows.add(o);
            } else {
                if (log.isTraceEnabled()) log.trace("   adding child table:" + sub);
                results.add(sub);
            }
        }
        if (log.isTraceEnabled()) {
            log.trace("Results:" + results);
            log.trace("Local rows:" + localRows);
        }
        if (!localRows.isEmpty() && !results.isEmpty()) throw new RuntimeException("Level inconsistency.  Found table rows and nested table at same level.");
        if (!results.isEmpty()) return results;
        List<String> names = new ArrayList();
        Iterator it = localRows.iterator();
        for (int i = 0; i < headerRows; i++) {
            List l = (List) it.next();
            while (names.size() < l.size()) names.add(null);
            for (int j = 0; j < l.size(); j++) {
                if (l.get(j) == null) continue;
                if (names.get(j) == null) names.set(j, value(l.get(j))); else names.set(j, names.get(j) + " " + value(l.get(j)));
            }
        }
        int index = 0;
        while (names.size() < getColumnNames().size()) names.add(null);
        for (String s : getColumnNames()) {
            if (s != null && s.length() > 0) names.set(index, s);
            index++;
        }
        for (int i = 0; i < names.size(); i++) {
            if (names.get(i) == null) names.set(i, "column" + i);
        }
        while (it.hasNext()) {
            List l = (List) it.next();
            Map record = new LinkedHashMap();
            for (int i = 0; i < l.size(); i++) {
                if (i >= names.size()) {
                    if (ignoreOutliers) continue;
                    throw new RuntimeException("Encountered out of bounds cell:" + l.get(i));
                }
                record.put(names.get(i), l.get(i));
            }
            results.add(record);
        }
        return results;
    }

    public Object filter(ExecutionContext context, DataElement container, Object o) {
        System.out.println("TableFilter.filter(" + o + ")");
        if (o instanceof DataElement) o = ((DataElement) o).getValue();
        if (!(o instanceof List)) return null;
        return composeTable((List) o);
    }
}
