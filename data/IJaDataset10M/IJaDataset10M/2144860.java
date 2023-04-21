package com.phloc.commons.io.streams;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.EOFException;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.WillClose;
import javax.annotation.WillNotClose;
import javax.annotation.concurrent.Immutable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.phloc.commons.CGlobal;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.callback.INonThrowingRunnableWithParameter;
import com.phloc.commons.charset.CharsetManager;
import com.phloc.commons.collections.ArrayHelper;
import com.phloc.commons.io.IInputStreamProvider;
import com.phloc.commons.mutable.MutableLong;
import com.phloc.commons.state.ESuccess;
import com.phloc.commons.stats.IStatisticsHandlerSize;
import com.phloc.commons.stats.StatisticsManager;
import com.phloc.commons.string.StringHelper;

/**
 * Some very basic IO stream utility stuff. All input stream (=reading) related
 * stuff is quite <code>null</code> aware, where on writing an output stream may
 * never be null.
 * 
 * @author philip
 */
@Immutable
public final class StreamUtils {

    /** buffer size for copy operations */
    private static final int DEFAULT_BUFSIZE = 16 * CGlobal.BYTES_PER_KILOBYTE;

    /** The logger to use. */
    private static final Logger s_aLogger = LoggerFactory.getLogger(StreamUtils.class);

    private static final IStatisticsHandlerSize s_aByteSizeHdl = StatisticsManager.getSizeHandler(StreamUtils.class.getName() + "$COPY");

    private static final IStatisticsHandlerSize s_aCharSizeHdl = StatisticsManager.getSizeHandler(StreamUtils.class.getName() + "$COPYCHARS");

    @PresentForCodeCoverage
    @SuppressWarnings("unused")
    private static final StreamUtils s_aInstance = new StreamUtils();

    private StreamUtils() {
    }

    /**
   * Check if the passed exception is a known EOF exception.
   * 
   * @param t
   *        The throwable/exception to be checked. May be <code>null</code>.
   * @return <code>true</code> if it is a user-created EOF exception
   */
    public static boolean isKnownEOFException(@Nullable final Throwable t) {
        return t == null ? false : isKnownEOFException(t.getClass());
    }

    /**
   * Check if the passed class is a known EOF exception class.
   * 
   * @param aClass
   *        The class to be checked. May be <code>null</code>.
   * @return <code>true</code> if it is a known EOF exception class.
   */
    public static boolean isKnownEOFException(@Nullable final Class<?> aClass) {
        if (aClass == null) return false;
        final String sClass = aClass.getName();
        return sClass.equals("java.io.EOFException") || sClass.equals("org.mortbay.jetty.EofException") || sClass.equals("org.eclipse.jetty.io.EofException") || sClass.equals("org.apache.catalina.connector.ClientAbortException");
    }

    /**
   * Close the passed object, without trying to call flush on it.
   * 
   * @param aCloseable
   *        The object to be closed. May be <code>null</code>.
   * @return {@link ESuccess#SUCCESS} if the object was successfully closed.
   */
    @Nonnull
    public static ESuccess closeWithoutFlush(@Nullable @WillClose final Closeable aCloseable) {
        if (aCloseable != null) {
            try {
                aCloseable.close();
                return ESuccess.SUCCESS;
            } catch (final IOException ex) {
                if (!isKnownEOFException(ex)) s_aLogger.error("Failed to close stream " + aCloseable.getClass().getName(), ex);
            }
        }
        return ESuccess.FAILURE;
    }

    /**
   * Close the passed stream by encapsulating the declared {@link IOException}.
   * If the passed object also implements the {@link Flushable} interface, it is
   * tried to be flushed before it is closed.
   * 
   * @param aCloseable
   *        The object to be closed. May be <code>null</code>.
   * @return {@link ESuccess} if the object was successfully closed.
   */
    @Nonnull
    public static ESuccess close(@Nullable @WillClose final Closeable aCloseable) {
        if (aCloseable != null) {
            try {
                if (aCloseable instanceof Flushable) flush((Flushable) aCloseable);
                aCloseable.close();
                return ESuccess.SUCCESS;
            } catch (final NullPointerException ex) {
            } catch (final IOException ex) {
                if (!isKnownEOFException(ex)) s_aLogger.error("Failed to close object " + aCloseable.getClass().getName(), ex);
            }
        }
        return ESuccess.FAILURE;
    }

