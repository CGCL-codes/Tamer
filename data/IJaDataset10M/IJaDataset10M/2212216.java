package net.sf.mmm.data.api.datatype;

import java.io.InputStream;
import java.io.OutputStream;
import net.sf.mmm.util.io.api.RuntimeIoException;
import net.sf.mmm.util.lang.api.Datatype;

/**
 * This is the interface for a BLOB (Binary Large OBject) value. The interface
 * only declares read methods.
 * 
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface Blob extends Datatype<Blob> {

    /**
   * This method gets the current size of this {@link Blob}.
   * 
   * @see java.io.File#length()
   * 
   * @return the size in bytes.
   */
    long getSize();

    /**
   * This method gets read access to the data of this {@link Blob}.
   * 
   * @see MutableBlob#streamData(OutputStream)
   * 
   * @return the {@link Blob} data as input stream.
   * @throws RuntimeIoException if the BLOB could not be opened for reading.
   */
    InputStream getReadAccess() throws RuntimeIoException;

    /**
   * This method gets read access to the data of this {@link Blob}.
   * 
   * @see MutableBlob#streamData(OutputStream)
   * 
   * @param offset is the index of the first byte to retrieve from the
   *        {@link Blob}.
   * @return the {@link Blob} data as input stream.
   * @throws RuntimeIoException if the BLOB could not be opened for reading or
   *         if the <code>offset</code> is negative or greater than
   *         {@link #getSize()}.
   */
    InputStream getReadAccess(long offset) throws RuntimeIoException;

    /**
   * This method writes the data of this BLOB to the given output stream. The
   * given output stream will NOT be closed.<br/>
   * <b>ATTENTION:</b><br>
   * This is a blocking operation. According to the size of the {@link Blob}
   * this may block the current thread for some time.
   * 
   * @param outStream is the stream where to write the data to.
   * @throws RuntimeIoException if the streaming fails.
   */
    void streamData(OutputStream outStream) throws RuntimeIoException;

    /**
   * This method writes the data of this BLOB to the given output stream. The
   * given output stream will NOT be closed.<br/>
   * <b>ATTENTION:</b><br>
   * This is a blocking operation. According to the size of the {@link Blob}
   * this may block the current thread for some time.
   * 
   * @param outStream is the stream where to write the data to.
   * @param offset is the index of the first byte to retrieve from the
   *        {@link Blob}.
   * @throws RuntimeIoException if the streaming fails.
   */
    void streamData(OutputStream outStream, long offset) throws RuntimeIoException;

    /**
   * This method gets the revision counter of this {@link Blob}. It is initially
   * <code>0</code> when the {@link Blob} is logically created and gets
   * increased on each {@link MutableBlob modification}.
   * 
   * @return the revision number.
   */
    int getRevision();
}
