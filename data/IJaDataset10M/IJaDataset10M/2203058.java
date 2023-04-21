package org.decisiondeck.xmcda_oo.structure.category;

import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedSet;
import org.decisiondeck.jmcda.structure.sorting.assignment.VersatileOrderedAssignments;
import org.decisiondeck.jmcda.structure.sorting.assignment.credibilities.IOrderedAssignmentsWithCredibilities;
import org.decisiondeck.jmcda.structure.sorting.assignment.credibilities.IOrderedAssignmentsWithCredibilitiesRead;
import org.decisiondeck.jmcda.structure.sorting.assignment.utils.AssignmentsUtils;
import org.decisiondeck.jmcda.structure.sorting.category.Category;
import org.decisiondeck.xmcda_oo.structure.Alternative;

public class CowOrderedAssignmentsWithCredibilities implements IOrderedAssignmentsWithCredibilities {

    /**
     * Never <code>null</code>.
     */
    private VersatileOrderedAssignments m_delegate;

    @Override
    public NavigableSet<Category> getCategories() {
        return m_delegate.getCategoriesSorted();
    }

    @Override
    public NavigableMap<Category, Double> getCredibilities(Alternative alternative) {
        return m_delegate.getCredibilitiesSorted(alternative);
    }

    @Override
    public boolean setCategories(SortedSet<Category> categories) {
        if (m_protected) {
            detach();
        }
        return m_delegate.setCategories(categories);
    }

    private boolean m_protected;

    public boolean isProtected() {
        return m_protected;
    }

    public void setProtected() {
        m_protected = true;
    }

    public CowOrderedAssignmentsWithCredibilities getProtectedCopy() {
        final CowOrderedAssignmentsWithCredibilities copy = new CowOrderedAssignmentsWithCredibilities(this.m_delegate);
        m_protected = true;
        return copy;
    }

    public boolean isCrisp() {
        return m_delegate.isCrisp();
    }

    private void detach() {
        m_delegate = new VersatileOrderedAssignments(m_delegate);
        m_protected = false;
    }

    @Override
    public boolean setCredibilities(Alternative alternative, Map<Category, Double> credibilities) {
        if (m_protected) {
            detach();
        }
        return m_delegate.setCredibilities(alternative, credibilities);
    }

    public CowOrderedAssignmentsWithCredibilities() {
        m_delegate = new VersatileOrderedAssignments();
        m_protected = false;
    }

    private CowOrderedAssignmentsWithCredibilities(VersatileOrderedAssignments delegate) {
        m_delegate = delegate;
        m_protected = false;
    }

    @Override
    public Set<Alternative> getAlternatives() {
        return m_delegate.getAlternatives();
    }

    @Override
    public Set<Alternative> getAlternatives(Category category) {
        return m_delegate.getAlternatives(category);
    }

    @Override
    public boolean clear() {
        if (m_protected) {
            detach();
        }
        return m_delegate.clear();
    }

    @Override
    public NavigableSet<Category> getCategories(Alternative alternative) {
        return m_delegate.getCategoriesSorted(alternative);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof IOrderedAssignmentsWithCredibilitiesRead)) {
            return false;
        }
        IOrderedAssignmentsWithCredibilitiesRead a2 = (IOrderedAssignmentsWithCredibilitiesRead) obj;
        return AssignmentsUtils.equivalentOrderedWithCredibilities(this, a2);
    }

    @Override
    public int hashCode() {
        return AssignmentsUtils.getEquivalenceRelationOrderedWithCredibilities().hash(this);
    }
}
