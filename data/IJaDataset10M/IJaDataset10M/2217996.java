package com.loribel.commons.gui;

import com.loribel.commons.abstraction.GB_ErrorReport;
import com.loribel.commons.util.GB_DateTools;
import com.loribel.commons.util.STools;

/**
 * Dialog to show error messages.
 */
public final class GB_ErrorReportTools {

    private GB_ErrorReportTools() {
    }

    public static void printSystemOut(GB_ErrorReport a_errorReport) {
        System.out.println("-----------------------------------------------");
        System.out.println("ERROR [" + GB_DateTools.getTodayIsoDateWithHours() + "]");
        System.out.println("-----------------------------------------------");
        System.out.println("Message: " + a_errorReport.getMessage());
        String l_code = a_errorReport.getErrorCode();
        if (STools.isNotNull(l_code)) {
            System.out.println("Code: " + l_code);
        }
        Throwable l_exception = a_errorReport.getException();
        if (l_exception != null) {
            l_exception.printStackTrace();
        } else {
            String l_details = a_errorReport.getDetails();
            if (STools.isNotNull(l_details)) {
                System.out.println("Details: " + l_code);
            }
        }
        System.out.println("-----------------------------------------------");
    }
}
