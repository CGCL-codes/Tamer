package ag.ion.noa4e.editor.ui.actions;

import org.eclipse.osgi.util.NLS;

/**
 * Native language binding.
 * 
 * @author Andreas Br�ker
 * @version $Revision: 9209 $
 */
public class Messages extends NLS {

    private static final String BUNDLE_NAME = "ag.ion.noa4e.editor.ui.actions.messages";

    private Messages() {
    }

    static {
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    public static String PrintAction_text;

    public static String PrintAction_dialog_title_internalError;
}
