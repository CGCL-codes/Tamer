package net.sf.jjmpeg.container;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Represents a very generic container, providing the methods that all container
 * types will need in order to operate properly. All container types must be a
 * child of this type.
 * 
 * @author Derek Whitaker & Trenton Pack
 * @version 1.0
 */
public interface Container {

    /**
	 * Initializes the container for reading. Once a container is initialized
	 * for reading, it can NOT be initialized for writing. Further
	 * initialization will do nothing.
	 * 
	 * @param in <CODE>InputStream</CODE> to read the container's data from
	 * @throws IOException if an IO error occurs in the input stream
	 */
    public void initializeForReading(InputStream in) throws IOException;

    /**
	 * Initializes the container for writing. Once a container is initialized
	 * for writing, it can NOT be initialized for reading. Further
	 * initialization will do nothing.
	 * 
	 * @param out <CODE>OutputStream</CODE> to send the container's data to
	 * @throws IOException if an IO error occurs in the output stream
	 */
    public void initializeForWriting(OutputStream out) throws IOException;

    /**
	 * Seek through the container to find the given frame. The first frame is
	 * indexed at <CODE>0</CODE>.
	 * 
	 * @param framenum the frame to go to
	 * @return if the frame was reach, then <CODE>true</CODE>; else
	 *         <CODE>false</CODE>
	 */
    boolean seekFrame(long framenum);

    /**
	 * Seek through the container for a given time code. If the time falls
	 * between frames, the next available full frame is used.
	 * 
	 * @param time the time code to go to
	 * @return if the time code was reached, then <CODE>true</CODE>; else
	 *         <CODE>false</CODE>
	 */
    boolean seekTime(long time);
}
