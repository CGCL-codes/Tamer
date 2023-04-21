package eu.ict.persist.ThirdPartyServices.DMTService.interfaces;

public interface IDMT2PSS {

    public void setPosition(double latitude, double longitude, double elevation, int double1);

    public void setDirection(double roll, double pitch, double yaw);

    public void gpsConnected(boolean connected);

    public void compassConnected(boolean connected);

    public void viewLoaded(String viewXML);

    public void poisSent();
}
