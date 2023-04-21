package org.grailrtls.gui.network;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import org.grailrtls.server.*;

public class LandmarkInfo implements Comparable<LandmarkInfo> {

    public final HubInfo hubInfo;

    public final int physicalLayer;

    public final int antenna;

    private final int hashCode;

    public volatile float bytesPerSecond = 0.0f;

    public volatile float samplesPerSecond = 0.0f;

    public volatile long lastSampleTime = 0l;

    public Map<RegionInfo, Point2D> locations = Collections.synchronizedMap(new HashMap<RegionInfo, Point2D>());

    public BufferedImage iconConnected = null;

    public BufferedImage iconDisconnected = null;

    private static MessageDigest digest;

    static {
        try {
            digest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException nsae) {
            System.err.println("Can't load SHA-1 hashing algorithm.  Exiting.");
            System.exit(1);
        }
    }

    public LandmarkInfo(HubInfo hubInfo, int physicalLayer, int antenna) {
        if (hubInfo == null) throw new NullPointerException("Cannot create a LandmarkInfo object with a null HubInfo object.");
        this.hubInfo = hubInfo;
        this.physicalLayer = physicalLayer;
        this.antenna = antenna;
        byte[] sha1Bytes = null;
        synchronized (LandmarkInfo.digest) {
            LandmarkInfo.digest.reset();
            LandmarkInfo.digest.update(this.hubInfo.getAddress().toByteArray());
            LandmarkInfo.digest.update((byte) this.physicalLayer);
            sha1Bytes = LandmarkInfo.digest.digest(new byte[] { (byte) this.antenna });
        }
        if (sha1Bytes == null) {
            System.err.println("Can't compute hash code: " + this.hubInfo.getAddress() + "/" + this.hubInfo.name + "/" + this.physicalLayer + "/" + this.antenna);
            System.exit(1);
        }
        int hashCode = 0;
        for (int i = 0; i < sha1Bytes.length; i++) {
            hashCode += (int) sha1Bytes[i] << (i % 4);
        }
        this.hashCode = hashCode;
        URL connected = this.getClass().getResource("/non-java/images/rxer-on.png");
        URL disconnected = this.getClass().getResource("/non-java/images/rxer-off.png");
        try {
            this.iconConnected = ImageIO.read(connected);
            this.iconDisconnected = ImageIO.read(disconnected);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int hashCode() {
        return this.hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof LandmarkInfo) {
            LandmarkInfo landmarkInfo = (LandmarkInfo) obj;
            return this.hubInfo.equals(landmarkInfo.hubInfo) && this.physicalLayer == landmarkInfo.physicalLayer && this.antenna == landmarkInfo.antenna;
        }
        return super.equals(obj);
    }

    public String toString() {
        return this.hubInfo.toString() + "/" + Landmark.PHY_STRINGS[this.physicalLayer] + "/" + this.antenna;
    }

    public int compareTo(LandmarkInfo o) {
        int diff = 0;
        if (this.hubInfo.name != null && o.hubInfo.name != null) diff = this.hubInfo.name.compareTo(o.hubInfo.name);
        if (diff != 0) return diff;
        diff = this.hubInfo.getAddress().compareTo(o.hubInfo.getAddress());
        if (diff != 0) return diff;
        diff = this.physicalLayer - o.physicalLayer;
        if (diff != 0) return diff;
        diff = this.antenna - o.antenna;
        if (diff != 0) return diff;
        return 0;
    }
}
