package net.sourceforge.wildlife.core.components;

import net.sourceforge.wildlife.core.environment.World;

/**
 * Abstraction class
 *
 * @author Jean Barata
 */
public abstract class Abstraction extends Component {

    /**
	 * Abstraction class constructor
	 * 
	 * @param world_p
	 */
    public Abstraction(World world_p) {
        super(world_p);
    }

    /**
	 * Abstraction class constructor
	 * 
	 * @param threadGroup_p
	 * @param world_p
	 */
    public Abstraction(ThreadGroup threadGroup_p, World world_p) {
        super(threadGroup_p, world_p);
    }

    /**
	 * abstraction's unique identifier
	 */
    public String get_type() {
        return "";
    }
}
