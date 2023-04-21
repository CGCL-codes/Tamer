package net.sourceforge.strategema.games;

import net.sourceforge.strategema.util.datastructures.HashMapTableModel;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class Player implements Serializable {

    /** Serialization */
    private static final long serialVersionUID = -6482744209525316352L;

    /** Logger */
    private static final Logger LOG = Logger.getLogger(Player.class.getName());

    /** Key for the time handicap field. */
    public static final String TIME_HANDICAP = "Multiplicative Time Handicap";

    /** Key for AI player clock rate. */
    public static final String CLOCK_RATE = "Clock Rate";

    /** The player's personal attributes. */
    private final HashMapTableModel<String, String> personalInfo = new HashMapTableModel<String, String>(String.class, String.class, Collections.singleton("ID"));

    /** The player's character attributes. */
    private final HashMapTableModel<String, Serializable> characterAttributes = new HashMapTableModel<String, Serializable>(String.class, Serializable.class);

    /** The player's statistics. */
    private final HashMapTableModel<String, Float> playerStats = new HashMapTableModel<String, Float>(String.class, Float.class);

    /** The player's public inventory. */
    private List<Serializable> publicInventory = new ArrayList<Serializable>();

    /** The player's private inventory. */
    private List<Serializable> privateInventory = new ArrayList<Serializable>();

    /** Flags for the player's situation during the present turn. */
    private Set<PlayerCondition> situation = new HashSet<PlayerCondition>();

    /** Miscellaneous game state data. Includes the score, which is the only publicly displayed item. */
    private Map<String, Serializable> gameData = new HashMap<String, Serializable>();

    /** The armies that this player controls. */
    private Set<Integer> colours = new HashSet<Integer>();

    /** The game being played. */
    public final String game;

    /** How the player is controlled - human or AI. */
    private transient InputController controller;

    /** Cached so that it survives serialization. */
    private String controllerName = null;

    /** True if the player has "started", e.g. rolled a six to begin if necessary. */
    public boolean hasStarted = true;

    /**
	 * Creates a new player template.
	 * @param name The name of the new player.
	 */
    public Player(final String name) {
        this.personalInfo.put("Name", name);
        this.personalInfo.put("ID", Long.toHexString((long) (Math.random() * (Long.MAX_VALUE - 1) + 1)).toUpperCase(Locale.US));
        this.game = null;
    }

    /**
	 * Creates a new player with name {@code name} who plays game {@code game}.
	 * 
	 * @param name The name of the new player.
	 * @param game The game that the player will play.
	 */
    public Player(final String name, final String game) {
        this.personalInfo.put("Name", name);
        this.personalInfo.put("ID", Long.toHexString((long) (Math.random() * (Long.MAX_VALUE - 1) + 1)).toUpperCase(Locale.US));
        this.game = game;
    }

    /**
	 * Creates a new player using the personal attributes of an existing player, perhaps from a
	 * different game.
	 * 
	 * @param defaultPlayer The player to inherit the personal attributes from.
	 * @param game The game that the new player will play.
	 */
    public Player(final Player defaultPlayer, final String game) {
        this.personalInfo.putAll(defaultPlayer.personalInfo);
        this.game = game;
        this.controllerName = defaultPlayer.controllerName;
    }

    /**
	 * Copies a player for the AI to use.
	 * @param human The human player to create an AI copy for.
	 */
    public Player(final Player human) {
        this.personalInfo.put("Name", "AI " + human.personalInfo.get("Name"));
        this.personalInfo.put("ID", "0");
        this.characterAttributes.putAll(human.characterAttributes);
        this.publicInventory.addAll(human.publicInventory);
        this.privateInventory.addAll(human.privateInventory);
        this.gameData.putAll(human.gameData);
        this.colours.addAll(human.colours);
        this.game = human.game;
        this.hasStarted = human.hasStarted;
    }

    @Override
    public final Player clone() {
        final Player clone = new Player(this);
        final String curName = this.personalInfo.get("Name");
        if (!curName.endsWith(" (historical")) {
            clone.personalInfo.put("Name", curName + " (historical)");
        }
        return clone;
    }

    /**
	 * Gets the player's name.
	 * @return The player's name.
	 */
    public String getName() {
        return this.personalInfo.get("Name");
    }

    /**
	 * Gets the player's unique ID number.
	 * @return The player's ID number.
	 */
    public long getID() {
        return Long.parseLong(this.personalInfo.get("ID"), 16);
    }

    /**
	 * Enrolls the player to play a new round.
	 * @param game The game to be played.
	 * @throws IncorrectGameException If this player plays a different game to the one requested.
	 * @throws NullPointerException If {@code game} is null.
	 */
    public void enroll(final String game) throws IncorrectGameException, NullPointerException {
        if (game == null) {
            throw new NullPointerException("game");
        } else if (!game.equals(this.game)) {
            throw new IncorrectGameException(this.personalInfo.get("Name"), game, this.game);
        }
        this.colours.clear();
        this.situation.clear();
    }

    /**
	 * Relinquishes control of all armies currently assigned to the player.
	 * @return True if the player had some armies to relinquish, or false if they didn't.
	 */
    boolean clearArmies() {
        final boolean hadArmies = !this.colours.isEmpty();
        this.colours.clear();
        return hadArmies;
    }

    /**
	 * Lets the player take control of an additional army.
	 * @param colour The army to take control of.
	 */
    public void takeControlOfArmy(final int colour) {
        this.colours.add(colour);
    }

    /**
	 * Transfers control of an army from this player to another player.
	 * @param colour The army to transfer.
	 * @param toPlayer The player to transfer the army to.
	 * @throws IllegalArgumentException If this player does not control the specified army.
	 */
    public void transferControlOfArmy(final int colour, final Player toPlayer) throws IllegalArgumentException {
        if (!this.colours.contains(colour)) {
            throw new IllegalArgumentException("Player does not control colour " + Integer.toString(colour));
        }
        this.colours.remove(colour);
        toPlayer.colours.add(colour);
    }

    /**
	 * Gets the armies controlled by this player.
	 * @return This player's colours.
	 */
    public Set<Integer> getArmies() {
        return Collections.unmodifiableSet(this.colours);
    }

    /**
	 * Swaps position with another player.
	 * @param p2 The player to swap with.
	 */
    public void swap(final Player p2) {
        final Set<Integer> p1Colours = this.colours;
        this.colours = p2.colours;
        p2.colours = p1Colours;
        final List<Serializable> p1PublicInv = this.publicInventory;
        this.publicInventory = p2.publicInventory;
        p2.publicInventory = p1PublicInv;
        final List<Serializable> p1PrivateInv = this.privateInventory;
        this.privateInventory = p2.privateInventory;
        p2.privateInventory = p1PrivateInv;
        final Set<PlayerCondition> p1Situation = this.situation;
        this.situation = p2.situation;
        p2.situation = p1Situation;
        final Map<String, Serializable> p1GameData = this.gameData;
        this.gameData = p2.gameData;
        p2.gameData = p1GameData;
        final boolean p1HasStarted = this.hasStarted;
        this.hasStarted = p2.hasStarted;
        p2.hasStarted = p1HasStarted;
    }

    /**
	 * Deletes all data about the current round or tournament, excluding inventories.
	 */
    public void clearMatchData() {
        this.gameData.clear();
        this.colours.clear();
        this.situation.clear();
    }

    /**
	 * Removes all items from the player's inventories.
	 */
    public void clearInventories() {
        this.publicInventory.clear();
        this.privateInventory.clear();
    }

    /**
	 * Retrieves the value of a character attribute.
	 * 
	 * @param attrib The attribute to retrieve.
	 * @return The value of the requested character attribute, or null if no value has been set.
	 */
    public final Serializable getAttribute(final String attrib) {
        return this.characterAttributes.get(attrib);
    }

    /**
	 * Sets the value of a character attribute.
	 * 
	 * @param attrib The attribute to update.
	 * @param value The value to assign to the character attribute.
	 */
    public final void setAttribute(final String attrib, final Serializable value) {
        this.characterAttributes.put(attrib, value);
    }

    /**
	 * Initializes a player statistic for a new player.
	 * 
	 * @param stat The name of the statistic to initialize.
	 * @param value The initialization value.
	 * @throws IllegalArgumentException if specified statistic has already been initialized.
	 */
    public final void initializeStatistic(final String stat, final float value) throws IllegalArgumentException {
        if (this.playerStats.containsKey(stat)) {
            throw new IllegalArgumentException("Error initializing player statistic: '" + stat + "' has already been initialized for player " + this.personalInfo.get("Name"));
        } else {
            this.playerStats.put(stat, value);
        }
    }

    /**
	 * Adjusts a player statistic.
	 * 
	 * @param stat The name of the statistic to update.
	 * @param delta The amount to change the statistic by.
	 */
    public final void updateStatistic(final String stat, final float delta) {
        this.playerStats.put(stat, this.playerStats.get(stat) + delta);
    }

    /**
	 * Resets all player statistics to zero.
	 */
    public final void resetStatistics() {
        this.playerStats.clear();
    }

    /**
	 * Raises a condition.
	 * @param condition The condition to raise.
	 */
    public final void raiseCondition(final PlayerCondition condition) {
        this.situation.add(condition);
    }

    /**
	 * Clears a condition.
	 * @param condition The condition to clear.
	 */
    public final void clearCondition(final PlayerCondition condition) {
        this.situation.remove(condition);
    }

    /**
	 * Tests if a condition applies to this player.
	 * @param condition The condition to test for.
	 * @return True if the condition applies, and false otherwise.
	 */
    public boolean conditionApplies(final PlayerCondition condition) {
        return this.situation.contains(condition);
    }

    /**
	 * Clears all turn-based conditions ready for a new turn.
	 */
    void clearTurnBasedConditions() {
        final Iterator<PlayerCondition> conditionIter = this.situation.iterator();
        while (conditionIter.hasNext()) {
            final PlayerCondition condition = conditionIter.next();
            if (condition.turnBased) {
                conditionIter.remove();
            }
        }
    }

    /**
	 * Retrieve a value about the player's current position in the game.
	 * 
	 * @param key The field to look up.
	 * @return The value of the specified field, or null if it has not been set.
	 */
    public final Serializable getValue(final String key) {
        return this.gameData.get(key);
    }

    /**
	 * Update the player's current position in the game.
	 * 
	 * @param key The field to update.
	 * @param value The value to assign to {@code key}
	 */
    public final void setValue(final String key, final Serializable value) {
        this.gameData.put(key, value);
    }

    /**
	 * Retrieves the player's public inventory.
	 * 
	 * @return The public inventory.
	 */
    public final Collection<Serializable> publicInventory() {
        return this.publicInventory;
    }

    /**
	 * Retrieves the items from the player's public inventory that match the specified type.
	 * @param <T> The type of item desired.
	 * @param cl The type of item desired.
	 * @return The items from the player's public inventory that match the specified type.
	 */
    @SuppressWarnings("unchecked")
    public <T extends Serializable> Collection<T> getPublicItems(final Class<T> cl) {
        final Collection<T> result = new ArrayList<T>(Math.min(this.publicInventory.size(), 10));
        for (final Serializable item : this.publicInventory) {
            if (cl.isInstance(item)) {
                result.add((T) item);
            }
        }
        return result;
    }

    /**
	 * Uses an item from the inventory (removes it).
	 * @param item The item being used.
	 * @param preferPublic Determines which inventory to take the item from if it is available from
	 * both the public inventory and the private inventory.
	 * @throws ItemNotHeldException If the player does not possess the item in their public or private
	 * inventories.
	 */
    public void useItem(final Serializable item, final boolean preferPublic) throws ItemNotHeldException {
        final boolean inPublic = this.publicInventory.contains(item);
        final boolean inPrivate = this.privateInventory.contains(item);
        if (!inPublic && !inPrivate) {
            throw new ItemNotHeldException(this, item);
        } else if (inPublic && inPrivate) {
            if (preferPublic) {
                this.publicInventory.remove(item);
            } else {
                this.privateInventory.remove(item);
            }
        } else if (inPublic) {
            this.publicInventory.remove(item);
        } else {
            this.privateInventory.remove(item);
        }
    }

    /**
	 * Retrieves the player's private inventory.
	 * 
	 * @return The private inventory.
	 */
    public final Collection<Serializable> privateInventory() {
        return this.privateInventory;
    }

    /**
	 * Moves an item from the player's private inventory into their public inventory.
	 * 
	 * @param item The the item to reveal to the other players.
	 * @throws IllegalArgumentException If the player does not have the specified item in their
	 * private inventory.
	 */
    public void revealItem(final Serializable item) throws IllegalArgumentException {
        if (!this.privateInventory.contains(item)) {
            throw new IllegalArgumentException("Player " + this.personalInfo.get("Name") + " does not have a " + item.toString());
        } else {
            this.privateInventory.remove(item);
            this.publicInventory.add(item);
        }
    }

    /**
	 * Gets the player's score for computing the high-score table. A score is either one of the
	 * player's statistics or one of their character attributes.
	 * 
	 * @param field The name of the statistic or character attribute.
	 * @return The player's score for the requested statistic or character attribute, or null if none
	 * is set.
	 */
    public final Serializable getHighScore(final String field) {
        final Serializable value = this.playerStats.get(field);
        if (value != null) {
            return value;
        } else {
            return this.characterAttributes.get(field);
        }
    }

    /**
	 * Displays important player features in the main game window.
	 */
    public void showPlayer() {
    }

    /**
	 * Displays a window that allows the user to update their personal information.
	 */
    public void editPersonalAttributes() {
    }

    /**
	 * Displays the player's personal details and statistics in a dialog box.
	 */
    public void showAbout() {
    }

    /**
	 * Writes a very short description of the player into the game log.
	 * 
	 * @param out The destination to write to.
	 * @throws IOException If the destination throws IOException.
	 */
    public void logHandle(final Appendable out) throws IOException {
        Game<?> game = null;
        try {
            game = GameFactory.FACTORY.getGame(this.game);
        } catch (final ClassNotFoundException e) {
            Player.LOG.info(e.toString());
        }
        final String name = this.personalInfo.get("Name");
        final String nameAbbrev;
        if (name.length() <= 18) {
            nameAbbrev = name;
        } else {
            nameAbbrev = name.substring(0, 15) + "...";
        }
        out.append(nameAbbrev);
        if (!this.colours.isEmpty()) {
            out.append(" (");
            if (game == null) {
                out.append("Team ");
                for (final int colour : this.colours) {
                    out.append(Integer.toString(colour));
                    out.append(", ");
                }
            } else {
                for (final int colour : this.colours) {
                    out.append(game.formatColour(colour));
                    out.append(", ");
                }
            }
            out.append(')');
        }
    }

    /**
	 * Writes a full description of the player into the game log.
	 * 
	 * @param out The destination to write to.
	 * @throws IOException If the destination throws IOException.
	 */
    public void logDetails(final Appendable out) throws IOException {
        Game<?> game = null;
        try {
            game = GameFactory.FACTORY.getGame(this.game);
        } catch (final ClassNotFoundException e) {
            Player.LOG.info(e.toString());
        }
        out.append(this.personalInfo.get("Name"));
        out.append("\t(");
        out.append(this.controllerName);
        if (!this.colours.isEmpty()) {
            out.append(" - ");
            if (game == null) {
                out.append("Team ");
                for (final int colour : this.colours) {
                    out.append(Integer.toString(colour));
                    out.append(", ");
                }
            } else {
                for (final int colour : this.colours) {
                    out.append(game.formatColour(colour));
                    out.append(", ");
                }
            }
        }
        out.append(")\n");
        if (this.controller != null) {
            out.append(this.controller.getName());
            out.append('\n');
        }
        int cols = 0;
        for (final String fieldName : this.personalInfo.keySet()) {
            cols = Math.max(fieldName.length(), cols);
        }
        for (final String fieldName : this.characterAttributes.keySet()) {
            cols = Math.max(fieldName.length(), cols);
        }
        for (final String fieldName : this.playerStats.keySet()) {
            cols = Math.max(fieldName.length(), cols);
        }
        cols = cols + 2;
        for (final Map.Entry<String, String> item : this.personalInfo.entrySet()) {
            final String fieldName = item.getKey();
            if (fieldName.equals("Name")) {
                continue;
            }
            out.append(fieldName);
            out.append(String.format("%1$#" + Integer.toString(cols - fieldName.length()) + "s", item.getValue()));
            out.append('\n');
        }
        for (final Map.Entry<String, Serializable> item : this.characterAttributes.entrySet()) {
            final String fieldName = item.getKey();
            out.append(fieldName);
            out.append(String.format("%1$#" + Integer.toString(cols - fieldName.length()) + "s", item.getValue().toString()));
            out.append('\n');
        }
        for (final Map.Entry<String, Float> item : this.playerStats.entrySet()) {
            final String fieldName = item.getKey();
            out.append(fieldName);
            out.append(String.format("%1$#" + Integer.toString(cols - fieldName.length()) + "s", item.getValue().toString()));
            out.append('\n');
        }
        out.append("Inventory:\n");
        for (final Serializable item : this.publicInventory) {
            out.append(item.toString());
            out.append('\n');
        }
    }

    /**
	 * Returns the player's current input controller.
	 * @return The player's input controller.
	 */
    public InputController getController() {
        return this.controller;
    }

    /**
	 * Tests if the player is located on this machine.
	 * @return True if the player is a local player, or false if the player is a network player.
	 */
    public boolean isLocal() {
        return this.controller != null;
    }

    /**
	 * Tests if the player is a human player.
	 * @return True if this player is a human player, or false for an AI player.
	 */
    public boolean isHuman() {
        return this.controllerName.equals("Human");
    }

    /**
	 * Sets the player's input controller.
	 * @param controller The player's input controller
	 * @throws IllegalArgumentException If a request is made to set a human player's controller to an
	 * AI algorithm.
	 */
    public void setController(final InputController controller) {
        final String shortName = controller.getShortName();
        if (this.controllerName != null && this.controllerName.equals("Human") && !shortName.equals("Human")) {
            throw new IllegalArgumentException("Human player cannot be controlled by AI.");
        }
        this.controller = controller;
        this.controllerName = shortName;
    }
}
