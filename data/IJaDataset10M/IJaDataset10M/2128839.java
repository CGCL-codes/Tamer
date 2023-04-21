package com.c5corp.DEMconvert.filters;

import java.io.*;
import com.c5corp.c5dem.*;
import com.c5corp.DEMconvert.*;

public final class SampleFilter extends C5DemAbstractFilter {

    String in_filename = null;

    String out_filename = null;

    static String out_dir = "DAT";

    DemTable demtab = null;

    PrintWriter out = null;

    /** the default constructor creates a blank object such that
	* writeHeader(Dem, PrintWriter) and writeData(Dem, Printwriter)
	* can be used with arbitrary Dem and PrintWriter objects
	*/
    public SampleFilter() {
    }

    /** this constructor is used by the main method in this class,
	* which is intended mostly for command line use.
	* This is optional - see comment for main.
	 * @param in_fileame the file name
	*/
    public SampleFilter(String in_fileame) {
        demtab = new DemTable(in_fileame);
        out = openOutputFile(demtab.get_file_name(), getOutputPath());
        writeHeader(demtab, out);
        writeData(demtab, out);
        out.close();
    }

    /** The main method is maintained so that this filter can be used
	* from the command line, in addition to being used inside of c5 dem tool.
	* This is optional - C5 DEM Tools interface only requires the methods
	* writeHeader and writeData, and getOutputPath below.
	* @param args Usage: $ SampleFilter infile.dem [output dir]
	*/
    public static void main(String args[]) {
        String in;
        try {
            in = args[0];
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Usage: $ SampleFilter infile.dem [output dir]");
            return;
        }
        try {
            out_dir = args[1];
        } catch (ArrayIndexOutOfBoundsException e) {
            out_dir = ".";
        }
        new SampleFilter(in.toString());
    }

    /** A method for output of metadata to the file (overrides C5DemAbstractFilter),
	displaying many of the methods of the Dem class.
	*/
    public void writeHeader(Dem dem, PrintWriter out) {
        double[] adub;
        out.println("");
        out.println("public void writeHeader(Dem dem, PrintWriter out)");
        out.println("test Block for TypeA data");
        out.println("1. filename: " + dem.get_file_name());
        out.println("2. mcorigin: " + dem.get_mc_origin());
        out.println("3. demlevelcode: " + dem.get_dem_level_code());
        out.println("4. elevationpattern: " + dem.get_elevation_pattern());
        out.println("5. planimetricsystem: " + dem.get_planimetric_system());
        out.println("6. planimetriczone: " + dem.get_planimetric_zone());
        adub = dem.get_projection_parameters();
        for (int i = 0; i < 15; i++) {
            out.println("7. projectionparameters: " + i + " " + adub[i]);
        }
        out.println("8. planimetricunit: " + dem.get_planimetric_unit());
        out.println("9. elevationunit: " + dem.get_elevation_unit());
        out.println("10. sides: " + dem.get_polygon_sides());
        double[][] dub2by4 = dem.get_ground_coordinates();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 2; j++) {
                out.println("11. corners: " + i + " " + j + " " + dub2by4[i][j]);
            }
        }
        adub = dem.get_min_and_max_values();
        for (int i = 0; i < 2; i++) {
            out.println("12. minmax: " + i + " " + adub[i]);
        }
        out.println("13. counterangle: " + dem.get_counter_angle());
        out.println("14. elevation accuracy code: " + dem.get_accuracy_code());
        adub = dem.get_xyz_resolution();
        for (int i = 0; i < 3; i++) {
            out.println("15. xyzresolution: " + i + " " + adub[i]);
        }
        short[] srt = dem.get_rows_and_columns();
        for (int i = 0; i < 2; i++) {
            out.println("16. rowsandcolumns: " + i + " " + srt[i]);
        }
        out.println("verticaldatum: " + dem.get_vertical_datum());
        out.println("horizontaldatum: " + dem.get_horizontal_datum());
        out.println("");
        out.println("");
        out.println("test Block for TypeC fields ");
        out.println("statsavailable: " + dem.get_stats_available());
        out.println("filermse: " + dem.get_file_rmse());
        out.println("filermsesamplesize: " + dem.get_file_rmse_sample_size());
        out.println("availability: " + dem.get_availability());
        out.println("datarmse: " + dem.get_data_rmse());
        out.println("getdatarmsesamplesize: " + dem.get_data_rmse_sample_size());
    }

    /** A method to write the output (formated) data to the file. (overrides C5DemAbstractFilter) */
    public void writeData(Dem dem, PrintWriter out) {
        TypeB typeB;
        int[] elevations;
        short[] srt;
        double[] adub;
        out.println("");
        out.println("public void writeData(Dem dem, PrintWriter out)");
        out.println("Test output of Dem TypeB fields.");
        for (int i = 0; i < 2; i++) {
            out.println("Type B data for profile: " + i);
            typeB = dem.getTypeB(i);
            srt = typeB.get_row_and_column_id();
            out.println("1. rowandcolumid 0: " + srt[0]);
            out.println("1. rowandcolumid 1: " + srt[1]);
            srt = typeB.get_number_of_elevations();
            out.println("2. numberofelevations 0: " + srt[0]);
            out.println("2. numberofelevations 1: " + srt[1]);
            adub = typeB.get_first_elevation_coords();
            out.println("3. firstelevationcoords 0: " + adub[0]);
            out.println("3. firstelevationcoords 1: " + adub[1]);
            out.println("4. localelevation: " + typeB.get_datum_elevation());
            adub = typeB.get_profile_min_max();
            out.println("5. profileminmax 0: " + adub[0]);
            out.println("5. profileminmax 1: " + adub[1]);
            elevations = typeB.get_elevations();
            for (int j = 0; j < elevations.length; j++) {
                out.print(" " + j + "=" + elevations[j]);
            }
            out.println();
        }
        DemTable demtab = (DemTable) dem;
        UtmCoordinatePairElev[][] table = demtab.getTable();
        out.println("");
        out.println("Test output of DemTable UtmCoordinatePairElev[][]");
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < table[i].length; j++) {
                if (table[i][j] != null) {
                    out.println("easting: " + table[i][j].getEasting() + " northing: " + table[i][j].getNorthing() + " elevation: " + table[i][j].getElevation());
                }
            }
        }
    }

    /** A method that returns a string containing information about the filter.*/
    public String getFilterInfo() {
        return "The source code for this filter demonstrates how to write your own filters.";
    }

    /** Returns a relative path describing the location of the output.*/
    public String getOutputPath() {
        return out_dir;
    }

    /** overrides java.lang.Object.toString() */
    public String toString() {
        return this.getClass().getName() + C5DemConstants.copy;
    }
}
