package com.carsongee.audiom;

import java.util.*;
import java.io.*;
import com.carsongee.jutils.*;

public class AudioM {

    private static MainFrame frame;

    private static PlayList pl;

    private static File flDstDirectory;

    private static File flDstPLS;

    private static double freeSpaceDstMiB;

    private static boolean blnTransferPLSFile = false;

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                frame = MainFrameUtility.createMainFrame();
            }
        });
    }

    public static PlayListInfo loadPlayList(File flPlayListIn) {
        PlayListInfo pli = new PlayListInfo();
        pl = new PlayList();
        String tmpFileName = (flPlayListIn.getName()).toLowerCase();
        int fileType;
        if (tmpFileName.endsWith(".m3u")) fileType = PlayList.FILE_TYPE_M3U; else if (tmpFileName.endsWith(".plp")) fileType = PlayList.FILE_TYPE_PLP; else if (tmpFileName.endsWith(".pla")) fileType = PlayList.FILE_TYPE_PLA; else if (tmpFileName.endsWith(".pls")) fileType = PlayList.FILE_TYPE_PLS; else {
            fileType = PlayList.FILE_TYPE_M3U;
        }
        if (pl.parsePlayList(flPlayListIn, fileType)) {
            pli.isParsed = true;
            pli.playListFile = pl.getInFile();
            pli.numFilesParsed = pl.getNumFiles();
            pli.sizeInMiBOfFilesParsed = Utils.roundTo(pl.getFileSizeMiB(), 2);
        } else {
            pl = null;
            pli.isParsed = false;
        }
        return pli;
    }

    public static DstDirectoryInfo loadDestination(File flDstDirectoryIn) {
        flDstDirectory = flDstDirectoryIn;
        DstDirectoryInfo ddi = new DstDirectoryInfo();
        if (flDstDirectory.exists() && flDstDirectory.canWrite()) {
            ddi.isUsable = true;
            ddi.dstDirectory = flDstDirectory;
            freeSpaceDstMiB = flDstDirectory.getUsableSpace() / (double) PlayList.BYTESINMIB;
            ddi.sizeInMiBOfFreeSpace = Utils.roundTo(freeSpaceDstMiB, 2);
        } else {
            flDstDirectory = null;
            ddi.isUsable = false;
        }
        return ddi;
    }

    public static String transferFiles() {
        ArrayList<File> transferList;
        if (pl == null) return "No valid source playlist file has been selected";
        if ((freeSpaceDstMiB - pl.getFileSizeMiB()) < 0 && flDstDirectory != null) return "There is not enough usable space in the destination directory";
        if (flDstDirectory != null) {
            transferList = pl.getParsedFiles();
            for (int i = 0; i < transferList.size(); i++) {
                File flTmp = transferList.get(i);
                try {
                    Utils.copy(flTmp, new File(flDstDirectory.toString() + File.separator + flTmp.getName()));
                } catch (Exception e) {
                    System.err.println("Could not copy file: " + flTmp + " to: " + flDstDirectory.toString() + File.separator + flTmp.getName());
                    return "Unable to copy at least one file...quitting.";
                }
            }
        }
        if (blnTransferPLSFile) {
            int fileType = PlayList.FILE_TYPE_M3U;
            String strTmp;
            if (flDstPLS != null) {
                strTmp = (flDstPLS.getName()).toLowerCase();
                if (strTmp.endsWith(".m3u")) fileType = PlayList.FILE_TYPE_M3U; else if (strTmp.endsWith(".plp")) fileType = PlayList.FILE_TYPE_PLP; else if (strTmp.endsWith(".pla")) fileType = PlayList.FILE_TYPE_PLA; else if (strTmp.endsWith(".pls")) fileType = PlayList.FILE_TYPE_PLS;
            } else {
                if (flDstDirectory == null) return "Unable to write playlist because there\n" + "There was no destination specified for the file";
                fileType = PlayList.FILE_TYPE_M3U;
                flDstPLS = new File(flDstDirectory.getPath() + File.separator + (pl.getInFile()).getName());
            }
            if (!pl.writePlayList(flDstDirectory, flDstPLS, fileType)) return "Unable to write playlist to destination.\n" + "If writing to PLP/PLA check that the devices first\n" + "directory is all caps";
        }
        return null;
    }

    public static boolean getTransferPLSFile() {
        return blnTransferPLSFile;
    }

    public static void setTransferPLSFile(boolean blnTxPLSFile) {
        blnTransferPLSFile = blnTxPLSFile;
    }

    public static boolean setDestinationPLSFile(File dstPLSFile) {
        File tmp = new File(dstPLSFile.getParent());
        if (tmp.canWrite()) {
            flDstPLS = dstPLSFile;
            return true;
        }
        return false;
    }
}

class PlayListInfo {

    public boolean isParsed = false;

    public File playListFile;

    public int numFilesParsed;

    public double sizeInMiBOfFilesParsed;
}

class DstDirectoryInfo {

    public boolean isUsable = false;

    public File dstDirectory;

    public double sizeInMiBOfFreeSpace;
}
