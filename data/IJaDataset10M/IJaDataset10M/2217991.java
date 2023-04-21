package edu.byu.jcreme;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.NumberFormat;

/**
 * @author Josh Engel <joshua.d.engel@gmail.com>
 */
public class TRP implements URFInterface {

    TRP(double apogee, double perigee, double inclination, int solmax, double init_long_ascend, double init_displ_ascend, double displ_perigee_ascend, double[] orbit_sections) {
        init(apogee, perigee, inclination, solmax, init_long_ascend, init_displ_ascend, displ_perigee_ascend, orbit_sections);
    }

    TRP(double apogee, double perigee, double inclination, int solmax, double init_long_ascend, double init_displ_ascend, double displ_perigee_ascend) {
        init(apogee, perigee, inclination, solmax, init_long_ascend, init_displ_ascend, displ_perigee_ascend, null);
    }

    TRP(double apogee, double perigee, double inclination, int solmax) {
        init(apogee, perigee, inclination, solmax, 0, 0, 0, null);
    }

    void init(double apogee, double perigee, double inclination, int solmax, double init_long_ascend, double init_displ_ascend, double displ_perigee_ascend, double[] orbit_sections) {
        _apogee = apogee;
        _perigee = perigee;
        _inclination = inclination;
        _init_long_ascend = init_long_ascend;
        _init_displ_ascend = init_displ_ascend;
        _displ_perigee_ascend = displ_perigee_ascend;
        _solmax = solmax;
        _orbit_sections = orbit_sections;
        _precalculated = false;
        _this_filename = construct_filename();
    }

    String construct_filename() {
        NumberFormat.getInstance().setMaximumFractionDigits(1);
        String filename = _apogee + "";
        if (_apogee != _perigee) filename += _perigee;
        filename += _inclination;
        if (_solmax == 1) filename += "_smx";
        if (_solmax == 0) filename += "_smn";
        if (_orbit_sections != null) filename += "_s_" + NumberFormat.getInstance().format(_orbit_sections[0]);
        filename = filename.replace('.', 'p');
        return filename.toUpperCase();
    }

    void toFile() {
        toFile(_this_filename);
    }

    void toFile(String filename) {
        PrintWriter outfile;
        try {
            outfile = new PrintWriter(new BufferedWriter(new FileWriter(filename + ".RQR")));
            if (_precalculated) outfile.println("1"); else {
                outfile.println("0");
                if (_solmax == 1) outfile.println("1"); else outfile.println("0");
                outfile.println(_apogee);
                outfile.println(_perigee);
                outfile.println(_inclination);
                outfile.println(_init_long_ascend);
                outfile.println(_init_displ_ascend);
                if (_apogee != _perigee) outfile.println(_displ_perigee_ascend);
                outfile.println("200");
                if (_orbit_sections == null) outfile.println("0"); else {
                    if (_orbit_sections.length == 1) {
                        outfile.println(1);
                        outfile.println(_orbit_sections[0]);
                    } else if (_orbit_sections.length == 2) {
                        outfile.println(2);
                        outfile.println(NumberFormat.getInstance().format(_orbit_sections[0]) + "," + NumberFormat.getInstance().format(_orbit_sections[1]));
                    } else throw new RuntimeException("Error: Code does not support more than pairs of orbits");
                }
            }
            outfile.println(filename + ".TRP");
            outfile.close();
            System.out.println(filename + ".RQR" + ",");
        } catch (IOException except) {
            System.out.println("There was an error created the specified file.");
        }
    }

    double getApogee() {
        return _apogee;
    }

    double getPerigee() {
        return _perigee;
    }

    double getInclination() {
        return _inclination;
    }

    double getInitLongAscend() {
        return _init_long_ascend;
    }

    double getInitDisplAscend() {
        return _init_displ_ascend;
    }

    double getDisplPerigeeAscend() {
        return _displ_perigee_ascend;
    }

    int getSolmax() {
        return _solmax;
    }

    double[] getOrbitSections() {
        return _orbit_sections;
    }

    public String getOutputFileName() {
        if (_orbit_sections == null) return _this_filename + ".TRP"; else return _this_filename + ".TR1";
    }

    public String getThisFileName() {
        return _this_filename + ".RQR";
    }

    private double _apogee;

    private double _perigee;

    private double _inclination;

    private double _init_long_ascend;

    private double _init_displ_ascend;

    private double _displ_perigee_ascend;

    private int _solmax;

    private double[] _orbit_sections;

    private boolean _precalculated;

    private String _this_filename;
}
