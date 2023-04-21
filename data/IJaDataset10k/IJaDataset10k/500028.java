package com.maddyhome.idea.copyright.util;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;

public class FileUtil {

    public static boolean areFiles(VirtualFile[] files) {
        if (files == null || files.length < 2) {
            return false;
        }
        for (VirtualFile file : files) {
            if (file.isDirectory()) {
                return false;
            }
        }
        return true;
    }

    public static PsiFile[] convertToPsiFiles(VirtualFile[] files, Project project) {
        PsiFile[] res = new PsiFile[files.length];
        PsiManager manager = PsiManager.getInstance(project);
        for (int i = 0; i < files.length; i++) {
            res[i] = manager.findFile(files[i]);
        }
        return res;
    }

    private FileUtil() {
    }
}
