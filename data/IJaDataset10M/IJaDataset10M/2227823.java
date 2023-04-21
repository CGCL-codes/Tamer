package org.oclc.da.ndiipp.struts.system.form;

import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.oclc.da.ndiipp.struts.core.form.NDIIPPForm;

/**
 * This class holds and handles the data used to render reports
 * <P>
 * @author Joe Nelson
 */
public class ManageReportsForm extends NDIIPPForm {

    /**
     * This is the list of all entities in the tool.
     */
    private ArrayList allEntities = new ArrayList();

    /**
     * What entity are we reporting on?
     */
    private String entity = "";

    /**
     * From which entity do we want the series list derived?
     */
    private String seriesSource = "";

    /**
     * Gets the allEntities
     * <P>
     * @return Returns the allEntities.
     */
    public ArrayList getAllEntities() {
        return allEntities;
    }

    /**
     * Gets the entity
     * <P>
     * @return Returns the entity.
     */
    public String getEntity() {
        return entity;
    }

    /**
     * Gets the seriesSource
     * <P>
     * @return Returns the seriesSource.
     */
    public String getSeriesSource() {
        return seriesSource;
    }

    /**
     * (non-Javadoc)
     * @see org.apache.struts.action.ActionForm#reset(
     *      org.apache.struts.action.ActionMapping,
     *      javax.servlet.http.HttpServletRequest)
     */
    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
    }

    /**
     * Sets the allEntities
     * <P>
     * @param allEntities The allEntities to set.
     */
    public void setAllEntities(ArrayList allEntities) {
        this.allEntities = allEntities;
    }

    /**
     * Sets the entity
     * <P>
     * @param entity The entity to set.
     */
    public void setEntity(String entity) {
        this.entity = entity;
    }

    /**
     * Sets the seriesSource
     * <P>
     * @param seriesSource The seriesSource to set.
     */
    public void setSeriesSource(String seriesSource) {
        this.seriesSource = seriesSource;
    }

    /**
     * (non-Javadoc)
     * @see org.apache.struts.action.ActionForm#validate(
     *      org.apache.struts.action.ActionMapping,
     *      javax.servlet.http.HttpServletRequest)
     */
    @Override
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors ae = new ActionErrors();
        if (request.getParameter("entityDetail") != null) {
            if ("".equals(entity)) {
                ae.add("entity", new ActionMessage("error.no.entity"));
            }
        }
        if (request.getParameter("seriesDetail") != null) {
            if ("".equals(seriesSource)) {
                ae.add("seriesSource", new ActionMessage("error.no.entity"));
            }
        }
        return (ae);
    }
}
