package src.backend;

import java.io.Serializable;
import src.gui.*;
import src.backend.*;

public class ChunkHeader implements Serializable {

    Chunk chunk;

    String tagString;

    int tag;

    int nextOffset;

    int offset;

    int length;

    private boolean hasChanged = false;

    public ChunkHeader() {
    }

    public void setChunk(Chunk chunk) {
        this.chunk = chunk;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public void setTagString(String tag) {
        this.tagString = tag;
    }

    public void setNextOffset(int nextOffset) {
        this.nextOffset = nextOffset;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public Chunk getChunk() {
        return this.chunk;
    }

    public int getTag() {
        return this.tag;
    }

    public String getTagString() {
        return this.tagString;
    }

    public int getNextOffset() {
        return this.nextOffset;
    }

    public int getLength() {
        return this.length;
    }

    public int getOffset() {
        return this.offset;
    }

    public void write() {
        MapWriter mw = this.chunk.getLevel().getMapData().getMapWriter();
        mw.seek(this.offset + this.chunk.getLevel().getOffset());
        mw.writeInt(this.tag);
        mw.writeInt(this.nextOffset);
        mw.writeInt(this.length);
        mw.writeInt(0);
    }
}
