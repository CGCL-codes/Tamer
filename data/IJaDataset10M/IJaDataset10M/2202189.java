package org.plazmaforge.bsolution.organization.client.swt.forms;

import java.util.List;
import java.util.Map;
import org.eclipse.swt.widgets.Composite;
import org.plazmaforge.bsolution.organization.common.services.ResponsableService;
import org.plazmaforge.framework.core.criteria.ICriteria;
import org.plazmaforge.framework.core.exception.ApplicationException;

public abstract class AbstractResponsableListForm extends AbstractOrganizableListForm {

    public AbstractResponsableListForm(Composite parent, int style) {
        super(parent, style);
    }

    protected ResponsableService getResponsableService() {
        return (ResponsableService) getEntityService();
    }

    protected List loadDataList() throws ApplicationException {
        if (isMyFormEmpty()) {
            return getEmptyDataList();
        }
        return getResponsableService().findByOrganizationId(getOrganizationId(), getResponsibleId());
    }

    protected List loadDataList(ICriteria criteria) throws ApplicationException {
        if (isMyFormEmpty()) {
            return getEmptyDataList();
        }
        return getResponsableService().findByOrganizationId(getOrganizationId(), getResponsibleId(), criteria);
    }

    public boolean isMyFormEmpty() {
        return isMyFormMode() && getContextResponsibleId() == null;
    }

    public Integer getResponsibleId() {
        return isMyFormMode() ? getContextResponsibleId() : null;
    }

    /**
     * Populate parameters special for <code>IMyForm</code>
     */
    protected void populateMyFormCriteria(Map<String, Object> parameters) {
        if (parameters == null) {
            return;
        }
        parameters.put("ORGANIZATION_ID", getOrganizationId());
        parameters.put("RESPONSIBLE_ID", getResponsibleId());
    }
}
