package com.casheen.infrastructure.util;

import java.io.File;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Copy;

public class FileUtils {

    public static void copyToFile(String srcFile, String destFile) {
        Project project = new Project();
        Copy copy = new Copy();
        copy.setProject(project);
        copy.setFile(new File(srcFile));
        copy.setTofile(new File(destFile));
        copy.execute();
    }

    public static void copyToDir(String srcFile, String destDir) {
        Project project = new Project();
        Copy copy = new Copy();
        copy.setProject(project);
        copy.setFile(new File(srcFile));
        copy.setTodir(new File(destDir));
        copy.execute();
    }

    public static void main(String[] args) {
        copyToFile("c:/aaa.txt", "d:\\b.txt");
        copyToDir("c:/aaa.txt", "d:\\dir");
    }
}
