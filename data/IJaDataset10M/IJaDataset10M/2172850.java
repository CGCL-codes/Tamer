package orcajo.azada.core.handlers;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

    private static final String BUNDLE_NAME = "orcajo.azada.core.handlers.messages";

    public static String QueryLinkHandler_error;

    public static String QueryLinkHandler_queryLink;

    public static String SwapAxesHandler_queryLinkBreakShort;

    public static String QueryLinkHandler_queryLinkBreak;

    public static String QueryLinkHandler_queryLinkJob_title;

    public static String SwapAxesHandler_swapOff;

    public static String SwapAxesHandler_Not_Supported;

    static {
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
