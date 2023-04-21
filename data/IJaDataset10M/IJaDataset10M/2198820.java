package org.freelords.armies;

import java.util.List;
import org.freelords.armies.stacks.UnitStack;
import org.freelords.util.GroupedEntityCollection;

/** A list of armies.
  * 
  * This class features a list of armies and functions to retrieve a subset of
  * the armies, depending on their id or location, in the form of a collection
  * or armies at the same location stacked together.
  */
public class ActiveArmies extends GroupedEntityCollection<UnitStack, Army> {

    @Override
    public UnitStack toGroupObject(List<Army> list) {
        if (list == null || list.isEmpty()) {
            return null;
        } else {
            return new UnitStack(list);
        }
    }
}
