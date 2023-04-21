package testRow.ams;

import java.io.*;

/**
 * Used to generate AMS test data
 * @author shaferia
 */
public class GenData {

    private boolean warn_overwrite;

    private static String THISLOC = "testRow/ams/";

    /**
	 * Use for one-time generation of data
	 * @param args	not used.
	 */
    public static void main(String[] args) {
        GenData d = new GenData();
        d.warn_overwrite = true;
        int items = 20;
        int mzlen = 20;
        d.writeData("Test", items, mzlen, new int[] { 2, 5, 6 });
        d.writeTimeSeries("TS", items, 3114199800l, 600);
        d.writeMZ("mz", mzlen);
    }

    /**
	 * Generate sample AMS data
	 * @param items	the number of AMS items to write
	 * @param mzlen	the maximum in the range of m/z values to consider
	 * @param peaks	a list containing the m/z values at which AMS items should have peaks (in range 1..mzlen)
	 * @param tstart	the time at which to start the particle timeseries
	 * @param tdelta	the fixed timestep for the particle timeseries
	 * @param fnames	names of the files to generate: {datasetname, timeseriesname, mzname}
	 * @return relative pathnames of the files created: {datasetname, timeseriesname, mzname}
	 */
    public static String[] generate(String[] fnames, int items, int mzlen, int[] peaks, long tstart, long tdelta) {
        GenData d = new GenData();
        d.warn_overwrite = false;
        d.writeData(fnames[0], items, mzlen, peaks);
        d.writeTimeSeries(fnames[1], items, tstart, tdelta);
        d.writeMZ(fnames[2], mzlen);
        for (int i = 0; i < fnames.length; ++i) fnames[i] = THISLOC + fnames[i] + ".txt";
        return fnames;
    }

    /**
	 * Get a PrintWriter that outputs to the specified file
	 * @param fname the name of the file
	 * @return a PrintWriter on file fname
	 */
    private PrintWriter getWriter(String fname) {
        File f = new File(THISLOC + fname);
        if (warn_overwrite && f.exists()) System.out.printf("Warning: file %s already exists; overwriting.\n", fname);
        try {
            return new PrintWriter(new BufferedWriter(new FileWriter(f)));
        } catch (IOException ex) {
            System.out.printf("Error: Could not create file %s\n", fname);
            ex.printStackTrace();
            return null;
        }
    }

    /**
	 * Write the 2D matrix data file for the AMS test data
	 * @param name	the dataset name (name and first line of file)
	 * @param items	the number of AMS items to write to the file
	 * @param mzlen	the number of m/z values in the data
	 * @param peaks	the m/z values at which to write non-negative values
	 */
    public void writeData(String name, int items, int mzlen, int[] peaks) {
        PrintWriter file = getWriter(name + ".txt");
        file.println(name);
        double[] peakVals = { 0.1, 0.2, 0.3, 0.4, 0.5 };
        int nothing = -999;
        for (int i = 0; i < items; ++i) {
            int k = 1;
            for (int j = 0; j < peaks.length; ++j) {
                for (; k < peaks[j]; ++k) {
                    file.print(nothing);
                    file.print("\t");
                }
                file.print(peakVals[(i * peaks.length + j) % peakVals.length]);
                file.print("\t");
                k++;
            }
            for (; k <= mzlen; ++k) {
                file.print(nothing);
                file.print("\t");
            }
            file.println();
        }
        file.close();
    }

    /**
	 * Write the time series
	 * @param name	the name of the time series (name and first line of file)
	 * @param items	the number of AMS items written to the data file
	 * @param tstart	the starting time of the time series
	 * @param tdelta	the difference between sequential times
	 */
    public void writeTimeSeries(String name, int items, long tstart, long tdelta) {
        PrintWriter file = getWriter(name + ".txt");
        file.println(name);
        for (int i = 0; i < items; ++i) {
            file.println(tstart);
            tstart += tdelta;
        }
        file.close();
    }

    /**
	 * Write the m/z file
	 * @param name	the name of the m/z list (name and first line of file)
	 * @param mzlen the length of the m/z list (starts at 1, ends at mzlen)
	 */
    public void writeMZ(String name, int mzlen) {
        PrintWriter file = getWriter(name + ".txt");
        file.println(name);
        for (int i = 1; i <= mzlen; ++i) {
            file.println(i);
        }
        file.close();
    }
}
