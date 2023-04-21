package com.pcmsolutions.device.EMU.E4.selections;

import com.pcmsolutions.device.EMU.E4.DeviceContext;

/**
 * Created by IntelliJ IDEA.
 * User: pmeehan
 * Date: 07-Aug-2003
 * Time: 09:18:31
 * To change this template use Options | File Templates.
 */
public interface E4Selection {

    public static final int MASTER_BASE = 100;

    public static final int PRESET_BASE = 200;

    public static final int VOICE_BASE = 200;

    public static final int LINK_BASE = 200;

    DeviceContext getSrcDevice();
}
