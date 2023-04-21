package org.jenmo.core.domain;

import java.nio.ByteBuffer;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;
import org.jenmo.common.marker.ICopyable;
import org.jenmo.core.listener.IListener;
import org.jenmo.core.multiarray.IBlobPartAccessor;

/**
 * A {@link SplitBlobPart} entity is a part of a {@link SplitBlob}. For very heavy blobs, having
 * this division allows 'decent' SQL queries when exporting database (using mysqldump for instance),
 * and allows to extract only few parts from database instead of the whole blob if needed.
 * <p>
 * Client code may be notified when extracting a new part from database using a {@link IListener}
 * (see {@link SplitBlob} or {@link IBlobPartAccessor}).
 * 
 * @author Nicolas Ocquidant
 * @since 1.0
 */
@Entity
@Table(name = "SPLITBLOBPART")
public class SplitBlobPart implements ICopyable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SplitBlobPartSeq")
    @SequenceGenerator(name = "SplitBlobPartSeq", sequenceName = "SPLITBLOBPART_ID_SEQ", allocationSize = 10)
    @Column(name = "ID")
    private long id;

    @Version
    @Column(name = "VERSION")
    private int version;

    @Basic
    @Column(name = "ORDR", updatable = false)
    private int ordr;

    @SuppressWarnings("unused")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_ID", nullable = false, updatable = false)
    private SplitBlob parent;

    @Basic
    @Column(name = "DATASIZE", updatable = false)
    private int dataSize;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "DATA", columnDefinition = "OID")
    private byte[] data;

    @Transient
    private ByteBuffer buffer;

    private SplitBlobPart() {
        buffer = null;
    }

    private SplitBlobPart(int wantedDataSize) {
        dataSize = wantedDataSize;
    }

    protected static SplitBlobPart newInstance(SplitBlob parent, int ordr, int wantedDataSize) {
        if (parent == null) {
            throw new NullPointerException("Parent cannot be null");
        }
        if (ordr < 0) {
            throw new IllegalArgumentException("Ord=" + ordr + ", must be >= 0");
        }
        if (wantedDataSize <= 0) {
            throw new IllegalArgumentException("WantedDataSize=" + wantedDataSize + ", must be > 0");
        }
        SplitBlobPart instance = new SplitBlobPart(wantedDataSize);
        instance.parent = parent;
        instance.ordr = ordr;
        return instance;
    }

    /**
    * The copy factory.
    */
    protected static SplitBlobPart copy(SplitBlob newParent, SplitBlobPart toCopy) {
        if (newParent == null) {
            throw new NullPointerException("Parent cannot be null");
        }
        if (toCopy == null) {
            throw new NullPointerException("Cannot copy null");
        }
        SplitBlobPart instance = new SplitBlobPart();
        instance.ordr = toCopy.ordr;
        instance.parent = newParent;
        instance.dataSize = toCopy.dataSize;
        instance.data = toCopy.data.clone();
        return instance;
    }

    /**
    * Gets the persistent identity of the instance.
    */
    public final long getId() {
        return id;
    }

    /**
    * Gets the immutable version field. A version field is useful to detect concurrent modifications
    * to the same datastore record.
    */
    public int getVersion() {
        return version;
    }

    protected final int getOrdr() {
        return ordr;
    }

    public final boolean isBufferOpen() {
        return (buffer != null);
    }

    /**
    * Opens the underlying buffer in order to be able to put/get values.
    */
    public final void openBuffer() {
        ByteBuffer var = buffer;
        if (var == null) {
            synchronized (this) {
                var = buffer;
                if (var == null) {
                    if (data == null) {
                        data = new byte[dataSize];
                    }
                    var = buffer = ByteBuffer.wrap(data);
                }
            }
        }
    }

    /**
    * Closes the underlying buffer if not needed any more.
    */
    public final void closeBuffer() {
        buffer = null;
    }

    /**
    * Absolute <i>put</i> method for writing a double value.
    * 
    * @param index
    *           The index at which the bytes will be written
    */
    public final void putDouble(final int index, final double v) {
        buffer.putDouble(index, v);
    }

    /**
    * Absolute <i>put</i> method for writing a float value.
    * 
    * @param index
    *           The index at which the bytes will be written
    */
    public final void putFloat(final int index, final float v) {
        buffer.putFloat(index, v);
    }

    /**
    * Absolute <i>put</i> method for writing a int value.
    * 
    * @param index
    *           The index at which the bytes will be written
    */
    public final void putInt(final int index, final int v) {
        buffer.putInt(index, v);
    }

    /**
    * Absolute <i>put</i> method for writing a long value.
    * 
    * @param index
    *           The index at which the bytes will be written
    */
    public final void putLong(final int index, final long v) {
        buffer.putLong(index, v);
    }

    /**
    * Absolute <i>put</i> method for writing a short value.
    * 
    * @param index
    *           The index at which the bytes will be written
    */
    public final void putShort(final int index, final short v) {
        buffer.putShort(index, v);
    }

    /**
    * Absolute <i>put</i> method for writing a byte value.
    * 
    * @param index
    *           The index at which the bytes will be written
    */
    public final void putByte(final int index, final byte v) {
        buffer.put(index, v);
    }

    /**
    * Absolute <i>put</i> method for writing a boolean value.
    * 
    * @param index
    *           The index at which the bytes will be written
    */
    public final void putBoolean(final int index, final boolean v) {
        putByte(index, (v == false) ? (byte) 0 : (byte) 1);
    }

    /**
    * Absolute <i>put</i> method for writing a char value.
    * 
    * @param index
    *           The index at which the bytes will be written
    */
    public final void putChar(final int index, final char v) {
        throw new UnsupportedOperationException();
    }

    /**
    * Absolute <i>get</i> method for reading a double value.
    * 
    * @param index
    *           The index from which the bytes will be read
    */
    public final double getDouble(final int index) {
        return buffer.getDouble(index);
    }

    /**
    * Absolute <i>get</i> method for reading a float value.
    * 
    * @param index
    *           The index from which the bytes will be read
    */
    public final float getFloat(final int index) {
        return buffer.getFloat(index);
    }

    /**
    * Absolute <i>get</i> method for reading an int value.
    * 
    * @param index
    *           The index from which the bytes will be read
    */
    public final int getInt(final int index) {
        return buffer.getInt(index);
    }

    /**
    * Absolute <i>get</i> method for reading a long value.
    * 
    * @param index
    *           The index from which the bytes will be read
    */
    public final long getLong(final int index) {
        return buffer.getLong(index);
    }

    /**
    * Absolute <i>get</i> method for reading a short value.
    * 
    * @param index
    *           The index from which the bytes will be read
    */
    public final short getShort(final int index) {
        return buffer.getShort(index);
    }

    /**
    * Absolute <i>get</i> method for reading a byte value.
    * 
    * @param index
    *           The index from which the bytes will be read
    */
    public final byte getByte(final int index) {
        return buffer.get(index);
    }

    /**
    * Absolute <i>get</i> method for reading a boolean value.
    * 
    * @param index
    *           The index from which the bytes will be read
    */
    public final boolean getBoolean(final int index) {
        byte out = buffer.get(index);
        return (out == 0) ? false : true;
    }

    /**
    * Absolute <i>get</i> method for reading a char value.
    * 
    * @param index
    *           The index from which the bytes will be read
    */
    public final char getChar(final int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return (super.toString() + "(pk=" + id + ")");
    }
}
