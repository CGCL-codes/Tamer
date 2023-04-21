package org.havi.ui.event;

import java.awt.Color;
import java.awt.Image;

/**
 * This class is able to describe the representation of an event generator as a
 * string, color or symbol (such as a triangle, '>', for 'play'). This allows an
 * application to describe a button on an input device correctly for a given
 * platform.
 * <p>
 * The particular text, color, or symbol can be determined by calling the
 * {@link org.havi.ui.event.HEventRepresentation#getStringgetString},
 * {@link org.havi.ui.event.HEventRepresentation#getColorgetColor}or
 * {@link org.havi.ui.event.HEventRepresentation#getSymbolgetSymbol}methods
 * respectively. All available events should return a valid text representation
 * from the {@linkorg.havi.ui.event.HEventRepresentation#getString getString}
 * method.
 * <p>
 * If supported the six colored key events (<code>VK_COLORED_KEY_0</code>
 * thru <code>VK_COLORED_KEY_5</code>) must also be represented by a color,
 * i.e. the {@linkorg.havi.ui.event.HEventRepresentation#getColor getColor}
 * method must return a valid <code>java.awt.Color</code> object.
 * <p>
 * Key events may also be represented as a symbol - if the platform does not
 * support a symbolic representation for a given event, then the application is
 * responsible for rendering the symbol itself. The rendering of keys with a
 * commonly known representation should follow the guidelines given here, as
 * defined in the following table.
 * <p>
 * <table border>
 * <tr>
 * <th>Event</th>
 * <th>Implied symbol</th>
 * <th>Sample</th>
 * </tr>
 * <tr>
 * <td>VK_GO_TO_START</td>
 * <td>Two equilateral triangles, pointing at a line to the left</td>
 * <td><img src=" {@docRoot}/images/start.gif" alt="*"></td>
 * </tr>
 * <tr>
 * <td>VK_REWIND</td>
 * <td>Two equilateral triangles, pointing to the left</td>
 * <td><img src=" {@docRoot}/images/rewind.gif" alt="*"></td>
 * </tr>
 * <tr>
 * <td>VK_STOP</td>
 * <td>A square</td>
 * <td><img src=" {@docRoot}/images/stop.gif" alt="*"></td>
 * </tr>
 * <tr>
 * <td>VK_PAUSE</td>
 * <td>Two vertical lines, side by side</td>
 * <td><img src=" {@docRoot}/images/pause.gif" alt="*"></td>
 * </tr>
 * <tr>
 * <td>VK_PLAY</td>
 * <td>One equilateral triangle, pointing to the right</td>
 * <td><img src=" {@docRoot}/images/play.gif" alt="*"></td>
 * </tr>
 * <tr>
 * <td>VK_FAST_FWD</td>
 * <td>Two equilateral triangles, pointing to the right</td>
 * <td><img src=" {@docRoot}/images/fastfwd.gif" alt="*"></td>
 * </tr>
 * <tr>
 * <td>VK_GO_TO_END</td>
 * <td>Two equilateral triangles, pointing to a line at the right</td>
 * <td><img src=" {@docRoot}/images/end.gif" alt="*"></td>
 * </tr>
 * <tr>
 * <td>VK_TRACK_PREV</td>
 * <td>One equilateral triangle, pointing to a line at the left</td>
 * <td><img src=" {@docRoot}/images/prevtrack.gif" alt="*"></td>
 * </tr>
 * <tr>
 * <td>VK_TRACK_NEXT</td>
 * <td>One equilateral triangle, pointing to a line at the right</td>
 * <td><img src=" {@docRoot}/images/nexttrack.gif" alt="*"></td>
 * </tr>
 * <tr>
 * <td>VK_RECORD</td>
 * <td>A circle, normally red</td>
 * <td><img src=" {@docRoot}/images/record.gif" alt="*"></td>
 * </tr>
 * <tr>
 * <td>VK_EJECT_TOGGLE</td>
 * <td>A line under a wide triangle which points up</td>
 * <td><img src=" {@docRoot}/images/eject.gif" alt="*"></td>
 * </tr>
 * <tr>
 * <td>VK_VOLUME_UP</td>
 * <td>A ramp, increasing to the right, near a plus sign</td>
 * <td><img src=" {@docRoot}/images/volup.gif" alt="*"></td>
 * </tr>
 * <tr>
 * <td>VK_VOLUME_DOWN</td>
 * <td>A ramp, increasing to the right, near a minus sign</td>
 * <td><img src=" {@docRoot}/images/voldown.gif" alt="*"></td>
 * </tr>
 * <tr>
 * <td>VK_UP</td>
 * <td>An arrow pointing up</td>
 * <td><img src=" {@docRoot}/images/up.gif" alt="*"></td>
 * </tr>
 * <tr>
 * <td>VK_DOWN</td>
 * <td>An arrow pointing down</td>
 * <td><img src=" {@docRoot}/images/down.gif" alt="*"></td>
 * </tr>
 * <tr>
 * <td>VK_LEFT</td>
 * <td>An arrow pointing to the left</td>
 * <td><img src=" {@docRoot}/images/left.gif" alt="*"></td>
 * </tr>
 * <tr>
 * <td>VK_RIGHT</td>
 * <td>An arrow pointing to the right</td>
 * <td><img src=" {@docRoot}/images/right.gif" alt="*"></td>
 * </tr>
 * <tr>
 * <td>VK_POWER</td>
 * <td>A circle, broken at the top, with a vertical line in the break</td>
 * <td><img src=" {@docRoot}/images/power.gif" alt="*"></td>
 * </tr>
 * </table>
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
 * <td colspan=5>None.</td>
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
 * <td colspan=4>None.</td>
 * </tr>
 * </table>
 */
