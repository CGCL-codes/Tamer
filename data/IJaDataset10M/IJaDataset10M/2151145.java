package org.xith3d.scenegraph;

import org.jagatoo.util.arrays.ArrayUtils;
import org.xith3d.scenegraph.utils.CopyListener;

/**
 * The LODShape3D is a Shape3D Node extension, that handles discrete LOD.
 * Discrete LOD (level of detail) is a technique, that
 * selects an item depending on its distance to the camera.
 * 
 * @see LODSwitch
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
public class LODShape3D extends AbstractLODShape3D {

    private Geometry[] lodGeoms = new Geometry[16];

    private Appearance[] lodApps = new Appearance[16];

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onLODChanged(int oldLOD, int newLOD, String name) {
        setGeometry(lodGeoms[newLOD]);
        setAppearance(lodApps[newLOD]);
    }

    /**
     * Adds a new LOD (level of detail).
     * 
     * @param name the name of the new LOD
     * @param minDist the minimum distance
     * @param maxDist the maximum distance
     * @param geom
     * @param app
     */
    public int addLOD(String name, float minDist, float maxDist, Geometry geom, Appearance app) {
        int oldNumLODs = getNumLODs();
        int level = super.addLOD(name, minDist, maxDist);
        if (oldNumLODs > 0) {
            lodGeoms = ArrayUtils.ensureCapacity(lodGeoms, Geometry.class, getNumLODs() + 1);
            lodApps = ArrayUtils.ensureCapacity(lodApps, Appearance.class, getNumLODs() + 1);
            if (level < oldNumLODs) {
                System.arraycopy(lodGeoms, level, lodGeoms, level + 1, oldNumLODs - level);
                System.arraycopy(lodApps, level, lodApps, level + 1, oldNumLODs - level);
            }
        }
        lodGeoms[level] = geom;
        lodApps[level] = app;
        return (level);
    }

    /**
     * Adds a new LOD (level of detail).
     * 
     * @param minDist the minimum distance
     * @param maxDist the maximum distance
     * @param geom
     * @param app
     */
    public int addLOD(float minDist, float maxDist, Geometry geom, Appearance app) {
        return (addLOD((String) null, minDist, maxDist, geom, app));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected LODShape3D newInstance() {
        boolean gib = Node.globalIgnoreBounds;
        Node.globalIgnoreBounds = this.isIgnoreBounds();
        LODShape3D newShape = new LODShape3D();
        Node.globalIgnoreBounds = gib;
        return (newShape);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void copy(Shape3D dst) {
        super.copy(dst);
        LODShape3D newShape = (LODShape3D) dst;
        if (newShape.lodGeoms.length != this.lodGeoms.length) newShape.lodGeoms = new Geometry[this.lodGeoms.length];
        System.arraycopy(this.lodGeoms, 0, newShape.lodGeoms, 0, this.lodGeoms.length);
        if (newShape.lodApps.length != this.lodApps.length) newShape.lodApps = new Appearance[this.lodApps.length];
        System.arraycopy(this.lodApps, 0, newShape.lodApps, 0, this.lodApps.length);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LODShape3D sharedCopy(CopyListener listener) {
        return ((LODShape3D) super.sharedCopy(listener));
    }

    /**
     * Constructs a new Shape3D object with specified geometry and
     * appearance components.
     */
    public LODShape3D() {
        super();
    }
}
