package org.vrforcad.model.cad;

import javax.media.j3d.Transform3D;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

/**
 * The shape bounding box.
 *  
 * @version 1.0 
 * @author Daniel Cioi <dan.cioi@vrforcad.org>
 */
public class ShapeBoundingBox {

    private float minX, maxX, minY, maxY, minZ, maxZ;

    private float cX, cY, cZ;

    /**
	 * Default constructor
	 */
    public ShapeBoundingBox() {
    }

    /**
	 * Check if the new vertex it's out of current boundary.
	 * @param vertex
	 */
    public void checkNewVertex(Vertex vertex) {
        cX = vertex.getPoint().x;
        cY = vertex.getPoint().y;
        cZ = vertex.getPoint().z;
        if (cX < minX) minX = cX; else if (cX > maxX) maxX = cX;
        if (cY < minY) minY = cY; else if (cY > maxY) maxY = cY;
        if (cZ < minZ) minZ = cZ; else if (cZ > maxZ) maxZ = cZ;
    }

    /**
	 * Get Transform3D to center the shape.
	 * @return
	 */
    public Transform3D getCenter() {
        Transform3D tr = new Transform3D();
        tr.setTranslation(new Vector3f(-(maxX - minX) / 2, -(maxY - minY) / 2, -(maxZ - minZ) / 2));
        return tr;
    }

    /**
	 * Get the shape's maxim size to adjust the camera view.
	 * @return
	 */
    public float getMaxDimension() {
        float result = maxX - minX;
        if (maxY - minY > result) result = maxY - minY;
        if (maxZ - minZ > result) result = maxZ - minZ;
        return result;
    }

    /**
	 * Get lower point to construct a visible bounding box.
	 * @return
	 */
    public Point3f getLowerPoint() {
        return new Point3f(minX, minY, minZ);
    }

    /**
	 * Get upper point to construct a visible bounding box.
	 * @return
	 */
    public Point3f getUpperPoint() {
        return new Point3f(maxX, maxY, maxZ);
    }
}
