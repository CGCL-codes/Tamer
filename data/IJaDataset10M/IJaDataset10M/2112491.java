package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The Class Continent.
 * 
 * Holds all relevant info about a risk continent
 */
public class Continent implements Serializable {

    private static final long serialVersionUID = 1L;

    private int menPerTurn;

    private String name;

    private List<Territory> terrs;

    /**
	 * Instantiates a new continent.
	 * 
	 * @param name
	 *            the name of the continent
	 * @param menPerTurn
	 *            the men per turn for this continent
	 */
    public Continent(String name, int menPerTurn) {
        this.menPerTurn = menPerTurn;
        this.name = name;
        this.terrs = new ArrayList<Territory>(10);
    }

    /**
	 * Gets the men per turn.
	 * 
	 * @return the men per turn
	 */
    public int getMenPerTurn() {
        return menPerTurn;
    }

    /**
	 * Adds the territory.
	 * 
	 * @param terr
	 *            the terr
	 */
    public void addTerritory(Territory terr) {
        if (terrs.contains(terr)) return;
        terrs.add(terr);
        terr.setContinent(this);
    }

    /**
	 * Gets the territories.
	 * 
	 * @return the territories
	 */
    public List<Territory> getTerritories() {
        return terrs;
    }

    /**
	 * Gets the name.
	 * 
	 * @return the name
	 */
    public String getName() {
        return name;
    }

    public boolean equals(Object o) {
        if (o == null) return false;
        if (!(o instanceof Continent)) return false;
        return ((Continent) o).name.equals(this.name);
    }
}
