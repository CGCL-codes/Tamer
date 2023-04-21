package org.decisiondeck.jmcda.structure.sorting.problem.group_results;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import org.decisiondeck.jmcda.structure.sorting.assignment.credibilities.IOrderedAssignmentsWithCredibilities;
import org.decisiondeck.jmcda.structure.sorting.assignment.credibilities.OrderedAssignmentsWithCredibilitiesForwarder;
import org.decisiondeck.jmcda.structure.sorting.assignment.utils.AssignmentsFactory;
import org.decisiondeck.jmcda.structure.sorting.assignment.utils.AssignmentsUtils;
import org.decisiondeck.jmcda.structure.sorting.category.Category;
import org.decisiondeck.jmcda.structure.sorting.category.CatsAndProfsForwarder;
import org.decisiondeck.jmcda.structure.sorting.category.ICatsAndProfs;
import org.decisiondeck.jmcda.structure.sorting.problem.group_assignments.IGroupSortingAssignmentsWithCredibilities;
import org.decisiondeck.jmcda.structure.sorting.problem.group_preferences.GroupSortingPreferencesForwarder;
import org.decisiondeck.jmcda.structure.sorting.problem.group_preferences.GroupSortingPreferencesImpl;
import org.decisiondeck.jmcda.structure.sorting.problem.group_preferences.IGroupSortingPreferences;
import org.decisiondeck.jmcda.structure.sorting.problem.results.ISortingResultsWithCredibilities;
import org.decisiondeck.jmcda.structure.sorting.problem.results.SortingResultsWithCredibilitiesViewGroupBacked;
import org.decisiondeck.utils.collections.AbstractSetView;
import org.decisiondeck.utils.collections.CollectionVariousUtils;
import org.decisiondeck.xmcda_oo.structure.Alternative;
import org.decisiondeck.xmcda_oo.structure.DecisionMaker;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Maps.EntryTransformer;

public class GroupSortingResultsWithCredibilitiesImpl extends GroupSortingPreferencesForwarder implements IGroupSortingResultsWithCredibilities {

    /**
     * Bound to a sorting object and a delegate (writeable) assignments object. When written, before writing to the
     * delegate, ensures that the sorting object contains the categories. Thus the categories used with these
     * assignments must be a subset of the categories in the sorting object. No categories are added or removed from the
     * sorting object. Alternatives are added to the sorting object when they are added to the assignments object.
     * 
     * @author Olivier Cailloux
     * 
     */
    private static class OrderedAssignmentsWithCredibilitiesWriteable extends OrderedAssignmentsWithCredibilitiesForwarder implements IOrderedAssignmentsWithCredibilities {

        private final IGroupSortingAssignmentsWithCredibilities m_sorting;

        public OrderedAssignmentsWithCredibilitiesWriteable(IGroupSortingAssignmentsWithCredibilities assignments, IOrderedAssignmentsWithCredibilities delegate) {
            super(delegate);
            Preconditions.checkNotNull(assignments);
            Preconditions.checkNotNull(delegate);
            m_sorting = assignments;
        }

        @Override
        public boolean setCredibilities(Alternative alternative, Map<Category, Double> credibilities) {
            Preconditions.checkNotNull(alternative);
            if (credibilities == null) {
                return delegate().setCredibilities(alternative, credibilities);
            }
            Preconditions.checkArgument(getCategories().containsAll(credibilities.keySet()));
            Preconditions.checkArgument(m_sorting.getCatsAndProfs().getCategories().containsAll(credibilities.keySet()));
            m_sorting.getAlternatives().add(alternative);
            return delegate().setCredibilities(alternative, credibilities);
        }

        @Override
        public boolean setCategories(SortedSet<Category> categories) {
            if (categories == null) {
                return delegate().setCategories(null);
            }
            final Set<Category> theseCategories = m_sorting.getCatsAndProfs().getCategories();
            Preconditions.checkArgument(CollectionVariousUtils.containsInOrder(theseCategories, categories));
            return delegate().setCategories(categories);
        }
    }

    /**
     * <p>
     * To avoid wasting space, the assignments are created lazily: the key set is a subset of all decision makers, it
     * contains only those assignments which have been requested by the user. However when the user requests a map view
     * the map is populated.
     * </p>
     * <p>
     * No <code>null</code> values or keys (but assignments objects may be empty).
     * </p>
     * <p>
     * Once an assignment object has been created for a decision maker, it must not be replaced, but its internal state
     * must be modified. That is because external users may hold view objects to the assignments. When a decision maker
     * is removed, its assignments should be emptied before being deleted to ensure that the views do not reflect old
     * state.
     * </p>
     * <p>
     * The set of categories inside the assignment objects are maintained in sync with this object categories. When the
     * categories in this object change, they change in the underlying assignments. (This is not required by the
     * contract but is simpler.)
     * </p>
     */
    private final Map<DecisionMaker, IOrderedAssignmentsWithCredibilities> m_allAssignments = Maps.newHashMap();

    public GroupSortingResultsWithCredibilitiesImpl() {
        this(new GroupSortingPreferencesImpl());
    }

    /**
     * Decorates a {@link IGroupSortingPreferences} and adds assignments functionality to it. External references to the
     * delegate should <em>not</em> be kept.
     * 
     * @param delegate
     *            not <code>null</code>.
     */
    public GroupSortingResultsWithCredibilitiesImpl(IGroupSortingPreferences delegate) {
        super(delegate);
    }

