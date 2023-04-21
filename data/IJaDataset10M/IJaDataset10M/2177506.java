package org.jmlspecs.eclipse.jdt.esc;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.internal.compiler.Compiler;
import org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration;
import org.jmlspecs.eclipse.jdt.core.EscJava2BatchWrapper;
import org.jmlspecs.eclipse.jdt.core.Log;

public class EscJava2Wrapper extends EscJava2BatchWrapper {

    public static boolean manualRun = false;

    public static IJavaProject currentProject;

    public EscJava2Wrapper() {
    }

    public void preCodeGeneration(Compiler compiler, CompilationUnitDeclaration unit) {
        if (manualRun || (compiler.options.jmlEnabled && compiler.options.jmlEsc2Enabled)) {
            comp(compiler, unit);
        }
    }

    protected String translatePath(String fileName) {
        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        if (workspace != null) {
            IFile f = workspace.getRoot().getFile(new Path(fileName));
            fileName = f.getRawLocation().toString();
        }
        return fileName;
    }

    public String[] setupArgs(Compiler compiler, String fileName) {
        String classPath = getClassPath(compiler);
        String s = compiler.options.jmlSimplifyPath;
        if (s != null && s.length() != 0) {
            System.setProperty("simplify", s);
        }
        Log.log("UI SPECS = " + compiler.options.jmlSpecPath);
        String path = compiler.options.jmlSpecPath;
        path = path.replaceAll("\\$\\{workspace\\}", ResourcesPlugin.getWorkspace().getRoot().getLocation().toString());
        if (currentProject != null) path = path.replaceAll("\\$\\{project\\}", currentProject.getProject().getLocation().toString());
        Log.log("UI SPECS = " + path);
        String[] a = { "-specs", path, "-nowarn", "Deadlock", "-classpath", classPath, fileName };
        if (Log.on) Log.log("FILE: " + fileName);
        return a;
    }
}
