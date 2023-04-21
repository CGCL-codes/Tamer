package net.kodeninja.DMAP;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import net.kodeninja.DMAP.DataTypes.DMAPParameter;

/**
 * Special class that parses a stream of DMAP tags and returns a heirachical structure.
 * @author Charles Ikeson
 *
 */
public class ParameterFactory {

    /**
	 * The default path for the dmap tags (general).
	 */
    public static final String CLASS_PACKAGE_DMAP = "net.kodeninja.DMAP.parameters.dmap";

    /**
	 * The default path for the daap tags (audio/video).
	 */
    public static final String CLASS_PACKAGE_DAAP = "net.kodeninja.DMAP.parameters.daap";

    /**
	 * The default path for the dpap tags(images).
	 */
    public static final String CLASS_PACKAGE_DPAP = "net.kodeninja.DMAP.parameters.dpap";

    /**
	 * The default path for the apple extension tags.
	 */
    public static final String CLASS_PACKAGE_APPLE_ITUNES = "net.kodeninja.DMAP.parameters.com.apple.itunes";

    /**
	 * Linked list of the paths to search when encountering a tag.
	 */
    protected static Set<String> searchPaths = new LinkedHashSet<String>();

    /**
	 * Adds a 'search path' class package prefix for a tag it attempts to lookup
	 * and load.
	 * @param path The path to add to the search paths.
	 */
    public static void addSearchPath(String path) {
        searchPaths.add(path);
    }

    /**
	 * Adds the default search paths: net.kodeninja.DMAP.parameters.DMAP
	 * net.kodeninja.DMAP.parameters.DAAP
	 */
    public static void addDefaultSearchPaths() {
        addSearchPath(CLASS_PACKAGE_DMAP);
        addSearchPath(CLASS_PACKAGE_DAAP);
        addSearchPath(CLASS_PACKAGE_DPAP);
        addSearchPath(CLASS_PACKAGE_APPLE_ITUNES);
    }

    /**
	 * Takes an empty list of parameters and fills it with the ones it finds on
	 * the inputstream up to the passed length.
	 *
	 * @param list
	 *            The list of parameters to populate.
	 * @param in
	 *            The input stream to read from.
	 * @param ContentLength
	 *            The length to read.
	 * @throws IOException
	 *             On any IO error or if it encounters an unknown/unsupported
	 *             tag.
	 */
    public static void readFromStream(ParameterList list, InputStream in, long ContentLength) throws IOException {
        int pos = 0;
        byte[] tagBuf = new byte[4];
        byte[] sizeBuf = new byte[4];
        if (searchPaths.isEmpty()) addDefaultSearchPaths();
        while ((in.available() > 0) && ((ContentLength == -1) || (pos < ContentLength - 1))) {
            String tag, classname = "UNDEFINED";
            boolean classFound = false;
            int size;
            Object o;
            DMAPParameter p = null;
            ByteBuffer iBuf = ByteBuffer.wrap(sizeBuf);
            iBuf.order(ByteOrder.BIG_ENDIAN);
            in.read(tagBuf);
            in.read(sizeBuf);
            size = iBuf.getInt();
            tag = new String(tagBuf);
            Iterator<String> it = searchPaths.iterator();
            while (it.hasNext()) {
                classname = it.next() + "." + tag;
                try {
                    o = ClassLoader.getSystemClassLoader().loadClass(classname).newInstance();
                    if (o instanceof DMAPParameter) p = (DMAPParameter) o; else throw new IOException("Class '" + classname + "' does not inherit net.kodeninja.DMAP.DMAPParameter");
                } catch (ClassNotFoundException e) {
                    continue;
                } catch (IOException e) {
                    throw e;
                } catch (Exception e) {
                    throw new IOException("Error loading class '" + classname + "'");
                }
                classFound = true;
                break;
            }
            if (classFound == false) p = new UnknownParameter();
            p.readDataFromStream(in, size);
            list.addParameter(p);
            pos += 8 + size;
        }
    }
}
