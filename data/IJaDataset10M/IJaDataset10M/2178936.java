package com.loribel.commons.swing;

import java.awt.Component;
import java.util.List;
import com.loribel.commons.abstraction.GB_ActionReport;
import com.loribel.commons.abstraction.GB_Cancelable;
import com.loribel.commons.abstraction.GB_Flag;
import com.loribel.commons.abstraction.GB_LabelIcon;
import com.loribel.commons.abstraction.GB_LabelIconOwnerSet;
import com.loribel.commons.abstraction.GB_ObjectActionReport;
import com.loribel.commons.abstraction.GB_StateMap;
import com.loribel.commons.exception.GB_Exception;
import com.loribel.commons.gui.action.GB_ActionReportsTableTools;
import com.loribel.commons.swing.tools.GB_ButtonTools;
import com.loribel.commons.swing.tools.GB_DialogTools;
import com.loribel.commons.util.CTools;
import com.loribel.commons.util.GB_ActionReportTools;
import com.loribel.commons.util.GB_LabelIconOwnerTools;
import com.loribel.commons.util.GB_ObjectActionReportTools;
import com.loribel.commons.util.Info;
import com.loribel.commons.util.impl.GB_ActionReportList;
import com.loribel.commons.util.impl.GB_FlagImpl;

/**
 * Button that implements GB_LongAction and use GB_ObjectActionReport.
 *
 * @author Gregory Borelli
 */
public abstract class GB_ButtonObjectActionReportAbstract extends GB_ButtonLongAction implements GB_LabelIconOwnerSet, GB_Cancelable {

    private GB_LabelIcon labelIcon;

    private GB_ActionReportList reports;

    /**
     * Items � traiter mis � jour par setItems
     */
    private List items;

    /**
     * Items � traiter mis � jour par setItems ou buildItems
     * Cette liste est recalcul� � chaque doAction()
     */
    private List itemsToTreat;

    private GB_Flag flagCancel = new GB_FlagImpl();

    private boolean cancelable = false;

    public GB_ButtonObjectActionReportAbstract() {
        super();
    }

    public GB_ButtonObjectActionReportAbstract(boolean a_bigIcon) {
        super();
        decoreBigIcon(a_bigIcon);
    }

    protected abstract List buildItems() throws Throwable;

    public boolean cancel() {
        flagCancel.setFlag(true);
        return true;
    }

    public void decoreBigIcon(boolean a_bigIcon) {
        if (a_bigIcon) {
            GB_ButtonTools.decoreBigIcon(this);
        }
    }

    public Object doAction() throws Throwable {
        Info.setTitleFromLabelIcon(getLabelIcon());
        flagCancel.setFlag(false);
        itemsToTreat = null;
        itemsToTreat = getItemsToTreat();
        GB_ObjectActionReport l_action = getObjectActionReport();
        if (CTools.isEmpty(itemsToTreat)) {
            throw new GB_Exception("Aucun item � traiter...");
        }
        if (l_action == null) {
            throw new GB_Exception("Aucune action � effectuer...");
        }
        reports = new GB_ActionReportList(getStateMap());
        cancelable = true;
        GB_ObjectActionReportTools.doAction(itemsToTreat, l_action, flagCancel, reports);
        return Boolean.TRUE;
    }

    public void doGuiAfter(Component a_parent, Object a_value) throws Throwable {
        if (a_value == null) {
            return;
        }
        showReports(a_parent);
        GB_ObjectActionReport l_action = getObjectActionReport();
        GB_DialogTools.showDoAfter(a_parent, l_action, a_value);
    }

    public boolean doGuiBefore(Component a_parent) throws Throwable {
        return true;
    }

    protected List getItemsToTreat() throws Throwable {
        if (itemsToTreat == null) {
            itemsToTreat = items;
        }
        if (itemsToTreat == null) {
            itemsToTreat = buildItems();
        }
        return itemsToTreat;
    }

    public GB_LabelIcon getLabelIcon() {
        if (labelIcon == null) {
            GB_ObjectActionReport l_action = getObjectActionReport();
            labelIcon = GB_LabelIconOwnerTools.getLabelIconSafe(l_action);
        }
        return labelIcon;
    }

    protected abstract GB_ObjectActionReport getObjectActionReport();

    /**
     * Overwrite this method to custom GB_StateMap to used.
     */
    protected GB_StateMap getStateMap() {
        return GB_ActionReportTools.STATE_MAP_ACTION;
    }

    public boolean isCancelable() {
        return cancelable;
    }

    public void setItems(List a_items) {
        items = a_items;
    }

    public void setLabelIcon(GB_LabelIcon a_labelIcon) {
        labelIcon = a_labelIcon;
    }

    protected void showReports(Component a_parent) throws Throwable {
        GB_ActionReport[] l_reports = reports.toArrayActionReport();
        GB_ActionReportsTableTools.showReports(a_parent, getItemsToTreat(), l_reports);
    }
}
