package gov.nasa.worldwind.terrain;

import gov.nasa.worldwind.geom.*;
import gov.nasa.worldwind.pick.PickedObject;
import gov.nasa.worldwind.render.*;
import java.awt.*;
import java.nio.DoubleBuffer;

/**
 * This interface provides access to individual terrain tiles, which are contained in a {@link SectorGeometryList}.
 * <p/>
 * Note: Three methods of this class assume that the {@link SectorGeometryList#beginRendering(gov.nasa.worldwind.render.DrawContext)}
 * method of the containing sector geometry list has been called prior to calling them. They are {@link
 * #pick(gov.nasa.worldwind.render.DrawContext, java.awt.Point)}, {@link #pick(gov.nasa.worldwind.render.DrawContext,
 * java.util.List)}, and {@link #renderMultiTexture(gov.nasa.worldwind.render.DrawContext, int)}.
 *
 * @version $Id: SectorGeometry.java 1 2011-07-16 23:22:47Z dcollins $
 */
public interface SectorGeometry extends Renderable {

    /**
     * Returns this sector geometry's extent.
     *
     * @return this sector geometry's extent, or null if the extent has not been computed.
     */
    Extent getExtent();

    /**
     * Indicates the {@link Sector} covered by this sector geometry.
     *
     * @return this sector geometry's sector.
     */
    Sector getSector();

    /**
     * Performs a pick on the geometry. The result, if any, is added to the draw context's picked-object list. See
     * {@link gov.nasa.worldwind.render.DrawContext#getPickedObjects()}.
     * <p/>
     * Note: This method assumes that {@link SectorGeometryList#beginRendering(gov.nasa.worldwind.render.DrawContext)}
     * was called prior to this method.
     *
     * @param dc        the current draw context.
     * @param pickPoint a screen coordinate points to pick test.
     *
     * @throws IllegalArgumentException if either the draw context or list of pick points is null.
     */
    void pick(DrawContext dc, java.awt.Point pickPoint);

    /**
     * Computes the Cartesian coordinates of a location on the geometry's surface.
     *
     * @param latitude     the position's latitude.
     * @param longitude    the position's longitude.
     * @param metersOffset the number of meters to offset the computed position from the geometry's surface.
     *
     * @return the computed Cartesian coordinates, or null if the specified location is not within the geometry's sector
     *         or no internal geometry exists (has not yet been created).
     *
     * @throws IllegalArgumentException if either the latitude or longitude are null.
     */
    Vec4 getSurfacePoint(Angle latitude, Angle longitude, double metersOffset);

    /**
     * Indicates that this sector geometry is about to be rendered one or more times. When rendering is complete, the
     * {@link #endRendering(gov.nasa.worldwind.render.DrawContext)} method must be called.
     *
     * @param dc              the current draw context.
     * @param numTextureUnits the number of texture units to use.
     */
    void beginRendering(DrawContext dc, int numTextureUnits);

    /**
     * Restores state established by {@link #beginRendering(gov.nasa.worldwind.render.DrawContext, int)}.
     *
     * @param dc the current draw context.
     */
    void endRendering(DrawContext dc);

    /**
     * Displays the geometry. The number of texture units to use may be specified, but at most only the number of
     * available units are used.
     * <p/>
     * Note: This method assumes that {@link SectorGeometryList#beginRendering(gov.nasa.worldwind.render.DrawContext)}
     * was called prior to this method.
     *
     * @param dc              the current draw context.
     * @param numTextureUnits the number of texture units to attempt to use.
     *
     * @throws IllegalArgumentException if the draw context is null or the number of texture units is less than one.
     */
    void renderMultiTexture(DrawContext dc, int numTextureUnits);

    /**
     * Displays the geometry's tessellation. Option parameters control whether to display the interior triangles, the
     * geometry's exterior boundary, or both.
     *
     * @param dc       the current draw context.
     * @param interior if true, displays the interior triangles.
     * @param exterior if true, displays the exterior boundary.
     *
     * @throws IllegalArgumentException if the draw context is null.
     */
    void renderWireframe(DrawContext dc, boolean interior, boolean exterior);

    /**
     * Displays the geometry's bounding volume.
     *
     * @param dc the current draw context.
     *
     * @throws IllegalArgumentException if the draw context is null.
     */
    void renderBoundingVolume(DrawContext dc);

    /**
     * Displays on the geometry's surface the tessellator level and the minimum and maximum elevations of the sector.
     *
     * @param dc the current draw context.
     *
     * @throws IllegalArgumentException if the draw context is null.
     */
    void renderTileID(DrawContext dc);