    /**
   * Flush the passed object encapsulating the declared {@link IOException}.
   * 
   * @param aFlushable
   *        The flushable to be flushed. May be <code>null</code>.
   * @return {@link ESuccess#SUCCESS} if the object was successfully flushed.
   */
    @Nonnull
    public static ESuccess flush(@Nullable final Flushable aFlushable) {
        if (aFlushable != null) try {
            aFlushable.flush();
            return ESuccess.SUCCESS;
        } catch (final NullPointerException ex) {
        } catch (final IOException ex) {
            if (!isKnownEOFException(ex)) s_aLogger.error("Failed to flush object " + aFlushable.getClass().getName(), ex);
        }
        return ESuccess.FAILURE;
    }

    /**
   * Pass the content of the given input stream to the given output stream. Both
   * the input stream and the output stream are automatically closed.
   * 
   * @param aIS
   *        The input stream to read from. May be <code>null</code>.
   *        Automatically closed!
   * @param aOS
   *        The output stream to write to. May be <code>null</code>.
   *        Automatically closed!
   * @return <code>{@link ESuccess#SUCCESS}</code> if copying took place,
   *         <code>{@link ESuccess#FAILURE}</code> otherwise
   */
    @Nonnull
    public static ESuccess copyInputStreamToOutputStreamAndCloseOS(@WillClose @Nullable final InputStream aIS, @WillClose @Nullable final OutputStream aOS) {
        try {
            return copyInputStreamToOutputStream(aIS, aOS);
        } finally {
            close(aOS);
        }
    }

    /**
   * Pass the content of the given input stream to the given output stream. The
   * input stream is automatically closed, whereas the output stream stays open!
   * 
   * @param aIS
   *        The input stream to read from. May be <code>null</code>.
   *        Automatically closed!
   * @param aOS
   *        The output stream to write to. May be <code>null</code>. Not
   *        automatically closed!
   * @return <code>{@link ESuccess#SUCCESS}</code> if copying took place,
   *         <code>{@link ESuccess#FAILURE}</code> otherwise
   */
    @Nonnull
    public static ESuccess copyInputStreamToOutputStream(@WillClose @Nullable final InputStream aIS, @WillNotClose @Nullable final OutputStream aOS) {
        return copyInputStreamToOutputStream(aIS, aOS, (MutableLong) null);
    }

    /**
   * Pass the content of the given input stream to the given output stream. The
   * input stream is automatically closed, whereas the output stream stays open!
   * 
   * @param aIS
   *        The input stream to read from. May be <code>null</code>.
   *        Automatically closed!
   * @param aOS
   *        The output stream to write to. May be <code>null</code>. Not
   *        automatically closed!
   * @param aCopyByteCount
   *        An optional mutable long object that will receive the total number
   *        of copied bytes. Note: and optional old value is overwritten!
   * @return <code>{@link ESuccess#SUCCESS}</code> if copying took place,
   *         <code>{@link ESuccess#FAILURE}</code> otherwise
   */
    @Nonnull
    public static ESuccess copyInputStreamToOutputStream(@WillClose @Nullable final InputStream aIS, @WillNotClose @Nullable final OutputStream aOS, @Nullable final MutableLong aCopyByteCount) {
        return copyInputStreamToOutputStream(aIS, aOS, new byte[DEFAULT_BUFSIZE], aCopyByteCount);
    }

    /**
   * Pass the content of the given input stream to the given output stream. The
   * input stream is automatically closed, whereas the output stream stays open!
   * 
   * @param aIS
   *        The input stream to read from. May be <code>null</code>.
   *        Automatically closed!
   * @param aOS
   *        The output stream to write to. May be <code>null</code>. Not
   *        automatically closed!
   * @param aBuffer
   *        The buffer to use. May not be <code>null</code>.
   * @return <code>{@link ESuccess#SUCCESS}</code> if copying took place,
   *         <code>{@link ESuccess#FAILURE}</code> otherwise
   */
    @Nonnull
    public static ESuccess copyInputStreamToOutputStream(@WillClose @Nullable final InputStream aIS, @WillNotClose @Nullable final OutputStream aOS, @Nonnull final byte[] aBuffer) {
        return copyInputStreamToOutputStream(aIS, aOS, aBuffer, null);
    }

