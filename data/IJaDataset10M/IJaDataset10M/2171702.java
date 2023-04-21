package org.placelab.util.ns1;

import java.util.Vector;
import java.util.Date;

/**
 * data object for APs.
 * holds APEncounters.
 */
public class AP {

    private String ssid = "";

    private String mac = "";

    private int max_signal = 0;

    private int max_noise = 0;

    private int max_snr = 0;

    private int last_channel = 0;

    private long channels = 0;

    private int flags = 0;

    private int beacon_interval = 0;

    private Date first_seen;

    private Date last_seen;

    private double best_lat = 0;

    private double best_lon = 0;

    private int best_gnr = 0;

    private String name = "";

    private Vector encounters = new Vector(0);

    /**
   * Get the value of encounters.
   * @return value of encounters.
   */
    public Vector getEncounters() {
        return encounters;
    }

    /**
     * Set the value of encounters.
     * @param v  Value to assign to encounters.
     */
    public void setEncounters(Vector v) {
        this.encounters = v;
    }

    /**
   * @param e the Encounter to add
   */
    public void addEncounter(Encounter e) {
        encounters.add(e);
    }

    /**
   * Get the value of name.
   * @return value of name.
   */
    public String getName() {
        return name;
    }

    /**
   * Set the value of name.
   * @param v  Value to assign to name.
   */
    public void setName(String v) {
        this.name = v;
    }

    /**
   * Get the value of best_gnr.
   * @return value of best_gnr.
   */
    public int getBestGnr() {
        return best_gnr;
    }

    /**
   * Set the value of best_gnr.
   * @param v  Value to assign to best_gnr.
   */
    public void setBestGnr(int v) {
        this.best_gnr = v;
    }

    /**
   * Get the value of best_lon.
   * @return value of best_lon.
   */
    public double getBestLon() {
        return best_lon;
    }

    /**
   * Set the value of best_lon.
   * @param v  Value to assign to best_lon.
   */
    public void setBestLon(double v) {
        this.best_lon = v;
    }

    /**
   * Get the value of best_lat.
   * @return value of best_lat.
   */
    public double getBestLat() {
        return best_lat;
    }

    /**
   * Set the value of best_lat.
   * @param v  Value to assign to best_lat.
   */
    public void setBestLat(double v) {
        this.best_lat = v;
    }

    /**
   * Get the value of last_seen.
   * @return value of last_seen.
   */
    public Date getLastSeen() {
        return last_seen;
    }

    /**
   * Set the value of last_seen.
   * @param v  Value to assign to last_seen.
   */
    public void setLastSeen(Date v) {
        this.last_seen = v;
    }

    /**
   * Get the value of first_seen.
   * @return value of first_seen.
   */
    public Date getFirstSeen() {
        return first_seen;
    }

    /**
   * Set the value of first_seen.
   * @param v  Value to assign to first_seen.
   */
    public void setFirstSeen(Date v) {
        this.first_seen = v;
    }

    /**
   * Get the value of beacon_interval.
   * @return value of beacon_interval.
   */
    public int getBeaconInterval() {
        return beacon_interval;
    }

    /**
   * Set the value of beacon_interval.
   * @param v  Value to assign to beacon_interval.
   */
    public void setBeaconInterval(int v) {
        this.beacon_interval = v;
    }

    /**
   * Get the value of flags.
   * @return value of flags.
   */
    public int getFlags() {
        return flags;
    }

    /**
   * Set the value of flags.
   * @param v  Value to assign to flags.
   */
    public void setFlags(int v) {
        this.flags = v;
    }

    /**
   * Get the value of channels.
   * @return value of channels.
   */
    public long getChannels() {
        return channels;
    }

    /**
   * Set the value of channels.
   * @param v  Value to assign to channels.
   */
    public void setChannels(long v) {
        this.channels = v;
    }

    /**
   * Get the value of last_channel.
   * @return value of last_channel.
   */
    public int getLastChannel() {
        return last_channel;
    }

    /**
   * Set the value of last_channel.
   * @param v  Value to assign to last_channel.
   */
    public void setLastChannel(int v) {
        this.last_channel = v;
    }

    /**
   * Get the value of max_snr.
   * @return value of max_snr.
   */
    public int getMaxSnr() {
        return max_snr;
    }

    /**
   * Set the value of max_snr.
   * @param v  Value to assign to max_snr.
   */
    public void setMaxSnr(int v) {
        this.max_snr = v;
    }

    /**
   * Get the value of max_noise.
   * @return value of max_noise.
   */
    public int getMaxNoise() {
        return max_noise;
    }

    /**
   * Set the value of max_noise.
   * @param v  Value to assign to max_noise.
   */
    public void setMaxNoise(int v) {
        this.max_noise = v;
    }

    /**
   * Get the value of max_signal.
   * @return value of max_signal.
   */
    public int getMaxSignal() {
        return max_signal;
    }

    /**
   * Set the value of max_signal.
   * @param v  Value to assign to max_signal.
   */
    public void setMaxSignal(int v) {
        this.max_signal = v;
    }

    /**
   * Get the value of ssid.
   * @return value of ssid.
   */
    public String getSsid() {
        return ssid;
    }

    /**
   * Set the value of ssid.
   * @param v  Value to assign to ssid.
   */
    public void setSsid(String v) {
        this.ssid = v;
    }

    /**
   * Get the value of mac.
   * @return value of mac.
   */
    public String getMac() {
        return mac;
    }

    /**
   * Set the value of mac.
   * @param v  Value to assign to mac.
   */
    public void setMac(String v) {
        this.mac = v;
    }

    public String toString() {
        return ssid + "," + mac + ", (" + max_signal + "," + max_noise + "," + max_snr + ")" + "[" + best_lat + "," + best_lon + "] " + last_seen + " " + encounters.size() + " observations";
    }
}
