package org.red5.server.script.groovy;

import groovy.lang.GroovyClassLoader;
import java.io.IOException;
import org.codehaus.groovy.control.CompilationFailedException;
import org.springframework.scripting.ScriptCompilationException;
import org.springframework.scripting.ScriptFactory;
import org.springframework.scripting.ScriptSource;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/**
 * {@link org.springframework.scripting.ScriptFactory} implementation
 * for a Groovy script.
 * 
 * <p>Typically used in combination with a
 * {@link org.springframework.scripting.support.ScriptFactoryPostProcessor};
 * see the latter's
 * {@link org.springframework.scripting.support.ScriptFactoryPostProcessor Javadoc}
 * for a configuration example.
 * 
 * @author Juergen Hoeller
 * @author Rob Harrop
 * @since 2.0
 * @see org.springframework.scripting.support.ScriptFactoryPostProcessor
 * @see groovy.lang.GroovyClassLoader
 */
public class GroovyScriptFactory implements ScriptFactory {

    private final String scriptSourceLocator;

    /**
	 * Create a new GroovyScriptFactory for the given script source.
	 * <p>We don't need to specify script interfaces here, since
	 * a Groovy script defines its Java interfaces itself.
	 * @param scriptSourceLocator a locator that points to the source of the script.
	 * Interpreted by the post-processor that actually creates the script.
	 * @throws IllegalArgumentException if the supplied String is empty
	 */
    public GroovyScriptFactory(String scriptSourceLocator) {
        Assert.hasText(scriptSourceLocator);
        this.scriptSourceLocator = scriptSourceLocator;
    }

    public String getScriptSourceLocator() {
        return this.scriptSourceLocator;
    }

    /**
	 * Groovy scripts determine their interfaces themselves,
	 * hence we don't need to explicitly expose interfaces here.
	 * @return <code>null</code> always
	 */
    public Class[] getScriptInterfaces() {
        return null;
    }

    /**
	 * Groovy scripts do not need a config interface,
	 * since they expose their setters as public methods.
	 * @return <code>false</code> always
	 */
    public boolean requiresConfigInterface() {
        return false;
    }

    /**
	 * Loads and parses the Groovy script via the GroovyClassLoader.
	 * @see groovy.lang.GroovyClassLoader
	 */
    public Object getScriptedObject(ScriptSource actualScriptSource, Class[] actualInterfaces) throws IOException, ScriptCompilationException {
        ClassLoader cl = ClassUtils.getDefaultClassLoader();
        GroovyClassLoader groovyClassLoader = new GroovyClassLoader(cl);
        try {
            Class clazz = groovyClassLoader.parseClass(actualScriptSource.getScriptAsString());
            return clazz.newInstance();
        } catch (CompilationFailedException ex) {
            throw new ScriptCompilationException("Could not compile Groovy script: " + actualScriptSource, ex);
        } catch (InstantiationException ex) {
            throw new ScriptCompilationException("Could not instantiate Groovy script class: " + actualScriptSource, ex);
        } catch (IllegalAccessException ex) {
            throw new ScriptCompilationException("Could not access Groovy script constructor: " + actualScriptSource, ex);
        }
    }
}
