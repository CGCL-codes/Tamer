package de.intarsys.pdf.st;

import java.io.IOException;
import de.intarsys.pdf.cos.COSObject;
import de.intarsys.pdf.cos.COSObjectKey;
import de.intarsys.pdf.crypt.ISystemSecurityHandler;
import de.intarsys.pdf.parser.COSLoadException;

/**
 * Abstract superclass for a XRef entry.
 * <p>
 * The XRef entry describes an object slot in a XRef. The entry consists of an
 * offset in the file, a generation number and a token indicating if this entry
 * is free.
 */
public abstract class STXRefEntry implements Comparable {

    /** The generation number of the entry */
    private COSObjectKey key;

    /**
	 * Create XRefEntry
	 * 
	 * @param index
	 * 
	 * @param generation
	 * 
	 */
    protected STXRefEntry(COSObjectKey key) {
        this.key = key;
    }

    public abstract boolean isFree();

    public int getGenerationNumber() {
        return getKey().getGenerationNumber();
    }

    public int compareTo(Object obj) {
        if (obj instanceof STXRefEntry) {
            return (getObjectNumber() - ((STXRefEntry) obj).getObjectNumber());
        }
        return -1;
    }

    public abstract STXRefEntryOccupied fill(int pos);

    protected abstract void unlink();

    public int getObjectNumber() {
        return getKey().getObjectNumber();
    }

    public String toString() {
        return getKey().toString();
    }

    public abstract COSObject load(STDocument doc, ISystemSecurityHandler securityHandler) throws IOException, COSLoadException;

    public abstract long getColumn1();

    public abstract int getColumn2();

    public COSObjectKey getKey() {
        return key;
    }

    public abstract void accept(IXRefEntryVisitor visitor) throws XRefEntryVisitorException;

    public abstract STXRefEntry copy();
}
