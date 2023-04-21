package CB_Core.Converter;

public class UTMConvert {

    final double dCvtDeg2Rad = Math.PI / 180.0;

    final double dCvtRad2Deg = 180.0 / Math.PI;

    final double eccSquared = 0.00669438;

    final double dEquatorialRadius = 6378137.0;

    final double dScaleFactor = 0.9996;

    public double UTMNorthing = 0;

    public double UTMEasting = 0;

    public String sUtmZone = "";

    public int iLatLon2UTM(double dLat, double dLon) {
        double dLatRad = dLat * dCvtDeg2Rad;
        double dLonWork = (dLon + 180) - ((int) ((dLon + 180) / 360)) * 360 - 180;
        double dLonRad = dLonWork * dCvtDeg2Rad;
        int iUTM_Zone_Num = this.iGetUtmZone(dLat, dLonWork);
        sUtmZone = String.valueOf(iUTM_Zone_Num) + sUtmLetterActual(dLat);
        double dCentralMeridian = dSet_CentralMeridian_from_UtmZone(iUTM_Zone_Num);
        double dCentralMeridian_Rad = dCentralMeridian * dCvtDeg2Rad;
        double eccPrimeSquared = eccSquared / (1 - eccSquared);
        double N = dEquatorialRadius / Math.sqrt(1 - eccSquared * Math.sin(dLatRad) * Math.sin(dLatRad));
        double T = Math.tan(dLatRad) * Math.tan(dLatRad);
        double C = eccPrimeSquared * Math.cos(dLatRad) * Math.cos(dLatRad);
        double A = Math.cos(dLatRad) * (dLonRad - dCentralMeridian_Rad);
        double M = dEquatorialRadius * ((1 - eccSquared / 4 - 3 * eccSquared * eccSquared / 64 - 5 * eccSquared * eccSquared * eccSquared / 256) * dLatRad - (3 * eccSquared / 8 + 3 * eccSquared * eccSquared / 32 + 45 * eccSquared * eccSquared * eccSquared / 1024) * Math.sin(2 * dLatRad) + (15 * eccSquared * eccSquared / 256 + 45 * eccSquared * eccSquared * eccSquared / 1024) * Math.sin(4 * dLatRad) - (35 * eccSquared * eccSquared * eccSquared / 3072) * Math.sin(6 * dLatRad));
        UTMEasting = (double) (dScaleFactor * N * (A + (1 - T + C) * A * A * A / 6 + (5 - 18 * T + T * T + 72 * C - 58 * eccPrimeSquared) * A * A * A * A * A / 120) + 500000.0);
        UTMNorthing = (double) (dScaleFactor * (M + N * Math.tan(dLatRad) * (A * A / 2 + (5 - T + 9 * C + 4 * C * C) * A * A * A * A / 24 + (61 - 58 * T + T * T + 600 * C - 330 * eccPrimeSquared) * A * A * A * A * A * A / 720)));
        if (dLat < 0) UTMNorthing += 10000000.0;
        return (0);
    }

    public double dLat = 0;

    public double dLon = 0;

    public int iUTM2LatLon(double UTMNorthing, double UTMEasting, String sUTMZone) {
        char cZoneLetter = sUTMZone.charAt(sUTMZone.length() - 1);
        boolean bNorthernHemisphere = (cZoneLetter >= 'N');
        String sZoneNum = sUTMZone.substring(0, sUTMZone.length() - 1);
        int iZoneNumber = Integer.valueOf(sZoneNum);
        double x = UTMEasting - 500000.0;
        double y = UTMNorthing;
        if (!bNorthernHemisphere) y -= 10000000.0;
        double dLongOrigin = (iZoneNumber - 1) * 6 - 180 + 3;
        double eccPrimeSquared = (eccSquared) / (1 - eccSquared);
        double M = y / dScaleFactor;
        double mu = M / (dEquatorialRadius * (1 - eccSquared / 4 - 3 * eccSquared * eccSquared / 64 - 5 * eccSquared * eccSquared * eccSquared / 256));
        double e1 = (1 - Math.sqrt(1 - eccSquared)) / (1 + Math.sqrt(1 - eccSquared));
        double phi1Rad = mu + (3 * e1 / 2 - 27 * e1 * e1 * e1 / 32) * Math.sin(2 * mu) + (21 * e1 * e1 / 16 - 55 * e1 * e1 * e1 * e1 / 32) * Math.sin(4 * mu) + (151 * e1 * e1 * e1 / 96) * Math.sin(6 * mu);
        double N1 = dEquatorialRadius / Math.sqrt(1 - eccSquared * Math.sin(phi1Rad) * Math.sin(phi1Rad));
        double T1 = Math.tan(phi1Rad) * Math.tan(phi1Rad);
        double C1 = eccPrimeSquared * Math.cos(phi1Rad) * Math.cos(phi1Rad);
        double R1 = dEquatorialRadius * (1 - eccSquared) / Math.pow(1 - eccSquared * Math.sin(phi1Rad) * Math.sin(phi1Rad), 1.5);
        double D = x / (N1 * dScaleFactor);
        dLat = phi1Rad - (N1 * Math.tan(phi1Rad) / R1) * (D * D / 2 - (5 + 3 * T1 + 10 * C1 - 4 * C1 * C1 - 9 * eccPrimeSquared) * D * D * D * D / 24 + (61 + 90 * T1 + 298 * C1 + 45 * T1 * T1 - 252 * eccPrimeSquared - 3 * C1 * C1) * D * D * D * D * D * D / 720);
        dLat = dLat * dCvtRad2Deg;
        dLon = (D - (1 + 2 * T1 + C1) * D * D * D / 6 + (5 - 2 * C1 + 28 * T1 - 3 * C1 * C1 + 8 * eccPrimeSquared + 24 * T1 * T1) * D * D * D * D * D / 120) / Math.cos(phi1Rad);
        dLon = dLongOrigin + dLon * dCvtRad2Deg;
        return (0);
    }

