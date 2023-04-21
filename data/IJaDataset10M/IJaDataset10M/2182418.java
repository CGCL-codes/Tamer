package meadow.common.units;

import meadow.common.*;
import java.awt.Rectangle;

/** A GamePiece with obvious implementations of its methods.
ISSUE: merge this class with GamePiece?*/
public abstract class StandardGamePiece extends GamePiece {

    /** The peice's (precise, centre) position in world coordinates. These are private to
	ensure changes are via setCenter, which updates the position rectangle.*/
    private float x, y, z;

    /** The piece's "radius"; note the piece is treated as a circle (or sphere).*/
    protected int radius;

    private Rectangle position;

    protected int hitPoints, initialHitPoints;

    protected TerrainModel terrainModel;

    private UniqueID uniqueID;

    protected int owner;

    protected boolean solid;

    /** */
    public StandardGamePiece(int x, int y, int z, int radius, int hitPoints, int owner, TerrainModel terrainModel, boolean solid) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.radius = radius;
        this.position = new Rectangle();
        this.updatePosition();
        this.initialHitPoints = this.hitPoints = hitPoints;
        this.uniqueID = new UniqueID();
        this.owner = owner;
        this.terrainModel = terrainModel;
        this.solid = solid;
    }

    public int getX() {
        return (int) x;
    }

    public int getY() {
        return (int) y;
    }

    public int getZ() {
        return (int) z;
    }

    /** @return A more precise value of the centre x co-ordinate.*/
    public float getCentreX() {
        return x;
    }

    /** @return A more precise value of the centre y co-ordinate.*/
    public float getCentreY() {
        return y;
    }

    /** @return A more precise value of the centre z co-ordinate.*/
    public float getCentreZ() {
        return z;
    }

    /** Moves this piece to (newX, newY). Leaves z unchanged.*/
    protected void setCentre(float newX, float newY) {
        x = newX;
        y = newY;
        updatePosition();
    }

    /** Moves this piece to (newX, newY, newZ).*/
    protected void setCentre(float newX, float newY, float newZ) {
        x = newX;
        y = newY;
        z = newZ;
        updatePosition();
    }

    /** Moves this piece by (dx, dy, 0), <em>relative to its current location</em>.*/
    protected void moveCentre(float dx, float dy) {
        x += dx;
        y += dy;
        updatePosition();
    }

    /** Moves this piece by (dx, dy, dz), <em>relative to its current location</em>.*/
    protected void moveCentre(float dx, float dy, float dz) {
        x += dx;
        y += dy;
        z += dz;
        updatePosition();
    }

    public TerrainModel getTerrain() {
        return terrainModel;
    }

    public int getHitPoints() {
        return hitPoints;
    }

    public int getInitialHitPoints() {
        return initialHitPoints;
    }

    /** Set internal position rectangle to correct location. This must be called 
	*immediately* after the unit has moved, or at least before getBounds() can be expected
	to return correct information. It is called automatically by the constructor and 
	setCenter(float, float).*/
    protected void updatePosition() {
        int xi = (int) x;
        int yi = (int) y;
        position.x = xi - radius;
        position.y = yi - radius;
        position.height = position.width = 2 * radius;
    }

    /** @return A rectangle containing position information, in 2d space. Note multiple
	calls to this method will return the same rectangle, and that this method is not to
	be altered directely from outside this class (use setCentre).*/
    public Rectangle getBounds() {
        return position;
    }

    /** Doesn't account for atypical effects. (Yet.)*/
    public void takeDamage(Damage damage) {
        int actualDamage = damage.intensity;
        if (actualDamage >= hitPoints) die(); else hitPoints -= actualDamage;
    }

    public void die() {
        hitPoints = 0;
    }

    public boolean isDead() {
        return hitPoints <= 0;
    }

    public boolean isAt(int x, int y) {
        return position.contains(x, y);
    }

    public int getOwner() {
        return owner;
    }

    public int getIDNum() {
        return uniqueID.getID();
    }

    public UniqueID getUniqueID() {
        return uniqueID;
    }

    public boolean isSolid() {
        return solid;
    }

    public boolean canProcessResources() {
        return false;
    }
}
