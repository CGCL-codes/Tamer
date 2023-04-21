package net.evil_lair.javalicious;

import java.io.Serializable;

/**
 * A Tag is basically just a single word.  If the Tag was retrieved from a 
 * <i>getTags</i> call, it can also contain a count, indicating how many posts
 * use this tag.
 * 
 */
public interface ITag extends Serializable, Comparable<ITag> {

    /**
	 * Returns the number of Posts that use this Tag, or -1 if not known.
	 * 
	 * @return The number of Posts that use this Tag, or -1 if not known.
	 */
    public int getCount();

    /**
	 * Returns the Tag's value.
	 * 
	 * @return The Tag's value.
	 */
    public String getTag();

    /**
	 * Creates a copy of this Tag.
	 * 
	 * @return A copy of this Tag.
	 */
    public ITag copy();
}
