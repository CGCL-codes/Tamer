package se.kth.speech.skatta.player.responsedevice;

import se.kth.speech.skatta.util.ExtendedElement;

/**
 * An instuction which is a label on a JPanel with a certain text.
 */
public class Instruction extends ResponseDevice {

    private static final long serialVersionUID = 7L;

    /**
     * Returns null. Called by: ResponsDeviceList through
     * ResponsDeviceRow
     *
     * @return a String
     */
    public String getValue() {
        return null;
    }

    /**
     * Does nothing.
     */
    public void setValue(String val) {
    }

    /**
     * @param label The instruction text.
     */
    public Instruction(ExtendedElement source) {
        super(null);
        add(source.labelAttribute("name"));
    }

    public boolean isSet() {
        return true;
    }
}
