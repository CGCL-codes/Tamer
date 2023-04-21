package geometry.serialization.esri;

import java.io.File;
import java.io.IOException;
import com.bbn.openmap.layer.shape.ShapeFile;

/**
 * @author Sebastian Kuerten (sebastian.kuerten@fu-berlin.de)
 * 
 *         An extension of the ShapeFile that permits random access to records.
 */
public class SeekableShapefile extends ShapeFile {

    /**
	 * Create a new seekable shapefile for the denoted filename.
	 * 
	 * @param name
	 *            the file to open
	 * @throws IOException
	 *             on I/O failure.
	 */
    public SeekableShapefile(String name) throws IOException {
        super(name);
    }

    /**
	 * Create a new seekable shapefile for the denoted filename.
	 * 
	 * @param file
	 *            the file to open
	 * @throws IOException
	 *             on I/O failure.
	 */
    public SeekableShapefile(File file) throws IOException {
        super(file);
    }

    /**
	 * Seek to the denoted position within the underlying file.
	 * 
	 * @param position
	 *            the position in 16 bit words.
	 * @throws IOException
	 *             on I/O failure.
	 */
    public void seekWord(long position) throws IOException {
        seek(position * 2);
    }

    /**
	 * Seek to the denoted position within the underlying file.
	 * 
	 * @param position
	 *            the position in bytes.
	 * @throws IOException
	 *             on I/O failure.
	 */
    public void seek(long position) throws IOException {
        raf.seek(position);
    }
}
