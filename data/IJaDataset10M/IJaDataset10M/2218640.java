package org.ms150hams.trackem.network.aprs;

import java.util.Date;
import java.util.Hashtable;
import java.util.logging.Logger;
import org.ms150hams.trackem.model.*;
import org.ms150hams.trackem.network.EventHandler;
import org.ms150hams.trackem.network.LayerThreeProtocol;
import org.ms150hams.trackem.network.LayerTwoProtocol;

public class APRSMicEDecoder implements LayerThreeProtocol {

    private static final Logger logger = Logger.getLogger("org.ms150hams.trackem.network.aprs");

    public static final String IsD700Key = "IsD700";

    private EventHandler eventHandler;

    public void setEventHandler(EventHandler evt) {
        eventHandler = evt;
    }

    public EventHandler getEventHandler() {
        return eventHandler;
    }

    public void receivePacket(byte[] data, Hashtable protoInfo) {
        char srcCall[] = new char[6];
        int srcSSID;
        int dstCall[] = new int[6];
        int dstSSID;
        int pos = 0;
        dstCall[0] = (int) ((data[pos++] & 0xFF) >> 1);
        dstCall[1] = (int) ((data[pos++] & 0xFF) >> 1);
        dstCall[2] = (int) ((data[pos++] & 0xFF) >> 1);
        dstCall[3] = (int) ((data[pos++] & 0xFF) >> 1);
        dstCall[4] = (int) ((data[pos++] & 0xFF) >> 1);
        dstCall[5] = (int) ((data[pos++] & 0xFF) >> 1);
        dstSSID = ((data[pos++] & 0xFF) >> 1) & 0x0F;
        srcCall[0] = (char) ((data[pos++] & 0xFF) >> 1);
        srcCall[1] = (char) ((data[pos++] & 0xFF) >> 1);
        srcCall[2] = (char) ((data[pos++] & 0xFF) >> 1);
        srcCall[3] = (char) ((data[pos++] & 0xFF) >> 1);
        srcCall[4] = (char) ((data[pos++] & 0xFF) >> 1);
        srcCall[5] = (char) ((data[pos++] & 0xFF) >> 1);
        srcSSID = ((data[pos] & 0xFF) >> 1) & 0x0F;
        while ((data[pos++] & 0x1) == 0) {
        }
        ;
        int control = data[pos++] & 0xFF;
        if (control != 0x03) return;
        int pid = data[pos++] & 0xFF;
        if (pid != 0xF0) return;
        int flag = data[pos++] & 0xFF;
        if (flag != 0x60 && flag != 0x27) return;
        int lonDeg = (data[pos++] - 28);
        if (lonDeg >= 180 && lonDeg <= 189) lonDeg -= 80;
        if (lonDeg >= 190 && lonDeg <= 199) lonDeg -= 190;
        int lonMin = (data[pos++] - 28);
        if (lonMin >= 60) lonMin -= 60;
        int lonHun = (data[pos++] - 28);
        int sp = (data[pos++] - 28);
        int dc = (data[pos++] - 28);
        int se = (data[pos++] - 28);
        int symbol = data[pos++] & 0xFF;
        int table = data[pos++] & 0xFF;
        if (false) {
            symbol++;
            table++;
        }
        char d700Flag = 0;
        if (data.length > pos) {
            d700Flag = (char) data[pos++];
        }
        int speed = (sp * 10) + (dc / 10);
        if (speed >= 800) speed -= 800;
        int heading = ((dc % 10) * 100) + se;
        if (heading >= 400) heading -= 400;
        int latDeg = (dstCall[0] & 0xF) * 10 + (dstCall[1] & 0xF);
        int latMin = (dstCall[2] & 0xF) * 10 + (dstCall[3] & 0xF);
        int latHun = (dstCall[4] & 0xF) * 10 + (dstCall[5] & 0xF);
        int latSign = ((dstCall[3] >> 6) & 0x1) == 1 ? 1 : -1;
        int lonSign = ((dstCall[5] >> 6) & 0x1) == 1 ? -1 : 1;
        int lonHundred = (dstCall[4] >> 6) & 0x1;
        double longitude = (((lonMin + (lonHun / 100D)) / 60D) + lonDeg + (lonHundred * 100)) * lonSign;
        double latitude = (((latMin + (latHun / 100D)) / 60D) + latDeg) * latSign;
        logger.finer("APRS MicE Packet " + longitude + " " + latitude + " " + speed + "mph " + heading + "deg");
        Station src = stationFromCallsignSSID(new String(srcCall).trim(), srcSSID);
        Date time = new Date();
        Location loc = new LatLonLocation(longitude, latitude);
        int status = StatusEvent.READY;
        VehicleStatusEvent evt = new VehicleStatusEvent(src, time, loc, status, heading, speed);
        protoInfo.put(LayerThreeProtocol.LayerThreeProtocolKey, this);
        boolean isD700 = flag == '\'' && d700Flag == ']';
        protoInfo.put(IsD700Key, new Boolean(isD700));
        if (eventHandler != null) eventHandler.receiveEvent(evt, protoInfo);
        dstSSID++;
    }

    private Station stationFromCallsignSSID(String call, int ssid) {
        if (call.startsWith("MS")) call = call.substring(2);
        return Station.stationForIdentifier(call + ssid);
    }

    public String formatCallsignSSID(char[] call, int ssid) {
        String str = new String(call).trim();
        if (ssid > 0) str += "-" + ssid;
        return str;
    }
}
