package visad;

import java.awt.Font;
import visad.util.HersheyFont;
import java.text.*;
import java.rmi.*;
import visad.util.Util;

/**
   TextControl is the VisAD class for controlling Text display scalars.<P>
*/
public class TextControl extends Control {

    private Object font = null;

    private double size = 1.0;

    private boolean sphere = false;

    private NumberFormat format = null;

    private double rotation = 0.0;

    private double characterRotation = 0.0;

    private double scale = 1.0;

    private double[] offset = new double[] { 0.0, 0.0, 0.0 };

    private boolean autoSize = false;

    private ProjectionControlListener pcl = null;

    /**
   * A class to represent the different types of justification.
   * Use a class so the user can't just pass in an arbitrary integer
   *
   * @author abcd 5 February 2001
   */
    public static class Justification {

        private String name;

        /** Predefined value for left justification */
        public static final Justification LEFT = new Justification("Left");

        /** Predefined value for center justification */
        public static final Justification CENTER = new Justification("Center");

        /** Predefined value for right justification */
        public static final Justification RIGHT = new Justification("Right");

        /** Predefined value for top justification */
        public static final Justification TOP = new Justification("Top");

        /** Predefined value for bottom justification */
        public static final Justification BOTTOM = new Justification("Bottom");

        /**
     * Constructor - simply store the name
     */
        public Justification(String newName) {
            name = newName;
        }
    }

    private Justification justification = Justification.LEFT;

    private Justification verticalJustification = Justification.BOTTOM;

    public TextControl(DisplayImpl d) {
        super(d);
    }

    public void setAutoSize(boolean auto) throws VisADException {
        if (auto == autoSize) return;
        DisplayImpl display = getDisplay();
        DisplayRenderer dr = display.getDisplayRenderer();
        MouseBehavior mouse = dr.getMouseBehavior();
        ProjectionControl pc = display.getProjectionControl();
        if (auto) {
            pcl = new ProjectionControlListener(mouse, this, pc);
            pc.addControlListener(pcl);
        } else {
            pc.removeControlListener(pcl);
        }
        autoSize = auto;
        try {
            changeControl(true);
        } catch (RemoteException e) {
        }
    }

    public boolean getAutoSize() {
        return autoSize;
    }

    public void nullControl() {
        try {
            setAutoSize(false);
        } catch (VisADException e) {
        }
        super.nullControl();
    }

    /** set the font; in the initial release this has no effect
  *
  * @param f is the java.awt.Font or the visad.util.HersheyFont
  */
    public void setFont(Object f) throws VisADException, RemoteException {
        if (f instanceof java.awt.Font || f instanceof visad.util.HersheyFont || f == null) {
            font = f;
            changeControl(true);
        } else {
            throw new VisADException("Font must be java.awt.Font or HersheyFont");
        }
    }

    /** return the java.awt.Font
  *
  * @return the java.awt.Font if the font is of that type; otherwise, null
  */
    public Font getFont() {
        if (font instanceof java.awt.Font) {
            return (Font) font;
        } else {
            return null;
        }
    }

    /** return the HersheyFont
  *
  * @return the visad.util.HersheyFont if the font is of
  * that type; otherwise, null
  */
    public HersheyFont getHersheyFont() {
        if (font instanceof visad.util.HersheyFont) {
            return (HersheyFont) font;
        } else {
            return null;
        }
    }

    /** set the centering flag; if true, text will be centered at
      mapped locations; if false, text will be to the right
      of mapped locations */
    public void setCenter(boolean c) throws VisADException, RemoteException {
        justification = Justification.CENTER;
        changeControl(true);
    }

    /** return the centering flag */
    public boolean getCenter() {
        if (justification == Justification.CENTER) {
            return true;
        } else {
            return false;
        }
    }

    public void setJustification(Justification newJustification) throws VisADException, RemoteException {
        justification = newJustification;
        changeControl(true);
    }

    public Justification getJustification() {
        return justification;
    }

