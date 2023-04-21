package com.germinus.xpression.cms.contents.service;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import com.germinus.xpression.cms.HibernatePersistanceManager;
import com.germinus.xpression.cms.hibernate.HQLCondition;
import com.germinus.xpression.cms.model.summaries.DateSummary;

public class DateSummaryService {

    private static final Class<DateSummary> PERSISTENT_CLASS = DateSummary.class;

    private HibernatePersistanceManager persistanceManager;

    public List<DateSummary> listBySpace(String space) {
        List<HQLCondition> conditions = equalSpaceConditions(space);
        return getPersistenceManager().findList(PERSISTENT_CLASS, conditions);
    }

    public DateSummary loadBySpaceYearMonth(String space, Integer year, Integer month) {
        List<HQLCondition> equalSpaceYearMonthConditions = equalSpaceYearMonthConditions(space, year, month);
        List<DateSummary> findList = getPersistenceManager().findList(PERSISTENT_CLASS, equalSpaceYearMonthConditions);
        if (findList.size() == 0) return null; else return findList.get(0);
    }

    public int countBySpace(String space) {
        Criteria equalSpaceCriteria = equalSpaceCriteria(space);
        return getPersistenceManager().countQuery(equalSpaceCriteria);
    }

    public void deleteSummary(String space) {
        log.info("Deleting date summary from " + space);
        HibernatePersistanceManager persistenceManager = getPersistenceManager();
        for (DateSummary dateSummary : listBySpace(space)) {
            persistenceManager.delete(dateSummary);
        }
        log.info("Deleted date summary from " + space);
    }

    public void createDateSummary(List<DateSummary> fromChronologicalEntries) {
        if (fromChronologicalEntries.size() > 0) {
            String space = fromChronologicalEntries.get(0).getSpace();
            log.info("Creating date summary for " + space);
            createSummaries(fromChronologicalEntries);
            log.info("Created date summary for " + space);
        }
    }

    public void storeSummaries(List<DateSummary> worldEntries) {
        log.info("Updating date summaries");
        HibernatePersistanceManager persistenceManager = getPersistenceManager();
        for (DateSummary dateSummary : worldEntries) {
            if (dateSummary.getCount().longValue() <= 0) {
                persistenceManager.delete(dateSummary);
            } else {
                persistenceManager.save(dateSummary);
            }
        }
        log.info("Updated date summaries");
    }

    private void createSummaries(List<DateSummary> fromChronologicalEntries) {
        HibernatePersistanceManager persistenceManager = getPersistenceManager();
        for (DateSummary dateSummary : fromChronologicalEntries) {
            persistenceManager.save(dateSummary);
        }
    }

    private Criteria equalSpaceCriteria(String space) {
        Criteria criteria = getPersistenceManager().createCriteria(PERSISTENT_CLASS);
        return criteria.add(equalSpaceRestriction(space));
    }

    private SimpleExpression equalSpaceRestriction(String space) {
        return equalRestriction("space", space);
    }

    private SimpleExpression equalYearRestriction(Integer year) {
        return equalRestriction("year", year);
    }

    private SimpleExpression equalMonthRestriction(Integer month) {
        return equalRestriction("space", month);
    }

    private SimpleExpression equalRestriction(String propertyName, Object propertyValue) {
        return Restrictions.eq(propertyName, propertyValue);
    }

    private List<HQLCondition> equalSpaceConditions(String space) {
        List<HQLCondition> conditions = new ArrayList<HQLCondition>();
        HQLCondition conditionOne = new HQLCondition("space", space, HQLCondition.EQUALS);
        conditions.add(conditionOne);
        return conditions;
    }

    private List<HQLCondition> equalSpaceYearMonthConditions(String space, Integer year, Integer month) {
        List<HQLCondition> conditions = new ArrayList<HQLCondition>();
        HQLCondition spaceCondition = new HQLCondition("space", space, HQLCondition.EQUALS);
        HQLCondition yearCondition = new HQLCondition("year", year, HQLCondition.EQUALS);
        HQLCondition monthCondition = new HQLCondition("month", month, HQLCondition.EQUALS);
        conditions.add(spaceCondition);
        conditions.add(yearCondition);
        conditions.add(monthCondition);
        return conditions;
    }

    private HibernatePersistanceManager getPersistenceManager() {
        if (persistanceManager == null) new HibernatePersistanceManager();
        return persistanceManager;
    }

    public void setPersistanceManager(HibernatePersistanceManager persistanceManager) {
        this.persistanceManager = persistanceManager;
    }

    private static final Log log = LogFactory.getLog(DateSummaryService.class);
}
