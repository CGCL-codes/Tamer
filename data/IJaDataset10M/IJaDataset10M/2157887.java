package org.decisiondeck.xmcda_oo.aggregators;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import org.decisiondeck.xmcda_oo.structure.DecisionMaker;
import org.decisiondeck.xmcda_oo.structure.category.Assignments;
import org.decisiondeck.xmcda_oo.structure.category.Categories;

/**
 *<p>
 * This is basically the multiple decision maker equivalent to {@link OrderedAssignments}.
 * </p>
 * <p>
 * A situation (or problem), more specifically a sorting problem, where assignment examples are provided by several
 * decision makers. This object holds assignments examples (one set of assignment examples per decision maker) and the
 * associated categories and possibly profiles.
 * </p>
 * <p>
 * This object provides methods to check that it is in a consistent state. Note however that if its underlying objects
 * are changed after a check for consistency, the consistency is not guaranteed any more.
 * </p>
 * <p>
 * This object is consistent iff all the following holds (TODO check & refine).
 * <ul>
 * <li>The categories contained in this object is a superset of the categories where alternatives are assigned, i.e., if
 * no alternatives are assigned to a category not contained in this object.</li>
 * <li>The categories are exactly ordered according to the profiles dominance relation.</li>
 * <li>The criteria used to evaluate alternatives and profiles are the same.</li>
 * <li>The alternatives evaluated are the same as the assigned alternatives.</li>
 * <li>The profiles evaluated are the same as the profiles contained in this object.</li>
 * </ul>
 * </p>
 * 
 * @author Olivier Cailloux
 * 
 */
public class GroupOrderedAssignments {

    /**
     * @param allAssignments
     *            not <code>null</code>.
     * @param categories
     *            not <code>null</code>.
     */
    public GroupOrderedAssignments(Map<DecisionMaker, Assignments> allAssignments, Categories categories) {
        if (allAssignments == null || categories == null) {
            throw new NullPointerException("" + allAssignments + categories);
        }
        m_allAssignments = allAssignments;
        m_categories = categories;
    }

    public GroupOrderedAssignments() {
        m_allAssignments = new HashMap<DecisionMaker, Assignments>();
        m_categories = new Categories();
    }

    /**
     * Never <code>null</code>.
     */
    private Map<DecisionMaker, Assignments> m_allAssignments;

    /**
     * Never <code>null</code>.
     */
    private Categories m_categories;

    /**
     * Modifications to the returned object are reflected in this object.
     * 
     * @return not <code>null</code>.
     */
    public Map<DecisionMaker, Assignments> getAllAssignments() {
        return m_allAssignments;
    }

    /**
     * Note that the returned object is a back-door to change this object state: tempering with the returned object
     * might render this object inconsistent.
     * 
     * @return not <code>null</code>.
     */
    public Categories getCategories() {
        return m_categories;
    }

    /**
     * <p>
     * Returns the set of decision makers that have provided assignment examples.
     * </p>
     * <p>
     * Note that the returned object is a back-door to change this object state: tempering with the returned object
     * might render this object inconsistent.
     * </p>
     * 
     * @return not <code>null</code>.
     */
    public Set<DecisionMaker> getDms() {
        return m_allAssignments.keySet();
    }

    /**
     * <p>
     * Returns the assignment examples provided by the given decision maker, iff assignment examples have been set for
     * the given decision maker.
     * </p>
     * <p>
     * Note that the returned object is a back-door to change this object state: tempering with the returned object
     * might render this object inconsistent.
     * </p>
     * 
     * @param dm
     *            not <code>null</code>.
     * 
     * @return <code>null</code> iff the given decision maker is not contained in this object.
     */
    public OrderedAssignments getOrderedAssignments(DecisionMaker dm) {
        if (dm == null) {
            throw new NullPointerException("" + dm);
        }
        final Assignments assignments = m_allAssignments.get(dm);
        if (assignments == null) {
            return null;
        }
        return new OrderedAssignments(assignments, m_categories);
    }

    /**
     * Note that setting the assignments may render this object state inconsistent.
     * 
     * @param allAssignments
     *            not <code>null</code>.
     */
    public void setAllAssignments(Map<DecisionMaker, Assignments> allAssignments) {
        if (allAssignments == null) {
            throw new NullPointerException();
        }
        m_allAssignments = allAssignments;
    }

    /**
     * Note that changing the state of this object in this manner may render it inconsistent. Also, using the object
     * given in parameter is a back-door to change this object state: tempering with the given object might render this
     * object inconsistent.
     * 
     * @param categories
     *            not <code>null</code>.
     */
    public void setCategories(Categories categories) {
        if (categories == null) {
            throw new NullPointerException();
        }
        m_categories = categories;
    }

    /**
     * <p>
     * Returns the ordered assignments of the decision makers at the time this method is called. The returned object
     * will not necessarily stay in sync with this object state. E.g. adding decision makers to this object is not
     * necessarily reflected in the returned object.
     * </p>
     * <p>
     * Note that the returned object is a back-door to change this object state: tempering with the returned object
     * might render this object inconsistent.
     * </p>
     * 
     * @return not <code>null</code>.
     */
    public Map<DecisionMaker, OrderedAssignments> getAllOrderedAssignments() {
        final HashMap<DecisionMaker, OrderedAssignments> allOrderedAssignments = new HashMap<DecisionMaker, OrderedAssignments>();
        for (DecisionMaker dm : getDms()) {
            allOrderedAssignments.put(dm, new OrderedAssignments(m_allAssignments.get(dm), m_categories));
        }
        return allOrderedAssignments;
    }

    /**
     * <p>
     * Sets the assignments. If the given assignments are not empty, also sets the categories in this object to one of
     * the categories contained in the assignments data.
     * </p>
     * <p>
     * Note that setting the assignments may render this object state inconsistent.
     * </p>
     * 
     * @param allAssignments
     *            not <code>null</code>.
     */
    public void setAllOrderedAssignments(Map<DecisionMaker, OrderedAssignments> allAssignments) {
        if (allAssignments == null) {
            throw new NullPointerException();
        }
        m_allAssignments = new HashMap<DecisionMaker, Assignments>();
        for (DecisionMaker dm : allAssignments.keySet()) {
            final OrderedAssignments orderedAssignments = allAssignments.get(dm);
            m_allAssignments.put(dm, orderedAssignments.getAssignments());
        }
        if (!allAssignments.isEmpty()) {
            final DecisionMaker dm = allAssignments.keySet().iterator().next();
            m_categories = allAssignments.get(dm).getCategories();
        }
    }

    /**
     * Permits to retrieve the assignments sorted by alphabetical order of the decision makers.
     * 
     * @return not <code>null</code>.
     */
    public SortedMap<DecisionMaker, Assignments> getAllAssignmentsByDms() {
        final TreeMap<DecisionMaker, Assignments> ordered = new TreeMap<DecisionMaker, Assignments>();
        ordered.putAll(m_allAssignments);
        return ordered;
    }
}
