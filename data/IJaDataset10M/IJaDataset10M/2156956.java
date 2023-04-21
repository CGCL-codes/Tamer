package com.c5corp.DEMconvert.filters;

import java.io.*;
import java.util.*;
import java.sql.*;
import com.c5corp.c5dem.*;
import com.c5corp.c5utm.*;
import com.c5corp.DEMconvert.*;

public final class Dem2C51KGrid extends C5DemAbstractFilter implements C5DemConstants {

    private String in_filename = null;

    private C5UTMconfs confs;

    private static String out_dir = "RECEIPTS";

    private boolean rez_ok = false;

    private boolean data_validates = false;

    private static boolean database_config = false;

    private PrintWriter out = null;

    /** Default constuctor, required by the newInstance() method of the Class class,
	* such that this class can be dynamically loaded.
	*/
    public Dem2C51KGrid() {
    }

    /** Builds tables from the specified file name.
	* Can be invoked from the command line.
	* @param filename the file name
	*/
    public Dem2C51KGrid(String filename) {
        this.in_filename = filename;
        DemTable demtab = new DemTable(filename);
        writeHeader(demtab, out);
        writeData(demtab, out);
    }

    /** the main method for command line usage
	 * @param args usage issue: -i input file must be .dem
	 */
    public static void main(String args[]) {
        String in = null;
        String check_me;
        boolean found_i = false;
        boolean found_d = false;
        if (args.length == 4 || args.length == 2) {
            for (int i = 0; i < args.length; i += 2) {
                if (args[i].equals("-i") && !found_i) {
                    in = args[i + 1];
                    found_i = true;
                } else if (args[i].equals("-d") && !found_d) {
                    out_dir = args[i + 1];
                    found_d = true;
                } else {
                    usageWarn();
                    return;
                }
            }
        } else {
            usageWarn();
            return;
        }
        if (found_i) {
            check_me = in.substring(in.length() - 4, in.length());
            if (!(check_me.equals(".dem"))) {
                usageWarn();
                System.err.println("usage issue: -i input file must be .dem");
                return;
            }
        }
        new Dem2C51KGrid(in);
    }

    public void writeHeader(Dem dem, PrintWriter out) {
        confs = new C5UTMconfs();
        database_config = confs.databaseWorking() && confs.databaseUnitsConfigured();
        if (!database_config) {
            System.err.print("config error: db_url, db_driver, access_password, and update password ");
            System.err.print("must be configured in com/c5corp/c5utm/conf/add2c5utm.conf and in the ");
            System.err.println("database itself. And the tables must be ready in the DB, of course.");
            System.err.println("configuration messages: " + confs.getMessages());
            return;
        }
        if (dem.get_planimetric_system() == 1 && hdatum[dem.get_horizontal_datum() - 1].equals(confs.getHorizontalDatum()) && vdatum[dem.get_vertical_datum() - 1].equals(confs.getVerticalDatum())) {
            data_validates = true;
        } else {
            System.err.println("Not processed: this C5UTM installation requires data in UTM coords, the horizontal datum " + confs.getHorizontalDatum() + ", and the vertical datum " + confs.getVerticalDatum() + ".");
            System.err.println(dem.get_file_name() + "\ncontains " + system[dem.get_planimetric_system()] + ", " + hdatum[dem.get_horizontal_datum() - 1] + ", " + vdatum[dem.get_vertical_datum() - 1]);
            return;
        }
        if (dem.get_spacial_rez_x() == 30 && dem.get_spacial_rez_y() == 30) {
            rez_ok = true;
        } else {
            System.err.println("Warn: can't process: " + dem.get_file_name() + ": The input spacial resolution (ing meters) x:" + dem.get_spacial_rez_x() + " y:" + dem.get_spacial_rez_y() + "\ncan not be converted with Dem2C51KGrid. (Both must be 30).\nBut Build1KGridFromDb can do it " + "Because the dem data is downsampled to 30 meters when added to the database.");
            return;
        }
    }

