package us.wthr.jdem846.scripting.groovy;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import org.codehaus.groovy.control.CompilationFailedException;
import us.wthr.jdem846.exception.ScriptCompilationFailedException;
import us.wthr.jdem846.exception.ScriptingException;
import us.wthr.jdem846.logging.Log;
import us.wthr.jdem846.logging.Logging;
import us.wthr.jdem846.scripting.ScriptProxy;

public class GroovyScriptLoader {

    private static Log log = Logging.getLog(GroovyScriptLoader.class);

    public static ScriptProxy parseScript(String scriptContent) throws ScriptingException, ScriptCompilationFailedException {
        log.info("Compiling Groovy script...");
        ClassLoader parent = GroovyScriptLoader.class.getClassLoader();
        GroovyClassLoader loader = new GroovyClassLoader(parent);
        Class<?> clazz = null;
        GroovyObject groovyObject = null;
        try {
            clazz = loader.parseClass(scriptContent);
        } catch (CompilationFailedException ex) {
            throw new ScriptCompilationFailedException(ex.getUnit(), "Failed to compile Groovy script: " + ex.getMessage(), ex);
        }
        try {
            groovyObject = (GroovyObject) clazz.newInstance();
        } catch (InstantiationException ex) {
            throw new ScriptingException("Failed to instantiate Groovy class: " + ex.getMessage(), ex);
        } catch (IllegalAccessException ex) {
            throw new ScriptingException("Failed to access Groovy class: " + ex.getMessage(), ex);
        }
        GroovyScriptProxy scriptProxy = new GroovyScriptProxy(groovyObject);
        return scriptProxy;
    }
}
