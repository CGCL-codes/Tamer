package net.sf.magicmap.client.plugin.udpscanner;

import java.util.Date;
import net.sf.magicmap.client.measurement.interfaces.AbstractScanResult;

public class WifiScanResult extends AbstractScanResult {

    double signalLevel;

    double noise;

    String ssid;

    /**
   * @param identifier
   *            Identifier of the Node
   * @param signalLevel
   *            Distance between the Client and this Node
   * @param timeStamp
   *            Time when the WifiScanResult was created
   * @param noise
   *            Noise value from the Wlan-card
   * @param ssid
   *            Identifier of the wireless network the client is assosiated to
   */
    public WifiScanResult(String identifier, double signalLevel, Date timeStamp, double noise, String ssid) {
        super(identifier, timeStamp);
        this.signalLevel = signalLevel;
        this.noise = noise;
        this.ssid = ssid;
    }
}
