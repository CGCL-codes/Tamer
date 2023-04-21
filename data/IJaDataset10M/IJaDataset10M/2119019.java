package com.jiexplorer.filetask;

import java.io.File;
import java.util.List;
import java.util.ListIterator;
import com.jiexplorer.db.JIThumbnailService;
import com.jiexplorer.util.JIUtility;

public class DeleteFileTask extends FileTask {

    public DeleteFileTask(final File file) {
        super(file, null, false);
        this.operator = "From ";
        setDestinationFolder(file.getParentFile());
    }

    public DeleteFileTask(final List<File> list) {
        super(list, null, true);
        this.operator = "From ";
        setDestinationFolder((list.get(0)).getParentFile());
    }

    @Override
    public String getOperationName() {
        return "Delete ";
    }

    public void run() {
        final ListIterator listiterator = getSourceFilesList().listIterator();
        while (listiterator.hasNext() && !isCancelled()) {
            final File file = (File) listiterator.next();
            setSource(file);
            setDestinationFolder(file.getParentFile());
            this.listener.fileTaskProgress(this);
            if (confirmDelete(file)) {
                JIUtility.deleteFile(file);
                JIThumbnailService.getInstance().removeFile(file);
                this.performed++;
            }
            setOverallProgress(getOverallProgress() + 1L);
            this.listener.fileTaskProgress(this);
        }
        this.listener.fileTaskCompleted(this);
    }
}
