package org.lcx.scrumvision.ui.editor;

import org.eclipse.osgi.util.NLS;

/**
 * @author Laurent Carbonnaux
 */
public class Messages extends NLS {

    private static final String BUNDLE_NAME = "org.lcx.scrumvision.ui.editor.messages";

    static {
        reloadMessages();
    }

    public static void reloadMessages() {
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    public static String ScrumVisionTaskEditorPageFactory_title;
}
