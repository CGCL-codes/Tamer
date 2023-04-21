package org.t2framework.commons.transaction.xa;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;
import javax.transaction.xa.Xid;

/**
 * An implementation of {@link Xid}.
 * 
 * @author shot
 * 
 */
public class XidImpl implements Xid, Serializable {

    public static final int FORMAT_ID = 0x0114;

    private static final long serialVersionUID = 1L;

    private static final byte[] INITIAL_BRANCH_ID = createBranchQualifierId(new byte[1]);

    private static final String GLOBAL_ID_BASE = System.nanoTime() + "/";

    protected final byte[] gtrid;

    protected final byte[] bqual;

    protected final int formatId;

    protected final int hashCode;

    private static AtomicInteger nextId = new AtomicInteger(0);

    public XidImpl() {
        this.formatId = FORMAT_ID;
        this.hashCode = createHashcode();
        this.gtrid = createGlobalId(hashCode);
        this.bqual = INITIAL_BRANCH_ID;
    }

    public XidImpl(Xid xid, int branchId) {
        this.formatId = xid.getFormatId();
        this.hashCode = xid.hashCode();
        this.gtrid = xid.getGlobalTransactionId();
        this.bqual = createBranchQualifierId(Integer.toString(branchId).getBytes());
    }

    @Override
    public byte[] getBranchQualifier() {
        return bqual.clone();
    }

    @Override
    public int getFormatId() {
        return this.formatId;
    }

    @Override
    public byte[] getGlobalTransactionId() {
        return gtrid.clone();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || (obj instanceof XidImpl) == false) {
            return false;
        }
        XidImpl other = (XidImpl) obj;
        if (this.formatId != other.getFormatId()) {
            return false;
        }
        final int gtridLength = this.gtrid.length;
        final int otherGtridLength = other.gtrid.length;
        final int bqualLength = this.bqual.length;
        final int otherBqualLength = other.bqual.length;
        if (gtridLength != otherGtridLength || bqualLength != otherBqualLength) {
            return false;
        }
        for (int i = 0; i < gtridLength; ++i) {
            if (this.gtrid[i] != other.gtrid[i]) {
                return false;
            }
        }
        for (int i = 0; i < bqualLength; ++i) {
            if (bqual[i] != other.bqual[i]) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    protected int createHashcode() {
        return nextId.incrementAndGet();
    }

    protected static byte[] createGlobalId(final int hashCode) {
        return convert64bytes((GLOBAL_ID_BASE + Integer.toString(hashCode)).getBytes(), MAXGTRIDSIZE);
    }

    protected static byte[] createBranchQualifierId(final byte[] bqual) {
        return convert64bytes(bqual, MAXBQUALSIZE);
    }

    private static byte[] convert64bytes(byte[] bytes, int size) {
        byte[] new64bytes = new byte[size];
        System.arraycopy(bytes, 0, new64bytes, 0, bytes.length);
        return new64bytes;
    }

    public String toString() {
        final String gid = new String(gtrid).trim();
        final String bid = new String(bqual).trim();
        return "FormatId=" + FORMAT_ID + ", GlobalId=" + gid + ", BranchId=" + (bid.isEmpty() == false ? bid : "none");
    }
}
