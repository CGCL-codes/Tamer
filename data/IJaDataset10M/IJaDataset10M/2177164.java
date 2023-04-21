package tchoukstats.system;

import java.lang.String;
import org.w3c.dom.*;
import tchoukstats.exceptions.XMLException;
import tchoukstats.utils.DOMizable;
import tchoukstats.utils.XMLUtils;

/**
 * Internal representation of a player. The first and last name of the player are stored.
 * A number can also being added.
 */
public class Player implements Comparable<Player>, DOMizable {

    private int id = -1;

    /** First name of this player. */
    private String firstName = null;

    /** Last name of this player. */
    private String name = null;

    /** Number of this player for this match. */
    private int number = -1;

    /**
	 * Constructor of a player with first and last name specified.
	 *
	 * @param name       last name of this player.
	 * @param firstName  first name of this player.
	 */
    public Player(int id, String name, String firstName) {
        this.id = id;
        this.firstName = firstName;
        this.name = name;
    }

    /**
	 * Constructor of a player with first name, last name and player number specified.
	 *
	 * @param name       last name of this player.
	 * @param firstName  first name of this player.
	 * @param number     number of this player.
	 */
    public Player(int id, String name, String firstName, int number) {
        this.id = id;
        this.firstName = firstName;
        this.name = name;
        this.number = number;
    }

    public int compareTo(Player p) {
        if (id != p.id) return 1;
        int r = name.compareToIgnoreCase(p.name);
        if (r == 0) {
            return firstName.compareToIgnoreCase(p.firstName);
        }
        return r;
    }

    public Element toDOMElement(Document document) {
        Element e = document.createElement("player");
        e.setAttribute("id", "" + id);
        e.setAttribute("first_name", firstName);
        e.setAttribute("name", name);
        e.setAttribute("number", "" + number);
        return e;
    }

    public static Player loadFromElement(Element e) throws XMLException {
        int _id = XMLUtils.getIntAttribute(e, "id");
        String _firstName = e.getAttribute("first_name");
        String _name = e.getAttribute("name");
        int _number = -1;
        if (e.hasAttribute("number")) _number = Integer.parseInt(e.getAttribute("number"));
        return new Player(_id, _name, _firstName, _number);
    }

    @Override
    public String toString() {
        if (number == -1) return name + " " + firstName;
        return "" + number;
    }

    public int getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFirstName() {
        return firstName;
    }

    public int getNumber() {
        return number;
    }
}
