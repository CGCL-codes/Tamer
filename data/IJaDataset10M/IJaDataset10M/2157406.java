package com.loribel.tools.business.action;

import java.awt.Component;
import javax.swing.Icon;
import com.loribel.commons.abstraction.GB_ActionReport;
import com.loribel.commons.abstraction.GB_LabelIcon;
import com.loribel.commons.abstraction.GB_OverwriteOwnerSet;
import com.loribel.commons.abstraction.GB_SavableO;
import com.loribel.commons.business.abstraction.GB_SimpleBusinessObject;
import com.loribel.commons.swing.tools.GB_DialogTools;
import com.loribel.commons.util.GB_ActionReportTools;
import com.loribel.commons.util.GB_IconTools;
import com.loribel.commons.util.GB_LabelIconTools;

/**
 * Action pour sauvegarder les BO.
 * 
 * @author Gregory Borelli
 */
public class GB_BOSaveAction extends GB_BOActionAbstract implements GB_OverwriteOwnerSet {

    public GB_BOSaveAction() {
        super();
    }

    protected GB_ActionReport doActionRptOnBo(GB_SimpleBusinessObject a_item) throws Throwable {
        if (a_item instanceof GB_SavableO) {
            GB_SavableO l_bo = (GB_SavableO) a_item;
            if (isSimulation()) {
                return GB_ActionReportTools.newReportToDo("To save (Simulation: pas de test overwrite)...");
            } else {
                l_bo.save(0, isOverwrite());
                return GB_ActionReportTools.newReportSuccess("Save OK");
            }
        }
        return GB_ActionReportTools.newReportWarning("Item not GB_SavableO");
    }

    public boolean doGuiBefore(Component a_parent) throws Throwable {
        boolean r = GB_DialogTools.showQuestionsInterface(a_parent, this);
        return r;
    }

    public GB_LabelIcon getLabelIcon() {
        String l_label = "Sauvegarde des BO...";
        Icon l_icon = GB_IconTools.get(AA.ICON.X32_FILE_ACTION);
        String l_desc = "...";
        return GB_LabelIconTools.newLabelIcon(l_label, l_icon, l_desc);
    }

    public String getMessageOverwrite() {
        return "";
    }
}
