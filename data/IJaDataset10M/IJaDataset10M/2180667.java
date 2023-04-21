package playground.fhuelsmann.emission.objects;

public class HbefaColdObject {

    private String VehCat;

    private String Component;

    private String parkingTime;

    private String distance;

    private double coldEF;

    public String getVehCat() {
        return VehCat;
    }

    public void setVehCat(String vehCat) {
        VehCat = vehCat;
    }

    public String getComponent() {
        return Component;
    }

    public void setComponent(String component) {
        Component = component;
    }

    public String getParkingTime() {
        return parkingTime;
    }

    public void setParkingTime(String parkingTime) {
        this.parkingTime = parkingTime;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public double getColdEF() {
        return coldEF;
    }

    public void setColdEF(double coldEF) {
        this.coldEF = coldEF;
    }

    public HbefaColdObject(String vehCat, String component, String parkingTime, String distance, double coldEF) {
        this.VehCat = vehCat;
        this.Component = component;
        this.parkingTime = parkingTime;
        this.distance = distance;
        this.coldEF = coldEF;
    }
}