public class HEventRepresentation extends Object {

    /**
	 * The event representation type for the current event is not supported.
	 * <p>
	 * The four ER_TYPE integers describe if an input mechanism is not
	 * supported, or is described by a string, color or symbol.
	 * <p>
	 * The values of the four ER_TYPE integers are required to be bitwise
	 * distinct, and the value of ER_TYPE_NOT_SUPPORTED should be 0.
	 * @see org.havi.ui.event.HEventRepresentation#ER_TYPE_STRING
	 * @see org.havi.ui.event.HEventRepresentation#ER_TYPE_COLOR
	 * @see org.havi.ui.event.HEventRepresentation#ER_TYPE_SYMBOL
	 */
    public static final int ER_TYPE_NOT_SUPPORTED = 0;

    /**
	 * The event representation type for the current event is supported as a
	 * string.
	 * @see org.havi.ui.event.HEventRepresentation#ER_TYPE_NOT_SUPPORTED
	 */
    public static final int ER_TYPE_STRING = 1;

    /**
	 * The event representation type for the current event is supported as a
	 * color.
	 * @see org.havi.ui.event.HEventRepresentation#ER_TYPE_NOT_SUPPORTED
	 */
    public static final int ER_TYPE_COLOR = 2;

    /**
	 * The event representation type for the current event is supported as a
	 * symbol.
	 * @see org.havi.ui.event.HEventRepresentation#ER_TYPE_NOT_SUPPORTED
	 */
    public static final int ER_TYPE_SYMBOL = 4;

    /**
	 * It is not intended that applications should directly construct
	 * {@link org.havi.ui.event.HEventRepresentation HEventRepresentation}
	 * objects.
	 * <p>
	 * Creates an {@link org.havi.ui.event.HEventRepresentation
	 * HEventRepresentation} object. See the class description for details of
	 * constructor parameters and default values.
	 * <p>
	 * This method is protected to allow the platform to override it in a
	 * different package scope.
	 */
    protected HEventRepresentation() {
    }

    /**
	 * This method returns true if the current event is supported by the
	 * platform.
	 */
    public boolean isSupported() {
        return (false);
    }

