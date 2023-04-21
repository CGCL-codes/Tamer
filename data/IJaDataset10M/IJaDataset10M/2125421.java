package com.genia.toolbox.portlet.editor.model.document.impl;

import java.io.File;
import java.rmi.server.UID;
import com.genia.toolbox.basics.editor.model.document.Document;
import com.genia.toolbox.portlet.editor.model.portlet.PortletType;
import com.genia.toolbox.portlet.editor.model.portlet.impl.PortletModel;

/**
 * The document model.
 */
public class DocumentModel implements Document<PortletModel> {

    /**
   * The document name.
   */
    private String documentName = null;

    /**
   * Whether is document is saved, and has not been modified.
   */
    private boolean isSaved = false;

    /**
   * The root portlet model.
   */
    private PortletModel portletModel = null;

    /**
   * The root portlet type.
   */
    private PortletType rootPortletType = null;

    /**
   * The unique ID.
   */
    private UID uniqueId = null;

    /**
   * The XML file.
   */
    private File xml = null;

    /**
   * Constructor.
   * 
   * @param name
   *          the document name.
   * @param xml
   *          the xml file.
   * @param portletModel
   *          the portlet model.
   */
    public DocumentModel(String name, File xml, PortletModel portletModel) {
        this(name, portletModel);
        this.xml = xml;
    }

    /**
   * Constructor.
   * 
   * @param name
   *          the document name.
   * @param portletModel
   *          the portlet model.
   */
    public DocumentModel(String name, PortletModel portletModel) {
        super();
        this.uniqueId = new UID();
        this.portletModel = portletModel;
        this.portletModel.setDocumentID(this.uniqueId);
        this.isSaved = false;
        this.documentName = name;
        this.rootPortletType = portletModel.getType();
    }

    /**
   * Check whether the settings are valid.
   * 
   * @return whether the settings are valid.
   */
    public boolean accept() {
        boolean isOK = true;
        if (this.getName() == null || this.getName().trim().length() == 0) {
            isOK = false;
        } else if (this.portletModel == null || !this.portletModel.accept()) {
            isOK = false;
        }
        return isOK;
    }

    /**
   * Get the document name.
   * 
   * @return the document name.
   */
    public String getName() {
        return this.documentName;
    }

    /**
   * Get the root portlet model.
   * 
   * @return the root porlet model.
   */
    public PortletModel getElementModel() {
        return this.portletModel;
    }

    /**
   * Get the portlet type.
   * 
   * @return the portlet type.
   */
    public PortletType getPortletType() {
        return this.rootPortletType;
    }

    /**
   * The unique ID.
   * 
   * @return the unique ID.
   */
    public UID getUniqueId() {
        return this.uniqueId;
    }

    /**
   * Get whether the document is saved and has not been modified.
   * 
   * @return whether the document is saved and has not been modified.
   */
    public boolean isSaved() {
        if (this.xml == null) {
            this.isSaved = false;
        }
        return this.isSaved;
    }

    /**
   * Get whether the document is a template (it contains at least one hole).
   * 
   * @return true if the document is a template, false if it is a portlet.
   */
    public boolean isTemplate() {
        return this.getElementModel().isTemplate();
    }

    /**
   * Set whether the document is saved and has not been modified.
   * 
   * @param saved
   *          whether the document is saved and has not been modified.
   */
    public void setIsSaved(boolean saved) {
        this.isSaved = saved;
    }

    /**
   * Set the document name.
   * 
   * @param name
   *          the document name.
   */
    public void setName(String name) {
        this.documentName = name;
    }

    /**
   * Set the root portlet model.
   * 
   * @param portletModel
   *          the root portlet model.
   */
    public void setElementModel(PortletModel portletModel) {
        this.portletModel = portletModel;
    }

    /**
   * Set the portlet type.
   * 
   * @param portletType
   *          the portlet type.
   */
    public void setPortletType(PortletType portletType) {
        this.rootPortletType = portletType;
    }

    /**
   * Set the xml file.
   * 
   * @param xml
   *          the xml file.
   */
    public void setXml(File xml) {
        this.xml = xml;
    }

    /**
   * Get the xml file.
   * 
   * @return the xml file.
   */
    public File getXml() {
        return this.xml;
    }

    /**
   * Update the specified portlet.
   * 
   * @param updatedPortlet
   *          the update portlet model.
   */
    public void updateElement(PortletModel updatedPortlet) {
        if (this.portletModel.getUniqueID().equals(updatedPortlet.getUniqueID())) {
            this.portletModel = updatedPortlet;
        } else {
            this.portletModel.updatePortlet(updatedPortlet);
        }
    }
}
