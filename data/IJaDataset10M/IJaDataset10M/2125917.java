package dsc.netgame;

import java.util.*;
import java.awt.*;
import dsc.awt.*;

/**
 * World is clientside representation of the game world. It stores
 * all world components and does various mapping between GameWindow,
 * ClientController and WorldComponent.
 *
 * @author Dodekaedron Software Creations, Inc. -- Wraith
 */
public abstract class World {

    private Hashtable worldComponents;

    private int round;

    private GameWindow gameWindow;

    private ClientController clientController;

    private WorldProperties worldProperties;

    private Alliance alliance;

    private WorldState worldState;

    /**
   * Creates empty clientside world.
   *
   * @param gw Main game window.
   * @param c Clientside controller.
   * @param wp Properties for the world.
   */
    public World(GameWindow gw, ClientController c, WorldProperties wp) {
        gameWindow = gw;
        clientController = c;
        worldProperties = wp;
        worldComponents = new Hashtable();
    }

    /**
   * Gets clientside controller. 
   *
   * @return Clientside game controller.
   */
    public ClientController getController() {
        return clientController;
    }

    /**
   * Gets all visible worldcomponents at specified location.
   *
   * @param l Location where the search for components.
   * @return All visible components at given location.
   */
    public synchronized Enumeration getComponentsByLocation(Location l) {
        Enumeration e = getWorldComponents();
        Vector v = new Vector();
        WorldComponent wc;
        while (e.hasMoreElements()) {
            wc = (WorldComponent) e.nextElement();
            if (wc.getLocation().equals(l)) v.addElement(wc);
        }
        return v.elements();
    }

    /**
   * Gets component by id.
   *
   * @param id Component ID number of the component requested.
   * @return Component with given ID, null if such component does not
   * exist or is not visible by this player.
   */
    public synchronized WorldComponent getComponentById(int id) {
        return (WorldComponent) worldComponents.get(new Integer(id));
    }

    /**
   * Gets all WorldComponents visible by this player.
   *
   * @return All WorldComponents known to this player.
   */
    public synchronized Enumeration getWorldComponents() {
        return worldComponents.elements();
    }

    /**
   * Gets all WorldComponents in a HashTable.
   *
   * @return All WorldComponents known to this player.
   */
    public synchronized Hashtable getWorldComponentsTable() {
        return ((Hashtable) worldComponents.clone());
    }

    /**
     * Adds new component to the client world hashtable.
     * Used to add new map objects in the middle of the round. 
     */
    protected synchronized void addWorldComponent(WorldComponent wc) {
        wc.setWorld(this);
        worldComponents.put(new Integer(wc.getId()), wc);
        wc.clientsideInitialization();
    }

    /**
   * Sets current worldcomponent hashtable. This method should 
   * only be called when new round packet is received from host.
   * Also calls components' initialization methods.
   */
    private synchronized void setWorldComponents(Hashtable h) {
        worldComponents = h;
        Enumeration e = worldComponents.elements();
        while (e.hasMoreElements()) {
            WorldComponent wc = (WorldComponent) e.nextElement();
            wc.clientsideInitialization();
        }
    }

    /**
   * Processes messages from host.
   *
   * @param m Message from HostWorld.
   */
    public void putControlMessage(HostWorldMessage m) {
        WorldComponent wct[];
        Hashtable ht = new Hashtable();
        if (m instanceof StartRoundMessage) {
            wct = ((StartRoundMessage) m).getComponents();
            for (int i = 0; i < wct.length; i++) {
                wct[i].setWorld(this);
                ht.put(new Integer(wct[i].getId()), wct[i]);
            }
            round = m.getRound();
            alliance = ((StartRoundMessage) m).getAlliance();
            setWorldComponents(ht);
            setWorldState(((StartRoundMessage) m).getWorldState());
            gameWindow.configTimeout(((StartRoundMessage) m).getWorldState());
            gameWindow.deactivateComponent();
            gameWindow.setAlliances(alliance);
            gameWindow.refreshMap();
            gameWindow.restartClock(((StartRoundMessage) m).getRoundTime());
            gameWindow.setModeLineText("Round " + round + " started.");
            AI ai = clientController.getAI();
            if (ai != null) {
                gameWindow.setModeLineText("AI thinking...");
                ai.doRound(this);
                gameWindow.setModeLineText("AI completed round " + round);
            }
            newRoundStarted();
        }
        if (m instanceof ModeLineTextMessage) {
            gameWindow.setModeLineText(((ModeLineTextMessage) m).getText());
        }
        if (m instanceof ResetTurnClockMessage) {
            gameWindow.restartClock(((ResetTurnClockMessage) m).getRoundTime());
        }
    }

