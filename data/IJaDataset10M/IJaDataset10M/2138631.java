package org.plazmaforge.bsolution.project.server.services;

import org.plazmaforge.bsolution.project.ProjectContext;
import org.plazmaforge.bsolution.project.common.beans.ProjectStatus;
import org.plazmaforge.bsolution.project.common.beans.ProjectType;
import org.plazmaforge.bsolution.project.common.services.ProjectContextService;
import org.plazmaforge.framework.core.exception.DAOException;
import org.plazmaforge.framework.core.exception.FinderException;
import org.plazmaforge.framework.service.hibernate.AbstractHibernateEntityService;

/** 
 * @author Oleh Hapon
 * $Id: ProjectContextServiceImpl.java,v 1.3 2010/12/05 07:55:59 ohapon Exp $
 */
public class ProjectContextServiceImpl extends AbstractHibernateEntityService<ProjectContext, Integer> implements ProjectContextService {

    private Integer ID = new Integer(1);

    protected Class getEntityClass() {
        return ProjectContext.class;
    }

    public ProjectContext load() throws DAOException, FinderException {
        ProjectContext context = findById(ID);
        context.setInitProjectType(findProjectType());
        context.setInitProjectStatus(findProjectStatus());
        return context;
    }

    protected ProjectType findProjectType() throws DAOException, FinderException {
        return (ProjectType) findEntityByCondition(ProjectType.class, " WHERE c.code = ?", ProjectType.STANDARD_TYPE, true);
    }

    protected ProjectStatus findProjectStatus() throws DAOException, FinderException {
        return (ProjectStatus) findEntityByCondition(ProjectStatus.class, " WHERE c.code = ?", ProjectStatus.NOTSTARTED_STATUS, true);
    }
}
