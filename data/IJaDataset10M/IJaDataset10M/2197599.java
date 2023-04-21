package net.smdp.java.jSmdpManager;

import javax.swing.filechooser.*;
import java.io.File;
import java.util.Hashtable;
import java.util.Enumeration;

/**
 * @author Felix Kronlage <fkr@grummel.net>
 * @version 0.0.1
 */
public class AudioFileFilter extends FileFilter {

    private Hashtable audioFilter;

    private String description = "Audio Files";

    public AudioFileFilter() {
        this.audioFilter = new Hashtable();
    }

    public boolean accept(File chosenFile) {
        boolean isAccepted = false;
        if (chosenFile != null) {
            if (chosenFile.isDirectory()) {
                isAccepted = true;
            }
            String extension = getExtension(chosenFile);
            if ((extension != null) && (audioFilter.get(getExtension(chosenFile)) != null)) {
                isAccepted = true;
            }
            ;
        }
        return (isAccepted);
    }

    public void addFilter(String filterString) {
        this.audioFilter.put(filterString.toLowerCase(), this);
    }

    public void addFilter(String[] filterStrings) {
        for (int i = 0; i < filterStrings.length; i++) {
            this.audioFilter.put(filterStrings[i].toLowerCase(), this);
        }
    }

    public String getExtension(File audioFile) {
        if (audioFile != null) {
            String filename = audioFile.getName();
            int i = filename.lastIndexOf('.');
            if ((i > 0) && (i < filename.length() - 1)) {
                return (filename.substring(i + 1).toLowerCase());
            }
            ;
        }
        return (null);
    }

    public void setDescription(String description, boolean showExtensions) {
        if (showExtensions) {
            Enumeration extensions = this.audioFilter.keys();
            if (extensions != null) {
                description += " ( ." + extensions.nextElement();
                while (extensions.hasMoreElements()) {
                    description += ", ." + (String) extensions.nextElement();
                }
                description += " )";
            }
        }
        this.description = description;
    }

    public String getDescription() {
        return (this.description);
    }
}
