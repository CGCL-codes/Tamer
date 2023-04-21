package hoverball.debug;

import hoverball.math.*;
import hoverball.layout.Layout;
import java.awt.*;

/**
 * The class Debug is the base class for the <b>Graphical Debugging</b>.
 * When programming Java units you have the possibility to output
 * graphical elements on screen by means of the
 * <a href="../Unit.html#debug(hoverball.debug.Debug)">debug(...)</a>-command.
 * These elements are objects of the class Debug (or another extending class) and
 * can be created and defined in many ways:
 * <p>
 * <ul>
 * <li>You use the predefined debugs of this package.
 *     <p>
 * <li>Debugs can be recursively interlaced. Each debug contains an (initially empty)
 *     array of sub-debugs
 *     <a href="#debugs">debugs</a>,
 *     which can be filled with any desired debugs.
 *     This generates a recursive tree of debug objects. A debug first plots
 *     itself and then the array of its sub-debugs.
 *     <p>
 *     The color of the debug is inherited. Sub-debugs of a blue debug
 *     are blue as well, unless they define an individual color. The variable
 *     <a href="#color">color</a>
 *     allows you to select a color for your debug or to choose the transparent
 *     color <b><code>null</code></b>.
 *     <p>
 *     You can thus simply define your debug as combination of known debugs.
 *     The following debug plots a blue triangle:
 *<pre>
 *class Triangle extends Debug
 *{
 *   Triangle (Vector a, Vector b, Vector c)
 *   {
 *      debugs = new Debug[] { new Line(a,b), new Line(b,c), new Line(c,a) };
 *      color = Color.blue;
 *   }
 *}
 *</pre>
 *     <p>
 * <li>You extend the class Debug and define your own debug by overwriting
 * the method
 *     <a href="#paint(java.awt.Graphics, java.awt.Color, java.awt.Color, double, hoverball.math.Sphere, hoverball.math.Matrix, boolean)">paint(...)</a>.
 *     Debugs are stored relatively to the unit and transformed into
 *     absolute coordinates only at the moment of plotting.
 *     The following debug tags a point by a small circle:
 *<pre>
 *class O extends Debug
 *{
 *   private Vector o;                            // relative position to unit
 *&nbsp;
 *   public O (Vector o) { this.o = vector(o); }  // store relative position
 *&nbsp;
 *   public void paint (Graphics g, Color color, Color globe, double scale, Sphere sphere, Matrix base, boolean front)
 *   {
 *      Vector p = Vector.mul(o,base);            // calculate absolute position
 *      if (front ^ p.z > 0) return;              // wrong side? return!
 *&nbsp;
 *      Point P = scale(scale,p);                 // scale in AWT-coordinates
 *      g.setColor(color(color,globe,p.z));       // set color
 *      g.drawOval(P.x-6,P.y-6,12,12);            // plot circle!
 *   }
 *}
 *</pre>
 * </ul>
 * <p>
 *
 * The color of the Debug is determined by the color specification
 * in the unit's debug(...)-command, or by the unit color itself
 * if it is called wihout color request.
 *
 */
public abstract class Debug {

    /**
    * Reads and corrects a vector.
    * <p>
    * This method should be called in the constructor of a debug, when position
    * vectors are to be transfered and saved. The passed vector is copied
    * and normalized to length 1 - it is hence specific to the scaling
    * of the
    * <a href="#paint(java.awt.Graphics, java.awt.Color, java.awt.Color, double, hoverball.math.Sphere, hoverball.math.Matrix, boolean)">paint(...)</a>-method.
    * If the vector is (0,0,0) or <b><code>null</code></b>,
    * the method returns the <i>"spherical null vector"</i> (0,0,1).
    *
    * @param v vector
    * @return rectified vector
    */
    public static Vector vector(Vector v) {
        if (v == null || v.zero()) return new Vector(0, 0, 1); else return Vector.norm(v);
    }

    /**
    * Detects the AWT coordinates of a vector for a certain scaling.
    * <p>
    * The Hoverball screen is oriented in such a way that
    * the <i>x</i>-axis points upward, the <i>y</i>-axis left and
    * the <i>z</i>-axis forward ("out of the screen").
    * This method projects the 3D coordinate system of the sphere onto
    * the 2D coordinate system of an AWT Graphics Context. You have to
    * indicate how many pixels represent the unit length.
    * The coordinate origin is never displaced.
    *
    * @param scale scaling (in pixels)
    * @param v vector
    * @return the AWT coordinates
    */
    public static Point scale(double scale, Vector v) {
        return Layout.scale(scale, v);
    }

    /**
    * Detects the color for a certain depth.
    * <p>
    * In order to obtain a perspective effect displaying the sphere,
    * objects eclipse in the background. This method calculates the
    * shadowed color of an input color dependent on a certain depth.
    * The depth value ranges between -1 and 1 and should correspond to
    * the <i>z</i>-coordinate of the normalized position vector.
    *
    * @param color color of the debug
    * @param globe color of the sphere
    * @param z depth
    * @return the shadowed color
    */
    public static Color color(Color color, Color globe, double z) {
        return Layout.color(color, globe, z);
    }

    /**
    * Color of a debug
    */
    public Color color = null;

    /**
    * Array of the sub-debugs
    */
    public Debug[] debugs = null;

    /**
    * Empty constructor.
    */
    public Debug() {
    }

    /**
    * Empty for the plotting of a debug on an AWT Graphics Context.
    * <p>
    * This method is responsible for the display of a debug with correct
    * perspective and color. Use the methods
    * <a href="#scale(double, hoverball.math.Vector)">scale(...)</a>
    * and
    * <a href="#color(java.awt.Color, java.awt.Color, double)">color(...)</a>
    * as well as the classes of the package
    * <a href="../math/package-summary.html">hoverball.math</a>.
    *
    * Please note:
    * <p>
    * <ul>
    * <li>The coordinate system of the Graphics Contexts is already shifted so that
    *     the point (0,0) lies in the middle of the sphere.
    *     <p>
    * <li>The scaling measure is chosen in a way that it indicates the
    *     sphere radius (instead of the Hoverball unit length). This means
    *     that vectors of length 1 are scaled alike, independently of the size
    *     of the sphere.
    *     <p>
    * <li>A unit's perspective is passed to the method by the matrix <code>base</code>.
    *     To bring vectors from a unit's vision into the right perspective,
    *     you only need a simple multiplication by the matrix <code>base</code>.
    *     <p>
    * <li>Front and back of the sphere are painted separately in order to effectuate
    *     a neat overlapping of the foreground in front of the background.
    *     The method paint(...) is called twice at each case. It demands a
    *     parameter <code>front</code> specifying which side is about
    *     to be painted.
    * </ul>
    *
    * @param g Graphics Context
    * @param color color of the debug
    * @param globe color of the sphere
    * @param scale scaling (sphere radius in pixels)
    * @param sphere game sphere
    * @param base perspective matrix
    * @param front front or back?
    */
    public void paint(Graphics g, Color color, Color globe, double scale, Sphere sphere, Matrix base, boolean front) {
    }
}
