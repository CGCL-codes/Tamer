package org.havi.ui;

import java.awt.Image;
import java.awt.Point;

/**
 * The {@link org.havi.ui.HImageMatte HImageMatte}class represents a matte that
 * varies over space but is constant over time, it can be specified by an
 * &quot;image mask&quot; (a single channel image) where the pixels indicate
 * matte transparency.
 * <hr>
 * The parameters to the constructors are as follows, in cases where parameters
 * are not used, then the constructor should use the default values.
 * <p>
 * <h3>Default parameter values exposed in the constructors</h3>
 * <table border>
 * <tr>
 * <th>Parameter</th>
 * <th>Description</th>
 * <th>Default value</th>
 * <th>Set method</th>
 * <th>Get method</th>
 * </tr>
 * <tr>
 * <td>data</td>
 * <td>The transparency value for this image matte.</td>
 * <td>null (the matte should be treated as being spatially unvarying and
 * opaque)</td>
 * <td>{@link org.havi.ui.HImageMatte#setMatteData}</td>
 * <td>{@link org.havi.ui.HImageMatte#getMatteData}</td>
 * </tr>
 * </table>
 * <h3>Default parameter values not exposed in the constructors</h3>
 * <table border>
 * <tr>
 * <th>Description</th>
 * <th>Default value</th>
 * <th>Set method</th>
 * <th>Get method</th>
 * </tr>
 * <tr>
 * <td>The pixel offset for the image matte, relative to the top, left corner
 * of its associated component.</td>
 * <td>A java.awt.Point (0, 0)</td>
 * <td>{@link org.havi.ui.HImageMatte#setOffset}</td>
 * <td>{@link org.havi.ui.HImageMatte#getOffset}</td>
 * </tr>
 * </table>
 */
public class HImageMatte implements HMatte {

    /**
	 * Creates an <code>HImageMatte</code> object. See the class description
	 * for details of constructor parameters and default values.
	 */
    public HImageMatte() {
    }

    /**
	 * Creates an <code>HImageMatte</code> object. See the class description
	 * for details of constructor parameters and default values.
	 */
    public HImageMatte(Image data) {
    }

    /**
	 * Sets the data for this matte. Any previously set data is replaced.
	 * <p>
	 * Note that if the size of the image is smaller than the size of the
	 * component to which the matte is applied, the empty space behaves as if it
	 * were an opaque flat matte of value 1.0. By default images are aligned at
	 * the top left corner of the component. This can be changed with the
	 * setOffset method.
	 * @param data the data for this matte. Specify a null object to remove the
	 *            associated data for this matte.
	 */
    public void setMatteData(Image data) {
    }

    /**
	 * Returns the data used for this matte.
	 * @return the data used for this matte (an image) or null if no matte data
	 *         has been set.
	 */
    public Image getMatteData() {
        return (null);
    }

    /**
	 * Set the offset of the matte relative to its component in pixels.
	 * @param p the offset of the matte relative to its component in pixels. If
	 *            p is null a NullPointerException is thrown.
	 */
    public void setOffset(Point p) {
    }

    /**
	 * Get the offset of the matte relative to its component in pixels.
	 * @return the offset of the specified frame of the matte relative to its
	 *         component in pixels (as a Point)
	 */
    public Point getOffset() {
        return (null);
    }
}
