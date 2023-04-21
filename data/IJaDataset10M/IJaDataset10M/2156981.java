package wotlas.common.universe;

import java.awt.Rectangle;
import wotlas.common.WorldManager;
import wotlas.common.router.MessageRouter;
import wotlas.common.router.MessageRouterFactory;
import wotlas.libs.graphics2d.ImageIdentifier;
import wotlas.utils.Debug;
import wotlas.utils.ScreenPoint;
import wotlas.utils.ScreenRectangle;

/** A TownMap represents a town in our Game Universe.
 *
 * @author Petrus, Aldiss
 * @see wotlas.common.universe.WorldMap
 * @see wotlas.common.universe.Building
 */
public class TownMap extends ScreenRectangle implements WotlasMap {

    /** ID of the TownMap (index in the array of townmaps in the WorldMap)
     */
    private int townMapID;

    /** Full name of the Town
     */
    private String fullName;

    /** Short name of the Town
     */
    private String shortName;

    /** Small Image (identifier) of this town for WorldMaps.
     */
    private ImageIdentifier smallTownImage;

    /** Full Image (identifier) of this town
     */
    private ImageIdentifier townImage;

    /** Point of insertion (teleportation, arrival)
     */
    private ScreenPoint insertionPoint;

    /** Music Name
     */
    private String musicName;

    /** Map exits...
     */
    private MapExit[] mapExits;

    /** Link to the worldMap we belong to...
     */
    private transient WorldMap myWorldMap;

    /** Array of Building
     */
    private transient Building[] buildings;

    /** Our message router. Owns the list of players of this map (not in buildings).
     */
    private transient MessageRouter messageRouter;

    /** Constructor for persistence.
     */
    public TownMap() {
    }

