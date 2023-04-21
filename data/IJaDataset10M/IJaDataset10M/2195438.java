package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.osgi.framework.Bundle;
import view.Activator;
import view.views.DbImportWizardPageTwo;

public class MainExtractFileThread extends Thread {

    String firstFileToExtract;

    String secondFileToextract;

    String mainPath;

    URL fileUrl = null;

    private boolean status = true;

    public MainExtractFileThread(String firstFileToExtract) {
        this.firstFileToExtract = firstFileToExtract;
        Bundle bundle = Activator.getDefault().getBundle();
        Path path = new Path("util/7za.exe");
        URL url = FileLocator.find(bundle, path, Collections.EMPTY_MAP);
        try {
            fileUrl = FileLocator.toFileURL(url);
        } catch (IOException e) {
            status = false;
        }
    }

    public void run() {
        mainPath = firstFileToExtract.substring(0, firstFileToExtract.lastIndexOf("\\"));
        File extrationDir = new File(mainPath);
        if (extrationDir.isDirectory()) {
            File[] filesInDir = extrationDir.listFiles();
            for (int i = 0; i < filesInDir.length; i++) {
                if (filesInDir[i].isFile() && filesInDir[i].equals(new File(firstFileToExtract)) == false && filesInDir[i].getName().indexOf(firstFileToExtract.substring(firstFileToExtract.lastIndexOf("\\") + 1, firstFileToExtract.lastIndexOf(".") - 1)) != -1) {
                    filesInDir[i].delete();
                }
            }
        }
        ExtractFileThread myExtractFile1 = new ExtractFileThread(firstFileToExtract, mainPath, false, null);
        myExtractFile1.start();
        long origFileSize = new File(firstFileToExtract).length();
        DbImportWizardPageTwo.updateProgress("Starting to extract first file", 1);
        int i = 0;
        while (myExtractFile1.isAlive()) {
            if (origFileSize > 200000000L) {
                if (i % 10 == 0 && i < 340) DbImportWizardPageTwo.updateProgress("", (i / 10) + 1);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
            } else {
                if (i < 34) DbImportWizardPageTwo.updateProgress("", i + 1);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
            }
            i++;
        }
        DbImportWizardPageTwo.updateProgress("Finished extracting first file", 34);
        ExtractFileThread myExtractFile2 = new ExtractFileThread(firstFileToExtract.substring(0, firstFileToExtract.lastIndexOf(".")), mainPath + "\\", true, "allDisks.txt");
        myExtractFile2.start();
        DbImportWizardPageTwo.updateProgress("Starting to extract internal disks folders", 1);
        File resultFile = new File(mainPath + "\\allDisks.txt");
        if (resultFile.exists()) resultFile.delete();
        while (myExtractFile2.isAlive()) {
            long txtFileSize = resultFile.length();
            if (origFileSize > 200000000L) {
                if ((double) txtFileSize / 3000000000L < 1.0) DbImportWizardPageTwo.updateProgress("", ((int) (((double) txtFileSize / 3000000000L) * 33) + 1)); else DbImportWizardPageTwo.updateProgress("", 34);
                ;
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                }
            } else {
                if ((double) txtFileSize / 58000000L < 1.0) DbImportWizardPageTwo.updateProgress("", ((int) (((double) txtFileSize / 58000000L) * 33) + 1)); else DbImportWizardPageTwo.updateProgress("", 34);
                ;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
            }
        }
        DbImportWizardPageTwo.updateProgress("Finished extracting internal disks folders", 34);
    }

    public synchronized boolean getStatus() {
        return status;
    }
}
