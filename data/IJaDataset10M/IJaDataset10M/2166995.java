package org.plazmaforge.bsolution.payroll.server.services;

import java.util.Map;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Restrictions;
import org.plazmaforge.bsolution.document.server.services.AbstractDocumentService;
import org.plazmaforge.bsolution.employee.server.services.EmployeeUtils;
import org.plazmaforge.bsolution.payroll.common.beans.EmployeeResult;
import org.plazmaforge.bsolution.payroll.common.services.EmployeeResultService;
import org.plazmaforge.framework.service.OwnCriteriaService;

public class EmployeeResultServiceImpl extends AbstractDocumentService<EmployeeResult, Integer> implements EmployeeResultService, OwnCriteriaService {

    public EmployeeResultServiceImpl() {
        super();
        setPartnerIdProperty("partnerId");
        setOrganizationIdProperty("organizationId");
    }

    protected Class getEntityClass() {
        return EmployeeResult.class;
    }

    protected void addPeriodFilter(Integer periodId, Criteria criteria) {
        Criterion c1 = Restrictions.eq(getPeriodIdProperty(), periodId);
        Criterion c2 = Restrictions.isNull(getPeriodIdProperty());
        criteria.add(Expression.or(c1, c2));
    }

    protected void populateHibernateAliasMap(Map<String, String> map) {
        EmployeeUtils.populateEmployeeDocumentHibernateAliasMap(map);
        map.put("emp.department", "dep");
    }

    protected void populateHibernatePropertyMap(Map<String, String> map) {
        EmployeeUtils.populateEmployeeDocumentHibernatePropertyMap(map);
        map.put("departmentName", "dep.name");
    }
}
