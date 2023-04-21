package snipplr;

import org.gjt.sp.jedit.*;
import org.gjt.sp.jedit.textarea.TextArea;
import org.gjt.sp.util.Log;
import org.gjt.sp.util.WorkThreadPool;
import org.apache.xmlrpc.XmlRpcException;
import javax.swing.JOptionPane;

public class SnipplrPlugin extends EditPlugin {

    public static final String NAME = "Snipplr";

    public static final String OPTION_PREFIX = "options." + NAME + ".";

    private static WorkThreadPool workPool;

    public SnipplrPlugin() {
    }

    public void start() {
        try {
            LanguageMapper.buildLanguageCache();
        } catch (XmlRpcException e) {
            Log.log(Log.ERROR, this, e);
            GUIUtilities.error(null, "snipplr.request.error", new String[] { e.toString() });
        }
    }

    public void stop() {
    }

    public static void newSnippet(View view, TextArea textarea) {
        Snippet snippet = new Snippet();
        Language lang = LanguageMapper.languageSearch(textarea.getBuffer().getMode().getName());
        snippet.setLanguage(lang);
        snippet.setSource(textarea.getSelectedText());
        new SnippetForm(view, snippet);
    }

    public static void addWorkRequest(Runnable run, boolean inAWT) {
        if (workPool == null) {
            workPool = new WorkThreadPool("Snipplr", 1);
            workPool.start();
        }
        workPool.addWorkRequest(run, inAWT);
    }
}