    /**
   * Sends component message to host. Sets round information in the
   * message to be current round known to this client.
   *
   * @param m Message to be sent.
   */
    public void putComponentMessage(WorldComponentMessage m) {
        m.setRound(round);
        clientController.sendToHost(m);
    }

    /**
   * Sends world message to host. Sets round information in the 
   * message to be current round know to this client.
   */
    public void sendToHost(WorldMessage m) {
        m.setRound(round);
        clientController.sendToHost(m);
    }

    /**
   * Adds World-menu into gamewindow.
   *
   * @param m Menu to be added.
   */
    public void addWorldMenu(Menu m) {
        gameWindow.addWorldMenu(m);
    }

    /**
   * Adds given menuitem to gamewindow's options menu.
   *
   * @param mi Menuitem to be added.
   */
    public void addOptionForMenu(MenuItem mi) {
        gameWindow.addAdditionalOption(mi);
    }

    /**
   * Gets image by filename. Uses buffered loading, so same image
   * is loaded into memory just once.
   *
   * @param s Filename of the image.
   * @return Loaded image, null if not found.
   */
    public Image getImage(String s) {
        return gameWindow.getImage(s);
    }

    /**
   * Sets gamewindow into location request mode. Cursor changes into 
   * crosshair and player is asked to select location. When player
   * clicks point on the map, callback method locationSelected(Location)
   * is called in currently active WorldComponent.
   *
   */
    public void requestLocation() {
        gameWindow.requestLocation();
    }

    /**
   * Sets gamewindow into component request mode. Cursor changes into 
   * crosshair and player is asked to select location. When player
   * clicks point on the map, callback method componentSelected(Location)
   * is called in currently active WorldComponent.
   *
   */
    public void requestComponent() {
        gameWindow.requestComponent();
    }

    /**
   * Gets world properties.
   *
   * @return World properties.
   */
    public WorldProperties getWorldProperties() {
        return worldProperties;
    }

    /**
   * Repaints game map area.
   */
    public void repaintMap() {
        gameWindow.repaintMap();
    }

    /**
   * Draws map background. This default implementation does nothing.
   *
   * @param g Graphics context of game map.
   */
    public void drawBackground(ScaledGraphics g) {
    }

    /**
   * Gets horisontal size of game area.
   *
   * @return Map width in pixels.
   */
    public int getXSize() {
        return gameWindow.getMapXSize();
    }

    /**
   * Gets vertical size of game area.
   *
   * @return Map height in pixels.
   */
    public int getYSize() {
        return gameWindow.getMapYSize();
    }

    /**
   * Player explicitly quits the game. This method is usually called
   * only from GameWindow when player selects quit or close window.
   */
    public void playerQuits() {
        clientController.playerQuits();
    }

    /**
   * When new round starts, this method is called after all default
   * round initialization preparations are made. Default implementation
   * does nothing.
   */
    public void newRoundStarted() {
    }

    /**
   * Gets current round number. This value should be used only to display
   * information to player, since World superclass takes care of all
   * round information in host messages.
   */
    protected int getRound() {
        return round;
    }

    /**
   * Gets alliance information for this player.
   *
   * @return alliance information.
   */
    public Alliance getAlliance() {
        return alliance;
    }

    /**
   * Sets alliance infromation for this player.
   *
   * param a Alliance information.
   */
    public void setAlliance(Alliance a) {
        alliance = a;
    }

    /**
   * Gets currenly active component.
   *
   * @return Currently active component or null.
   */
    public WorldComponent getActiveComponent() {
        return gameWindow.getActiveComponent();
    }

    /**
   * Sets world state for this round.
   */
    private void setWorldState(WorldState ws) {
        worldState = ws;
    }

    /**
   * Gets the state of the world for this round. Note that
   * by default this state tells nothing.
   */
    public WorldState getWorldState() {
        return worldState;
    }

    /** 
     * Ends turn. Client should call this method when it no longer
     * wishes to give commands to world components.
     */
    public void endTurn() {
        gameWindow.endTurn();
    }

    /** 
     * Deactivates currenty active map component. 
     *
     */
    public void deactivateComponent() {
        gameWindow.deactivateComponent();
    }

    public void activateComponent(WorldComponent wc) {
        gameWindow.activateComponent(wc);
    }

    public void refreshMap() {
        gameWindow.refreshMap();
    }

    /**
     * Check whether given player has any visible objects on map.
     *
     * @param player number of player
     *
     * @return true if at least one object owned by player is found on map
     *
     */
    public boolean playerHasVisibleObjects(int player) {
        Enumeration e = getWorldComponents();
        while (e.hasMoreElements()) {
            WorldComponent wc = (WorldComponent) e.nextElement();
            if (wc.getOwner() == player) return true;
        }
        return false;
    }
}
