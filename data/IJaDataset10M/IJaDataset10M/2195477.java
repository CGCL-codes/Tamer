package org.slizardo.madcommander.dialogs.progressive;

import java.io.File;
import java.util.ArrayList;
import org.apache.commons.io.FileUtils;
import org.slizardo.madcommander.util.gui.DialogFactory;

public class CopyProgressDialog extends AbstractProgressDialog {

    public void run() {
        if (srcPath.equals(dstPath)) {
            DialogFactory.showErrorMessage(src.getParent(), "You cannot copy a file to itself!");
            dispose();
            return;
        }
        ArrayList<File> selectedFiles = src.getSelectedFiles();
        int numFiles = selectedFiles.size();
        myProcess.totalProgress = 0;
        for (int i = 0; i < selectedFiles.size(); i++) {
            myProcess.currentFile = selectedFiles.get(i).getName();
            myProcess.currentProgress = 0;
            currentFileLabel.setText("Current file: " + myProcess.currentFile);
            String fullSrc = srcPath + File.separator + myProcess.currentFile;
            String fullDst = dstPath + File.separator + myProcess.currentFile;
            StringBuffer buffer = new StringBuffer();
            buffer.append("Copying [ ");
            buffer.append(fullSrc);
            buffer.append(" => ");
            buffer.append(fullDst);
            buffer.append(" ]");
            logger.info(buffer.toString());
            try {
                File fileSrc = new File(fullSrc);
                File fileDst = new File(fullDst);
                if (fileSrc.isDirectory()) FileUtils.copyDirectory(fileSrc, fileDst); else FileUtils.copyFile(fileSrc, fileDst);
                myProcess.currentProgress = 100;
                myProcess.totalProgress = (i * 100) / numFiles;
                if (myProcess.cancel) {
                    logger.info("Cancel copying.");
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        myProcess.totalProgress = 100;
    }
}
