package moxie.log;

import ostore.util.QuickSerializable;
import org.acplt.oncrpc.XdrAble;

public interface DataBlock extends QuickSerializable, XdrAble {

    /**
     * Return true if <i>data</i> for <code>DataBlock is <code>null</code>
     * or contains no  bytes (i.e. size==0); otherwise, return true.
     *
     * @return true if <i>data</i> for <code>DataBlock</code> is
     * <code>null</code> or contains no bytes (i.e. size==0). Otherwise,
     * false.  */
    public boolean isEmpty();

    /**
     * Return size (i.e. length) of data in number of bytes.  
     * If <code>DataBlock</code> <code>{@link #isEmpty}==true</code>, then 
     * return value for size is undefined.
     *
     * @return size (i.e. length) of data in number of bytes.
     * If <code>DataBlock</code> <code>{@link #isEmpty}==true</code>, then 
     * return value for size is undefined. */
    public int getSize();

    /**
     * Return data in byte array form.
     *
     * @return data (in byte array form). */
    public byte[] getData();

    /** Specified by java.lang.Object */
    public boolean equals(Object other);

    /** Specified by java.lang.Object */
    public String toString();
}
