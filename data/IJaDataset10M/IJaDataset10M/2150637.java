package imageTools;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Result.java (  imageTools )
 * Created: Dec 5, 2009
 * @author Michael Nekrasov
 * 
 * Description: A Result of applying a particular rule on an image,
 * 				Contains within itself a reference to a corresponding rule
 * 				This is a result for, as well as locations where objects
 * 				matching this rule were discovered. 
 *
 */
public class Result implements Iterable<Location> {

    private final Rule correspondingRule;

    protected LinkedList<Location> locations;

    /**
	 * Creates an empty result for a given rule
	 * @param correspondingRule
	 */
    public Result(Rule correspondingRule) {
        this.correspondingRule = correspondingRule;
        locations = new LinkedList<Location>();
    }

    /**
	 * Adds a location to the result
	 * @param loc of matched object to add
	 */
    public void add(Location loc) {
        locations.add(loc);
    }

    /** 
	 * Number of locations stored in result
	 * @return the number of locations
	 */
    public int count() {
        return locations.size();
    }

    /**
	 * The name of the result which matches the name of the corresponding rule
	 * @return the name
	 */
    public final String getName() {
        return correspondingRule.getName();
    }

    /**
	 * Get the corresponding rule associated with this result
	 * @return the corresponding rule
	 */
    public final Rule getCorrespondingRule() {
        return correspondingRule;
    }

    @Override
    public String toString() {
        return getName() + "[" + count() + "]";
    }

    @Override
    public Iterator<Location> iterator() {
        return locations.iterator();
    }
}