    @Nonnegative
    private static long _copy(@Nonnull final InputStream aIS, @Nonnull final OutputStream aOS, @Nonnull final byte[] aBuffer) throws IOException {
        long nTotalBytesCopied = 0;
        int nBytesRead;
        while ((nBytesRead = aIS.read(aBuffer, 0, aBuffer.length)) > -1) {
            aOS.write(aBuffer, 0, nBytesRead);
            nTotalBytesCopied += nBytesRead;
        }
        return nTotalBytesCopied;
    }

    @Nonnegative
    private static long _copy(@Nonnull final Reader aReader, @Nonnull final Writer aWriter, @Nonnull final char[] aBuffer) throws IOException {
        long nTotalBytesCopied = 0;
        int nBytesRead;
        while ((nBytesRead = aReader.read(aBuffer, 0, aBuffer.length)) > -1) {
            aWriter.write(aBuffer, 0, nBytesRead);
            nTotalBytesCopied += nBytesRead;
        }
        return nTotalBytesCopied;
    }

    /**
   * Pass the content of the given input stream to the given output stream. The
   * input stream is automatically closed, whereas the output stream stays open!
   * 
   * @param aIS
   *        The input stream to read from. May be <code>null</code>.
   *        Automatically closed!
   * @param aOS
   *        The output stream to write to. May be <code>null</code>. Not
   *        automatically closed!
   * @param aBuffer
   *        The buffer to use. May not be <code>null</code>.
   * @param aCopyByteCount
   *        An optional mutable long object that will receive the total number
   *        of copied bytes. Note: and optional old value is overwritten!
   * @return <code>{@link ESuccess#SUCCESS}</code> if copying took place,
   *         <code>{@link ESuccess#FAILURE}</code> otherwise
   */
    @Nonnull
    public static ESuccess copyInputStreamToOutputStream(@WillClose @Nullable final InputStream aIS, @WillNotClose @Nullable final OutputStream aOS, @Nonnull @Nonempty final byte[] aBuffer, @Nullable final MutableLong aCopyByteCount) {
        if (ArrayHelper.isEmpty(aBuffer)) throw new IllegalArgumentException("buffer is empty");
        try {
            if (aIS != null && aOS != null) {
                final long nTotalBytesCopied = _copy(aIS, aOS, aBuffer);
                s_aByteSizeHdl.addSize(nTotalBytesCopied);
                if (aCopyByteCount != null) aCopyByteCount.set(nTotalBytesCopied);
                return ESuccess.SUCCESS;
            }
        } catch (final IOException ex) {
            if (!isKnownEOFException(ex)) s_aLogger.error("Failed to copy from stream to stream", ex);
        } finally {
            close(aIS);
        }
        return ESuccess.FAILURE;
    }

    /**
   * Get the number of available bytes in the passed input stream.
   * 
   * @param aIS
   *        The input stream to use. May be <code>null</code>.
   * @return 0 in case of an error or if the parameter was <code>null</code>.
   */
    public static int getAvailable(@Nullable final InputStream aIS) {
        if (aIS != null) try {
            return aIS.available();
        } catch (final IOException ex) {
        }
        return 0;
    }

    /**
   * Read all bytes from the passed input stream into a byte array.
   * 
   * @param aISP
   *        The input stream provider to read from. May be <code>null</code> .
   * @return The byte array or <code>null</code> if the parameter or the
   *         resolved input stream is <code>null</code>.
   */
    @Nullable
    public static byte[] getAllBytes(@Nullable final IInputStreamProvider aISP) {
        if (aISP == null) return null;
        return getAllBytes(aISP.getInputStream());
    }

    /**
   * Get a byte buffer with all the available content of the passed input
   * stream.
   * 
   * @param aIS
   *        The source input stream. May not be <code>null</code>.
   * @return A new {@link NonBlockingByteArrayOutputStream} with all available
   *         content inside.
   */
    @Nonnull
    public static NonBlockingByteArrayOutputStream getCopy(@Nonnull @WillClose final InputStream aIS) {
        final int nAvailable = Math.max(DEFAULT_BUFSIZE, getAvailable(aIS));
        final NonBlockingByteArrayOutputStream aBAOS = new NonBlockingByteArrayOutputStream(nAvailable);
        copyInputStreamToOutputStreamAndCloseOS(aIS, aBAOS);
        return aBAOS;
    }

    /**
   * Read all bytes from the passed input stream into a byte array.
   * 
   * @param aIS
   *        The input stream to read from. May be <code>null</code>.
   * @return The byte array or <code>null</code> if the input stream is
   *         <code>null</code>.
   */
    @Nullable
    public static byte[] getAllBytes(@Nullable @WillClose final InputStream aIS) {
        if (aIS == null) return null;
        return getCopy(aIS).toByteArray();
    }

