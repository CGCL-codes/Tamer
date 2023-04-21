package de.ipkgatersleben.agbi.uploader.fileIO.fileDialog;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 * fuer jede AuswahlMoeglichkeit in der Combobox
 * im Oeffnen/Speichern - Dialog ist ein FileFilter
 * notwendig.
 * Definiert Zuordnung Endung - Beschreibung
 * *.upl - Uploader-File
 *
 * @author Thomas Rutkowski
 */
public class UplSettingsFilter extends FileFilter {

    @Override
    public boolean accept(File f) {
        if (f.isDirectory()) return true;
        String ext = getExt(f);
        if (ext != null && (ext.equalsIgnoreCase("upl"))) return true;
        if (ext != null && (ext.equalsIgnoreCase("up"))) return true;
        return false;
    }

    @Override
    public String getDescription() {
        return "UploaderSettingsFile";
    }

    public FileFilter getFilter() {
        return this;
    }

    private String getExt(File f) {
        String ext = null;
        if (f.isDirectory()) return ext;
        String s = f.getName();
        int i = s.lastIndexOf('.');
        if (i > 0 && i < s.length() - 1) ext = s.substring(i + 1).toLowerCase();
        return ext;
    }
}
