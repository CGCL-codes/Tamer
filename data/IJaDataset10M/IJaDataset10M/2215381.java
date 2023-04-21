package vivace.view.openDialogue;

import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.*;

/**
 * A class to view developer chosen icons for certain files in the open dialogue
 */
public class ImageFileView extends FileView {

    ImageIcon midiIcon = Utils.createImageIcon("images/midiIconSmall.png");

    public String getName(File f) {
        return null;
    }

    public String getDescription(File f) {
        return null;
    }

    public Boolean isTraversable(File f) {
        return null;
    }

    /**
	 * Get the type of File
	 * 
	 * @param 	File 	file		file for view
	 * @return	String	type	returns the type of the file
	 */
    public String getTypeDescription(File f) {
        String extension = Utils.getExtension(f);
        String type = null;
        if (extension != null) {
            if (extension.equals(Utils.mid)) {
                type = "MIDI File";
            }
        }
        return type;
    }

    /**
	 * Enumerator that explains the supported operations for this class.	
	 * 
	 * @param 	File 	f		file for view
	 * @return	Icon	icon	returns the icon to show
	 */
    public Icon getIcon(File file) {
        String extension = Utils.getExtension(file);
        Icon icon = null;
        if (extension != null) {
            if (extension.equals(Utils.mid)) {
                icon = midiIcon;
            }
        }
        return icon;
    }
}
