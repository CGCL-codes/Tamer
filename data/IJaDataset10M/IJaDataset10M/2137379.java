package nu.transformations;

import nu.entities.*;
import java.util.Vector;

public class NUAMDatumUtils {

    public NUAMDatumUtils() {
    }

    public double findScalarXmax(Vector v) {
        double x_max;
        if (v.size() == 0) x_max = -1; else {
            x_max = ((NUAMScalarDatum) v.elementAt(0)).getMagnitude();
            for (int i = 0; i < v.size(); i++) {
                x_max = ((NUAMScalarDatum) v.elementAt(i)).getMagnitude() > x_max ? ((NUAMScalarDatum) v.elementAt(i)).getMagnitude() : x_max;
            }
        }
        return x_max;
    }

    public double findScalarXmin(Vector v) {
        double x_min;
        if (v.size() == 0) x_min = -1; else {
            x_min = ((NUAMScalarDatum) v.elementAt(0)).getMagnitude();
            for (int i = 0; i < v.size(); i++) {
                x_min = ((NUAMScalarDatum) v.elementAt(i)).getMagnitude() < x_min ? ((NUAMScalarDatum) v.elementAt(i)).getMagnitude() : x_min;
            }
        }
        return x_min;
    }

    public double findScalarEmax(Vector v) {
        double e_max;
        if (v.size() == 0) e_max = -1; else {
            e_max = ((NUAMScalarDatum) v.elementAt(0)).getError();
            for (int i = 0; i < v.size(); i++) {
                e_max = ((NUAMScalarDatum) v.elementAt(i)).getError() > e_max ? ((NUAMScalarDatum) v.elementAt(i)).getError() : e_max;
            }
        }
        return e_max;
    }

    public double findScalarEmin(Vector v) {
        double e_min;
        if (v.size() == 0) e_min = -1; else {
            e_min = ((NUAMScalarDatum) v.elementAt(0)).getError();
            for (int i = 0; i < v.size(); i++) {
                e_min = ((NUAMScalarDatum) v.elementAt(i)).getError() < e_min ? ((NUAMScalarDatum) v.elementAt(i)).getError() : e_min;
            }
        }
        return e_min;
    }
}
