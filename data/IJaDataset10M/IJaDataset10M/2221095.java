package org.jazzteam.model;

import java.util.ArrayList;

public class Apartment extends Identifiable {

    private ArrayList<Residents> residents;

    private ApartmentDetalis apartmentDetalis;

    Apartment() {
        apartmentDetalis = new ApartmentDetalis();
        residents = new ArrayList<Residents>();
    }

    public ApartmentDetalis getApartmentDetalis() {
        return apartmentDetalis;
    }

    public ArrayList<Residents> getResidents() {
        return residents;
    }

    public void setApartmentDetalis(ApartmentDetalis apartmentDetalis) {
        this.apartmentDetalis = apartmentDetalis;
    }

    public void setResidents(ArrayList<Residents> residents) {
        this.residents = residents;
    }
}
