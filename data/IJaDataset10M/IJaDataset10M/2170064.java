package checkers3d.logic;

import checkers3d.presentation.IRenderResource;
import java.awt.Point;

/**
 * A 'safezone' object. Prevents a player's piece from being killed if that piece
 * is currently placed in a PlayingSurfacePosition with a BoardObjectSafe.
 *
 * @author Ruben Acuna
 */
public class BoardObjectSafe implements IBoardObject {

    /**
     * Reference to the player that placed this object and now owns it.
     */
    private Player owner;

    /**
     * The position of this BoardObjectSafe within a GUIContainer or window
     * space. Unused, automatically calculated by RenderResourcePlayingSurface2D.
     */
    Point drawPosition;

    /**
     * The IRenderResource that defines how BoardObjectSafe looks.
     */
    IRenderResource resource;

    /**
     * Class consturctor that create a BoardObjectSafe. Assigns a player as the
     * object's owner, sets it's draw position to 0,0 (unused), and null for the
     * RenderResource.
     *
     * @param player Object's controller.
     */
    public BoardObjectSafe(Player player, IRenderResource resource) {
        setOwner(player);
        setDrawPosition(new Point());
        setRenderResource(resource);
    }

    /**
    * Returns the position of the BoardObjectSafe. Unused.
    *
    * @return The position of this BoardObjectSafe.
    * @see IBoardObject
    */
    public Point getDrawPosition() {
        return drawPosition;
    }

    /**
    * Sets the current the position of the BoardObjectSafe. Unused.
    *
    * @param  position The new position of this BoardObjectSafe.
    * @see IBoardObject
    */
    public void setDrawPosition(Point position) {
        drawPosition = position;
    }

    /**
    * Returns the IRenderResource of the BoardObjectSafe.
    *
    * @return The IRenderResource of this BoardObjectSafe.
    * @see IBoardObject
    */
    public IRenderResource getRenderResource() {
        return resource;
    }

    /**
    * Sets the current the IRenderResource of the BoardObjectSafe.
    *
    * @param  resource The new IRenderResource of this BoardObjectSafe.
    */
    public void setRenderResource(IRenderResource resource) {
        this.resource = resource;
    }

    /**
     * Returns the total number of pieces of this type which a given player
     * may setup, and later control, each game.
     *
     * @param boardSizeX The width of the board.
     * @param boardSizeY The height of the board.
     * @param boardSizeZ The number of boards.
     * @return The number of pieces.
     * @see IBoardObject
     */
    public int getSetupCount(int boardSizeX, int boardSizeY, int boardSizeZ) {
        int setupCount = 1;
        return setupCount;
    }

    /**
     * Returns the Player object that currently controls the BoardObjectSafe.
     *
     * @return Returns Player object that controls this object.
     * @see IBoardObject
     */
    public Player getOwner() {
        return owner;
    }

    /**
     * Sets the Player object that controls the BoardObjectSafe.
     *
     * @param player The player object which owns this object.
     * @see IBoardObject
     */
    public void setOwner(Player player) {
        owner = player;
    }

    /**
     * Returns true if BoardObjectSafe prevents other objects from existing in
     * the same location.
     *
     * @return Returns false because BoardObjectSafe des not block a location.
     * @see IBoardObject
     */
    public boolean isBlocking() {
        return false;
    }

    /**
     * Returns true if the BoardObjectSafe may be placed on a certain location
     * during the game setup. Only returns false if a BoardObjectSafe already
     * exists at position since it does not matter if the position has a
     * blocking object in it.
     *
     * @param playingSurface3D Reference the playing surface.
     * @param location Location this piece could be placed.
     * @see IBoardObject
     */
    public boolean isValidSetupPosition(PlayingSurface3D playingSurface3D, Point3D location) {
        boolean valid = false;
        if (!UtilBoardObject.isPositionContainObject(playingSurface3D.getPosition(location), this)) if (!UtilBoardObject.isPositionPermanentlyBlocked(playingSurface3D.getPosition(location))) if ((location.y >= (playingSurface3D.getSizeY() / 2))) valid = true;
        return valid;
    }

    /**
     * Returns true if a given Player can see this BoardObjectSafe during that
     * Player's turn.
     *
     * @param  player The player object trying to see the BoardObjectSafe.
     * @return Boolean indicating visibility.
     * @see IBoardObject
     */
    public boolean isVisibleTo(Object player) {
        return true;
    }

    /**
     * Event to let the BoardObjectSafe know that it was touched by another object
     * entering this object's location. Returns a protection effect.
     *
     * @param other The object that touched this object.
     * @return      An effect that should be applied to the other object.
     * @see IBoardObject
     */
    public BoardObjectTouchEffect onTouch(IBoardObject other) {
        return BoardObjectTouchEffect.PROTECTS;
    }

    /**
     * Returns a string containing data about this object needed to save it's
     * state.
     *
     * @return String with object name and it's owner.
     * @see IBoardObject
     */
    public String getDataAsString() {
        return ("Safe\t" + owner.getDataAsString());
    }

    /**
     * Returns a new instance of this object type with all local variables the
     * same.
     *
     * @return A new instance of this object.
     * @see IBoardObject
     */
    @Override
    public IBoardObject clone() {
        return new BoardObjectSafe(getOwner(), getRenderResource());
    }

    /**
     * Returns string representation of the object with information about it's
     * owner.
     *
     * @return A string representation of the object.
     */
    @Override
    public String toString() {
        return (getClass().getName() + " - owner: " + getOwner().toString());
    }
}
