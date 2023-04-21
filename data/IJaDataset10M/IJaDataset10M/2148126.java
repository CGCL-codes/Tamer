package com.ca.commons.cbutil;

import java.io.IOException;
import java.io.InputStream;

/**
 * This class attempts to launch a program (depending on its file extension) and attempts
 * to open the specified file.  For example: launch MS word (winword.exe) with file 'a.doc'.
 *
 * @author Trudi.
 */
public class CBLauncher {

    /**
     * This class attempts to launch a program (depending on its file extension) and attempts
     * to open the specified file.  For example: launch MS word (winword.exe) with file 'a.doc'.
     *
     * @param extension the file extension that determines what program to use (e.g. '.mp3').
     * @param fileName  the name of the file that needs to be opened (e.g. 'a.mp3').
     */
    public static void launchProgram(String extension, String fileName) {
        String command = null;
        StringBuffer fileType = new StringBuffer(20);
        StringBuffer program = new StringBuffer(50);
        Process p;
        InputStream stdOut = null;
        boolean collecting;
        int b;
        if (command == null) {
            try {
                p = Runtime.getRuntime().exec("cmd /c assoc " + extension);
                stdOut = p.getInputStream();
                collecting = false;
                while ((b = stdOut.read()) != -1) {
                    char c = (char) b;
                    if (c == '\r' || c == '\n') {
                    } else if (collecting) {
                        fileType.append(c);
                    } else if (c == '=') {
                        collecting = true;
                    }
                }
            } catch (IOException e) {
                CBUtility.error(CBIntText.get("Error trying to associate file extension: {0} with program", new String[] { extension }) + "\n" + e);
            }
            try {
                p = Runtime.getRuntime().exec("cmd /c ftype " + fileType.toString());
                stdOut = p.getInputStream();
                collecting = false;
                while ((b = stdOut.read()) != -1) {
                    char c = (char) b;
                    if (c == '\r' || c == '\n') {
                    } else if (collecting) {
                        program.append(c);
                    } else if (c == '=') {
                        collecting = true;
                    }
                }
            } catch (IOException e) {
                CBUtility.error(CBIntText.get("Error trying to associate file extension: {0} with program", new String[] { extension }) + "\n" + e);
            }
            command = program.toString();
        }
        String fullProgramName = program.toString();
        String runProgramName;
        if (program.toString().endsWith("%1")) runProgramName = fullProgramName.substring(0, fullProgramName.lastIndexOf("%1") - 1); else if (program.toString().endsWith("\"%L\"")) runProgramName = fullProgramName.substring(0, fullProgramName.lastIndexOf("\"%L\"") - 1); else runProgramName = program.toString();
        Runtime r = Runtime.getRuntime();
        try {
            r.exec(runProgramName + " \"" + fileName + "\"");
        } catch (IOException e) {
            CBUtility.error(CBIntText.get("Error occured when trying to launch program: {0} with the file {1}. Either the program found does not support the file type, or the file name was incorrect.", new String[] { runProgramName, fileName }) + "\n\n\n" + e);
        }
    }
}
