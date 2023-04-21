package com.loribel.lib.webcapture;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import com.loribel.commons.util.GB_DebugTools;

/**
 * Interface for WebCapture
 * 
 * @author Gregory Borelli
 */
public class GB_WebCaptureWebshot implements GB_WebCapture {

    private static String DIR = "C:/ProgramsCopy/WebShot";

    private static String EXE = DIR + "/webshotcmd.exe";

    public static void setHome(File a_dir) {
        if (a_dir == null) {
            return;
        }
        DIR = a_dir.getAbsolutePath();
        EXE = DIR + "/webshotcmd.exe";
    }

    public File createScreenshot(String a_url, File a_fileDest, boolean a_overwrite) throws IOException {
        if (DIR == null) {
            throw new IOException("WEBSHOT_DIR not specified.");
        }
        File l_file = new File(EXE);
        if (!l_file.exists()) {
            throw new FileNotFoundException("File " + l_file.getAbsolutePath() + " not found.");
        }
        if (a_overwrite) {
            a_fileDest.delete();
        }
        if (a_fileDest.exists()) {
            return a_fileDest;
        }
        a_fileDest.getParentFile().mkdirs();
        String l_cmd;
        try {
            l_cmd = EXE + " /url \"" + a_url + "\" /out \"" + a_fileDest + "\"";
            Runtime.getRuntime().exec(l_cmd);
            while (!a_fileDest.exists()) {
                Thread.sleep(500);
            }
            GB_DebugTools.debug(this, "write " + a_fileDest.getAbsolutePath());
            Thread.sleep(1000);
        } catch (Exception ex) {
            throw new IOException("Cannot create " + a_fileDest + ": " + ex.getMessage());
        }
        return a_fileDest;
    }
}
