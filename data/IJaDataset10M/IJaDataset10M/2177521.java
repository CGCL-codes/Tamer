package net.sf.iauthor.core;

import javax.persistence.Entity;

/**
 * @author Andreas Beckers
 *
 */
@Entity
public class Location extends AbstractItem {

    /**
	 * @param string
	 */
    public Location(String name) {
        super(name);
    }
}
