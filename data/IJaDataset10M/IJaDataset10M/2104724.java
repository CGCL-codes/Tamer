package net.sourceforge.mazix.uicomponents.graphic.labels.buttons;

import static javax.media.j3d.BranchGroup.ALLOW_DETACH;
import static javax.media.j3d.Group.ALLOW_CHILDREN_EXTEND;
import static javax.media.j3d.Group.ALLOW_CHILDREN_WRITE;
import static javax.media.j3d.Shape3D.ALLOW_APPEARANCE_WRITE;
import static javax.media.j3d.Shape3D.ALLOW_GEOMETRY_WRITE;
import static javax.media.j3d.Text3D.ALIGN_CENTER;
import static javax.media.j3d.Text3D.PATH_RIGHT;
import static net.sourceforge.mazix.components.constants.CommonConstants.BLANK_STRING;
import static net.sourceforge.mazix.uicomponents.constants.GraphicConstants.DEFAULT_ACTIVATED_APPEARANCE;
import static net.sourceforge.mazix.components.constants.GraphicConstants.DEFAULT_FONT_NAME;
import static net.sourceforge.mazix.components.constants.GraphicConstants.DEFAULT_FONT_SIZE;
import static net.sourceforge.mazix.components.constants.GraphicConstants.DEFAULT_FONT_STYLE;
import static net.sourceforge.mazix.uicomponents.constants.GraphicConstants.DEFAULT_NOT_ACTIVATED_APPEARANCE;
import static net.sourceforge.mazix.uicomponents.constants.GraphicConstants.X_DEFAULT;
import static net.sourceforge.mazix.uicomponents.constants.GraphicConstants.Y_DEFAULT;
import static net.sourceforge.mazix.uicomponents.constants.GraphicConstants.Z_DEFAULT;
import static net.sourceforge.mazix.uicomponents.resource.AbstractResourceManagerAdapter.getFont3D;
import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Font3D;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Text3D;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

/**
 * Class storing a menus on a screen which can be activated by the user. It stores several menu
 * properties, its text, its coordinates on the screen and its activation.
 * 
 * @author Benjamin Croizet (graffity2199@yahoo.fr)
 * @since 0.6
 * @version 0.7
 */
public class GraphicButton3D extends AbstractGraphicButton {

    /** The button {@code Text3D}. */
    private Text3D buttonText3D = null;

    /** The button {@code Font3D} which is used. */
    private Font3D buttonFont3D = null;

    /** The {@code Shape3D} of the button. */
    private Shape3D buttonInternalShape = null;

    /** The {@code Appearance} of the button when activated. */
    private Appearance buttonActivatedAppearance = null;

    /** The {@code Appearance} of the button when not activated. */
    private Appearance buttonNotActivatedAppearance = null;

    /**
     * Default constructor. Sets all labels, position, font, appearances and button activation to
     * their default value.
     * 
     * @since 0.6
     * @see #GraphicButton3D(String, String, String)
     * @see #GraphicButton3D(String, float, float, float)
     * @see #GraphicButton3D(String, String, String, Vector3f)
     * @see #GraphicButton3D(String, String, float, float, float)
     * @see #GraphicButton3D(String, String, String, float, float, float)
     * @see #GraphicButton3D(String, String, String, Font3D, Appearance, Appearance, Vector3f)
     * @see #GraphicButton3D(String, String, String, Font3D, float, float, float)
     */
    public GraphicButton3D() {
        this(BLANK_STRING, BLANK_STRING, BLANK_STRING, getFont3D(DEFAULT_FONT_NAME, DEFAULT_FONT_STYLE, DEFAULT_FONT_SIZE), DEFAULT_ACTIVATED_APPEARANCE, DEFAULT_NOT_ACTIVATED_APPEARANCE, new Vector3f(X_DEFAULT, Y_DEFAULT, Z_DEFAULT));
    }

