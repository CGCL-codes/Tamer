package com.acv.dao.templates;

import java.sql.Timestamp;
import java.util.List;
import com.acv.dao.common.Dao;
import com.acv.dao.templates.model.VelocityTemplate;

/**
 * The Interface VelocityTemplateDao.
 */
public interface VelocityTemplateDao extends Dao {

    /**
	 * Gets the velocity templates.
	 * 
	 * @return the velocity templates
	 */
    public List<VelocityTemplate> getVelocityTemplates();

    /**
	 * Gets the velocity template.
	 * 
	 * @param templateType the template type
	 * 
	 * @return the velocity template
	 */
    public VelocityTemplate getVelocityTemplate(String templateType);

    /**
	 * Gets the velocity template last modified date.
	 * 
	 * @param templateType the template type
	 * 
	 * @return the velocity template last modified date
	 */
    public Timestamp getVelocityTemplateLastModifiedDate(String templateType);

    /**
	 * Save template path.
	 * 
	 * @param velocityTemplate the velocity template
	 */
    public void saveTemplatePath(VelocityTemplate velocityTemplate);

    /**
	 * Gets the template title.
	 * 
	 * @param templateType the template type
	 * 
	 * @return the template title
	 */
    public String getTemplateTitle(String templateType);
}
