package org.chaoticengine.ast.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.chaoticengine.ast.GlobalData;
import org.chaoticengine.ast.model.WorldMap.LocationEdge;
import org.chaoticengine.ast.pattern.Model;
import org.newdawn.slick.geom.Vector2f;

/**
 *
 * @author Matt Van Der Westhuizen <mattpwest@gmail.com>
 */
public class AirShip extends Model implements HasTurn {

    public static enum NavMode {

        Safety, Speed
    }

    ;

    /** The name of the ship. */
    protected String name = "";

    /** Trade goods carried by this ship. */
    protected TradeInventory inventory = null;

    /** Current altitude. */
    protected Altitude altitude = Altitude.GROUND;

    /** Current route. */
    protected List<ShipLocation> route = null;

    /** Current location - either at a Location, or moving between them. */
    protected ShipLocation location = null;

    /** Distance travelled towards next location (if moving). */
    protected float distance = 0.0f;

    /** Speed - distance travelled per hour. */
    protected float speed = 0.5f;

    public AirShip() {
        super();
        route = new ArrayList<ShipLocation>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        refreshObservers();
    }

    public Altitude getAltitude() {
        return altitude;
    }

    public void setAltitude(Altitude altitude) {
        this.altitude = altitude;
        refreshObservers();
    }

    public ShipLocation getLocation() {
        return location;
    }

    public void setLocation(ShipLocation location) {
        this.location = location;
        refreshObservers();
    }

    public Vector2f getPosition() {
        if (location instanceof Location) {
            Vector2f p = ((Location) location).getPosition();
            return (p);
        } else if (location instanceof LocationEdge) {
            Location l1 = getSource();
            Location l2 = getDestination();
            Vector2f p = l1.getPosition().add(l2.getPosition().sub(l1.getPosition()).scale(getDistance()));
            return (p);
        } else {
            return (null);
        }
    }

    public List<ShipLocation> getRoute() {
        if (route != null) {
            return (route);
        } else {
            return (new ArrayList<ShipLocation>());
        }
    }

    public void goTo(int x, int y) {
        if (!(this.getLocation() instanceof Location)) {
            return;
        }
        WorldMap map = GlobalData.getInstance().getWorld().getMap();
        Location src = (Location) this.getLocation();
        Location dest = map.getLocation(x, y);
        if (dest != null) {
            route = map.findPathBetween(src, dest);
        }
    }

    public void takeTurn(float hours) {
        if ((route == null) || (route.isEmpty())) return;
        float move = hours * speed;
        WorldMap map = GlobalData.getInstance().getWorld().getMap();
        while ((move > 0.0f) && (!location.equals(route.get(route.size() - 1)))) {
            if (isMoving()) {
                Location src = getSource();
                Location dst = getDestination();
                float fullDist = src.getPosition().distance(dst.getPosition());
                float remainDist = fullDist * (1.0f - distance);
                if (move >= remainDist) {
                    move -= remainDist;
                    location = dst;
                    route = route.subList(route.indexOf(location), route.size());
                    distance = 0.0f;
                } else {
                    distance = (fullDist - remainDist + move) / fullDist;
                    move = 0.0f;
                }
            } else {
                int index = route.indexOf(location);
                if (index + 2 < route.size()) {
                    Location src = (Location) location;
                    Location dst = (Location) route.get(index + 2);
                    Iterator<LocationEdge> edgeIt = map.getPaths(src);
                    while (edgeIt.hasNext()) {
                        LocationEdge edge = edgeIt.next();
                        if (((edge.src.equals(src)) || (edge.src.equals(dst))) && ((edge.dst.equals(dst)) || (edge.dst.equals(src)))) {
                            location = edge;
                            distance = 0.0f;
                            break;
                        }
                    }
                }
            }
        }
        refreshObservers();
    }

    /** Is the ship currently travelling? */
    public boolean isMoving() {
        return ((getLocation() != null) && (getLocation() instanceof LocationEdge));
    }

    /** Where is the ship travelling from? */
    public Location getSource() {
        if (!isMoving()) {
            return ((Location) location);
        }
        LocationEdge edge = (LocationEdge) location;
        int srcIndex = route.indexOf(edge.src);
        int dstIndex = route.indexOf(edge.dst);
        if (srcIndex < dstIndex) {
            return (edge.src);
        } else {
            return (edge.dst);
        }
    }

    /** Where is the ship travelling to? (next step, not final destination) */
    public Location getDestination() {
        if (!isMoving()) {
            return ((Location) location);
        }
        LocationEdge edge = (LocationEdge) location;
        int srcIndex = route.indexOf(edge.src);
        int dstIndex = route.indexOf(edge.dst);
        if (srcIndex > dstIndex) {
            return (edge.src);
        } else {
            return (edge.dst);
        }
    }

    public float getDistance() {
        return (distance);
    }
}
