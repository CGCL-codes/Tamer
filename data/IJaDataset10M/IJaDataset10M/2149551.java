package uk.ac.bolton.archimate.canvas.policies;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

    private static final String BUNDLE_NAME = "uk.ac.bolton.archimate.canvas.policies.messages";

    public static String CanvasDNDEditPolicy_0;

    public static String CanvasDNDEditPolicy_1;

    public static String CanvasDNDEditPolicy_2;

    static {
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
