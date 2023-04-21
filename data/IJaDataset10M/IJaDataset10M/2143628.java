package org.spbu.plweb.diagram.util;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import org.spbu.plweb.Page;
import org.spbu.plweb.diagram.util.projects.ProjectOperationException;

public class ProjectGenerator {

    private String pathWr = null;

    private String pathWrNew = null;

    private List<String> pages = null;

    public ProjectGenerator(String pathWr, String pathWrNew) {
        this.pathWr = pathWr;
        this.pathWrNew = pathWrNew;
    }

    public void setPagesToCopy(List<Page> list) {
        this.pages = new LinkedList<String>();
        for (Page page : list) {
            pages.add(page.getSource());
        }
    }

    public void generate() throws ProjectOperationException {
        if (pages == null) {
            throw new ProjectOperationException("No pages list is set");
        }
        File projectWrNew = new File(pathWrNew);
        if (projectWrNew.exists()) {
            projectWrNew.delete();
        }
        File projectWr = new File(pathWr);
        copyDirs(projectWr, projectWrNew);
    }

    private void copyDirs(File sourceDir, File targetDir) {
        if (sourceDir == null || targetDir == null) {
            return;
        }
        String[] children = sourceDir.list();
        if (children == null) {
            return;
        }
        targetDir.mkdir();
        for (String child : children) {
            File childFile = new File(sourceDir + "/" + child);
            File targetChildFile = new File(targetDir + "/" + child);
            if (child.equals("plweb") || child.equals(".Temp~")) {
                continue;
            }
            if (child.startsWith("page") && !pages.contains(child)) {
                continue;
            }
            if (childFile.isDirectory()) {
                copyDirs(childFile, targetChildFile);
            } else {
                copyFiles(childFile, targetChildFile);
            }
        }
    }

    private void copyFiles(File source, File target) {
        try {
            FileReader in = new FileReader(source);
            FileWriter out = new FileWriter(target);
            int c;
            while ((c = in.read()) != -1) {
                out.write(c);
            }
            in.close();
            out.close();
        } catch (IOException e) {
        }
    }
}
