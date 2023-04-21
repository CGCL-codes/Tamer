package org.jazzteam.lang.aero;

import java.util.ArrayList;

/**
 * Класс реализует модель самолета
 *
 * @author Andrey Avtuchovich
 */
public class Plane extends Identifiable {

    private ArrayList<Personal> personal;

    private PlaneDetails planeDetails;

    private String status;

    Plane() {
        planeDetails = new PlaneDetails();
        personal = new ArrayList<Personal>();
    }

    public PlaneDetails getPlaneDetails() {
        return planeDetails;
    }

    public void setPlaneDetails(PlaneDetails planeDetails) {
        this.planeDetails = planeDetails;
    }

    public ArrayList<Personal> getPersonal() {
        return personal;
    }

    public void setPersonal(ArrayList<Personal> personal) {
        this.personal = personal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
