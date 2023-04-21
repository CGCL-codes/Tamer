package edu.nupt.jxta.impl.dht.table;

import java.io.Serializable;
import net.jxta.peer.PeerID;

/**
 * �ο�openChord���룬���isInIntervalʵ��,����fromString����by zeng��
 * 
 * 
 * Identifier for nodes and user-defined objects. New instances of this class
 * are created either when a node joins the network, or by the local node
 * inserting a user-defined object.
 * 
 * Once created, an ID instance is unmodifiable.
 * 
 * IDs of same length can be compared as this class implements
 * java.lang.Comparable. IDs of different length cannot be compared.
 * 
 * @author Sven Kaffille, Karsten Loesing
 * @version 1.0.5
 */
public final class ID implements Comparable<ID>, Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 6860626236168125168L;

    private HashFunction hashFunction = HashFunction.getHashFunction();

    private static final int HEX = 2;

    private static final int DEC = 1;

    private static final int BIN = 0;

    /**
	 * The representation of an id returned as String when {@link #toString()}
	 * is invoked. Is intialized with help of property
	 * <code>de.uniba.wiai.lspi.chord.data.ID.displayed.representation</code>.
	 * Possible values: <br/><br/>
	 * 
	 * <code>0 = BIN</code>, binary<br/> <code>1 = DEC</code>, decimal<br/>
	 * <code>2 = HEX</code>, hexadecimal<br/>
	 */
    private static int displayedRepresentation = HEX;

    /**
	 * The number of (highest) bytes of an id returned as String when
	 * {@link #toString()} is invoked. Is intialized with help of property
	 * <code>de.uniba.wiai.lspi.chord.data.ID.number.of.displayed.bytes</code>.
	 */
    private static int numberOfDisplayedBytes = Integer.MAX_VALUE;

    /**
	 * The bytes representing the id.
	 */
    private final byte[] id;

    /**
	 * Creates a new ID consisting of the given byte[] array. The ID is assumed
	 * to have (ID.length * 8) bits. It must have leading zeros if its value has
	 * fewer digits than its maximum length.
	 * 
	 * @param id1
	 *            Byte array containing the ID.
	 */
    public ID(byte[] id1) {
        this.id = new byte[id1.length];
        System.arraycopy(id1, 0, this.id, 0, id1.length);
    }

    /**
	 * Representation of this as a String.
	 */
    private transient String stringRepresentation = null;

    /**
	 * Returns a string of the decimal representation of this ID, including
	 * leading zeros.
	 * 
	 * @return Decimal string of ID
	 */
    public final String toString() {
        if (this.stringRepresentation == null) {
            int rep = ID.displayedRepresentation;
            switch(rep) {
                case 0:
                    this.stringRepresentation = this.toBinaryString(ID.numberOfDisplayedBytes);
                    break;
                case 1:
                    this.stringRepresentation = this.toDecimalString(ID.numberOfDisplayedBytes);
                    break;
                default:
                    this.stringRepresentation = this.toHexString(ID.numberOfDisplayedBytes);
            }
        }
        return this.stringRepresentation;
    }

    /**
	 * Returns a string of the hexadecimal representation of the first
	 * <code>n</code> bytes of this ID, including leading zeros.
	 * 
	 * @param numberOfBytes
	 * 
	 * @return Hex string of ID
	 */
    public final String toHexString(int numberOfBytes) {
        int displayBytes = Math.max(1, Math.min(numberOfBytes, this.id.length));
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < displayBytes; i++) {
            String block = Integer.toHexString(this.id[i] & 0xff).toUpperCase();
            if (block.length() < 2) {
                block = "0" + block;
            }
            result.append(block + " ");
        }
        return result.toString();
    }

    /**
	 * Returns a string of the hexadecimal representation of this ID, including
	 * leading zeros.
	 * 
	 * @return Hex string of ID
	 */
    public final String toHexString() {
        return this.toHexString(this.id.length);
    }

    /**
	 * Returns a string of the decimal representation of the first
	 * <code>n</code> bytes of this ID, including leading zeros.
	 * 
	 * @param numberOfBytes
	 * 
	 * @return Hex string of ID
	 */
    public final String toDecimalString(int numberOfBytes) {
        int displayBytes = Math.max(1, Math.min(numberOfBytes, this.id.length));
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < displayBytes; i++) {
            String block = Integer.toString(this.id[i] & 0xff);
            result.append(block + " ");
        }
        return result.toString();
    }

    /**
	 * Returns a string of the decimal representation of this ID, including
	 * leading zeros.
	 * 
	 * @return Decimal string of ID
	 */
    public final String toDecimalString() {
        return this.toDecimalString(this.id.length);
    }

    /**
	 * Returns a string of the binary representation of the first <code>n</code>
	 * bytes of this ID, including leading zeros.
	 * 
	 * @param numberOfBytes
	 * 
	 * @return Hex string of ID
	 */
    public final String toBinaryString(int numberOfBytes) {
        int displayBytes = Math.max(1, Math.min(numberOfBytes, this.id.length));
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < displayBytes; i++) {
            String block = Integer.toBinaryString(this.id[i] & 0xff);
            while (block.length() < 8) {
                block = "0" + block;
            }
            result.append(block + " ");
        }
        return result.toString();
    }

    /**
	 * Returns a string of the binary representation of this ID, including
	 * leading zeros.
	 * 
	 * @return Binary string of ID
	 */
    public final String toBinaryString() {
        return this.toBinaryString(this.id.length);
    }

    /**
	 * Returns length of this ID measured in bits. ID length is determined by
	 * the length of the stored byte[] array, i.e. leading zeros have to be
	 * stored in the array.
	 * 
	 * @return Length of this ID measured in bits.
	 */
    public final int getLength() {
        return 7;
    }

    /**
	 * Calculates the ID which is 2^powerOfTwo bits greater than the current ID
	 * modulo the maximum ID and returns it.
	 * 
	 * @param powerOfTwo
	 *            Power of two which is added to the current ID. Must be a value
	 *            of the interval [0, length-1], including both extremes.
	 * @return ID which is 2^powerOfTwo bits greater than the current ID modulo
	 *         the maximum ID.
	 */
    public final ID addPowerOfTwo(int powerOfTwo) {
        if (powerOfTwo < 0 || powerOfTwo >= (this.id.length * 8)) {
            throw new IllegalArgumentException("The power of two is out of range! It must be in the interval " + "[0, length-1]");
        }
        byte[] copy = new byte[this.id.length];
        System.arraycopy(this.id, 0, copy, 0, this.id.length);
        int indexOfByte = this.id.length - 1 - (powerOfTwo / 8);
        byte[] toAdd = { 1, 2, 4, 8, 16, 32, 64, -128 };
        byte valueToAdd = toAdd[powerOfTwo % 8];
        byte oldValue;
        do {
            oldValue = copy[indexOfByte];
            copy[indexOfByte] += valueToAdd;
            valueToAdd = 1;
        } while (oldValue < 0 && copy[indexOfByte] >= 0 && indexOfByte-- > 0);
        return new ID(copy);
    }

    /**
	 * Checks the given object for equality with this {@link ID}.
	 * 
	 * @param equalsTo
	 *            Object to check equality with this {@link ID}.
	 */
    public final boolean equals(Object equalsTo) {
        if (equalsTo == null || !(equalsTo instanceof ID)) {
            return false;
        }
        return (this.compareTo((ID) equalsTo) == 0);
    }

    /**
	 * Compare current ID with the given object. If either the object is not a
	 * ID or both IDs' lengths do not match, a ClassCastException is thrown.
	 * Otherwise both IDs are compared byte by byte.
	 * 
	 * @return -1, 0, or 1, if this ID is smaller, same size, or greater than
	 *         the given object, respectively.
	 */
    public final int compareTo(ID otherKey) throws ClassCastException {
        if (this.getLength() != otherKey.getLength()) {
            throw new ClassCastException("Only ID objects with same length can be " + "compared! This ID is " + this.id.length + " bits long while the other ID is " + otherKey.getLength() + " bits long.");
        }
        byte[] otherBytes = new byte[this.id.length];
        System.arraycopy(otherKey.id, 0, otherBytes, 0, this.id.length);
        for (int i = 0; i < this.id.length; i++) {
            if ((byte) (this.id[i] - 128) < (byte) (otherBytes[i] - 128)) {
                return -1;
            } else if ((byte) (this.id[i] - 128) > (byte) (otherBytes[i] - 128)) {
                return 1;
            }
        }
        return 0;
    }

    /**
	 * @see java.lang.Object#hashCode()
	 */
    public final int hashCode() {
        int result = 19;
        for (int i = 0; i < this.id.length; i++) {
            result = 13 * result + this.id[i];
        }
        return result;
    }

    /**
	 * Checks if this ID is in the interval determined by the two given IDs.
	 * Neither of the boundary IDs is included in the interval����Ӧ��Ҫ��һ���Ǳյ�X��. If both IDs
	 * match, the interval is assumed to span the whole ID ring.
	 * 
	 * @param fromID
	 *            Lower bound of interval.
	 * @param toID
	 *            Upper bound of interval.
	 * @return If this key is included in the given interval.
	 */
    public final boolean isInInterval(ID fromID, ID toID) {
        if (fromID.equals(toID)) {
            return (!this.equals(fromID));
        }
        if (fromID.compareTo(toID) < 0) {
            return (this.compareTo(fromID) > 0 && this.compareTo(toID) < 0);
        }
        byte[] minIDBytes = new byte[this.id.length];
        ID minID = new ID(minIDBytes);
        byte[] maxIDBytes = new byte[this.id.length];
        for (int i = 0; i < maxIDBytes.length; i++) {
            maxIDBytes[i] = -1;
        }
        ID maxID = new ID(maxIDBytes);
        return ((!fromID.equals(maxID) && this.compareTo(fromID) > 0 && this.compareTo(maxID) < 0) || (!minID.equals(toID) && this.compareTo(minID) >= 0 && this.compareTo(toID) < 0));
    }

    /**
 * ��ʱ��֧��16����
 * 
 * @param id
 * @return
 */
    public static ID fromString(String id) {
        String[] ids = id.split("\\s+");
        byte[] idb = new byte[1];
        int index = 0;
        for (String s : ids) {
            Integer i = Integer.parseInt(s, 16);
            idb[index] = i.byteValue();
            index++;
        }
        return new ID(idb);
    }

    public static void main(String[] args) {
        String zz1 = "ff";
        String zz2 = "ssss";
        String zz3 = "aa";
        String zz4 = "9erer";
        ID id1 = HashFunction.getHashFunction().createID(zz1.getBytes());
        ID id2 = HashFunction.getHashFunction().createID(zz2.getBytes());
        ID id3 = HashFunction.getHashFunction().createID(zz3.getBytes());
        ID id4 = HashFunction.getHashFunction().createID(zz4.getBytes());
        System.out.println(id1.toHexString());
        System.out.println(id2.toHexString());
        System.out.println(id3.toHexString());
        System.out.println(id4.toHexString());
    }
}
