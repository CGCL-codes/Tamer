package org.eclipse.ui.internal.texteditor;

import org.eclipse.osgi.util.NLS;

/**
 * Helper class to get NLSed messages.
 */
final class RulerMessages extends NLS {

    private static final String BUNDLE_NAME = RulerMessages.class.getName();

    private RulerMessages() {
    }

    public static String AbstractDecoratedTextEditor_revision_quickdiff_switch_title;

    public static String AbstractDecoratedTextEditor_revision_quickdiff_switch_message;

    public static String AbstractDecoratedTextEditor_revision_quickdiff_switch_rememberquestion;

    static {
        NLS.initializeMessages(BUNDLE_NAME, RulerMessages.class);
    }
}
