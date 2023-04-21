package net.sourceforge.gendo.attributes;

/**
 * This object defines an attribute. An attribute consists of a Name, a short
 * name and a score.
 * 
 * @version 1.0
 * @author Jo-Herman Haugholt
 */
public class Attribute {

    private String name;

    private String shortname;

    private int score;

    /**
	 * Creates a new attribute
	 * 
	 * @param name
	 *            Name of attribute. e.g. 'Strength'
	 * @param shortname
	 *            Short Name of attribute. e.g. 'STR'
	 * @param score
	 *            initial score of the attribute.
	 */
    public Attribute(String name, String shortname, int score) {
        this.name = name;
        this.shortname = shortname;
        this.score = score;
    }

    /**
	 * Gets the score of this Attribute
	 * 
	 * @return Returns the score.
	 */
    public int getScore() {
        return score;
    }

    /**
	 * Sets the score of this attribute
	 * 
	 * @param score
	 *            The score to set.
	 */
    public void setScore(int score) {
        this.score = score;
    }

    /**
	 * Returns the name of this attribute
	 * 
	 * @return Returns the name.
	 */
    public String getName() {
        return name;
    }

    /**
	 * Returns the short name of this attribute
	 * 
	 * @return Returns the shortname.
	 */
    public String getShortName() {
        return shortname;
    }
}
