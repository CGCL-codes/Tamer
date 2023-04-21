package be.lassi.ui.control;

import java.util.List;
import be.lassi.control.midi.MidiSystemDevices;

/**
 * Asks the user to select a midi input device from the list of 
 * midi input devices that are available on this computer.
 */
public class InputDeviceSelectionDialog extends DeviceSelectionDialog {

    /**
     * Constructs a new dialog.
     */
    public InputDeviceSelectionDialog() {
        this(new MidiSystemDevices().getInputNames());
    }

    /**
     * Constructs a new dialog.
     *
     * @param names the names of the midi input devices on this computer
     */
    public InputDeviceSelectionDialog(final List<String> names) {
        super("Midi input devices", names);
    }

    /**
     * {@inheritDoc}
     */
    protected String getLabel() {
        return "Select input device name:";
    }

    public static void main(String[] args) {
        new InputDeviceSelectionDialog().select();
    }
}
