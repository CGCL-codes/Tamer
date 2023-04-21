package xxl.core.io.converters;

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import xxl.core.io.DataInputInputStream;
import xxl.core.io.DataOutputOutputStream;

/**
 * This class provides a converter that converts a byte array into a zip
 * compressed byte array representation and vice versa. Only big byte arrays
 * (>256 Bytes) are worth compressing!
 *
 * @see DataInput
 * @see DataOutput
 * @see IOException
 */
public class ByteArrayZipConverter extends Converter<byte[]> {

    /**
	 * This instance can be used for getting a default instance of a byte array
	 * zip converter. It is similar to the <i>Singleton Design Pattern</i> (for
	 * further details see Creational Patterns, Prototype in <i>Design
	 * Patterns: Elements of Reusable Object-Oriented Software</i> by Erich
	 * Gamma, Richard Helm, Ralph Johnson, and John Vlissides) except that
	 * there are no mechanisms to avoid the creation of other instances of a
	 * byte array zip converter.
	 */
    public static final ByteArrayZipConverter DEFAULT_INSTANCE = new ByteArrayZipConverter();

    /**
	 * Reads in a zip compressed byte array for the specified from the
	 * specified data input and returns the decompressed byte array.
	 * 
	 * <p>When the specified <code>byte</code> array is <code>null</code> or
	 * the size of the specified byte array is not sufficient, this
	 * implementation returns a new array of <code>byte</code> values.</p>
	 *
	 * @param dataInput the stream to read a string from in order to return an
	 *        decompressed byte array.
	 * @param object the byte array to be decompressed.
	 * @return the read decompressed byte array.
	 * @throws IOException if I/O errors occur.
	 */
    @Override
    public byte[] read(DataInput dataInput, byte[] object) throws IOException {
        ZipInputStream zis = new ZipInputStream(new DataInputInputStream(dataInput));
        zis.getNextEntry();
        if (object != null && zis.read(object) < object.length) return object;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (object != null) baos.write(object);
        if (object == null || object.length < 512) object = new byte[512];
        int read;
        do {
            read = zis.read(object);
            baos.write(object, 0, Math.max(0, read));
        } while (read == object.length);
        return baos.toByteArray();
    }

    /**
	 * Writes the specified <code>byte</code> array compressed to the specified
	 * data output.
	 *
	 * @param dataOutput the stream to write the string representation of the
	 *        specified object to.
	 * @param object the byte array object that string representation should be
	 *        written to the data output.
	 * @throws IOException includes any I/O exceptions that may occur.
	 */
    @Override
    public void write(DataOutput dataOutput, byte[] object) throws IOException {
        ZipOutputStream zos = new ZipOutputStream(new DataOutputOutputStream(dataOutput));
        zos.setMethod(ZipOutputStream.DEFLATED);
        zos.putNextEntry(new ZipEntry("a"));
        zos.write(object, 0, object.length);
        zos.finish();
        zos.close();
    }
}
