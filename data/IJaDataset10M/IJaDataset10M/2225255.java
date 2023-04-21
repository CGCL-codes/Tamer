package eu.pouvesle.nicolas.dpp.winsurf;

import java.awt.List;

/**
 * Coresponds to a section line in a WinSurf Grammar.
 * This class layer was implemneted in order to store the location
 * data from the original command or script file.
 * Since V2.00 all section line are represented by a List.
 *
 * @author Nicolas Pouvesle
 * @version 1.00
 * @since dpp 2.01.00
 */
public class WSSectionLine extends List {

    private int line;

    /**
	 * Default constructor.
	 */
    public WSSectionLine() {
        super();
        line = -1;
    }

    /**
	 * Default constructor and line initialisation.
	 *
	 * @param line Line location.
	 */
    public WSSectionLine(int line) {
        this();
        this.line = line;
    }

    /**
	 * Returns the line location.
	 */
    public int getLine() {
        return line;
    }

    /**
	 * Sets the value of the line location.
	 * @param line The value to assign line.
	 */
    public void setLine(int line) {
        this.line = line;
    }

    /**
	* Get the line as String.
	*
	* @return The line as one String
	*/
    public String getAsString() {
        String line = new String();
        for (int i = 0; i < getItemCount(); i++) line += getItem(i) + " ";
        return line;
    }

    /**
	 * Get a new list of item between from and the last index.
	 *
	 * @param from Inder of the first element to include.
	 * @return     A new list.
	 */
    public List getFrom(int from) {
        List newList = new List();
        for (int i = from; i < getItemCount(); i++) newList.add(getItem(i));
        return newList;
    }
}
