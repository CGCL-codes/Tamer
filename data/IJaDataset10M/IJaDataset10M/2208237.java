package org.mushroomdb.index.hash;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;
import org.mushroomdb.catalog.encoding.Encoder;
import org.mushroomdb.exception.RDBMSRuntimeException;
import org.mushroomdb.filemanagement.Page;
import org.mushroomdb.util.Properties;
import org.mushroomdb.util.PropertiesHelper;

public class DirectoryBucket implements Bucket {

    private List bucketEntries;

    private Page page;

    private Class keyClass;

    private Encoder encoder;

    private long depth;

    public DirectoryBucket(int depth) {
        bucketEntries = new LinkedList();
        this.depth = depth;
    }

    public void addEntry(BucketEntry entry) {
        if (bucketEntries.size() < 4) bucketEntries.add(entry); else throw new RDBMSRuntimeException("Buckets for Hash Index directory have a maximum size of 4");
    }

    public List getBucketEntries() {
        return bucketEntries;
    }

    public boolean isFull() {
        return bucketEntries.size() >= 4;
    }

    public long getDepth() {
        return depth;
    }

    public void setDepth(long depth) {
        this.depth = depth;
    }

    public void removeEntry(BucketEntry entry) {
        bucketEntries.remove(entry);
    }
}
