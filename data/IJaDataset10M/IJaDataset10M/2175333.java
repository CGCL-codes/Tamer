package de.walware.statet.nico.core;

import org.eclipse.osgi.util.NLS;

/**
 * Public/shared strings of Nico Core.
 */
public class NicoCoreMessages {

    public static String Status_Starting_label;

    public static String Status_StartedIdle_label;

    public static String Status_StartedProcessing_label;

    public static String Status_StartedPaused_label;

    public static String Status_StartedSuspended_label;

    public static String Status_Terminated_label;

    public static String LoadHistoryJob_label;

    public static String SaveHistoryJob_label;

    public static String SubmitTask_label;

    private static final String BUNDLE_NAME = NicoCoreMessages.class.getName();

    static {
        NLS.initializeMessages(BUNDLE_NAME, NicoCoreMessages.class);
    }

    private NicoCoreMessages() {
    }
}
