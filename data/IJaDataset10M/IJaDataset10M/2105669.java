package org.decisiondeck.xmcda_oo.utils.matrix;

import java.util.Set;
import org.decisiondeck.xmcda_oo.structure.Alternative;
import org.decisiondeck.xmcda_oo.structure.Criterion;
import com.google.common.collect.Sets;

/**
 * An unmodifiable view of the union of two sets of evaluations. The two sets of entries must be disjoint, thus there
 * must be no entry that are in both underlying evaluations.
 * 
 * @author Olivier Cailloux
 * 
 */
public class EvaluationsDoubleView implements IRdEvaluations {

    private final IRdEvaluations m_evaluations1;

    private final IRdEvaluations m_evaluations2;

    /**
     * Builds a new evaluations view delegating to the given evaluations.
     * 
     * @param delegate1
     *            not <code>null</code>.
     * @param delegate2
     *            not <code>null</code>.
     */
    public EvaluationsDoubleView(IRdEvaluations delegate1, IRdEvaluations delegate2) {
        if (delegate1 == null || delegate2 == null) {
            throw new NullPointerException();
        }
        m_evaluations1 = delegate1;
        m_evaluations2 = delegate2;
    }

    @Override
    public boolean approxEquals(IRdFloatMatrix<Alternative, Criterion> m2, double imprecision) {
        if (m2 == null) {
            return false;
        }
        if (m2.getValueCount() != getValueCount()) {
            return false;
        }
        if (isComplete() != m2.isComplete()) {
            return false;
        }
        if (!getRows().equals(m2.getRows())) {
            return false;
        }
        if (!getColumns().equals(m2.getColumns())) {
            return false;
        }
        for (Alternative alternative : getRows()) {
            for (Criterion criterion : getColumns()) {
                final Double value1 = m_evaluations1.getEntry(alternative, criterion);
                final Double value2 = m_evaluations2.getEntry(alternative, criterion);
                if ((value1 == null) != (value2 == null)) {
                    return false;
                }
                if (value1 != null && value2 != null && Math.abs(value2.doubleValue() - value1.doubleValue()) > imprecision) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public Set<Criterion> getColumns() {
        return Sets.union(m_evaluations1.getColumns(), m_evaluations2.getColumns());
    }

    @Override
    public Double getEntry(Alternative row, Criterion column) {
        final Double entry1 = m_evaluations1.getEntry(row, column);
        if (entry1 != null) {
            return entry1;
        }
        return m_evaluations2.getEntry(row, column);
    }

    @Override
    public Set<Alternative> getRows() {
        return Sets.union(m_evaluations1.getRows(), m_evaluations2.getRows());
    }

    @Override
    public int getValueCount() {
        return m_evaluations1.getValueCount() + m_evaluations2.getValueCount();
    }

    @Override
    public boolean isComplete() {
        final boolean equalRows = m_evaluations1.getRows().equals(m_evaluations2.getRows());
        if (!equalRows) {
            return false;
        }
        final boolean equalColumns = m_evaluations1.getColumns().equals(m_evaluations2.getColumns());
        if (!equalColumns) {
            return false;
        }
        final boolean complete = m_evaluations1.isComplete() && m_evaluations2.isComplete();
        if (!complete) {
            return false;
        }
        return true;
    }

    @Override
    public boolean isEmpty() {
        return m_evaluations1.isEmpty() && m_evaluations2.isEmpty();
    }
}
