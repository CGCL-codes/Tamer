package com.c5corp.c5utm.util;

/** <p>This convienence class calls BuildPointStatsFromFiles.BuildPointStats()
* and BuildPointStatsFromDb.BuildPointStats() in this order.</p>
* This is generally a smart order in which to call these utilities, because
* it can be much faster to build the 1K grid data and images from files first,
* (BuildPointStatsFromFiles) followed by stitching and infilling the unfilled
* edges utilizing the UTM_COORDS data in the database, which is what
* BuildPointStatsFromDb does if the point_stats data attribute of DEM_METADATA
* is already set to 1. However, BuildPointStatsFromDb will process an entire
* dem area if asked to (assuming that 1K_grid is set to 0). For some
* situations this might be a better option depending on relative i/o speed.</p>
* @see BuildPointStatsFromFiles
* @see BuildPointStatsFromDb
* @author  Brett Stalbaum
* @version 2.0
* @since 2.0
*/
public class BuildPointStats {

    /** Run from the command line 
	 * @param args args true or no argument for verbose, false to quiet */
    public static void main(String[] args) {
        try {
            boolean verbose = false;
            if (args[0] != null && args[0].equals("true")) {
                verbose = true;
            }
            BuildPointStatsFromFiles.BuildPointStats(verbose);
            BuildPointStatsFromDb.BuildPointStats(verbose);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("usage: BuildPointStats [true|false]");
            System.err.println("(\"true\" turns on verbose mode)");
            e.printStackTrace();
        }
    }
}
