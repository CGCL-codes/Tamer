package com.loribel.tools.file.gui;

import java.io.File;
import java.io.IOException;
import javax.swing.JComponent;
import com.loribel.commons.abstraction.GB_ActionReport;
import com.loribel.commons.abstraction.GB_LabelIcon;
import com.loribel.commons.abstraction.GB_ObjectActionReport;
import com.loribel.commons.abstraction.GB_ObjectActionReportOwner;
import com.loribel.commons.abstraction.GB_StringAction;
import com.loribel.commons.business.abstraction.GB_BOConfigurable;
import com.loribel.commons.business.abstraction.GB_SimpleBusinessObject;
import com.loribel.commons.exception.GB_ConfigException;
import com.loribel.commons.gui.bo.GB_BOPanelSingleObject;
import com.loribel.commons.module.states.GB_StateMapAction;
import com.loribel.commons.swing.GB_PanelRows;
import com.loribel.commons.util.FTools;
import com.loribel.commons.util.GB_ExceptionTools;
import com.loribel.commons.util.GB_LabelIconTools;
import com.loribel.commons.util.impl.GB_ActionReportImpl;
import com.loribel.tools.fa.bo.GB_FARenameBO;
import com.loribel.tools.sa.swing.GB_PanelSASelect;

/**
 * File Copy.
 * 
 * @author Gr�gory Borelli
 */
public class GB_FACopy implements GB_BOConfigurable, GB_ObjectActionReportOwner, GB_ObjectActionReport {

    private class MyView extends GB_PanelRows {

        MyView() {
            super();
            GB_BOPanelSingleObject l_panelConfig = new GB_BOPanelSingleObject(config);
            l_panelConfig.addAllProperties();
            panelSA = new GB_PanelSASelect(null);
            this.addRowFill(panelSA);
            this.addRowFill(l_panelConfig);
            this.addRowEnd();
        }
    }

    private GB_FARenameBO config;

    private GB_StringAction sa;

    private GB_PanelSASelect panelSA;

    protected boolean flagSimulation = false;

    public GB_FACopy() {
        super();
        config = new GB_FARenameBO();
    }

    public boolean acceptSimulation() {
        return true;
    }

    protected JComponent buildView() {
        return new MyView();
    }

    public GB_ActionReport doActionRpt(Object a_file) {
        if (sa == null) {
            sa = panelSA.getStringAction();
        }
        File l_file = (File) a_file;
        String l_name;
        String l_newName;
        switch(config.getType()) {
            case GB_FARenameBO.TYPE.FULL_NAME:
                l_name = l_file.getAbsolutePath();
                l_newName = sa.doActionStr(l_name);
                return treatFile(l_file, l_newName);
            case GB_FARenameBO.TYPE.NAME:
                l_name = l_file.getName();
                l_newName = l_file.getParent() + AA.FS + sa.doActionStr(l_name);
                return treatFile(l_file, l_newName);
            case GB_FARenameBO.TYPE.DIRECTORY:
                l_name = l_file.getParent();
                l_newName = sa.doActionStr(l_name) + AA.FS + l_file.getName();
                return treatFile(l_file, l_newName);
            default:
                return new GB_ActionReportImpl(GB_StateMapAction.STATE_ERROR);
        }
    }

    public GB_ObjectActionReport getAction(boolean a_useSimulation) throws GB_ConfigException {
        if (panelSA.getStringAction() == null) {
            throw new GB_ConfigException("Vous devez d�finir une action pour renommer ou copier les fichiers.");
        }
        flagSimulation = a_useSimulation;
        return this;
    }

    /**
     * Returns the config of this action.
     *
     * @return GB_SimpleBusinessObject
     */
    public GB_SimpleBusinessObject getBOConfig() {
        return config;
    }

    public GB_LabelIcon getLabelIcon() {
        String l_label = "Copier des fichiers";
        return GB_LabelIconTools.newLabelIcon(l_label);
    }

    public boolean isSimulationAvailable() {
        return true;
    }

    /**
     * Sets the config of this action.
     */
    public void setBOConfig(GB_SimpleBusinessObject a_config) throws GB_ConfigException {
        GB_FARenameBO l_config = null;
        try {
            l_config = (GB_FARenameBO) a_config;
        } catch (ClassCastException e) {
            throw new GB_ConfigException(e);
        }
        setConfig(l_config);
    }

    /**
     * Sets the config of this action.
     */
    public void setConfig(GB_FARenameBO a_config) {
        config = a_config;
    }

    protected GB_ActionReport treatFile(File a_file, String a_newName) {
        try {
            a_newName = a_newName.trim();
            String l_name = a_file.getAbsolutePath().trim();
            System.out.println(l_name + "->\n" + a_newName);
            if (l_name.equals(a_newName)) {
                System.out.println("IDEM: " + l_name);
                return new GB_ActionReportImpl(GB_StateMapAction.STATE_NOTHING_TO_DO);
            }
            File l_newFile = new File(a_newName);
            l_newFile.getParentFile().mkdirs();
            if (!flagSimulation) {
                FTools.copy(a_file, l_newFile);
            }
            String l_msg;
            if (a_file.getParent().equalsIgnoreCase(l_newFile.getParent())) {
                l_msg = "Fichier copi�: " + l_newFile.getName();
            } else if (a_file.getName().equalsIgnoreCase(l_newFile.getName())) {
                l_msg = "Fichier copi� " + l_newFile.getParent();
            } else {
                l_msg = "Fichier copi� et renomm�: " + l_newFile.getAbsolutePath();
            }
            return new GB_ActionReportImpl(GB_StateMapAction.STATE_SUCCESS, l_msg);
        } catch (IOException ex) {
            String l_msg = "Erreur de copie: " + a_newName;
            String l_details = GB_ExceptionTools.toStackTraceString(ex);
            return new GB_ActionReportImpl(GB_StateMapAction.STATE_ERROR, l_msg, l_details);
        }
    }
}
