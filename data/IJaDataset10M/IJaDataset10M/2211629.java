package org.iqual.chaplin.example.pump.res.dir;

import org.iqual.chaplin.FromContext;
import org.iqual.chaplin.ToContext;
import org.iqual.chaplin.composite.CompositeLifeCycle;
import org.iqual.chaplin.composite.Composite;
import org.iqual.chaplin.example.pump.MetaReader;
import org.iqual.chaplin.example.pump.ReaderEntry;
import java.util.Iterator;
import java.util.concurrent.SynchronousQueue;

/**
 * @author Zbynek Slajchrt
 * @since Jul 3, 2009 7:36:33 PM
 */
public abstract class DirReader implements MetaReader {

    private static final Object END_OF_DIR = new Object();

    private final SynchronousQueue<Object> filesQueue = new SynchronousQueue<Object>();

    private Thread traversingThread;

    @ToContext(CompositeLifeCycle.class)
    void init(Composite composite) {
        if (traversingThread == null) {
            traversingThread = new Thread() {

                public void run() {
                    try {
                        traverseDirectory(null);
                        filesQueue.put(END_OF_DIR);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            };
            traversingThread.start();
        }
    }

    @FromContext(Directory.class)
    protected abstract Iterator<Object> listResources(Object dirResource);

    @FromContext(Directory.class)
    protected abstract ReaderEntry createEntryForResource(Object resource);

    @FromContext(Directory.class)
    protected abstract boolean isDirResource(Object resource);

    private void traverseDirectory(Object dirResource) throws InterruptedException {
        Iterator<Object> resourcesIter = listResources(dirResource);
        while (resourcesIter.hasNext()) {
            Object resource = resourcesIter.next();
            if (isDirResource(resource)) {
                traverseDirectory(resource);
            } else {
                filesQueue.put(resource);
            }
        }
    }

    public ReaderEntry next() throws Exception {
        Object resource = filesQueue.take();
        if (resource == END_OF_DIR) {
            return null;
        }
        return createEntryForResource(resource);
    }
}
