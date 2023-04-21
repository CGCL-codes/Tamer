package net.sf.solarnetwork.node.impl;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.logging.Level;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import net.sf.solarnetwork.node.ConsumptionDatum;
import net.sf.solarnetwork.node.DayDatum;
import net.sf.solarnetwork.node.PowerDatum;
import net.sf.solarnetwork.node.PriceDatum;
import net.sf.solarnetwork.node.UploadService;
import net.sf.solarnetwork.node.WeatherDatum;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.core.io.Resource;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

/**
 * Implementation of {@link UploadService} that posts XML to a URL.
 * 
 * <p>The general procedure for uploading a datum this implementation follows is:</p>
 * 
 * <ol>
 *   <li>Turn the Datum instance into an XML Document, as a single element
 *   with class properties rendered as attributes. Date properties are serialized
 *   into XML Schema dateTime representation, in the UTC time zone.</li>
 *   
 *   <li>Run an XSLT transformation on the XML Document, passing in {@link #getNodeId()}
 *   as the {@link #XSLT_PARAM_NODE_ID} parameter. It is the job of this XSLT
 *   to transform the raw XML Document into the form expected by the remote service 
 *   accepting this datum. For example the XSLT might tranform the XML into a
 *   SOAP encoded XML-RPC call.</li>
 *   
 *   <li>The result of the XSLT transformation is posted to the remote service. The
 *   service is expected to return a valid XML response, with an integer "tracking ID"
 *   value representing the unique ID of the uploaded datum on the remote service's
 *   system.</li>
 *   
 *   <li>An XPath expression is used to extract the remote service's "tracking ID".</li>
 * </ol>
 * 
 * <p>Using this procedure datum values can be uploaded in any textual format (the
 * XSLT could transform the XML into plain text, for example). For each type
 * of datum supported by this implementation, there are three configurable properties
 * you must configure:</p>
 * 
 * <dl class="class-properties">
 *   <dt>*DatumUrl</dt>
 *   <dd>The URL to post the datum to.</dd>
 *   
 *   <dt>*DatumXsltResource</dt>
 *   <dd>A Resource reference of the XSLT to run.</dd>
 *   
 *   <dt>*DatumIdXPath</dt>
 *   <dd>The XPath expression to execute on the remote service's response that
 *   returns the remote "tracking ID" value.</dd>
 * </dl>
 * 
 * @author matt.magoffin
 * @version $Revision: 275 $ $Date: 2009-08-07 05:00:11 -0400 (Fri, 07 Aug 2009) $
 */
public class XmlWebPostUploadService extends XmlServiceSupport implements UploadService {

    /** The date format to use for DayDatum dates. */
    public static final String DAY_DATE_FORMAT = "yyyy-MM-dd";

    /** The date format to use for DayDatum times. */
    public static final String DAY_TIME_FORMAT = "HH:mm";

    /** The XSLT parameter for the current node ID. */
    public static final String XSLT_PARAM_NODE_ID = "node-id";

    /** The default value for the {@code connectionTimeout} property. */
    public static final int DEFAULT_CONNECTION_TIMEOUT = 15000;

    private String nodeId = null;

    private String key = null;

    private int connectionTimeout = DEFAULT_CONNECTION_TIMEOUT;

    private String powerDatumIdXPath = null;

    private Resource powerDatumXsltResource = null;

    private String powerDatumUrl = null;

    private String consumptionDatumUrl = null;

    private Resource consumptionDatumXsltResource = null;

    private String consumptionDatumIdXPath = null;

    private String dayDatumUrl = null;

    private Resource dayDatumXsltResource = null;

    private String dayDatumIdXPath = null;

    private String priceDatumUrl = null;

    private Resource priceDatumXsltResource = null;

    private String priceDatumIdXPath = null;

    private String weatherDatumUrl = null;

    private Resource weatherDatumXsltResource = null;

    private String weatherDatumIdXPath = null;

    private XPathExpression powerDatumTrackingIdXPath = null;

    private Templates powerDatumXslt = null;

    private XPathExpression consumptionDatumTrackingIdXPath = null;

    private Templates consumptionDatumXslt = null;

    private XPathExpression dayDatumTrackingIdXPath = null;

    private Templates dayDatumXslt = null;

    private XPathExpression priceDatumTrackingIdXPath = null;

    private Templates priceDatumXslt = null;

    private XPathExpression weatherDatumTrackingIdXPath = null;