    /**
     * Performs a pick on the geometry.
     * <p/>
     * Note: This method assumes that {@link SectorGeometryList#beginRendering(gov.nasa.worldwind.render.DrawContext)}
     * was called prior to this method.
     *
     * @param dc         the current draw context.
     * @param pickPoints a list of screen coordinate points to pick test.
     *
     * @return an array of resolved pick objects corresponding to the specified pick points. Null is returned as the
     *         picked object for points not on the geometry or otherwise not resolvable. Returns null if the pick point
     *         list's size is zero.
     *
     * @throws IllegalArgumentException if either the draw context or list of pick points is null.
     */
    PickedObject[] pick(DrawContext dc, java.util.List<? extends Point> pickPoints);

    /**
     * Computes the Cartesian coordinates of a line's intersections with the geometry.
     *
     * @param line the line to intersect.
     *
     * @return the Cartesian coordinates of each intersection, or null if there is no intersection or no internal
     *         geometry has been computed.
     *
     * @throws IllegalArgumentException if the line is null.
     */
    Intersection[] intersect(Line line);

    /**
     * Computes the geometry's intersections with a globe at a specified elevation.
     *
     * @param elevation the elevation for which intersection points are to be found.
     *
     * @return an array of intersection pairs, or null if no intersections were found. The returned array of
     *         intersections describes a list of individual segments - two <code>Intersection</code> elements for each,
     *         corresponding to each geometry triangle that intersects the given elevation.
     */
    Intersection[] intersect(double elevation);

    /**
     * Computes texture coordinates for the geometry. Specific coordinate values are computed by a specified computer
     * implementing the {@link GeographicTextureCoordinateComputer} interface. The computer is invoked once for each
     * tessellation vertex of the geometry. The latitude and longitude of the location is specified in that invocation.
     *
     * @param computer the texture coordinate computer.
     *
     * @return the computed texture coordinates. The first entry in the buffer corresponds to the lower left corner of
     *         the geometry (minimum latitude and longitude). The entries are then ordered by increasing longitude and
     *         then increasing latitude (typically called row-major order).
     *
     * @throws IllegalArgumentException if the computer is null.
     */
    DoubleBuffer makeTextureCoordinates(GeographicTextureCoordinateComputer computer);

    /**
     * Displays the geometry. The number of texture units to use may be specified, but at most only the number of
     * available units are used.
     * <p/>
     * Note: This method allows but does not require that {@link SectorGeometryList#beginRendering(gov.nasa.worldwind.render.DrawContext)}
     * was called prior to this method. See the description of the <code>beginRenderingCalled</code> argument.
     *
     * @param dc                   the current draw context.
     * @param numTextureUnits      the number of texture units to attempt to use.
     * @param beginRenderingCalled indicates whether this sector geometry's <code>beginRendering</code> method has been
     *                             called prior to calling this method. True indicated it was called, false indicates
     *                             that it was not. Calling <beginRendering> eliminates redundant rendering set-up and
     *                             is used when this sector geometry is rendered several times in succession.
     *
     * @throws IllegalArgumentException if the draw context is null or the number of texture units is less than one.
     * @see #beginRendering(gov.nasa.worldwind.render.DrawContext, int)
     */
    void renderMultiTexture(DrawContext dc, int numTextureUnits, boolean beginRenderingCalled);

    /**
     * Displays the geometry.
     * <p/>
     * Note: This method allows but does not require that {@link SectorGeometryList#beginRendering(gov.nasa.worldwind.render.DrawContext)}
     * was called prior to this method. See the description of the <code>beginRenderingCalled</code> argument.
     *
     * @param dc                   the current draw context.
     * @param beginRenderingCalled indicates whether this sector geometry's <code>beginRendering</code> method has been
     *                             called prior to calling this method. True indicated it was called, false indicates
     *                             that it was not. Calling <beginRendering> eliminates redundant rendering set-up and
     *                             is used when this sector geometry is rendered several times in succession.
     *
     * @throws IllegalArgumentException if the draw context is null or the number of texture units is less than one.
     * @see #beginRendering(gov.nasa.worldwind.render.DrawContext, int)
     */
    void render(DrawContext dc, boolean beginRenderingCalled);

    /** An interface for computing texture coordinates for a given location. */
    public interface GeographicTextureCoordinateComputer {

        /**
         * Computes a texture coordinate for a specified location.
         *
         * @param latitude  the location's latitude.
         * @param longitude the location's longitude.
         *
         * @return the [s,t] texture coordinate, where s corresponds to the longitude axis and t corresponds to the
         *         latitude axis.
         */
        double[] compute(Angle latitude, Angle longitude);
    }
}
