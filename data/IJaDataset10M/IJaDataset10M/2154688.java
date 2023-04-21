package org.octaedr.upnp.tools.devicesniffer;

import org.octaedr.upnp.tools.common.AboutData;
import org.octaedr.upnp.tools.common.AboutPerson;
import org.octaedr.upnp.tools.devicesniffer.engine.DeviceSnifferEngine;
import org.octaedr.upnp.tools.devicesniffer.ui.DeviceSnifferFrame;

/**
 * DeviceSniffer application class.
 */
public class DeviceSniffer {

    /**
     * Main function.
     * 
     * @param args
     *            Execution arguments.
     */
    public static void main(final String[] args) {
        AboutData aboutData = new AboutData("DeviceSniffer", "0.1");
        aboutData.addAuthor(new AboutPerson("Krzysztof Kapuscik", "Author", "k.kapuscik@gmail.com"));
        DeviceSnifferEngine snifferEngine = new DeviceSnifferEngine();
        DeviceSnifferFrame snifferFrame = new DeviceSnifferFrame(snifferEngine, aboutData);
        snifferFrame.setVisible(true);
    }
}
