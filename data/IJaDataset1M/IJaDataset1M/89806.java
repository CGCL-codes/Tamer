package jtk.project4.fleet.domain;

public class LastPmSetUp {

    private String pmservicename;

    private String type;

    private String date;

    private String mileage;

    public String getPmservice() {
        return pmservicename;
    }

    public void setPmservice(String pmservicename) {
        this.pmservicename = pmservicename;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMileage() {
        return mileage;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }
}
