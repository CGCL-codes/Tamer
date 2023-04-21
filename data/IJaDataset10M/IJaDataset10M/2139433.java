package fr.cnes.sitools.plugins.applications.dto;

import java.util.ArrayList;
import fr.cnes.sitools.common.dto.ExtensionModelDTO;
import fr.cnes.sitools.common.model.Category;
import fr.cnes.sitools.plugins.applications.model.ApplicationPluginParameter;

/**
 * DTO for ApplicationPlugin
 * 
 * 
 * @author m.gond
 */
public class ApplicationPluginModelDTO extends ExtensionModelDTO<ApplicationPluginParameter> {

    /** serialVersionUID */
    private static final long serialVersionUID = -1506062585910799631L;

    /** the label of the applicationInstance */
    private String label;

    /** url attachment */
    private String urlAttach;

    /** the status of the application */
    private String status;

    /** category of the application plugin */
    private Category category;

    /**
   * Default constructor
   */
    public ApplicationPluginModelDTO() {
        this.setParameters(new ArrayList<ApplicationPluginParameter>());
        setCategory(Category.USER_DYNAMIC);
    }

    /**
   * Gets the urlAttach value
   * 
   * @return the urlAttach
   */
    public String getUrlAttach() {
        return urlAttach;
    }

    /**
   * Sets the value of urlAttach
   * 
   * @param urlAttach
   *          the urlAttach to set
   */
    public void setUrlAttach(String urlAttach) {
        this.urlAttach = urlAttach;
    }

    /**
   * Sets the value of status
   * 
   * @param status
   *          the status to set
   */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
   * Gets the status value
   * 
   * @return the status
   */
    public String getStatus() {
        return status;
    }

    /**
   * Sets the value of label
   * 
   * @param label
   *          the label to set
   */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
   * Gets the label value
   * 
   * @return the label
   */
    public String getLabel() {
        return label;
    }

    /**
   * Sets the value of category
   * 
   * @param category
   *          the category to set
   */
    public void setCategory(Category category) {
        this.category = category;
    }

    /**
   * Gets the category value
   * 
   * @return the category
   */
    public Category getCategory() {
        return category;
    }
}
