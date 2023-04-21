package unbboolean.gui.save;

import unbboolean.solids.CSGSolid;
import unbboolean.solids.SphereSolid;

/**
 * Class representing a sphere solid to be saved
 * 
 * @author Danilo Balby Silva Castanheira(danbalby@yahoo.com)
 */
public class SaveSphereSolid extends SavePrimitiveSolid {

    /** sphere ray in X */
    private double rayX;

    /** sphere ray in Y */
    private double rayY;

    /** sphere ray in Z */
    private double rayZ;

    /**
	 * Constructs a SaveSphereSolid object based on a SphereSolid object
	 * 
	 * @param solid sphere solid to be saved
	 */
    public SaveSphereSolid(SphereSolid solid) {
        super(solid);
        rayX = solid.getRayX();
        rayY = solid.getRayY();
        rayZ = solid.getRayZ();
    }

    /**
	 * Gets the solid corresponding to this save solid
	 * 
	 * @return the solid corresponding to this save solid
	 */
    public CSGSolid getSolid() {
        SphereSolid sphere = new SphereSolid(name, rayX, rayY, rayZ, color);
        sphere.updateLocation(transformMatrix);
        return sphere;
    }
}
