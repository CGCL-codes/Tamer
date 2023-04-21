package org.freeorion.api;

import org.freeorion.Orion;
import org.freeorion.util.Textbox;
import org.freeorion.util.Toolbox;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import java.lang.reflect.Method;

/** The MasterController (aka Master Control Unit, Central Orion
 * Engine or "Master of Orion") is the point through which the "Game
 * Layer" (a GUI or an AI-Player) communicates with "Free Orion API".
 * From here the "Game Layer" gets access to the Universe with its
 * StarSystems and to the list of Players, Colonies, etc.
 *
 * There is exactly one Conroller per "Game Layer" and it is responsible for
 * restricting the view of this "Game Layer" to what it is allowed to see.
 */
public class MasterController extends BaseController {

    /** Create the (one and only) Master Controller.
	 * @param size The size of the Universe.
	 */
    MasterController(int size) {
        super(size);
        status = STATUS_SERVER_INIT;
        super.initialize();
        universe.genesis();
    }

    /** This methods initializes the MasterController. It needs to be
	 * called after all Players have been added to this Controller.
	 */
    public void finishInitialization() {
        Enumeration stars = universe.getStarSystems();
        Enumeration players = getPlayers();
        while (players.hasMoreElements() && stars.hasMoreElements()) {
            Player player = (Player) players.nextElement();
            CelestialBody body = (CelestialBody) stars.nextElement();
            if (!(body instanceof StarSystem)) continue;
            AlienType type = player.getAlienType();
            StarSystem star = (StarSystem) body;
            Universe.Wormhole worm = universe.wormholeAtStar(star);
            if (worm != null) universe._removeWormhole(worm);
            int home = Toolbox.getRandomInt(StarSystem.MAX_PLANETS);
            star.setName(type.getHomeSystemName());
            int climate = (type.hasAttribute(AlienType.AQUATIC)) ? Planet.CLIMATE_OCEAN : Planet.CLIMATE_TERRAN;
            int gravity;
            if (type.getAttribute(AlienType.HOME_GRAVITY) == AlienType.HOME_GRAVITY_LOW) gravity = Planet.GRAVITY_LOW; else if (type.getAttribute(AlienType.HOME_GRAVITY) == AlienType.HOME_GRAVITY_HIGH) gravity = Planet.GRAVITY_HIGH; else gravity = Planet.GRAVITY_NORMAL;
            int minerals;
            if (type.getAttribute(AlienType.HOME_MINERALS) == AlienType.HOME_MINERALS_POOR) minerals = Planet.MINERALS_POOR; else if (type.getAttribute(AlienType.HOME_MINERALS) == AlienType.HOME_MINERALS_RICH) minerals = Planet.MINERALS_RICH; else minerals = Planet.MINERALS_ABUNDANT;
            int planetSize = (type.hasAttribute(AlienType.HOME_LARGE)) ? Planet.SIZE_LARGE : Planet.SIZE_MEDIUM;
            Planet planet = new Planet(this, star, home, climate, gravity, minerals, planetSize);
            star.setPlanetoid(planet, home);
            Colony colony = new Colony(this, planet, player);
            player._populateHomeColony(this, colony);
            colony.addBuilding(Building.MARINE_BARRACKS);
            colony.addBuilding(Building.STAR_BASE);
            player.addTechnology(Building.COLONY_BASE);
            player.addTechnology(Building.MARINE_BARRACKS);
            player.addTechnology(Building.HYDROPONIC_FARM);
            player.addShipDesign(new ShipDesign(this, "COLONY_SHIP", player));
            player.addShipDesign(new ShipDesign(this, "OUTPOST_SHIP", player));
            player.addShipDesign(new ShipDesign(this, "TRANSPORT_SHIP", player));
        }
        players = getPlayers();
        while (players.hasMoreElements()) {
            Player player = (Player) players.nextElement();
            Enumeration colonies = player.getColonies();
            while (colonies.hasMoreElements()) {
                Colony colony = (Colony) colonies.nextElement();
                Coordinate position = colony.getPlanet().getStarSystem().getCoordinate();
                universe.addShip(new ColonyShip(this, player, position));
                universe.addShip(new OutpostShip(this, player, position));
                universe.addShip(new TransportShip(this, player, position));
                universe.addShip(new CombatShip(this, player, position, CombatShip.SIZE_BATTLESHIP));
                Orion.DEBUG("Four new Ships at " + colony.getPlanet().getName());
            }
        }
        status = STATUS_SERVER_SENDING_TURN_DATA;
    }