    /** Constructor with x,y positions & width,height dimension on WorldMap.
     * @param x x position of this building on a WorldMap.
     * @param y y position of this building on a WorldMap.
     * @param width width dimension of this building on a WorldMap.
     * @param height height dimension of this building on a WorldMap.
     */
    public TownMap(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public void setTownMapID(int myTownMapID) {
        this.townMapID = myTownMapID;
    }

    public int getTownMapID() {
        return this.townMapID;
    }

    public void setFullName(String myFullName) {
        this.fullName = myFullName;
    }

    public String getFullName() {
        return this.fullName;
    }

    public void setShortName(String myShortName) {
        this.shortName = myShortName;
    }

    public String getShortName() {
        return this.shortName;
    }

    public void setSmallTownImage(ImageIdentifier smallTownImage) {
        this.smallTownImage = smallTownImage;
    }

    public ImageIdentifier getSmallTownImage() {
        return this.smallTownImage;
    }

    public void setTownImage(ImageIdentifier townImage) {
        this.townImage = townImage;
    }

    public ImageIdentifier getTownImage() {
        return this.townImage;
    }

    public void setMapExits(MapExit[] myMapExits) {
        this.mapExits = myMapExits;
    }

    public MapExit[] getMapExits() {
        return this.mapExits;
    }

    public void setInsertionPoint(ScreenPoint myInsertionPoint) {
        this.insertionPoint = myInsertionPoint;
    }

    public ScreenPoint getInsertionPoint() {
        return new ScreenPoint(this.insertionPoint);
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public String getMusicName() {
        return this.musicName;
    }

    /** Transient fields getter & setter
     */
    public WorldMap getMyWorldMap() {
        return this.myWorldMap;
    }

    public void setBuildings(Building[] myBuildings) {
        this.buildings = myBuildings;
    }

    public Building[] getBuildings() {
        return this.buildings;
    }

    public MessageRouter getMessageRouter() {
        return this.messageRouter;
    }

    /** To Get a building by its ID.
     *
     * @param id buildingID
     * @return corresponding building, null if ID does not exist.
     */
    public Building getBuildingFromID(int id) {
        if (id >= this.buildings.length || id < 0) {
            Debug.signal(Debug.ERROR, this, "getBuildingFromID : Bad building ID " + id);
            return null;
        }
        return this.buildings[id];
    }

    /** To get the wotlas location associated to this Map.
     *  @return associated Wotlas Location
     */
    public WotlasLocation getLocation() {
        return new WotlasLocation(this.myWorldMap.getWorldMapID(), this.townMapID);
    }

    /** Add a new Building object to our list {@link #buildings buildings})
     *
     * @param building Building object to add
     */
    public void addBuilding(Building building) {
        if (this.buildings == null) {
            this.buildings = new Building[building.getBuildingID() + 1];
        } else if (this.buildings.length <= building.getBuildingID()) {
            Building[] myBuildings = new Building[building.getBuildingID() + 1];
            System.arraycopy(this.buildings, 0, myBuildings, 0, this.buildings.length);
            this.buildings = myBuildings;
        }
        this.buildings[building.getBuildingID()] = building;
    }

    /** Add a new Building object to the array {@@link #buildings buildings})
     *
     * @return a new Building object
     */
    public Building addNewBuilding() {
        Building myBuilding = new Building();
        if (this.buildings == null) {
            this.buildings = new Building[1];
            myBuilding.setBuildingID(0);
            this.buildings[0] = myBuilding;
        } else {
            Building[] myBuildings = new Building[this.buildings.length + 1];
            myBuilding.setBuildingID(this.buildings.length);
            System.arraycopy(this.buildings, 0, myBuildings, 0, this.buildings.length);
            myBuildings[this.buildings.length] = myBuilding;
            this.buildings = myBuildings;
        }
        return myBuilding;
    }

    /** Add a new MapExit object to the array {@link #mapExits mapExits}
     *
     * @return a new MapExit object
     */
    public MapExit addMapExit(ScreenRectangle r) {
        MapExit myMapExit = new MapExit(r);
        if (this.mapExits == null) {
            this.mapExits = new MapExit[1];
            myMapExit.setMapExitID(0);
            this.mapExits[0] = myMapExit;
        } else {
            MapExit[] myMapExits = new MapExit[this.mapExits.length + 1];
            myMapExit.setMapExitID(this.mapExits.length);
            System.arraycopy(this.mapExits, 0, myMapExits, 0, this.mapExits.length);
            myMapExits[this.mapExits.length] = myMapExit;
            this.mapExits = myMapExits;
        }
        return myMapExit;
    }

    /** Add a new MapExit object to the array {@link #mapExits mapExits}
     *
     * @param me MapExit object
     */
    public void addMapExit(MapExit me) {
        if (this.mapExits == null) {
            this.mapExits = new MapExit[1];
            this.mapExits[0] = me;
        } else {
            MapExit[] myMapExits = new MapExit[this.mapExits.length + 1];
            System.arraycopy(this.mapExits, 0, myMapExits, 0, this.mapExits.length);
            myMapExits[this.mapExits.length] = me;
            this.mapExits = myMapExits;
        }
    }

    /** To init this town ( it rebuilds shortcuts ). DON'T CALL this method directly, use
     *  the init() method of the associated world.
     *
     * @param myWorldMap our parent WorldMap.
     */
    public void init(WorldMap myWorldMap) {
        this.myWorldMap = myWorldMap;
        if (this.buildings == null) {
            Debug.signal(Debug.WARNING, this, "Town has no buildings ! " + this);
            return;
        }
        for (int i = 0; i < this.buildings.length; i++) if (this.buildings[i] != null) this.buildings[i].init(this);
        if (this.mapExits == null) return;
        WotlasLocation thisLocation = new WotlasLocation(myWorldMap.getWorldMapID(), this.townMapID);
        for (int i = 0; i < this.mapExits.length; i++) this.mapExits[i].setMapExitLocation(thisLocation);
    }

    /** To init this townmap for message routing. We create an appropriate message router
     *  for the town via the provided factory.
     *
     *  Don't call this method yourself it's called via the WorldManager !
     *
     * @param msgRouterFactory our router factory
     */
    public void initMessageRouting(MessageRouterFactory msgRouterFactory, WorldManager wManager) {
        this.messageRouter = msgRouterFactory.createMsgRouterForTownMap(this, wManager);
        for (int i = 0; i < this.buildings.length; i++) if (this.buildings[i] != null && this.buildings[i].getInteriorMaps() != null) {
            InteriorMap imaps[] = this.buildings[i].getInteriorMaps();
            for (int j = 0; j < imaps.length; j++) if (imaps[j] != null && imaps[j].getRooms() != null) {
                Room rooms[] = imaps[j].getRooms();
                for (int k = 0; k < rooms.length; k++) rooms[k].initMessageRouting(msgRouterFactory, wManager);
            }
        }
    }

    /** Returns the MapExit which is on the side given by the specified rectangle.
     *  It's an helper for you : if your player is on a WorldMap and wants to go inside
     *  a TownMap use this method to retrieve a valid MapExit and get an insertion point.
     *
     *  The MapExit is in fact a ScreenRectangle and the so called "insertion point"
     *  should be the center of this ScreenRectangle.
     * 
     * @param rCurrent rectangle containing the player's current position, width & height
     *        the rectangle position can be anything BUT it should represent in some
     *        way the direction by which the player hits this TownMap zone.
     * @return the appropriate MapExit, null if there are no MapExits.
     */
    public MapExit findTownMapExit(Rectangle fromPosition) {
        if (this.mapExits == null) {
            if (this.buildings == null || this.buildings.length == 0 || this.buildings[0] == null || this.buildings[0].getBuildingExits() == null || this.buildings[0].getBuildingExits().length == 0) {
                Debug.signal(Debug.ERROR, this, "Failed to find town map exit !");
                return null;
            }
            if (fromPosition == null) return null;
            MapExit bExits[] = this.buildings[0].getBuildingExits();
            for (int i = 0; i < bExits.length; i++) {
                if (bExits[i].getMapExitSide() == MapExit.WEST && fromPosition.x <= this.x + this.width / 2) return bExits[i];
                if (bExits[i].getMapExitSide() == MapExit.EAST && fromPosition.x >= this.x + this.width / 2) return bExits[i];
                if (bExits[i].getMapExitSide() == MapExit.NORTH && fromPosition.y <= this.y + this.height / 2) return bExits[i];
                if (bExits[i].getMapExitSide() == MapExit.SOUTH && fromPosition.y >= this.y + this.height / 2) return bExits[i];
            }
            return bExits[0];
        }
        if (this.mapExits.length == 1) return this.mapExits[0];
        for (int i = 0; i < this.mapExits.length; i++) {
            if (this.mapExits[i].getMapExitSide() == MapExit.WEST && fromPosition.x <= this.x + this.width / 2) return this.mapExits[i];
            if (this.mapExits[i].getMapExitSide() == MapExit.EAST && fromPosition.x >= this.x + this.width / 2) return this.mapExits[i];
            if (this.mapExits[i].getMapExitSide() == MapExit.NORTH && fromPosition.y <= this.y + this.height / 2) return this.mapExits[i];
            if (this.mapExits[i].getMapExitSide() == MapExit.SOUTH && fromPosition.y >= this.y + this.height / 2) return this.mapExits[i];
        }
        return this.mapExits[0];
    }

    /** Tests if the given player rectangle has its x,y cordinates in a Building.
     *
     * @param destX destination x position of the player movement ( endPoint of path )
     * @param destY destination y position of the player movement ( endPoint of path )
     * @param rCurrent rectangle containing the player's current position, width & height
     * @return the Building the player is heading to (if he has reached it, or if there
     *         are any), null if none.
     */
    public Building isEnteringBuilding(int destX, int destY, Rectangle rCurrent) {
        if (this.buildings == null) return null;
        for (int i = 0; i < this.buildings.length; i++) {
            Rectangle buildRect = this.buildings[i].toRectangle();
            if (buildRect.contains(destX, destY) && buildRect.intersects(rCurrent)) return this.buildings[i];
        }
        return null;
    }

    /** Returns the eventual MapExit the given player is intersecting.
     *
     * @param rCurrent rectangle containing the player's current position, width & height
     * @return the Building the player is heading to (if he has reached it, or if there
     *         are any), null if none.
     */
    public MapExit isIntersectingMapExit(int destX, int destY, Rectangle rCurrent) {
        if (this.mapExits == null) return null;
        for (int i = 0; i < this.mapExits.length; i++) if (this.mapExits[i].toRectangle().contains(destX, destY) && this.mapExits[i].toRectangle().intersects(rCurrent)) return this.mapExits[i];
        return null;
    }

    /** String Info.
     */
    @Override
    public String toString() {
        return "TownMap - " + this.fullName;
    }
}
