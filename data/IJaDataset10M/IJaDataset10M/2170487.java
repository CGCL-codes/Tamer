package net.sf.ninja.collection;

import java.util.Collection;
import net.sf.ninja.util.ArraySequence;
import net.sf.ninja.model.MobileHydrogenGroup;

/**
 * A default implementation of the <code>MobileHydrogenGrouCollection</code> interface.
 * 
 * @author Richard Apodaca
 */
public class BasicMobileHydrogenGroupCollection implements MobileHydrogenGroupCollection {

    private ArraySequence groups;

    /**
   * Constructs an empty <code>BasicMobileHydrogenGroupCollection</code>.
   */
    public BasicMobileHydrogenGroupCollection() {
        this.groups = new ArraySequence();
    }

    /**
   * Constructs a <code>BasicMobileHydrogenGroupCollection</code> form the elements
   * contained in <code>groups</code>.
   * 
   * @param groups the Collection containing <code>MobileHydrogenGroups</code>
   */
    public BasicMobileHydrogenGroupCollection(Collection groups) {
        this.groups = new ArraySequence(groups, MobileHydrogenGroup.class);
    }

    public boolean contains(MobileHydrogenGroup group) {
        return groups.contains(group);
    }

    public int countMobileHydrogenGroups() {
        return groups.size();
    }

    public int getIndex(MobileHydrogenGroup group) {
        return groups.indexOf(group);
    }

    public MobileHydrogenGroup getMobileHydrogenGroup(int index) {
        return (MobileHydrogenGroup) groups.get(index);
    }

    public MobileHydrogenGroup[] toMobileHydrogenGroupArray() {
        return (MobileHydrogenGroup[]) groups.toArray();
    }
}
