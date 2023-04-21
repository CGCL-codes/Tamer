package commands;

import java.util.Iterator;
import java.io.File;
import java.io.IOException;

/**
 * fixme.
 */
public class FileCompletionIterator implements Iterator {

    private String dirList[];

    private String matchName;

    private String nextFileName;

    private int index;

    public FileCompletionIterator(String startFile) {
        try {
            File f = (new File(startFile)).getCanonicalFile();
            matchName = f.getName();
            dirList = f.getParentFile().list();
        } catch (IOException e) {
            dirList = null;
            matchName = null;
        }
        index = 0;
    }

    public boolean hasNext() {
        if (dirList == null) return false;
        while (index < dirList.length) {
            nextFileName = dirList[index++];
            if (nextFileName.startsWith(matchName)) return true;
        }
        return false;
    }

    public Object next() {
        return nextFileName;
    }

    public void remove() {
    }
}
