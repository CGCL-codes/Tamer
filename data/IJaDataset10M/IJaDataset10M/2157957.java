package fr.ecp.lgi.cplex.shared_profiles;

import org.decisiondeck.xmcda_oo.structure.Criterion;
import org.decisiondeck.xmcda_oo.structure.DecisionMaker;

public class VariableWeight implements FindProfilesVariable, FindRestrictionsVariable {

    /** never <code>null</code>. */
    private final Criterion m_criterion;

    /**
     * @return not <code>null</code>.
     */
    public Criterion getCriterion() {
        return m_criterion;
    }

    /**
     * @return not <code>null</code>.
     */
    public DecisionMaker getDecisionMaker() {
        return m_dm;
    }

    /** never <code>null</code>. */
    private final DecisionMaker m_dm;

    public VariableWeight(DecisionMaker dm, Criterion criterion) {
        if (criterion == null || dm == null) {
            throw new NullPointerException("" + criterion + dm);
        }
        m_criterion = criterion;
        m_dm = dm;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((m_criterion == null) ? 0 : m_criterion.hashCode());
        result = prime * result + ((m_dm == null) ? 0 : m_dm.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        VariableWeight other = (VariableWeight) obj;
        if (m_criterion == null) {
            if (other.m_criterion != null) {
                return false;
            }
        } else if (!m_criterion.equals(other.m_criterion)) {
            return false;
        }
        if (m_dm == null) {
            if (other.m_dm != null) {
                return false;
            }
        } else if (!m_dm.equals(other.m_dm)) {
            return false;
        }
        return true;
    }
}
