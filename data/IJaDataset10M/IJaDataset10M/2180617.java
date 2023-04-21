package com.kreative.rsrc;

import java.awt.*;
import java.io.*;
import com.kreative.ksfl.KSFLConstants;

/**
 * The <code>ScaleableFontResource</code> class represents a scaleable font resource.
 * @since KSFL 1.0
 * @author Rebecca G. Bettencourt, Kreative Software
 */
public class ScaleableFontResource extends MacResource {

    /**
	 * The resource type of a scaleable font resource,
	 * the four-character constant <code>sfnt</code>.
	 */
    public static final int RESOURCE_TYPE = KSFLConstants.sfnt;

    /**
	 * Checks if a resource type is one this class knows how to handle.
	 * The default implementation is to always return true.
	 * It is recommended that subclasses override this.
	 * @param type A resource type to check.
	 * @return True if this class can handle this resource type, false otherwise.
	 */
    public static boolean isMyType(int type) {
        return (type == RESOURCE_TYPE);
    }

    /**
	 * Constructs a new resource of type <code>sfnt</code> with the specified ID and data.
	 * The name is set to an empty string, and all attributes are cleared.
	 * @param id The resource ID.
	 * @param data The resource data.
	 */
    public ScaleableFontResource(short id, byte[] data) {
        super(RESOURCE_TYPE, id, data);
    }

    /**
	 * Constructs a new resource of type <code>sfnt</code> with the specified ID, name, and data.
	 * All attributes are cleared.
	 * @param id The resource ID.
	 * @param name The resource name.
	 * @param data The resource data.
	 */
    public ScaleableFontResource(short id, String name, byte[] data) {
        super(RESOURCE_TYPE, id, name, data);
    }

    /**
	 * Constructs a new resource of type <code>sfnt</code> with the specified ID, attributes, and data.
	 * The name is set to an empty string.
	 * @param id The resource ID.
	 * @param attr The resource attributes as a byte.
	 * @param data The resource data.
	 */
    public ScaleableFontResource(short id, byte attr, byte[] data) {
        super(RESOURCE_TYPE, id, attr, data);
    }

    /**
	 * Constructs a new resource of type <code>sfnt</code> with the specified ID, attributes, name, and data.
	 * @param id The resource ID.
	 * @param attr The resource attributes as a byte.
	 * @param name The resource name.
	 * @param data The resource data.
	 */
    public ScaleableFontResource(short id, byte attr, String name, byte[] data) {
        super(RESOURCE_TYPE, id, attr, name, data);
    }

    /**
	 * Constructs a new resource with the specified type, ID, and data.
	 * The name is set to an empty string, and all attributes are cleared.
	 * @param type The resource type as an integer.
	 * @param id The resource ID.
	 * @param data The resource data.
	 */
    public ScaleableFontResource(int type, short id, byte[] data) {
        super(type, id, data);
    }

    /**
	 * Constructs a new resource with the specified type, ID, name, and data.
	 * All attributes are cleared.
	 * @param type The resource type as an integer.
	 * @param id The resource ID.
	 * @param name The resource name.
	 * @param data The resource data.
	 */
    public ScaleableFontResource(int type, short id, String name, byte[] data) {
        super(type, id, name, data);
    }

    /**
	 * Constructs a new resource with the specified type, ID, attributes, and data.
	 * The name is set to an empty string.
	 * @param type The resource type as an integer.
	 * @param id The resource ID.
	 * @param attr The resource attributes as a byte.
	 * @param data The resource data.
	 */
    public ScaleableFontResource(int type, short id, byte attr, byte[] data) {
        super(type, id, attr, data);
    }

    /**
	 * Constructs a new resource with the specified type, ID, attributes, name, and data.
	 * @param type The resource type as an integer.
	 * @param id The resource ID.
	 * @param attr The resource attributes as a byte.
	 * @param name The resource name.
	 * @param data The resource data.
	 */
    public ScaleableFontResource(int type, short id, byte attr, String name, byte[] data) {
        super(type, id, attr, name, data);
    }

    /**
	 * Returns this resource as an AWT font object.
	 * If the data is improperly formed, returns null.
	 * @return this resource as an AWT font object.
	 */
    public Font toFont() {
        try {
            return Font.createFont(Font.TRUETYPE_FONT, new ByteArrayInputStream(data));
        } catch (IOException ioe) {
            return null;
        } catch (FontFormatException ffe) {
            return null;
        }
    }
}
