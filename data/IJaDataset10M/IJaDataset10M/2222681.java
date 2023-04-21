package com.elibera.m.fileio;

import javax.microedition.lcdui.Displayable;
import com.elibera.m.utils.ActionListener;
import com.elibera.m.xml.HelperXMLParser;

/**
 * @author meisi
 *
 */
public class FileBrowserData extends FileBrowserElement {

    public static int ACTION_ID = 101;

    private ActionListener el;

    public FileBrowserData(ActionListener _el, Displayable background, String rootFolder, boolean useAllFiles, boolean useOnlyDirectories) {
        super(null, null, rootFolder, useAllFiles, useOnlyDirectories, -1, (ProcFileBrowser) HelperXMLParser.getProcDisplayElement(ProcFileBrowser.CLASS_ID));
        el = _el;
        dc = background;
    }

    /**
	 * this method is called, if the recording was finished<br>
	 * your action Listener will be calles
	 */
    public void recordingFinished() {
        System.out.println("recording finished!" + this.fileName);
        String[] fileInfo = { this.fileName, this.contentType, this.fileSize + "", this.lastModified + "" };
        el.doAction(ACTION_ID, fileInfo);
    }

    public void errorEoccured(String error) {
        System.out.println("error occured!" + error);
        el.errorOccured(ACTION_ID, error, ACTION_ID);
    }
}
