package totalcommander;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import javax.swing.filechooser.FileSystemView;

/**
 *
 * @author Vostro1500
 */
public class ListFileEntries {

    FileSystemView f = FileSystemView.getFileSystemView();

    private FileEntry[] _listFileEntries = null;

    public ListFileEntries(String Path) {
        File List = new File(Path);
        if (List.exists()) {
            if (f.isDrive(List)) {
                _listFileEntries = new FileEntry[List.listFiles().length];
                for (int i = 0; i < List.listFiles().length; i++) {
                    File temp = List.listFiles()[i];
                    _listFileEntries[i] = new FileEntry(temp);
                }
                Sort();
            } else {
                if (List.listFiles() != null) {
                    _listFileEntries = new FileEntry[List.listFiles().length + 1];
                    for (int i = 0; i < List.listFiles().length; i++) {
                        File temp = List.listFiles()[i];
                        _listFileEntries[i] = new FileEntry(temp);
                    }
                    FileEntry Back = new FileEntry();
                    _listFileEntries[_listFileEntries.length - 1] = Back;
                    Sort();
                } else {
                    FileEntry Back = new FileEntry();
                    _listFileEntries = new FileEntry[1];
                    _listFileEntries[_listFileEntries.length - 1] = Back;
                }
            }
        }
    }

    public FileEntry[] getList() {
        return _listFileEntries;
    }

    public void Sort() {
        Comparator<FileEntry> byDirThenAlpha = new DirAlphaComparator();
        Arrays.sort(_listFileEntries, byDirThenAlpha);
    }
}

class DirAlphaComparator implements Comparator<FileEntry> {

    public int compare(FileEntry filea, FileEntry fileb) {
        if (filea.isBackEntry()) {
            return -1;
        }
        if (fileb.isBackEntry()) {
            return 1;
        }
        if (filea.isDir() && !fileb.isDir()) {
            return -1;
        } else if (!filea.isDir() && fileb.isDir()) {
            return 1;
        } else {
            return filea.getName().compareToIgnoreCase(fileb.getName());
        }
    }
}
