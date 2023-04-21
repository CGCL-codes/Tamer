package de.haumacher.timecollect.common.report;

import java.util.Map;
import de.haumacher.timecollect.common.util.Util;

public abstract class Grouping {

    private final String[] lastValues;

    private boolean isFirst;

    protected final String[] columns;

    protected final int[] valueIdx;

    public Grouping(String[] columns) {
        this.columns = columns;
        this.valueIdx = new int[columns.length];
        this.lastValues = new String[columns.length];
    }

    public void init(Map<String, Integer> idxByColumnName) throws ReportError {
        this.isFirst = true;
        for (int n = 0, cnt = columns.length; n < cnt; n++) {
            String column = columns[n];
            Integer index = idxByColumnName.get(column);
            if (index == null) {
                throw new ReportError("Column '" + column + "' not in query.");
            }
            valueIdx[n] = index;
        }
    }

    public boolean update(boolean parentGroupMatch, String[] values, ReportBuilder builder, Object body) {
        boolean isNext = false;
        for (int n = 0, cnt = columns.length; n < cnt; n++) {
            String oldValue = lastValues[n];
            String newValue = values[valueIdx[n]];
            isNext = isNext || (!Util.equals(newValue, oldValue));
            lastValues[n] = newValue;
        }
        if (isFirst) {
            isFirst = false;
            return firstUpdate(parentGroupMatch, values, builder, body);
        } else {
            if (isNext) {
                return nextUpdate(parentGroupMatch, values, builder, body);
            } else {
                return groupUpdate(parentGroupMatch, values, builder, body);
            }
        }
    }

    protected abstract boolean firstUpdate(boolean parentGroupMatch, String[] values, ReportBuilder builder, Object body);

    protected abstract boolean nextUpdate(boolean parentGroupMatch, String[] values, ReportBuilder builder, Object body);

    protected abstract boolean groupUpdate(boolean parentGroupMatch, String[] values, ReportBuilder builder, Object body);
}
