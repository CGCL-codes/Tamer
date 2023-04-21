package net.sjava.logging;

/**
 * 
 * @author mcsong@gmail.com
 * @since 2009. 6. 19.
 * @version
 */
public class Level {

    /** level number */
    private int level = 0;

    /** level name */
    private String name = null;

    /**
	 * Constructor
	 * 
	 * @param level
	 * @param name
	 */
    public Level(int level, String name) {
        this.level = level;
        this.name = name;
    }

    /**
	 * @return the level
	 */
    public int getLevel() {
        return level;
    }

    /**
	 * @param level the level to set
	 */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
	 * @return the name
	 */
    public String getName() {
        return name;
    }

    /**
	 * @param name the name to set
	 */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "name is " + this.name + ", level is " + this.level;
    }
}
