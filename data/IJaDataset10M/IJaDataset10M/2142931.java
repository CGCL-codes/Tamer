package ar.com.oddie.persistence.entities;

import java.io.Serializable;

@SuppressWarnings("serial")
public class BPlusTreeBlockHeader implements Serializable {

    private long id;

    private int keyCount;

    private boolean leaf;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getKeyCount() {
        return keyCount;
    }

    public void setKeyCount(int keyCount) {
        this.keyCount = keyCount;
    }

    public boolean isLeaf() {
        return leaf;
    }

    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }
}
