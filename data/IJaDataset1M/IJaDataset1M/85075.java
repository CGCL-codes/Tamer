package request_files;

import java.io.*;
import java.util.Vector;

public class ORBIT {

    ORBIT(double apogee, double perigee, double inclination, boolean gtrn_weather, int trp_solmax, double init_long_ascend, double init_displ_ascend, double displ_perigee_ascend, double[] orbit_sect) {
        init(apogee, perigee, inclination, gtrn_weather, trp_solmax, init_long_ascend, init_displ_ascend, displ_perigee_ascend, orbit_sect);
    }

    ORBIT(double apogee, double perigee, double inclination, boolean gtrn_weather, int trp_solmax, double init_long_ascend, double init_displ_ascend, double displ_perigee_ascend) {
        init(apogee, perigee, inclination, gtrn_weather, trp_solmax, init_long_ascend, init_displ_ascend, displ_perigee_ascend, null);
    }

    ORBIT(double apogee, double perigee, double inclination, boolean gtrn_weather, int trp_solmax) {
        init(apogee, perigee, inclination, gtrn_weather, trp_solmax, 0, 0, 0, null);
    }

    void init(double apogee, double perigee, double inclination, boolean gtrn_weather, int trp_solmax, double init_long_ascend, double init_displ_ascend, double displ_perigee_ascend, double[] orbit_sect) {
        _apogee = apogee;
        _perigee = perigee;
        _inclination = inclination;
        _init_long_ascend = init_long_ascend;
        _init_displ_ascend = init_displ_ascend;
        _displ_perigee_ascend = displ_perigee_ascend;
        _gtrn_weather = gtrn_weather;
        _trp_solmax = trp_solmax;
        _orbit_sect = orbit_sect;
        _my_gtrn = new GTRN(apogee, perigee, inclination, gtrn_weather, init_long_ascend, init_displ_ascend, displ_perigee_ascend, orbit_sect);
        if (_trp_solmax == 2) _my_trp = null; else _my_trp = new TRP(apogee, perigee, inclination, trp_solmax, init_long_ascend, init_displ_ascend, displ_perigee_ascend, orbit_sect);
        if (_orbit_sect == null) {
            _orbit_sections = false;
            _num_of_orbit_sections = 0;
        } else {
            _orbit_sections = true;
            _num_of_orbit_sections = _orbit_sect.length;
        }
        _precalculated = false;
    }

    double getInclination() {
        return _inclination;
    }

    double getApogee() {
        return _apogee;
    }

    double getPerigee() {
        return _perigee;
    }

    boolean getGTRNWeather() {
        return _gtrn_weather;
    }

    int getTRPSolmax() {
        return _trp_solmax;
    }

    double getInitLongAscend() {
        return _init_long_ascend;
    }

    double getInitDisplAscend() {
        return _init_displ_ascend;
    }

    double getDisplPerigeeAsecend() {
        return _displ_perigee_ascend;
    }

    double[] getOrbitSect() {
        return _orbit_sect;
    }

    GTRN getGTRN() {
        return _my_gtrn;
    }

    TRP getTRP() {
        return _my_trp;
    }

    String getThisFileName() {
        return _apogee + " " + _perigee + " " + _inclination + " " + _gtrn_weather + " " + _trp_solmax + " " + _init_long_ascend + " " + _init_displ_ascend + " " + _displ_perigee_ascend + " " + _orbit_sect;
    }

    public static void main(String[] args) {
    }

    public static ORBIT[] CreateOrbits(double[] apogee, double[] perigee, double[] inclination, boolean[] gtrn_weather, int[] trp_solmax, double[] init_long_ascend, double[] init_displ_ascend, double[] displ_perigee_ascend, double[][] orbit_sect) {
        if (perigee == null) perigee = apogee;
        if (apogee.length != perigee.length) throw new ArrayIndexOutOfBoundsException("apogee and perigee arrays not of equal length");
        Vector orbit_vector = new Vector();
        int index = 0;
        for (int h = 0; h < gtrn_weather.length; h++) {
            for (int j = 0; j < trp_solmax.length; j++) {
                for (int i = 0; i < apogee.length; i++) {
                    for (int k = 0; k < inclination.length; k++) {
                        for (int m = 0; m < init_long_ascend.length; m++) {
                            for (int n = 0; n < init_displ_ascend.length; n++) {
                                for (int o = 0; o < displ_perigee_ascend.length; o++) {
                                    if (orbit_sect != null) {
                                        for (int p = 0; p < orbit_sect.length; p++) {
                                            orbit_vector.add(index, new ORBIT(apogee[i], perigee[i], inclination[k], gtrn_weather[h], trp_solmax[j], init_long_ascend[m], init_displ_ascend[n], displ_perigee_ascend[o], orbit_sect[p]));
                                            GTRN temp_gtrn = ((ORBIT) orbit_vector.elementAt(index)).getGTRN();
                                            if (temp_gtrn != null) temp_gtrn.toFile();
                                            TRP temp_trp = ((ORBIT) orbit_vector.elementAt(index)).getTRP();
                                            if (temp_trp != null) temp_trp.toFile();
                                            index++;
                                        }
                                    } else {
                                        orbit_vector.add(index, new ORBIT(apogee[i], perigee[i], inclination[k], gtrn_weather[h], trp_solmax[j], init_long_ascend[m], init_displ_ascend[n], displ_perigee_ascend[o], null));
                                        GTRN temp_gtrn = ((ORBIT) orbit_vector.elementAt(index)).getGTRN();
                                        if (temp_gtrn != null) temp_gtrn.toFile();
                                        TRP temp_trp = ((ORBIT) orbit_vector.elementAt(index)).getTRP();
                                        if (temp_trp != null) temp_trp.toFile();
                                        index++;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        ORBIT[] orbit_toReturn = new ORBIT[orbit_vector.size()];
        return (ORBIT[]) orbit_vector.toArray(orbit_toReturn);
    }

    private GTRN _my_gtrn;

    private TRP _my_trp;

    private double _apogee;

    private double _perigee;

    private double _inclination;

    private double _init_long_ascend;

    private double _init_displ_ascend;

    private double _displ_perigee_ascend;

    private boolean _gtrn_weather;

    private int _trp_solmax;

    private boolean _orbit_sections;

    private double[] _orbit_sect;

    private boolean _precalculated;

    private int _num_of_orbit_sections;
}
