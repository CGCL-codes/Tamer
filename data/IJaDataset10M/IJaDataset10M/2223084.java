package xutools.universe.creators.stationcreator;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;
import xutools.config.ObjectType;
import xutools.config.ObjectTypeLibrary;
import xutools.config.X2Metrics;
import xutools.config.XToolsConfiguration;
import xutools.helpers.ObjectCoordinates;
import xutools.random.EnhancedRandom;
import xutools.universe.core.Sector;
import xutools.universe.inventory.distributions.AbstractDistributionPattern;
import xutools.universe.inventory.distributions.DistributionPattern;
import xutools.universe.inventory.items.Asteroid;
import xutools.universe.inventory.items.Gate;
import xutools.universe.inventory.items.Station;
import xutools.universe.inventory.items.X2SpaceObject;

/**
 * A special type of pattern used for mines. Picks locations by replacing
 * existing asteroids. If no appropriate asteroids can be found, a default
 * distribution pattern will be used instead. This default pattern should be
 * similar or equal to the one used for other kinds of stations.
 * 
 * @author Tobias Weigel
 * @date 07.11.2008
 * 
 */
public class AsteroidReplacementPattern extends AbstractDistributionPattern {

    /**
	 * Creates a new AsteroidReplacementPattern. Note that during creation
	 * process, all asteroids existing in the sector will be listed to be used
	 * later when object coordinates are requested. As a result, any changes to
	 * the inventory after this constructor is called will not be registered.
	 * For example, new asteroids added will not be used. Removing asteroid from
	 * the inventory might turn out dangerous as they will still be listed for
	 * suitable station places. However, it is very unlikely that inventory
	 * items will be removed after they have been created (ironically, this
	 * distribution pattern might be one of the few points where items are
	 * removed).
	 * 
	 * @param sector
	 * @param defaultPattern
	 * @param maxRadius
	 * @param maxZRadius
	 */
    public AsteroidReplacementPattern(Sector sector, DistributionPattern defaultPattern, double maxRadius, double maxZRadius) {
        super(sector);
        this.defaultPattern = defaultPattern;
        this.maxRadius = maxRadius;
        this.maxZRadius = maxZRadius;
        sector.getInventory().addInventoryMembersTo(ObjectTypeLibrary.ASTEROID, asteroids);
    }

    private DistributionPattern defaultPattern;

    private double maxRadius;

    private double maxZRadius;

    private boolean useDefaultPattern = false;

    private Vector<X2SpaceObject> asteroids = new Vector<X2SpaceObject>();

    @Override
    public ObjectCoordinates nextCoordinates(ObjectType objectType, double collisionRange) {
        EnhancedRandom rnd = XToolsConfiguration.getInstance().getRandom();
        if (!useDefaultPattern) {
            while (!asteroids.isEmpty()) {
                X2SpaceObject obj = asteroids.remove(rnd.nextInt(asteroids.size()));
                if (isSuitable(obj)) {
                    Asteroid oldAsteroid = (Asteroid) obj;
                    if (sector.getInventory().removeMember(obj)) {
                        new Asteroid(sector, oldAsteroid.getType(), oldAsteroid.getAmount(), oldAsteroid.getSubtype(), oldAsteroid.getSize());
                        return new ObjectCoordinates(obj.getCoords());
                    }
                }
            }
            useDefaultPattern = true;
        }
        return defaultPattern.nextCoordinates(objectType, collisionRange);
    }

    private boolean isSuitable(X2SpaceObject obj) {
        Collection<X2SpaceObject> gates = new LinkedList<X2SpaceObject>();
        sector.getInventory().addInventoryMembersTo(ObjectTypeLibrary.GATE, gates);
        for (Iterator<X2SpaceObject> iter = gates.iterator(); iter.hasNext(); ) {
            Gate gate = (Gate) iter.next();
            if (gate.collidesWith2D(obj.getCoords().getPosition(), X2Metrics.gateCollisionRange, 1.f)) return false;
        }
        for (Iterator<Station> iter = sector.getInventory().iterateStations(); iter.hasNext(); ) {
            Station station = iter.next();
            if (station.collidesWith2D(obj.getCoords().getPosition(), X2Metrics.stationCollisionRange, 1.f)) return false;
        }
        return (obj.getCoords().getPosition().xyDistanceFromCenter() < maxRadius) && (Math.abs(obj.getCoords().getPosition().getZ()) < maxZRadius);
    }
}
