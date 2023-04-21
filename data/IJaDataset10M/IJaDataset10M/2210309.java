package org.nist.usarui.handlers;

import org.nist.usarui.*;
import org.nist.usarui.ui.IridiumUI;
import java.util.*;

/**
 * Suppresses STA messages and displays an info bar with the contents instead.
 *
 * @author Stephen Carlson (NIST)
 */
public class RobotStatusHandler extends AbstractStatusHandler {

    /**
	 * Creates a new instance.
	 *
	 * @param ui the application managing this handler
	 */
    public RobotStatusHandler(IridiumUI ui) {
        super(ui);
    }

    public String getPrefix() {
        return "Sta_";
    }

    public boolean statusReceived(USARPacket packet) {
        String type, batt, key, deg;
        float value;
        boolean keep = true;
        if (packet.getType().equals("STA")) {
            type = packet.getParam("Type");
            batt = packet.getParam("Battery");
            if (batt != null) try {
                ui.updateBattery(Integer.parseInt(batt));
            } catch (NumberFormatException ignore) {
            }
            if (type != null && type.equals("LeggedVehicle")) {
                final USARPacket newPacket = new USARPacket(packet);
                final Map<String, String> params = newPacket.getParams();
                params.remove("Time");
                params.remove("Type");
                params.remove("Battery");
                params.remove("LightIntensity");
                params.remove("LightToggle");
                ui.updateJoints(newPacket);
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    key = entry.getKey();
                    try {
                        value = Float.parseFloat(entry.getValue());
                        if (ui.isInDegrees()) {
                            value = (float) Math.toDegrees(value);
                            deg = DEG_SIGN;
                        } else deg = "";
                        setInformation("Joints", key, String.format("%.2f%s", value, deg));
                    } catch (RuntimeException ignore) {
                    }
                }
            }
            keep = type == null && packet.getParam("Name") != null;
        }
        return keep;
    }
}
