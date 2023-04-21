package net.sourceforge.freecol.gui.panel;

import java.awt.Component;
import net.sourceforge.freecol.common.MapListener;
import net.sourceforge.freecol.common.Player;
import net.sourceforge.freecol.common.Unit;

/**
* Contains and manages Panels from the package
* net.sourceforge.freecol.gui.panel.
*/
public interface PanelContainer extends MapListener {

    /**
    * Gets called when the user wants to start a new game.
    */
    void newGame();

    /**
    * Gets called when the user wants to continue an existing game.
    */
    void openGame();

    /**
    * Gets called when a certain component needs to be removed from this
    * PanelContainer.
    * @param comp The Component to remove from this PanelContainer.
    */
    void remove(Component comp);

    /**
    * Gets called when the user wants to disembark one of his units in the
    * given direction.
    * @param direction The direction in which to disembark the unit.
    */
    void disembarkActiveUnit(int direction);

    /**
    * Gets called when the user wants to embark a unit with another one in
    * the given direction.
    * @param direction The direction in which to embark.
    * @param unitId The Id of the unit to embark.
    */
    void embarkActiveUnit(int direction, int unitId);

    /**
    * Sends the currently active unit to Europe. This method will be called
    * when we have confirmation from the user.
    */
    void moveToEurope();

    /**
    * Gets called when the multiplayer panel needs to appear on the screen.
    */
    void openMultiplayerPanel();

    /**
    * Should return the correct child panel.
    * @return The single player panel that lies in this container.
    */
    SinglePlayerPanel getSinglePlayerPanel();

    /**
    * Should return the correct child panel.
    * @return The multiplayer panel that lies in this container.
    */
    MultiplayerPanel getMultiplayerPanel();

    /**
    * Asks the panel container if the user is playing in single player mode.
    * @return 'true' if the user is playing in single player mode, 'false' otherwise.
    */
    boolean isSinglePlayerGame();

    /**
    * Gets called when the single player panel needs to appear on the screen.
    */
    void openSinglePlayerPanel();

    /**
    * Gets called when we receive information telling us that the game
    * is starting.
    * @param firstPlayerName The name of the player who will go first.
    */
    void startGame(String firstPlayerName);

    /**
    * Gets called when an error message needs to be displayed.
    * @param message The error message that needs to be displayed.
    */
    void errorMessage(String message);

    /**
    * Gets called when the user wants to play a single player game.
    * @param name The name of the player.
    */
    void singlePlayer(String name);

    /**
    * Gets called when the user wants to join a multiplayer game
    * that is hosted at a server that is already running.
    * @param host The host to connect to.
    * @param port The port to connect to.
    * @param name The name to use when logging in.
    */
    void multiPlayer(String host, int port, String name);

    /**
    * Gets called when the user wants to start a multiplayer game. A
    * server should be started locally and other players should be able
    * to join.
    */
    void startServer(int port);

    /**
    * Gets called when a chat message needs to be displayed to the user.
    * @param sender The player who sent the chat message to the server.
    * @param message The chat message.
    * @param privateChat 'true' if the message is a private one, 'false' otherwise.
    */
    void displayChatMessage(Player sender, String message, boolean privateChat);

    /**
    * Gets called when the GUI needs to be made ready to process new incoming
    * commands. Most of the time this doesn't need to be done explicitly. Methods
    * such as disembarkActiveUnit will make sure that the server sends a conformation
    * message (UpdateMap) and notification will automatically happen when handling
    * such messages.
    * The most common case in which to call this method is when the user starts an
    * action (for example disembark) and then aborts the action. If that happens then
    * there will be no interaction with the server so notification needs to be done
    * manually with this method.
    */
    void notifyNext();

    /**
    * Gets called when the program needs to quit. There will be no question asked to
    * the user so only call this method if you already have the user's confirmation.
    */
    void quit();

    /**
    * Gets called when the user first asked to quit. This method should ask the user
    * if he's sure he wants to quit.
    */
    void requestQuit();

    /**
    * Gets called when the user wants to send a ship from Europe to America.
    * @param unit The ship to send back to America.
    */
    void sendToAmerica(Unit unit);

    /**
    * Gets called when the user wants to send a ship from America to Europe.
    * @param unit The ship to send back to Europe.
    */
    void sendToEurope(Unit unit);

    /**
    * Gets called when a unit needs to be placed on the docks in Europe.
    * @param unit The unit to place on the docks in Europe.
    */
    void sendToDocks(Unit unit);

    /**
    * Gets called when a unit needs to board another unit (perhaps when in Europe).
    * This method should not change the map.
    * @param ship The unit to board.
    * @param unit The unit that will board.
    */
    void boardShip(Unit ship, Unit unit);

    /**
    * Gets called when this PanelContainer needs to show its map controls on the screen.
    */
    void showMapControls();

    /**
    * Gets called when the user closes the error panel.
    */
    void closeErrorPanel();

    /**
    * Gets called when the panel container needs to repaint itself.
    */
    void refresh();

    /**
    * Gets called when the user wants to recruit a unit in Europe.
    * @param slot The slot of the unit to recruit. Must be 1, 2 or 3.
    */
    void recruit(int slot);

    /**
    * Gets called when the user wants to train a unit in Europe.
    * @param type The type of the unit to train.
    */
    void train(int type);

    /**
    * Gets called when the user wants to purchase a unit in Europe.
    * @param type The type of the unit to purchase.
    */
    void purchase(int type);

    /**
    * Gets called when the user wants to trade goods in Europe.
    * @param buy 'true' if the goods should be bought, 'false' if they should be sold.
    * @param type The type of goods to trade.
    * @param unit The unit that will carry or sell the goods.
    * @param amount The amount to trade of that specific type.
    */
    void trade(boolean buy, int type, Unit unit, int amount);

    /**
    * Gets called when the user wants to dress a unit as a missionary or
    * when he wants a unit to take off the missionary clothes.
    * @param unit The unit in question.
    */
    void dressAsMissionary(Unit unit);

    /**
    * Returns the image provider that is being used by this panel container.
    * @return The image provider that is being used by this panel container.
    */
    ImageProvider getImageProvider();
}
