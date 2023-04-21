package edu.byu.jcreme;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;

/**
 * @author Josh Engel <joshua.d.engel@gmail.com>
 */
public class LETSPEC implements URFInterface {

    LETSPEC(URFInterface flx_input_object, int min_element, int max_element, double min_energy_value, boolean diff_spect, boolean print_min_energy) {
        init_flux(flx_input_object, min_element, max_element, min_energy_value, diff_spect, print_min_energy);
    }

    LETSPEC(URFInterface flx_input_object, int min_element, int max_element, double min_energy_value, boolean print_min_energy) {
        init_flux(flx_input_object, min_element, max_element, min_energy_value, true, print_min_energy);
    }

    void init_flux(URFInterface flx_input_object, int min_element, int max_element, double min_energy_value, boolean diff_spect, boolean print_min_energy) {
        _flx_input_file = flx_input_object.getOutputFileName();
        _min_element = min_element;
        _max_element = max_element;
        _min_energy_value = min_energy_value;
        _diff_spect = diff_spect;
        _print_min_energy = print_min_energy;
        _this_filename = construct_filename();
    }

    String construct_filename() {
        String filename = _flx_input_file;
        filename = filename.replace(".flx", "");
        filename = filename.replaceAll(".FLX", "");
        filename = filename.replaceAll(".tfx", "");
        filename = filename.replaceAll(".TFX", "");
        if (_print_min_energy) filename += "_minengy" + _min_energy_value;
        filename = filename.replace('.', 'p');
        return filename.toUpperCase();
    }

    void toFile() {
        toFile(_this_filename);
    }

    void toFile(String filename) {
        PrintWriter outfile;
        try {
            outfile = new PrintWriter(new BufferedWriter(new FileWriter(filename + ".RQL")));
            outfile.println(_flx_input_file);
            outfile.println(_min_element + "," + _max_element);
            outfile.println(_min_energy_value);
            outfile.println(filename + ".LET");
            if (_diff_spect) outfile.println("1"); else outfile.println("0");
            outfile.close();
            System.out.println(filename + ".RQL" + ",");
        } catch (IOException except) {
            System.out.println("There was an error creating the specified file.");
        }
    }

    int getMinElement() {
        return _min_element;
    }

    int getMaxElement() {
        return _max_element;
    }

    double getMinEnergyValue() {
        return _min_energy_value;
    }

    boolean getDiffSpect() {
        return _diff_spect;
    }

    String getFlxInputFile() {
        return _flx_input_file;
    }

    public String getOutputFileName() {
        return _this_filename + ".LET";
    }

    public String getThisFileName() {
        return _this_filename + ".RQL";
    }

    public static LETSPEC[] CreateLETSPEC_URF(URFInterface[] flx_input_object, int[] min_element, int[] max_element, double[] min_energy_value, boolean[] diff_spect, boolean print_min_energy) {
        Vector letspec_vector = new Vector();
        int index = 0;
        for (int i = 0; i < flx_input_object.length; i++) {
            for (int k = 0; k < min_element.length; k++) {
                for (int l = 0; l < max_element.length; l++) {
                    for (int m = 0; m < min_energy_value.length; m++) {
                        for (int n = 0; n < diff_spect.length; n++) {
                            letspec_vector.add(index, new LETSPEC(flx_input_object[i], min_element[k], max_element[l], min_energy_value[m], diff_spect[n], print_min_energy));
                            ((LETSPEC) letspec_vector.elementAt(index)).toFile();
                            index++;
                        }
                    }
                }
            }
        }
        LETSPEC[] letspec_toReturn = new LETSPEC[letspec_vector.size()];
        return (LETSPEC[]) letspec_vector.toArray(letspec_toReturn);
    }

    private int _min_element;

    private int _max_element;

    private double _min_energy_value;

    private boolean _diff_spect;

    private String _flx_input_file;

    private String _this_filename;

    private boolean _print_min_energy;
}
