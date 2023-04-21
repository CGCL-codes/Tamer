package hu.schmidtsoft.map;

/**
 * A service that extends map functionality.
 * 
 * When a map viewer will start all map extensions
 * for the map viewer component.
 * @author rizsi
 *
 */
public interface IMapExtension {

    void init(MapInstance mapInstance);

    void start();

    void stop();

    IMapExtension getClone();
}
