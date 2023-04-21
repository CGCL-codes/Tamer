package org.columba.core.scripting.interpreter;

import java.util.Map;
import org.columba.core.scripting.ScriptLogger;
import org.columba.core.scripting.model.ColumbaScript;

/**
    @author Celso Pinto (cpinto@yimports.com)
 */
public abstract class ScriptInterpreter {

    public static final String SCRIPT_PATH = "scriptPath", SCRIPT_OBJ = "scriptObj";

    protected ScriptLogger logger = ScriptLogger.getInstance();

    public abstract String getName();

    public abstract String[] getSupportedExtensions();

    public abstract void execute(ColumbaScript script, Map vars);
}
