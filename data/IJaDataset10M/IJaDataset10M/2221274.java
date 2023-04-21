package de.dhbwmannheim.tim.entity;

/**
 * 
 * @author Christopher Sohn
 * @author Ulrich Wolf
 * 
 */
public class Colors {

    private int id = -1;

    private String name;

    /**
	 * @return the id
	 */
    public int getId() {
        return id;
    }

    /**
	 * @param id
	 *            the id to set
	 */
    public void setId(int id) {
        this.id = id;
    }

    /**
	 * @return the name
	 */
    public String getName() {
        return name;
    }

    /**
	 * @param name
	 *            the name to set
	 */
    public void setName(String name) {
        this.name = name;
    }
}