    /**
   * Set the vertical justification flag
   *
   * Possible values are TextControl.Justification.TOP,
   * TextControl.Justification.CENTER and TextControl.Justification.BOTTOM
   *
   * @author abcd 19 March 2003
   */
    public void setVerticalJustification(Justification newJustification) throws VisADException, RemoteException {
        verticalJustification = newJustification;
        changeControl(true);
    }

    /**
   * Return the vertical justification value
   *
   * @author abcd 19 March 2003
   */
    public Justification getVerticalJustification() {
        return verticalJustification;
    }

    /** set the size of characters; the default is 1.0 */
    public void setSize(double s) throws VisADException, RemoteException {
        size = s;
        changeControl(true);
    }

    /** return the size */
    public double getSize() {
        return size;
    }

    public void setSphere(boolean s) throws VisADException, RemoteException {
        sphere = s;
        changeControl(true);
    }

    public boolean getSphere() {
        return sphere;
    }

    public void setNumberFormat(NumberFormat f) throws VisADException, RemoteException {
        format = f;
        changeControl(true);
    }

    public NumberFormat getNumberFormat() {
        return format;
    }

    private boolean fontEquals(Object newFont) {
        if (font == null) {
            if (newFont != null) {
                return false;
            }
        } else if (newFont == null) {
            return false;
        } else if (!font.equals(newFont)) {
            return false;
        }
        return true;
    }

    private boolean formatEquals(NumberFormat newFormat) {
        if (format == null) {
            if (newFormat != null) {
                return false;
            }
        } else if (newFormat == null) {
            return false;
        } else if (!format.equals(newFormat)) {
            return false;
        }
        return true;
    }

    /**
   * Set the rotation
   *
   * abcd 1 February 2001
   */
    public void setRotation(double newRotation) throws VisADException, RemoteException {
        rotation = newRotation;
        changeControl(true);
    }

    /**
   * Get the rotation
   *
   * abcd 1 February 2001
   */
    public double getRotation() {
        return rotation;
    }

    /** get a string that can be used to reconstruct this control later */
    public String getSaveString() {
        return null;
    }

    /** reconstruct this control using the specified save string */
    public void setSaveString(String save) throws VisADException, RemoteException {
        throw new UnimplementedException("Cannot setSaveString on this type of control");
    }

    /** copy the state of a remote control to this control */
    public void syncControl(Control rmt) throws VisADException {
        if (rmt == null) {
            throw new VisADException("Cannot synchronize " + getClass().getName() + " with null Control object");
        }
        if (!(rmt instanceof TextControl)) {
            throw new VisADException("Cannot synchronize " + getClass().getName() + " with " + rmt.getClass().getName());
        }
        TextControl tc = (TextControl) rmt;
        boolean changed = false;
        if (!fontEquals(tc.font)) {
            changed = true;
            font = tc.font;
        }
        if (justification != tc.justification) {
            changed = true;
            justification = tc.justification;
        }
        if (verticalJustification != tc.verticalJustification) {
            changed = true;
            verticalJustification = tc.verticalJustification;
        }
        if (!Util.isApproximatelyEqual(size, tc.size)) {
            changed = true;
            size = tc.size;
        }
        if (sphere != tc.sphere) {
            changed = true;
            sphere = tc.sphere;
        }
        if (!formatEquals(tc.format)) {
            changed = true;
            format = tc.format;
        }
        if (!Util.isApproximatelyEqual(rotation, tc.rotation)) {
            changed = true;
            rotation = tc.rotation;
        }
        if (!Util.isApproximatelyEqual(characterRotation, tc.characterRotation)) {
            changed = true;
            characterRotation = tc.characterRotation;
        }
        if (autoSize != tc.autoSize) {
            setAutoSize(tc.autoSize);
        }
        if (!Util.isApproximatelyEqual(scale, tc.scale)) {
            changed = true;
            scale = tc.scale;
        }
        for (int i = 0; i < 3; i++) {
            if (!Util.isApproximatelyEqual(offset[i], tc.offset[i])) {
                changed = true;
                offset[i] = tc.offset[i];
            }
        }
        if (changed) {
            try {
                changeControl(true);
            } catch (RemoteException re) {
                throw new VisADException("Could not indicate that control" + " changed: " + re.getMessage());
            }
        }
    }

