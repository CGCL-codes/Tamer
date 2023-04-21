package open.gps.gopens.render.model;

import java.util.List;
import open.gps.gopens.retriever.TilesRetriever;
import open.gps.gopens.retrievers.utils.Position;
import open.gps.gopens.retrievers.utils.TileNumber;
import open.gps.gopens.route.Indication;
import open.gps.gopens.route.RouteManager;
import open.gps.gopens.route.RouteRetriever;
import open.gps.gopens.utils.Items;
import open.gps.gopens.utils.Tiles;
import android.content.Context;
import android.location.Location;

/**
 * 
 * @author Cyrille Mortier
 * @author Camille Tardy
 * @author Aurelien Lanoiselier
 *
 */
public interface RenderModel {

    /**
	 * Updates the current tiles when a scroll occurs
	 * @param scrollX Value on x of the scroll event
	 * @param scrollY Value on y of the scroll event
	 */
    void addScroll(int scrollX, int scrollY);

    /**Returns the application context*/
    Context getContext();

    /**Returns the current tiles*/
    Tiles getCurrentTiles();

    /**Returns the current latitude*/
    double getLatitude();

    /**Returns the TileNumber of the current location*/
    TileNumber getLocationTile();

    /**Returns the current longitude*/
    double getLongitude();

    /**Returns the current MainTile (the center of the view)*/
    TileNumber getMainTile();

    /**Returns the current orientation of the phone */
    float getOrientation();

    /**Returns a list of points of interest*/
    Items getPointsOfInterest();

    /**Returns the position of the location in the location tile*/
    Position getPositionOnLocationTile();

    /**Returns the route Retriever*/
    RouteRetriever getRoutRetriever();

    /**Gets the tiles surrounding the current location*/
    Tiles getTiles();

    /** Returns the tileRetriever*/
    TilesRetriever getTilesRetriever();

    /**Returns a boolean telling whether or not the trace is activated*/
    boolean getTraceActivated();

    /**Returns the current zoom*/
    int getZoom();

    /**Check if the navigation mode is enabled*/
    boolean isNavigationMode();

    /**Centers the view on the current location and updates the tiles*/
    void returnToLocation();

    /**Set the position navigation mode*/
    void setNavigationMode(boolean navigationMode);

    /**Set a list of points of interest*/
    void setPointsOfInterest(Items pois);

    /**Stop everything */
    void stop();

    /**Starts recording a GPX trace*/
    void startTrace();

    /**Stops listening to the GPS */
    void stopGPSListener();

    /**Start listening to the GPS */
    void startGPSListener();

    /**Stop recording a GPX trace*/
    void stopTrace();

    /**inverts the trace mode*/
    void switchActivateTraceMod();

    /**update the orientation with the new value and notify the changes to the
	 * observer.
	 * @param orientation
	 */
    void updade(float orientation);

    /**
	 * update the location with the new value and notify the changes to the
	 * observer.
	 * @param location
	 */
    void updade(Location location);

    /**Stores the tiles surrounding the current location in the currentTiles attributes*/
    void updateTiles();

    /**Zooms in when it's possible. Updates the tiles, The new model will be centered on the same point as the current one.*/
    void zoomIn();

    /**Zooms out when it's possible. Updates the tiles, The new model will be centered on the same point as the current one.*/
    void zoomOut();

    /**
	 * Creates a manager for the current itinerary in order to provide the directions
	 * @param startLocation Itinerary starting point 
	 * @param stopLocation Itinerary ending point
	 * @param routeType Type of the vehicle (car, cycle, ...)
	 * @param isTheFastest Boolean telling whether it is the fastest route
	 */
    void createRouteManager(Position startLocation, Position stopLocation, String routeType, boolean isTheFastest);

    /**
	 * Returns the route manager
	 * @return The route manager
	 */
    RouteManager getRouteManager();

    /**
	 * 
	 * @return
	 */
    List<Indication> getListIndications();

    /**
	 * 
	 * @return
	 */
    int getCurrentIndicationIndex();

    /**
	 * 
	 * @return
	 */
    int getClosestDistantIndicationIndex();

    /**
	 * 
	 * @return
	 */
    List<String> getListDirections();

    /**
	 * 
	 * @return 
	 */
    List<Integer> getListDistances();

    /**
	 * @return
	 */
    boolean isLost();

    /**
	 * @return
	 */
    float getDistToCurrentIndication();
}