    /** A method to write the output data to the (database). (overrides C5DemAbstractFilter) */
    public void writeData(Dem dem, PrintWriter out) {
        DemTable demtab = (DemTable) dem;
        boolean feetConvertedToMeters = false;
        boolean metersConvertedToFeet = false;
        if (!(rez_ok && data_validates && database_config)) {
            System.err.println("Validation error: cannot write to database: file resolution ok: " + rez_ok + " data_validates (dem and db match): " + data_validates + " config_valid: " + database_config + " for file: " + dem.get_file_name() + ", (all must be true).");
            return;
        }
        if (!confs.getVerticalUnits().equals(units[dem.get_elevation_unit() - 1])) {
            if (dem.get_elevation_unit() == 1) {
                demtab.convertToVerticalMeters();
                feetConvertedToMeters = true;
            } else if (dem.get_elevation_unit() == 2) {
                demtab.convertToVerticalFeet();
                metersConvertedToFeet = true;
            } else {
                System.err.println(dem.get_file_name() + " might contain an unknown vertical unit.");
                return;
            }
        }
        Connection con = null;
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = null;
        try {
            con = pool.getConnection();
        } catch (SQLException e) {
            System.err.println(e);
        }
        if (dataOKtoAdd(dem, con)) {
            System.out.print("Dem2C51KGrid will try to add the data from:");
            System.out.println(dem.getInputFile());
        } else {
            System.err.print("Dem2C51KGrid: either the 1K grid data already exists in the DB, or the dem data has not been added: ");
            System.err.println(dem.get_file_name());
            return;
        }
        pool.freeConnection(con);
        PreciseUTMcoordPair sw = demtab.getSWcorner();
        PreciseUTMcoordPair nw = demtab.getNWcorner();
        PreciseUTMcoordPair ne = demtab.getNEcorner();
        PreciseUTMcoordPair se = demtab.getSEcorner();
        double temp = sw.getEasting() < nw.getEasting() ? sw.getEasting() : nw.getEasting();
        int easting_start = ((int) temp / 1000) * 1000;
        temp = sw.getNorthing() < se.getNorthing() ? sw.getNorthing() : se.getNorthing();
        int northing_start = ((int) temp / 1000) * 1000;
        temp = ne.getEasting() > se.getEasting() ? ne.getEasting() : se.getEasting();
        int easting_max = (((int) temp / 1000) * 1000 + 1000) - easting_start;
        temp = nw.getNorthing() > ne.getNorthing() ? nw.getNorthing() : ne.getNorthing();
        int northing_max = (((int) temp / 1000) * 1000 + 1000) - northing_start;
        Point any = null;
        try {
            UpdateConnectionPool uppool = UpdateConnectionPool.getInstance();
            try {
                connection = uppool.getConnection();
            } catch (SQLException e) {
                System.err.println(e);
            }
            for (int x = 0; x < easting_max; x += 1000) {
                for (int y = 0; y < northing_max; y += 1000) {
                    UtmCoordinatePairElev[][] area = demtab.getPoints(x + easting_start, y + northing_start, 1000, 1000);
                    if (area != null) {
                        addToDatabase(area, dem, connection);
                        createImage(area, dem, x + easting_start, y + northing_start);
                    }
                }
            }
            any = C5UTM.getPoint(dem.get_planimetric_zone(), easting_start + 2000, northing_start + 2000, connection);
            Statement insert = connection.createStatement();
            insert = connection.createStatement();
            insert.executeUpdate("UPDATE DEM_METADATA SET 1K_grid=1 WHERE DEM_METADATA_id=\"" + any.getDemId() + "\"");
            insert.close();
            uppool.freeConnection(connection);
        } catch (SQLException e) {
            System.err.println("Exception UPDATE DEM_METADATA: " + any.getDemId() + "\n");
            e.printStackTrace();
        }
        if (feetConvertedToMeters == true) {
            demtab.convertToVerticalFeet();
        } else if (metersConvertedToFeet == true) {
            demtab.convertToVerticalMeters();
        }
        System.out.println("Succesfully processed 1K grid data for " + in_filename + " to level 1.\n");
    }

    private void createImage(UtmCoordinatePairElev[][] area, Dem dem, int easting, int northing) {
        int zone = dem.get_planimetric_zone();
        String east = new String("" + easting).substring(0, 3);
        String north = new String("" + northing).substring(0, 4);
        String fileName = new String(zone + "_" + east + "_" + north);
        File file = new File(confs.getUTMImageDir() + File.separator + fileName);
        if (!file.exists()) {
            Points points = ArtificialPoints.getPoints(area, dem.get_planimetric_zone());
            UtmImage image = new UtmImage(points, UtmImage.RENDER_HIGH | UtmImage.RENDER_LOW | UtmImage.RENDER_MEAN | UtmImage.RENDER_MEDIAN | UtmImage.RENDER_MODE);
            image.writeImageFile(confs.getUTMImageDir(), fileName);
        }
    }

