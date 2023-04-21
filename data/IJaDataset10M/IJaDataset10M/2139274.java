package jaxlib.io.stream;

import java.io.Closeable;
import java.io.IOException;
import javax.annotation.CheckForNull;

/**
 * An input stream interpreting an underlying datasource as objects.
 * <i>
 * Unlike {@link java.io.ObjectInput} this type of stream is not necessarily bound to a stream of bytes.
 * </i>
 *
 * @author  jw
 * @since   JaXLib 1.0
 * @version $Id: ObjectReader.java 3029 2011-12-29 00:36:48Z joerg_wassmer $
 */
public interface ObjectReader<T> extends Closeable {

    /**
   * Read the next object.
   * This call blocks until the next object is available or the end of the stream has been reached.
   *
   * @return
   *  the next object in this stream; {@code null} if the end of the stream has been reached.
   *
   * @throws EOFException
   *  if the next object in the stream is truncated.
   * @throws IOException
   *  on any I/O error.
   *
   * @since JaXLib 1.0
   */
    @CheckForNull
    public T readNext() throws IOException;
}
