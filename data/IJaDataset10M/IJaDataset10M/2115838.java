package photospace.vfs;

import java.io.*;
import org.apache.commons.logging.*;
import photospace.meta.*;
import photospace.search.*;
import photospace.vfs.event.*;

public class ChangeHandler implements FileEventListener {

    private static final Log log = LogFactory.getLog(ChangeHandler.class);

    private Persister persister;

    private Indexer indexer;

    public void handleFileEvent(FileEvent event) {
        try {
            if (log.isDebugEnabled()) log.debug("Handling file system event " + event);
            if (isExternalMetadataFile(event.getSource()) && event.getSource().exists()) {
                indexer.merge(new Meta[] { persister.getMeta(event.getSource()) });
                return;
            }
            if (FileEvent.CREATED.equals(event.getType())) {
                indexer.index(new Meta[] { persister.getMeta(event.getSource()) }, false);
            } else if (FileEvent.DELETED.equals(event.getType())) {
                Meta meta = new Meta();
                meta.setPath(persister.getPath(event.getSource()));
                indexer.delete(new Meta[] { meta });
            } else if (FileEvent.UPDATED.equals(event.getType())) {
                indexer.merge(new Meta[] { persister.getMeta(event.getSource()) });
            } else {
                throw new IllegalStateException("Don't know what to do with event " + event);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error handling file event " + event, e);
        }
    }

    private boolean isExternalMetadataFile(File file) {
        return !file.equals(persister.getMetaFile(file));
    }

    public void setPersister(Persister persister) {
        this.persister = persister;
    }

    public void setIndexer(Indexer indexer) {
        this.indexer = indexer;
    }
}
