package org.csiro.darjeeling.infuser.ant;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.Code;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;

public class CodeSizeTask extends Task {

    private ArrayList<FileSet> fileSets;

    private int totalFileSize, totalCodeSize = 0;

    private String name;

    public CodeSizeTask() {
        fileSets = new ArrayList<FileSet>();
    }

    private int getFileSize(String fileName) throws IOException {
        File file = new File(fileName);
        return (int) file.length();
    }

    private int getCodeSize(String fileName) throws IOException {
        int ret = 0;
        ClassParser parser = new ClassParser(fileName);
        JavaClass javaClass = parser.parse();
        for (Method method : javaClass.getMethods()) {
            Code code = method.getCode();
            if (code != null) ret += code.getCode().length;
        }
        return ret;
    }

    public void execute() {
        for (FileSet fileSet : fileSets) {
            String files[] = fileSet.getDirectoryScanner(getProject()).getIncludedFiles();
            for (String file : files) {
                try {
                    String fileName = fileSet.getDir() + "/" + file;
                    totalFileSize += getFileSize(fileName);
                    totalCodeSize += getCodeSize(fileName);
                } catch (IOException ex) {
                    System.out.println("Warning: io exception loading " + file + ": " + ex.getMessage());
                }
            }
        }
        System.out.println("name: " + name);
        System.out.println("total file size : " + totalFileSize);
        System.out.println("total code size : " + totalCodeSize);
    }

    public void addFileset(FileSet fileset) {
        fileSets.add(fileset);
    }

    public void setName(String name) {
        this.name = name;
    }
}
