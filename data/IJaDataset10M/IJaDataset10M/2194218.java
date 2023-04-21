package net.sourceforge.crhtetris.misc.settings;

/**
 * A setting that can either be <code>true</code> or <code>false</code>. Means that this value can only contain
 * <code>yes</code> or <code>no</code>.
 * 
 * @author croesch
 * @since Date: Aug 29, 2011
 */
public class BooleanSetting extends Setting {

    /**
   * A setting that can either be <code>true</code> or <code>false</code>. Means that this value can only contain
   * <code>yes</code> or <code>no</code>.
   * 
   * @since Date: Aug 29, 2011
   * @param defValue the default value of this setting.
   */
    public BooleanSetting(final String defValue) {
        super(defValue);
        if (!"yes".equals(defValue) && !"no".equals(defValue)) {
            throw new IllegalArgumentException();
        }
    }
}
