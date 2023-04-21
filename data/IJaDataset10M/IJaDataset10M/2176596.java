package net.sf.hdkp.fsv;

import javax.swing.filechooser.FileSystemView;

public class MacFileSystemViewProvider implements FileSystemViewProvider {

    private static FileSystemView FileSystemView;

    @Override
    public synchronized FileSystemView createFileSystemView() {
        if (MacFileSystemViewProvider.FileSystemView == null) {
            MacFileSystemViewProvider.FileSystemView = new MacAppFileSystemView();
        }
        return MacFileSystemViewProvider.FileSystemView;
    }
}
