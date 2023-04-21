package com.loribel.commons.util;

import java.util.Collection;
import java.util.Iterator;
import com.loribel.commons.abstraction.GB_ActionBeforeAfter;
import com.loribel.commons.abstraction.GB_ActionReport;
import com.loribel.commons.abstraction.GB_Flag;
import com.loribel.commons.abstraction.GB_ItemWithReport;
import com.loribel.commons.abstraction.GB_ObjectActionReport;
import com.loribel.commons.module.states.GB_StateMapInfo;
import com.loribel.commons.util.impl.GB_ActionReportList;

/**
 * Tools for GB_ObjectActionReport.
 *
 * @author Gregory Borelli
 */
public final class GB_ObjectActionReportTools {

    /**
     * Execute an action and add report to a_returnReports if not null.
     */
    public static void doAction(Collection a_items, GB_ObjectActionReport a_action, GB_Flag a_cancel, GB_ActionReportList a_returnReports) throws Exception {
        if (a_items == null) {
            return;
        }
        GB_ActionBeforeAfter l_action2 = null;
        if (a_action instanceof GB_ActionBeforeAfter) {
            l_action2 = (GB_ActionBeforeAfter) a_action;
            if (!l_action2.doBefore()) {
                return;
            }
        }
        GB_ActionReport l_report;
        Object l_item;
        int len = CTools.getSize(a_items);
        String l_title = GB_LabelIconTools.getLabelSafe(a_action);
        int l_idInfo = Info.newId(len, l_title);
        int i = 0;
        for (Iterator it = a_items.iterator(); it.hasNext(); ) {
            if (GB_FlagTools.isTrue(a_cancel)) {
                break;
            }
            l_item = it.next();
            Info.setInfoList(l_idInfo, i, l_item);
            l_report = GB_ObjectActionReportTools.doActionSafe(l_item, a_action);
            if (a_returnReports != null) {
                a_returnReports.add(l_report);
                String l_info = a_returnReports.getInfo();
                Info.setInfo(l_info);
            }
            i++;
        }
        Info.end(l_idInfo);
        if (l_action2 != null) {
            l_action2.doAfter();
        }
    }

    public static GB_ActionReport[] doAction(Collection a_items, GB_ObjectActionReport a_action, GB_Flag a_cancel, GB_StateMapInfo a_stateMapInfo) throws Exception {
        if (a_items == null) {
            return new GB_ActionReport[0];
        }
        GB_ActionBeforeAfter l_action2 = null;
        if (a_action instanceof GB_ActionBeforeAfter) {
            l_action2 = (GB_ActionBeforeAfter) a_action;
            if (!l_action2.doBefore()) {
                return new GB_ActionReport[0];
            }
        }
        GB_ActionReport l_report;
        Object l_item;
        int len = CTools.getSize(a_items);
        String l_title = GB_LabelIconTools.getLabelSafe(a_action);
        int l_idInfo = Info.newId(len, l_title);
        GB_ActionReport[] retour = new GB_ActionReport[len];
        int i = 0;
        for (Iterator it = a_items.iterator(); it.hasNext(); ) {
            if (GB_FlagTools.isTrue(a_cancel)) {
                break;
            }
            String l_info = "";
            if (a_stateMapInfo != null) {
                l_info = a_stateMapInfo.getInfo() + " - ";
            }
            l_item = it.next();
            l_info += l_item;
            Info.setInfoList(l_idInfo, i + 1, l_info);
            l_report = doActionSafe(l_item, a_action);
            if (a_stateMapInfo != null) {
                a_stateMapInfo.addItem(l_report);
            }
            retour[i] = l_report;
            i++;
        }
        Info.end(l_idInfo);
        if (l_action2 != null) {
            l_action2.doAfter();
        }
        return retour;
    }

    public static GB_ActionReport[] doAction(Object[] a_items, GB_ObjectActionReport a_action, GB_Flag a_cancel, GB_StateMapInfo a_stateMapInfo) throws Exception {
        if (a_items == null) {
            return new GB_ActionReport[0];
        }
        GB_ActionBeforeAfter l_action2 = null;
        if (a_action instanceof GB_ActionBeforeAfter) {
            l_action2 = (GB_ActionBeforeAfter) a_action;
            if (!l_action2.doBefore()) {
                return new GB_ActionReport[0];
            }
        }
        GB_ActionReport l_report;
        Object l_item;
        String l_info = "";
        int len = CTools.getSize(a_items);
        int l_idInfo = Info.newId(len);
        GB_ActionReport[] retour = new GB_ActionReport[len];
        for (int i = 0; i < len; i++) {
            if (GB_FlagTools.isTrue(a_cancel)) {
                break;
            }
            if (a_stateMapInfo != null) {
                l_info = a_stateMapInfo.getInfo() + " - ";
            }
            l_item = a_items[i];
            l_info += l_item;
            Info.setInfoList(l_idInfo, i + 1, l_info);
            l_report = doActionSafe(l_item, a_action);
            if (a_stateMapInfo != null) {
                a_stateMapInfo.addItem(l_report);
            }
            retour[i] = l_report;
        }
        Info.end(l_idInfo);
        if (l_action2 != null) {
            l_action2.doAfter();
        }
        return retour;
    }

    public static GB_ActionReport doActionSafe(Object a_item, GB_ObjectActionReport a_action) {
        try {
            return a_action.doActionRpt(a_item);
        } catch (Throwable ex) {
            ex.printStackTrace();
            String l_msg = ex.getMessage();
            return GB_ActionReportTools.newReportError(l_msg, ex);
        }
    }

    public static GB_ItemWithReport[] doActionToItemsWithReports(Object[] a_items, GB_ObjectActionReport a_action, GB_Flag a_cancel, GB_StateMapInfo a_stateMapInfo) throws Exception {
        GB_ActionReport[] l_reports = doAction(a_items, a_action, a_cancel, a_stateMapInfo);
        return GB_ItemWithReportTools.buildItemsWithReports(a_items, l_reports, a_cancel);
    }

    private GB_ObjectActionReportTools() {
    }
}
