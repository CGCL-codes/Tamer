package net.sourceforge.javautil.groovy.proxy;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.List;
import org.codehaus.groovy.control.CompilationFailedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import net.sourceforge.javautil.common.ReflectionUtil;
import net.sourceforge.javautil.common.exception.ThrowableManagerRegistry;
import net.sourceforge.javautil.common.io.IVirtualArtifact;
import net.sourceforge.javautil.common.io.VirtualArtifactWatcher;
import net.sourceforge.javautil.common.io.IVirtualArtifactWatcherListener;
import net.sourceforge.javautil.common.io.IVirtualFile;
import net.sourceforge.javautil.common.io.VirtualArtifactWatcher.VirtualArtifactChange;
import net.sourceforge.javautil.common.io.VirtualArtifactWatcher.VirtualArtifactWatchable;
import net.sourceforge.javautil.common.io.IVirtualArtifactWatcherListener.VirtualArtifactWatcherEvent;

/**
 * This will allow an underlying groovy source file to be reloaded on changes detected without the
 * need to change the proxy object that routes calls to the current version of the groovy object.
 * 
 * @author elponderador
 * @author $Author$
 * @version $Id$
 */
public class GroovyObjectHandlerReloadable extends GroovyObjectHandler implements IGroovyObjectWatchable {

    /**
	 * A proxy that will allow the underlying target source to be recompiled and used for posterior invocations.
	 * 
	 * @param gcl The class loader to use for groovy compilation
	 * @param source The source of the groovy script
	 * @return A watchable resource
	 */
    public static IGroovyObjectWatchable create(GroovyClassLoader gcl, IVirtualFile source) {
        return (IGroovyObjectWatchable) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[] { IGroovyObjectWatchable.class }, new GroovyObjectHandlerReloadable(gcl, source));
    }

    protected Logger log = LoggerFactory.getLogger(GroovyObjectHandlerReloadable.class);

    protected final GroovyClassLoader gcl;

    protected final IVirtualFile source;

    protected GroovyObject original;

    public GroovyObjectHandlerReloadable(GroovyClassLoader gcl, IVirtualFile source) {
        this.gcl = gcl;
        this.source = source;
        this.compile();
    }

    public Object getProperty(String propertyName) {
        if (original != null) return original.getProperty(propertyName); else {
            log.warn("No valid compiled source available: " + source);
            return null;
        }
    }

    public void setProperty(String propertyName, Object newValue) {
        if (original != null) original.setProperty(propertyName, newValue); else log.warn("No valid compiled source available: " + source);
    }

    public Object invokeMethod(String name, Object args) {
        if (original != null) return original.invokeMethod(name, args); else {
            log.warn("No valid compiled source available: " + source);
            return null;
        }
    }

    public void watch(VirtualArtifactWatcher watcher) {
        watcher.watch(source);
        watcher.addListener(new ReloaderWatcher(), source.getPath());
    }

    /**
	 * Compile the source and store a new instance of it
	 */
    protected void compile() {
        try {
            log.info("Compiling: " + source);
            Class<? extends GroovyObject> sc = gcl.parseClass(source.getInputStream());
            this.original = ReflectionUtil.newInstance(sc, new Class[0]);
        } catch (CompilationFailedException e) {
            log.warn("Could not compile: " + source, e);
        } catch (IOException e) {
            log.warn("Error accessing source: " + source, e);
        }
    }

    /**
	 * The instance that will be notified in order to reload the groovy source.
	 * 
	 * @author elponderador
	 * @author $Author$
	 * @version $Id$
	 */
    public class ReloaderWatcher implements IVirtualArtifactWatcherListener {

        public void handle(VirtualArtifactWatcherEvent evt) {
            compile();
        }
    }
}
