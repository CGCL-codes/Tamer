package net.sourceforge.skatmanager.vo;

import java.io.Serializable;

/**
 * @author sj05127
 *
 */
public class Season extends IdActiveObject implements Serializable {

    /**
	 * serialVersionUID
	 */
    private static final long serialVersionUID = 4180456783920722168L;

    /**
	 * Parameterless defaultconstruktor
	 */
    public Season() {
    }

    /** Season-Name */
    private String name;

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
}
