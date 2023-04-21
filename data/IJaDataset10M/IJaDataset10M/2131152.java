package org.activision.io.cache;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.activision.io.InStream;
import org.activision.io.OutStream;

public class IdxSystemWriter {

    private RandomAccessFile idx;

    private int type;

    private int lastEdit;

    private Map<Object, CacheFile> files;

    public IdxSystemWriter(RandomAccessFile idx, int type) {
        this.setIdx(idx);
        this.setType(type);
        loadIdx();
    }

    public void loadIdx() {
        try {
            this.getIdx().writeByte(this.getType());
            String date = new Date().getDate() + "" + (new Date().getMonth() + 1) + "" + (new Date().getYear() + 1900);
            int lastEdit = Integer.parseInt(date);
            this.getIdx().writeInt(lastEdit);
            this.setLastEdit(lastEdit);
            this.setFiles(new HashMap<Object, CacheFile>());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeFile(Object key, OutStream file) {
        if (this.getType() == 0) {
            if (!this.getFiles().containsKey(key)) {
                try {
                    this.getIdx().seek((8 * (Integer) key) + 5);
                    int position = RS2CacheWriter.getData().addData(file);
                    this.getIdx().writeInt(position);
                    this.getIdx().writeInt(file.offset());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (this.getType() == 1) {
            try {
                this.getIdx().writeLong((Long) key);
                int position = RS2CacheWriter.getData().addData(file);
                this.getIdx().writeInt(position);
                this.getIdx().writeInt(file.offset());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public InStream getData(Object key) {
        if (this.getType() == 0) {
            if (!this.getFiles().containsKey(key)) {
                try {
                    this.getIdx().seek((8 * (Integer) key) + 5);
                    CacheFile file = new CacheFile(this.getIdx().readInt(), this.getIdx().readInt());
                    this.getFiles().put(key, file);
                    return file.getData();
                } catch (IOException e) {
                    return null;
                }
            }
            return this.getFiles().get(key).getData();
        } else if (this.getType() == 1) {
            if (!this.getFiles().containsKey(key)) return null;
            return this.getFiles().get(key).getData();
        }
        return null;
    }

    public int amtOfFiles() {
        try {
            if (this.getType() == 0) return (int) ((idx.length() - 5) / 8); else if (this.getType() == 1) return this.getFiles().size();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void setIdx(RandomAccessFile idx) {
        this.idx = idx;
    }

    public RandomAccessFile getIdx() {
        return idx;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setFiles(Map<Object, CacheFile> files) {
        this.files = files;
    }

    public Map<Object, CacheFile> getFiles() {
        return files;
    }

    public void setLastEdit(int lastEdit) {
        this.lastEdit = lastEdit;
    }

    public int getLastEdit() {
        return lastEdit;
    }
}
