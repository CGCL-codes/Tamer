package org.plazmaforge.bsolution.project;

import org.plazmaforge.bsolution.base.EnterpriseEnvironment;
import org.plazmaforge.bsolution.base.common.beans.Currency;
import org.plazmaforge.bsolution.contact.common.beans.ContactHeader;
import org.plazmaforge.bsolution.partner.common.beans.PartnerHeader;
import org.plazmaforge.bsolution.project.common.beans.ProjectStage;
import org.plazmaforge.bsolution.project.common.beans.ProjectStatus;
import org.plazmaforge.bsolution.project.common.beans.ProjectType;
import org.plazmaforge.bsolution.project.common.services.ProjectContextService;
import org.plazmaforge.framework.core.exception.ApplicationException;
import org.plazmaforge.framework.platform.Environment;
import org.plazmaforge.framework.platform.service.ServiceFactory;

/**
 * @author Oleh Hapon
 *
 * $Id: ProjectEnvironment.java,v 1.3 2010/12/05 07:56:00 ohapon Exp $
 * 
 */
public class ProjectEnvironment extends Environment {

    private static ProjectEnvironment instance;

    private ProjectContext context;

    private ProjectEnvironment() {
    }

    public static ProjectEnvironment getInstance() {
        if (instance == null) {
            instance = new ProjectEnvironment();
            instance.loadContext();
        }
        return instance;
    }

    public static ProjectContext getContext() {
        return getInstance().context;
    }

    /**
     * Load Context
     * 
     */
    private void loadContext() {
        try {
            context = ((ProjectContextService) ServiceFactory.getService(ProjectContextService.class)).load();
            if (context == null) {
                throw new Exception("ProjectContext is null");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (context == null) {
                context = new ProjectContext();
            }
        }
    }

    public static void refresh() throws ApplicationException {
        getInstance().loadContext();
    }

    public static Currency getCurrency() {
        return getContext().getCurrency() == null ? EnterpriseEnvironment.getCurrency() : getContext().getCurrency();
    }

    public static PartnerHeader getPartner() {
        return getContext().getPartner() == null ? EnterpriseEnvironment.getPartner() : getContext().getPartner();
    }

    public static ContactHeader getResponsible() {
        return getContext().getResponsible() == null ? EnterpriseEnvironment.getResponsible() : getContext().getResponsible();
    }

    public static ProjectStage getProjectStage() {
        return getContext().getProjectStage();
    }

    public static ProjectStatus getProjectStatus() {
        return getContext().getProjectStatus();
    }

    public static ProjectStatus getDefaultProjectStatus() {
        return getContext().getDefaultProjectStatus();
    }

    public static ProjectType getProjectType() {
        return getContext().getProjectType();
    }

    public static ProjectType getDefaultProjectType() {
        return getContext().getDefaultProjectType();
    }
}
