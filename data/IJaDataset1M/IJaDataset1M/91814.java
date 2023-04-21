package datalog.coord;

import java.io.File;
import java.io.IOException;
import java.nio.ByteOrder;
import javax.imageio.stream.FileImageInputStream;

public class Egm96 extends Geoid {

    public Egm96(String gfn) {
        super(gfn);
        readBinaryGeoidFile(gfn);
    }

    private static double bilinear(double x1, double y1, double x2, double y2, double x, double y, double z11, double z12, double z21, double z22) {
        double delta;
        if (y1 == y2 && x1 == x2) {
            return (z11);
        }
        if (y1 == y2 && x1 != x2) {
            return (z22 * (x - x1) + z11 * (x2 - x)) / (x2 - x1);
        }
        if (x1 == x2 && y1 != y2) {
            return (z22 * (y - y1) + z11 * (y2 - y)) / (y2 - y1);
        }
        delta = (y2 - y1) * (x2 - x1);
        return (z22 * (y - y1) * (x - x1) + z12 * (y2 - y) * (x - x1) + z21 * (y - y1) * (x2 - x) + z11 * (y2 - y) * (x2 - x)) / delta;
    }

    public double separation(double lat, double lon) {
        int ilat, ilon;
        int ilat1, ilat2, ilon1, ilon2;
        if (((lat > latN) || (lat < latS)) || ((lon < lonE) || (lon > lonO))) {
            return .0d;
        }
        ilat = (int) Math.round(((latN - lat) / iLat));
        ilon = (int) Math.round(((lon) / iLon)) - 1;
        ilat1 = ilat;
        ilon1 = ilon;
        ilat2 = (ilat < this.GEOID_ROW - 1) ? ilat + 1 : ilat;
        ilon2 = (ilon < this.GEOID_COL - 1) ? ilon + 1 : ilon;
        double a = 0.0, b = 0.0, c = 0.0, d = 0.0;
        try {
            a = (geoid_delta[ilat1][ilon1] / facteur);
            b = (geoid_delta[ilat1][ilon2] / facteur);
            c = (geoid_delta[ilat2][ilon1] / facteur);
            d = (geoid_delta[ilat2][ilon2] / facteur);
        } catch (Exception e) {
            return .0d;
        }
        return bilinear(ilon1 * iLon, latN - ilat1 * iLat, ilon2 * iLon, latN - ilat2 * iLat, lon, lat, a, b, c, d);
    }

    private void readBinaryGeoidFile(String gfn) {
        char[] name = new char[31];
        File fichier = new File(gfn);
        try {
            FileImageInputStream fip = new FileImageInputStream(fichier);
            fip.setByteOrder(ByteOrder.BIG_ENDIAN);
            latS = -90.000000;
            latN = 90.000000;
            lonO = 360.000000;
            lonE = .000000;
            iLat = .250000;
            iLon = .250000;
            facteur = 100;
            GEOID_ROW = (int) Math.round(((latN - latS) / iLat));
            GEOID_COL = (int) Math.round(((lonO - lonE) / iLon));
            geoid_delta = new int[GEOID_ROW][GEOID_COL];
            for (int i = 0; i < GEOID_ROW; i++) {
                for (int j = 0; j < GEOID_COL; j++) {
                    geoid_delta[i][j] = fip.readShort();
                }
            }
        } catch (IOException ex) {
        }
        return;
    }

    public static void main(String[] args) {
        Egm96 e = new Egm96("geoid\\WW15MGH.DAC");
        Raf98 r = new Raf98("geoid\\RAF98.BIN");
        double h1 = e.separation(45, 4);
        double h2 = r.separation(45, 4);
        System.out.println("45, 4 : " + h1 + "/ " + h2);
    }
}
