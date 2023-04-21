package org.jaffa.applications.mylife.admin.components.changehistorylookup.tx;

import java.util.*;
import java.lang.reflect.Method;
import org.apache.log4j.Logger;
import org.jaffa.exceptions.ApplicationExceptions;
import org.jaffa.exceptions.FrameworkException;
import org.jaffa.persistence.UOW;
import org.jaffa.persistence.exceptions.UOWException;
import org.jaffa.persistence.Criteria;
import org.jaffa.persistence.AtomicCriteria;
import org.jaffa.components.finder.CriteriaField;
import org.jaffa.components.finder.OrderByField;
import org.jaffa.components.finder.FinderTx;
import org.jaffa.applications.mylife.admin.components.changehistorylookup.IChangeHistoryLookup;
import org.jaffa.applications.mylife.admin.components.changehistorylookup.dto.ChangeHistoryLookupInDto;
import org.jaffa.applications.mylife.admin.components.changehistorylookup.dto.ChangeHistoryLookupOutDto;
import org.jaffa.applications.mylife.admin.components.changehistorylookup.dto.ChangeHistoryLookupOutRowDto;
import org.jaffa.applications.mylife.admin.domain.ChangeHistory;
import org.jaffa.applications.mylife.admin.domain.ChangeHistoryMeta;

/** Lookup for ChangeHistory objects.
*/
public class ChangeHistoryLookupTx implements IChangeHistoryLookup {

    private static Logger log = Logger.getLogger(ChangeHistoryLookupTx.class);

    /**
     * This should be invoked, when done with the component.
     */
    public void destroy() {
    }

    /** Searches for ChangeHistory objects.
     * @param input The criteria based on which the search will be performed.
     * @throws ApplicationExceptions This will be thrown if the criteria contains invalid data.
     * @throws FrameworkException Indicates some system error
     * @return The search results.
     */
    public ChangeHistoryLookupOutDto find(ChangeHistoryLookupInDto input) throws FrameworkException, ApplicationExceptions {
        UOW uow = null;
        try {
            if (log.isDebugEnabled()) {
                log.debug("Input: " + (input != null ? input.toString() : null));
            }
            uow = new UOW();
            Criteria criteria = buildCriteria(input, uow);
            Collection results = uow.query(criteria);
            ChangeHistoryLookupOutDto output = buildDto(uow, results, input);
            if (log.isDebugEnabled()) {
                log.debug("Output: " + (output != null ? output.toString() : null));
            }
            return output;
        } finally {
            if (uow != null) uow.rollback();
        }
    }

    private Criteria buildCriteria(ChangeHistoryLookupInDto input, UOW uow) {
        Criteria criteria = new Criteria();
        criteria.setTable(ChangeHistoryMeta.getName());
        FinderTx.addCriteria(input.getContentId(), ChangeHistoryMeta.CONTENT_ID, criteria);
        FinderTx.addCriteria(input.getUpdatedOn(), ChangeHistoryMeta.UPDATED_ON, criteria);
        FinderTx.addCriteria(input.getUpdatedBy(), ChangeHistoryMeta.UPDATED_BY, criteria);
        OrderByField[] orderByFields = input.getOrderByFields();
        if (orderByFields != null) {
            for (int i = 0; i < orderByFields.length; i++) {
                OrderByField orderByField = orderByFields[i];
                int sort = Criteria.ORDER_BY_ASC;
                if (orderByField.getSortAscending() != null && !orderByField.getSortAscending().booleanValue()) sort = Criteria.ORDER_BY_DESC;
                criteria.addOrderBy(orderByField.getFieldName(), sort);
            }
        }
        return criteria;
    }

    private ChangeHistoryLookupOutDto buildDto(UOW uow, Collection results, ChangeHistoryLookupInDto input) throws UOWException {
        ChangeHistoryLookupOutDto output = new ChangeHistoryLookupOutDto();
        int maxRecords = input.getMaxRecords() != null ? input.getMaxRecords().intValue() : 0;
        int counter = 0;
        for (Iterator i = results.iterator(); i.hasNext(); ) {
            if (++counter > maxRecords && maxRecords > 0) {
                output.setMoreRecordsExist(Boolean.TRUE);
                break;
            }
            ChangeHistoryLookupOutRowDto row = new ChangeHistoryLookupOutRowDto();
            ChangeHistory changeHistory = (ChangeHistory) i.next();
            row.setContentId(changeHistory.getContentId());
            row.setUpdatedOn(changeHistory.getUpdatedOn());
            row.setUpdatedBy(changeHistory.getUpdatedBy());
            output.addRows(row);
        }
        return output;
    }
}
