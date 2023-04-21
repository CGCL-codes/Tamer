package io;

import java.io.*;
import world.Element;
import world.terrain.Terrain;
import world.resource.Resource;

/**
 * writes elements
 * @author Jack
 *
 */
public final class ElementWriter {

    /**
	 * writes the passed element to the stream, 
	 * writes an ID representing the type of element
	 * that is being written and other pertinant
	 * information
	 * @param e the element to be written
	 * @param dos the stream that the element is written to
	 * @throws IOException
	 */
    public static void writeElement(Element e, DataOutputStream dos) throws IOException {
        if (e.getClass().getSuperclass() == Terrain.class) {
            dos.writeInt(IOConstants.terrain);
        } else if (e.getClass().getSuperclass() == Resource.class) {
            dos.writeInt(IOConstants.resource);
        }
        dos.writeInt(e.getName().length());
        dos.writeChars(e.getName());
        dos.writeDouble(e.getLocation().x);
        dos.writeDouble(e.getLocation().y);
        dos.writeInt(e.getWidth());
        dos.writeInt(e.getHeight());
    }
}
