package de.mpiwg.vspace.generation.copier.internal;

import java.io.File;
import java.util.Comparator;
import org.eclipse.core.runtime.Path;

public class FolderStructureComparator implements Comparator<String> {

    public int compare(String o1, String o2) {
        if ((o1 == null) || (o2 == null)) return 0;
        if (o1.startsWith(File.separator)) o1 = o1.substring(1, o1.length());
        if (o2.startsWith(File.separator)) o2 = o2.substring(1, o2.length());
        if (o1.endsWith(File.separator)) o1 = o1.substring(0, o1.length() - 1);
        if (o2.endsWith(File.separator)) o2 = o2.substring(0, o2.length() - 1);
        Path path1 = new Path(o1);
        Path path2 = new Path(o2);
        String[] folders1 = path1.segments();
        String[] folders2 = path2.segments();
        return folders1.length - folders2.length;
    }
}
