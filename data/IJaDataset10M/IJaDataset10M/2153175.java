package net.sf.gridarta.model.maparchobject;

import java.awt.Point;
import java.io.Serializable;
import net.sf.gridarta.model.direction.Direction;
import net.sf.gridarta.utils.Size2D;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Interface for MapArchObjects. <h4>Enter coordinates</h4> Enter coordinates
 * ({@link #getEnterX()}, {@link #setEnterX(int)}, {@link #getEnterY()}, {@link
 * #setEnterY(int)}) determines where a player enters this map from another map
 * when using an exit.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @author Andreas Kirschbaum
 */
public interface MapArchObject<A extends MapArchObject<A>> extends Cloneable, Serializable {

    /**
     * Resets the state of this object to the state of the given map arch
     * object.
     * @param mapArchObject the map arch object to copy the state from
     */
    void setState(@NotNull A mapArchObject);

    /**
     * Sets the map size.
     * @param mapSize the new map size
     * @xxx this does not change the size of the underlying MapModel. We
     * eventually should change that so a map size isn't stored twice.
     */
    void setMapSize(@NotNull Size2D mapSize);

    /**
     * Returns the map size.
     * @return size of the map reflected by this MapArchObject
     */
    @NotNull
    Size2D getMapSize();

    /**
     * Returns the map name.
     * @return the map name
     */
    @NotNull
    String getMapName();

    /**
     * Sets the map name.
     * @param name the new map name
     */
    void setMapName(@NotNull String name);

    /**
     * Returns the enter x coordinate.
     * @return the enter x coordinate
     */
    int getEnterX();

    /**
     * Sets the enter x coordinate.
     * @param enterX the new enter x coordinate
     */
    void setEnterX(int enterX);

    /**
     * Returns the enter y coordinate.
     * @return the enter y coordinate
     */
    int getEnterY();

    /**
     * Sets the enter y coordinate.
     * @param enterY the new enter y coordinate
     */
    void setEnterY(int enterY);

    /**
     * Returns the enter coordinates.
     * @return the enter coordinates
     */
    @NotNull
    Point getEnter();

    /**
     * Returns whether the map is an "outdoor" map.
     * @return whether the map is an "outdoor" map
     */
    boolean isOutdoor();

    /**
     * Sets whether the map is an "outdoor" map.
     * @param outdoor whether the map is an "outdoor" map
     */
    void setOutdoor(boolean outdoor);

    /**
     * Returns the reset timeout (in seconds).
     * @return the reset timeout (in seconds)
     */
    int getResetTimeout();

    /**
     * Sets the reset timeout (in seconds).
     * @param resetTimeout the new reset timeout (in seconds)
     */
    void setResetTimeout(int resetTimeout);

    /**
     * Returns the swap time (in ticks).
     * @return the swap time (in ticks)
     */
    int getSwapTime();

    /**
     * Sets the swap time (in ticks).
     * @param swapTime the swap time (in ticks)
     */
    void setSwapTime(int swapTime);

    /**
     * Returns the map's difficulty.
     * @return the map's difficulty
     */
    int getDifficulty();

    /**
     * Sets the map's difficulty.
     * @param difficulty the new difficulty for this map
     */
    void setDifficulty(int difficulty);

    /**
     * Returns whether this map uses a fixed reset.
     * @return whether this map uses a fixed reset
     */
    boolean isFixedReset();

    /**
     * Sets whether this map uses a fixed reset.
     * @param fixedReset whether this map should use a fixed reset
     */
    void setFixedReset(boolean fixedReset);

    /**
     * Returns the light / darkness of this map. (0 means fully bright)
     * @return the light / darkness of this map
     */
    int getDarkness();

    /**
     * Sets the light / darkness of this map.
     * @param darkness the new light / darkness of this map
     */
    void setDarkness(int darkness);

    /**
     * Returns the number of tile paths.
     * @return the number of tile paths
     */
    int getTilePaths();

    /**
     * Returns a tile path.
     * @param direction the tile path direction
     * @return the tile path
     */
    @NotNull
    String getTilePath(@NotNull Direction direction);

    /**
     * Sets a tile path.
     * @param direction the tile path direction
     * @param tilePath the new tile path
     */
    void setTilePath(@NotNull Direction direction, @NotNull String tilePath);

    /**
     * Creates a copy of this object.
     * @return the newly created clone of this object
     */
    @NotNull
    A clone();

    /**
     * Registers an event listener.
     * @param listener the listener to register
     */
    void addMapArchObjectListener(@NotNull MapArchObjectListener listener);

    /**
     * Unregisters an event listener.
     * @param listener the listener to unregister
     */
    void removeMapArchObjectListener(@NotNull MapArchObjectListener listener);

    /**
     * Starts a new transaction. Transactions may be nested. Transactions serve
     * the purpose of firing events to the views when more changes are known to
     * come before the view is really required to update. Each invocation of
     * this function requires its own invocation of {@link #endTransaction()}.
     * <p/> Beginning a nested transaction is a cheap operation.
     * @see #endTransaction()
     * @see #endTransaction(boolean)
     */
    void beginTransaction();

    /**
     * Ends a transaction. Invoking this method will reduce the transaction
     * depth by only 1. <p/> Ending a nested operation is a cheap operation.
     * Ending a transaction without changes also is a cheap operation. <p/> If
     * the last transaction is ended, the changes are committed. <p/> Same as
     * {@link #endTransaction(boolean) endTransaction(false)}.
     * @see #beginTransaction()
     * @see #endTransaction(boolean)
     */
    void endTransaction();

    /**
     * Ends a transaction. Invoking this method will reduce the transaction
     * depth by only 1. <p/> Ending a nested operation is a cheap operation.
     * Ending a transaction without changes also is a cheap operation. <p/> If
     * the last transaction is ended, the changes are committed. <p/> An example
     * where setting <var>fireEvent</var> to <code>true</code> is useful even
     * though the outermost transaction is not ended is when during painting the
     * UI should be updated though painting is not finished.
     * @param fireEvent <code>true</code> if an event should be fired even in
     * case this doesn't end the outermost transaction.
     * @note If the outermost transaction is ended, <var>fireEvent</var> is
     * ignored and the event is always fired.
     * @note An event is never fired when there were no changes, no matter
     * whether the outermost transaction is ended or <var>fireEvent</var> is set
     * to <code>true</code>.
     * @note If the event is fired, the internal change list is not cleared.
     * @see #beginTransaction()
     * @see #endTransaction()
     */
    void endTransaction(boolean fireEvent);

    /**
     * Appends 'text' to the map text.
     * @param text the string to add
     */
    void addText(@NotNull String text);

    /**
     * Sets the map text.
     * @param text the new map text
     */
    void setText(String text);

    /**
     * Returns the message text.
     * @return the message text
     */
    @NotNull
    String getText();

    /**
     * Updates the "Modified:" attribute in the message text.
     * @param userName the user name to include
     */
    void updateModifiedAttribute(@NotNull String userName);

    /**
     * Checks whether the given coordinate is within map bounds.
     * @param pos the coordinates to check
     * @return <code>true</code> if the given coordinates are on the map,
     *         otherwise <code>false</code> (also returns <code>false</code> if
     *         <code><var>pos</var> == null</code>)
     */
    boolean isPointValid(@Nullable Point pos);
}
