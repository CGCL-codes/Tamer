package codebarre.com.google.zxing.client.result;

/**
 * @author Sean Owen
 */
public final class GeoParsedResult extends ParsedResult {

    private final double latitude;

    private final double longitude;

    private final double altitude;

    private final String query;

    GeoParsedResult(double latitude, double longitude, double altitude, String query) {
        super(ParsedResultType.GEO);
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.query = query;
    }

    public String getGeoURI() {
        StringBuffer result = new StringBuffer();
        result.append("geo:");
        result.append(latitude);
        result.append(',');
        result.append(longitude);
        if (altitude > 0) {
            result.append(',');
            result.append(altitude);
        }
        if (query != null) {
            result.append('?');
            result.append(query);
        }
        return result.toString();
    }

    /**
   * @return latitude in degrees
   */
    public double getLatitude() {
        return latitude;
    }

    /**
   * @return longitude in degrees
   */
    public double getLongitude() {
        return longitude;
    }

    /**
   * @return altitude in meters. If not specified, in the geo URI, returns 0.0
   */
    public double getAltitude() {
        return altitude;
    }

    /**
   * @return query string associated with geo URI or null if none exists
   */
    public String getQuery() {
        return query;
    }

    public String getDisplayResult() {
        StringBuffer result = new StringBuffer(20);
        result.append(latitude);
        result.append(", ");
        result.append(longitude);
        if (altitude > 0.0) {
            result.append(", ");
            result.append(altitude);
            result.append('m');
        }
        if (query != null) {
            result.append(" (");
            result.append(query);
            result.append(')');
        }
        return result.toString();
    }
}
