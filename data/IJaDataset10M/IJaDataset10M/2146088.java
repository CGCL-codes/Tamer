package mexif.TagDB;

public class ExifMetaTagDB_GPS extends ExifMetaTagDB {

    public final int _MEXIF_TAGID_GPS_Count = 31;

    public final int MEXIF_TAGID_GPS_GPSVersionID = 0;

    public final int MEXIF_TAGID_GPS_GPSLatitudeRef = 1;

    public final int MEXIF_TAGID_GPS_GPSLatitude = 2;

    public final int MEXIF_TAGID_GPS_GPSLongitudeRef = 3;

    public final int MEXIF_TAGID_GPS_GPSLongitude = 4;

    public final int MEXIF_TAGID_GPS_GPSAltitudeRef = 5;

    public final int MEXIF_TAGID_GPS_GPSAltitude = 6;

    public final int MEXIF_TAGID_GPS_GPSTimeStamp = 7;

    public final int MEXIF_TAGID_GPS_GPSSatellites = 8;

    public final int MEXIF_TAGID_GPS_GPSStatus = 9;

    public final int MEXIF_TAGID_GPS_GPSMeasureMode = 10;

    public final int MEXIF_TAGID_GPS_GPSDOP = 11;

    public final int MEXIF_TAGID_GPS_GPSSpeedRef = 12;

    public final int MEXIF_TAGID_GPS_GPSSpeed = 13;

    public final int MEXIF_TAGID_GPS_GPSTrackRef = 14;

    public final int MEXIF_TAGID_GPS_GPSTrack = 15;

    public final int MEXIF_TAGID_GPS_GPSImgDirectionRef = 16;

    public final int MEXIF_TAGID_GPS_GPSImgDirection = 17;

    public final int MEXIF_TAGID_GPS_GPSMapDatum = 18;

    public final int MEXIF_TAGID_GPS_GPSDestLatitudeRef = 19;

    public final int MEXIF_TAGID_GPS_GPSDestLatitude = 20;

    public final int MEXIF_TAGID_GPS_GPSDestLongitudeRef = 21;

    public final int MEXIF_TAGID_GPS_GPSDestLongitude = 22;

    public final int MEXIF_TAGID_GPS_GPSDestBearingRef = 23;

    public final int MEXIF_TAGID_GPS_GPSDestBearing = 24;

    public final int MEXIF_TAGID_GPS_GPSDestDistanceRef = 25;

    public final int MEXIF_TAGID_GPS_GPSDestDistance = 26;

    public final int MEXIF_TAGID_GPS_GPSProcessingMethod = 27;

    public final int MEXIF_TAGID_GPS_GPSAreaInformation = 28;

    public final int MEXIF_TAGID_GPS_GPSDateStamp = 29;

    public final int MEXIF_TAGID_GPS_GPSDifferential = 30;

    public ExifMetaTagDB_GPS() {
        mDB = new ExifTagDB[_MEXIF_TAGID_GPS_Count];
        mDB[0] = new ExifTagDB(0, "GPSVersionID", "GPS tag version");
        mDB[1] = new ExifTagDB(1, "GPSLatitudeRef", "North or South Latitude");
        mDB[2] = new ExifTagDB(2, "GPSLatitude", "Latitude");
        mDB[3] = new ExifTagDB(3, "GPSLongitudeRef", "East or West Longitude");
        mDB[4] = new ExifTagDB(4, "GPSLongitude", "Longitude");
        mDB[5] = new ExifTagDB(5, "GPSAltitudeRef", "Altitude reference");
        mDB[6] = new ExifTagDB(6, "GPSAltitude", "Altitude");
        mDB[7] = new ExifTagDB(7, "GPSTimeStamp", "GPS time (atomic clock)");
        mDB[8] = new ExifTagDB(8, "GPSSatellites", "GPS satellites used for measurement");
        mDB[9] = new ExifTagDB(9, "GPSStatus", "GPS receiver status");
        mDB[10] = new ExifTagDB(10, "GPSMeasureMode", "GPS measurement mode");
        mDB[11] = new ExifTagDB(11, "GPSDOP", "Measurement precision");
        mDB[12] = new ExifTagDB(12, "GPSSpeedRef", "Speed unit");
        mDB[13] = new ExifTagDB(13, "GPSSpeed", "Speed of GPS receiver");
        mDB[14] = new ExifTagDB(14, "GPSTrackRef", "Reference for direction of movement");
        mDB[15] = new ExifTagDB(15, "GPSTrack", "Direction of movement");
        mDB[16] = new ExifTagDB(16, "GPSImgDirectionRef", "Reference for direction of image");
        mDB[17] = new ExifTagDB(17, "GPSImgDirection", "Direction of image");
        mDB[18] = new ExifTagDB(18, "GPSMapDatum", "Geodetic survey data used");
        mDB[19] = new ExifTagDB(19, "GPSDestLatitudeRef", "Reference for latitude of destination");
        mDB[20] = new ExifTagDB(20, "GPSDestLatitude", "Latitude of destination");
        mDB[21] = new ExifTagDB(21, "GPSDestLongitudeRef", "Reference for longitude of destination");
        mDB[22] = new ExifTagDB(22, "GPSDestLongitude", "Longitude of destination");
        mDB[23] = new ExifTagDB(23, "GPSDestBearingRef", "Reference for bearing of destination");
        mDB[24] = new ExifTagDB(24, "GPSDestBearing", "Bearing of destination");
        mDB[25] = new ExifTagDB(25, "GPSDestDistanceRef", "Reference for distance to destination");
        mDB[26] = new ExifTagDB(26, "GPSDestDistance", "Distance to destination");
        mDB[27] = new ExifTagDB(27, "GPSProcessingMethod", "Name of GPS processing method");
        mDB[28] = new ExifTagDB(28, "GPSAreaInformation", "Name of GPS area");
        mDB[29] = new ExifTagDB(29, "GPSDateStamp", "GPS date");
        mDB[30] = new ExifTagDB(30, "GPSDifferential", "GPS differential correction");
    }

