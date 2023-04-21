package com.sun.jini.mercury;

/**
 * Class that serves the purpose of an enumeration type. It's used in
 * conjunction with <tt>StreamKey</tt> in order to distinguish among different
 * <tt>LogStream</tt> objects that can potentially act upon the same underlying
 * log file.
 * 
 * @author Sun Microsystems, Inc.
 * 
 * @since 1.1
 */
class StreamType {

    /**
	 * Simple constructor. Private protection is used in order to prevent
	 * external instantiation of this class type.
	 */
    private StreamType() {
        ;
    }

    /** Event data output stream enumeration */
    static final StreamType OUTPUT = new StreamType();

    /** Event data input stream enumeration */
    static final StreamType INPUT = new StreamType();

    /**
	 * Control data stream enumeration. Control data will not use separate
	 * input/output data streams and therefore no distinction is made.
	 */
    static final StreamType CONTROL = new StreamType();
}