    @Override
    public Map<DecisionMaker, IOrderedAssignmentsWithCredibilities> getAssignments() {
        final Map<DecisionMaker, IOrderedAssignmentsWithCredibilities> transformed = Maps.transformEntries(m_allAssignments, new EntryTransformer<DecisionMaker, IOrderedAssignmentsWithCredibilities, IOrderedAssignmentsWithCredibilities>() {

            @Override
            public IOrderedAssignmentsWithCredibilities transformEntry(DecisionMaker key, IOrderedAssignmentsWithCredibilities value) {
                return getAssignments(key);
            }
        });
        return Collections.unmodifiableMap(transformed);
    }

    @Override
    public IOrderedAssignmentsWithCredibilities getAssignments(DecisionMaker dm) {
        if (dm == null) {
            throw new NullPointerException();
        }
        if (!getDms().contains(dm)) {
            return null;
        }
        final IOrderedAssignmentsWithCredibilities assignments = lazyInitAssignments(dm);
        return new OrderedAssignmentsWithCredibilitiesWriteable(this, assignments);
    }

    /**
     * @param dm
     *            must be in this object.
     * @return not <code>null</code>.
     */
    private IOrderedAssignmentsWithCredibilities lazyInitAssignments(DecisionMaker dm) {
        if (!getDms().contains(dm)) {
            throw new IllegalStateException("" + dm);
        }
        final IOrderedAssignmentsWithCredibilities assignments = m_allAssignments.get(dm);
        if (assignments != null) {
            return assignments;
        }
        final IOrderedAssignmentsWithCredibilities created = AssignmentsFactory.newOrderedAssignmentsWithCredibilities();
        created.setCategories(getCatsAndProfs().getCategories());
        m_allAssignments.put(dm, created);
        return created;
    }

    @Override
    public boolean hasCompleteAssignments() {
        for (DecisionMaker dm : getDms()) {
            final IOrderedAssignmentsWithCredibilities assignments = m_allAssignments.get(dm);
            if (assignments.getAlternatives().size() < getAlternatives().size()) {
                return false;
            }
        }
        return true;
    }

    private void beforeRemoveDm(DecisionMaker dm) {
        final IOrderedAssignmentsWithCredibilities assignments = m_allAssignments.get(dm);
        if (assignments != null) {
            for (Alternative alternative : assignments.getAlternatives()) {
                assignments.setCredibilities(alternative, null);
            }
        }
    }

    private void beforeRemoveAlternative(Alternative alternative) {
        for (DecisionMaker dm : getDms()) {
            if (m_allAssignments.get(dm) != null) {
                final IOrderedAssignmentsWithCredibilities assignments = m_allAssignments.get(dm);
                assignments.setCredibilities(alternative, null);
            }
        }
    }

    @Override
    public ISortingResultsWithCredibilities getResults(DecisionMaker dm) {
        Preconditions.checkNotNull(dm);
        if (!getDms().contains(dm)) {
            return null;
        }
        return new SortingResultsWithCredibilitiesViewGroupBacked(this, dm);
    }

    @Override
    public Set<Alternative> getAlternatives() {
        return new AbstractSetView<Alternative>(delegate().getAlternatives()) {

            @Override
            public void beforeRemove(Object object) {
                if (object instanceof Alternative) {
                    Alternative alternative = (Alternative) object;
                    beforeRemoveAlternative(alternative);
                }
            }
        };
    }

    @Override
    public Set<DecisionMaker> getDms() {
        return new AbstractSetView<DecisionMaker>(delegate().getDms()) {

            @Override
            public void beforeRemove(Object object) {
                if (object instanceof DecisionMaker) {
                    DecisionMaker dm = (DecisionMaker) object;
                    beforeRemoveDm(dm);
                }
            }
        };
    }

    @Override
    public ICatsAndProfs getCatsAndProfs() {
        return new CatsAndProfsForwarder(delegate().getCatsAndProfs()) {

            @Override
            public void setCategory(String oldName, Category newCategory) {
                delegate().setCategory(oldName, newCategory);
                final Category oldCategory = new Category(oldName);
                renameInAllAssignments(oldCategory, newCategory);
            }

            /**
	     * @param oldCategory
	     *            may exist or not (if not, nothing is done).
	     * @param newCategory
	     *            must not exist.
	     */
            private void renameInAllAssignments(final Category oldCategory, Category newCategory) {
                for (DecisionMaker dm : m_allAssignments.keySet()) {
                    final IOrderedAssignmentsWithCredibilities assignments = m_allAssignments.get(dm);
                    if (assignments.getCategories().contains(oldCategory)) {
                        AssignmentsUtils.renameCategory(assignments, oldCategory, newCategory);
                    }
                }
            }

            @Override
            public void setCategoryDown(Alternative profile, Category category) {
                final Category oldCategory = delegate().getCategoryDown(profile);
                delegate().setCategoryDown(profile, category);
                renameInAllAssignments(oldCategory, category);
            }

            @Override
            public void setCategoryUp(Alternative profile, Category category) {
                final Category oldCategory = delegate().getCategoryDown(profile);
                delegate().setCategoryUp(profile, category);
                renameInAllAssignments(oldCategory, category);
            }

            @Override
            public boolean removeCategory(String name) {
                final boolean changed = delegate().removeCategory(name);
                if (!changed) {
                    return false;
                }
                final Category category = new Category(name);
                for (DecisionMaker dm : m_allAssignments.keySet()) {
                    final IOrderedAssignmentsWithCredibilities assignments = m_allAssignments.get(dm);
                    AssignmentsUtils.removeCategory(assignments, category);
                }
                return true;
            }

            @Override
            public boolean clear() {
                final boolean changed = delegate().clear();
                if (!changed) {
                    return false;
                }
                for (DecisionMaker dm : m_allAssignments.keySet()) {
                    final IOrderedAssignmentsWithCredibilities assignments = m_allAssignments.get(dm);
                    assignments.clear();
                }
                return true;
            }
        };
    }
}
