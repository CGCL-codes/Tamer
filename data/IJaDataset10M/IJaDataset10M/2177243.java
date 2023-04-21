package org.nakedobjects.example.expenses.services.hibernate;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.nakedobjects.applib.annotation.Hidden;
import org.nakedobjects.example.expenses.claims.Claim;
import org.nakedobjects.example.expenses.claims.ClaimRepositoryAbstract;
import org.nakedobjects.example.expenses.claims.ClaimStatus;
import org.nakedobjects.example.expenses.claims.ExpenseItem;
import org.nakedobjects.example.expenses.claims.ExpenseType;
import org.nakedobjects.example.expenses.claims.items.AbstractExpenseItem;
import org.nakedobjects.example.expenses.employee.Employee;

public class ClaimRepositoryHibernate extends ClaimRepositoryAbstract {

    private HibernateHelper hibernateHelper;

    /**
     * This field is not persisted, nor displayed to the user.
     */
    protected HibernateHelper getHibernateHelper() {
        return this.hibernateHelper;
    }

    /**
     * Injected by the application container.
     */
    public void setHibernateHelper(final HibernateHelper hibernateHelper) {
        this.hibernateHelper = hibernateHelper;
    }

    static final String TEMPLATE_DESCRIPTION = "Template";

    private Integer maxClaimsToRetrieve;

    private List<Claim> findAllClaims(final Employee employee, final ClaimStatus status, final String description) {
        final Criteria criteria = hibernateHelper.createCriteria(Claim.class);
        if (employee != null) {
            criteria.add(Restrictions.eq("claimant", employee));
        }
        if (status != null) {
            criteria.add(Restrictions.eq("status", status));
        }
        if (description != null) {
            criteria.add(Restrictions.like("description", description, MatchMode.ANYWHERE));
        }
        if (maxClaimsToRetrieve != null) {
            criteria.setMaxResults(maxClaimsToRetrieve.intValue());
        }
        criteria.addOrder(Order.desc("dateCreated"));
        return hibernateHelper.findByCriteria(criteria, Employee.class);
    }

    @SuppressWarnings("unchecked")
    @Hidden
    public List<Claim> findClaims(final Employee employee, final ClaimStatus status, final String description) {
        final List<Claim> foundClaims = findAllClaims(employee, status, description);
        if (foundClaims.size() > MAX_CLAIMS) {
            warnUser("Too many claims found - refine search");
            return null;
        } else if (foundClaims.size() == 0) {
            informUser("No claims found");
        }
        return foundClaims;
    }

    /**
     * For testing to inject mock
     */
    @Hidden
    public void setHibernateSession(final Session session) {
    }

    @Hidden
    public List<Claim> findRecentClaims(final Employee employee) {
        maxClaimsToRetrieve = Integer.valueOf(MAX_ITEMS);
        final List<Claim> foundClaims = findClaims(employee, null, null);
        maxClaimsToRetrieve = null;
        return foundClaims;
    }

    @Hidden
    public List<Claim> findClaimsAwaitingApprovalBy(final Employee approver) {
        final Criteria criteria = hibernateHelper.createCriteria(Claim.class);
        criteria.add(Restrictions.eq("approver", approver)).createCriteria("status").add(Restrictions.eq("titleString", ClaimStatus.SUBMITTED));
        return hibernateHelper.findByCriteria(criteria, Claim.class);
    }

    @Override
    @Hidden
    public List<ExpenseItem> findExpenseItemsOfType(final Employee employee, final ExpenseType type) {
        final Query query = hibernateHelper.createEntityQuery("o.expenseType = ? and o.claim.claimant = ?", AbstractExpenseItem.class);
        query.setEntity(0, type);
        query.setEntity(1, employee);
        return hibernateHelper.findByQuery(query, ExpenseItem.class);
    }
}
