package de.mpiwg.vspace.generation.control.internal;

import java.io.File;
import de.mpiwg.vspace.filehandler.services.FileHandler;
import de.mpiwg.vspace.generation.control.Activator;

public class TempFolderManager {

    public static final TempFolderManager INSTANCE = new TempFolderManager();

    private final String temp = "temp2";

    private final String images = "resizedImages";

    private TempFolderManager() {
    }

    public File getImageFolder() {
        File folder = FileHandler.getAbsoluteFileFromProject(Activator.PLUGIN_ID, getRelativeImageFolderPath());
        return folder;
    }

    public String getRelativeImageFolderPath() {
        return File.separator + temp + File.separator + images + File.separator;
    }

    public void clearTempFolder() {
        File folder = FileHandler.getAbsoluteFileFromProject(Activator.PLUGIN_ID, getRelativeImageFolderPath());
        if (folder != null) clearFolder(folder);
    }

    private void clearFolder(File folder) {
        if (folder == null) return;
        File[] files = folder.listFiles();
        if ((files == null) || (files.length == 0)) return;
        for (File f : files) {
            if (f.isFile()) f.delete(); else if (!f.getName().startsWith(".")) clearFolder(f); else continue;
        }
    }
}
