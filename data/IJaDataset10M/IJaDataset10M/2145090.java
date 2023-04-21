package it.ame.permflow.IO.file;

import it.ame.permflow.packets.PacketConstants;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import it.ame.permflow.util.*;

public class Save {

    private DataOutputStream dosHum;

    private DataOutputStream dosPerm;

    private boolean humReady = false, permReady = false;

    public Save() {
        dosHum = null;
        dosPerm = null;
    }

    public void init(String sessionName, int type) {
        try {
            if (PacketConstants.HUM_SENSOR == type || PacketConstants.BOTH_SENSOR == type) {
                dosHum = new DataOutputStream(new FileOutputStream(new File(sessionName + ".hum"), true));
                humReady = true;
            }
            if (PacketConstants.PERM_SENSOR == type || PacketConstants.BOTH_SENSOR == type) {
                dosPerm = new DataOutputStream(new FileOutputStream(new File(sessionName + ".perm"), true));
                permReady = true;
            }
        } catch (Exception e) {
            Logger.reportException(e);
            e.printStackTrace();
        }
    }

    public synchronized void close(int type) {
        try {
            if (type == PacketConstants.HUM_SENSOR || type == PacketConstants.BOTH_SENSOR) {
                humReady = false;
                dosHum.close();
            }
            if (type == PacketConstants.PERM_SENSOR || type == PacketConstants.BOTH_SENSOR) {
                permReady = false;
                dosPerm.close();
            }
        } catch (Exception e) {
            Logger.reportException(e);
            e.printStackTrace();
        }
    }

    public synchronized void write(String value, int type) {
        try {
            if (humReady && type == PacketConstants.HUM_SENSOR) dosHum.writeBytes(value + "\r\n");
            if (permReady && type == PacketConstants.PERM_SENSOR) dosPerm.writeBytes(value + "\r\n");
        } catch (IOException e) {
            e.printStackTrace();
            Logger.reportException(e);
        }
    }
}
