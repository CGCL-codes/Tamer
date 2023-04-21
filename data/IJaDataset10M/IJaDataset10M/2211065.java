package org.javaseis.io;

import java.io.File;

/**
 * This class represents a folder that can be used for virtual IO.  
 * 
 * @author Steve Angelovich
 *
 */
public class VirtualFolder {

    private String _path;

    private Attribute _attrib;

    public enum Attribute {

        READ_ONLY, READ_WRITE, OVERFLOW_ONLY, RETIRED
    }

    ;

    public VirtualFolder(String path) {
        if (path == null || path.length() < 1) throw new IllegalArgumentException("A valid path must be specified");
        String[] parts = path.split(",");
        _path = parts[0];
        if (parts.length == 2) _attrib = Attribute.valueOf(parts[1]); else _attrib = Attribute.READ_WRITE;
        if (_path.charAt(_path.length() - 1) == File.separatorChar) _path = _path.substring(0, _path.length() - 1);
    }

    public void addElement(String item) {
        _path += File.separatorChar + item;
    }

    public void setPath(String path) {
        _path = path;
    }

    public String getPath() {
        return _path;
    }

    public Attribute getAttribute() {
        return _attrib;
    }

    public String toString() {
        return _path + "," + _attrib.toString();
    }

    public boolean isReadOnly() {
        return _attrib == Attribute.READ_ONLY;
    }

    public boolean isOverFlowOnly() {
        return _attrib == Attribute.OVERFLOW_ONLY;
    }

    public boolean isRetired() {
        return _attrib == Attribute.RETIRED;
    }

    public boolean isWriteable() {
        return _attrib == Attribute.READ_WRITE;
    }

    public void setAttribute(Attribute attrib) {
        _attrib = attrib;
    }

    /**
   * Currently equals is only looking at the path information
   * and not other attributes such as RO.
   */
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof VirtualFolder)) return false;
        if (_path.compareToIgnoreCase(((VirtualFolder) obj).getPath()) == 0) return true;
        return false;
    }

    /** Helper method to see if the folder is empty **/
    public int count() {
        File f = new File(_path);
        if (f.exists() == false) return 0;
        String[] stuff = f.list();
        if (stuff != null && stuff.length > 0) return stuff.length;
        return 0;
    }
}