    /** Signals this MasterController, that it shall wait for the
	 * Clients to finish their turns.
	 */
    public void waitForClients() {
        checkStatus(STATUS_SERVER_SENDING_TURN_DATA);
        status = STATUS_SERVER_WAITING;
    }

    /** Signals this MasterController, that it shall be prepared to
	 * receive Turn Commands.
	 */
    public void startTurnCommands() {
        checkStatus(STATUS_SERVER_WAITING);
        thePlayer = null;
        update(OrionObject.UPDATE_SECONDARY);
        status = STATUS_SERVER_RUNNING;
    }

    /** Process turn commands which have been received from a Client.
	 */
    public void processTurnCommandsFor(CommandList commands, Player player) {
        checkStatus(STATUS_SERVER_RUNNING);
        thePlayer = player;
        update(OrionObject.UPDATE_SECONDARY_PLAYER);
        update(OrionObject.UPDATE_ACCESS_COMMANDS);
        processor.process(commands);
        thePlayer = null;
    }

    /** Recieves an ID from the command processor. This method should
	 * only be called by constructors of OrionObjects, that are
	 * constructed during the execution of a CommandList. For a
	 * MasterController this should only occur when evaluating the
	 * Clients commands (STATUS_SERVER_RUNNING).
	 *
	 * @return An ID read from the CommandProcessor.
	 */
    public String retrieveObjectId() {
        checkStatus(STATUS_SERVER_RUNNING);
        return processor.getNextId();
    }

    /** Signals this MasterController, that all turn commands have
	 * been transmittet and that no more turn commands will follow in
	 * this turn.
	 */
    public void endTurnCommands() {
        checkStatus(STATUS_SERVER_RUNNING);
        status = STATUS_SERVER_END_OF_TURN_ACTIONS;
    }

    /** Performs the actions at the end of a turn.
	 */
    public void performEndOfTurnActions() {
        checkStatus(STATUS_SERVER_END_OF_TURN_ACTIONS);
        thePlayer = null;
        update(OrionObject.UPDATE_SECONDARY);
        update(OrionObject.UPDATE_ACCESS_DUMP);
        moveShips();
        buildColonyConstructions();
        performResearch();
        populationGrowth();
        status = STATUS_SERVER_SENDING_TURN_DATA;
    }

    private void performResearch() {
        Enumeration enumeration = players.elements();
        while (enumeration.hasMoreElements()) {
            thePlayer = (Player) enumeration.nextElement();
            thePlayer.getTechnologyTree().research(thePlayer.getOverallResearch());
        }
        thePlayer = null;
    }

    private void buildColonyConstructions() {
        Enumeration player = players.elements();
        while (player.hasMoreElements()) {
            thePlayer = (Player) player.nextElement();
            Enumeration colonies = thePlayer.getColonies();
            while (colonies.hasMoreElements()) {
                Colony colony = (Colony) colonies.nextElement();
                colony.buildConstruction();
            }
        }
        thePlayer = null;
    }

    private void populationGrowth() {
        Enumeration player = players.elements();
        while (player.hasMoreElements()) {
            thePlayer = (Player) player.nextElement();
            Enumeration colonies = thePlayer.getColonies();
            while (colonies.hasMoreElements()) {
                Colony colony = (Colony) colonies.nextElement();
                colony.growPopulation();
            }
        }
        thePlayer = null;
    }

    private void moveShips() {
        Orion.DEBUG("MasterController: Moving all ships ...");
        Enumeration ships = universe.getShips();
        while (ships.hasMoreElements()) {
            Ship ship = (Ship) ships.nextElement();
            Orion.DEBUG("Ship " + ship.getId() + ": " + (ship.isMoving() ? "moves." : "doesn't want to move."));
            ship.move();
        }
    }

    public void dumpForPlayer(OrionCommands processor, Player player) {
        checkStatus(STATUS_SERVER_SENDING_TURN_DATA);
        Player p = thePlayer;
        thePlayer = player;
        update(OrionObject.UPDATE_ACCESS_DUMP);
        super.dump(processor);
        thePlayer = p;
    }
}
