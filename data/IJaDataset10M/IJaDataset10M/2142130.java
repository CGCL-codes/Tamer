package com.nhncorp.cubridqa.result.mail;

/**
 * 
 * This model is used send performance test results as an email.
 * @ClassName: MailModelRow
 * @date 2009-9-4
 * @version V1.0
 * Copyright (C) www.nhn.com
 */
public class MailModelRow {

    private String ok = "";

    private String path = "";

    private String avg = "";

    private String min = "";

    private String max = "";

    private String sd = "";

    private String time = "";

    private String avg2 = "";

    private String min2 = "";

    private String max2 = "";

    private String sd2 = "";

    private String time2 = "";

    private String color = "black";

    public String getOk() {
        return ok;
    }

    public void setOk(String ok) {
        this.ok = ok;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        String mymin = ResultMailSender.format(min);
        this.min = mymin;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        String mymax = ResultMailSender.format(max);
        this.max = mymax;
    }

    public String getSd() {
        return sd;
    }

    public void setSd(String sd) {
        String mysd = ResultMailSender.format(sd);
        this.sd = mysd;
    }

    public String getAvg() {
        return avg;
    }

    public void setAvg(String avg) {
        String myavg = ResultMailSender.format(avg);
        this.avg = myavg;
    }

    public String getAvg2() {
        return avg2;
    }

    public void setAvg2(String avg2) {
        String myavg2 = ResultMailSender.format(avg2);
        this.avg2 = myavg2;
    }

    public String getMin2() {
        return min2;
    }

    public void setMin2(String min2) {
        String mymin2 = ResultMailSender.format(min2);
        this.min2 = mymin2;
    }

    public String getMax2() {
        return max2;
    }

    public void setMax2(String max2) {
        String mymax2 = ResultMailSender.format(max2);
        this.max2 = mymax2;
    }

    public String getSd2() {
        return sd2;
    }

    public void setSd2(String sd2) {
        String mysd2 = ResultMailSender.format(sd2);
        this.sd2 = mysd2;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime2() {
        return time2;
    }

    public void setTime2(String time2) {
        this.time2 = time2;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