    /**
     * Medium constructor. May throw a {@code NullPointerException} if {@code labelT} is
     * <code>null</code>. Sets the button activation, font and appearances to default.
     * 
     * @param labelT
     *            the button label text, mustn't be <code>null</code>.
     * @param xCoord
     *            the x coordinate of the button.
     * @param yCoord
     *            the y coordinate of the button.
     * @param zCoord
     *            the z coordinate of the button.
     * @since 0.6
     * @see #GraphicButton3D()
     * @see #GraphicButton3D(String, String, String)
     * @see #GraphicButton3D(String, String, String, Vector3f)
     * @see #GraphicButton3D(String, String, float, float, float)
     * @see #GraphicButton3D(String, String, String, float, float, float)
     * @see #GraphicButton3D(String, String, String, Font3D, Appearance, Appearance, Vector3f)
     * @see #GraphicButton3D(String, String, String, Font3D, float, float, float)
     */
    public GraphicButton3D(final String labelT, final float xCoord, final float yCoord, final float zCoord) {
        this(labelT, BLANK_STRING, BLANK_STRING, getFont3D(DEFAULT_FONT_NAME, DEFAULT_FONT_STYLE, DEFAULT_FONT_SIZE), DEFAULT_ACTIVATED_APPEARANCE, DEFAULT_NOT_ACTIVATED_APPEARANCE, new Vector3f(xCoord, yCoord, zCoord));
    }

    /**
     * Medium constructor. May throw a {@code NullPointerException} if {@code labelT} or {@code
     * labelV} are <code>null</code> . Sets the button activation, font and appearances to default.
     * 
     * @param labelT
     *            the button label text, mustn't be <code>null</code>.
     * @param labelV
     *            the button label value, mustn't be <code>null</code>.
     * @param xCoord
     *            the x coordinate of the button.
     * @param yCoord
     *            the y coordinate of the button.
     * @param zCoord
     *            the z coordinate of the button.
     * @since 0.6
     * @see #GraphicButton3D()
     * @see #GraphicButton3D(String, String, String)
     * @see #GraphicButton3D(String, float, float, float)
     * @see #GraphicButton3D(String, String, String, Vector3f)
     * @see #GraphicButton3D(String, String, String, float, float, float)
     * @see #GraphicButton3D(String, String, String, Font3D, Appearance, Appearance, Vector3f)
     * @see #GraphicButton3D(String, String, String, Font3D, float, float, float)
     */
    public GraphicButton3D(final String labelT, final String labelV, final float xCoord, final float yCoord, final float zCoord) {
        this(labelT, labelV, BLANK_STRING, getFont3D(DEFAULT_FONT_NAME, DEFAULT_FONT_STYLE, DEFAULT_FONT_SIZE), DEFAULT_ACTIVATED_APPEARANCE, DEFAULT_NOT_ACTIVATED_APPEARANCE, new Vector3f(xCoord, yCoord, zCoord));
    }

    /**
     * Medium constructor. May throw a {@code NullPointerException} if {@code labelT}, {@code
     * labelS} or {@code labelV} are <code>null</code>. Sets the button activation, font and
     * appearances to default.
     * 
     * @param labelT
     *            the button label text, mustn't be <code>null</code>.
     * @param labelS
     *            the button label separator, mustn't be <code>null</code>.
     * @param labelV
     *            the button label value, mustn't be <code>null</code>.
     * @since 0.6
     * @see #GraphicButton3D()
     * @see #GraphicButton3D(String, float, float, float)
     * @see #GraphicButton3D(String, String, String, Vector3f)
     * @see #GraphicButton3D(String, String, float, float, float)
     * @see #GraphicButton3D(String, String, String, float, float, float)
     * @see #GraphicButton3D(String, String, String, Font3D, Appearance, Appearance, Vector3f)
     * @see #GraphicButton3D(String, String, String, Font3D, float, float, float)
     */
    public GraphicButton3D(final String labelT, final String labelS, final String labelV) {
        this(labelT, labelS, labelV, getFont3D(DEFAULT_FONT_NAME, DEFAULT_FONT_STYLE, DEFAULT_FONT_SIZE), DEFAULT_ACTIVATED_APPEARANCE, DEFAULT_NOT_ACTIVATED_APPEARANCE, new Vector3f(X_DEFAULT, Y_DEFAULT, Z_DEFAULT));
    }

    /**
     * Medium constructor. May throw a {@code NullPointerException} if {@code labelT}, {@code
     * labelS} or {@code labelV} are <code>null</code>. Sets the button activation, font and
     * appearances to default.
     * 
     * @param labelT
     *            the button label text, mustn't be <code>null</code>.
     * @param labelS
     *            the button label separator, mustn't be <code>null</code>.
     * @param labelV
     *            the button label value, mustn't be <code>null</code>.
     * @param xCoord
     *            the x coordinate of the button.
     * @param yCoord
     *            the y coordinate of the button.
     * @param zCoord
     *            the z coordinate of the button.
     * @since 0.6
     * @see #GraphicButton3D()
     * @see #GraphicButton3D(String, String, String)
     * @see #GraphicButton3D(String, float, float, float)
     * @see #GraphicButton3D(String, String, String, Vector3f)
     * @see #GraphicButton3D(String, String, float, float, float)
     * @see #GraphicButton3D(String, String, String, Font3D, Appearance, Appearance, Vector3f)
     * @see #GraphicButton3D(String, String, String, Font3D, float, float, float)
     */
    public GraphicButton3D(final String labelT, final String labelS, final String labelV, final float xCoord, final float yCoord, final float zCoord) {
        this(labelT, labelS, labelV, getFont3D(DEFAULT_FONT_NAME, DEFAULT_FONT_STYLE, DEFAULT_FONT_SIZE), DEFAULT_ACTIVATED_APPEARANCE, DEFAULT_NOT_ACTIVATED_APPEARANCE, new Vector3f(xCoord, yCoord, zCoord));
    }

