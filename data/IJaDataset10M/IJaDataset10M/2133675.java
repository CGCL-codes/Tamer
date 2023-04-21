package com.bluebrim.content.impl.client;

import java.io.*;
import com.bluebrim.content.impl.shared.*;

/**
 * A windows implementation of the FileOpener.
 * Creation date: (2001-10-01 15:37:40)
 * @author: Marcus Hirt
 */
public class CoFileOpenerWinImpl extends CoFileOpener {

    private static final String EXEC_COMMAND = "rundll32 url.dll,FileProtocolHandler ";

    private static final String TEMP_NAME = "calvin_tmp_file";

    public CoFileOpenerWinImpl() {
        super();
    }

    /**
	 * Returns the extension of the supplied filename.
	 * Creation date: (2001-10-05 10:32:10)
	 * @param: String fileName The file name from which to retrieve the extention (suffix).
	 * @return: String The extension (suffix) of the file.
	 */
    public static String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    /**
	 * Returns the id of the os that this FileOpener handles.
	 */
    public int getOpenerType() {
        return CoFileOpener.OsEnumeration.WINDOWS;
    }

    /**
	 * Test. Supply the name of a file. That file will be read into
	 * a content object, exported to a temporary directory and
	 * opened with whatever program is associated with that kind of
	 * file.
	 * Creation date: (2001-10-03 15:09:43)
	 * @param args java.lang.String[]
	 */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Need to supply a filename!\nExiting!");
            System.exit(1);
        }
        try {
            com.bluebrim.content.shared.CoOriginalContent content = new CoDefaultOriginalContent();
            content.load(args[0]);
            CoFileOpener.getFileOpener().saveAndOpenTemporaryFile(content);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
	 * The actual implementation of the "open a file" functionality for
	 * a specific target platform. Fixme: Maybe use an URL or File in the future?
	 * Creation date: (2001-10-01 16:49:16)
	 * @param: String location The path to the file. 
	 */
    public void openFile(String location) throws IOException {
        Process p = Runtime.getRuntime().exec(EXEC_COMMAND + location);
        try {
            p.waitFor();
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }

    /**
	 * Looks for a temporary directory (os dependant), stores the content 
	 * in a file using a temporary name and opens the newly stored file.
	 * @parameter: com.bluebrim.content.shared.CoOriginalContent content The content to export data from.
	 * @return: returns the filename under which the content was saved.
	 */
    public String saveAndOpenTemporaryFile(com.bluebrim.content.shared.CoOriginalContent content) throws IOException {
        String tmpDir = System.getProperty("java.io.tmpdir");
        if (tmpDir == null) tmpDir = System.getProperty("user.dir");
        if (tmpDir == null) tmpDir = "C:\\";
        String tmpFile = tmpDir + TEMP_NAME + getExtension(content.getOriginalFileName());
        int i = 0;
        while ((new File(tmpFile)).exists()) {
            tmpFile = tmpDir + TEMP_NAME + (i++) + getExtension(content.getOriginalFileName());
        }
        content.save(tmpFile);
        openFile(tmpFile);
        return tmpFile;
    }
}
