package net.sourceforge.transmogrify.hook.ffj;

import net.sourceforge.transmogrify.hook.*;
import javax.swing.*;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;
import org.openide.*;
import org.openide.cookies.*;
import org.openide.nodes.*;
import org.openide.windows.*;

public abstract class NoPantsAction extends CallableSystemAction {

    private static NoPants noPants;

    public NoPants getNoPants() {
        if (noPants == null) {
            noPants = new NoPants();
        }
        return noPants;
    }

    public EditorCookie getEditorCookie() {
        TopComponent comp = TopManager.getDefault().getWindowManager().getRegistry().getActivated();
        Node[] nodes = comp.getRegistry().getActivatedNodes();
        EditorCookie cookie = null;
        if (nodes.length == 1) {
            cookie = (EditorCookie) nodes[0].getCookie(EditorCookie.class);
        }
        return cookie;
    }

    public JEditorPane getEditorPane(EditorCookie cookie) {
        JEditorPane[] panes = cookie.getOpenedPanes();
        if (panes.length == 1) {
            return panes[0];
        }
        return null;
    }
}
