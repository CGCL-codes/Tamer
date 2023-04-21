package org.ulmac.fileItems;

import org.ulmac.enums.FileFormat;

public class FlacQueueItem extends FileQueueItem {

    public FlacQueueItem(String path, String fileName) {
        super(path, fileName, FileFormat.FLAC);
    }

    @Override
    public void processItem() {
        super.convertFile();
        System.out.println("done processing");
    }
}
