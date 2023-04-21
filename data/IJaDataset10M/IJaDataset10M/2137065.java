package org.openxml4j.opc;

/**
 * Specify package access.
 * 
 * @author Julien Chable
 * 
 * @version 1.0
 */
public class PackageAccess implements Comparable {

    private static int nextOrdinal = 0;

    private final int ordinal = nextOrdinal++;

    private final String name;

    private PackageAccess(String name) {
        this.name = name;
    }

    public int compareTo(Object o) {
        return ordinal - ((PackageAccess) o).ordinal;
    }

    public String toString() {
        return name;
    }

    public static final PackageAccess Read = new PackageAccess("Read");

    public static final PackageAccess Write = new PackageAccess("Write");

    public static final PackageAccess ReadWrite = new PackageAccess("ReadWrite");
}
