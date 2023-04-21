package sywico.core.diff;

import java.io.Serializable;
import java.util.List;
import sywico.core.message.Message;

/**
 * 
 * a Change Chunk 
 *
 */
public class Chunk implements Serializable {

    static final long serialVersionUID = Message.serialVersionUID;

    /**
	 * The first line replaced by the change  
	 */
    int oldIdxStart;

    /**
	 * the first line AFTER the change
	 */
    int oldIdxStop;

    /**
	 * provided as a convenience. it is the first line of the change
	 * in the new version
	 * 
	 */
    int newIdxStart;

    /**
	 * contents of the change. If it is empty, the change is a delete 
	 */
    List<String> newContents;

    public List<String> getNewContents() {
        return newContents;
    }

    public int getNewIdxStart() {
        return newIdxStart;
    }

    public int getOldIdxStart() {
        return oldIdxStart;
    }

    public int getOldIdxStop() {
        return oldIdxStop;
    }

    public Chunk(int oldIdxStart, int oldIdxStop, int newIdxStart, List<String> newContents) {
        super();
        this.oldIdxStart = oldIdxStart;
        this.oldIdxStop = oldIdxStop;
        this.newIdxStart = newIdxStart;
        this.newContents = newContents;
    }

    public Chunk(int oldIdxStart, int oldIdxStop, List<String> newContents) {
        this(oldIdxStart, oldIdxStop, -1, newContents);
    }

    public String toString() {
        String retVal = "@[" + oldIdxStart + "-" + oldIdxStop + "[ (@" + newIdxStart + "):";
        for (String content : newContents) {
            retVal += ">" + content;
        }
        return retVal;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Chunk)) return false;
        Chunk chunk = (Chunk) obj;
        return this.oldIdxStart == chunk.oldIdxStart && this.oldIdxStop == chunk.oldIdxStop && this.newIdxStart == chunk.newIdxStart && this.newContents.equals(chunk.newContents);
    }

    public int hashCode() {
        return newContents.hashCode() + oldIdxStart;
    }
}