    /**
     * Full constructor. May throw a {@code NullPointerException} if {@code labelT}, {@code labelS}
     * or {@code labelV}, or {@code position} are <code>null</code>. Sets the button activation to
     * default.
     * 
     * @param labelT
     *            the button label text, mustn't be <code>null</code>.
     * @param labelS
     *            the button label separator, mustn't be <code>null</code>.
     * @param labelV
     *            the button label value, mustn't be <code>null</code>.
     * @param font
     *            the button font, mustn't be <code>null</code>.
     * @param actApp
     *            the button appearances when activated, mustn't be <code>null</code>.
     * @param notActApp
     *            the button appearances when not activated, mustn't be <code>null</code>.
     * @param position
     *            the {@code Vector3f} containing the 3D position of the button, can't be
     *            <code>null</code>.
     * @since 0.6
     * @see #GraphicButton3D()
     * @see #GraphicButton3D(String, String, String)
     * @see #GraphicButton3D(String, float, float, float)
     * @see #GraphicButton3D(String, String, String, Vector3f)
     * @see #GraphicButton3D(String, String, float, float, float)
     * @see #GraphicButton3D(String, String, String, float, float, float)
     * @see #GraphicButton3D(String, String, String, Font3D, float, float, float)
     */
    public GraphicButton3D(final String labelT, final String labelS, final String labelV, final Font3D font, final Appearance actApp, final Appearance notActApp, final Vector3f position) {
        super(labelT, labelS, labelV, position);
        assert font != null : "font is null";
        assert actApp != null : "actApp is null";
        assert notActApp != null : "notActApp is null";
        setButtonFont3D(font);
        setButtonActivatedAppearance(actApp);
        setButtonNotActivatedAppearance(notActApp);
    }

    /**
     * Medium constructor. May throw a {@code NullPointerException} if {@code labelT}, {@code
     * labelS} or {@code labelV} are <code>null</code>. Sets the button activation, font and
     * appearances to default.
     * 
     * @param labelT
     *            the button label text, mustn't be <code>null</code>.
     * @param labelS
     *            the button label separator, mustn't be <code>null</code>.
     * @param labelV
     *            the button label value, mustn't be <code>null</code>.
     * @param font
     *            the button font, mustn't be <code>null</code>.
     * @param xCoord
     *            the x coordinate of the button.
     * @param yCoord
     *            the y coordinate of the button.
     * @param zCoord
     *            the z coordinate of the button.
     * @since 0.6
     * @see #GraphicButton3D()
     * @see #GraphicButton3D(String, String, String)
     * @see #GraphicButton3D(String, float, float, float)
     * @see #GraphicButton3D(String, String, String, Vector3f)
     * @see #GraphicButton3D(String, String, float, float, float)
     * @see #GraphicButton3D(String, String, String, float, float, float)
     * @see #GraphicButton3D(String, String, String, Font3D, Appearance, Appearance, Vector3f)
     */
    public GraphicButton3D(final String labelT, final String labelS, final String labelV, final Font3D font, final float xCoord, final float yCoord, final float zCoord) {
        this(labelT, labelS, labelV, font, DEFAULT_ACTIVATED_APPEARANCE, DEFAULT_NOT_ACTIVATED_APPEARANCE, new Vector3f(xCoord, yCoord, zCoord));
    }

