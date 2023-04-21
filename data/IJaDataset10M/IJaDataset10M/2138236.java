package de.objectcode.time4u.server.ejb.seam.api.report;

import java.io.Serializable;

public class CrossTableResult implements Serializable {

    private static final long serialVersionUID = 5081987309859160511L;

    private final ValueLabelPair[] m_columnHeaders;

    private final CrossTableRow[] m_rows;

    private final Object[] m_columnAggregates;

    private final Object m_totalAggregate;

    public CrossTableResult(final ValueLabelPair[] columnHeaders, final CrossTableRow[] rows, final Object[] columnAggregates, final Object totalAggregate) {
        m_columnHeaders = columnHeaders;
        m_rows = rows;
        m_columnAggregates = columnAggregates;
        m_totalAggregate = totalAggregate;
    }

    public ValueLabelPair[] getColumnHeaders() {
        return m_columnHeaders;
    }

    public Object[] getColumnAggregates() {
        return m_columnAggregates;
    }

    public CrossTableRow[] getRows() {
        return m_rows;
    }

    public Object getTotalAggregate() {
        return m_totalAggregate;
    }

    public static class CrossTableRow implements Serializable {

        private static final long serialVersionUID = -345071174481756958L;

        private final ValueLabelPair m_rowHeader;

        private final Object[] m_data;

        private final Object m_rowAggregate;

        private final int m_index;

        public CrossTableRow(final ValueLabelPair rowHeader, final Object[] data, final Object rowAggregate, final int index) {
            m_rowHeader = rowHeader;
            m_data = data;
            m_rowAggregate = rowAggregate;
            m_index = index;
        }

        public ValueLabelPair getRowHeader() {
            return m_rowHeader;
        }

        public Object[] getData() {
            return m_data;
        }

        public Object getRowAggregate() {
            return m_rowAggregate;
        }

        public int getIndex() {
            return m_index;
        }
    }
}
