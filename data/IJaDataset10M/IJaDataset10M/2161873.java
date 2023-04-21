package javax.print.attribute.standard;

import java.util.Locale;
import javax.print.attribute.PrintJobAttribute;
import javax.print.attribute.TextSyntax;

/**
 * The <code>OutputDeviceAssigned</code> printing attribute specifies the
 * output printer device assigned by a print service to a print job. 
 * <p>
 * This may be empty if a print service is embedded in a printer, e.g. is the
 * output device. However there exist print services with several physical 
 * output devices (e.g. CUPS classes) where this attribute provides the actual
 * output device.
 * </p>
 * <p>
 * <b>IPP Compatibility:</b> OutputDeviceAssigned is an IPP 1.1 attribute.
 * </p>
 * 
 * @author Michael Koch (konqueror@gmx.de)
 */
public final class OutputDeviceAssigned extends TextSyntax implements PrintJobAttribute {

    private static final long serialVersionUID = 5486733778854271081L;

    /**
   * Creates a <code>OutputDeviceAssigned</code> object.
   *
   * @param deviceName the name of the device.
   * @param locale the locale to use, if <code>null</code> the default
   * locale is used.
   *
   * @exception NullPointerException if deviceName is <code>null</code>.
   */
    public OutputDeviceAssigned(String deviceName, Locale locale) {
        super(deviceName, locale);
    }

    /**
   * Tests if the given object is equal to this object.
   *
   * @param obj the object to test
   *
   * @return <code>true</code> if both objects are equal, 
   * <code>false</code> otherwise.
   */
    public boolean equals(Object obj) {
        if (!(obj instanceof OutputDeviceAssigned)) return false;
        return super.equals(obj);
    }

    /**
   * Returns category of this class.
   *
   * @return The class <code>OutputDeviceAssigned</code> itself.
   */
    public Class getCategory() {
        return OutputDeviceAssigned.class;
    }

    /**
   * Returns the name of this attribute.
   *
   * @return The name "output-device-assigned".
   */
    public String getName() {
        return "output-device-assigned";
    }
}
