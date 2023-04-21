package com.loribel.commons.util.impl;

import com.loribel.commons.abstraction.GB_ErrorMgr;
import com.loribel.commons.abstraction.GB_ErrorReport;

/**
 * Default implementation of GB_ErrorMgr.
 *
 * @author Gregory Borelli
 */
public class GB_ErrorMgrImpl implements GB_ErrorMgr {

    public GB_ErrorReport buildReport(Object a_source, int a_type, String a_title, String a_message, Throwable a_throwable) {
        return new GB_ErrorReportImpl(a_type, a_title, a_message, a_throwable);
    }

    public boolean isReportErrorAvailable() {
        return false;
    }

    public String reportError(GB_ErrorReport a_report) {
        return AA.MSG_REPORT_ERROR_NOT_AVAILABLE;
    }
}
