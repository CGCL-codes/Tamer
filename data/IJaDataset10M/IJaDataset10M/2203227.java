package universe.client.database;

import universe.common.*;
import universe.common.database.*;
import universe.client.database.*;

/**
 * @author Sean Starkey
 * @version $Id: FleetView.java,v 1.1 2003/04/03 00:20:53 sstarkey Exp $
 *
 * This class represents a client's view of a fleet..
 */
public class FleetView extends DatabaseObjectView {

    private String name;

    private Location location;

    private CivID owner;

    public FleetView(Index index, String name, Location loc, CivID owner) {
        super(index);
        this.name = name;
        location = loc;
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public CivID getOwner() {
        return owner;
    }
}
