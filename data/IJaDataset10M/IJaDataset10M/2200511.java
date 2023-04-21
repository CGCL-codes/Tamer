package joeq.ClassLib.sun13_linux.java.util.zip;

/**
 * ZipFile
 *
 * @author  John Whaley <jwhaley@alum.mit.edu>
 * @version $Id: ZipFile.java 1451 2004-03-09 06:27:08Z jwhaley $
 */
public abstract class ZipFile {

    private String name;

    private java.util.Vector inflaters;

    private java.io.RandomAccessFile raf;

    private java.util.Hashtable entries;

    public void __init__(String name) throws java.io.IOException {
        this.name = name;
        java.io.RandomAccessFile raf = new java.io.RandomAccessFile(name, "r");
        this.raf = raf;
        this.inflaters = new java.util.Vector();
        this.readCEN();
    }

    private native void readCEN() throws java.io.IOException;
}
