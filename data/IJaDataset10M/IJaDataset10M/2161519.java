package org.jcvi.vics.model.user_data.hmmer3;

import org.jcvi.vics.model.tasks.Task;
import org.jcvi.vics.model.user_data.FileNode;

public class HMMER3ResultFileNode extends FileNode {

    protected long hitCount;

    public static final String TAG_OUTPUT_FILE = "hmmer3.out";

    public static final String TAG_PER_SEQ_HITS_FILE = "hmmer3SeqHits.tbl";

    public static final String TAG_PER_DOMAIN_HITS_FILE = "hmmer3DomainHits.tbl";

    public HMMER3ResultFileNode() {
    }

    public String getSubDirectory() {
        return "HMMER3Results";
    }

    public HMMER3ResultFileNode(String owner, Task task, String name, String description, String visibility, String relativeSessionPath) {
        super(owner, task, name, description, visibility, "directory", relativeSessionPath);
    }

    public String getFilePathByTag(String tag) {
        if (tag.equals(TAG_OUTPUT_FILE)) return getFilePath(TAG_OUTPUT_FILE);
        if (tag.equals(TAG_PER_SEQ_HITS_FILE)) return getFilePath(TAG_PER_SEQ_HITS_FILE);
        if (tag.equals(TAG_PER_DOMAIN_HITS_FILE)) return getFilePath(TAG_PER_DOMAIN_HITS_FILE);
        return null;
    }

    public long getHitCount() {
        return hitCount;
    }

    public void setHitCount(long hitCount) {
        this.hitCount = hitCount;
    }
}
