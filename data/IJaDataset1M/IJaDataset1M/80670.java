package org.makagiga.commons.sb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is @b not thread-safe.
 * 
 * @since 2.0
 */
public final class Hash implements Serializable {

    public static final String BLACKLIST = "goog-black-hash";

    public static final String MALWARE = "goog-malware-hash";

    private int majorVersion = 1;

    private int minorVersion = NO_VERSION;

    private static final int NO_VERSION = -1;

    private List<String> list;

    private String type;

    /**
	 * Returns the major version number.
	 * 
	 * "The major version is currently 1, and is used to describe the wire
	 * format for serializing the lists (...)"
	 * 
	 * @return -1 If no hash
	 */
    public int getMajorVersion() {
        return majorVersion;
    }

    /**
	 * Returns the minor version number.
	 * 
	 * "The minor number indicates the version of the list. When we (Google) add
	 * or remove items from a list, we increment its minor version number."
	 * 
	 * @return -1 If no hash
	 */
    public int getMinorVersion() {
        return minorVersion;
    }

    /**
	 * Returns the type of this hash.
	 * 
	 * Valid return values:
	 * - @c null - Unknown type (no hash)
	 * - "goog-black-hash"
	 * - "goog-malware-hash"
	 */
    public String getType() {
        return type;
    }

    /**
	 * Returns a string in the following format:
	 * &lt;type&gt;:&lt;majorVersion&gt;:&lt;minorVersion&gt;
	 * 
	 * Example: "goog-black-hash:1:1024"
	 */
    @Override
    public String toString() {
        return type + ":" + majorVersion + ":" + minorVersion;
    }

    Hash(final int initialCapacity) {
        list = new ArrayList<String>(initialCapacity);
    }

    int getSize() {
        return list.size();
    }

    /**
	 * "The header line will be followed by data lines
	 * which begin with a + or -.
	 * A plus indicates an addition to the table and is followed by a
	 * tab-separated key/value pair. A minus means to remove a key from the
	 * table and is followed by the key itself."
	 */
    void merge(final String line) {
        String md5 = line.substring(1, line.length() - 1);
        switch(line.charAt(0)) {
            case '+':
                list.add(md5);
                break;
            case '-':
                list.remove(md5);
                break;
        }
    }

    void setInfo(final String type, final int majorVersion, final int minorVersion) {
        this.type = type;
        this.majorVersion = majorVersion;
        this.minorVersion = minorVersion;
    }
}