    /**
   * Read all bytes from the passed input stream into a string.
   * 
   * @param aISP
   *        The input stream provider to read from. May be <code>null</code> .
   * @param sCharset
   *        The charset to use. May not be <code>null</code> .
   * @return The String or <code>null</code> if the parameter or the resolved
   *         input stream is <code>null</code>.
   */
    @Nullable
    public static String getAllBytesAsString(@Nullable final IInputStreamProvider aISP, @Nonnull @Nonempty final String sCharset) {
        if (aISP == null) return null;
        return getAllBytesAsString(aISP.getInputStream(), sCharset);
    }

    /**
   * Read all bytes from the passed input stream into a string.
   * 
   * @param aIS
   *        The input stream to read from. May be <code>null</code>.
   * @param sCharset
   *        The charset to use. May not be <code>null</code> .
   * @return The String or <code>null</code> if the input stream is
   *         <code>null</code>.
   */
    @Nullable
    public static String getAllBytesAsString(@Nullable @WillClose final InputStream aIS, @Nonnull @Nonempty final String sCharset) {
        if (StringHelper.hasNoText(sCharset)) throw new IllegalArgumentException("empty charset");
        if (aIS == null) return null;
        return getCopy(aIS).getAsString(sCharset);
    }

    /**
   * Pass the content of the given reader to the given writer. The reader and
   * the writer are automatically closed!
   * 
   * @param aReader
   *        The reader to read from. May be <code>null</code>. Automatically
   *        closed!
   * @param aWriter
   *        The writer to write to. May be <code>null</code>. Automatically
   *        closed!
   * @return <code>{@link ESuccess#SUCCESS}</code> if copying took place,
   *         <code>{@link ESuccess#FAILURE}</code> otherwise
   */
    @Nonnull
    public static ESuccess copyReaderToWriterAndCloseWriter(@WillClose @Nullable final Reader aReader, @WillClose @Nullable final Writer aWriter) {
        try {
            return copyReaderToWriter(aReader, aWriter);
        } finally {
            close(aWriter);
        }
    }

    /**
   * Pass the content of the given reader to the given writer. The reader is
   * automatically closed, whereas the writer stays open!
   * 
   * @param aReader
   *        The reader to read from. May be <code>null</code>. Automatically
   *        closed!
   * @param aWriter
   *        The writer to write to. May be <code>null</code>. Not automatically
   *        closed!
   * @return <code>{@link ESuccess#SUCCESS}</code> if copying took place,
   *         <code>{@link ESuccess#FAILURE}</code> otherwise
   */
    @Nonnull
    public static ESuccess copyReaderToWriter(@WillClose @Nullable final Reader aReader, @WillNotClose @Nullable final Writer aWriter) {
        return copyReaderToWriter(aReader, aWriter, (MutableLong) null);
    }

    /**
   * Pass the content of the given reader to the given writer. The reader is
   * automatically closed, whereas the writer stays open!
   * 
   * @param aReader
   *        The reader to read from. May be <code>null</code>. Automatically
   *        closed!
   * @param aWriter
   *        The writer to write to. May be <code>null</code>. Not automatically
   *        closed!
   * @param aCopyCharCount
   *        An optional mutable long object that will receive the total number
   *        of copied characters. Note: and optional old value is overwritten!
   * @return <code>{@link ESuccess#SUCCESS}</code> if copying took place,
   *         <code>{@link ESuccess#FAILURE}</code> otherwise
   */
    @Nonnull
    public static ESuccess copyReaderToWriter(@WillClose @Nullable final Reader aReader, @WillNotClose @Nullable final Writer aWriter, @Nullable final MutableLong aCopyCharCount) {
        return copyReaderToWriter(aReader, aWriter, new char[DEFAULT_BUFSIZE], aCopyCharCount);
    }

