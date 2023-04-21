package net.sf.mmm.ui.toolkit.base.gwt;

import net.sf.mmm.ui.toolkit.api.UiDevice;

/**
 * This is the implementation of {@link UiDevice} for the web (GWT).
 * 
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class UiDeviceImpl implements UiDevice {

    /**
   * The constructor.
   */
    public UiDeviceImpl() {
        super();
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public String toString() {
        return "Browser-Window";
    }
}
