package com.mojang.joxsi.loader;

import java.io.Serializable;

/**
 * The header of a dotXSI file.
 * @author Notch
 * @author Egal
 */
public class Header implements Serializable {

    /** Version/major number. */
    public int majorVersion;

    /** Version/minor number. */
    public int minorVersion;

    /** Format type; txt or bin. */
    public String formatType;

    /** Compression type; zip. */
    public String compressionType;

    /** Float size; 32 or 64bit floats. */
    public int floatSize;

    /** String that reprsents the Text dotXSI format type. */
    public static final String TEXT_FORMAT_TYPE = "txt";
}
