package org.freelords.armies;

import java.io.Serializable;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import org.freelords.xml.XMLElement;

/** Class that provides some modifier "template".
  * 
  * Each instance holds an id (some string, so it should be chosen unique!)
  * and a list of effects that it has on the armies. For actual instances used
  * in the game, see {@link StatModifierInstance}.
  */
public class StatModifierInfo implements Serializable {

    private static final long serialVersionUID = -6609634316269927491L;

    /** The reason for the modification */
    private String name;

    /** The list of the modifier adjustment for each stat, each value here will be added to the base stats */
    private EnumMap<ArmyStat, Integer> modifiers = new EnumMap<ArmyStat, Integer>(ArmyStat.class);

    /** Initialises the modifier info.
	  *
	  * @param name      the name (and id) of the modification.
	  * @param modifiers a list of army stat modifications that this modifier
	  * contains.
	  */
    public StatModifierInfo(@XMLElement(name = "id", attribute = true) String name, @XMLElement(name = "stats") EnumMap<ArmyStat, Integer> modifiers) {
        if (name == null) {
            throw new NullPointerException("Name must be provided");
        }
        if (modifiers == null) {
            throw new NullPointerException("Modifiers must be provided");
        }
        this.name = name;
        this.modifiers = modifiers;
    }

    /** Returns the modifiers that this info stores. */
    public Map<ArmyStat, Integer> getModifiers() {
        return Collections.unmodifiableMap(modifiers);
    }

    /** Applies the modifiers to a map of army stats. */
    public void applyOnto(Map<ArmyStat, Integer> base) {
        for (Map.Entry<ArmyStat, Integer> entry : modifiers.entrySet()) {
            ArmyStat stat = entry.getKey();
            int baseValue = base.get(stat);
            base.put(stat, baseValue + entry.getValue());
        }
    }

    /** Returns the name/id of the modifier info. */
    public String getName() {
        return name;
    }
}
