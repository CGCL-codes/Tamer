package bittorrent.data;

import java.util.List;
import protopeer.util.quantities.Data;

/**
 * Abstract base class for any file shared in the file-sharing (and/or VoD)
 * simulation.
 * 
 * An abstract file constists of a number of chunks and has a size and an id.
 */
public abstract class AbstractFile {

    /**
	 * Name of this file
	 */
    protected String id;

    /**
	 * List of the chunks the file constists of
	 */
    protected List<AbstractChunk> chunks;

    /**
	 * Size of this file
	 */
    private Data size;

    protected void setSize(Data size) {
        this.size = size;
    }

    public Data getSize() {
        return size;
    }

    public void setChunks(List<AbstractChunk> chunks) {
        this.chunks = chunks;
    }

    public List<AbstractChunk> getChunks() {
        return this.chunks;
    }

    public AbstractChunk getChunk(int chunkIndex) {
        if (chunkIndex < chunks.size()) {
            return chunks.get(chunkIndex);
        } else {
            return null;
        }
    }

    public int getNumChunks() {
        return chunks.size();
    }

    public int getNumBlocks() {
        int numblocks = 0;
        for (AbstractChunk chunk : this.chunks) {
            numblocks += chunk.getNumBlocks();
        }
        return numblocks;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
