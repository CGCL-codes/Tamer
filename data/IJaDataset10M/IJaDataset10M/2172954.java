package gmgen.pluginmgr.messages;

import gmgen.pluginmgr.GMBComponent;
import gmgen.pluginmgr.GMBMessage;
import javax.swing.filechooser.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Send this message to inform all components that a a call has taken place
 * asking the current tab to open a file.
 *
 * @author Soulcatcher
 * @since May 23, 2003
 */
public class FileTypeMessage extends GMBMessage {

    private ArrayList fileTypes = new ArrayList();

    /**
	 * Constructor for the StateChangedMessage object
	 *
	 * @param comp
	 *          Component sending the state changed message
	 */
    public FileTypeMessage(GMBComponent comp) {
        super(comp);
    }

    /**
	 * @return Returns the file.
	 */
    public FileFilter[] getFileypes() {
        return (FileFilter[]) fileTypes.toArray(new FileFilter[fileTypes.size()]);
    }

    /**
	 * @param aFileTypes The file types to add.
	 */
    public void addFileTypes(FileFilter[] aFileTypes) {
        this.fileTypes.addAll(Arrays.asList(aFileTypes));
    }
}
