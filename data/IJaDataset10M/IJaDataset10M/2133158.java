package net.sf.hdkp.fsv;

import java.io.File;
import java.util.*;
import javax.swing.filechooser.FileSystemView;

public class MacAppFileSystemView extends FilteredFileSystemView {

    public MacAppFileSystemView() {
        super(FileSystemView.getFileSystemView());
    }

    @Override
    protected File[] filterFiles(File[] files) {
        final List<File> filtered = new ArrayList<File>();
        for (File file : files) {
            if (!file.getName().toLowerCase().endsWith(".app")) {
                filtered.add(file);
            }
        }
        return filtered.toArray(new File[filtered.size()]);
    }
}
