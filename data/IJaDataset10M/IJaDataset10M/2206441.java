package com.sun.org.apache.bcel.internal.classfile;

/** 
 * Thrown when the BCEL attempts to read a class file and determines
 * that the file is malformed or otherwise cannot be interpreted as a
 * class file.
 *
 * @version $Id: ClassFormatException.java,v 1.1.2.1 2005/07/31 23:46:20 jeffsuttor Exp $
 * @author  <A HREF="mailto:markus.dahm@berlin.de">M. Dahm</A>
 */
public class ClassFormatException extends RuntimeException {

    public ClassFormatException() {
        super();
    }

    public ClassFormatException(String s) {
        super(s);
    }
}
