package gumbo.ardor3d.shape;

import gumbo.core.util.AssertUtils;
import com.ardor3d.scenegraph.Node;
import com.ardor3d.scenegraph.Spatial;

/**
 * An axis-aligned planar rectangular shape that is composed of multiple
 * rectangular tile shapes. The width is along the X axis, the height is along
 * the Y axis, and the surface normal of the front side is along the Z axis.
 * Texturing spans the whole shape, with the front side appearing "normal".
 * <p>
 * Intended to provide more realistic surface transparency effects for
 * intersecting and overlapping surfaces. The Ardor3D transparency model is based
 * on the sorted distances between the camera and the centroid of each spatial.
 * Thus, if transparent surfaces are not tiled, portions that appear to
 * overlap may not be rendered in correct order. By multi-tiling the surface,
 * each tile will be rendered separately.
 * @author jonb
 */
public class A3dMultiRect {

    /**
	 * Creates an instance, centered about the origin.
	 * @param name Local shape name. Never null.
	 * @param width Size along width (X axis). >0
	 * @param height Size along height (Y axis). >0
	 * @param countW Tile count along width. >0
	 * @param countH Tile count along height. >0
	 */
    public A3dMultiRect(String name, double width, double height, int countW, int countH) {
        AssertUtils.assertNonNullArg(name);
        AssertUtils.assertValidArg(width > 0, this, "width must be >0. width=" + width);
        AssertUtils.assertValidArg(height > 0, this, "height must be >0. height=" + height);
        AssertUtils.assertValidArg(countW > 0, this, "width count must be >0. countW=" + countW);
        AssertUtils.assertValidArg(countH > 0, this, "height count must be >0. countH=" + countH);
        _width = width;
        _height = height;
        _root = new Node(name);
        double tileW = width / countW;
        double tileH = height / countH;
        double bgnPosX = -(width - tileW) / 2.0;
        double bgnPosY = -(height - tileH) / 2.0;
        double posY = bgnPosY;
        for (int rowI = 0; rowI < countH; rowI++) {
            double posX = bgnPosX;
            for (int colI = 0; colI < countW; colI++) {
                A3dMultiTile tile = new A3dMultiTile(name + "." + rowI + "." + colI, width, height, tileW, tileH, posX, posY);
                _root.attachChild(tile);
                posX += tileW;
            }
            posY += tileH;
        }
    }

    public double getWidth() {
        return _width;
    }

    public double getHeight() {
        return _height;
    }

    public Spatial asSpatial() {
        return _root;
    }

    private static final long serialVersionUID = 1L;

    private double _width;

    private double _height;

    private Node _root;
}
