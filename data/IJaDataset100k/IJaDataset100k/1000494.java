package org.javaseis.io;

import org.javaseis.io.VirtualFolder.Attribute;
import edu.mines.jtk.util.ParameterSet;

/**
 * A bogus policy that puts all the extents into the first folder.
 * This is only intended to be used for development/testing.
 * 
 * @author Steve Angelovich
 *
 */
public class ExtentPolicyFolder1 implements ExtentPolicy {

    public static final String ID = "Folder1";

    public String getID() {
        return ID;
    }

    public int getNextExtentFolder(int index, int maxNumExtents, VirtualFolders vFolders) {
        int r = -1;
        Integer[] indexes = vFolders.getIndexes(Attribute.READ_WRITE);
        if (indexes.length > 0) r = indexes[0];
        return r;
    }

    public void store(ParameterSet parameterSet) {
        parameterSet.setString(ExtentPolicy.POLICY_ID, getID());
    }

    public void load(ParameterSet parameterSet) {
    }

    public String getHelp() {
        return "Puts all extents ino the first folder-used for testing";
    }
}
