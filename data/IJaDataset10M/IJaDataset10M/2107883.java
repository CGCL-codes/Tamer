package net.sourceforge.btthud.engine;

import net.sourceforge.btthud.engine.commands.HUDGeneralStatic;
import net.sourceforge.btthud.engine.commands.HUDGeneralStatus;
import net.sourceforge.btthud.engine.commands.HUDContacts;
import net.sourceforge.btthud.engine.commands.HUDContactsBuildings;
import net.sourceforge.btthud.engine.commands.HUDTactical;
import net.sourceforge.btthud.engine.commands.HUDArmorStatus;
import net.sourceforge.btthud.engine.commands.HUDWeaponStatus;
import net.sourceforge.btthud.engine.commands.HUDAmmoStatus;
import net.sourceforge.btthud.engine.commands.HUDArmorOriginal;
import net.sourceforge.btthud.engine.commands.HUDConditions;
import java.util.*;
import net.sourceforge.btthud.data.*;

public class MUCommandsTask extends TimerTask {

    MUConnection conn;

    MUData data;

    MUPrefs prefs;

    public boolean forceContacts;

    public boolean forceTactical;

    public boolean forceLOS;

    public boolean forceGeneralStatus;

    public boolean forceArmorStatus;

    int count;

    public MUCommandsTask(MUConnection conn, MUData data, MUPrefs prefs) {
        this.conn = conn;
        this.data = data;
        this.prefs = prefs;
    }

    public void run() {
        try {
            if ((System.currentTimeMillis() - data.lastDataTime) > (2000 * prefs.mediumCommandUpdate) && data.lastDataTime != 0) {
                if (System.currentTimeMillis() - data.lastDataTime > (2000 * prefs.slugCommandUpdate)) {
                    data.lastDataTime = System.currentTimeMillis();
                }
            } else {
                if (data.hudStarted && conn != null && count % (4 * prefs.fastCommandUpdate) == 0) {
                    conn.sendCommand(new HUDGeneralStatic());
                }
                if (data.hudRunning && (forceGeneralStatus || (count % (4 * prefs.fastCommandUpdate) == 0))) {
                    conn.sendCommand(new HUDGeneralStatus());
                    forceGeneralStatus = false;
                }
                if (!data.myUnit.status.matches("S|s") && data.hudRunning && (forceContacts || (count % (4 * prefs.fastCommandUpdate) == 0))) {
                    conn.sendCommand(new HUDContacts());
                    if (data.hiSupportsBuildingContacts()) conn.sendCommand(new HUDContactsBuildings());
                    synchronized (data) {
                        data.expireAllContacts();
                    }
                    forceContacts = false;
                }
                if (data.hudRunning && (forceTactical || (count % (4 * (data.mapLOSOnly ? prefs.mediumCommandUpdate : prefs.slugCommandUpdate)) == 0))) {
                    conn.sendCommand(new HUDTactical(prefs.hudinfoTacHeight));
                    forceTactical = false;
                }
                if (data.hudRunning && (data.lastLOSX != data.myUnit.position.getHexX() || data.lastLOSY != data.myUnit.position.getHexY() || data.lastLOSZ != data.myUnit.position.getHexZ() || forceLOS)) {
                    data.clearLOS();
                    conn.sendCommand(new HUDTactical(prefs.hudinfoTacHeight, true));
                    data.lastLOSX = data.myUnit.position.getHexX();
                    data.lastLOSY = data.myUnit.position.getHexY();
                    data.lastLOSZ = data.myUnit.position.getHexZ();
                    forceLOS = false;
                }
                if (data.hudRunning && (forceArmorStatus || (count % (4 * prefs.mediumCommandUpdate) == 0))) {
                    conn.sendCommand(new HUDArmorStatus());
                    conn.sendCommand(new HUDWeaponStatus());
                    conn.sendCommand(new HUDAmmoStatus());
                    forceArmorStatus = false;
                }
                if (conn != null && data.lastDataTime == 0) {
                    conn.sendCommand(new HUDArmorOriginal());
                }
                if (data.hudRunning && (count % (30 * prefs.mediumCommandUpdate) == 0)) {
                    conn.sendCommand(new HUDConditions());
                }
            }
        } catch (Exception e) {
            System.out.println("Error: MUCommandsTask: " + e);
        }
        count++;
    }
}
