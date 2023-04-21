package com.sun.jini.mercury;

import java.io.File;

/**
 * Class used as the key value for an associated <tt>LogStream</tt> object in a
 * collection of type <tt>java.util.Map</tt>.
 * 
 * @author Sun Microsystems, Inc.
 * 
 * @since 1.1
 */
class StreamKey {

    /** Holds the <tt>File</tt> attribute for the associated stream. */
    private File file;

    /** Holds the "enumeration" type for the associated stream. */
    private StreamType type;

    /** Holds the cached value of the <tt>file</tt> field's hashCode. */
    private int hash;

    /**
	 * Simple constructor that accepts <tt>File</tt> and <tt>StreamType</tt>
	 * arguments and then assigns them to the appropriate internal fields.
	 * Neither argument can be <code>null</code>. The <tt>File</tt> argument
	 * must represent an absolute pathname.
	 * 
	 * @exception IllegalArgumentException
	 *                thrown if either of the arguments are null or if the
	 *                <tt>file</tt> argument does not represent an absolute
	 *                pathname.
	 */
    StreamKey(File file, StreamType type) {
        if (file == null || type == null) throw new IllegalArgumentException("Cannot use <null> for " + "path or type arguments.");
        if (!file.isAbsolute()) throw new IllegalArgumentException("Cannot use a relative path " + "for the <file> argument");
        this.file = file;
        this.type = type;
        this.hash = file.hashCode();
    }

    public int hashCode() {
        return hash;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (o.getClass() != getClass()) return false;
        StreamKey sk = (StreamKey) o;
        return ((type == sk.type) && (file.equals(sk.file)));
    }
}
