package com.platform.esg.project;

import java.beans.PropertyEditorSupport;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author     Sean
 * @created    July 13, 2007
 */
public class ProjectStatusSupport extends PropertyEditorSupport {

    /**
	 *  Description of the Field
	 */
    protected final Log logger = LogFactory.getLog(getClass());

    private ProjectRepository projectRepository;

    /**
	 *  Sets the asText attribute of the ProjectSupport object
	 *
	 * @param  string                        The new asText value
	 * @exception  IllegalArgumentException  Description of the Exception
	 */
    public void setAsText(String string) throws IllegalArgumentException {
        logger.info("setAsText");
        try {
            Long id = Long.valueOf(string);
            logger.info("Identifier: " + id);
            ProjectStatus ps = projectRepository.getProjectStatus(id);
            if (ps == null) {
                throw new IllegalArgumentException("Id de Autor Inv�lido");
            }
            setValue(ps);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Invalid id for Autor: " + string);
        }
    }

    /**
	 *  Sets the projectRepository attribute of the ProjectStatusSupport object
	 *
	 * @param  projectRepository  The new projectRepository value
	 */
    public void setProjectRepository(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    /**
	 *  Gets the projectRepository attribute of the ProjectStatusSupport object
	 *
	 * @return    The projectRepository value
	 */
    public ProjectRepository getProjectRepository() {
        return this.projectRepository;
    }
}
