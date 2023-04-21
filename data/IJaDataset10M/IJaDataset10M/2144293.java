package com.jaxws.json.codec.doc.provider;

import java.io.IOException;
import java.util.Collection;
import java.util.Properties;
import java.util.regex.Pattern;
import com.jaxws.json.codec.JSONCodec;
import com.jaxws.json.codec.doc.AbstractHttpMetadataProvider;
import com.jaxws.json.codec.doc.HttpMetadataProvider;
import com.jaxws.json.codec.doc.JSONHttpMetadataPublisher;
import com.sun.xml.ws.transport.http.WSHTTPConnection;

/**
 * @author Sundaramurthi Saminathan
 * @since JSONWebservice codec version 0.4
 * @version 1.0
 * 
 * Meta data document provider which serves currently
 *         configured properties.
 */
public class ServiceConfigurationProvider extends AbstractHttpMetadataProvider implements HttpMetadataProvider {

    private static final String[] queries = new String[] { "config" };

    /**
	 * Configuration information holder. This version configuration is global
	 * only. So cached at first request.
	 */
    private static final StringBuffer configInfo = new StringBuffer();

    /**
	 * Request recived codec holder.
	 */
    private JSONCodec codec = null;

    /**
	 * "config" query handled.
	 */
    public String[] getHandlingQueries() {
        return queries;
    }

    /**
	 * Utility method returns list of patterns to String
	 * 
	 * @param patterns
	 * @return
	 */
    private String getPatternStrings(Collection<Pattern> patterns) {
        String patternStrings = "";
        if (patterns != null) {
            for (Pattern pattern : patterns) {
                patternStrings += " | " + pattern.pattern();
            }
        }
        return patternStrings;
    }

    /**
	 * Document request with config query handled by ServiceConfigurationServer
	 * 
	 */
    public boolean canHandle(String queryString) {
        return queryString != null && queryString.equalsIgnoreCase(queries[0]);
    }

    public void setJSONCodec(JSONCodec codec) {
        this.codec = codec;
    }

    public String getContentType() {
        return "text/html; charset=\"utf-8\"";
    }

    /**
	 * Init configuration holder
	 */
    public void process() {
        synchronized (configInfo) {
            if (configInfo.length() == 0) {
                Properties templates = new Properties();
                try {
                    templates.load(JSONHttpMetadataPublisher.class.getResourceAsStream("codec.properties"));
                    String propertyTemplate = templates.getProperty("template.config.property", "<html><body>My Bad...Undefined template</body></html>");
                    StringBuffer propertys = new StringBuffer();
                    propertys.append(String.format(propertyTemplate, JSONCodec.dateFormat_KEY, String.valueOf(JSONCodec.dateFormat)));
                    propertys.append(String.format(propertyTemplate, JSONCodec.responsePayloadEnabled_KEY, String.valueOf(JSONCodec.responsePayloadEnabled)));
                    propertys.append(String.format(propertyTemplate, JSONCodec.excludeNullProperties_KEY, String.valueOf(JSONCodec.excludeNullProperties)));
                    propertys.append(String.format(propertyTemplate, JSONCodec.createDefaultOnNonNullable_KEY, String.valueOf(JSONCodec.createDefaultOnNonNullable)));
                    propertys.append(String.format(propertyTemplate, JSONCodec.gzip_KEY, String.valueOf(JSONCodec.gzip)));
                    propertys.append(String.format(propertyTemplate, JSONCodec.listWrapperSkip_KEY, String.valueOf(JSONCodec.listWrapperSkip)));
                    propertys.append(String.format(propertyTemplate, JSONCodec.excludeProperties_KEY, getPatternStrings(JSONCodec.excludeProperties)));
                    propertys.append(String.format(propertyTemplate, JSONCodec.includeProperties_KEY, getPatternStrings(JSONCodec.includeProperties)));
                    propertys.append(String.format(propertyTemplate, JSONCodec.globalMapKeyPattern_KEY, String.valueOf(JSONCodec.globalMapKeyPattern)));
                    propertys.append(String.format(propertyTemplate, JSONCodec.globalMapValuePattern_KEY, String.valueOf(JSONCodec.globalMapValuePattern)));
                    propertys.append(String.format(propertyTemplate, JSONCodec.STATUS_PROPERTY_NAME_KEY, String.valueOf(JSONCodec.STATUS_PROPERTY_NAME)));
                    configInfo.append(String.format(templates.getProperty("template.config.main", "<html><body>My Bad... Undefined template</body></html>"), codec.getEndpoint().getPortName().getLocalPart(), "65%", propertys.toString()));
                } catch (IOException e) {
                }
            }
        }
    }

    public void doResponse(WSHTTPConnection ouStream) throws IOException {
        process();
        ouStream.getOutput().write(configInfo.toString().getBytes());
        ouStream.getOutput().flush();
    }

    public int compareTo(HttpMetadataProvider o) {
        if (o.equals(this)) {
            return 0;
        } else {
            return Integer.MAX_VALUE;
        }
    }
}
