package org.dcopolis.platform.tcp;

import org.dcopolis.platform.*;
import java.net.*;
import java.io.*;
import java.util.*;

@SuppressWarnings("unchecked")
class TCPBroadcaster {

    TCPPlatform platform;

    BeaconSender sender;

    BeaconListener listener;

    Hashtable<TCPPlatformIdentifier, TCPPlatformIdentifier> knownPlatforms;

    Hashtable<TCPPlatformIdentifier, Long> lastBeaconTime;

    /**
     * Number of ms between update beacons.
     */
    public static int BEACON_INTERVAL = 1000;

    public static int BEACON_PORT = 15191;

    public static int MAX_BEACON_SIZE = 4096;

    public static int PLATFORM_TIMEOUT = BEACON_INTERVAL * 4;

    public TCPBroadcaster(TCPPlatform platform) {
        this.platform = platform;
        knownPlatforms = new Hashtable<TCPPlatformIdentifier, TCPPlatformIdentifier>();
        knownPlatforms.put(platform.getIdentifier(), platform.getIdentifier());
        lastBeaconTime = new Hashtable<TCPPlatformIdentifier, Long>();
        sender = new BeaconSender();
        listener = new BeaconListener();
    }

    public void stop() {
        sender.stop();
        listener.stop();
    }

    class BeaconSender implements Runnable {

        Thread thread;

        public BeaconSender() {
            thread = new Thread(this);
            thread.setName("BeaconSender");
            thread.start();
        }

        public void stop() {
            thread = null;
        }

        public void run() {
            DatagramSocket socket = null;
            while (thread != null) {
                try {
                    platform.getIdentifier().updateUpdateTime();
                    if (socket == null) socket = new DatagramSocket();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(baos);
                    oos.writeObject(knownPlatforms);
                    oos.flush();
                    byte buf[] = baos.toByteArray();
                    if (buf.length > MAX_BEACON_SIZE) {
                        System.err.println("ERROR: beacon size is too big (there are likely too many known hosts)!");
                    } else {
                        DatagramPacket packet = new DatagramPacket(buf, buf.length, platform.getBroadcastAddress(), BEACON_PORT);
                        socket.send(packet);
                        oos.close();
                        baos.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(BEACON_INTERVAL);
                } catch (Exception e) {
                }
            }
            if (socket != null) socket.close();
        }
    }

    public HashSet<TCPPlatformIdentifier> getKnownPlatforms() {
        return new HashSet<TCPPlatformIdentifier>(knownPlatforms.values());
    }

    class BeaconListener implements Runnable {

        Thread thread;

        public BeaconListener() {
            thread = new Thread(this);
            thread.setName("BeaconListener");
            thread.start();
        }

        public void stop() {
            thread = null;
        }

        public void run() {
            DatagramSocket socket = null;
            byte buf[] = new byte[MAX_BEACON_SIZE];
            while (thread != null) {
                try {
                    if (socket == null) {
                        try {
                            socket = new DatagramSocket(null);
                            socket.setReuseAddress(true);
                            socket.bind(new InetSocketAddress(BEACON_PORT));
                        } catch (Exception e) {
                            socket = null;
                            Thread.sleep(500);
                            throw e;
                        }
                    }
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    socket.receive(packet);
                    ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(packet.getData()));
                    Hashtable<TCPPlatformIdentifier, TCPPlatformIdentifier> newHosts = (Hashtable<TCPPlatformIdentifier, TCPPlatformIdentifier>) ois.readObject();
                    long currentTime = System.currentTimeMillis();
                    for (TCPPlatformIdentifier thi : newHosts.values()) {
                        if (thi.equals(platform.getIdentifier())) continue;
                        TCPPlatformIdentifier existingHost = knownPlatforms.get(thi);
                        if (existingHost != null) {
                            if (existingHost.getUpdateTime() < thi.getUpdateTime()) {
                                existingHost.updateTime = thi.getUpdateTime();
                                lastBeaconTime.put(thi, new Long(currentTime));
                                if (!existingHost.getStatus().equals(thi.getStatus())) {
                                    existingHost.setStatus(thi.getStatus());
                                    knownPlatforms.put(thi, thi);
                                    platform.handlePlatformStatusUpdate(thi);
                                }
                            }
                        } else {
                            knownPlatforms.put(thi, thi);
                            platform.handleNewPlatformDiscovery(thi);
                            lastBeaconTime.put(thi, new Long(currentTime));
                        }
                    }
                    HashSet<TCPPlatformIdentifier> remove = new HashSet<TCPPlatformIdentifier>();
                    for (TCPPlatformIdentifier tpi : lastBeaconTime.keySet()) {
                        long lastTime = lastBeaconTime.get(tpi).longValue();
                        if (currentTime - lastTime > PLATFORM_TIMEOUT) remove.add(tpi);
                    }
                    for (TCPPlatformIdentifier tpi : remove) {
                        lastBeaconTime.remove(tpi);
                        knownPlatforms.remove(tpi);
                        platform.handlePlatformShutdown(tpi);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (socket != null) socket.close();
        }
    }
}
