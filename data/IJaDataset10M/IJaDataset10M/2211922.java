package org.jaitools.media.jai.regionalize;

import javax.media.jai.OperationDescriptorImpl;
import javax.media.jai.registry.RenderedRegistryMode;

/**
 * Describes the "Regionalize" operation.
 * <p>
 * This operation takes a single source image and identifies regions
 * of connected pixels with uniform value, where value comparisons
 * take into account a user-specified tolerance.
 * <p>
 * Note: At present, this operator only deals with a single band.
 * <p>
 * <b>Algorithm</b><p>
 * The operator scans the source image left to right, top to bottom. When
 * it reaches a pixel that has not been allocated to a region yet it uses
 * that pixel as the starting point for a flood-fill search (similar to
 * flood-filling in a paint program). The value of the starting pixel is
 * recorded as the reference value for the new region. The search works
 * its way outwards from the starting pixel, testing other pixels for
 * inclusion in the region. A pixel will be included if: <br>
 * <pre>      |value - reference value| <= tolerance </pre>
 * where tolerance is a user-specified parameter.
 * <p>
 * If the diagonal parameter is set to true, the flood-fill search will
 * include pixels that can only be reached via a diagonal step; if false,
 * only orthogonal steps are taken.
 * <p>
 * The search continues until no further pixels can be added to the region.
 * The region is then allocated a unique integer ID and summary statistics
 * (bounds, number of pixels, reference value) are recorded for it.
 * <p>
 * The output of the operation is an image of data type TYPE_INT, where each
 * pixel's value is its region ID. A {@linkplain RegionData} object can be
 * retrieved as a property of the output image using the property name
 * {@linkplain RegionalizeDescriptor#REGION_DATA_PROPERTY}).
 * <p>
 * <b>Example</b>
 * <pre><code>
 * RenderedImage myImg = ...
 *
 * ParameterBlockJAI pb = new ParameterBlockJAI("regionalize");
 * pb.setSource("source0", myImg);
 * pb.setParameter("band", 0);
 * pb.setParameter("tolerance", 0.1d);
 * pb.setParameter("diagonal", false);
 * RenderedOp regionImg = JAI.create("Regionalize", pb);
 *
 * // have a look at the image (this will force rendering and
 * // the calculation of region data)
 *
 * // print the summary data
 * RegionData regData =
 *    (RegionData)op.getProperty(RegionalizeDescriptor.REGION_DATA_PROPERTY);
 *
 * List&lt;Region> regions = regData.getData();
 * Iterator&lt;Region> iter = regions.iterator();
 * System.out.println("ID\tValue\tSize\tMin X\tMax X\tMin Y\tMax Y");
 * while (iter.hasNext()) {
 *     Region r = iter.next();
 *     System.out.println( String.format("%d\t%.2f\t%d\t%d\t%d\t%d\t%d",
 *         r.getId(),
 *         r.getRefValue(),
 *         r.getNumPixels(),
 *         r.getMinX(),
 *         r.getMaxX(),
 *         r.getMinY(),
 *         r.getMaxY() ));
 * </code></pre>
 *
 * <b>Summary of parameters:</b>
 * <table border="1", cellpadding="3">
 * <tr>
 * <th>Name</th>
 * <th>Class</th>
 * <th>Default</th>
 * <th>Description</th>
 * </tr>
 * </tr>
 * 
 * <tr>
 * <td>band</td>
 * <td>int</td>
 * <td>0</td>
 * <td>The source image band to process</td>
 * </tr>
 * 
 * <tr>
 * <td>tolerance</td>
 * <td>double</td>
 * <td>0</td>
 * <td>Tolerance for comparison of image values</td>
 * </tr>
 * 
 * <tr>
 * <td>diagonal</td>
 * <td>boolean</td>
 * <td>false</td>
 * <td>
 * If {@code true} diagonal connections are allowed; if {@code false}
 * only orthogonal connections are allowed
 * </td>
 * </tr>
 * </table>
 *
 * @author Michael Bedward
 * @since 1.0
 * @version $Id: RegionalizeDescriptor.java 1794 2011-06-23 03:50:58Z michael.bedward $
 */
public class RegionalizeDescriptor extends OperationDescriptorImpl {

    /**
     * The propoerty name to retrieve the {@linkplain RegionData}
     * object which holds summary data for regions identified in
     * the source image and depicted in the destination image
     */
    public static final String REGION_DATA_PROPERTY = "regiondata";

    static final int BAND_ARG_INDEX = 0;

    static final int TOLERANCE_ARG_INDEX = 1;

    static final int DIAGONAL_ARG_INDEX = 2;

    private static final String[] paramNames = { "band", "tolerance", "diagonal" };

    private static final Class[] paramClasses = { Integer.class, Double.class, Boolean.class };

    private static final Object[] paramDefaults = { Integer.valueOf(0), Double.valueOf(0d), Boolean.TRUE };

    /** Constructor. */
    public RegionalizeDescriptor() {
        super(new String[][] { { "GlobalName", "Regionalize" }, { "LocalName", "Regionalize" }, { "Vendor", "org.jaitools.media.jai" }, { "Description", "Identifies sufficiently uniform regions in a source image" }, { "DocURL", "http://code.google.com/p/jaitools/" }, { "Version", "1.0.0" }, { "arg0Desc", "band (int) - the band to regionalize" }, { "arg1Desc", "tolerance (double) - tolerance for pixel value comparison" }, { "arg2Desc", "diagonal (boolean) - true to include diagonal neighbours;" + "false for only orthogonal neighbours" } }, new String[] { RenderedRegistryMode.MODE_NAME }, 1, paramNames, paramClasses, paramDefaults, null);
    }
}
