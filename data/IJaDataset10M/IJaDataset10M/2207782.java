package firstmate;

import org.gjt.sp.jedit.EBMessage;
import org.gjt.sp.jedit.EBPlugin;
import org.gjt.sp.jedit.jEdit;
import org.gjt.sp.jedit.View;
import org.gjt.sp.jedit.gui.DefaultInputHandler;
import org.gjt.sp.jedit.gui.InputHandler;
import org.gjt.sp.jedit.msg.ViewUpdate;
import org.gjt.sp.util.Log;

/**
 * FirstMate plugin.
 * 
 * Implements some features from TextMate.
 */
public class FirstMatePlugin extends EBPlugin {

    public void start() {
        loadProperties();
        enabled = jEdit.getBooleanProperty("firstmate.auto-enable", true);
        if (enabled) {
            Log.log(Log.DEBUG, this, "Adding FirstMate input handlers");
            View[] views = jEdit.getViews();
            for (int i = 0; i < views.length; i++) {
                initView(views[i]);
            }
        }
    }

    public void stop() {
        if (enabled) {
            View[] views = jEdit.getViews();
            for (int i = 0; i < views.length; i++) {
                unInitView(views[i]);
            }
        }
    }

    public void handleMessage(EBMessage msg) {
        if (msg instanceof ViewUpdate) {
            ViewUpdate vu = (ViewUpdate) msg;
            if (vu.getWhat() == ViewUpdate.CREATED) {
                if (enabled) initView(vu.getView());
            } else if (vu.getWhat() == ViewUpdate.CLOSED) {
                if (enabled) unInitView(vu.getView());
            }
        }
    }

    private static void loadProperties() {
        noApostropheAfterLetter = jEdit.getBooleanProperty("firstmate.no-apostrophe-after-letter", true);
        undoOnBackspace = jEdit.getBooleanProperty("firstmate.undo-on-backspace", true);
        wrapSelections = jEdit.getBooleanProperty("firstmate.wrap-selections", true);
    }

    private static void initView(View view) {
        InputHandler defaultHandler = view.getInputHandler();
        if (defaultHandler instanceof DefaultInputHandler) {
            FirstMateInputHandler handler = new FirstMateInputHandler(view, (DefaultInputHandler) defaultHandler);
            view.setInputHandler(handler);
        } else {
            Log.log(Log.ERROR, FirstMatePlugin.class, "Current InputHandler not instance of DefaultInputHandler:" + defaultHandler);
        }
    }

    private static void unInitView(View view) {
        InputHandler handler = view.getInputHandler();
        if (handler instanceof FirstMateInputHandler) {
            view.setInputHandler(((FirstMateInputHandler) handler).getDefaultHandler());
        } else {
            Log.log(Log.DEBUG, FirstMatePlugin.class, "handler is not FirstMateInputHandler: " + handler);
        }
    }

    public static boolean isEnabled() {
        return enabled;
    }

    public static void setEnabled(boolean enable) {
        if (enabled != enable) {
            View[] views = jEdit.getViews();
            for (int i = 0; i < views.length; i++) {
                if (enable) initView(views[i]); else unInitView(views[i]);
            }
            enabled = enable;
        }
    }

    public static boolean getIgnoreNext() {
        return ignoreNext;
    }

    public static void setIgnoreNext(boolean ignore) {
        ignoreNext = ignore;
    }

    public static boolean getWrapSelections() {
        return wrapSelections;
    }

    public static boolean getNoApostropheAfterLetter() {
        return noApostropheAfterLetter;
    }

    public static boolean getUndoOnBackspace() {
        return undoOnBackspace;
    }

    private static boolean enabled = false;

    private static boolean ignoreNext = false;

    private static boolean noApostropheAfterLetter = true;

    private static boolean undoOnBackspace = true;

    private static boolean wrapSelections = true;
}
