package net.sipvip.server.domains;

public class GeoInfo {

    private String Status;

    private String CountryCode;

    private String CountryName;

    private String RegionCode;

    private String RegionName;

    private String City;

    private String ZipPostalCode;

    private String Latitude;

    private String Longitude;

    private String Gmtoffset;

    private String Dstoffset;

    private String TimezoneName;

    private String Isdst;

    private String Ip;

    public GeoInfo(String status, String countryCode, String countryName, String regionCode, String regionName, String city, String zipPostalCode, String latitude, String longitude, String gmtoffset, String dstoffset, String timezoneName, String isdst, String ip) {
        this.Status = status;
        this.CountryCode = countryCode;
        this.CountryName = countryName;
        this.RegionCode = regionCode;
        this.RegionName = regionName;
        this.City = city;
        this.ZipPostalCode = zipPostalCode;
        this.Latitude = latitude;
        this.Longitude = longitude;
        this.Gmtoffset = gmtoffset;
        this.Dstoffset = dstoffset;
        this.TimezoneName = timezoneName;
        this.Isdst = isdst;
        this.Ip = ip;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getCountryCode() {
        return CountryCode;
    }

    public void setCountryCode(String countryCode) {
        CountryCode = countryCode;
    }

    public String getCountryName() {
        return CountryName;
    }

    public void setCountryName(String countryName) {
        CountryName = countryName;
    }

    public String getRegionCode() {
        return RegionCode;
    }

    public void setRegionCode(String regionCode) {
        RegionCode = regionCode;
    }

    public String getRegionName() {
        return RegionName;
    }

    public void setRegionName(String regionName) {
        RegionName = regionName;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getZipPostalCode() {
        return ZipPostalCode;
    }

    public void setZipPostalCode(String zipPostalCode) {
        ZipPostalCode = zipPostalCode;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getGmtoffset() {
        return Gmtoffset;
    }

    public void setGmtoffset(String gmtoffset) {
        Gmtoffset = gmtoffset;
    }

    public String getDstoffset() {
        return Dstoffset;
    }

    public void setDstoffset(String dstoffset) {
        Dstoffset = dstoffset;
    }

    public String getTimezoneName() {
        return TimezoneName;
    }

    public void setTimezoneName(String timezoneName) {
        TimezoneName = timezoneName;
    }

    public String getIsdst() {
        return Isdst;
    }

    public void setIsdst(String isdst) {
        Isdst = isdst;
    }

    public String getIp() {
        return Ip;
    }

    public void setIp(String ip) {
        Ip = ip;
    }
}
