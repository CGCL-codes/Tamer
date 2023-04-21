package com.googlecode.intelliguard.util;

import com.intellij.facet.ui.ValidationResult;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.JarFileSystem;
import com.intellij.openapi.util.io.FileUtil;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;

/**
 * Created by IntelliJ IDEA.
 * User: Ronnie
 * Date: 2009-nov-09
 * Time: 22:11:49
 */
public class ObfuscatorUtils {

    public static ValidationResult checkYGuard(@NotNull final String jarPath) {
        final VirtualFile jarFile = findJarFile(jarPath);
        if (jarFile == null) {
            return new ValidationResult("File " + jarPath + " does not exist");
        }
        String yGuardClassName = "com.yworks.yguard.YGuardTask";
        final VirtualFile yGuardClassFile = jarFile.findFileByRelativePath(yGuardClassName.replace('.', '/') + ".class");
        if (yGuardClassFile == null) {
            return new ValidationResult(jarPath + " does not contain class " + yGuardClassName);
        }
        return ValidationResult.OK;
    }

    @Nullable
    private static VirtualFile findJarFile(@NotNull final String jarPath) {
        return JarFileSystem.getInstance().refreshAndFindFileByPath(FileUtil.toSystemIndependentName(jarPath) + JarFileSystem.JAR_SEPARATOR);
    }
}
