package org.codehaus.mojo.gwt;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.LinkedList;
import java.util.List;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * Goal which compiles a GWT file.
 *
 * @goal newcompile
 * @phase compile
 * @author Shinobu Kawai
 */
public class NewCompileMojo extends AbstractMojo {

    /**
     * Location of the source files.
     *
     * @parameter expression="${basedir}/src/main/java"
     * @required
     */
    private File sourceDirectory;

    /**
     * Location of the file.
     *
     * @parameter expression="${project.build.directory}/gwt/www"
     * @required
     */
    private File outputDirectory;

    /**
     * The java class to compile.
     *
     * @parameter
     * @required
     */
    private String className;

    public void execute() throws MojoExecutionException {
        if (getLog().isDebugEnabled()) {
            getLog().debug("CompileMojo#execute()");
        }
        ClassLoader loader = getClassLoader();
        final String GWTCOMPILER_CLASS_NAME = "com.google.gwt.dev.GWTCompiler";
        Class compiler = null;
        try {
            compiler = loader.loadClass(GWTCOMPILER_CLASS_NAME);
        } catch (ClassNotFoundException e) {
            throw new MojoExecutionException("Could not find GWTCompiler.", e);
        }
        if (getLog().isDebugEnabled()) {
            getLog().debug("  Found class:" + compiler);
        }
        final Method main;
        try {
            main = compiler.getMethod("main", new Class[] { String[].class });
        } catch (SecurityException e) {
            throw new MojoExecutionException("Permission not granted for reflection.", e);
        } catch (NoSuchMethodException e) {
            throw new MojoExecutionException("Could not find GWTCompiler#main(String[]).", e);
        }
        if (getLog().isDebugEnabled()) {
            getLog().debug("  Found method:" + main);
        }
        final List args = new LinkedList();
        args.add("-out");
        args.add(outputDirectory.getAbsolutePath());
        args.add(className);
        if (getLog().isDebugEnabled()) {
            getLog().debug("  Invoking main with" + args);
        }
        Runnable compile = new Runnable() {

            public void run() {
                try {
                    main.invoke(null, new Object[] { args.toArray(new String[args.size()]) });
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException("This shouldn't happen.", e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Permission not granted for reflection.", e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException("GWTCompiler#main(String[]) failed.", e);
                }
            }
        };
        Thread compileThread = new Thread(compile);
        compileThread.setContextClassLoader(loader);
        compileThread.start();
        try {
            compileThread.join();
        } catch (InterruptedException e) {
            throw new MojoExecutionException("Compiler thread stopped.", e);
        }
    }

    private ClassLoader getClassLoader() throws MojoExecutionException {
        if (getLog().isDebugEnabled()) {
            getLog().debug("CompileMojo#getClassLoader()");
        }
        URLClassLoader myClassLoader = (URLClassLoader) getClass().getClassLoader();
        URL[] originalUrls = myClassLoader.getURLs();
        URL[] urls = new URL[originalUrls.length + 1];
        for (int index = 0; index < originalUrls.length; ++index) {
            try {
                String url = originalUrls[index].toExternalForm();
                urls[index] = new File(url.substring("file:".length())).toURI().toURL();
            } catch (MalformedURLException e) {
                throw new MojoExecutionException("Failed to convert original classpath to URL.", e);
            }
        }
        try {
            urls[originalUrls.length] = sourceDirectory.toURL();
        } catch (MalformedURLException e) {
            throw new MojoExecutionException("Failed to convert source root to URL.", e);
        }
        if (getLog().isDebugEnabled()) {
            for (int i = 0; i < urls.length; i++) {
                getLog().debug("  URL:" + urls[i]);
            }
        }
        return new URLClassLoader(urls, myClassLoader.getParent());
    }
}
