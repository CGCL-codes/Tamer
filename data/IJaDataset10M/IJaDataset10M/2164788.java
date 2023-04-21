package ru.sitekeeper.cpn;

/**
 * 
 * @author $Author: alx27 $
 * @version $Id: ClassVersionNumber.java,v 1.1 2008/08/03 08:04:47 alx27 Exp $
 *
 */
public class ClassVersionNumber {

    public static final ClassVersionNumber NOT_A_CLASS = new ClassVersionNumber();

    ClassVersionNumber(int _major, int _minor) {
        this.major = _major;
        this.minor = _minor;
    }

    ClassVersionNumber() {
        this(0, 0);
    }

    public final int major;

    public final int minor;

    @Override
    public String toString() {
        return "" + major + "." + minor;
    }
}
