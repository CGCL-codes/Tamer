package com.googlecode.quillen.util;

import com.googlecode.quillen.domain.ShadowFile;
import com.googlecode.quillen.domain.FileInfo;
import com.googlecode.quillen.domain.AttributeStorageException;
import com.googlecode.quillen.service.ShadowFileService;
import static com.googlecode.quillen.util.Utils.logInfo;
import java.util.*;
import java.util.concurrent.Semaphore;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by IntelliJ IDEA.
 * User: greg
 * Date: Apr 11, 2009
 * Time: 3:45:33 PM
 */
public class ShadowFileBatch {

    private static final Log LOG = LogFactory.getLog(ShadowFileBatch.class);

    private static final int BATCH_SIZE = 100;

    private final Map<String, ShadowFile> shadowFiles;

    private final Map<String, Semaphore> semaphores;

    private final ShadowFileService shadowFileService;

    private final WorkQueue workQueue;

    private final Collection<String> snapshots;

    private final ResultConsumer<FileInfo> consumer;

    private long lastSize = 0;

    public ShadowFileBatch(ShadowFileService shadowFileService, WorkQueue workQueue, Collection<String> snapshots, ResultConsumer<FileInfo> consumer) {
        shadowFiles = new HashMap<String, ShadowFile>();
        semaphores = new HashMap<String, Semaphore>();
        this.shadowFileService = shadowFileService;
        this.workQueue = workQueue;
        this.snapshots = snapshots;
        this.consumer = consumer;
    }

    public ShadowFile add(String shadowKey, String filename, long size) throws AttributeStorageException, WorkQueueAbortedException {
        ShadowFile shadowFile;
        synchronized (this) {
            shadowFile = shadowFiles.get(shadowKey);
        }
        boolean isNew = false;
        if (shadowFile == null) {
            shadowFile = shadowFileService.getStub(shadowKey);
            if (shadowFile == null) {
                shadowFile = new ShadowFile(shadowKey);
                isNew = true;
            }
            shadowFile.setDate(new Date());
        }
        synchronized (this) {
            shadowFile.addFile(filename);
            shadowFiles.put(shadowKey, shadowFile);
            if (isNew) {
                synchronized (semaphores) {
                    semaphores.put(shadowKey, new Semaphore(0));
                }
            }
        }
        if (size != lastSize) {
            flush(BATCH_SIZE);
        }
        synchronized (this) {
            lastSize = size;
        }
        return isNew ? shadowFile : null;
    }

    public void flush(int min) throws WorkQueueAbortedException {
        final List<ShadowFile> toPut;
        synchronized (this) {
            if (shadowFiles.size() >= min) {
                toPut = new ArrayList<ShadowFile>(shadowFiles.values());
                shadowFiles.clear();
            } else {
                toPut = null;
            }
        }
        if (toPut != null) {
            workQueue.enqueue(new WorkItem() {

                public void run() throws Exception {
                    for (final ShadowFile sf : toPut) {
                        Semaphore sem;
                        synchronized (semaphores) {
                            sem = semaphores.get(sf.getKey());
                        }
                        if (sem != null) {
                            sem.acquire();
                            synchronized (semaphores) {
                                semaphores.remove(sf.getKey());
                            }
                        }
                    }
                    shadowFileService.batchPut(toPut, snapshots);
                    for (final ShadowFile sf : toPut) {
                        for (String filename : sf.getFilenames()) {
                            logInfo(LOG, "backed up file %s", filename);
                            if (consumer != null) {
                                for (String snapshot : snapshots) {
                                    consumer.newResult(new FileInfo(snapshot, filename, sf.getSize(), sf.getDate()));
                                }
                            }
                        }
                    }
                }
            });
        }
    }

    public void signalDone(String shadowKey) {
        synchronized (semaphores) {
            semaphores.get(shadowKey).release();
        }
    }
}
