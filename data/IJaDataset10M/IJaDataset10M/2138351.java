package data;

import data.constants.Technology;

public class Tech extends AbstractData {

    public double drive, weapon, shield, cargo;

    public Tech() {
    }

    public Tech(Tech t) {
        copy(t);
    }

    public void copy(AbstractData data) {
        if (data == null) return;
        super.copy(data);
        Tech t = (Tech) data;
        drive = t.drive;
        weapon = t.weapon;
        shield = t.shield;
        cargo = t.cargo;
    }

    public Tech(double d, double w, double s, double c) {
        drive = d;
        weapon = w;
        shield = s;
        cargo = c;
    }

    public double get(Technology t) {
        switch(t) {
            case DRIVE:
                return drive;
            case WEAPONS:
                return weapon;
            case SHIELDS:
                return shield;
            case CARGO:
                return cargo;
            default:
                return 0;
        }
    }

    public void set(Technology t, double val) {
        switch(t) {
            case DRIVE:
                drive = val;
                break;
            case WEAPONS:
                weapon = val;
                break;
            case SHIELDS:
                shield = val;
                break;
            case CARGO:
                cargo = val;
                break;
        }
    }
}