    /**
   * Pass the content of the given reader to the given writer. The reader is
   * automatically closed, whereas the writer stays open!
   * 
   * @param aReader
   *        The reader to read from. May be <code>null</code>. Automatically
   *        closed!
   * @param aWriter
   *        The writer to write to. May be <code>null</code>. Not automatically
   *        closed!
   * @param aBuffer
   *        The buffer to use. May not be <code>null</code>.
   * @return <code>{@link ESuccess#SUCCESS}</code> if copying took place,
   *         <code>{@link ESuccess#FAILURE}</code> otherwise
   */
    @Nonnull
    public static ESuccess copyReaderToWriter(@WillClose @Nullable final Reader aReader, @WillNotClose @Nullable final Writer aWriter, @Nonnull final char[] aBuffer) {
        return copyReaderToWriter(aReader, aWriter, aBuffer, null);
    }

    /**
   * Pass the content of the given reader to the given writer. The reader is
   * automatically closed, whereas the writer stays open!
   * 
   * @param aReader
   *        The reader to read from. May be <code>null</code>. Automatically
   *        closed!
   * @param aWriter
   *        The writer to write to. May be <code>null</code>. Not automatically
   *        closed!
   * @param aBuffer
   *        The buffer to use. May not be <code>null</code>.
   * @param aCopyCharCount
   *        An optional mutable long object that will receive the total number
   *        of copied characters. Note: and optional old value is overwritten!
   * @return <code>{@link ESuccess#SUCCESS}</code> if copying took place,
   *         <code>{@link ESuccess#FAILURE}</code> otherwise
   */
    @Nonnull
    public static ESuccess copyReaderToWriter(@WillClose @Nullable final Reader aReader, @WillNotClose @Nullable final Writer aWriter, @Nonnull @Nonempty final char[] aBuffer, @Nullable final MutableLong aCopyCharCount) {
        if (ArrayHelper.isEmpty(aBuffer)) throw new IllegalArgumentException("buffer is empty");
        try {
            if (aReader != null && aWriter != null) {
                final long nTotalCharsCopied = _copy(aReader, aWriter, aBuffer);
                s_aCharSizeHdl.addSize(nTotalCharsCopied);
                if (aCopyCharCount != null) aCopyCharCount.set(nTotalCharsCopied);
                return ESuccess.SUCCESS;
            }
        } catch (final IOException ex) {
            if (!isKnownEOFException(ex)) s_aLogger.error("Failed to copy from stream to stream", ex);
        } finally {
            close(aReader);
        }
        return ESuccess.FAILURE;
    }

    @Nonnull
    public static NonBlockingStringWriter getCopy(@Nonnull @WillClose final Reader aReader) {
        final NonBlockingStringWriter aWriter = new NonBlockingStringWriter(DEFAULT_BUFSIZE);
        copyReaderToWriterAndCloseWriter(aReader, aWriter);
        return aWriter;
    }

    /**
   * Read all characters from the passed reader into a char array.
   * 
   * @param aReader
   *        The reader to read from. May be <code>null</code>.
   * @return The character array or <code>null</code> if the reader is
   *         <code>null</code>.
   */
    @Nullable
    public static char[] getAllCharacters(@Nullable @WillClose final Reader aReader) {
        if (aReader == null) return null;
        return getCopy(aReader).getAsCharArray();
    }

    /**
   * Read all characters from the passed reader into a String.
   * 
   * @param aReader
   *        The reader to read from. May be <code>null</code>.
   * @return The character array or <code>null</code> if the reader is
   *         <code>null</code>.
   */
    @Nullable
    public static String getAllCharactersAsString(@Nullable @WillClose final Reader aReader) {
        if (aReader == null) return null;
        return getCopy(aReader).toString();
    }

    /**
   * Get the content of the passed Spring resource as one big string in the
   * passed character set.
   * 
   * @param aISP
   *        The resource to read. May not be <code>null</code>.
   * @param sCharset
   *        The character set to use. May not be <code>null</code>.
   * @return <code>null</code> if the resolved input stream is <code>null</code>
   *         , the content otherwise.
   */
    @Nullable
    @ReturnsMutableCopy
    public static List<String> readStreamLines(@Nullable final IInputStreamProvider aISP, @Nonnull @Nonempty final String sCharset) {
        return readStreamLines(aISP, sCharset, 0, CGlobal.ILLEGAL_UINT);
    }

