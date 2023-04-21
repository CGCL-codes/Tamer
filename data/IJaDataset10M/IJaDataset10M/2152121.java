package org.lcelb.accounts.manager.ui.workbench.views.totals;

import org.eclipse.osgi.util.NLS;

/**
 * @author Guigui
 *
 * 19 juil. 08
 */
public class Messages extends NLS {

    private static final String BUNDLE_NAME = "org.lcelb.accounts.manager.ui.workbench.views.totals.messages";

    public static String TotalsView_NoDataMessage;

    static {
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
