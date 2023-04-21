package org.pustefixframework.maven.plugins.iwrapperbean;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author mleidig@schlund.de
 *
 */
public class IWrapperFileScanner {

    private long fileReadCount;

    private long fileReadTime;

    public IWrapperFileScanner() {
    }

    public void reset() {
        fileReadCount = 0;
        fileReadTime = 0;
    }

    public List<File> getChangedFiles(File srcDir, File destDir, long lastScanTime) {
        List<File> changedFiles = new ArrayList<File>();
        getChangedFiles(srcDir, destDir, changedFiles, lastScanTime);
        return changedFiles;
    }

    private void getChangedFiles(File srcDir, File destDir, List<File> changedFiles, long lastScanTime) {
        File[] srcFiles = srcDir.listFiles();
        for (File srcFile : srcFiles) {
            if (srcFile.isDirectory()) {
                if (!(srcFile.getName().equals(".svn") || srcFile.getName().equals("CVS"))) {
                    File destFile = new File(destDir, srcFile.getName());
                    getChangedFiles(srcFile, destFile, changedFiles, lastScanTime);
                }
            } else {
                if (srcFile.getName().endsWith(".java")) {
                    String name = srcFile.getName().substring(0, srcFile.getName().length() - 5) + ".class";
                    File destFile = new File(destDir, name);
                    if (!destFile.exists() || srcFile.lastModified() > destFile.lastModified() || srcFile.lastModified() > lastScanTime) {
                        boolean found = false;
                        long t1 = System.currentTimeMillis();
                        try {
                            FileInputStream fis = new FileInputStream(srcFile);
                            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
                            String line = null;
                            int cnt = 0;
                            while ((line = reader.readLine()) != null) {
                                if (line.contains("IWrapper")) {
                                    if (line.contains("@IWrapper") || line.contains("@de.schlund.pfixcore.generator.annotation.IWrapper")) {
                                        found = true;
                                        break;
                                    }
                                }
                                cnt++;
                                if (cnt > 70) break;
                                if (line.contains("public class")) break;
                            }
                            fis.close();
                        } catch (IOException x) {
                            x.printStackTrace();
                        }
                        long t2 = System.currentTimeMillis();
                        fileReadCount++;
                        fileReadTime += (t2 - t1);
                        if (found) changedFiles.add(srcFile);
                    }
                }
            }
        }
    }

    public long getScanCount() {
        return fileReadCount;
    }

    public String printStatistics() {
        return "Scanned " + fileReadCount + " file(s) in " + fileReadTime + "ms";
    }
}
