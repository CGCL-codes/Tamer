package org.plazmaforge.bsolution.organization.client.swt.forms;

import java.util.List;
import org.eclipse.swt.widgets.Composite;
import org.plazmaforge.bsolution.base.SessionContext;
import org.plazmaforge.bsolution.base.EnterpriseEnvironment;
import org.plazmaforge.bsolution.base.SessionEnvironment;
import org.plazmaforge.bsolution.organization.common.services.OrganizableService;
import org.plazmaforge.framework.client.swt.forms.AbstractListForm;
import org.plazmaforge.framework.core.criteria.ICriteria;
import org.plazmaforge.framework.core.criteria.IOwnCriteriaImplementator;
import org.plazmaforge.framework.core.exception.ApplicationException;

/** 
 * @author Oleh Hapon
 * $Id: AbstractOrganizableListForm.java,v 1.5 2010/12/05 07:57:22 ohapon Exp $
 */
public abstract class AbstractOrganizableListForm extends AbstractListForm implements IOrganizableForm, IOwnCriteriaImplementator {

    /** Organization ID **/
    private Integer organizationId;

    public AbstractOrganizableListForm(Composite parent, int style) {
        super(parent, style);
    }

    public Integer getOrganizationId() {
        if (organizationId == null) {
            return getContextOrganizationId();
        }
        return organizationId;
    }

    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

    protected OrganizableService getOrganizableService() {
        return (OrganizableService) getEntityService();
    }

    protected List loadDataList() throws ApplicationException {
        return getOrganizableService().findByOrganizationId(getOrganizationId());
    }

    protected List loadDataList(ICriteria criteria) throws ApplicationException {
        return getOrganizableService().findByOrganizationId(getOrganizationId(), criteria);
    }

    protected SessionContext getEnterpriseContext() {
        return SessionEnvironment.getContext();
    }

    public Integer getContextOrganizationId() {
        return EnterpriseEnvironment.getOrganizationId(getEnterpriseContext());
    }

    public Integer getContextUserId() {
        return EnterpriseEnvironment.getUserId(getEnterpriseContext());
    }

    public Integer getContextResponsibleId() {
        return EnterpriseEnvironment.getResponsibleId(getEnterpriseContext());
    }
}
