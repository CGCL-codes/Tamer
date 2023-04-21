package net.cwroethel.swing.JPopupCalendar;

import javax.swing.JButton;
import net.cwroethel.swing.JPopupCalendar.SerializeClone;

/**
 * <p>A publicly cloneable version of JButton used by
 * DateChooserStyle and DateChooser to define the various
 * customizable buttons used in DateChooser.</p>
 * @author Will Roethel, http://www.cwroethel.net
 * @version $Revision: 1.5 $
 * @see DateChooserStyle
 * @see DateChooser
 */
public class DateChooserButton extends JButton implements Cloneable {

    public boolean FORCE_SERIALIZE_COPY = false;

    public DateChooserButton() {
    }

    /**
   * <p>Clone this instance of DateChooserButton</p>
   * @return Object
   */
    public Object clone() {
        String version = System.getProperties().getProperty("java.class.version");
        float versionNo;
        try {
            versionNo = Float.parseFloat(version.trim());
        } catch (NumberFormatException e) {
            System.err.println("Failed to get class version. Assuming pre-1.5");
            versionNo = 0;
        }
        if (versionNo < 49.0 || this.FORCE_SERIALIZE_COPY) {
            try {
                return SerializeClone.clone(this);
            } catch (Exception e) {
                return null;
            }
        } else {
            try {
                DateChooserButton copy = (DateChooserButton) super.clone();
                return copy;
            } catch (CloneNotSupportedException e) {
                throw new Error("Label is not cloneable.");
            }
        }
    }
}
