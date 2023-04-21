package org.decisiondeck.xmcda_oo.structure.alternatives_scores;

import org.apache.commons.collections15.OrderedIterator;
import org.decisiondeck.xmcda_oo.structure.Alternative;

class ScoredAlternativesIteratorBasis {

    private final AlternativesScores m_delegate;

    private final OrderedIterator<AlternativeScore> m_iterator;

    private AlternativeScore m_last;

    public ScoredAlternativesIteratorBasis(final AlternativesScores delegate) {
        m_iterator = delegate.getInternalIterator();
        m_delegate = delegate;
        m_last = null;
    }

    public Alternative getKey() {
        if (m_last == null) {
            throw new IllegalStateException("Next never called.");
        }
        return m_last.getAlternative();
    }

    public double getScore() {
        if (m_last == null) {
            throw new IllegalStateException("Next never called.");
        }
        return m_last.getScore();
    }

    public Double getValue() {
        if (m_last == null) {
            throw new IllegalStateException("Next never called.");
        }
        return m_last.getValue();
    }

    public boolean hasNext() {
        return m_iterator.hasNext();
    }

    public boolean hasPrevious() {
        return m_iterator.hasPrevious();
    }

    protected AlternativeScore nextScored() {
        m_last = m_iterator.next();
        return m_last;
    }

    protected AlternativeScore previousScored() {
        m_last = m_iterator.previous();
        return m_last;
    }

    public void remove() {
        m_iterator.remove();
        m_delegate.removeFromMapping(m_last);
    }
}
