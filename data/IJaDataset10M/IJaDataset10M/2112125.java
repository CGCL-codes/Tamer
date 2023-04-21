package ra.lajolla.utilities;

import java.io.File;
import java.util.ArrayList;

public class RecursiveFileCollector {

    ArrayList<String> arrayListWithFiles;

    public RecursiveFileCollector() {
        this.arrayListWithFiles = new ArrayList<String>();
    }

    public void collectFilesRecursively(String rootDirectory) {
        File dir = new File(rootDirectory);
        if (dir.isFile()) {
            if (!dir.isHidden()) {
                this.arrayListWithFiles.add(dir.getAbsolutePath());
            }
        }
        if (dir.isDirectory()) {
            if (!dir.isHidden()) {
                String[] children = dir.list();
                for (int i = 0; i < children.length; i++) {
                    collectFilesRecursively(dir.getPath() + File.separator + children[i]);
                }
            }
        }
    }

    public ArrayList<String> getArrayListWithFiles() {
        return this.arrayListWithFiles;
    }

    /**
	 * resets the contents. Can omit another new() recursive bla... 
	 */
    public void reset() {
        arrayListWithFiles = new ArrayList<String>();
    }
}
