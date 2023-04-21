package net.xelnaga.screplay.interfaces;

/**
 * The <code>IReplayMap</code> interface is implemented by objects representing
 * a replay map.
 *
 * @author Russell Wilson
 *
 */
public interface IReplayMap {

    /**
     * Returns the map name.
     * 
     * @return the map name.
     */
    String getName();

    /**
     * Returns the map width.
     *
     * @return the map width.
     */
    int getWidth();

    /**
     * Returns the map height.
     *
     * @return the map height.
     */
    int getHeight();
}
