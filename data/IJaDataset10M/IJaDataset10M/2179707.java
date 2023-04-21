package wjhk.jupload2.gui;

import java.util.Comparator;
import wjhk.jupload2.filedata.FileData;

/**
 * Technical class, used to sort rows in the
 * wjhk.jupload2.gui.FilePanelDataModel2 class.
 */
public class ColumnComparator implements Comparator<FileData> {

    protected int index;

    protected boolean ascending;

    /**
     * Creates a new instance.
     * 
     * @param index The column index of the table data to be compared
     * @param ascending Specifies the sort order.
     */
    public ColumnComparator(int index, boolean ascending) {
        this.index = index;
        this.ascending = ascending;
    }

    /**
     * @param one
     * @param two
     * @return -1, 0 or 1, as usual.
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    public int compare(FileData one, FileData two) {
        Object oOne;
        Object oTwo;
        switch(this.index) {
            case FilePanelDataModel2.COLINDEX_NAME:
                oOne = (one).getFileName();
                oTwo = (two).getFileName();
                break;
            case FilePanelDataModel2.COLINDEX_SIZE:
                oOne = new Long((one).getFileLength());
                oTwo = new Long((two).getFileLength());
                break;
            case FilePanelDataModel2.COLINDEX_DIRECTORY:
                oOne = (one).getDirectory();
                oTwo = (two).getDirectory();
                break;
            case FilePanelDataModel2.COLINDEX_MODIFIED:
                oOne = (one).getLastModified();
                oTwo = (two).getLastModified();
                break;
            default:
                return 0;
        }
        if (oOne instanceof Comparable && oTwo instanceof Comparable) {
            return ((Comparable) oOne).compareTo(oTwo) * (this.ascending ? 1 : -1);
        }
        return 0;
    }
}
