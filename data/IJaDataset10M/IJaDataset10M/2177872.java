package org.thenesis.planetino2.graphics3D.texture;

import java.util.Vector;
import org.thenesis.planetino2.math3D.Rectangle3D;
import org.thenesis.planetino2.math3D.TexturedPolygon3D;
import org.thenesis.planetino2.math3D.Vector3D;

/**
 A ShadedSurface is a pre-shaded Texture that maps onto a
 polygon.
 */
public final class ShadedSurface extends Texture {

    public static final int SURFACE_BORDER_SIZE = 1;

    public static final int SHADE_RES_BITS = 4;

    public static final int SHADE_RES = 1 << SHADE_RES_BITS;

    public static final int SHADE_RES_MASK = SHADE_RES - 1;

    public static final int SHADE_RES_SQ = SHADE_RES * SHADE_RES;

    public static final int SHADE_RES_SQ_BITS = SHADE_RES_BITS * 2;

    private int[] buffer;

    private boolean dirty;

    private ShadedTexture sourceTexture;

    private Rectangle3D sourceTextureBounds;

    private Rectangle3D surfaceBounds;

    private byte[] shadeMap;

    private int shadeMapWidth;

    private int shadeMapHeight;

    private int shadeValue;

    private int shadeValueInc;

    /**
	 Creates a ShadedSurface with the specified width and
	 height.
	 */
    public ShadedSurface(int width, int height) {
        this(null, width, height);
    }

    /**
	 Creates a ShadedSurface with the specified buffer,
	 width and height.
	 */
    public ShadedSurface(int[] buffer, int width, int height) {
        super(width, height);
        this.buffer = buffer;
        sourceTextureBounds = new Rectangle3D();
        dirty = true;
    }

    /**
	 Creates a ShadedSurface for the specified polygon. The
	 shade map is created from the specified list of point
	 lights and ambient light intensity.
	 */
    public static void createShadedSurface(TexturedPolygon3D poly, ShadedTexture texture, Vector lights, float ambientLightIntensity) {
        Vector3D origin = poly.getVertex(0);
        Vector3D dv = new Vector3D(poly.getVertex(1));
        dv.subtract(origin);
        Vector3D du = new Vector3D();
        du.setToCrossProduct(poly.getNormal(), dv);
        Rectangle3D bounds = new Rectangle3D(origin, du, dv, texture.getWidth(), texture.getHeight());
        createShadedSurface(poly, texture, bounds, lights, ambientLightIntensity);
    }

    /**
	 Creates a ShadedSurface for the specified polygon. The
	 shade map is created from the specified list of point
	 lights and ambient light intensity.
	 */
    public static void createShadedSurface(TexturedPolygon3D poly, ShadedTexture texture, Rectangle3D textureBounds, Vector lights, float ambientLightIntensity) {
        poly.setTexture(texture, textureBounds);
        Rectangle3D surfaceBounds = poly.calcBoundingRectangle();
        Vector3D du = new Vector3D(surfaceBounds.getDirectionU());
        Vector3D dv = new Vector3D(surfaceBounds.getDirectionV());
        du.multiply(SURFACE_BORDER_SIZE);
        dv.multiply(SURFACE_BORDER_SIZE);
        surfaceBounds.getOrigin().subtract(du);
        surfaceBounds.getOrigin().subtract(dv);
        int width = (int) Math.ceil(surfaceBounds.getWidth() + SURFACE_BORDER_SIZE * 2);
        int height = (int) Math.ceil(surfaceBounds.getHeight() + SURFACE_BORDER_SIZE * 2);
        surfaceBounds.setWidth(width);
        surfaceBounds.setHeight(height);
        ShadedSurface surface = new ShadedSurface(width, height);
        surface.setTexture(texture, textureBounds);
        surface.setSurfaceBounds(surfaceBounds);
        surface.buildShadeMap(lights, ambientLightIntensity);
        poly.setTexture(surface, surfaceBounds);
    }

    /**
	 Gets the 16-bit color of the pixel at location (x,y) in
	 the bitmap. The x and y values are assumbed to be within
	 the bounds of the surface; otherwise an
	 ArrayIndexOutOfBoundsException occurs.
	 */
    public int getColor(int x, int y) {
        return sourceTexture.getColor(x, y);
    }

    private int getColor565(int x, int y) {
        return buffer[x + y * width];
    }

    /**
	 Gets the 16-bit color of the pixel at location (x,y) in
	 the bitmap. The x and y values are checked to be within
	 the bounds of the surface, and if not, the pixel on the
	 edge of the texture is returned.
	 */
    public int getColorChecked(int x, int y) {
        if (x < 0) {
            x = 0;
        } else if (x >= width) {
            x = width - 1;
        }
        if (y < 0) {
            y = 0;
        } else if (y >= height) {
            y = height - 1;
        }
        return getColor565(x, y);
    }

    /**
	 Marks whether this surface is dirty. Surfaces marked as
	 dirty may be cleared externally.
	 */
    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    /**
	 Checks wether this surface is dirty. Surfaces marked as
	 dirty may be cleared externally.
	 */
    public boolean isDirty() {
        return dirty;
    }

    /**
	 Creates a new surface and add a SoftReference to it.
	 */
    protected void newSurface(int width, int height) {
        buffer = new int[width * height];
    }

    /**
	 Clears this surface, allowing the garbage collector to
	 remove it from memory if needed.
	 */
    public void clearSurface() {
        buffer = null;
    }

    /**
	 Checks if the surface has been cleared.
	 */
    public boolean isCleared() {
        return (buffer == null);
    }

    /**
	 Sets the source texture for this ShadedSurface.
	 */
    public void setTexture(ShadedTexture texture) {
        this.sourceTexture = texture;
        sourceTextureBounds.setWidth(texture.getWidth());
        sourceTextureBounds.setHeight(texture.getHeight());
    }

    /**
	 Sets the source texture and source bounds for this
	 ShadedSurface.
	 */
    public void setTexture(ShadedTexture texture, Rectangle3D bounds) {
        setTexture(texture);
        sourceTextureBounds.setTo(bounds);
    }

    /**
	 Sets the surface bounds for this ShadedSurface.
	 */
    public void setSurfaceBounds(Rectangle3D surfaceBounds) {
        this.surfaceBounds = surfaceBounds;
    }

    /**
	 Gets the surface bounds for this ShadedSurface.
	 */
    public Rectangle3D getSurfaceBounds() {
        return surfaceBounds;
    }

    /**
	 Builds the surface. First, this method calls
	 retrieveSurface() to see if the surface needs to be
	 rebuilt. If not, the surface is built by tiling the
	 source texture and apply the shade map.
	 */
    public void buildSurface() {
    }

    /**
	 Gets the shade (from the built shade map) for the
	 specified (u,v) location.
	 */
    public int getShade(int u, int v) {
        return shadeMap[u + v * shadeMapWidth];
    }

    /**
	 Builds the shade map for this surface from the specified
	 list of point lights and the ambiant light intensity.
	 */
    public void buildShadeMap(Vector pointLights, float ambientLightIntensity) {
    }

    public ShadedTexture getSourceTexture() {
        return sourceTexture;
    }
}
