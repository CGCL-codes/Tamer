package sourcefileslister;

import java.io.File;
import java.util.Comparator;

public abstract class FileComparator implements Comparator<File> {

    private FileComparator baseComparator;

    private int min;

    private int max;

    public FileComparator(boolean ascending, FileComparator baseComparator) {
        this.baseComparator = baseComparator;
        this.min = (ascending) ? 0 : 1;
        this.max = (ascending) ? 1 : 0;
    }

    public int compare(File file0, File file1) {
        int value = this.compareFiles(file0, file1);
        if (value != 0) {
            return value;
        }
        if (this.baseComparator != null) {
            return this.baseComparator.compare(file0, file1);
        }
        return max;
    }

    protected int getMin() {
        return this.min;
    }

    protected int getMax() {
        return this.max;
    }

    protected abstract int compareFiles(File file0, File file1);
}
