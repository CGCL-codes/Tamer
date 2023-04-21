package com.bing.maps.rest.services.constant;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.bing.maps.rest.schema.ValueEnum;

/**
 * The Class GoogleMapsApiUrls.
 */
public final class BingMapsApiUrls {

    /** The Constant API_URLS_FILE. */
    public static final String API_URLS_FILE = "GoogleMapsApiUrls.properties";

    /** The Constant LOG. */
    private static final Logger LOG = Logger.getLogger(BingMapsApiUrls.class.getCanonicalName());

    /** The Constant googleApiUrls. */
    private static final Properties googleApiUrls = new Properties();

    static {
        try {
            googleApiUrls.load(BingMapsApiUrls.class.getResourceAsStream(API_URLS_FILE));
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "An error occurred while loading urls.", e);
        }
    }

    /** The Constant GEOCODE_URL. */
    public static final String GEOCODE_URL = googleApiUrls.getProperty("com.googleapis.maps.services.geocode");

    /** The Constant DIRECTIONS_URL. */
    public static final String DIRECTIONS_URL = googleApiUrls.getProperty("com.googleapis.maps.services.directions");

    /** The Constant ELEVATION_URL. */
    public static final String ELEVATION_URL = googleApiUrls.getProperty("com.googleapis.maps.services.elevation");

    /** The Constant PLACE_URL. */
    public static final String PLACE_URL = googleApiUrls.getProperty("com.googleapis.maps.services.place");

    /**
     * Instantiates a new google maps api urls.
     */
    private BingMapsApiUrls() {
    }

    /**
     * The Class GoogleMapsApiUrlBuilder.
     */
    public static class GoogleMapsApiUrlBuilder {

        /** The Constant API_URLS_PLACEHOLDER_START. */
        private static final char API_URLS_PLACEHOLDER_START = '{';

        /** The Constant API_URLS_PLACEHOLDER_END. */
        private static final char API_URLS_PLACEHOLDER_END = '}';

        /** The Constant QUERY_PARAMETERS_PLACEHOLDER. */
        private static final String QUERY_PARAMETERS_PLACEHOLDER = "queryParameters";

        /** The url format. */
        private String urlFormat;

        /** The parameters map. */
        private Map<String, Collection<String>> parametersMap = new HashMap<String, Collection<String>>();

        /**
	     * Instantiates a new google maps api url builder.
	     * 
	     * @param urlFormat the url format
	     */
        public GoogleMapsApiUrlBuilder(String urlFormat) {
            this(urlFormat, ApplicationConstants.DEFAULT_API_VERSION);
        }

        /**
	     * Instantiates a new google maps api url builder.
	     * 
	     * @param urlFormat the url format
	     * @param apiVersion the api version
	     */
        public GoogleMapsApiUrlBuilder(String urlFormat, String apiVersion) {
            this.urlFormat = urlFormat;
        }

        /**
	     * With parameter.
	     * 
	     * @param name the name
	     * @param value the value
	     * 
	     * @return the google maps api url builder
	     */
        public GoogleMapsApiUrlBuilder withParameter(String name, String value) {
            if (value != null && value.length() > 0) {
                Collection<String> values = parametersMap.get(name);
                if (values == null) {
                    values = new ArrayList<String>();
                    parametersMap.put(name, values);
                }
                values.add(encodeUrl(value));
            }
            return this;
        }

        /**
	     * With parameter suffix.
	     * 
	     * @param name the name
	     * @param suffix the suffix
	     * 
	     * @return the google maps api url builder
	     */
        public GoogleMapsApiUrlBuilder withParameterSuffix(String name, String suffix) {
            if (suffix != null && suffix.length() > 0) {
                Collection<String> values = parametersMap.get(name);
                if (values != null) {
                    List<String> updatedValues = new ArrayList<String>(values.size());
                    for (String value : values) {
                        updatedValues.add(encodeUrl(suffix) + value);
                    }
                    parametersMap.put(name, updatedValues);
                }
            }
            return this;
        }

        /**
	     * With parameters.
	     * 
	     * @param name the name
	     * @param values the values
	     * 
	     * @return the google maps api url builder
	     */
        public GoogleMapsApiUrlBuilder withParameters(String name, Collection<String> values) {
            List<String> encodedValues = new ArrayList<String>(values.size());
            for (String value : values) {
                encodedValues.add(encodeUrl(value));
            }
            parametersMap.put(name, encodedValues);
            return this;
        }

        /**
	     * With parameter enum set.
	     * 
	     * @param name the name
	     * @param enumSet the enum set
	     * 
	     * @return the google maps api url builder
	     */
        public GoogleMapsApiUrlBuilder withParameterEnumSet(String name, Set<? extends ValueEnum> enumSet) {
            Set<String> values = new HashSet<String>(enumSet.size());
            for (ValueEnum fieldEnum : enumSet) {
                values.add(encodeUrl(fieldEnum.value()));
            }
            parametersMap.put(name, values);
            return this;
        }

        /**
	     * With parameter enum.
	     * 
	     * @param name the name
	     * @param value the value
	     * 
	     * @return the google maps api url builder
	     */
        public GoogleMapsApiUrlBuilder withParameterEnum(String name, ValueEnum value) {
            withParameter(name, value.value());
            return this;
        }

        /**
	     * With parameter enum map.
	     * 
	     * @param enumMap the enum map
	     * 
	     * @return the google maps api url builder
	     */
        public GoogleMapsApiUrlBuilder withParameterEnumMap(Map<? extends ValueEnum, String> enumMap) {
            for (ValueEnum parameter : enumMap.keySet()) {
                withParameter(parameter.value(), enumMap.get(parameter));
            }
            return this;
        }

        /**
	     * Builds the url.
	     * 
	     * @return the string
	     */
        public String buildUrl() {
            StringBuilder urlBuilder = new StringBuilder();
            StringBuilder placeHolderBuilder = new StringBuilder();
            boolean placeHolderFlag = false;
            for (int i = 0; i < urlFormat.length(); i++) {
                if (urlFormat.charAt(i) == API_URLS_PLACEHOLDER_START) {
                    placeHolderBuilder = new StringBuilder();
                    placeHolderFlag = true;
                } else if (placeHolderFlag && urlFormat.charAt(i) == API_URLS_PLACEHOLDER_END) {
                    String placeHolder = placeHolderBuilder.toString();
                    if (QUERY_PARAMETERS_PLACEHOLDER.equals(placeHolder)) {
                        StringBuilder builder = new StringBuilder();
                        if (!parametersMap.isEmpty()) {
                            Iterator<String> iter = parametersMap.keySet().iterator();
                            while (iter.hasNext()) {
                                String name = iter.next();
                                Collection<String> parameterValues = parametersMap.get(name);
                                Iterator<String> iterParam = parameterValues.iterator();
                                while (iterParam.hasNext()) {
                                    builder.append(name);
                                    builder.append("=");
                                    builder.append(iterParam.next());
                                    if (iterParam.hasNext()) {
                                        builder.append("&");
                                    }
                                }
                                if (iter.hasNext()) {
                                    builder.append("&");
                                }
                            }
                        }
                        urlBuilder.append(builder.toString());
                    } else {
                        urlBuilder.append(API_URLS_PLACEHOLDER_START);
                        urlBuilder.append(placeHolder);
                        urlBuilder.append(API_URLS_PLACEHOLDER_END);
                    }
                    placeHolderFlag = false;
                } else if (placeHolderFlag) {
                    placeHolderBuilder.append(urlFormat.charAt(i));
                } else {
                    urlBuilder.append(urlFormat.charAt(i));
                }
            }
            return urlBuilder.toString();
        }

        /**
         * Encode url.
         * 
         * @param original the original
         * 
         * @return the string
         */
        private static String encodeUrl(String original) {
            try {
                return URLEncoder.encode(original, ApplicationConstants.CONTENT_ENCODING);
            } catch (UnsupportedEncodingException e) {
                return original;
            }
        }
    }
}
