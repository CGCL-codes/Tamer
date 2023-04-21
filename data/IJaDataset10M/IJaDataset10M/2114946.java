package jahuwaldt.maptools;

import java.awt.Graphics;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.Point;
import java.awt.Image;
import java.awt.MediaTracker;
import jahuwaldt.swing.tiledimage.TiledImageReader;
import jahuwaldt.swing.tiledimage.TiledImageRenderer;

/**
*  Class that represents a Digital Raster Graphic (DRG)
*  2D map layer data structure.  A DRG consists of a raster
*  image of a scanned USGS topographic map georeferenced
*  to the UTM grid.
*
*  <p>  Modified by:  Joseph A. Huwaldt  </p>
*
*  @author  Joseph A. Huwaldt   Date:  April 22, 2000
*  @version June 12, 2004
**/
public class DRGLayer extends UTMMapLayer {

    private static final boolean DEBUG = false;

    /**
	*  The type string returned by this layer's "type()" method.
	**/
    public static final String kType = "DRG";

    /**
	*  An image object that contains this DRGs raster image.
	**/
    private Image image;

    /**
	*  A tiled image object that represents this DRG's raster image
	*  when displaying a tiled DRG layer.
	**/
    private TiledImageRenderer tiledImage;

    /**
	*  Construct a DRG map layer using an AWT Image.
	*
	*  @param  name    The name of the DRG map layer.
	*  @param  mapRef  A reference to the upper left (northwest) corner
	*                  of the map, plus the resolution or cell size
	*                  of the DRG map data (usually 2.4 meters/pixel).
	*  @param  quadNW  The UTM coordinates of the northwest corner of the
	*                  map quadrangle.
	*  @param  quadSE  The UTM coordinates of the southeast corner of the
	*                  map quadrangle.
	*  @param  width   The width of the DRG in meters.
	*  @param  height  The height of the DRG in meters.
	*  @param  image   The AWT raster image for this DRG map.
	**/
    public DRGLayer(String name, UTMMapData mapRef, UTMCoord quadNW, UTMCoord quadSE, double width, double height, Image image) {
        super(name, mapRef, width, height, quadNW, quadSE);
        this.image = image;
    }

    /**
	*  Construct a DRG map layer using a TiledImageRenderer for rendering tiled image files.
	*
	*  @param  name    The name of the DRG map layer.
	*  @param  mapRef  A reference to the upper left (northwest) corner
	*                  of the map, plus the resolution or cell size
	*                  of the DRG map data (usually 2.4 meters/pixel).
	*  @param  quadNW  The UTM coordinates of the northwest corner of the
	*                  map quadrangle.
	*  @param  quadSE  The UTM coordinates of the southeast corner of the
	*                  map quadrangle.
	*  @param  width   The width of the DRG in meters.
	*  @param  height  The height of the DRG in meters.
	*  @param  image   The tiled raster image for this DRG map.
	**/
    public DRGLayer(String name, UTMMapData mapRef, UTMCoord quadNW, UTMCoord quadSE, double width, double height, TiledImageRenderer image) {
        super(name, mapRef, width, height, quadNW, quadSE);
        this.tiledImage = image;
    }

    /**
	*  Method that draws the DRG raster map image.  This method returns immediately
	*  even if the image has not been drawn yet.
	*
	*  @param  gc      The graphics context into which the map data is
	*                  to be rendered.
	*  @param  geoRef  Geographic reference (map information) that
	*                  is to be used for the map rendering.  If a map
	*                  is made up of multiple layers, this geographic
	*                  reference may be different than that contained in
	*                  any particular layer.
	*  @param  parent  The parent component that this map layer is being rendered
	*                  into.
	**/
    public void draw(Graphics gc, UTMMapData globalRef, Component parent) {
        internalDraw(gc, globalRef, false, parent);
    }

