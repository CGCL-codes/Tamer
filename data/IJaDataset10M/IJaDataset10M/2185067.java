package de.iritgo.aktera.script;

import org.python.util.PythonInterpreter;
import de.iritgo.simplelife.string.StringTools;

public class PythonScriptCompiler implements ScriptCompiler {

    public static class CompiledPythonScript extends CompiledScript {

        private String scriptName;

        private String script;

        public CompiledPythonScript(String scriptName, String script) {
            this.scriptName = scriptName;
            this.script = script;
        }

        @Override
        public Object execute(String methodName, Object... args) throws ScriptMethodNotFoundException, ScriptExecutionException {
            PythonInterpreter interpreter = new PythonInterpreter();
            interpreter.eval(script);
            interpreter.eval(methodName + "ReturnValue = " + methodName + "(" + StringTools.concatWithDelimiter(args, ",") + ")");
            return interpreter.get(methodName + "ReturnValue");
        }
    }

    public CompiledScript compile(String scriptName, String scriptCode) throws ScriptCompilerException {
        return new CompiledPythonScript(scriptName, scriptCode);
    }

    public void check(String scriptCode) throws ScriptCompilerException {
        PythonInterpreter interpreter = new PythonInterpreter();
        interpreter.eval(scriptCode);
    }
}
