package jp.locky.research.stumbler;

import java.util.ArrayList;

public class WiFiCollectedData {

    private String bssid;

    private String essid;

    private ArrayList<Integer> rssiList;

    private double locX;

    private double locY;

    private String imgName;

    private ArrayList<Long> timeList;

    public WiFiCollectedData() {
        this.rssiList = new ArrayList<Integer>();
        this.timeList = new ArrayList<Long>();
    }

    public String getBssid() {
        return bssid;
    }

    public void setBssid(String bssid) {
        this.bssid = bssid;
    }

    public String getEssid() {
        return essid;
    }

    public void setEssid(String essid) {
        this.essid = essid;
    }

    public int getRssi(int i) {
        return this.rssiList.get(i);
    }

    public int getRssiListSize() {
        return this.rssiList.size();
    }

    public int getRssiAverage() {
        int sum = 0;
        for (int i = 0; i < this.getRssiListSize(); i++) {
            sum += this.getRssi(i);
        }
        return (int) ((double) sum / (double) this.getRssiListSize());
    }

    public int getRssiVariance() {
        int sum = 0;
        for (int i = 0; i < this.getRssiListSize(); i++) {
            sum += Math.pow(this.getRssi(i), 2);
        }
        return (int) (((double) sum / (double) this.getRssiListSize()) - Math.pow((double) this.getRssiAverage(), 2));
    }

    public void addRssi(int rssi) {
        this.rssiList.add(rssi);
    }

    public double getLocX() {
        return locX;
    }

    public void setLocX(double locX) {
        this.locX = locX;
    }

    public double getLocY() {
        return locY;
    }

    public void setLocY(double locY) {
        this.locY = locY;
    }

    public void setLoc(double locX, double locY) {
        this.setLocX(locX);
        this.setLocY(locY);
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public long getTime(int i) {
        return this.timeList.get(i);
    }

    public int getTimeListSize() {
        return this.timeList.size();
    }

    public void addTime(long time) {
        this.timeList.add(time);
    }
}