    public int iGetUtmZone(double dLat, double dLon) {
        if ((dLon < -180.0) || (dLon > 180.0)) return (0);
        int iUTM_Zone_Num = (int) ((180.0 + dLon) / 6.0 + 1.0);
        if (dLat >= 56.0 && dLat < 64.0) {
            if (dLon >= 3.0 && dLon < 12.0) iUTM_Zone_Num = 32;
        } else if (dLat >= 72.0 && dLat < 84.0) {
            if (dLon >= 0.0 && dLon < 9.0) iUTM_Zone_Num = 31; else if (dLon >= 9.0 && dLon < 21.0) iUTM_Zone_Num = 33; else if (dLon >= 21.0 && dLon < 33.0) iUTM_Zone_Num = 35; else if (dLon >= 33.0 && dLon < 42.0) iUTM_Zone_Num = 37;
        }
        return (iUTM_Zone_Num);
    }

    private String sUtmLetterActual(double Lat) {
        char cLetterDesignator = 'Z';
        if ((84 >= Lat) && (Lat >= 72)) cLetterDesignator = 'X'; else if ((72 > Lat) && (Lat >= 64)) cLetterDesignator = 'W'; else if ((64 > Lat) && (Lat >= 56)) cLetterDesignator = 'V'; else if ((56 > Lat) && (Lat >= 48)) cLetterDesignator = 'U'; else if ((48 > Lat) && (Lat >= 40)) cLetterDesignator = 'T'; else if ((40 > Lat) && (Lat >= 32)) cLetterDesignator = 'S'; else if ((32 > Lat) && (Lat >= 24)) cLetterDesignator = 'R'; else if ((24 > Lat) && (Lat >= 16)) cLetterDesignator = 'Q'; else if ((16 > Lat) && (Lat >= 8)) cLetterDesignator = 'P'; else if ((8 > Lat) && (Lat >= 0)) cLetterDesignator = 'N'; else if ((0 > Lat) && (Lat >= -8)) cLetterDesignator = 'M'; else if ((-8 > Lat) && (Lat >= -16)) cLetterDesignator = 'L'; else if ((-16 > Lat) && (Lat >= -24)) cLetterDesignator = 'K'; else if ((-24 > Lat) && (Lat >= -32)) cLetterDesignator = 'J'; else if ((-32 > Lat) && (Lat >= -40)) cLetterDesignator = 'H'; else if ((-40 > Lat) && (Lat >= -48)) cLetterDesignator = 'G'; else if ((-48 > Lat) && (Lat >= -56)) cLetterDesignator = 'F'; else if ((-56 > Lat) && (Lat >= -64)) cLetterDesignator = 'E'; else if ((-64 > Lat) && (Lat >= -72)) cLetterDesignator = 'D'; else if ((-72 > Lat) && (Lat >= -80)) cLetterDesignator = 'C';
        return String.valueOf(cLetterDesignator);
    }

    public double dSet_CentralMeridian_from_UtmZone(int iZoneNum) {
        double dCentralMeridian = (183.0 - (6.0 * (double) iZoneNum)) * -1.0;
        return (dCentralMeridian);
    }
}
