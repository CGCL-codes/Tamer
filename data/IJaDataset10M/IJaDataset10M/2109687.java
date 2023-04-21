package fr.esrf.tangoatk.core;

import fr.esrf.TangoApi.*;
import fr.esrf.Tango.DevFailed;

/**
 * <code>IAttribute</code> is the top interface for all attributes. 
 * It defines the standard behaviour for the attributes. All attributes
 * at this level are images, eg their data is represented as an array in
 * two dimensions.
 * @author <a href="mailto:assum@esrf.fr">Erik Assum</a>
 * @version $Revision: 15107 $
 */
public interface IAttribute extends IEntity {

    public static final String OK = "OK";

    public static final String UNKNOWN = "UNKNOWN";

    public static final String VALID = "VALID";

    public static final String INVALID = "INVALID";

    public static final String ALARM = "ALARM";

    public static final String WARNING = "WARNING";

    public static final String CHANGING = "CHANGING";

    /**
     * <code>getFormat</code> returns the human readable representation
     * of the format of this attribute. To obtain the numeric code of the
     * format, please consult getPropertyMap("format");
     * @return a <code>String</code> value
     */
    public String getFormat();

    /**
     * <code>getUnit</code> returns the unit of this attribute.
     *
     * @return a <code>String</code> value
     */
    public String getUnit();

    /**
     * <code>getDisplayUnit</code> returns the display unit of this attribute
     *
     * @return a <code>String</code> value
     */
    public String getDisplayUnit();

    /**
     * <code>getDisplayUnitFactor</code> returns the DisplayUnit Multiplication factor of this attribute.
     * The display unit factor is the value which must be multiplied with the 
     * value of the attribute (returned by the device server) to obtain the value
     * which should be displayed (the value converted into display unit).
     * All the Number Change Events inside ATK and the Viewers use the value converted into
     * the display unit.
     * @return a <code>double</code> value
     *   returns 1.0 if the attribute property "display_unit" is not defined or has an invalid value
     *   returns the value defined by the attribute property "display_unit".
     */
    public double getDisplayUnitFactor();

    /**
     * <code>getStandardUnit</code> returns the standard unit of this attribute
     *
     * @return a <code>String</code> value
     */
    public String getStandardUnit();

    /**
     * <code>getStandardUnitFactor</code> returns the standard unit of this attribute.
     * The standard unit is the value which must be multiplied with the 
     * value of the attribute to obtain the value in a unit conforming to
     * the standard metric system.
     * @return a <code>double</code> value
     */
    public double getStandardUnitFactor();

    /**
     * <code>getLabel</code> returns the label of this attribute.
     *
     * @return a <code>String</code> value
     */
    public String getLabel();

    /**
     * <code>setLabel</code> sets the label of this attribute
     *
     * @param label a <code>String</code> value
     */
    public void setLabel(String label);

    /**
     * <code>getState</code> returns a human-readable representation of 
     * the state.
     * @return a <code>String</code> value
     */
    public String getState();

    /**
     * <code>getType</code> returns a human-readable representaion of the
     * type of this attribute.
     * @return a <code>String</code> value
     */
    public String getType();

    /**
     * <code>getDescription</code> returns the description of this attribute.
     *
     * @return a <code>String</code> value
     */
    public String getDescription();

    /**
     * <code>setDescription</code> sets the description of this attribute
     *
     * @param desc a <code>String</code> value
     */
    public void setDescription(String desc);

    /**
     * <code>setName</code> sets the name of this attribute.
     *
     * @param s a <code>String</code> value
     */
    public void setName(String s);

    /**
     * <code>isWritable</code> returns true if this attribute is writable
     *
     * @return a <code>boolean</code> value
     */
    public boolean isWritable();

    /**
     * <code>addStateListener</code> adds a listener to state-changes
     * for this attribute.
     * @param l an <code>IAttributeStateListener</code> value
     */
    public void addStateListener(IAttributeStateListener l);

    /**
     * <code>removeStateListener</code> removes a listener to state-changes
     * for this attribute.
     * @param l an <code>IAttributeStateListener</code> value
     */
    public void removeStateListener(IAttributeStateListener l);

    /**
     * An <code>ISetErrorListener</code> is an object that listens to 
     * <tt>setting error</tt> property changes from this sttribute.
     * @param l an <code>ISetErrorListener</code> value
     */
    public void addSetErrorListener(ISetErrorListener l);

    /**
     * Removes a setErrorListener.
     * An <code>ISetErrorListener</code> is an object that listens to 
     * <tt>setting error</tt> property changes from this attribute.
     * @param l an <code>ISetErrorListener</code> value
     */
    public void removeSetErrorListener(ISetErrorListener l);

    /**
     * <code>setProperty</code>
     *
     * @param name a <code>String</code> value containing the name of the
     * property
     * @param n a <code>Number</code> value containing the numeric value of
     * the property
     */
    void setProperty(String name, Number n);

    /**
     * <code>setProperty</code>
     *
     * @param name a <code>String</code> value containing the name of the
     * property
     * @param n a <code>Number</code> value containing the value of the
     * property
     * @param editable a <code>boolean</code> value which decides if the
     * property is editable or not.
     */
    void setProperty(String name, Number n, boolean editable);

    /**
     * <code>getMaxXDimension</code> returns the max x-dimension of the
     * attribute
     * @return an <code>int</code> value
     */
    public int getMaxXDimension();

    /**
     * <code>getMaxYDimension</code> returns the max y-dimension of the
     * attribute
     * @return an <code>int</code> value
     */
    public int getMaxYDimension();

    /**
     * <code>getXDimension</code> returns the actual x-dimension of the
     * attribute.
     * @return an <code>int</code> value
     */
    public int getXDimension();

    /**
     * <code>getYDimension</code> returns the actual y-dimension of the
     * attribute
     * @return an <code>int</code> value
     */
    public int getYDimension();

    /**
     * <code>getHeight</code> alias for getYDimension()
     *
     * @return an <code>int</code> value
     */
    public int getHeight();

    /**
     * <code>getWidth</code> alias for getXDimension()
     *
     * @return an <code>int</code> value
     */
    public int getWidth();

    /**
     * <code>hasEvents</code> returns true if the attribute is refreshed by event system
     *
     * @return true if the attribute is refreshed thanks to event system
     *         false if it is refreshed by the ATK refresher thread
     */
    public boolean hasEvents();

    /**
     * Setting this property to true means that the attribute should
     * not read nor distribute new values when its refresh is called
     */
    public void setSkippingRefresh(boolean b);

    public boolean isSkippingRefresh();

    /**
     * <code>areAttPropertiesLoaded</code> returns true if the some ATK specific attribute properties have already
     * been loaded from the Tango DB. One example for these ATK specific attribute properties is : OpenCloseInverted for a DevStateScalar
     * attribute
     *
     * @return true if all the attribute properties have been read from the Tango DB and initialized
     *         false the ATK specific attribute properties have not been initialized yet
     */
    public boolean areAttPropertiesLoaded();

    /**
     * Method used by some attribute viewers to force the reading of some specific at setModel(). The viewer first
     * tests if the ATK specific attribute properties have not been initialized yet (call to areAttPropertiesLoaded())
     * 
     */
    public void loadAttProperties();
}
