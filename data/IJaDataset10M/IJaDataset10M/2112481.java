package org.docflower.sylvia.rcp.util;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

    private static final String BUNDLE_NAME = "org.docflower.sylvia.rcp.locale.messages";

    public static String NewReportWizard_0;

    public static String NewReportWizard_1;

    public static String NewReportWizardMainPage_0;

    public static String NewReportWizardMainPage_1;

    public static String NewReportWizardMainPage_2;

    public static String NewReportWizardModel_0;

    public static String NewReportWizardModel_1;

    public static String NewReportWizardModel_2;

    static {
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