    /**
   * Get the content of the passed Spring resource as one big string in the
   * passed character set.
   * 
   * @param aISP
   *        The resource to read. May be <code>null</code>.
   * @param sCharset
   *        The character set to use. May not be <code>null</code>.
   * @param nLinesToSkip
   *        The 0-based index of the first line to read. Pass in 0 to indicate
   *        to read everything.
   * @param nLinesToRead
   *        The number of lines to read. Pass in {@link CGlobal#ILLEGAL_UINT} to
   *        indicate that all lines should be read. If the number passed here
   *        exceeds the number of lines in the file, nothing happens.
   * @return <code>null</code> if the resolved input stream is <code>null</code>
   *         , the content otherwise.
   */
    @Nullable
    @ReturnsMutableCopy
    public static List<String> readStreamLines(@Nullable final IInputStreamProvider aISP, @Nonnull @Nonempty final String sCharset, @Nonnegative final int nLinesToSkip, final int nLinesToRead) {
        if (aISP == null) return null;
        return readStreamLines(aISP.getInputStream(), sCharset, nLinesToSkip, nLinesToRead);
    }

    /**
   * Get the content of the passed stream as a list of lines in the passed
   * character set.
   * 
   * @param aIS
   *        The input stream to read from. May be <code>null</code>.
   * @param sCharset
   *        The character set to use. May not be <code>null</code>.
   * @return <code>null</code> if the input stream is <code>null</code>, the
   *         content lines otherwise.
   */
    @Nullable
    @ReturnsMutableCopy
    public static List<String> readStreamLines(@WillClose @Nullable final InputStream aIS, @Nonnull @Nonempty final String sCharset) {
        return readStreamLines(aIS, sCharset, 0, CGlobal.ILLEGAL_UINT);
    }

    /**
   * Get the content of the passed stream as a list of lines in the passed
   * character set.
   * 
   * @param aIS
   *        The input stream to read from. May be <code>null</code>.
   * @param sCharset
   *        The character set to use. May not be <code>null</code>.
   * @param aTargetList
   *        The list to be filled with the lines. May not be <code>null</code>.
   */
    @Nullable
    @ReturnsMutableCopy
    public static void readStreamLines(@WillClose @Nullable final InputStream aIS, @Nonnull @Nonempty final String sCharset, @Nonnull final List<String> aTargetList) {
        if (aIS != null) readStreamLines(aIS, sCharset, 0, CGlobal.ILLEGAL_UINT, new INonThrowingRunnableWithParameter<String>() {

            public void run(final String sLine) {
                aTargetList.add(sLine);
            }
        });
    }

    /**
   * Get the content of the passed stream as a list of lines in the passed
   * character set.
   * 
   * @param aIS
   *        The input stream to read from. May be <code>null</code>.
   * @param sCharset
   *        The character set to use. May not be <code>null</code>.
   * @param nLinesToSkip
   *        The 0-based index of the first line to read. Pass in 0 to indicate
   *        to read everything.
   * @param nLinesToRead
   *        The number of lines to read. Pass in {@link CGlobal#ILLEGAL_UINT} to
   *        indicate that all lines should be read. If the number passed here
   *        exceeds the number of lines in the file, nothing happens.
   * @return <code>null</code> if the input stream is <code>null</code>, the
   *         content lines otherwise.
   */
    @Nullable
    @ReturnsMutableCopy
    public static List<String> readStreamLines(@WillClose @Nullable final InputStream aIS, @Nonnull @Nonempty final String sCharset, @Nonnegative final int nLinesToSkip, final int nLinesToRead) {
        if (aIS == null) return null;
        final List<String> ret = new ArrayList<String>();
        readStreamLines(aIS, sCharset, nLinesToSkip, nLinesToRead, new INonThrowingRunnableWithParameter<String>() {

            public void run(final String sLine) {
                ret.add(sLine);
            }
        });
        return ret;
    }

    /**
   * Read the complete content of the passed stream and pass each line
   * separately to the passed callback.
   * 
   * @param aIS
   *        The input stream to read from. May be <code>null</code>.
   * @param sCharset
   *        The character set to use. May not be <code>null</code>.
   * @param aLineCallback
   *        The callback that is invoked for all read lines. Each passed line
   *        does NOT contain the line delimiter!
   */
    public static void readStreamLines(@WillClose @Nullable final InputStream aIS, @Nonnull @Nonempty final String sCharset, @Nonnull final INonThrowingRunnableWithParameter<String> aLineCallback) {
        readStreamLines(aIS, sCharset, 0, CGlobal.ILLEGAL_UINT, aLineCallback);
    }

