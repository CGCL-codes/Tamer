package lg.mobile.gui.filesystem;

import java.io.File;
import java.io.IOException;
import javax.swing.filechooser.FileSystemView;
import sun.awt.shell.ShellFolder;

public class DefaultFileSystemView extends FileSystemView {

    private FileSystemView view;

    public DefaultFileSystemView() {
        view = getFileSystemView();
    }

    @Override
    public File[] getRoots() {
        File[] roots = super.getRoots();
        File[] newRoot = roots[0].listFiles();
        int index = 0;
        for (int i = 0; i < newRoot.length; i++) {
            if (newRoot[i] instanceof ShellFolder) {
                ShellFolder folder = (ShellFolder) newRoot[index];
                if (folder.getPath().equals("::{20D04FE0-3AeA-1069-A2D8-08002B309D}")) {
                    index = i;
                    break;
                }
            }
        }
        return newRoot[index].listFiles();
    }

    @Override
    public File getDefaultDirectory() {
        File[] roots = super.getRoots();
        File[] newRoot = roots[0].listFiles();
        int index = 0;
        for (int i = 0; i < newRoot.length; i++) {
            if (newRoot[i] instanceof ShellFolder) {
                ShellFolder folder = (ShellFolder) newRoot[index];
                if (folder.getPath().equals("::{20D04FE0-3AeA-1069-A2D8-08002B309D}")) {
                    index = i;
                    break;
                }
            }
        }
        return newRoot[index];
    }

    @Override
    public File createNewFolder(File file) throws IOException {
        return null;
    }
}
