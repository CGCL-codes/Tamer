package jlibs.core.io;

import jlibs.core.util.DefaultComparator;
import java.io.File;

/**
 * @author Santhosh Kumar T
 */
public class FileNameComparator extends DefaultComparator<File> {

    @Override
    protected int _compare(File file1, File file2) {
        return file1.getName().compareToIgnoreCase(file2.getName());
    }
}
