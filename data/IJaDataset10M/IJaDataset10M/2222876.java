package ie.ucd.nexus.core;

import javax.media.j3d.BranchGroup;

/**
 * Version 1.3.1 of Java3D doesn't have a field for
 * giving a name to a BranchGroup, so this extended
 * version is designed to overcome that shortfall until
 * such a time as NeXuS is compatible with more recent
 * versions of Java3D
 * @author John Stafford
 */
public class NamedBranchGroup extends BranchGroup {

    /** The name of the NamedBranchGroup */
    String name;

    /**
     * Sets the name of the NamedBranchGroup
     * @param name The name of the NamedBranchGroup
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the name of the NamedBranchGroup
     * @return String
     */
    public String getName() {
        return name;
    }
}
