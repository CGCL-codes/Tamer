package org.decisiondeck.xmcda_oo.utils.matrix;

import java.util.Collections;
import java.util.Set;

public class ConstantCompleteMatrix<Row, Column> implements IRdZeroToOneMatrix<Row, Column> {

    private final Double m_constant;

    private final Set<Row> m_rows;

    private final Set<Column> m_columns;

    public ConstantCompleteMatrix(Set<Row> rows, Set<Column> columns, double constant) {
        if (rows == null || columns == null) {
            throw new NullPointerException("" + rows + columns);
        }
        m_rows = rows;
        m_columns = columns;
        m_constant = Double.valueOf(constant);
    }

    @Override
    public Double getEntry(Row row, Column column) {
        return m_rows.contains(row) && m_columns.contains(column) ? m_constant : null;
    }

    @Override
    public boolean approxEquals(IRdFloatMatrix<Row, Column> m2, double imprecision) {
        if (m2 == null) {
            return false;
        }
        if (m2.getValueCount() != getValueCount()) {
            return false;
        }
        if (!m2.isComplete()) {
            return false;
        }
        for (Row row : m2.getRows()) {
            if (!m_rows.contains(row)) {
                return false;
            }
            for (Column column : m2.getColumns()) {
                if (!m_columns.contains(column)) {
                    return false;
                }
                final Double entry2 = m2.getEntry(row, column);
                if (Math.abs(entry2.doubleValue() - m_constant.doubleValue()) > imprecision) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public Set<Column> getColumns() {
        return Collections.unmodifiableSet(m_columns);
    }

    @Override
    public Set<Row> getRows() {
        return Collections.unmodifiableSet(m_rows);
    }

    @Override
    public int getValueCount() {
        return m_rows.size() * m_columns.size();
    }

    @Override
    public boolean isComplete() {
        return true;
    }

    @Override
    public boolean isEmpty() {
        return m_columns.isEmpty() || m_rows.isEmpty();
    }
}