    /**
     * Full constructor. May throw a {@code NullPointerException} if {@code labelT}, {@code labelS}
     * or {@code labelV}, or {@code position} are <code>null</code>. Sets the button activation to
     * default.
     * 
     * @param labelT
     *            the button label text, mustn't be <code>null</code>.
     * @param labelS
     *            the button label separator, mustn't be <code>null</code>.
     * @param labelV
     *            the button label value, mustn't be <code>null</code>.
     * @param position
     *            the {@code Vector3f} containing the 3D position of the button, can't be
     *            <code>null</code>.
     * @since 0.4
     * @see #GraphicButton3D()
     * @see #GraphicButton3D(String, String, String)
     * @see #GraphicButton3D(String, float, float, float)
     * @see #GraphicButton3D(String, String, float, float, float)
     * @see #GraphicButton3D(String, String, String, float, float, float)
     * @see #GraphicButton3D(String, String, String, Font3D, Appearance, Appearance, Vector3f)
     * @see #GraphicButton3D(String, String, String, Font3D, float, float, float)
     */
    public GraphicButton3D(final String labelT, final String labelS, final String labelV, final Vector3f position) {
        this(labelT, labelS, labelV, getFont3D(DEFAULT_FONT_NAME, DEFAULT_FONT_STYLE, DEFAULT_FONT_SIZE), DEFAULT_ACTIVATED_APPEARANCE, DEFAULT_NOT_ACTIVATED_APPEARANCE, position);
    }

    /**
     * @see org.mazix.component3D.GraphicGameComponent#buildGraphicShape()
     * @since 0.7
     */
    @Override
    protected BranchGroup buildGraphicShape() {
        assert getButtonInternalShape() != null : "getButtonInternalShape() is null";
        final BranchGroup shape = new BranchGroup();
        shape.setCapability(ALLOW_DETACH);
        shape.setCapability(ALLOW_CHILDREN_WRITE);
        shape.setCapability(ALLOW_CHILDREN_EXTEND);
        shape.addChild(getButtonInternalShape());
        return shape;
    }

    /**
     * Be careful, this method doesn't entirely clone the {@code Font3D} field and the {@code
     * Appearance}s. The {@code Font3D} is managed by the {@code ResourceManager} so they should
     * have the same reference. The {@code Appearance}s of the button have also the same reference
     * as the cloned object because of their complexity, so you should use the {@code
     * setButtonAppearance} method after the clone method if you don't want the same reference.
     * 
     * @since 0.6
     */
    @Override
    public GraphicButton3D deepClone() {
        final GraphicButton3D b = (GraphicButton3D) super.deepClone();
        b.setButtonText3D(null);
        b.setButtonInternalShape(null);
        return b;
    }

    /**
     * Gets the value of buttonActivatedAppearance.
     * 
     * @return the value of buttonActivatedAppearance.
     * @see #setButtonActivatedAppearance(Appearance)
     * @since 0.6
     */
    public Appearance getButtonActivatedAppearance() {
        return buttonActivatedAppearance;
    }

    /**
     * Gets the value of buttonFont3D.
     * 
     * @return the value of buttonFont3D.
     * @see #setButtonFont3D(Font3D)
     * @since 0.7
     */
    private Font3D getButtonFont3D() {
        return buttonFont3D;
    }

    /**
     * Gets the value of buttonInternalShape.
     * 
     * @return the value of buttonInternalShape.
     * @see #setButtonNotActivatedAppearance(Appearance)
     * @since 0.7
     */
    private Shape3D getButtonInternalShape() {
        assert getButtonText3D() != null : "getButtonText3D() is null";
        assert getButtonActivatedAppearance() != null : "getButtonActivatedAppearance() is null";
        assert getButtonNotActivatedAppearance() != null : "getButtonNotActivatedAppearance() is null";
        if (buttonInternalShape == null) {
            buttonInternalShape = new Shape3D(getButtonText3D(), isActivated() ? getButtonActivatedAppearance() : getButtonNotActivatedAppearance());
            buttonInternalShape.setCapability(ALLOW_GEOMETRY_WRITE);
            buttonInternalShape.setCapability(ALLOW_APPEARANCE_WRITE);
        }
        return buttonInternalShape;
    }

    /**
     * Gets the value of buttonNotActivatedAppearance.
     * 
     * @return the value of buttonNotActivatedAppearance.
     * @see #setButtonNotActivatedAppearance(Appearance)
     * @since 0.7
     */
    public Appearance getButtonNotActivatedAppearance() {
        return buttonNotActivatedAppearance;
    }

    /**
     * Gets the value of buttonText3D.
     * 
     * @return the value of buttonText3D.
     * @see #setButtonText3D(Text3D)
     * @since 0.7
     */
    private Text3D getButtonText3D() {
        assert getLabelText() != null : "getLabelText() is null";
        assert getLabelSeparator() != null : "getLabelSeparator() is null";
        assert getLabelValue() != null : "getLabelValue() is null";
        assert getButtonFont3D() != null : "getButtonFont3D() is null";
        if (buttonText3D == null) {
            buttonText3D = new Text3D(getButtonFont3D(), getLabelText() + getLabelSeparator() + getLabelValue(), new Point3f(0, 0, 0), ALIGN_CENTER, PATH_RIGHT);
        }
        return buttonText3D;
    }

