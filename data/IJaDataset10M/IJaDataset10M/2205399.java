package net.sf.joafip.heapfile.record.entity;

import java.io.Serializable;

/**
 * data record identifier value type: a really too much huge number composed of
 * 3 long !<br>
 * 
 * @author luc peuvrier
 * 
 */
public class DataRecordIdentifier implements Comparable<DataRecordIdentifier>, Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1978464325793039601L;

    private final long long1;

    /**
	 * create the first data record identifier: #0
	 * 
	 */
    public DataRecordIdentifier() {
        super();
        this.long1 = 0;
    }

    /**
	 * create a data record identifier with initial small value ( the firsts
	 * data recording )
	 * 
	 * @param value
	 *            initiale value
	 */
    public DataRecordIdentifier(final int value) {
        super();
        this.long1 = value;
    }

    /**
	 * create setting value
	 * 
	 * @param long1
	 * @param long2
	 */
    public DataRecordIdentifier(final long long1) {
        super();
        this.long1 = long1;
    }

    /**
	 * create the next value of a data record identifier
	 * 
	 * @param dataRecordIdentifier
	 *            reference data identifier
	 */
    public DataRecordIdentifier(final DataRecordIdentifier dataRecordIdentifier) {
        super();
        this.long1 = dataRecordIdentifier.long1 + 1;
    }

    public long getLong1() {
        return long1;
    }

    public int compareTo(final DataRecordIdentifier dataRecordIdentifier) {
        final int compareTo;
        if (long1 < dataRecordIdentifier.long1) {
            compareTo = -1;
        } else if (long1 > dataRecordIdentifier.long1) {
            compareTo = 1;
        } else {
            compareTo = 0;
        }
        return compareTo;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + (int) (long1 ^ (long1 >>> 32));
        return result;
    }

    @Override
    @SuppressWarnings("PMD")
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final DataRecordIdentifier other = (DataRecordIdentifier) obj;
        if (long1 != other.long1) return false;
        return true;
    }

    @Override
    public String toString() {
        return "" + long1;
    }
}
