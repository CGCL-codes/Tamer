package de.walware.statet.ext.ui.editors;

import java.util.ResourceBundle;
import org.eclipse.osgi.util.NLS;

public class EditorMessages extends NLS {

    public static String GotoMatchingBracketAction_label;

    public static String GotoMatchingBracketAction_tooltip;

    public static String GotoMatchingBracketAction_description;

    public static String GotoMatchingBracketAction_error_BracketOutsideSelectedElement;

    public static String GotoMatchingBracketAction_error_InvalidSelection;

    public static String GotoMatchingBracketAction_error_NoMatchingBracket;

    public static String ToggleCommentAction_error;

    public static String FoldingMenu_label;

    private static final String BUNDLE_NAME = EditorMessages.class.getName();

    static {
        NLS.initializeMessages(BUNDLE_NAME, EditorMessages.class);
    }

    private static ResourceBundle fgCompatibilityBundle = ResourceBundle.getBundle(BUNDLE_NAME);

    public static ResourceBundle getCompatibilityBundle() {
        return fgCompatibilityBundle;
    }
}
