package org.codehaus.jam.internal.javadoc;

import com.sun.javadoc.Doclet;
import com.sun.javadoc.RootDoc;
import org.codehaus.jam.provider.JamLogger;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>This class does its best to encapsulate and make threadsafe the
 * nastiness that is the javadoc 'invocation' API.</p>
 *
 * @author Patrick Calahan &lt;email: pcal-at-bea-dot-com&gt;
 */
public class JavadocRunner extends Doclet {

    private static final String JAVADOC_RUNNER_150 = "org.codehaus.jam.internal.javadoc.JavadocRunner_150";

    public static JavadocRunner newInstance() {
        try {
            Class.forName("com.sun.javadoc.AnnotationDesc");
        } catch (ClassNotFoundException e) {
            return new JavadocRunner();
        }
        try {
            Class onefive = Class.forName(JAVADOC_RUNNER_150);
            return (JavadocRunner) onefive.newInstance();
        } catch (ClassNotFoundException cnfe) {
        } catch (IllegalAccessException e) {
        } catch (InstantiationException e) {
        }
        return new JavadocRunner();
    }

    public JavadocRunner() {
    }

    synchronized RootDoc run(File[] files, PrintWriter out, String sourcePath, String classPath, String[] javadocArgs, JamLogger logger) throws IOException, FileNotFoundException {
        if (files == null || files.length == 0) {
            throw new FileNotFoundException("No input files found.");
        }
        List argList = new ArrayList();
        if (javadocArgs != null) {
            argList.addAll(Arrays.asList(javadocArgs));
        }
        argList.add("-private");
        if (sourcePath != null) {
            argList.add("-sourcepath");
            argList.add(sourcePath);
        }
        if (classPath != null) {
            argList.add("-classpath");
            argList.add(classPath);
            argList.add("-docletpath");
            argList.add(classPath);
        }
        for (int i = 0; i < files.length; i++) {
            argList.add(files[i].toString());
            if (out != null) out.println(files[i].toString());
        }
        String[] args = new String[argList.size()];
        argList.toArray(args);
        PrintWriter spewWriter;
        StringWriter spew = null;
        if (out == null) {
            spewWriter = new PrintWriter(spew = new StringWriter());
        } else {
            spewWriter = out;
        }
        ClassLoader originalCCL = Thread.currentThread().getContextClassLoader();
        try {
            JavadocResults.prepare();
            if (logger.isVerbose(this)) {
                logger.verbose("Invoking javadoc.  Command line equivalent is: ");
                StringWriter sw = new StringWriter();
                sw.write("javadoc ");
                for (int i = 0; i < args.length; i++) {
                    sw.write("'");
                    sw.write(args[i]);
                    sw.write("' ");
                }
                logger.verbose("  " + sw.toString());
            }
            int result = com.sun.tools.javadoc.Main.execute("JAM", spewWriter, spewWriter, spewWriter, this.getClass().getName(), args);
            RootDoc root = JavadocResults.getRoot();
            if (result != 0 || root == null) {
                spewWriter.flush();
                if (JavadocClassloadingException.isClassloadingProblemPresent()) {
                    throw new JavadocClassloadingException();
                }
                throw new RuntimeException("Unknown javadoc problem: result=" + result + ", root=" + root + ":\n" + ((spew == null) ? "" : spew.toString()));
            }
            return root;
        } catch (RuntimeException e) {
            throw e;
        } finally {
            Thread.currentThread().setContextClassLoader(originalCCL);
        }
    }

    public static boolean start(RootDoc root) {
        JavadocResults.setRoot(root);
        return true;
    }
}
