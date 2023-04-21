package scripting;

import clojure.contrib.jsr223.ClojureScriptEngineFactory;
import scripting.ScriptEngineService;
import org.gjt.sp.jedit.EditPlugin;
import org.gjt.sp.jedit.Mode;
import org.gjt.sp.jedit.jEdit;

public class ClojureScriptEnginePlugin extends EditPlugin implements ScriptEngineService {

    private ClojureScriptEngineFactory factory = new ClojureScriptEngineFactory();

    public Class getEngineFactoryClass() {
        return factory.getClass();
    }

    public Mode getMode() {
        return jEdit.getMode("clojure");
    }

    public void start() {
    }

    public void stop() {
    }
}
