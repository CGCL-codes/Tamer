package net.sf.janos.control;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import net.sbbi.upnp.devices.UPNPRootDevice;
import net.sbbi.upnp.messages.UPNPResponseException;
import net.sf.janos.model.Entry;
import net.sf.janos.model.SeekTargetFactory;
import net.sf.janos.model.TransportInfo.TransportState;
import org.xml.sax.SAXException;

/**
 * Corresponds to a physical Zone Player, and gives access all the devices and
 * services that a Zone Player has.
 * 
 * @author David Wheeler
 * 
 */
public class ZonePlayer {

    private final UPNPRootDevice dev;

    private final MediaServerDevice mediaServer;

    private final MediaRendererDevice mediaRenderer;

    private final AlarmClockService alarm;

    private final AudioInService audioIn;

    private final DevicePropertiesService deviceProperties;

    private final SystemPropertiesService systemProperties;

    private final ZoneGroupTopologyService zoneGroupTopology;

    private final ZoneGroupManagementService zoneGroupManagement;

    private InetAddress ip;

    private final int port;

    /**
   * Creates a new sonos device around the given UPNPRootDevice. This device
   * must be a sonos device
   * 
   * @param dev
   * @throws IllegalArgumentException if dev is not a sonos device.
   */
    protected ZonePlayer(UPNPRootDevice dev) {
        if (!dev.getDeviceType().equals(ZonePlayerConstants.SONOS_DEVICE_TYPE)) {
            throw new IllegalArgumentException("dev must be a sonos device, not " + dev.getDeviceType());
        }
        this.dev = dev;
        try {
            this.ip = InetAddress.getByName(dev.getURLBase().getHost());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        this.port = dev.getURLBase().getPort();
        this.mediaServer = new MediaServerDevice(dev.getChildDevice(ZonePlayerConstants.MEDIA_SERVER_DEVICE_TYPE));
        this.mediaRenderer = new MediaRendererDevice(dev.getChildDevice(ZonePlayerConstants.MEDIA_RENDERER_DEVICE_TYPE));
        this.alarm = new AlarmClockService(dev.getService(ZonePlayerConstants.SONOS_SERVICE_ALARM_CLOCK));
        this.audioIn = new AudioInService(dev.getService(ZonePlayerConstants.SONOS_SERVICE_AUDIO_IN));
        this.deviceProperties = new DevicePropertiesService(dev.getService(ZonePlayerConstants.SONOS_SERVICE_DEVICE_PROPERTIES));
        this.systemProperties = new SystemPropertiesService(dev.getService(ZonePlayerConstants.SONOS_SERVICE_SYSTEM_PROPERTIES));
        this.zoneGroupTopology = new ZoneGroupTopologyService(dev.getService(ZonePlayerConstants.SONOS_SERVICE_ZONE_GROUP_TOPOLOGY));
        this.zoneGroupManagement = new ZoneGroupManagementService(dev.getService(ZonePlayerConstants.SONOS_SERVICE_ZONE_GROUP_MANAGEMENT));
    }

    public void dispose() {
        this.mediaServer.dispose();
        this.mediaRenderer.dispose();
        this.alarm.dispose();
        this.audioIn.dispose();
        this.deviceProperties.dispose();
        this.systemProperties.dispose();
        this.zoneGroupTopology.dispose();
        this.zoneGroupManagement.dispose();
    }

    /**
   * @return the UPNPRootDevice around which this object has been created.
   */
    public UPNPRootDevice getRootDevice() {
        return dev;
    }

    /**
   * @return the DeviceProperties service for this zone player
   */
    public DevicePropertiesService getDevicePropertiesService() {
        return deviceProperties;
    }

    /**
   * @return a SonosMediaServerDevice for our zone player.
   */
    public MediaServerDevice getMediaServerDevice() {
        return mediaServer;
    }

    /**
   * @return a UPNPDevice of type MediaRenderer, from our sonos object.
   */
    public MediaRendererDevice getMediaRendererDevice() {
        return mediaRenderer;
    }

    /**
   * @return the AlarmClockService for this zone player.
   */
    public AlarmClockService getAlarmService() {
        return alarm;
    }

    /**
   * @return the audio in service for this zone player.
   */
    public AudioInService getAudioInService() {
        return audioIn;
    }

    /**
   * @return system properties service for this zone player.
   */
    public SystemPropertiesService getSystemPropertiesService() {
        return systemProperties;
    }

    /**
   * @return the zone group management service for this player.
   */
    public ZoneGroupManagementService getZoneGroupManagementService() {
        return zoneGroupManagement;
    }

    /**
   * @return the zone group topology service for this zone player.
   */
    public ZoneGroupTopologyService getZoneGroupTopologyService() {
        return zoneGroupTopology;
    }

    /**
   * Adds the given entry to the play queue for this zone player.
   * 
   * NOTE: this should only be called if this zone player is the zone group
   * coordinator.
   * 
   * @param entry
   *          the entry to enqueue.
   * @throws UPNPResponseException 
   * @throws IOException 
   */
    public int enqueueEntry(Entry entry) throws IOException, UPNPResponseException {
        AVTransportService serv = getMediaRendererDevice().getAvTransportService();
        int index = serv.addToQueue(entry);
        return index;
    }

    /**
   * Enqueues the given entry, skips to it and ensure the zone is playing.
   * @param entry
   * @throws IOException
   * @throws UPNPResponseException
   * @throws SAXException
   */
    public void enqueueAndPlayEntry(Entry entry) throws IOException, UPNPResponseException, SAXException {
        playQueueEntry(enqueueEntry(entry));
    }

    /**
   * Seeks to the given entry in the queue (0 is the first entry in the queue).
   * 
   * @param index
   * @throws IOException
   * @throws UPNPResponseException
   * @throws SAXException 
   */
    public void playQueueEntry(int index) throws IOException, UPNPResponseException, SAXException {
        AVTransportService serv = getMediaRendererDevice().getAvTransportService();
        if (!serv.getMediaInfo().getCurrentURI().startsWith("x-rincon-queue:")) {
            serv.setAvTransportUriToQueue(getId());
        }
        serv.seek(SeekTargetFactory.createTrackSeekTarget(index));
        if (!serv.getTransportInfo().getState().equals(TransportState.PLAYING)) {
            serv.play();
        }
    }

    /**
   * @return the IP address for this zone player.
   */
    public InetAddress getIP() {
        return ip;
    }

    /**
   * @return the port for HTTP requests to this zone player.
   */
    public int getPort() {
        return port;
    }

    /**
   * Creates a new URL by appending the given string to this zonePlayer's attributes. 
   * @param url the url to append, eg "/images/image1.png"
   * @return the complete url eg "http://192.168.0.1:1400/images/image1.png"
   * @throws MalformedURLException 
   */
    public URL appendUrl(String url) throws MalformedURLException {
        return new URL("http", getIP().getHostAddress(), getPort(), url);
    }

    /**
   * @return A string of characters identifying this sonos to other sonos
   */
    public String getId() {
        return getRootDevice().getUDN().substring(5);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof ZonePlayer) {
            ZonePlayer zp = (ZonePlayer) obj;
            return zp.getRootDevice().getUDN().equals(getRootDevice().getUDN());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getRootDevice().getUDN().hashCode();
    }
}
