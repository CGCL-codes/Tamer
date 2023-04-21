package uk.ac.lkl.common.util.datafile.event;

import uk.ac.lkl.common.util.datafile.DataFile;
import uk.ac.lkl.common.util.event.EventObject;

public class DataFileEvent extends EventObject<DataFile> {

    private int index;

    public DataFileEvent(DataFile file, int index) {
        super(file);
        this.index = index;
    }

    public DataFile getFile() {
        return getSource();
    }

    public int getIndex() {
        return index;
    }
}
