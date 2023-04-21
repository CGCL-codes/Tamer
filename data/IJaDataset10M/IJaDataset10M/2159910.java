package org.intellij.plugins.junit4.listeners;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.refactoring.listeners.RefactoringElementListener;
import com.intellij.refactoring.listeners.RefactoringElementListenerProvider;
import org.apache.log4j.Logger;
import org.intellij.plugins.junit4.ClassNameResolver;
import org.intellij.plugins.junit4.JavaFileFactory;
import org.intellij.plugins.junit4.JavaFile;

/**
 * @author Justin Tomich
 */
public class RenameListenerProvider implements RefactoringElementListenerProvider {

    private final Logger log = Logger.getLogger("CONSOLE-WARN");

    private final JavaFileFactory fileFactory;

    private final ClassNameResolver resolver;

    public RenameListenerProvider(JavaFileFactory fileFactory, ClassNameResolver resolver) {
        this.fileFactory = fileFactory;
        this.resolver = resolver;
        log.warn("RefactoringElementListener constructed!");
    }

    public RefactoringElementListener getListener(PsiElement element) {
        log.warn("RenameListenerProvider.getListener() called for " + element);
        if (!(element instanceof PsiClass)) return null;
        final PsiClass psiCass = (PsiClass) element;
        if (isInnerClass(psiCass)) return null;
        JavaFile javaFile = fileFactory.create(psiCass);
        if (javaFile.isTest()) return null;
        Project project = element.getProject();
        RenameListener renameListener = new RenameListener(project, javaFile, resolver);
        log.warn("RenameListenerProvider.getListener() constructed:" + renameListener);
        return renameListener;
    }

    private boolean isInnerClass(PsiClass psiClass) {
        final String name = psiClass.getName();
        if (name == null) return false;
        final PsiFile file = psiClass.getContainingFile();
        final String fileName = file.getName();
        if (fileName == null) return false;
        return (fileName.indexOf(name) < 0);
    }
}
