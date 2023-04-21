package de.wadndadn.midiclipse.ui.preference.fieldeditor;

import javax.sound.midi.MidiDevice.Info;
import org.eclipse.swt.widgets.Composite;

/**
 * TODO Document.
 * 
 * @author SchubertCh
 */
public final class DeviceDescriptionLabelFieldEditor extends AbstractDeviceLabelFieldEditor {

    /**
     * Constructor.
     * 
     * @param labelText
     *            TODO Document
     * @param deviceInfo
     *            TODO Document
     * @param parent
     *            TODO Document
     */
    public DeviceDescriptionLabelFieldEditor(final String labelText, final Info deviceInfo, final Composite parent) {
        super(labelText, deviceInfo, parent);
    }

    /**
     * {@inheritDoc}
     * 
     * @see de.wadndadn.midiclipse.ui.preference.fieldeditor.AbstractDeviceLabelFieldEditor#getValue(Info)
     */
    @Override
    protected String getValue(final Info deviceInfo) {
        return deviceInfo.getDescription();
    }
}
