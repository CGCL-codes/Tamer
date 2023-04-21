package com.thoughtworks.fireworks.adapters.psi;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiMethod;
import com.thoughtworks.fireworks.adapters.ProjectAdapter;
import junit.framework.TestCase;

public class PsiUtils {

    public static PsiMethod findMethod(PsiClass aClass, String testMethodName) {
        if (testMethodName != null) {
            PsiMethod[] methods = aClass.getMethods();
            for (PsiMethod method : methods) {
                if (method.getName().equals(testMethodName)) {
                    return method;
                }
            }
        }
        return null;
    }

    public static PsiClass getTestCasePsiClass(ProjectAdapter project) throws ClassNotFoundException {
        String packageName = TestCase.class.getPackage().getName();
        return project.getPackage(packageName).getPsiClass(TestCase.class);
    }

    public static PsiClass getPsiClass(ProjectAdapter project, VirtualFile file) {
        PsiJavaFile psiJavaFile = getPsiJavaFile(project, file);
        if (psiJavaFile == null) {
            return null;
        }
        final PsiClass[] classes = psiJavaFile.getClasses();
        final String fileName = file.getNameWithoutExtension();
        for (final PsiClass aClass : classes) {
            if (fileName.equals(aClass.getName())) {
                return aClass;
            }
        }
        return null;
    }

    private static PsiJavaFile getPsiJavaFile(ProjectAdapter project, VirtualFile file) {
        PsiFile psiFile = project.getPsiManager().findFile(file);
        if (psiFile instanceof PsiJavaFile) {
            return (PsiJavaFile) psiFile;
        }
        return null;
    }
}
