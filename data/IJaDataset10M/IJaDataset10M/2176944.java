package org.rubypeople.rdt.internal.ui.browsing;

import org.eclipse.osgi.util.NLS;

/**
 * Helper class to get NLSed messages.
 */
public final class RubyBrowsingMessages extends NLS {

    private static final String BUNDLE_NAME = RubyBrowsingMessages.class.getName();

    private RubyBrowsingMessages() {
    }

    public static String RubyBrowsingPart_toolTip;

    public static String RubyBrowsingPart_toolTip2;

    public static String LexicalSortingAction_label;

    public static String LexicalSortingAction_tooltip;

    public static String LexicalSortingAction_description;

    public static String StatusBar_concat;

    static {
        NLS.initializeMessages(BUNDLE_NAME, RubyBrowsingMessages.class);
    }
}
