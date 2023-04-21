package org.furthurnet.servergui;

import java.io.File;
import java.io.IOException;
import org.furthurnet.flac.FlacStatusListener;
import org.furthurnet.furi.ErrorConsole;
import org.furthurnet.furi.MainFrame;
import org.furthurnet.md5.MD5StatusListener;

public class FileSetVerify {

    private FileSetInfo fsi;

    private ListPanel listPanel = null;

    private MainFrame mainFrame = null;

    public FileSetVerify(FileSetInfo fileSet, ListPanel lp, MainFrame mf) {
        fsi = fileSet;
        listPanel = lp;
        mainFrame = mf;
    }

    public void checkFileSetStructure() throws FileSetValidationException, IOException {
        fsi.checkFileTypes();
        generateMD5();
    }

    public void validateFilesInSet() throws FileSetValidationException {
        if (fsi.isFlac()) {
            validateFlac();
        } else {
            fsi.checkChecksums();
        }
        fsi.overrideStatus(ServerGuiConstants.STATUS_NOT_SERVING);
        Thread.yield();
        listPanel.repaintList();
        mainFrame.saveSharingInfo();
    }

    public void doFileSetValidation(FileSetInfo f) throws FileSetValidationException, IOException {
        fsi = f;
        doFileSetValidation();
    }

    public void doFileSetValidation() throws FileSetValidationException, IOException {
        checkFileSetStructure();
        validateFilesInSet();
    }

    private void validateFlac() throws FileSetValidationException {
        fsi.overrideStatus(ServerGuiConstants.STATUS_FLAC_VERIFICATION);
        FlacStatusListener listener = new FlacListener(fsi, mainFrame, listPanel);
        fsi.checkFlac(listener);
    }

    private void generateMD5() throws IOException {
        fsi.overrideStatus(ServerGuiConstants.STATUS_GENERATING_MD5);
        MD5Listener listener = new MD5Listener(fsi, mainFrame);
        for (int i = 0; i < fsi.numFiles; i++) {
            fsi.files[i] = fsi.files[i].setMd5(org.furthurnet.md5.MD5.getMD5(new File(fsi.files[i].getFileNameWithPath()), listener, true));
        }
        fsi.generateId();
        listPanel.repaintList();
    }

    public class FlacListener implements FlacStatusListener {

        private MainFrame mainFrame = null;

        private FileSetInfo fsi = null;

        private ListPanel listPanel = null;

        private long lastStatusUpdate = 0;

        public FlacListener(FileSetInfo _fsi, MainFrame _mainFrame, ListPanel panel) {
            fsi = _fsi;
            mainFrame = _mainFrame;
            listPanel = panel;
        }

        public void updateStatus(int numFiles, int totalFiles) {
            fsi.overrideStatus((int) ((double) numFiles / (double) totalFiles * 100.0) + "% " + ServerGuiConstants.STATUS_FLAC_VERIFICATION);
            long now = System.currentTimeMillis();
            if (now - lastStatusUpdate >= 2000) {
                lastStatusUpdate = now;
                listPanel.repaintList();
            }
        }

        public void completed() {
            fsi.generateId();
            fsi.overrideStatus(ServerGuiConstants.STATUS_NOT_SERVING);
            Thread.yield();
            listPanel.repaintList();
            mainFrame.saveSharingInfo();
        }

        public void reportFlacError(String error) {
            listPanel.removeRow(fsi);
            ErrorConsole.logWithPopup("Invalid File Set", error);
        }

        public void reportFlacIOError(String error) {
            listPanel.removeRow(fsi);
            ErrorConsole.logWithPopup("I/O Error While Verifying File Set", error);
        }
    }

    private class MD5Listener implements MD5StatusListener {

        private MainFrame mainFrame = null;

        private FileSetInfo fsi = null;

        private long total = 0;

        private long fileSetSize = 0;

        private long lastStatusUpdate = 0;

        public MD5Listener(FileSetInfo _fsi, MainFrame _mainFrame) {
            fsi = _fsi;
            mainFrame = _mainFrame;
            fileSetSize = fsi.totalBytes;
        }

        public void updateStatus(long bytesProcessed) {
            fsi.overrideStatus((int) ((bytesProcessed + total) * 100.0 / fileSetSize) + "% " + ServerGuiConstants.STATUS_GENERATING_MD5);
            long now = System.currentTimeMillis();
            if (now - lastStatusUpdate >= 2000) {
                lastStatusUpdate = now;
                listPanel.repaintList();
            }
        }

        public void completed(long bytesProcessed) {
            total += bytesProcessed;
        }
    }
}
