package org.javasock.jssniff.handlerpanel;

import javax.swing.JPanel;
import org.javasock.jssniff.handlerdata.ARPData;
import org.javasock.jssniff.handlerdata.DeviceData;
import org.javasock.jssniff.handlerdata.HandlerData;
import org.javasock.jssniff.handlerdata.ICMPData;
import org.javasock.jssniff.handlerdata.IPData;
import org.javasock.jssniff.handlerdata.TCPData;
import org.javasock.jssniff.handlerdata.UDPData;

public abstract class HandlerPanel extends JPanel {

    public abstract boolean okAction();

    public static HandlerPanel createHandlerPanel(HandlerData handlerDataI) {
        if (handlerDataI instanceof DeviceData) {
            return new DevicePanel((DeviceData) handlerDataI);
        } else if (handlerDataI instanceof ARPData) {
            return new ARPPanel((ARPData) handlerDataI);
        } else if (handlerDataI instanceof ICMPData) {
            return new ICMPPanel((ICMPData) handlerDataI);
        } else if (handlerDataI instanceof IPData) {
            return new IPPanel((IPData) handlerDataI);
        } else if (handlerDataI instanceof TCPData) {
            return new TCPPanel((TCPData) handlerDataI);
        } else if (handlerDataI instanceof UDPData) {
            return new UDPPanel((UDPData) handlerDataI);
        }
        return null;
    }
}
