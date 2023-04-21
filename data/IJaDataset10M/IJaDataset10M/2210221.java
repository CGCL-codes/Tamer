package com.guzzservices.manager.impl.ip;

import java.io.Serializable;
import org.guzz.util.Assert;
import org.guzz.util.StringUtil;
import com.guzzservices.business.IPLocationCity;

/**
 * 
 * IP范围断对应的地理位置
 * 
 * @author liu kaixuan(liukaixuan@gmail.com)
 * @date Apr 10, 2009 10:28:34 AM
 */
public class CityMark implements Serializable, Comparable<CityMark> {

    private long startIPSeq;

    private long endIPSeq;

    private long ipRange;

    private String cityName;

    private String detailLocation;

    private String cityMarker;

    private static long s_seg2 = 1000;

    private static long s_seg3 = s_seg2 * 1000;

    private static long s_seg4 = s_seg3 * 1000;

    protected static long stringIP2Number(String ipAddress) {
        if (ipAddress == null) return 0L;
        long seq = 0;
        String[] segs = ipAddress.split("\\.");
        Assert.assertEquals(segs.length, 4, "IP V4 only. passed IP is:" + ipAddress);
        seq = StringUtil.toInt(segs[3], 0) + StringUtil.toInt(segs[2], 0) * s_seg2 + StringUtil.toInt(segs[1], 0) * s_seg3 + StringUtil.toInt(segs[0], 0) * s_seg4;
        return seq;
    }

    public CityMark() {
    }

    public CityMark(String beginIP, String endIP, String cityName, String detailLocation, String cityMarker) {
        this.startIPSeq = stringIP2Number(beginIP);
        if (StringUtil.isEmpty(endIP)) {
            this.endIPSeq = this.startIPSeq;
        } else {
            this.endIPSeq = stringIP2Number(endIP);
        }
        if (startIPSeq > endIPSeq) {
            long a = this.startIPSeq;
            this.startIPSeq = this.endIPSeq;
            this.endIPSeq = a;
        }
        this.ipRange = endIPSeq - startIPSeq;
        this.cityName = cityName;
        this.detailLocation = detailLocation;
        this.cityMarker = cityMarker;
    }

    public CityMark(IPLocationCity city) {
        this.startIPSeq = city.getStartIPSeq();
        this.endIPSeq = city.getEndIPSeq();
        if (startIPSeq > endIPSeq) {
            long a = this.startIPSeq;
            this.startIPSeq = this.endIPSeq;
            this.endIPSeq = a;
        }
        this.ipRange = city.getIpRange();
        this.cityName = city.getCityName();
        this.detailLocation = city.getDetailLocation();
        this.cityMarker = city.getCityMarker();
    }

    public int compareTo(CityMark otherCity) {
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
        if (obj instanceof CityMark) {
            return compareTo((CityMark) obj) == 0;
        }
        return false;
    }

    public long getStartIPSeq() {
        return startIPSeq;
    }

    public void setStartIPSeq(long startIPSeq) {
        this.startIPSeq = startIPSeq;
    }

    public long getEndIPSeq() {
        return endIPSeq;
    }

    public void setEndIPSeq(long endIPSeq) {
        this.endIPSeq = endIPSeq;
    }

    public long getIpRange() {
        return ipRange;
    }

    public void setIpRange(long ipRange) {
        this.ipRange = ipRange;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDetailLocation() {
        return detailLocation;
    }

    public void setDetailLocation(String detailLocation) {
        this.detailLocation = detailLocation;
    }

    public String getCityMarker() {
        return cityMarker;
    }

    public void setCityMarker(String cityMarker) {
        this.cityMarker = cityMarker;
    }
}
