package org.sqlexp.util.script;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import java.io.File;
import org.codehaus.groovy.control.CompilationFailedException;

/**
 * Simple Groovy script executor.
 * @author Matthieu Réjou
 */
@Deprecated
public final class ScriptExecutor {

    private static final String ARGUMENT_NAME = "arg";

    private static ScriptExecutor instance;

    /**
	 * Gets script executor unique instance.
	 * @return the instance
	 */
    public static ScriptExecutor getInstance() {
        if (instance == null) {
            instance = new ScriptExecutor();
        }
        return instance;
    }

    /**
	 * Executes the given script.
	 * @param scriptFile to execute
	 * @param args script arguments
	 * @return script return value
	 * @throws CompilationFailedException if script is not valid
	 * @throws Exception if script execution thrown an exception
	 */
    public Object execute(final File scriptFile, final Object... args) throws CompilationFailedException, Exception {
        GroovyShell shell = new GroovyShell();
        return execute(shell.parse(scriptFile), args);
    }

    /**
	 * Executes the given script.
	 * @param scriptText to execute
	 * @param args script arguments
	 * @return script return value
	 * @throws CompilationFailedException if script is not valid
	 * @throws Exception if script execution thrown an exception
	 */
    public Object execute(final String scriptText, final Object... args) throws CompilationFailedException, Exception {
        GroovyShell shell = new GroovyShell();
        return execute(shell.parse(scriptText), args);
    }

    /**
	 * Executes the given script.
	 * @param script to execute
	 * @param args script arguments
	 * @return script return value
	 * @throws CompilationFailedException if script is not valid
	 * @throws Exception if script execution thrown an exception
	 */
    public Object execute(final Script script, final Object... args) throws CompilationFailedException, Exception {
        if (args != null && args.length > 0) {
            Binding binding = new Binding();
            int i = 0;
            for (Object arg : args) {
                binding.setVariable(ARGUMENT_NAME + i, arg);
            }
            script.setBinding(binding);
        }
        return script.run();
    }
}
