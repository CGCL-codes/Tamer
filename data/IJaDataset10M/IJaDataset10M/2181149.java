package com.googlecode.lawu.util;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

/**
 * Utility methods for manipulating streams and "streamable" objects.
 * 
 * @author Miorel-Lucian Palii
 */
public class Streams {

    /**
	 * There is no need to instantiate this class.
	 */
    private Streams() {
    }

    /**
	 * Copies a file.
	 * 
	 * @param source
	 *            the file to copy
	 * @param destination
	 *            where to copy to
	 * @throws FileNotFoundException
	 *             if the source cannot be opened for reading or the destination
	 *             cannot be opened for writing
	 * @throws IOException
	 *             if another I/O error occurs
	 * @see #copy(InputStream, File)
	 */
    public static void copy(File source, File destination) throws FileNotFoundException, IOException {
        if (source == null) throw new NullPointerException("The source may not be null.");
        if (destination == null) throw new NullPointerException("The destination may not be null.");
        FileInputStream sourceStream = new FileInputStream(source);
        destination.getParentFile().mkdirs();
        FileOutputStream destStream = new FileOutputStream(destination);
        try {
            FileChannel sourceChannel = sourceStream.getChannel();
            FileChannel destChannel = destStream.getChannel();
            destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
        } finally {
            try {
                sourceStream.close();
                destStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
	 * Copies up to the specified number of bytes from a stream to a file.
	 * 
	 * @param source
	 *            the stream to read from
	 * @param destination
	 *            where to write to
	 * @param size
	 *            the maximum number of bytes to copy
	 * @throws IOException
	 *             if an I/O error occurs
	 * @see #copy(InputStream, File)
	 */
    public static void copy(InputStream source, File destination, long size) throws IOException {
        if (source == null) throw new NullPointerException("The source may not be null.");
        if (destination == null) throw new NullPointerException("The destination may not be null.");
        if (size < 0) throw new IllegalArgumentException("The size may not be negative.");
        ReadableByteChannel sourceChannel = Channels.newChannel(source);
        try {
            destination.getParentFile().mkdirs();
            destination.getAbsoluteFile().getParentFile().mkdirs();
        } catch (Exception e) {
        }
        FileOutputStream destStream = new FileOutputStream(destination);
        try {
            FileChannel destChannel = destStream.getChannel();
            destChannel.transferFrom(sourceChannel, 0, size);
        } finally {
            try {
                source.close();
                destStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
	 * Copies a stream to a file. This method is equivalent to
	 * <code>Streams.{@linkplain #copy(InputStream,File,long) copy}(source, destination, Long.MAX_VALUE)</code>.
	 * 
	 * @param source
	 *            the stream to read from
	 * @param destination
	 *            where to write to
	 * @throws IOException
	 *             if an I/O error occurs
	 * @see #copy(File, File)
	 */
    public static void copy(InputStream source, File destination) throws IOException {
        copy(source, destination, Long.MAX_VALUE);
    }

    /**
	 * Reads the entire contents of a stream into a buffer.
	 * 
	 * @param stream
	 *            the stream to read
	 * @return a buffer containing the contents of the stream
	 * @throws IOException
	 *             if an I/O error occurs
	 * @see Iterators#lines(InputStream)
	 * @see #slurp(File)
	 * @see #slurp(FileDescriptor)
	 * @see #slurp(Reader)
	 * @see #slurp(String)
	 * @see #slurp(URL)
	 */
    public static StringBuilder slurp(InputStream stream) throws IOException {
        return slurp(new InputStreamReader(stream));
    }

    /**
	 * Reads the entire contents of a <code>Reader</code> into a buffer.
	 * 
	 * @param reader
	 *            the <code>Reader</code> to read
	 * @return a buffer containing the contents of the <code>Reader</code>
	 * @throws IOException
	 *             if an I/O error occurs
	 * @see Iterators#lines(BufferedReader)
	 * @see Iterators#lines(Reader)
	 * @see #slurp(File)
	 * @see #slurp(FileDescriptor)
	 * @see #slurp(InputStream)
	 * @see #slurp(String)
	 * @see #slurp(URL)
	 */
    public static StringBuilder slurp(Reader reader) throws IOException {
        StringBuilder ret = new StringBuilder();
        char[] buf = new char[1 << 12];
        try {
            for (int n; (n = reader.read(buf)) != -1; ) ret.append(new String(buf, 0, n));
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    /**
	 * Reads the entire contents of a file into a buffer.
	 * 
	 * @param file
	 *            the file to read
	 * @return a buffer containing the contents of the file
	 * @throws FileNotFoundException
	 *             if the file cannot be opened for reading
	 * @throws IOException
	 *             if an I/O error occurs
	 * @see Iterators#lines(File)
	 * @see #slurp(FileDescriptor)
	 * @see #slurp(InputStream)
	 * @see #slurp(Reader)
	 * @see #slurp(String)
	 * @see #slurp(URL)
	 */
    public static StringBuilder slurp(File file) throws FileNotFoundException, IOException {
        return slurp(new FileInputStream(file));
    }

    /**
	 * Reads the entire contents of a file descriptor into a buffer.
	 * 
	 * @param fd
	 *            the file descriptor to read
	 * @return a buffer containing the contents of the file descriptor
	 * @throws IOException
	 *             if an I/O error occurs
	 * @see Iterators#lines(FileDescriptor)
	 * @see #slurp(File)
	 * @see #slurp(InputStream)
	 * @see #slurp(Reader)
	 * @see #slurp(String)
	 * @see #slurp(URL)
	 */
    public static StringBuilder slurp(FileDescriptor fd) throws IOException {
        return slurp(new FileInputStream(fd));
    }

    /**
	 * Reads the entire contents of a file into a buffer.
	 * 
	 * @param path
	 *            the path of the file to read
	 * @return a buffer containing the contents of the file
	 * @throws FileNotFoundException
	 *             if the file cannot be opened for reading
	 * @throws IOException
	 *             if an I/O error occurs
	 * @see #slurp(File)
	 * @see #slurp(FileDescriptor)
	 * @see #slurp(InputStream)
	 * @see #slurp(Reader)
	 * @see #slurp(URL)
	 */
    public static StringBuilder slurp(String path) throws FileNotFoundException, IOException {
        return slurp(new FileInputStream(path));
    }

    /**
	 * Reads the entire contents of a resource into a buffer.
	 * 
	 * @param url
	 *            address of the resource to read
	 * @return a buffer containing the contents of the resource
	 * @throws IOException
	 *             if an I/O error occurs
	 * @see Iterators#lines(URL)
	 * @see #slurp(File)
	 * @see #slurp(FileDescriptor)
	 * @see #slurp(InputStream)
	 * @see #slurp(Reader)
	 * @see #slurp(String)
	 */
    public static StringBuilder slurp(URL url) throws IOException {
        return slurp(url.openStream());
    }
}