    /**
     * Gets the value of labelFont.
     * 
     * @return the value of labelFont.
     * @since 0.6
     */
    public Font3D getLabelFont3D() {
        return buttonFont3D;
    }

    /**
     * Sets the value of buttonActivatedAppearance. This methods updates the shape if is has been
     * built. May throw a {@code NullPointerException} if {@code value} is <code>null</code>.
     * 
     * @param value
     *            the buttonActivatedAppearance to set, mustn't be <code>null</code>.
     * @see #getButtonActivatedAppearance()
     * @since 0.6
     */
    public void setButtonActivatedAppearance(final Appearance value) {
        assert value != null : "value is null";
        buttonActivatedAppearance = value;
        updateButtonActivated();
    }

    /**
     * Set the value of labelFont. May throw a {@code NullPointerException} if {@code value} is
     * <code>null</code>.
     * 
     * @param value
     *            the labelFont to be set, mustn't be <code>null</code>.
     * @see #getLabelFont3D()
     * @since 0.6
     */
    public void setButtonFont3D(final Font3D value) {
        assert value != null : "value is null";
        buttonFont3D = value;
        updateLabelGeometry();
    }

    /**
     * Sets the value of buttonInternalShape. This methods updates the shape if is has been built.
     * 
     * @param value
     *            the buttonInternalShape to set, can be <code>null</code>.
     * @see #getButtonInternalShape()
     * @since 0.7
     */
    private void setButtonInternalShape(final Shape3D value) {
        buttonInternalShape = value;
        updateButtonActivated();
    }

    /**
     * Sets the value of buttonNotActivatedAppearance. This methods updates the shape if is has been
     * built. May throw a {@code NullPointerException} if {@code value} is <code>null</code>.
     * 
     * @param value
     *            the buttonNotActivatedAppearance to set, mustn't be <code>null</code>.
     * @see #getButtonNotActivatedAppearance()
     * @since 0.6
     * @since 0.7
     */
    public void setButtonNotActivatedAppearance(final Appearance value) {
        assert value != null : "value is null";
        buttonNotActivatedAppearance = value;
        updateButtonActivated();
    }

    /**
     * Sets the value of buttonText3D. This methods updates the shape if is has been built.
     * 
     * @param value
     *            the buttonText3D to set, can be <code>null</code>.
     * @see #getButtonText3D()
     * @since 0.7
     */
    private void setButtonText3D(final Text3D value) {
        buttonText3D = value;
    }

    /**
     * @see net.sourceforge.mazix.components.graphic.labels.buttons.AbstractGraphicButton#toString()
     * @return a string representation of the object.
     * @since 0.4
     */
    @Override
    public String toString() {
        return super.toString();
    }

    /**
     * @see org.AbstractGraphicButton.component3D.GraphicButton#updateButtonActivated()
     * @since 0.6
     */
    @Override
    protected void updateButtonActivated() {
        if (isBuilt()) {
            assert getButtonInternalShape() != null : "getButtonInternalShape() is null";
            assert getButtonActivatedAppearance() != null : "getButtonActivatedAppearance() is null";
            assert getButtonNotActivatedAppearance() != null : "getButtonNotActivatedAppearance() is null";
            getButtonInternalShape().setAppearance(isActivated() ? getButtonActivatedAppearance() : getButtonNotActivatedAppearance());
        }
    }

    /**
     * @see org.AbstractGraphicLabel.component3D.GraphicLabel#updateLabelGeometry()
     * @since 0.6
     */
    @Override
    protected void updateLabelGeometry() {
        if (isBuilt()) {
            assert getButtonInternalShape() != null : "getButtonInternalShape() is null";
            assert getButtonFont3D() != null : "getButtonFont3D() is null";
            assert getLabelText() != null : "getLabelText() is null";
            assert getLabelSeparator() != null : "getLabelSeparator() is null";
            assert getLabelValue() != null : "getLabelValue() is null";
            setButtonText3D(new Text3D(getButtonFont3D(), getLabelText() + getLabelSeparator() + getLabelValue(), new Point3f(0, 0, 0), ALIGN_CENTER, PATH_RIGHT));
            getButtonInternalShape().setGeometry(getButtonText3D());
        }
    }
}
