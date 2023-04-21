package calclipse.caldron.gui.script;

import calclipse.core.gui.Dispatcher;
import calclipse.lib.math.rpn.RPNException;
import calclipse.mcomp.MCLocator;
import calclipse.mcomp.MComp;

/**
 * Used for locating
 * {@link calclipse.caldron.gui.script.ScriptItem}s
 * in the project.
 * @author T. Sommerland
 */
public class ScriptItemLocator implements MCLocator {

    public ScriptItemLocator() {
    }

    @Override
    public boolean acceptsURL(final String url, final String referrer) {
        return projectContains(url);
    }

    @Override
    public MComp getMComp(final String url, final String referrer) throws RPNException {
        return getScriptItemAsMComp(url);
    }

    private static boolean projectContains(final String url) {
        final boolean[] contains = new boolean[1];
        Dispatcher.dispatch(new Runnable() {

            @Override
            public void run() {
                contains[0] = ScriptItem.getItem(url) != null;
            }
        });
        return contains[0];
    }

    private static MComp getScriptItemAsMComp(final String url) throws RPNException {
        final ScriptCapture[] capture = new ScriptCapture[1];
        Dispatcher.dispatch(new Runnable() {

            @Override
            public void run() {
                final ScriptItem item = ScriptItem.getItem(url);
                if (item != null) {
                    capture[0] = new ScriptCapture(item.getEditor(), false);
                }
            }
        });
        if (capture[0] == null) {
            return null;
        }
        return capture[0].getMComp();
    }
}
