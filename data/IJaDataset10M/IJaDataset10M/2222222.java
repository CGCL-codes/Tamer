package com.ynhenc.gis.projection;

import com.ynhenc.gis.model.shape.PntShort;

public class WgsToUtm extends CoordinateConversion {

    public static void main(String[] args) {
        WgsToUtm c = new WgsToUtm();
        double lonx = 126.90789856572154;
        double laty = 37.46332335838327;
        Wgs wgs = Wgs.wgs(lonx, laty);
        System.out.println(wgs);
        String res = c.convertLatLonToUTM(wgs);
        System.out.println(res);
    }

    private String convertLatLonToUTM(Wgs wgs) {
        String longZone = this.getLongZone(wgs);
        LatZoneList latZoneList = new LatZoneList();
        String latZone = latZoneList.getLatZone(wgs);
        PntShort utm = this.getConvertValue(wgs);
        String utmWithZone = longZone + " " + latZone + " " + utm.getX() + " " + utm.getY();
        return utmWithZone;
    }

    @Override
    public PntShort getConvertValue(PntShort wgs) {
        this.validate(wgs);
        this.setVariables(wgs);
        return Utm.utm(this.getEasting(), this.getNorthing(wgs));
    }

    public void setVariables(PntShort wgs) {
        double laty = wgs.getY();
        laty = this.degreeToRadian(laty);
        this.rho = this.equatorialRadius * (1 - this.e * this.e) / this.POW(1 - this.POW(this.e * this.SIN(laty), 2), 3 / 2.0);
        this.nu = this.equatorialRadius / this.POW(1 - this.POW(this.e * this.SIN(laty), 2), (1 / 2.0));
        double var1;
        double lonx = wgs.getX();
        if (lonx < 0.0) {
            var1 = ((int) ((180 + lonx) / 6.0)) + 1;
        } else {
            var1 = ((int) (lonx / 6)) + 31;
        }
        double var2 = (6 * var1) - 183;
        double var3 = lonx - var2;
        this.p = var3 * 3600 / 10000;
        this.S = this.A0 * laty - this.B0 * this.SIN(2 * laty) + this.C0 * this.SIN(4 * laty) - this.D0 * this.SIN(6 * laty) + this.E0 * this.SIN(8 * laty);
        this.K1 = this.S * this.k0;
        this.K2 = this.nu * this.SIN(laty) * this.COS(laty) * this.POW(this.sin1, 2) * this.k0 * (100000000) / 2;
        this.K3 = ((this.POW(this.sin1, 4) * this.nu * this.SIN(laty) * Math.pow(this.COS(laty), 3)) / 24) * (5 - this.POW(this.TAN(laty), 2) + 9 * this.e1sq * this.POW(this.COS(laty), 2) + 4 * this.POW(this.e1sq, 2) * this.POW(this.COS(laty), 4)) * this.k0 * (10000000000000000L);
        this.K4 = this.nu * this.COS(laty) * this.sin1 * this.k0 * 10000;
        this.K5 = this.POW(this.sin1 * this.COS(laty), 3) * (this.nu / 6) * (1 - this.POW(this.TAN(laty), 2) + this.e1sq * this.POW(this.COS(laty), 2)) * this.k0 * 1000000000000L;
        this.A6 = (this.POW(this.p * this.sin1, 6) * this.nu * this.SIN(laty) * this.POW(this.COS(laty), 5) / 720) * (61 - 58 * this.POW(this.TAN(laty), 2) + this.POW(this.TAN(laty), 4) + 270 * this.e1sq * this.POW(this.COS(laty), 2) - 330 * this.e1sq * this.POW(this.SIN(laty), 2)) * this.k0 * (1E+24);
    }

    protected String getLongZone(Wgs wgs) {
        double lonx = wgs.getX();
        double longZone = 0;
        if (lonx < 0.0) {
            longZone = ((180.0 + lonx) / 6) + 1;
        } else {
            longZone = (lonx / 6) + 31;
        }
        String val = String.valueOf((int) longZone);
        if (val.length() == 1) {
            val = "0" + val;
        }
        return val;
    }

    protected double getNorthing(PntShort wgs) {
        double northing = this.K1 + this.K2 * this.p * this.p + this.K3 * this.POW(this.p, 4);
        if (wgs.getY() < 0.0) {
            northing = 10000000 + northing;
        }
        return northing;
    }

    protected double getEasting() {
        return 500000 + (this.K4 * this.p + this.K5 * this.POW(this.p, 3));
    }

    @Override
    public String getCoordinateSystemName() {
        return "WGS84->UTM";
    }

    double equatorialRadius = 6378137;

    double polarRadius = 6356752.314;

    double flattening = 0.00335281066474748;

    double inverseFlattening = 298.257223563;

    double rm = this.POW(this.equatorialRadius * this.polarRadius, 1 / 2.0);

    double k0 = 0.9996;

    double e = Math.sqrt(1 - this.POW(this.polarRadius / this.equatorialRadius, 2));

    double e1sq = this.e * this.e / (1 - this.e * this.e);

    double n = (this.equatorialRadius - this.polarRadius) / (this.equatorialRadius + this.polarRadius);

    double rho = 6368573.744;

    double nu = 6389236.914;

    double S = 5103266.421;

    double A0 = 6367449.146;

    double B0 = 16038.42955;

    double C0 = 16.83261333;

    double D0 = 0.021984404;

    double E0 = 0.000312705;

    double p = -0.483084;

    double sin1 = 4.84814E-06;

    double K1 = 5101225.115;

    double K2 = 3750.291596;

    double K3 = 1.397608151;

    double K4 = 214839.3105;

    double K5 = -2.995382942;

    double A6 = -1.00541E-07;
}