    /**
   * Read the content of the passed stream line by line and invoking a callback
   * on all matching lines.
   * 
   * @param aIS
   *        The input stream to read from. May be <code>null</code>.
   * @param sCharset
   *        The character set to use. May not be <code>null</code>.
   * @param nLinesToSkip
   *        The 0-based index of the first line to read. Pass in 0 to indicate
   *        to read everything.
   * @param nLinesToRead
   *        The number of lines to read. Pass in {@link CGlobal#ILLEGAL_UINT} to
   *        indicate that all lines should be read. If the number passed here
   *        exceeds the number of lines in the file, nothing happens.
   * @param aLineCallback
   *        The callback that is invoked for all read lines. Each passed line
   *        does NOT contain the line delimiter! Note: it is not invoked for
   *        skipped lines!
   */
    public static void readStreamLines(@WillClose @Nullable final InputStream aIS, @Nonnull @Nonempty final String sCharset, @Nonnegative final int nLinesToSkip, final int nLinesToRead, @Nonnull final INonThrowingRunnableWithParameter<String> aLineCallback) {
        if (StringHelper.hasNoText(sCharset)) throw new IllegalArgumentException("Empty charset passed!");
        if (nLinesToSkip < 0) throw new IllegalArgumentException("First line may not be negative: " + nLinesToSkip);
        final boolean bReadAllLines = nLinesToRead == CGlobal.ILLEGAL_UINT;
        if (nLinesToRead < 0 && !bReadAllLines) throw new IllegalArgumentException("Line count may not be that negative: " + nLinesToRead);
        if (aLineCallback == null) throw new NullPointerException("lineCallback");
        if (aIS != null) try {
            if (bReadAllLines || nLinesToRead > 0) {
                BufferedReader aBR = null;
                try {
                    aBR = new BufferedReader(createReader(aIS, sCharset));
                    String sLine;
                    for (int i = 0; i < nLinesToSkip; ++i) if ((sLine = aBR.readLine()) == null) break;
                    if (bReadAllLines) {
                        while ((sLine = aBR.readLine()) != null) aLineCallback.run(sLine);
                    } else {
                        int nRead = 0;
                        while ((sLine = aBR.readLine()) != null) {
                            aLineCallback.run(sLine);
                            ++nRead;
                            if (nRead >= nLinesToRead) break;
                        }
                    }
                } catch (final IOException ex) {
                    s_aLogger.error("Failed to read from input stream", ex);
                } finally {
                    close(aBR);
                }
            }
        } finally {
            close(aIS);
        }
    }

    /**
   * Write bytes to an {@link OutputStream}.
   * 
   * @param aOS
   *        The output stream to write to. May not be <code>null</code>. Is
   *        closed independent of error or success.
   * @param aBuf
   *        The byte array from which is to be written. May not be
   *        <code>null</code>.
   * @param nOfs
   *        The 0-based index to the first byte in the array to be written. May
   *        not be &lt; 0.
   * @param nLen
   *        The non-negative amount of bytes to be written. May not be &lt; 0.
   * @return {@link ESuccess}
   */
    @Nonnull
    public static ESuccess writeStream(@WillClose @Nonnull final OutputStream aOS, @Nonnull final byte[] aBuf, @Nonnegative final int nOfs, @Nonnegative final int nLen) {
        if (aOS == null) throw new NullPointerException("outputStream");
        if (aBuf == null) throw new NullPointerException("content");
        if (nOfs < 0 || nLen < 0 || (nOfs + nLen) > aBuf.length) throw new IllegalArgumentException("ofs:" + nOfs + ";len=" + nLen + ";bufLen=" + aBuf.length);
        try {
            aOS.write(aBuf, nOfs, nLen);
            aOS.flush();
            return ESuccess.SUCCESS;
        } catch (final IOException ex) {
            s_aLogger.error("Failed to write to output stream", ex);
            return ESuccess.FAILURE;
        } finally {
            close(aOS);
        }
    }

    /**
   * Write bytes to an {@link OutputStream}.
   * 
   * @param aOS
   *        The output stream to write to. May not be <code>null</code>. Is
   *        closed independent of error or success.
   * @param aBuf
   *        The byte array to be written. May not be <code>null</code>.
   * @return {@link ESuccess}
   */
    @Nonnull
    public static ESuccess writeStream(@WillClose @Nonnull final OutputStream aOS, @Nonnull final byte[] aBuf) {
        return writeStream(aOS, aBuf, 0, aBuf.length);
    }

