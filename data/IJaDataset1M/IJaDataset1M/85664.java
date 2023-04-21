package com.zpyr.mvc.vo;

public class Carc_user_info {

    private String user_info_seq;

    private String name;

    private String phone1;

    private String phone2;

    private String phone3;

    private String open_time;

    private String close_time;

    private String lat;

    private String lng;

    private String addr_sido;

    private String addr_gugun;

    private String addr_dong;

    private String addr;

    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUser_info_seq() {
        return user_info_seq;
    }

    public void setUser_info_seq(String user_info_seq) {
        this.user_info_seq = user_info_seq;
    }

    public String getInfo_seq() {
        return getUser_info_seq();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getPhone3() {
        return phone3;
    }

    public void setPhone3(String phone3) {
        this.phone3 = phone3;
    }

    public String getOpen_time() {
        return open_time;
    }

    public void setOpen_time(String open_time) {
        this.open_time = open_time;
    }

    public String getClose_time() {
        return close_time;
    }

    public void setClose_time(String close_time) {
        this.close_time = close_time;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getAddr_sido() {
        return addr_sido;
    }

    public void setAddr_sido(String addr_sido) {
        this.addr_sido = addr_sido;
    }

    public String getAddr_gugun() {
        return addr_gugun;
    }

    public void setAddr_gugun(String addr_gugun) {
        this.addr_gugun = addr_gugun;
    }

    public String getAddr_dong() {
        return addr_dong;
    }

    public void setAddr_dong(String addr_dong) {
        this.addr_dong = addr_dong;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getWorkTime() {
        return getWorkTime(" ~ ");
    }

    public String getWorkTime(String delimeter) {
        return getOpen_time(":") + delimeter + getClose_time(":");
    }

    public String getOpen_time(String delimeter) {
        String src = getOpen_time();
        if (src == null || src.length() != 4) {
            return "";
        }
        return src.substring(0, 2) + delimeter + src.substring(2, 4);
    }

    public String getClose_time(String delimeter) {
        String src = getClose_time();
        if (src == null || src.length() != 4) {
            return "";
        }
        return src.substring(0, 2) + delimeter + src.substring(2, 4);
    }
}
