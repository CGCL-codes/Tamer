package de.uni_bremen.informatik.p2p.peeranha42.core.plugin.loader;

import java.io.File;
import java.util.Vector;

/**
 * @author dkleine
 *
 * This class holds a vector which only can contain File references pointing to directories;
 */
public class DirVector {

    private Vector directories;

    /**
     * This constructor initializes the internal Vector as new empty vector. 
     */
    public DirVector() {
        super();
        this.directories = new Vector();
    }

    /**
     * This method adds a File reference to this DirVector, if it is pointing do a directory.
     * 
     * @param f A File reference
     * @return true if it is a directory and the given reference is added, false else.
     */
    public boolean add(File f) {
        if (f.isDirectory()) {
            directories.add(f);
            return true;
        } else return false;
    }

    /**
     * This method returns the File reference on position i in this DirVector.
     * 
     * @param i a position in this DirVector
     * @return The reference from position i or null if i &lt; 0 or i &gt; this DirVectors size
     */
    public File get(int i) {
        if (i < directories.size() && i > -1) {
            return (File) (directories.get(i));
        }
        return null;
    }

    /**
     * This method returns how many elements are currently stored in this DirVector
     * 
     * @return number of elements
     */
    public int size() {
        return directories.size();
    }
}
