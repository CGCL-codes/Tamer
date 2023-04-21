package com.pcmsolutions.device.EMU.E4.events.preset.requests;

import java.io.ByteArrayInputStream;

/**
 * User: paulmeehan
 * Date: 03-Sep-2004
 * Time: 17:13:46
 */
public interface PresetDumpResult {

    ByteArrayInputStream getDump();

    boolean isEmpty();
}
