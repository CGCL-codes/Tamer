package org.opengis.openoffice;

import com.sun.star.uno.XInterface;
import com.sun.star.beans.XPropertySet;

/**
 * Services from the {@link org.opengis.referencing} package to be exported to
 * <A HREF="http://www.openoffice.org">OpenOffice.org</A>.
 *
 * This interface is derived from the {@code XReferencing.idl} file using the {@code javamaker}
 * tool provided in OpenOffice SDK, and disassembling the output using the {@code javap} tool
 * provided in Java SDK. This source file exists mostly for javadoc purpose and in order to keep
 * IDE happy. The {@code .class} file compiled from this source file <strong>MUST</strong> be
 * overwritten by the {@code .class} file generated by {@code javamaker}.
 *
 * @author  Martin Desruisseaux (IRD)
 * @version 3.1
 * @since   3.1
 */
public interface XReferencing extends XInterface {

    /**
     * Returns the identified object description from an authority code.
     *
     * @param  xOptions Provided by OpenOffice.
     * @param  authorityCode The code allocated by the authority.
     * @return The identified object description, or {@code null}.
     */
    String getDescription(XPropertySet xOptions, String authorityCode);

    /**
     * Returns the scope for an identified object.
     *
     * @param  xOptions Provided by OpenOffice.
     * @param  authorityCode The code allocated by the authority.
     * @return The identified object scope, or {@code null}.
     */
    String getScope(XPropertySet xOptions, String authorityCode);

    /**
     * Returns the valid area as a textual description for an identified object.
     *
     * @param  xOptions Provided by OpenOffice.
     * @param  authorityCode The code allocated by the authority.
     * @return The identified object valid area, or {@code null}.
     */
    String getValidArea(XPropertySet xOptions, String authorityCode);

    /**
     * Returns the valid area as a geographic bounding box for an identified object. This method
     * returns a 2&times;2 matrix. The first row contains the latitude and longitude of upper left
     * corder, and the second row contains the latitude and longitude or bottom right corner. Units
     * are degrees.
     *
     * @param  xOptions Provided by OpenOffice.
     * @param  authorityCode The code allocated by the authority.
     * @return The identified object bounding box, or {@code null}.
     */
    double[][] getBoundingBox(XPropertySet xOptions, String authorityCode);

    /**
     * Returns the remarks for an identified object.
     *
     * @param  xOptions Provided by OpenOffice.
     * @param  authorityCode The code allocated by the authority.
     * @return The identified object remarks, or {@code null}.
     */
    String getRemarks(XPropertySet xOptions, String authorityCode);

    /**
     * Returns the axis name for the specified dimension in an identified object.
     *
     * @param  xOptions Provided by OpenOffice.
     * @param  authorityCode The code allocated by the authority.
     * @param  dimension The dimension (1, 2, ...).
     * @return The name of the given axis, or {@code null}.
     */
    String getAxis(XPropertySet xOptions, String authorityCode, int dimension);

    /**
     * Returns the value for a coordinate reference system parameter.
     *
     * @param  xOptions Provided by OpenOffice.
     * @param  authorityCode The code allocated by the authority.
     * @param  parameter The parameter name (e.g. "False easting").
     * @return The value of the given parameter, or {@code null}.
     */
    Object getParameter(XPropertySet xOptions, String authorityCode, String parameter);

    /**
     * Returns the Well Know Text (WKT) for an identified object.
     *
     * @param  xOptions      Provided by OpenOffice.
     * @param  authorityCode The code allocated by the authority.
     * @param  authority     The authority name for choice of parameter names. Usually "OGC".
     * @return The identified object WKT, or {@code null}.
     */
    String getWKT(XPropertySet xOptions, String authorityCode, Object authority);

    /**
     * Returns the Well Know Text (WKT) of a transformation between two coordinate reference
     * systems.
     *
     * @param  xOptions Provided by OpenOffice.
     * @param  sourceCRS The authority code for the source coordinate reference system.
     * @param  targetCRS The authority code for the target coordinate reference system.
     * @param  authority The authority name for choice of parameter names. Usually "OGC".
     * @return The transform WKT, or {@code null}.
     */
    String getTransformWKT(XPropertySet xOptions, String sourceCRS, String targetCRS, Object authority);

    /**
     * Returns the accuracy of a transformation between two coordinate reference systems.
     *
     * @param  xOptions Provided by OpenOffice.
     * @param  sourceCRS The authority code for the source coordinate reference system.
     * @param  targetCRS The authority code for the target coordinate reference system.
     * @return The transformation accuracy, or {@code null}.
     */
    double getAccuracy(XPropertySet xOptions, String sourceCRS, String targetCRS);

    /**
     * Transforms coordinates from the specified source CRS to the specified target CRS.
     *
     * @param  xOptions Provided by OpenOffice.
     * @param  coordinates The coordinates to transform.
     * @param  sourceCRS The authority code for the source coordinate reference system.
     * @param  targetCRS The authority code for the target coordinate reference system.
     * @return The transformed coordinates.
     */
    double[][] getTransformedCoordinates(XPropertySet xOptions, double[][] coordinates, String sourceCRS, String targetCRS);
}
