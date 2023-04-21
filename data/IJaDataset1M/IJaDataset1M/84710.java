package com.genia.toolbox.portlet.editor.model.portlet.impl;

import java.rmi.server.UID;
import java.util.List;
import com.genia.toolbox.basics.editor.model.document.VariableModel;
import com.genia.toolbox.web.portlet.description.ContainerPortletDescription.LayoutDirection;

/**
 * A container portlet model.
 */
public class ContainerPortletModel extends AbstractPortletModel {

    /**
   * The container view.
   */
    private String view = null;

    /**
   * The minimum number of sub-portlets.
   */
    private Integer minNbSubPortlets = null;

    /**
   * The maximum number of sub-portlets.
   */
    private Integer maxNbSubPortlets = null;

    /**
   * The layout direction.
   */
    private LayoutDirection direction = null;

    /**
   * The sub-portlets.
   */
    private List<PortletModel> subportlets = null;

    /**
   * The attributes.
   */
    private List<VariableModel> attributes = null;

    /**
   * Constructor.
   */
    public ContainerPortletModel() {
        super();
    }

    /**
   * Get the attributes.
   * 
   * @return the attributes.
   */
    public List<VariableModel> getAttributes() {
        return this.attributes;
    }

    /**
   * Set the attributes.
   * 
   * @param attributes
   *          the attributes.
   */
    public void setAttributes(List<VariableModel> attributes) {
        this.attributes = attributes;
    }

    /**
   * Get the maximum number of sub-portlets.
   * 
   * @return the maximum number of sub-portlets.
   */
    public Integer getMaxNbSubPortlets() {
        return this.maxNbSubPortlets;
    }

    /**
   * Set the maximum number of sub-portlets.
   * 
   * @param maxNbSubPortlets
   *          the maximum number of sub-portlets.
   */
    public void setMaxNbSubPortlets(Integer maxNbSubPortlets) {
        this.maxNbSubPortlets = maxNbSubPortlets;
    }

    /**
   * Get the minimum number of sub-portlets.
   * 
   * @return the minimum number of sub-portlets.
   */
    public Integer getMinNbSubPortlets() {
        return this.minNbSubPortlets;
    }

    /**
   * Set the minimum number of sub-portlets.
   * 
   * @param minNbSubPortlets
   *          the minimum number of sub-portlets.
   */
    public void setMinNbSubPortlets(Integer minNbSubPortlets) {
        this.minNbSubPortlets = minNbSubPortlets;
    }

    /**
   * Get the sub-portlets.
   * 
   * @return the sub-portlets.
   */
    public List<PortletModel> getSubportlets() {
        return this.subportlets;
    }

    /**
   * Set the sub-portlets.
   * 
   * @param subportlets
   *          the sub-portlets.
   */
    public void setSubportlets(List<PortletModel> subportlets) {
        this.subportlets = subportlets;
    }

    /**
   * Get the view name.
   * 
   * @return the view name.
   */
    public String getView() {
        return this.view;
    }

    /**
   * Set the view name.
   * 
   * @param view
   *          the view name.
   */
    public void setView(String view) {
        this.view = view;
    }

    /**
   * Get the direction.
   * 
   * @return the direction.
   */
    public LayoutDirection getDirection() {
        return this.direction;
    }

    /**
   * Set the direction.
   * 
   * @param direction
   *          the direction.
   */
    public void setDirection(LayoutDirection direction) {
        this.direction = direction;
    }

    /**
   * Update a sub-portlet.
   * 
   * @param updatedPortlet
   *          the updated portlet.
   */
    @Override
    public void updatePortlet(PortletModel updatedPortlet) {
        for (PortletModel subportlet : this.subportlets) {
            if (subportlet.getUniqueID().equals(updatedPortlet.getUniqueID())) {
                subportlet = updatedPortlet;
            } else {
                subportlet.updatePortlet(updatedPortlet);
            }
        }
    }

    /**
   * Get whether the settings are valid.
   * 
   * @return whether the settings are valid.
   */
    @Override
    public boolean accept() {
        boolean isOK = super.accept();
        for (VariableModel model : this.getAttributes()) {
            if (!model.accept()) {
                isOK = false;
                break;
            }
        }
        int nbSubPortlets = 0;
        if (this.getSubportlets() != null) {
            nbSubPortlets = this.getSubportlets().size();
        }
        if (nbSubPortlets < this.getMinNbSubPortlets()) {
            isOK = false;
        }
        for (PortletModel model : this.getSubportlets()) {
            if (!model.accept()) {
                isOK = false;
                break;
            }
        }
        return isOK;
    }

    /**
   * Set the document unique ID.
   * 
   * @param documentID
   *          the document unique ID.
   */
    @Override
    public void setDocumentID(UID documentID) {
        super.setDocumentID(documentID);
        for (PortletModel submodel : this.getSubportlets()) {
            submodel.setDocumentID(documentID);
        }
    }

    /**
   * Get whether the portlet is a template (it contains at least one hole).
   * 
   * @return whether the portlet is a template.
   */
    @Override
    public boolean isTemplate() {
        boolean isTemplate = false;
        for (PortletModel model : this.getSubportlets()) {
            if (model.isTemplate()) {
                isTemplate = true;
                break;
            }
        }
        return isTemplate;
    }

    /**
   * Get the number of sub-portlets.
   * 
   * @return the number of sub-portlets.
   */
    public int getNbSubPortlets() {
        int nbDisplayedSubPortlets = 1;
        if (this.getSubportlets() != null && !this.getSubportlets().isEmpty()) {
            nbDisplayedSubPortlets = this.getSubportlets().size();
        }
        return nbDisplayedSubPortlets;
    }

    /**
   * Get whether the element has at least one sub-element.
   * 
   * @return whether the element has at least one sub-element.
   */
    @Override
    public boolean isContainer() {
        return true;
    }
}
