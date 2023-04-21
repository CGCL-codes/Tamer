package com.turtle3d;

import java.io.File;
import java.io.FileFilter;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.media.j3d.Shape3D;
import com.turtle3d.formallanguage.grammar.Grammar;
import com.turtle3d.util.TaskTimer;
import com.turtle3d.visualizator.Visualizator;

/**
 * Turtle3D uses different resources - java code grammars and visualisators and .obj files.
 * This class simplifies resource access.
 * 
 * @author Marek Paterczyk <marek.paterczyk@gmail.com>
 *
 */
public abstract class ResourceManager {

    public static final String grammarsPackage = "com.turtle3d.user.grammars";

    public static final String visualizatorsPackage = "com.turtle3d.user.visualizators";

    public static final String pluginsDir = "plugins/";

    private static ArrayList<String> listClassFileNames(File dir) {
        ArrayList<String> fileNames = new ArrayList<String>();
        try {
            for (File f : dir.listFiles(new ClassFileFilter())) {
                fileNames.add(getClassSimpleName(f));
            }
            return fileNames;
        } catch (RuntimeException e) {
            App.printlnError("Error getting grammar lists. Check console.");
            e.printStackTrace();
            return fileNames;
        }
    }

    /**
	 * 
	 * @return List of avaible grammars
	 */
    public static ArrayList<String> getGrammarsList() {
        return listClassFileNames(new File(pluginsDir + grammarsPackage.replaceAll("\\.", "/")));
    }

    /**
	 * 
	 * @return List of avaible visualizators
	 */
    public static ArrayList<String> getVisualizatorsList() {
        return listClassFileNames(new File(pluginsDir + visualizatorsPackage.replaceAll("\\.", "/")));
    }

    /**
	 * Comiles if necessery and loads grammar object. Still requires initialization.
	 * @param name Grammar class name
	 * @return uninitialized Grammar object
	 */
    public static Grammar loadGrammar(String name) {
        if (needsToBeCompiled(grammarsPackage + "." + name)) compile(grammarsPackage + "." + name);
        try {
            File classesDir = new File(pluginsDir);
            URLClassLoader loader1 = new URLClassLoader(new URL[] { classesDir.toURL() }, Grammar.class.getClassLoader());
            Class cls1 = loader1.loadClass(grammarsPackage + "." + name);
            Grammar grammar = (Grammar) cls1.newInstance();
            return grammar;
        } catch (Exception e) {
            App.printlnError("\"" + name + "\" no such grammar!");
            return null;
        }
    }

    /**
	 * Compiles if necessery and loads visualizator object.
	 * @param name Visualizator name
	 * @return Visualizator object
	 */
    public static Visualizator initVisualizator(String name) {
        if (needsToBeCompiled(visualizatorsPackage + "." + name)) compile(visualizatorsPackage + "." + name);
        try {
            File classesDir = new File(pluginsDir);
            URLClassLoader loader1 = new URLClassLoader(new URL[] { classesDir.toURL() }, Grammar.class.getClassLoader());
            Class cls1 = loader1.loadClass(visualizatorsPackage + "." + name);
            Visualizator visualizator = (Visualizator) cls1.newInstance();
            return visualizator;
        } catch (Exception e) {
            App.printlnError("\"" + name + "\" no such visualizator!");
            return null;
        }
    }

    public static Shape3D getShape3D(String path) {
        return Shape3DCacheLoader.getShape3DCacheLoader().createShape3D(path);
    }

    public static void compile(String javaClassName) {
        try {
            TaskTimer.getTaskTimer().taskStart(javaClassName + "Compilation");
            String fullJavaClassName = javaClassName.replaceAll("\\.", "/") + ".java";
            int result = com.sun.tools.javac.Main.compile(new String[] { "-classpath", "turtle3d.jar" + getClasspathsDelimiter() + "build", "-d", pluginsDir, pluginsDir + fullJavaClassName });
            if (result != 0) App.printlnWarning("Could not compile " + javaClassName);
            TaskTimer.getTaskTimer().taskComplete(javaClassName + "Compilation");
        } catch (NoClassDefFoundError e) {
            App.printlnWarning("Could not compile " + javaClassName);
            e.printStackTrace();
        } catch (Exception ex) {
            App.printlnWarning("Could not compile " + javaClassName);
            ex.printStackTrace();
        }
    }

    public static boolean needsToBeCompiled(String javaClassName) {
        try {
            File sourceFile = new File(pluginsDir + javaClassName.replaceAll("\\.", "/") + ".java");
            File classFile = new File(pluginsDir + javaClassName.replaceAll("\\.", "/") + ".class");
            return sourceFile.lastModified() > classFile.lastModified();
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    private static String getClasspathsDelimiter() {
        String os = System.getProperty("os.name");
        Pattern linux = Pattern.compile(".*(Linux).*", Pattern.CASE_INSENSITIVE);
        Matcher m = linux.matcher(os);
        if (m.matches()) return ":"; else return ";";
    }

    private static String getClassSimpleName(File file) {
        return file.getName().substring(0, file.getName().lastIndexOf('.'));
    }
}

final class ClassFileFilter implements FileFilter {

    public boolean accept(File pathname) {
        String name = pathname.getName();
        String ext = name.substring(name.lastIndexOf('.') + 1, name.length());
        return ext.equals("java");
    }
}
