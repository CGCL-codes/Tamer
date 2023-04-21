package starcraft.gameserver.game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import starcraft.gamemodel.shared.BasePlacement;
import starcraft.gamemodel.shared.GameException;
import starcraft.gamemodel.shared.OrderTokenPlacement;
import starcraft.gamemodel.shared.OrderTokens;
import starcraft.gamemodel.shared.Planet;
import starcraft.gamemodel.shared.PlanetArea;
import starcraft.gamemodel.shared.PlanetAreaGraphics;
import starcraft.gamemodel.shared.PlanetAreaRef;
import starcraft.gamemodel.shared.PlanetConnector;
import starcraft.gamemodel.shared.PlanetGraphics;
import starcraft.gamemodel.shared.PlanetPlacement;
import starcraft.gamemodel.shared.PlanetRef;
import starcraft.gamemodel.shared.PlayerRef;
import starcraft.gamemodel.shared.ResourceDepletion;
import starcraft.gamemodel.shared.ResourceType;
import starcraft.gamemodel.shared.compatibility.Dimension;
import starcraft.gamemodel.shared.compatibility.Point;
import starcraft.gamemodel.shared.compatibility.Rectangle;
import starcraft.gamemodel.shared.logic.PlanetSystemMath;
import starcraft.gamemodel.shared.races.Faction;
import starcraft.gamemodel.shared.races.Unit;
import starcraft.gamemodel.shared.races.UnitType;
import common.utilities.CollectionUtilities;
import common.utilities.rmi.CachabeMethod;

public class PlanetSystemImpl {

    private final Log log = LogFactory.getLog(getClass());

    private final GameImpl game;

    /**
	 * List of all planets at all. Mapped to planet name.
	 */
    public final Map<PlanetRef, Planet> allPlanets;

    /**
	 * List of all planets in game. They get added to the two dimensional
	 * <code>placedPlanets</code> array as they get placed by the player.
	 */
    private final Map<PlayerRef, List<PlanetRef>> drawnPlanets = new HashMap<PlayerRef, List<PlanetRef>>();

    private final List<PlanetPlacement> placedPlanets = new ArrayList<PlanetPlacement>();

    private final List<BasePlacement> placedBases = new ArrayList<BasePlacement>();

    private final List<Unit> placedUnits = new ArrayList<Unit>();

    private final Map<PlanetConnector, List<Faction>> placedTransports = new HashMap<PlanetConnector, List<Faction>>();

    private final Map<Integer, List<OrderTokenPlacement>> placedOrders = new HashMap<Integer, List<OrderTokenPlacement>>();

    ;

    public PlanetSystemImpl(GameImpl game) {
        this.game = game;
        try {
            allPlanets = Collections.unmodifiableMap(loadPlanets());
        } catch (Exception e) {
            throw new RuntimeException("Error while loading planets.", e);
        }
    }

