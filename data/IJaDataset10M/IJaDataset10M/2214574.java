package org.roomwareproject.module.fake;

import org.roomwareproject.server.*;
import org.roomwareproject.utils.*;
import java.util.*;
import java.util.logging.*;
import java.io.*;
import java.beans.*;

public class Module extends AbstractModule {

    protected boolean doLoop = true;

    protected Set<Presence> presences = new HashSet<Presence>();

    public Module(Properties properties) throws RoomWareException {
        super(properties);
        init();
    }

    protected void init() throws RoomWareException {
        String deviceNameList = properties.getProperty("devices");
        String[] deviceAddressNames = deviceNameList.split(",");
        for (String deviceAddressName : deviceAddressNames) {
            deviceAddressName = deviceAddressName.trim();
            if (deviceAddressName.equals("")) continue;
            String type = properties.getProperty(deviceAddressName + "-type", "fake");
            String zone = properties.getProperty(deviceAddressName + "-zone", "fake");
            Device d = new Device(new FakeDeviceAddress(deviceAddressName.trim(), type));
            String name = properties.getProperty(deviceAddressName + "-name");
            if (name != null) d.setFriendlyName(name.trim());
            presences.add(new Presence(this, zone, d, new Date()));
        }
    }

    public void run() {
        for (Presence p : presences) {
            propertyChange(new PropertyChangeEvent(p.getDevice(), "in range", p.getZone(), null));
        }
        while (doLoop) {
            try {
                synchronized (this) {
                    wait();
                }
            } catch (InterruptedException cause) {
                if (doLoop) {
                    logger.warning("we are waked for unknown reason!");
                }
            }
        }
    }

    public synchronized void stop() {
        doLoop = false;
        notifyAll();
    }

    public synchronized Set<Presence> getPresences() {
        return presences;
    }

    public void sendMessage(Device device, Message message) throws RoomWareException {
        throw new RoomWareException("Operation not supported!");
    }
}