    /**
   * Write bytes to an {@link OutputStream}.
   * 
   * @param aOS
   *        The output stream to write to. May not be <code>null</code>. Is
   *        closed independent of error or success.
   * @param sContent
   *        The string to be written. May not be <code>null</code>.
   * @param sCharset
   *        The charset to be used, to convert the String to a byte array.
   * @return {@link ESuccess}
   */
    @Nonnull
    public static ESuccess writeStream(@WillClose @Nonnull final OutputStream aOS, @Nonnull final String sContent, @Nonnull final String sCharset) {
        if (sContent == null) throw new NullPointerException("content");
        if (sCharset == null) throw new NullPointerException("charset");
        return writeStream(aOS, CharsetManager.getAsBytes(sContent, sCharset));
    }

    @Nullable
    public static InputStreamReader createReader(@Nullable final InputStream aIS, @Nonnull final String sCharset) {
        try {
            return aIS == null ? null : new InputStreamReader(aIS, sCharset);
        } catch (final UnsupportedEncodingException ex) {
            throw new IllegalArgumentException("Failed to create Reader for charset '" + sCharset + "'", ex);
        }
    }

    @Nullable
    public static OutputStreamWriter createWriter(@Nullable final OutputStream aOS, @Nonnull final String sCharset) {
        try {
            return aOS == null ? null : new OutputStreamWriter(aOS, sCharset);
        } catch (final UnsupportedEncodingException ex) {
            throw new IllegalArgumentException("Failed to create Writer for charset '" + sCharset + "'", ex);
        }
    }

    /**
   * Fully skip the passed amounts in the input stream. Only forward skipping is
   * possible!
   * 
   * @param aIS
   *        The input stream to skip in.
   * @param nBytesToSkip
   *        The number of bytes to skip. Must be &ge; 0.
   * @throws IOException
   *         In case something goes wrong internally
   */
    public static void skipFully(@Nonnull final InputStream aIS, @Nonnegative final long nBytesToSkip) throws IOException {
        if (aIS == null) throw new NullPointerException("InputStream");
        if (nBytesToSkip < 0) throw new IllegalArgumentException("Cannot skip " + nBytesToSkip + " bytes. Only forward skip is possible!");
        long nRemaining = nBytesToSkip;
        while (nRemaining > 0) {
            final long nSkipped = aIS.skip(nRemaining);
            if (nSkipped == 0) {
                if (aIS.read() == -1) {
                    throw new EOFException("Failed to skip a total of " + nBytesToSkip + " bytes on input stream. Only skipped " + (nBytesToSkip - nRemaining) + " bytes so far!");
                }
                nRemaining--;
            } else {
                nRemaining -= nSkipped;
            }
        }
    }

    /**
   * Read the whole buffer from the input stream.
   * 
   * @param aIS
   *        The input stream to read from. May not be <code>null</code>.
   * @param aBuffer
   *        The buffer to read from. May not be <code>null</code>.
   * @throws IOException
   *         In case reading fails
   */
    public static void readFully(@Nonnull final InputStream aIS, @Nonnull final byte[] aBuffer) throws IOException {
        readFully(aIS, aBuffer, 0, aBuffer.length);
    }

    /**
   * Read the whole buffer from the input stream.
   * 
   * @param aIS
   *        The input stream to read from. May not be <code>null</code>.
   * @param aBuffer
   *        The buffer to read from. May not be <code>null</code>.
   * @param nOfs
   *        The offset into the destination buffer to use. May not be &lt; 0.
   * @param nLen
   *        The number of bytes to read into the destination buffer to use. May
   *        not be &lt; 0.
   * @throws IOException
   *         In case reading fails
   */
    public static void readFully(@Nonnull final InputStream aIS, @Nonnull final byte[] aBuffer, @Nonnegative final int nOfs, @Nonnegative final int nLen) throws IOException {
        if (aIS == null) throw new NullPointerException("inputStream");
        if (nOfs < 0 || nLen < 0 || (nOfs + nLen) > aBuffer.length) throw new IllegalArgumentException("ofs:" + nOfs + ";len=" + nLen + ";bufLen=" + aBuffer.length);
        int nTotalBytesRead = 0;
        while (nTotalBytesRead < nLen) {
            final int nBytesRead = aIS.read(aBuffer, nOfs + nTotalBytesRead, nLen - nTotalBytesRead);
            if (nBytesRead < 0) throw new EOFException("Failed to read a total of " + nLen + " bytes from input stream. Only read " + nTotalBytesRead + " bytes so far.");
            nTotalBytesRead += nBytesRead;
        }
    }
}