    /**
	*  Method that draws the DRG raster map image and blocks until the image drawing
	*  is complete.
	*
	*  @param  gc  The graphics context into which the map data is
	*              to be rendered.
	*  @param  geoRef  Geographic reference (map information) that
	*              is to be used for the map rendering.  If a map
	*              is made up of multiple layers, this geographic
	*              reference may be different than that contained in
	*              any particular layer.
	*  @param  parent  The parent component that this map layer is being rendered
	*                  into.
	**/
    public void drawAndWait(Graphics gc, UTMMapData globalRef, Component parent) {
        internalDraw(gc, globalRef, true, parent);
    }

    /**
	*  Returns the type of this map layer as a String.
	*  @reutrns The String "DRG".
	**/
    public String type() {
        return kType;
    }

    /**
	*  Method that returns an image of this map layer at this
	*  map layer's preferred scale.  For raster type map layers
	*  this means at the resolution of the map data.  This method
	*  will return "null" if the DRG uses a tiled image renderer.
	**/
    public Image getImage() {
        return image;
    }

    /**
	*  Method that draws the DRG raster map image.
	*
	*  @param  gc      The graphics context into which the map data is
	*                  to be rendered.
	*  @param  geoRef  Geographic reference (map information) that
	*                  is to be used for the map rendering.  If a map
	*                  is made up of multiple layers, this geographic
	*                  reference may be different than that contained in
	*                  any particular layer.
	*  @param wait     Flag indicating if the drawing should wait to complete drawing before returning
	*                  or return immediately.
	*  @param  parent  The parent component that this map layer is being rendered
	*                  into.
	**/
    private void internalDraw(Graphics gc, UTMMapData globalRef, boolean wait, Component parent) {
        if (isVisible() && (image != null || tiledImage != null)) {
            Rectangle clipRect = gc.getClipBounds();
            UTMMapData localRef = getGeoReference();
            UTMCoord upperLeft = localRef.getNorthWest();
            Point ulPoint = globalRef.UTMtoPoint(upperLeft);
            double globalResX = globalRef.getXRes();
            double globalResY = globalRef.getYRes();
            double localResX = localRef.getXRes();
            double localResY = localRef.getYRes();
            int iWidth = (int) ((getMapWidth() + localResX) / globalResX);
            int iHeight = (int) ((getMapHeight() + localResY) / globalResY);
            if (DEBUG) {
                System.out.println("\nDrawing DRG: " + getName());
                System.out.println("scaleX = " + (localResX / globalResX) + ", scaleY = " + (localResY / globalResY));
                System.out.println("iWidth = " + iWidth + ", iHeight = " + iHeight);
                System.out.println("ulPoint.x = " + ulPoint.x + ", ulPoint.y = " + ulPoint.y);
                System.out.println("clipRect = " + clipRect);
            }
            if (intersectingRects(ulPoint, iWidth, iHeight, clipRect)) {
                if (image != null) renderImage(gc, globalRef, clipRect, wait, parent); else renderTiledImage(gc, globalRef, clipRect, (float) (localResX / globalResX), (float) (localResY / globalResY), wait, parent);
            }
        }
    }

    /**
	*  Determines if two rectangles intersect.  The 1st rectangle is specified
	*  by a a point for the upper left and a width and height.  The
	*  second rectangle is specified by a Rectangle object.
	**/
    private boolean intersectingRects(Point ulPoint1, int rWidth, int rHeight, Rectangle rect2) {
        int r2l = rect2.x;
        int r2t = rect2.y;
        int r2r = r2l + rect2.width;
        int r2b = r2t + rect2.height;
        int r1r = ulPoint1.x + rWidth;
        int r1b = ulPoint1.y + rHeight;
        if ((r2l < r1r && r2r > ulPoint1.x) && (r2t < r1b && r2b > ulPoint1.y)) return true;
        return false;
    }

    private transient UTMCoord aCoord;

    private transient Point aPoint;

