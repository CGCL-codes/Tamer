package controller;

import java.util.List;
import java.util.Map;
import java.util.Set;
import model.map.Tile;
import model.unit.Unit;

/**
 * @23 juil. 2010
 * @author Gronowski St�phane stephane.gronowski@gmail.com
 */
public interface ICOEventManager {

    /**
     * End the player turn.
     */
    public void end();

    /**
     * Begin the player turn.
     */
    public void begin();

    /**
     * Cancel the last action of the player.
     * 
     * @throws IllegalEventException
     */
    public void cancelAction() throws IllegalEventException;

    /**
     * Notify a {@link Tile} selection from the player.
     * 
     * @param selected
     *            the selected {@link Tile}.
     * @throws IllegalEventException
     */
    public void selectTile(Tile selected) throws IllegalEventException;

    /**
     * Return the moving capabilities of the Unit on the {@link Tile}.
     * 
     * @param tile
     *            the location of the {@link Unit}
     * @return return the moving capabilities of the Unit on the {@link Tile}.
     * @throws IllegalEventException
     */
    public Set<Tile> getMovingCapabilities(Tile tile) throws IllegalEventException;

    /**
     * Return the attack capabilities of the Unit on the {@link Tile}.
     * 
     * @param tile
     *            the location of the {@link Unit}
     * @return return the attack capabilities of the Unit on the {@link Tile}.
     * @throws IllegalEventException
     * 
     */
    public Set<Tile> getAllAttackCapabilities(Tile tile) throws IllegalEventException;

    /**
     * Ask to Build a {@link Unit} .
     * 
     * @param unit
     *            the {@link Unit}
     * @throws IllegalEventException
     */
    public void buidUnit(Unit unit) throws IllegalEventException;

    /**
     * Ask to move a {@link Unit} following the specified path.
     * 
     * @param path
     *            the path to follow
     * @throws IllegalEventException
     */
    public void moveUnit(List<Tile> path) throws IllegalEventException;

    /**
     * End the turn for the {@link Unit}
     * 
     * @throws IllegalEventException
     */
    public void stopUnit() throws IllegalEventException;

    /**
     * Destroy a {@link Unit}, must be one of the current player
     * 
     * @throws IllegalEventException
     */
    public void destroyUnit() throws IllegalEventException;

    /**
     * Activate the Stealth mode of the {@link Unit}
     * 
     * @throws IllegalEventException
     */
    public void activateStealthMode() throws IllegalEventException;

    /**
     * Disable the Stealth mode of the {@link Unit}
     * 
     * @throws IllegalEventException
     * 
     */
    public void disableStealthMode() throws IllegalEventException;

    /**
     * Supply the {@link Tile}, and everything on it.
     * 
     * @param supplied
     *            the supplied {@link Tile}.
     * @throws IllegalEventException
     * 
     */
    public void supply(Tile supplied) throws IllegalEventException;

    /**
     * Check if the current {@link Tile} can attack other tile.
     * 
     * @return a Map with every {@link Tile} that can be attack and an
     *         estimation damage
     * @throws IllegalEventException
     * 
     */
    public Map<Tile, Integer> canAttack() throws IllegalEventException;

    /**
     * Attack a {@link Tile} : attack the {@link Unit} on it or if there is no
     * {@link Unit} attack the {@link Tile} if possible.
     * @param defender
     *            the {@link Tile} that is attacked
     * 
     * @throws IllegalEventException
     * 
     */
    public void attack(Tile defender) throws IllegalEventException;

    /**
     * Load the current {@link Unit} in the {@link Unit} located on the
     * {@link Tile} where the current {@link Unit} has been moved.
     * 
     * @throws IllegalEventException
     * 
     */
    public void loadUnit() throws IllegalEventException;

    /**
     * Drop a {@link Unit} that was loaded in the current {@link Unit}.
     * 
     * @param unit
     *            the {@link Unit} to drop
     * @param tile
     *            where to drop the {@link Unit}
     * @throws IllegalEventException
     * 
     */
    public void dropUnit(Unit unit, Tile tile) throws IllegalEventException;

    /**
     * Group the current {@link Unit} with the {@link Unit} located on the
     * {@link Tile} where the current {@link Unit} has been moved.
     * 
     * @throws IllegalEventException
     * 
     */
    public void regroupUnit() throws IllegalEventException;

    /**
     * Begin the capture of the {@link Tile}.
     * 
     * @throws IllegalEventException
     * 
     */
    public void captureTile() throws IllegalEventException;

    /**
     * Try to use the first power.
     * 
     * @throws IllegalEventException
     */
    public void useFirstPower() throws IllegalEventException;

    /**
     * Try to use the second power.
     * 
     * @throws IllegalEventException
     * 
     * 
     */
    public void useSecondPower() throws IllegalEventException;
}
