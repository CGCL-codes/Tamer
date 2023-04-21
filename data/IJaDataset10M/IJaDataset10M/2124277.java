package com.ibm.bx.dao.hibernate;

import com.ibm.bx.dao.BudgetDao;
import com.ibm.bx.model.Budget;
import java.util.List;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.orm.ObjectRetrievalFailureException;

/**
 * 
 * @author ibm
 *
 */
public class BudgetDaoHibernate extends HibernateDaoSupport implements BudgetDao {

    /**
	 * 
	 */
    private static final long serialVersionUID = 2185837075733004612L;

    public Budget getBudget(Long id) {
        Budget budget = (Budget) getHibernateTemplate().get(Budget.class, id);
        if (null == budget) {
            throw new ObjectRetrievalFailureException(Budget.class, id);
        }
        return budget;
    }

    public List getBudgets() {
        return getHibernateTemplate().find("from Budget budget order by budget.y_budget,budget.m_budget desc");
    }

    public void saveBudget(Budget budget) {
        getHibernateTemplate().saveOrUpdate(budget);
    }

    public void removeBudget(Long id) {
        getHibernateTemplate().delete(this.getBudget(id));
    }

    public List getUnitBudgets(String unitName) {
        return getHibernateTemplate().find("from Budget budget where unit_name = ? order by budget.y_budget,budget.m_budget desc", unitName);
    }

    public List getUnitBudgetsByDate(String unitName, String beginYM, String endYM) {
        return null;
    }

    public List getUnitBudgetsByY(String unitName, String year, String unitFlag) {
        return getHibernateTemplate().find("from Budget budget where y_budget = '" + year + "' and unit_flag = '" + unitFlag + "' and unit_name = '" + unitName + "' order by budget.unit_name desc");
    }

    public List getUnit2sYearBudget(String currentYear) {
        return getHibernateTemplate().find("from Budget budget where y_budget = ? and unit_flag = '2' order by budget.unit_name desc", currentYear);
    }
}