    /**
	*  Method that renders the DRG image to the specified graphics context.
	**/
    private void renderImage(Graphics gc, UTMMapData globalRef, Rectangle clipRect, boolean wait, Component parent) {
        if (aCoord == null) {
            aCoord = new UTMCoord();
            aPoint = new Point();
        }
        int dx1 = clipRect.x, dy1 = clipRect.y;
        int dx2 = dx1 + clipRect.width, dy2 = dy1 + clipRect.height;
        globalRef.PointToUTM(dx1, dy1, aCoord);
        UTMMapData mapRef = getGeoReference();
        mapRef.UTMtoPoint(aCoord, aPoint);
        int sx1 = aPoint.x - 1, sy1 = aPoint.y - 1;
        if (sx1 < 0) sx1 = 0;
        if (sy1 < 0) sy1 = 0;
        mapRef.PointToUTM(sx1, sy1, aCoord);
        globalRef.UTMtoPoint(aCoord, aPoint);
        dx1 = aPoint.x;
        dy1 = aPoint.y;
        globalRef.PointToUTM(dx2, dy2, aCoord);
        mapRef.UTMtoPoint(aCoord, aPoint);
        int sx2 = aPoint.x + 1, sy2 = aPoint.y + 1;
        int width = image.getWidth(parent);
        int height = image.getHeight(parent);
        if (width > 0 && sx2 >= width) sx2 = width - 1;
        if (height > 0 && sy2 >= height) sy2 = height - 1;
        mapRef.PointToUTM(sx2, sy2, aCoord);
        globalRef.UTMtoPoint(aCoord, aPoint);
        dx2 = aPoint.x;
        dy2 = aPoint.y;
        if (DEBUG) {
            System.out.println("sx1,sy1 = " + sx1 + "," + sy1 + ";  sx2,sy2 = " + sx2 + "," + sy2);
            System.out.println("dx1,dy1 = " + dx1 + "," + dy1 + ";  dx2,dy2 = " + dx2 + "," + dy2);
        }
        if (wait) {
            try {
                MediaTracker tracker = new MediaTracker(parent);
                tracker.addImage(image, 0);
                tracker.waitForID(0);
            } catch (InterruptedException ignore) {
            }
        }
        gc.drawImage(image, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, parent);
    }

    /**
	*  Method that renders the tiled DRG image to the specified graphics context.
	**/
    private void renderTiledImage(Graphics gc, UTMMapData globalRef, Rectangle clipRect, float scaleX, float scaleY, boolean wait, Component parent) {
        if (aCoord == null) {
            aCoord = new UTMCoord();
            aPoint = new Point();
        }
        int dx1 = clipRect.x, dy1 = clipRect.y;
        int dx2 = dx1 + clipRect.width, dy2 = dy1 + clipRect.height;
        globalRef.PointToUTM(dx1, dy1, aCoord);
        UTMMapData mapRef = getGeoReference();
        mapRef.UTMtoPoint(aCoord, aPoint);
        int sx1 = aPoint.x, sy1 = aPoint.y;
        if (sx1 < 0) sx1 = 0;
        if (sy1 < 0) sy1 = 0;
        mapRef.PointToUTM(sx1, sy1, aCoord);
        globalRef.UTMtoPoint(aCoord, aPoint);
        dx1 = aPoint.x;
        dy1 = aPoint.y;
        globalRef.PointToUTM(dx2, dy2, aCoord);
        mapRef.UTMtoPoint(aCoord, aPoint);
        int sx2 = aPoint.x, sy2 = aPoint.y;
        int width = tiledImage.getWidth();
        int height = tiledImage.getHeight();
        if (sx2 >= width) sx2 = width - 1;
        if (sy2 >= height) sy2 = height - 1;
        mapRef.PointToUTM(sx2, sy2, aCoord);
        globalRef.UTMtoPoint(aCoord, aPoint);
        dx2 = aPoint.x;
        dy2 = aPoint.y;
        if (DEBUG) {
            System.out.println("dx1,dy1 = " + dx1 + "," + dy1 + ";  dx2,dy2 = " + dx2 + "," + dy2);
            System.out.println("sx1,sy1 = " + sx1 + "," + sy1 + ";  sx2,sy2 = " + sx2 + "," + sy2);
            System.out.println("globalRef = " + globalRef);
            System.out.println("localRef = " + mapRef);
        }
        tiledImage.draw(gc, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, scaleX, scaleY, wait, parent);
    }
}