    private boolean addToDatabase(UtmCoordinatePairElev[][] area, Dem dem, Connection connection) {
        Points ob = ArtificialPoints.getPoints(area, dem.get_planimetric_zone());
        return insertRecord(ob, dem, connection);
    }

    private boolean insertRecord(Points ob, Dem dem, Connection connection) {
        boolean ok = false;
        try {
            Point sw = C5UTM.getPoint(ob.getPointAt(0, 0).getZone(), ob.getPointAt(0, 0).getEasting(), ob.getPointAt(0, 0).getNorthing(), connection);
            Point mid = C5UTM.getPoint(ob.getPointAt(16, 16).getZone(), ob.getPointAt(16, 16).getEasting(), ob.getPointAt(16, 16).getNorthing(), connection);
            if (mid == null || ob == null) {
                return ok;
            }
            Statement insert = connection.createStatement();
            try {
                insert.executeUpdate("INSERT INTO UTM_POINT_STATS " + "(UTM_POINT_STATS_id, low, high, mean, median, stdev, percentile, " + "contiguous_modality_percentage, topographic_descriptor, " + " terrain_descriptor) " + "values (\"" + mid.getId() + "\"," + ob.getLowest() + "," + ob.getHighest() + "," + (int) Math.round(ob.calculateMean()) + "," + ob.calculateMedian() + "," + ob.calculateStandardDeviation() + "," + ob.calculatePercentile(mid.getElevation()) + "," + ob.calculateContiguousModalityPercentage() + "," + ob.calculateTopographyDescriptor() + "," + ob.calculateTerrainDescriptor() + ")");
                insert.close();
                int[] mode = ob.calculateMode();
                for (int i = 0; i < mode.length; i++) {
                    insert = connection.createStatement();
                    insert.execute("INSERT INTO MODE (UTM_POINT_STATS_id, mode) VALUES (\"" + mid.getId() + "\"," + mode[i] + ")");
                    insert.close();
                }
            } catch (SQLException e) {
            }
            insert = connection.createStatement();
            insert.executeUpdate("INSERT INTO UTM_1K_GRID_DATA (UTM_1K_GRID_DATA_id, UTM_POINT_STATS_id) VALUES (\"" + sw.getId() + "\",\"" + mid.getId() + "\")");
            insert.close();
            ok = true;
        } catch (SQLException e) {
            System.err.println("Exception on adding new UTM_POINT_STATS,UTM_1K_GRID_DATA, and MODE:: " + ob + "\n");
            e.printStackTrace();
            ok = false;
        }
        return ok;
    }

    private boolean dataOKtoAdd(Dem dem, Connection connection) {
        Vector results;
        String testname = dem.get_file_name();
        boolean gridDataInDb = false;
        boolean demInDb = false;
        System.out.println("Checking if '" + testname + "' is ready to be processed into the 1K grid");
        results = C5UTM.findExactDemNames(testname, connection);
        if (results.size() > 0) {
            demInDb = true;
        } else {
            return false;
        }
        DemMetadata metadata = (DemMetadata) results.get(0);
        if (metadata.get1kGridStatus() > 0) {
            gridDataInDb = true;
        } else {
            gridDataInDb = false;
        }
        return demInDb && (!gridDataInDb);
    }

    private static void usageWarn() {
        System.err.println("usage: Dem2C51KGrid.java -i infile.dem [-d RECEIPT dir]\n" + "Check out the README.");
    }

    /** A method that returns a string containing information about the filter.*/
    public String getFilterInfo() {
        return "This filter is a wolly little adventure in data management, building the 1K grid data. \nThe database must be installed to use it.\n. (c) C5 corp, 2002-2003.";
    }

    /** Returns a relative path describing the location of the output.*/
    public String getOutputPath() {
        return out_dir;
    }

    /** sets the path to the output file
	 * @param path path to output directory
	 */
    public void setOutputPath(String path) {
        out_dir = path;
    }

    /** overrides java.lang.Object.toString() */
    public String toString() {
        return this.getClass().getName() + C5DemConstants.copy;
    }
}