    public boolean equals(Object o) {
        if (!super.equals(o)) {
            return false;
        }
        TextControl tc = (TextControl) o;
        if (!fontEquals(font)) {
            return false;
        }
        if (justification != tc.justification) {
            return false;
        }
        if (verticalJustification != tc.verticalJustification) {
            return false;
        }
        if (sphere != tc.sphere) {
            return false;
        }
        if (!formatEquals(tc.format)) {
            return false;
        }
        if (!Util.isApproximatelyEqual(rotation, tc.rotation)) {
            return false;
        }
        if (!Util.isApproximatelyEqual(size, tc.size)) {
            return false;
        }
        if (autoSize != tc.autoSize) {
            return false;
        }
        if (!Util.isApproximatelyEqual(characterRotation, tc.characterRotation)) {
            return false;
        }
        if (!Util.isApproximatelyEqual(scale, tc.scale)) {
            return false;
        }
        for (int i = 0; i < 3; i++) {
            if (!Util.isApproximatelyEqual(offset[i], tc.offset[i])) {
                return false;
            }
        }
        return true;
    }

    /**
   * Gets the value of characterRotation
   *
   * @return the value of characterRotation
   */
    public double getCharacterRotation() {
        return this.characterRotation;
    }

    /**
   * Sets the value of characterRotation
   *
   * @param argCharacterRotation Value to assign to this.characterRotation
   */
    public void setCharacterRotation(double argCharacterRotation) throws VisADException, RemoteException {
        this.characterRotation = argCharacterRotation;
        changeControl(true);
    }

    /**
   * Gets the value of scale
   *
   * @return the value of scale
   */
    public double getScale() {
        return this.scale;
    }

    /**
   * Sets the value of scale
   *
   * @param argScale Value to assign to this.scale
   */
    public void setScale(double argScale) throws VisADException, RemoteException {
        this.scale = argScale;
        changeControl(true);
    }

    /**
   * Gets the value of offset
   *
   * @return the value of offset
   */
    public double[] getOffset() {
        double[] aOffset = new double[] { this.offset[0], this.offset[1], this.offset[2] };
        return aOffset;
    }

    /**
   * Sets the value of offset
   *
   * @param argOffset Value to assign to this.offset
   */
    public void setOffset(double[] argOffset) throws VisADException, RemoteException {
        this.offset[0] = argOffset[0];
        this.offset[1] = argOffset[1];
        this.offset[2] = argOffset[2];
        changeControl(true);
    }

    class ProjectionControlListener implements ControlListener {

        private boolean pfirst = true;

        private MouseBehavior mouse;

        private ProjectionControl pcontrol;

        private TextControl text_control;

        private double base_scale = 1.0;

        private float last_cscale = 1.0f;

        private double base_size = 1.0;

        ProjectionControlListener(MouseBehavior m, TextControl t, ProjectionControl p) {
            mouse = m;
            text_control = t;
            pcontrol = p;
        }

        public void controlChanged(ControlEvent e) throws VisADException, RemoteException {
            double[] matrix = pcontrol.getMatrix();
            double[] rot = new double[3];
            double[] scale = new double[1];
            double[] trans = new double[3];
            mouse.instance_unmake_matrix(rot, scale, trans, matrix);
            if (pfirst) {
                pfirst = false;
                base_scale = scale[0];
                last_cscale = 1.0f;
                base_size = text_control.getSize();
            } else {
                float cscale = (float) (base_scale / scale[0]);
                float ratio = cscale / last_cscale;
                if (ratio < 0.95f || 1.05f < ratio) {
                    last_cscale = cscale;
                    text_control.setSize(base_size * cscale);
                }
            }
        }
    }
}
