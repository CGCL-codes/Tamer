package org.osmius.service;

import org.osmius.model.OsmEventTemplate;
import java.util.List;

/**
 * Business Service Interface to handle communication between web and persistence layer.
 * Exposes the neccessary methods to handle templates of events
 */
public interface OsmEventTemplateManager extends Manager {

    /**
    * Gets a template of events
    * @param indTemplate The identifier of the template
    * @return The Template
    */
    public OsmEventTemplate getOsmEventTemplate(String typInstance, String indTemplate);

    /**
    * Gets a list of templates of events
    * @param osmEventTemplate A parametrized object to filter. Can be null
    * @return The list of templates
    */
    public List getOsmEventTemplates(OsmEventTemplate osmEventTemplate);

    public void removeOsmEventTemplate(String typInstance, String idnTemplate);
}