    private Templates weatherDatumXslt = null;

    /**
	 * Initialize this class after properties are set.
	 */
    @Override
    public void init() {
        super.init();
        try {
            XPath xp = getXpathFactory().newXPath();
            if (getNsContext() != null) {
                xp.setNamespaceContext(getNsContext());
            }
            if (this.powerDatumIdXPath != null) {
                this.powerDatumTrackingIdXPath = xp.compile(this.powerDatumIdXPath);
            }
            if (this.consumptionDatumIdXPath != null) {
                this.consumptionDatumTrackingIdXPath = xp.compile(this.consumptionDatumIdXPath);
            }
            if (this.dayDatumIdXPath != null) {
                this.dayDatumTrackingIdXPath = xp.compile(this.dayDatumIdXPath);
            }
            if (this.priceDatumIdXPath != null) {
                this.priceDatumTrackingIdXPath = xp.compile(this.priceDatumIdXPath);
            }
            if (this.weatherDatumIdXPath != null) {
                this.weatherDatumTrackingIdXPath = xp.compile(this.weatherDatumIdXPath);
            }
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
        if (this.powerDatumXsltResource != null) {
            this.powerDatumXslt = getTemplates(this.powerDatumXsltResource);
        }
        if (this.consumptionDatumXsltResource != null) {
            this.consumptionDatumXslt = getTemplates(this.consumptionDatumXsltResource);
        }
        if (this.dayDatumXsltResource != null) {
            this.dayDatumXslt = getTemplates(this.dayDatumXsltResource);
        }
        if (this.priceDatumXsltResource != null) {
            this.priceDatumXslt = getTemplates(this.priceDatumXsltResource);
        }
        if (this.weatherDatumXsltResource != null) {
            this.weatherDatumXslt = getTemplates(this.weatherDatumXsltResource);
        }
    }

    public String getKey() {
        return "XmlWebPostUploadService:" + key;
    }

    public Long uploadPowerDatum(PowerDatum data) {
        Source in = getSource(data);
        ByteArrayOutputStream byos = runTransformation(this.powerDatumXslt, in, null);
        String respXml = postXml(this.powerDatumUrl, "", byos.toString());
        if (log.isLoggable(Level.FINEST)) {
            try {
                log.finest("Result XML: " + getXmlAsString(new StreamSource(new StringReader(respXml)), true));
            } catch (RuntimeException e) {
                log.finest("Result: " + respXml);
            }
        }
        return extractTrackingId(respXml, this.powerDatumTrackingIdXPath, this.powerDatumIdXPath);
    }

    public Long uploadConsumptionDatum(ConsumptionDatum data) {
        Source in = getSource(data);
        ByteArrayOutputStream byos = runTransformation(this.consumptionDatumXslt, in, null);
        String respXml = postXml(this.consumptionDatumUrl, "", byos.toString());
        if (log.isLoggable(Level.FINEST)) {
            try {
                log.finest("Result XML: " + getXmlAsString(new StreamSource(new StringReader(respXml)), true));
            } catch (RuntimeException e) {
                log.finest("Result: " + respXml);
            }
        }
        return extractTrackingId(respXml, this.consumptionDatumTrackingIdXPath, this.consumptionDatumIdXPath);
    }

    public Long uploadPriceDatum(PriceDatum data) {
        Source in = getSource(data);
        ByteArrayOutputStream byos = runTransformation(this.priceDatumXslt, in, null);
        String respXml = postXml(this.priceDatumUrl, "", byos.toString());
        if (log.isLoggable(Level.FINEST)) {
            try {
                log.finest("Result XML: " + getXmlAsString(new StreamSource(new StringReader(respXml)), true));
            } catch (RuntimeException e) {
                log.finest("Result: " + respXml);
            }
        }
        return extractTrackingId(respXml, this.priceDatumTrackingIdXPath, this.priceDatumIdXPath);
    }

    private Long extractTrackingId(String xml, XPathExpression xp, String xpath) {
        Double tid;
        try {
            tid = (Double) xp.evaluate(new InputSource(new StringReader(xml)), XPathConstants.NUMBER);
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
        if (tid.isNaN()) {
            log.warning("Unable to extract tracking ID via XPath [" + xpath + ']');
            return null;
        }
        if (log.isLoggable(Level.FINE)) {
            log.fine("Found tracking ID [" + tid.longValue() + ']');
        }
        return tid.longValue();
    }

    private ByteArrayOutputStream runTransformation(Templates t, Source in, Map<String, ?> parameters) {
        ByteArrayOutputStream byos = new ByteArrayOutputStream();
        Transformer xform;
        try {
            xform = t.newTransformer();
            xform.setParameter(XSLT_PARAM_NODE_ID, this.nodeId);
            if (parameters != null) {
                for (Map.Entry<String, ?> me : parameters.entrySet()) {
                    xform.setParameter(me.getKey(), me.getValue());
                }
            }
            xform.transform(in, new StreamResult(byos));
        } catch (TransformerConfigurationException e) {
            throw new RuntimeException(e);
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }
        if (log.isLoggable(Level.FINEST)) {
            log.finest("Post XML: " + getXmlAsString(new StreamSource(new ByteArrayInputStream(byos.toByteArray())), true));
        }
        return byos;
    }

    public Long uploadDayDatum(DayDatum data) {
        return uploadDayDatum(data, null);
    }

    private Long uploadDayDatum(DayDatum data, Map<String, ?> attributes) {
        Source in = getSource(data);
        ByteArrayOutputStream byos = runTransformation(this.dayDatumXslt, in, attributes);
        String respXml = postXml(this.dayDatumUrl, "", byos.toString());
        return extractTrackingId(respXml, this.dayDatumTrackingIdXPath, this.dayDatumIdXPath);
    }

    private String postXml(String url, String soapAction, String xml) {
        try {
            URLConnection conn = new URL(url).openConnection();
            if (conn instanceof HttpURLConnection) {
                HttpURLConnection hConn = (HttpURLConnection) conn;
                hConn.setRequestMethod("POST");
            }
            conn.setConnectTimeout(this.connectionTimeout);
            conn.setReadTimeout(this.connectionTimeout);
            conn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
            conn.setRequestProperty("Accept", "application/soap+xml, text/*");
            if (soapAction != null) {
                conn.setRequestProperty("SOAPAction", soapAction);
            }
            conn.setDoOutput(true);
            OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
            out.write(xml);
            out.close();
            BufferedReader resp = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder buf = new StringBuilder();
            String str;
            while ((str = resp.readLine()) != null) {
                buf.append(str);
            }
            return buf.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Long uploadWeatherDatum(WeatherDatum data) {
        return uploadWeatherDatum(data, null);
    }

    private Long uploadWeatherDatum(WeatherDatum data, Map<String, ?> attributes) {
        Source in = getSource(data);
        ByteArrayOutputStream byos = runTransformation(this.weatherDatumXslt, in, attributes);
        String respXml = postXml(this.weatherDatumUrl, "", byos.toString());
        return extractTrackingId(respXml, this.weatherDatumTrackingIdXPath, this.weatherDatumIdXPath);
    }

    public Long[] uploadDayAndWeatherDatum(DayDatum dayData, WeatherDatum weatherData, Map<String, ?> attributes) {
        return new Long[] { uploadDayDatum(dayData, attributes), uploadWeatherDatum(weatherData, attributes) };
    }

    private Document getDayDatumDocument(DayDatum datum) {
        BeanWrapper dayBean = PropertyAccessorFactory.forBeanPropertyAccess(datum);
        SimpleDateFormat dateFormat = new SimpleDateFormat(DAY_DATE_FORMAT);
        dateFormat.setLenient(false);
        dayBean.registerCustomEditor(Date.class, "day", new CustomDateEditor(dateFormat, false));
        SimpleDateFormat timeFormat = new SimpleDateFormat(DAY_TIME_FORMAT);
        dateFormat.setLenient(false);
        dayBean.registerCustomEditor(Date.class, "sunrise", new CustomDateEditor(timeFormat, false));
        dayBean.registerCustomEditor(Date.class, "sunset", new CustomDateEditor(timeFormat, false));
        return getDocument(dayBean, "dayDatum");
    }

    /**
	 * Turn a PowerDatum into an XML Document.
	 * 
	 * <p>The XML looks like this:<p>
	 * 
	 * <pre>&lt;powerDatum
	 *   id="123"
	 *   pvVolts="123.123"
	 *   ... /&gt;</pre>
	 * 
	 * @param data the PowerDatum to turn into XML
	 * @return a Source
	 */
    private Source getSource(PowerDatum data) {
        return getSimpleSource(data, "powerDatum");
    }

    /**
	 * Turn a ConsumptionDatum into an XML Document.
	 * 
	 * <p>The XML looks like this:<p>
	 * 
	 * <pre>&lt;consumptionDatum
	 *   id="123"
	 *   amps="12.1"
	 *   ... /&gt;</pre>
	 * 
	 * @param data the ConsumptionDatum to turn into XML
	 * @return a Source
	 */
    private Source getSource(ConsumptionDatum data) {
        return getSimpleSource(data, "consumptionDatum");
    }

    /**
	 * Turn a DayDatum into an XML Document.
	 * 
	 * <p>The XML looks like this:<p>
	 * 
	 * <pre>&lt;dayDatum
	 *   id="123"
	 *   ... /&gt;</pre>
	 * 
	 * @param data the DayDatum to turn into XML
	 * @return a Source
	 */
    private Source getSource(DayDatum data) {
        return getSource(getDayDatumDocument(data));
    }

    /**
	 * Turn a PriceDatum into an XML Document.
	 * 
	 * <p>The XML looks like this:<p>
	 * 
	 * <pre>&lt;priceDatum
	 *   id="123"
	 *   price="12.1"
	 *   ... /&gt;</pre>
	 * 
	 * @param data the PriceDatum to turn into XML
	 * @return a Source
	 */
    private Source getSource(PriceDatum data) {
        return getSimpleSource(data, "priceDatum");
    }

    /**
	 * Turn a WeatherDatum into an XML Document.
	 * 
	 * <p>The XML looks like this:<p>
	 * 
	 * <pre>&lt;weatherDatum
	 *   id="123"
	 *   ... /&gt;</pre>
	 * 
	 * @param data the DayDatum to turn into XML
	 * @return a Source
	 */
    private Source getSource(WeatherDatum data) {
        return getSimpleSource(data, "weatherDatum");
    }

    /**
	 * Get the {@code powerDatumIdXPath} value.
	 * @return the powerDatumIdXPath
	 * @deprecated this has been replaced by {@link #getPowerDatumIdXPath()}
	 */
    @Deprecated
    public String getTrackingIdXPath() {
        return getPowerDatumIdXPath();
    }

    /**
	 * Set the {@code powerDatumIdXPath} value.
	 * @param trackingIdXPath the trackingIdXPath to set
	 * @deprecated this has been replaced by {@link #setPowerDatumIdXPath(String)}
	 */
    @Deprecated
    public void setTrackingIdXPath(String trackingIdXPath) {
        setPowerDatumIdXPath(trackingIdXPath);
    }

    /**
	 * @return the powerDatumIdXPath
	 */
    public String getPowerDatumIdXPath() {
        return powerDatumIdXPath;
    }

    /**
	 * @param powerDatumIdXPath the powerDatumIdXPath to set
	 */
    public void setPowerDatumIdXPath(String powerDatumIdXPath) {
        this.powerDatumIdXPath = powerDatumIdXPath;
    }

    /**
	 * @return the powerDatumXsltResource
	 */
    public Resource getPowerDatumXsltResource() {
        return powerDatumXsltResource;
    }

    /**
	 * @param powerDatumXsltResource the powerDatumXsltResource to set
	 */
    public void setPowerDatumXsltResource(Resource powerDatumXsltResource) {
        this.powerDatumXsltResource = powerDatumXsltResource;
    }

    /**
	 * @return the powerDatumUrl
	 */
    public String getPowerDatumUrl() {
        return powerDatumUrl;
    }

    /**
	 * @param powerDatumUrl the powerDatumUrl to set
	 */
    public void setPowerDatumUrl(String powerDatumUrl) {
        this.powerDatumUrl = powerDatumUrl;
    }

    /**
	 * @return the nodeId
	 */
    public String getNodeId() {
        return nodeId;
    }

    /**
	 * @param nodeId the nodeId to set
	 */
    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    /**
	 * @param key the key to set
	 */
    public void setKey(String key) {
        this.key = key;
    }

    /**
	 * @return the dayDatumUrl
	 */
    public String getDayDatumUrl() {
        return dayDatumUrl;
    }

    /**
	 * @param dayDatumUrl the dayDatumUrl to set
	 */
    public void setDayDatumUrl(String dayDatumUrl) {
        this.dayDatumUrl = dayDatumUrl;
    }

    /**
	 * @return the dayDatumXsltResource
	 */
    public Resource getDayDatumXsltResource() {
        return dayDatumXsltResource;
    }

    /**
	 * @param dayDatumXsltResource the dayDatumXsltResource to set
	 */
    public void setDayDatumXsltResource(Resource dayDatumXsltResource) {
        this.dayDatumXsltResource = dayDatumXsltResource;
    }

    /**
	 * @return the dayDatumIdXPath
	 */
    public String getDayDatumIdXPath() {
        return dayDatumIdXPath;
    }

    /**
	 * @param dayDatumIdXPath the dayDatumIdXPath to set
	 */
    public void setDayDatumIdXPath(String dayDatumIdXPath) {
        this.dayDatumIdXPath = dayDatumIdXPath;
    }

    /**
	 * @return the weatherDatumUrl
	 */
    public String getWeatherDatumUrl() {
        return weatherDatumUrl;
    }

    /**
	 * @param weatherDatumUrl the weatherDatumUrl to set
	 */
    public void setWeatherDatumUrl(String weatherDatumUrl) {
        this.weatherDatumUrl = weatherDatumUrl;
    }

    /**
	 * @return the weatherDatumXsltResource
	 */
    public Resource getWeatherDatumXsltResource() {
        return weatherDatumXsltResource;
    }

    /**
	 * @param weatherDatumXsltResource the weatherDatumXsltResource to set
	 */
    public void setWeatherDatumXsltResource(Resource weatherDatumXsltResource) {
        this.weatherDatumXsltResource = weatherDatumXsltResource;
    }

    /**
	 * @return the weatherDatumIdXPath
	 */
    public String getWeatherDatumIdXPath() {
        return weatherDatumIdXPath;
    }

    /**
	 * @param weatherDatumIdXPath the weatherDatumIdXPath to set
	 */
    public void setWeatherDatumIdXPath(String weatherDatumIdXPath) {
        this.weatherDatumIdXPath = weatherDatumIdXPath;
    }

    /**
	 * @return the connectionTimeout
	 */
    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    /**
	 * @param connectionTimeout the connectionTimeout to set
	 */
    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    /**
	 * @return the consumptionDatumUrl
	 */
    public String getConsumptionDatumUrl() {
        return consumptionDatumUrl;
    }

    /**
	 * @param consumptionDatumUrl the consumptionDatumUrl to set
	 */
    public void setConsumptionDatumUrl(String consumptionDatumUrl) {
        this.consumptionDatumUrl = consumptionDatumUrl;
    }

    /**
	 * @return the consumptionDatumXsltResource
	 */
    public Resource getConsumptionDatumXsltResource() {
        return consumptionDatumXsltResource;
    }

    /**
	 * @param consumptionDatumXsltResource the consumptionDatumXsltResource to set
	 */
    public void setConsumptionDatumXsltResource(Resource consumptionDatumXsltResource) {
        this.consumptionDatumXsltResource = consumptionDatumXsltResource;
    }

    /**
	 * @return the consumptionDatumIdXPath
	 */
    public String getConsumptionDatumIdXPath() {
        return consumptionDatumIdXPath;
    }

    /**
	 * @param consumptionDatumIdXPath the consumptionDatumIdXPath to set
	 */
    public void setConsumptionDatumIdXPath(String consumptionDatumIdXPath) {
        this.consumptionDatumIdXPath = consumptionDatumIdXPath;
    }

    /**
	 * @return the priceDatumUrl
	 */
    public String getPriceDatumUrl() {
        return priceDatumUrl;
    }

    /**
	 * @param priceDatumUrl the priceDatumUrl to set
	 */
    public void setPriceDatumUrl(String priceDatumUrl) {
        this.priceDatumUrl = priceDatumUrl;
    }

    /**
	 * @return the priceDatumXsltResource
	 */
    public Resource getPriceDatumXsltResource() {
        return priceDatumXsltResource;
    }

    /**
	 * @param priceDatumXsltResource the priceDatumXsltResource to set
	 */
    public void setPriceDatumXsltResource(Resource priceDatumXsltResource) {
        this.priceDatumXsltResource = priceDatumXsltResource;
    }

    /**
	 * @return the priceDatumIdXPath
	 */
    public String getPriceDatumIdXPath() {
        return priceDatumIdXPath;
    }

    /**
	 * @param priceDatumIdXPath the priceDatumIdXPath to set
	 */
    public void setPriceDatumIdXPath(String priceDatumIdXPath) {
        this.priceDatumIdXPath = priceDatumIdXPath;
    }
}