    /**
	 * Sets the type of representation(s) available for the event code which
	 * this this instance of {@linkorg.havi.ui.event.HEventRepresentation
	 * HEventRepresentation} represents. If no representation(s) are available
	 * then <code>aType</code> should be set to ER_TYPE_NOT_SUPPORTED.
	 * Otherwise the representation should be set to the sum of one or more of
	 * the following: ER_TYPE_STRING, ER_TYPE_COLOR, ER_TYPE_SYMBOL.
	 * <p>
	 * For example, if both string and color representations are available then
	 * the system should call this method with the parameter set to
	 * <code>ER_TYPE_STRING + ER_TYPE_COLOR</code>.
	 * <p>
	 * This method is protected to allow the platform to override it in
	 * subclasses of HEventRepresentation. It is not intended for use by the
	 * application and conformant applications shall not use this method.
	 * @param aType the type of representation(s) available for this event
	 */
    protected void setType(int aType) {
    }

    /**
	 * This returns the type of representation(s) available for the event code
	 * which this instance of {@linkorg.havi.ui.event.HEventRepresentation
	 * HEventRepresentation} represents.
	 * <p>
	 * If the event code can be represented in multiple ways, then the returned
	 * type will be the sum of the supported types, e.g. an event generated by a
	 * key with a particular font representation of an &quot;A&quot; in yellow
	 * might return ER_TYPE_STRING + ER_TYPE_COLOR + ER_TYPE_SYMBOL. Where the
	 * string representation is &quot;A&quot;, the color representation is
	 * &quot;yellow&quot; and the symbol representation might be a likeness of
	 * the &quot;A glyph&quot; from a particular font.
	 */
    public int getType() {
        return (0);
    }

    /**
	 * Sets the Color representation for this {@link
	 * org.havi.ui.event.HEventRepresentation HEventRepresentation}. Any
	 * previous value is overwritten.
	 * <p>
	 * This method is protected to allow the platform to override it in
	 * subclasses of HEventRepresentation. It is not intended for use by the
	 * application and conformant applications shall not use this method.
	 * @param aColor - the color to be associated with this event.
	 */
    protected void setColor(java.awt.Color aColor) {
    }

    /**
	 * This returns the color representation (generally used for colored soft
	 * keys) of the current event code.
	 * @return The color representation of the current event code, or null if
	 *         not available.
	 */
    public Color getColor() {
        return (null);
    }

    /**
	 * Sets the string representation for this {@link
	 * org.havi.ui.event.HEventRepresentation HEventRepresentation}. Any
	 * previous value is overwritten.
	 * <p>
	 * This method is protected to allow the platform to override it in
	 * subclasses of HEventRepresentation. It is not intended for use by the
	 * application and conformant applications shall not use this method.
	 * @param aText - the text string to be associated with this event.
	 */
    protected void setString(String aText) {
    }

    /**
	 * Returns the text representation of the current event code.
	 * @return The text representation of the current event code, or null if not
	 *         available.
	 */
    public String getString() {
        return (null);
    }

    /**
	 * Sets the symbolic representation for this {@link
	 * org.havi.ui.event.HEventRepresentation HEventRepresentation}. Any
	 * previous value is overwritten.
	 * <p>
	 * This method is protected to allow the platform to override it in
	 * subclasses of HEventRepresentation. It is not intended for use by the
	 * application and conformant applications shall not use this method.
	 * @param aSymbol - the symbol image to be associated with this event.
	 */
    protected void setSymbol(java.awt.Image aSymbol) {
    }

    /**
	 * This returns an image-based representation (generally used for symbolic
	 * keys) of the current event code.
	 * <p>
	 * Note that it is platform specific whether this method will return a valid
	 * Image, in particular it is a valid implementation option to always return
	 * null. Note that for non-null Images, the size and other Image
	 * characteristics are dependent on particular manufacturer implementation.
	 * @return The symbolic representation of the current event code, or null if
	 *         not available.
	 */
    public Image getSymbol() {
        return (null);
    }
}
