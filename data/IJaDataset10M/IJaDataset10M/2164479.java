package com.guzzservices.business;

import java.io.Serializable;

/**
 * 
 * IP范围断对应的地理位置
 * 
 * @author liu kaixuan(liukaixuan@gmail.com)
 * @date Jul 8, 2008 10:28:34 AM
 * @hibernate.class table="commonIPLocationCity"
 */
public class IPLocationCity implements Serializable, Comparable<IPLocationCity> {

    private int id;

    private String startIP;

    private String endIP;

    private long startIPSeq;

    private long endIPSeq;

    private long ipRange;

    private String cityName;

    private String detailLocation;

    private String cityMarker;

    private String provider;

    private String areaName;

    public int compareTo(IPLocationCity otherCity) {
        if (otherCity == null) return 1;
        if (this == otherCity) return 0;
        if (this.startIPSeq == otherCity.startIPSeq) {
            if (this.ipRange > otherCity.ipRange) {
                return -1;
            } else if (this.ipRange == otherCity.ipRange) {
                return 0;
            } else {
                return 1;
            }
        } else {
            if (this.startIPSeq > otherCity.startIPSeq) {
                return 1;
            } else if (this.startIPSeq == otherCity.startIPSeq) {
                return 0;
            } else {
                return -1;
            }
        }
    }

    public boolean equals(Object obj) {
        if (obj instanceof IPLocationCity) {
            return compareTo((IPLocationCity) obj) == 0;
        }
        return false;
    }

    /**
	 * @hibernate.id generator-class="native"
	 */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
	 * @hibernate.property
	 */
    public String getStartIP() {
        return startIP;
    }

    public void setStartIP(String startIP) {
        this.startIP = startIP;
    }

    /**
	 * @hibernate.property
	 */
    public String getEndIP() {
        return endIP;
    }

    public void setEndIP(String endIP) {
        this.endIP = endIP;
    }

    /**
	 * @hibernate.property
	 */
    public long getStartIPSeq() {
        return startIPSeq;
    }

    public void setStartIPSeq(long startIPSeq) {
        this.startIPSeq = startIPSeq;
    }

    /**
	 * @hibernate.property
	 */
    public long getEndIPSeq() {
        return endIPSeq;
    }

    public void setEndIPSeq(long endIPSeq) {
        this.endIPSeq = endIPSeq;
    }

    /**
	 * @hibernate.property
	 */
    public long getIpRange() {
        return ipRange;
    }

    public void setIpRange(long ipRange) {
        this.ipRange = ipRange;
    }

    /**
	 * @hibernate.property
	 */
    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    /**
	 * @hibernate.property
	 */
    public String getDetailLocation() {
        return detailLocation;
    }

    public void setDetailLocation(String detailLocation) {
        this.detailLocation = detailLocation;
    }

    /**
	 * @hibernate.property
	 */
    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getCityMarker() {
        return cityMarker;
    }

    public void setCityMarker(String cityMarker) {
        this.cityMarker = cityMarker;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }
}
