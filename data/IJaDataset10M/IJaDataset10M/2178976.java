package org.decisiondeck.jmcda.structure.sorting.problem.preferences;

import java.util.Map;
import java.util.Set;
import org.decisiondeck.jmcda.exc.ConsistencyChecker;
import org.decisiondeck.jmcda.structure.coalitions.CoalitionsUtils;
import org.decisiondeck.jmcda.structure.sorting.category.ICatsAndProfs;
import org.decisiondeck.jmcda.structure.sorting.problem.group_preferences.IGroupSortingPreferences;
import org.decisiondeck.xmcda_oo.aggregators.ICriteriaWithThresholds;
import org.decisiondeck.xmcda_oo.aggregators.Thresholds;
import org.decisiondeck.xmcda_oo.aggregators.ThresholdsView;
import org.decisiondeck.xmcda_oo.structure.Alternative;
import org.decisiondeck.xmcda_oo.structure.Coalitions;
import org.decisiondeck.xmcda_oo.structure.Criterion;
import org.decisiondeck.xmcda_oo.structure.DecisionMaker;
import org.decisiondeck.xmcda_oo.structure.EvaluationMatrix;
import org.decisiondeck.xmcda_oo.structure.ICoalitions;
import org.decisiondeck.xmcda_oo.structure.IOrderedInterval;
import org.decisiondeck.xmcda_oo.utils.EvaluationsUtils;
import org.decisiondeck.xmcda_oo.utils.matrix.EvaluationsView;
import org.decisiondeck.xmcda_oo.utils.matrix.IRdEvaluations;
import com.google.common.base.Preconditions;

/**
 * A writeable view that reads and writes through an {@link IGroupSortingPreferences} object.
 * 
 * @author Olivier Cailloux
 * 
 */
public class SortingPreferencesViewGroupBacked implements ISortingPreferences {

    private final IGroupSortingPreferences m_delegate;

    /**
     * <code>null</code> to access the shared informations.
     */
    private final DecisionMaker m_dm;

    /**
     * A new view that delegates to the shared preferential informations instead of delegating to a specific decision
     * maker's preferences.
     * 
     * @param delegate
     *            not <code>null</code>.
     */
    public SortingPreferencesViewGroupBacked(IGroupSortingPreferences delegate) {
        if (delegate == null) {
            throw new NullPointerException("" + delegate);
        }
        m_delegate = delegate;
        m_dm = null;
    }

    /**
     * 
     * Creates a new preferences view that reads the given decision maker information into the given delegate. To read
     * the shared information instead, use the other constructor.
     * 
     * @param delegate
     *            not <code>null</code>.
     * @param dm
     *            not <code>null</code>.
     */
    public SortingPreferencesViewGroupBacked(IGroupSortingPreferences delegate, DecisionMaker dm) {
        Preconditions.checkNotNull(delegate);
        Preconditions.checkNotNull(dm);
        m_delegate = delegate;
        m_dm = dm;
    }

    @Override
    public Set<Alternative> getAllAlternatives() {
        return m_delegate.getAllAlternatives();
    }

    @Override
    public IRdEvaluations getAlternativesEvaluations() {
        return m_delegate.getAlternativesEvaluations();
    }

    @Override
    public ICatsAndProfs getCatsAndProfs() {
        return m_delegate.getCatsAndProfs();
    }

    @Override
    public Set<Alternative> getProfiles() {
        return m_delegate.getProfiles();
    }

    @Override
    public Set<Criterion> getCriteria() {
        return m_delegate.getCriteria();
    }

    @Override
    public Set<Alternative> getAlternatives() {
        return m_delegate.getAlternatives();
    }

    @Override
    public Map<Criterion, IOrderedInterval> getScales() {
        return m_delegate.getScales();
    }

    @Override
    public boolean isConsistentData() {
        return m_delegate.isConsistentData();
    }

    @Override
    public boolean setEvaluation(Alternative alternative, Criterion criterion, Double value) {
        return m_delegate.setEvaluation(alternative, criterion, value);
    }

    @Override
    public boolean setScale(Criterion criterion, IOrderedInterval scale) {
        return m_delegate.setScale(criterion, scale);
    }

    @Override
    public IRdEvaluations getAllEvaluations() {
        if (m_dm == null) {
            final IRdEvaluations sharedProfilesEvaluations = m_delegate.getSharedProfilesEvaluations();
            return EvaluationsUtils.union(m_delegate.getAlternativesEvaluations(), sharedProfilesEvaluations);
        }
        IRdEvaluations allEvaluations = m_delegate.getAllEvaluations(m_dm);
        return allEvaluations == null ? new EvaluationsView(new EvaluationMatrix()) : allEvaluations;
    }

    @Override
    public ICoalitions getCoalitions() {
        if (m_dm == null) {
            return m_delegate.getSharedCoalitions();
        }
        ICoalitions coalitions = m_delegate.getCoalitions(m_dm);
        return coalitions == null ? CoalitionsUtils.getCoalitionsView(new Coalitions()) : coalitions;
    }

    @Override
    public IRdEvaluations getProfilesEvaluations() {
        if (m_dm == null) {
            return m_delegate.getSharedProfilesEvaluations();
        }
        IRdEvaluations profilesEvaluations = m_delegate.getProfilesEvaluations(m_dm);
        return profilesEvaluations == null ? new EvaluationsView(new EvaluationMatrix()) : profilesEvaluations;
    }

    @Override
    public ICriteriaWithThresholds getThresholds() {
        if (m_dm == null) {
            return m_delegate.getSharedThresholds();
        }
        ICriteriaWithThresholds thresholds = m_delegate.getThresholds(m_dm);
        return thresholds == null ? new ThresholdsView(new Thresholds()) : thresholds;
    }

    @Override
    public Double getWeight(Criterion criterion) {
        if (m_dm == null) {
            return m_delegate.getSharedCoalitions().getWeight(criterion);
        }
        Double weight = m_delegate.getWeight(m_dm, criterion);
        return weight;
    }

    @Override
    public boolean isConsistentPreferences() {
        ConsistencyChecker checker = new ConsistencyChecker();
        return checker.isConsistentPreferences(this);
    }

    @Override
    public boolean setCoalitions(ICoalitions coalitions) {
        if (m_dm == null) {
            return m_delegate.setSharedCoalitions(coalitions);
        }
        return m_delegate.setCoalitions(m_dm, coalitions);
    }

    @Override
    public boolean setProfilesEvaluations(IRdEvaluations evaluations) {
        if (m_dm == null) {
            return m_delegate.setSharedProfilesEvaluations(evaluations);
        }
        return m_delegate.setProfilesEvaluations(m_dm, evaluations);
    }

    @Override
    public boolean setThresholds(ICriteriaWithThresholds thresholds) {
        if (m_dm == null) {
            return m_delegate.setSharedThresholds(thresholds);
        }
        return m_delegate.setThresholds(m_dm, thresholds);
    }

    @Override
    public boolean setEvaluations(IRdEvaluations evaluations) {
        return m_delegate.setEvaluations(evaluations);
    }

    /**
     * Retrieves the decision maker whose related informations this objects reads in the delegated group sorting object.
     * 
     * @return <code>null</code> iff this objects read the shared informations.
     */
    public DecisionMaker getViewedDm() {
        return m_dm;
    }

    protected IGroupSortingPreferences delegate() {
        return m_delegate;
    }
}
