package com.mymaps.geocode;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.vividsolutions.jts.geom.Coordinate;

/**
 * CityGeoCoder utilizes http://brainoff.com to
 * retrieve coordinates for City/State/Country 
 * combinations.  
 * 
 * @author todd
 *
 */
public class CityGeoCoder implements GeoCoder {

    private static final Log logger = LogFactory.getLog(CityGeoCoder.class);

    private String mainUrl = "brainoff.com/geocoder/rest";

    private String error = null;

    private Coordinate coordinate = null;

    private HttpClient client = null;

    private HttpMethod method = null;

    private int responseCode = -1;

    private InputStream response = null;

    private static final String KEY_CITY = GeoCodeParam.KEY_CODE_CITY;

    private static final String KEY_STATE = GeoCodeParam.KEY_CODE_STATE;

    private static final String KEY_COUNTRY = GeoCodeParam.KEY_CODE_COUNTRY;

    public CityGeoCoder() {
        super();
    }

    private void getGeocode(String city, String state, String country) {
        StringBuffer urlString = new StringBuffer();
        urlString.append("http://");
        urlString.append(mainUrl);
        urlString.append("?city=");
        urlString.append(createCityString(city, state, country));
        client = new HttpClient();
        method = new GetMethod(urlString.toString());
        try {
            responseCode = client.executeMethod(method);
            response = method.getResponseBodyAsStream();
        } catch (HttpException e) {
            logger.error(e.getMessage(), new Throwable(e));
        } catch (IOException e) {
            logger.error(e.getMessage(), new Throwable(e));
        }
    }

    private void closeConnection() {
        if (response != null) {
            try {
                response.close();
            } catch (IOException e) {
            }
        }
        if (method != null) {
            try {
                method.releaseConnection();
            } catch (Exception e) {
            }
        }
        response = null;
        method = null;
        client = null;
    }

    private String createCityString(String city, String state, String country) {
        StringBuffer retVal = new StringBuffer();
        retVal.append(ResponseParser.cleanValue(city));
        retVal.append(",");
        if (state != null && state.trim().length() > 0) {
            retVal.append(ResponseParser.cleanValue(state));
            if (country != null && country.trim().length() > 0) {
                retVal.append(",");
            }
        }
        retVal.append(ResponseParser.cleanValue(country));
        return retVal.toString();
    }

    public int getResponseCode() {
        return responseCode;
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        CityGeoCoder coder = new CityGeoCoder();
        System.out.println("Working = " + coder.isServiceAvailable());
        System.out.println("Done");
    }

    public boolean isServiceAvailable() {
        boolean retVal = false;
        GeoCodeParam[] params = getParams();
        GeoCodeParam.setValue(params, KEY_CITY, "Portland");
        GeoCodeParam.setValue(params, KEY_STATE, "OR");
        GeoCodeParam.setValue(params, KEY_COUNTRY, "US");
        callService(params);
        retVal = coordinate != null;
        error = null;
        coordinate = null;
        return retVal;
    }

    public GeoCodeParam[] getParams() {
        GeoCodeParam[] retVal = new GeoCodeParam[3];
        GeoCodeParam city = new GeoCodeParam(KEY_CITY, "City name", true);
        GeoCodeParam state = new GeoCodeParam(KEY_STATE, "State name, if in the US", false);
        GeoCodeParam country = new GeoCodeParam(KEY_COUNTRY, "2 digit country code (such as US,UK,etc.)", false);
        retVal[0] = city;
        retVal[1] = state;
        retVal[2] = country;
        return retVal;
    }

    public void callService(GeoCodeParam[] params) {
        coordinate = null;
        error = null;
        String city = GeoCodeParam.getValue(params, KEY_CITY);
        String state = GeoCodeParam.getValue(params, KEY_STATE);
        String country = GeoCodeParam.getValue(params, KEY_COUNTRY);
        if ((city == null) && (state == null && country == null)) {
            error = "Not enough information provided to get coordinates";
            return;
        }
        if (country == null) {
            country = "US";
        }
        try {
            getGeocode(city, state, country);
            Coordinate c = ResponseParser.parseCoordinate(response);
            if (c != null) {
                coordinate = c;
            } else {
                error = "Couldn't geocode " + createCityString(city, state, country);
            }
        } finally {
            closeConnection();
        }
    }

    public String getError() {
        return error;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }
}
