package net.davidlauzon.assemblandroid.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import net.davidlauzon.assemblandroid.AssemblaAPIAdapter;
import net.davidlauzon.assemblandroid.asyncTask.ParsedArrayList;
import net.davidlauzon.assemblandroid.exceptions.AssemblaAPIException;
import net.davidlauzon.assemblandroid.exceptions.XMLParsingException;
import net.davidlauzon.net.RestfulException;

public class Space implements Serializable {

    private static final long serialVersionUID = 6656872344467459407L;

    public static final String TIMER_LOADING = "spaceLoading";

    public static final String TIMER_PARSING = "spaceParsing";

    private String id = null;

    private String name = null;

    private String description = null;

    private ParsedArrayList<Ticket> tickets = null;

    public String id() {
        return this.id;
    }

    public String listItemText() {
        return this.name;
    }

    public Space(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    /**
	 * WARNING: this method returns the tickets that we have in memory.
	 * Use reloadTickets() if you want to make sure to have the latest data
	 * 
	 * @return ArrayList<Ticket> tickets
	 * @throws XMLParsingException 
	 * @throws RestfulException 
	 * @throws AssemblaAPIException 
	 */
    public ParsedArrayList<Ticket> getTickets() {
        return tickets;
    }

    public ParsedArrayList<Ticket> reloadTickets(boolean includeClosed, boolean includeOthers) throws XMLParsingException, AssemblaAPIException, RestfulException {
        tickets = AssemblaAPIAdapter.getInstance().getTicketsBySpaceId(this.id, includeClosed, includeOthers);
        return tickets;
    }

    /**
	 * @return true if records were already loaded
	 */
    public boolean ticketsLoaded() {
        return !(tickets == null);
    }

    public String name() {
        return this.name;
    }

    public String toString() {
        try {
            return "[Space] id = " + id + " ; name = " + name + " ; description = " + description + " ; tickets = \n[" + getTickets() + "]";
        } catch (Exception e) {
            return "[Space] id = " + id + " ; name = " + name + " ; description = " + description + " ; tickets = \n[" + "EXCEPTION raised while retrieving tickets" + "]";
        }
    }
}
