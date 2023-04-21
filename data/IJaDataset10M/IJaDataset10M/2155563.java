package au.org.ala.spatial.utils;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * DivaToAsc
 *
 * Transforms DIVA grids to ASCII grids
 *
 * @author ajay
 */
class DivaToAsc {

    public static void convertDivaToAsc(String filename, String ascfilename) throws FileNotFoundException, Exception {
        convertDivaToAsc(new Grid(filename), ascfilename);
    }

    public static void convertDivaToAsc(Grid grid, String ascfilename) throws FileNotFoundException, Exception {
        if (grid == null) {
            throw new FileNotFoundException("The specified grid file was not available at " + grid.filename + ". Please check the grid file exists.");
        }
        BufferedWriter fw = null;
        try {
            fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(ascfilename), "US-ASCII"));
            fw.append("ncols ").append(String.valueOf(grid.ncols)).append("\n");
            fw.append("nrows ").append(String.valueOf(grid.nrows)).append("\n");
            fw.append("xllcorner ").append(String.valueOf(grid.xmin)).append("\n");
            fw.append("yllcorner ").append(String.valueOf(grid.ymin)).append("\n");
            fw.append("cellsize ").append(String.valueOf(grid.xres)).append("\n");
            fw.append("NODATA_value ").append(String.valueOf(-1));
            float[] grid_data = grid.getGrid();
            System.out.println("grid_data: " + grid_data.length);
            System.out.println("grid_data[0]: " + grid_data[0]);
            for (int i = 0; i < grid.nrows; i++) {
                fw.append("\n");
                for (int j = 0; j < grid.ncols; j++) {
                    if (j > 0) {
                        fw.append(" ");
                    }
                    if (Float.isNaN(grid_data[i * grid.ncols + j])) {
                        fw.append("-1");
                    } else {
                        fw.append(String.valueOf(grid_data[i * grid.ncols + j]));
                    }
                }
            }
            fw.append("\n");
            fw.close();
            writeProjectionFile(ascfilename.replace(".asc", ".prj"));
        } catch (Exception e) {
            throw e;
        }
    }

    private static void writeProjectionFile(String filename) {
        try {
            PrintWriter spWriter = new PrintWriter(new BufferedWriter(new FileWriter(filename)));
            StringBuffer sbProjection = new StringBuffer();
            sbProjection.append("GEOGCS[\"WGS 84\", ").append("\n");
            sbProjection.append("    DATUM[\"WGS_1984\", ").append("\n");
            sbProjection.append("        SPHEROID[\"WGS 84\",6378137,298.257223563, ").append("\n");
            sbProjection.append("            AUTHORITY[\"EPSG\",\"7030\"]], ").append("\n");
            sbProjection.append("        AUTHORITY[\"EPSG\",\"6326\"]], ").append("\n");
            sbProjection.append("    PRIMEM[\"Greenwich\",0, ").append("\n");
            sbProjection.append("        AUTHORITY[\"EPSG\",\"8901\"]], ").append("\n");
            sbProjection.append("    UNIT[\"degree\",0.01745329251994328, ").append("\n");
            sbProjection.append("        AUTHORITY[\"EPSG\",\"9122\"]], ").append("\n");
            sbProjection.append("    AUTHORITY[\"EPSG\",\"4326\"]] ").append("\n");
            spWriter.write(sbProjection.toString());
            spWriter.close();
        } catch (IOException ex) {
            System.out.println("error writing species file:");
            ex.printStackTrace(System.out);
        }
    }

    public static void convertUTMToDD(double easting, double northing, int zone, boolean isSouth) {
        double a = 6378137.0;
        double f = 1 / 298.2572236;
        double k = 1;
        double drad = Math.PI / 180;
        System.out.println("a: " + a);
        System.out.println("f: " + f);
        System.out.println("k: " + k);
        System.out.println("d: " + drad);
        double k0 = 0.9996;
        double b = a * (1 - f);
        double e = Math.sqrt(1 - (b / a) * (b / a));
        double e0 = e / Math.sqrt(1 - e * e);
        double esq = (1 - (b / a) * (b / a));
        double e0sq = e * e / (1 - e * e);
        double x = easting;
        if (x < 160000 || x > 840000) {
            System.out.println("Outside permissible range of easting values \n Results may be unreliable \n Use with caution");
        }
        double y = northing;
        if (y < 0) {
            System.out.println("Negative values not allowed \n Results may be unreliable \n Use with caution");
        }
        if (y > 10000000) {
            System.out.println("Northing may not exceed 10,000,000 \n Results may be unreliable \n Use with caution");
        }
        double utmz = zone;
        double zcm = 3 + 6 * (utmz - 1) - 180;
        double e1 = (1 - Math.sqrt(1 - e * e)) / (1 + Math.sqrt(1 - e * e));
        double M0 = 0;
        double M = M0 + y / k0;
        if (isSouth) {
            M = M0 + (y - 10000000) / k;
        }
        double mu = -0.7357368535830084;
        double phi1 = mu + e1 * (3 / 2 - 27 * e1 * e1 / 32) * Math.sin(2 * mu) + e1 * e1 * (21 / 16 - 55 * e1 * e1 / 32) * Math.sin(4 * mu);
        phi1 = phi1 + e1 * e1 * e1 * (Math.sin(6 * mu) * 151 / 96 + e1 * Math.sin(8 * mu) * 1097 / 512);
        double C1 = e0sq * Math.pow(Math.cos(phi1), 2);
        double T1 = Math.pow(Math.tan(phi1), 2);
        double N1 = a / Math.sqrt(1 - Math.pow(e * Math.sin(phi1), 2));
        double R1 = N1 * (1 - e * e) / (1 - Math.pow(e * Math.sin(phi1), 2));
        double D = (x - 500000) / (N1 * k0);
        double phi = (D * D) * (1 / 2 - D * D * (5 + 3 * T1 + 10 * C1 - 4 * C1 * C1 - 9 * e0sq) / 24);
        phi = phi + Math.pow(D, 6) * (61 + 90 * T1 + 298 * C1 + 45 * T1 * T1 - 252 * e0sq - 3 * C1 * C1) / 720;
        phi = phi1 - (N1 * Math.tan(phi1) / R1) * phi;
        double lat = Math.floor(1000000 * phi / drad) / 1000000;
        double lngtmp = D * (1 + D * D * ((-1 - 2 * T1 - C1) / 6 + D * D * (5 - 2 * C1 + 28 * T1 - 3 * C1 * C1 + 8 * e0sq + 24 * T1 * T1) / 120)) / Math.cos(phi1);
        double lngd = zcm + lngtmp / drad;
        double lng = Math.floor(1000000 * lngd) / 1000000;
        System.out.println("M: " + M);
        System.out.println("esq: " + esq);
        System.out.println("mu: " + mu);
        System.out.println("MU: " + (M / (a * (1 - esq * (1 / 4 + esq * (3 / 64 + 5 * esq / 256))))));
        System.out.println("mmuu: " + (-4684767.0 / (6378137.0 * (1 - 0.006694379989312105 * (1 / 4 + 0.006694379989312105 * (3 / 64 + 5 * 0.006694379989312105 / 256))))));
        System.out.println("Latitude, Longitude: " + lat + "," + lng);
    }

    public static void main(String[] args) {
        try {
            convertUTMToDD(444062, 5315233, 55, true);
        } catch (Exception e) {
            System.out.println("Opps");
            e.printStackTrace(System.out);
        }
    }
}
