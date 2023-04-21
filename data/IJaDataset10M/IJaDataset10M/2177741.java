package com.loribel.tools.fa.vm.action;

import java.io.File;
import java.io.IOException;
import javax.swing.JComponent;
import com.loribel.commons.abstraction.GB_ActionReport;
import com.loribel.commons.abstraction.GB_LabelIcon;
import com.loribel.commons.abstraction.GB_ObjectActionReport;
import com.loribel.commons.abstraction.GB_ObjectActionReportOwner;
import com.loribel.commons.abstraction.GB_StringAction;
import com.loribel.commons.exception.GB_ConfigException;
import com.loribel.commons.gui.GB_ViewManagerAbstract;
import com.loribel.commons.module.states.GB_StateMapAction;
import com.loribel.commons.swing.GB_PanelRows;
import com.loribel.commons.util.FTools;
import com.loribel.commons.util.GB_ExceptionTools;
import com.loribel.commons.util.GB_LabelIconTools;
import com.loribel.commons.util.impl.GB_ActionReportImpl;
import com.loribel.tools.sa.swing.GB_PanelSASelect;

/**
 * File Rename.
 * 
 * @author Gr�gory Borelli
 */
public class GB_FileModifyVM extends GB_ViewManagerAbstract implements GB_ObjectActionReportOwner, GB_ObjectActionReport {

    private class MyView extends GB_PanelRows {

        MyView() {
            super();
            panelSA = new GB_PanelSASelect(null);
            this.addRowFill(panelSA);
            this.addRowEnd();
        }
    }

    private GB_StringAction sa;

    private GB_PanelSASelect panelSA;

    private boolean flagSimulation = false;

    public GB_FileModifyVM() {
        super();
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
        try {
            File l_file = (File) a_file;
            String l_content = FTools.readFile(l_file);
            String l_newContent = sa.doActionStr(l_content);
            boolean l_change = true;
            if ((l_content.length() == l_newContent.length()) && (l_content.equals(l_newContent))) {
                l_change = false;
            }
            if (!l_change) {
                return new GB_ActionReportImpl(GB_StateMapAction.STATE_NOTHING_TO_DO, null);
            }
            int l_stateSuccess = GB_StateMapAction.STATE_SUCCESS;
            if (!flagSimulation) {
                FTools.writeFile(l_file, l_newContent, false);
            } else {
                l_stateSuccess = GB_StateMapAction.STATE_TO_DO;
            }
            String l_msg = "Fichier modifi�";
            return new GB_ActionReportImpl(l_stateSuccess, l_msg);
        } catch (IOException ex) {
            String l_details = GB_ExceptionTools.toStackTraceString(ex);
            String l_msg = "Erreur";
            return new GB_ActionReportImpl(GB_StateMapAction.STATE_ERROR, l_msg, l_details);
        }
    }

    public GB_ObjectActionReport getAction(boolean a_useSimulation) throws GB_ConfigException {
        if (panelSA.getStringAction() == null) {
            throw new GB_ConfigException("Vous devez d�finir une action pour modifier le contenu de vos fichiers.");
        }
        flagSimulation = a_useSimulation;
        return this;
    }

    public GB_LabelIcon getLabelIcon() {
        String l_label = "Modifier le contenu";
        return GB_LabelIconTools.newLabelIcon(l_label);
    }

    public boolean isSimulationAvailable() {
        return true;
    }
}
