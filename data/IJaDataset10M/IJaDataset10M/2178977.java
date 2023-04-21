package org.nightlabs.editor2d;

public interface EllipseDrawComponent extends ShapeDrawComponent, IConnectable {

    public static final String PROP_START_ANGLE = "startAngle";

    public static final String PROP_END_ANGLE = "endAngle";

    public static final int START_ANGLE_DEFAULT = 0;

    public static final int END_ANGLE_DEFAULT = 360;

    public static final boolean CONNECT_DEFAULT = false;

    /**
	 * returns the startAngle of the EllipseDrawComponent in degrees
	 * @return the startAngle of the EllipseDrawComponent in degrees
	 */
    int getStartAngle();

    /**
   * sets the startAngle of the EllipseDrawComponent in degrees
   * @param value the new startAngle to set
   */
    void setStartAngle(int value);

    /**
   * returns the endAngle of the EllipseDrawComponent in degrees
   * @return the endAngle of the EllipseDrawComponent in degrees
   */
    int getEndAngle();

    /**
   * sets the endAngle of the EllipseDrawComponent in degrees
   * @param value the new endAngle to set
   */
    void setEndAngle(int value);
}