    @Override
    public ExifTagDB searchDB(int tag) {
        switch(tag) {
            case MEXIF_TAGID_GPS_GPSVersionID:
                return mDB[0];
            case MEXIF_TAGID_GPS_GPSLatitudeRef:
                return mDB[1];
            case MEXIF_TAGID_GPS_GPSLatitude:
                return mDB[2];
            case MEXIF_TAGID_GPS_GPSLongitudeRef:
                return mDB[3];
            case MEXIF_TAGID_GPS_GPSLongitude:
                return mDB[4];
            case MEXIF_TAGID_GPS_GPSAltitudeRef:
                return mDB[5];
            case MEXIF_TAGID_GPS_GPSAltitude:
                return mDB[6];
            case MEXIF_TAGID_GPS_GPSTimeStamp:
                return mDB[7];
            case MEXIF_TAGID_GPS_GPSSatellites:
                return mDB[8];
            case MEXIF_TAGID_GPS_GPSStatus:
                return mDB[9];
            case MEXIF_TAGID_GPS_GPSMeasureMode:
                return mDB[10];
            case MEXIF_TAGID_GPS_GPSDOP:
                return mDB[11];
            case MEXIF_TAGID_GPS_GPSSpeedRef:
                return mDB[12];
            case MEXIF_TAGID_GPS_GPSSpeed:
                return mDB[13];
            case MEXIF_TAGID_GPS_GPSTrackRef:
                return mDB[14];
            case MEXIF_TAGID_GPS_GPSTrack:
                return mDB[15];
            case MEXIF_TAGID_GPS_GPSImgDirectionRef:
                return mDB[16];
            case MEXIF_TAGID_GPS_GPSImgDirection:
                return mDB[17];
            case MEXIF_TAGID_GPS_GPSMapDatum:
                return mDB[18];
            case MEXIF_TAGID_GPS_GPSDestLatitudeRef:
                return mDB[19];
            case MEXIF_TAGID_GPS_GPSDestLatitude:
                return mDB[20];
            case MEXIF_TAGID_GPS_GPSDestLongitudeRef:
                return mDB[21];
            case MEXIF_TAGID_GPS_GPSDestLongitude:
                return mDB[22];
            case MEXIF_TAGID_GPS_GPSDestBearingRef:
                return mDB[23];
            case MEXIF_TAGID_GPS_GPSDestBearing:
                return mDB[24];
            case MEXIF_TAGID_GPS_GPSDestDistanceRef:
                return mDB[25];
            case MEXIF_TAGID_GPS_GPSDestDistance:
                return mDB[26];
            case MEXIF_TAGID_GPS_GPSProcessingMethod:
                return mDB[27];
            case MEXIF_TAGID_GPS_GPSAreaInformation:
                return mDB[28];
            case MEXIF_TAGID_GPS_GPSDateStamp:
                return mDB[29];
            case MEXIF_TAGID_GPS_GPSDifferential:
                return mDB[30];
        }
        return null;
    }
}