    public boolean isDoneWithPlacingPlanetsAndBases() {
        try {
            return (placedPlanets.size() / drawnPlanets.keySet().size() == 2) && (placedBases.size() / drawnPlanets.keySet().size() == 1);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isDoneWithPlacingStartingForce() {
        Map<PlayerRef, List<UnitType>> placedForces = new HashMap<PlayerRef, List<UnitType>>();
        for (Unit placedUnit : placedUnits) {
            CollectionUtilities.addToList(placedForces, placedUnit.getOwningPlayer(), placedUnit.getType());
        }
        for (PlayerRef playerRef : game.getPlayers()) {
            List<UnitType> list = placedForces.get(playerRef);
            if (list == null) {
                return false;
            }
            PlayerImpl playerImpl = game.getPlayer(playerRef);
            List<UnitType> startingForce = playerImpl.getStartingForce();
            CollectionUtilities.containExactSameElementsIgnoreOrder(list, startingForce);
        }
        return true;
    }

    public boolean isDoneWithPlacingStartingForce(PlayerRef playerRef) {
        Map<PlayerRef, List<UnitType>> placedForces = new HashMap<PlayerRef, List<UnitType>>();
        for (Unit placedUnit : placedUnits) {
            CollectionUtilities.addToList(placedForces, placedUnit.getOwningPlayer(), placedUnit.getType());
        }
        List<UnitType> list = placedForces.get(playerRef);
        if (list == null) {
            return false;
        }
        PlayerImpl playerImpl = game.getPlayer(playerRef);
        List<UnitType> startingForce = playerImpl.getStartingForce();
        CollectionUtilities.containExactSameElementsIgnoreOrder(list, startingForce);
        return true;
    }

    public boolean isSetupComplete() {
        return isDoneWithPlacingPlanetsAndBases() && isDoneWithPlacingStartingForce();
    }

    public List<Planet> getAllUsedPlanets() {
        return new ArrayList<Planet>(allPlanets.values());
    }

    public List<PlanetRef> getDrawnPlanets(PlayerRef player) throws GameException {
        List<PlanetRef> planets = drawnPlanets.get(player);
        if (planets == null) {
            throw new GameException("The planets have not yet been drawn.");
        }
        return new ArrayList<PlanetRef>(planets);
    }

    public Set<PlanetPlacement> getPlacedPlanets() {
        return new HashSet<PlanetPlacement>(placedPlanets);
    }

    public Set<PlanetPlacement> getPlacedPlanets(PlayerRef player) {
        Set<PlanetPlacement> result = new HashSet<PlanetPlacement>();
        for (PlanetPlacement planetPlacement : placedPlanets) {
            if (planetPlacement.getPlacedBy().equals(player)) {
                result.add(planetPlacement);
            }
        }
        return result;
    }

    /**
	 * Returns the last placed planet of the given player. This returns null, if not
	 * in planet placing mode or if no planet has been placed by that player.
	 */
    @CachabeMethod
    public PlanetPlacement getLastPlacedPlanet(PlayerRef player) {
        if (isDoneWithPlacingPlanetsAndBases()) {
            return null;
        }
        PlanetPlacement last = null;
        for (PlanetPlacement placement : placedPlanets) {
            if (player.equals(placement.getPlacedBy())) {
                last = placement;
            }
        }
        return last;
    }

    @CachabeMethod
    public Set<PlanetConnector> getConnectors() {
        Set<PlanetConnector> result = PlanetSystemMath.getConnectors(allPlanets, placedPlanets);
        return new HashSet<PlanetConnector>(result);
    }

    public List<BasePlacement> getPlacedBases() {
        return new ArrayList<BasePlacement>(placedBases);
    }

    public List<BasePlacement> getPlacedBases(PlayerRef player) {
        List<BasePlacement> result = new ArrayList<BasePlacement>();
        for (BasePlacement basePlacement : placedBases) {
            if (basePlacement.getPlacedBy().equals(player)) {
                result.add(basePlacement);
            }
        }
        return result;
    }

    public List<Unit> getPlacedUnits() {
        return new ArrayList<Unit>(placedUnits);
    }

    public Map<PlanetConnector, List<Faction>> getPlacedTransports() {
        return new HashMap<PlanetConnector, List<Faction>>(placedTransports);
    }

    public List<OrderTokenPlacement> getOrderTokenPlacements() {
        return getOrderTokenPlacements(game.getCurrentRound());
    }

    public List<OrderTokenPlacement> getOrderTokenPlacements(int forRound) {
        List<OrderTokenPlacement> result = placedOrders.get(forRound);
        if (result == null) {
            return new ArrayList<OrderTokenPlacement>();
        }
        return result;
    }

    public Planet getPlanet(PlanetRef planetRef) {
        return allPlanets.get(planetRef);
    }

    public PlanetArea getPlanetArea(PlanetAreaRef planetAreaRef) {
        Planet planet = getPlanet(planetAreaRef.getPlanetRef());
        return planet.getArea(planetAreaRef);
    }

    public void placePlanet(PlayerRef player, PlanetRef planetRef, int field, int orientation) throws GameException {
        for (PlanetPlacement planetPlacement : placedPlanets) {
            if (planetPlacement.getLocation() == field) {
                throw new GameException("The field '" + field + "' is already occupied.");
            }
        }
        Planet planet = getPlanet(planetRef);
        placedPlanets.add(new PlanetPlacement(player, planet.getID(), field, orientation));
        PlayerImpl playerImpl = game.getPlayer(player);
        String playerName = playerImpl.getDisplayName();
        log.debug("planet '" + planet.getName() + "' placed by '" + playerName + "'.");
        debugPrintPlacedPlanets();
    }

    public void placeBase(PlayerRef player, PlanetAreaRef planetAreaRef) {
        Planet planet = getPlanet(planetAreaRef.getPlanetRef());
        PlanetArea planetArea = getPlanetArea(planetAreaRef);
        PlayerImpl playerImpl = game.getPlayer(player);
        String playerName = playerImpl.getDisplayName();
        Faction playerFaction = playerImpl.getFaction();
        placedBases.add(new BasePlacement(player, planetAreaRef));
        log.debug("base placed on '" + planet.getName() + "/" + planetArea.getAreaID() + "' by '" + playerName + "'.");
    }

    public void placeOrder(PlayerRef placedBy, OrderTokens orderToken, PlanetRef planet) {
        PlayerImpl player = game.getPlayer(placedBy);
        Faction faction = player.getFaction();
        OrderTokenPlacement orderTokenPlacement = new OrderTokenPlacement(orderToken, placedBy, faction, planet);
        Integer currentRound = game.getCurrentRound();
        CollectionUtilities.addToList(placedOrders, currentRound, orderTokenPlacement);
        log.debug("order placed on '" + planet + "' by '" + player.getDisplayName() + "'.");
    }

    public void placeNewUnit(PlayerRef player, UnitType unitType, PlanetAreaRef location) {
        PlayerImpl playerImpl = game.getPlayer(player);
        String playerName = playerImpl.getDisplayName();
        Faction playerFaction = playerImpl.getFaction();
        Planet planet = getPlanet(location.getPlanetRef());
        Unit unit = new Unit(unitType, player, playerFaction, location);
        placedUnits.add(unit);
        log.debug("new unit '" + unitType.getName() + "' placed on '" + planet.getName() + "/" + location.getAreaID() + "' by '" + playerName + "'.");
    }

    public void placeTransport(PlayerRef player, PlanetConnector planetConnector) {
        PlayerImpl playerImpl = game.getPlayer(player);
        String playerName = playerImpl.getDisplayName();
        Faction playerFaction = playerImpl.getFaction();
        List<Faction> list = placedTransports.get(planetConnector);
        if (list == null) {
            list = new ArrayList<Faction>();
            placedTransports.put(planetConnector, list);
        }
        list.add(playerFaction);
        log.debug("new transport placed on '" + planetConnector + "' by '" + playerName + "'.");
    }

    public void connectPlanets(String clientID, Planet sourcePlanet, Planet targetPlanet) {
        throw new UnsupportedOperationException("not implemented");
    }

    void initializeDrawnPlanets(Collection<PlayerRef> players, int planetsPerPlayer) {
        Map<PlayerRef, List<PlanetRef>> result = new HashMap<PlayerRef, List<PlanetRef>>();
        List<Planet> toDrawFrom = new ArrayList<Planet>(allPlanets.values());
        for (int i = 0; i < planetsPerPlayer; i++) {
            if (toDrawFrom.isEmpty()) {
                throw new RuntimeException("No planets to draw from.");
            }
            for (PlayerRef player : players) {
                List<PlanetRef> list = result.get(player);
                if (list == null) {
                    list = new ArrayList<PlanetRef>();
                    result.put(player, list);
                }
                int index = (int) Math.round(Math.random() * (toDrawFrom.size() - 1));
                Planet drawn = toDrawFrom.remove(index);
                list.add(drawn.getID());
            }
        }
        this.drawnPlanets.clear();
        this.drawnPlanets.putAll(result);
        log.debug("initialized drawn planets.");
    }

    private Map<PlanetRef, Planet> loadPlanets() throws Exception {
        log.debug("loading planets...");
        XMLConfiguration xml = new XMLConfiguration(getClass().getClassLoader().getResource("planets.xml"));
        Map<PlanetRef, Planet> result = new HashMap<PlanetRef, Planet>();
        List<?> planets = xml.configurationsAt("planet");
        for (Object tp : planets) {
            HierarchicalConfiguration planetConfig = (HierarchicalConfiguration) tp;
            String connectorsString = planetConfig.getString("[@connectors]");
            boolean connectorInNorth = connectorsString.charAt(0) == 'y';
            boolean connectorInEast = connectorsString.charAt(1) == 'y';
            boolean connectorInSouth = connectorsString.charAt(2) == 'y';
            boolean connectorInWest = connectorsString.charAt(3) == 'y';
            Planet planet = new Planet(planetConfig.getString("[@name]"), new PlanetGraphics(planetConfig.getString("graphics[@image]"), new Dimension(planetConfig.getShort("graphics.size[@width]"), planetConfig.getShort("graphics.size[@height]")), new Point(planetConfig.getShort("graphics.center[@x]"), planetConfig.getShort("graphics.center[@y]")), new Rectangle(planetConfig.getShort("graphics.orders[@x]"), planetConfig.getShort("graphics.orders[@y]"), planetConfig.getShort("graphics.orders[@width]"), planetConfig.getShort("graphics.orders[@height]"))), connectorInNorth, connectorInEast, connectorInSouth, connectorInWest);
            result.put(planet.getID(), planet);
            List<?> areas = planetConfig.configurationsAt("area");
            for (int i = 0; i < areas.size(); i++) {
                HierarchicalConfiguration areaConfig = (HierarchicalConfiguration) areas.get(i);
                String id = "area" + i;
                PlanetArea planetArea = new PlanetArea(id, planet.getID(), ResourceType.fromString(areaConfig.getString("[@type]")), areaConfig.getShort("[@size]"), areaConfig.getShort("[@unitLimit]"), new PlanetAreaGraphics(areaConfig.getShort("arc[@start]"), areaConfig.getShort("arc[@end]"), new Rectangle(areaConfig.getShort("base[@x]"), areaConfig.getShort("base[@y]"), areaConfig.getShort("base[@width]"), areaConfig.getShort("base[@height]")), new Rectangle(areaConfig.getShort("units[@x]"), areaConfig.getShort("units[@y]"), areaConfig.getShort("units[@width]"), areaConfig.getShort("units[@height]"))), ResourceDepletion.NotDepleted);
                planet.addArea(planetArea);
            }
        }
        while (result.size() < 10) {
            throw new IllegalStateException("There are less than 10 planets defined.");
        }
        return result;
    }

    public void debugPrintPlacedPlanets() {
        log.debug("placed Planets: ");
        Set<Integer> occupiedFields = new HashSet<Integer>();
        for (PlanetPlacement placement : placedPlanets) {
            occupiedFields.add(placement.getLocation());
        }
        log.debug("    12345678901234567890123");
        for (int row = 1; row <= 23; row++) {
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("%1$2s: ", row));
            for (int col = 1; col <= 23; col++) {
                int index = ((row - 1) * 23) + col;
                if (placedPlanets.contains(index)) {
                    sb.append("p");
                } else if (PlanetSystemMath.hasAdjutantFields(index, occupiedFields)) {
                    sb.append("a");
                } else {
                    sb.append("-");
                }
            }
            log.debug(sb.toString());
        }
        for (PlanetPlacement placement : placedPlanets) {
            log.debug("planet placement location: " + placement);
        }
    }
}
