package com.pcmsolutions.device.EMU.E4.preset;

import java.io.ByteArrayOutputStream;

/**
 *
 * @author  pmeehan
 */
public interface OfflinePreset extends ContextEditablePreset {

    public ByteArrayOutputStream getDump();
}
