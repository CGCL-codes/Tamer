package xmage.turbine.codec;

import xmage.turbine.Node;
import java.io.File;
import java.io.OutputStream;
import java.io.IOException;

/**
 * This interface is a superclass of all geometry file format codecs that write data.
 */
public interface GeometryOutputCodec {

    /**
	 * Write geometry data to file.
	 *
	 * @param node graph node to write
	 * @param filename file name to write data to
	 * @throws IOException when file is unwritable
	 */
    void write(Node node, String filename) throws IOException;

    /**
	 * Write geometry data to file.
	 *
	 * @param node graph node to write
	 * @param file file to write data to
	 * @throws IOException when file is unwritable
	 */
    void write(Node node, File file) throws IOException;

    /**
	 * Write geometry data to stream.
	 *
	 * @param node graph node to write
	 * @param stream stream to write data to
	 * @throws IOException when file is unwritable
	 */
    void write(Node node, OutputStream stream) throws IOException;
}
