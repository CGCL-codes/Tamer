package net.firefly.client.controller.request;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TimeZone;
import java.util.TreeMap;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import net.firefly.client.Version;
import net.firefly.client.http.aws.filters.AWSSoapResponseXmlFilter;
import net.firefly.client.tools.BASE64Encoder;
import net.firefly.client.tools.FireflyClientException;
import net.firefly.client.tools.HTTPTools;
import net.firefly.client.tools.XMLTools;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

public class AmazonWSRequestManager {

    private static String AWSAccessKeyId = "0Q1GVS612VRT50H8XX02";

    private static String AWSSecretKeyId = "5D1ldx+PL4EtH7NhTUbaG2GpWGfF8pOe49J8Nobu";

    private static final String ENDPOINT = "ecs.amazonaws.fr";

    private static final String ENCODING = "UTF-8";

    private static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";

    private static final String REQUEST_URI = "/onca/xml";

    private static final String REQUEST_METHOD = "GET";

    private SecretKeySpec secretKeySpec = null;

    private Mac mac = null;

    private Map httpHeaders;

    public AmazonWSRequestManager() {
        this.httpHeaders = new HashMap();
        this.httpHeaders.put("User-Agent", Version.getUserAgentId());
        byte[] secretKeyBytes = new byte[0];
        try {
            secretKeyBytes = AWSSecretKeyId.getBytes(ENCODING);
            secretKeySpec = new SecretKeySpec(secretKeyBytes, HMAC_SHA256_ALGORITHM);
            mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
            mac.init(secretKeySpec);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
    }

    public byte[] getAlbumCover(String artist, String album, String proxyHost, String proxyPort) throws FireflyClientException {
        byte[] cover = null;
        HttpURLConnection con = null;
        try {
            URL url = new URL(getCoverSOAPRequest(artist, album));
            con = HTTPTools.getUrlConnection(url, proxyHost, proxyPort, null, null, httpHeaders);
            AWSSoapResponseXmlFilter xmlFilter = new AWSSoapResponseXmlFilter();
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            saxParserFactory.setValidating(false);
            SAXParser saxParser = saxParserFactory.newSAXParser();
            XMLReader xmlReader = saxParser.getXMLReader();
            xmlFilter.setParent(xmlReader);
            InputStream is = con.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            BufferedReader br = new BufferedReader(new InputStreamReader(bis, XMLTools.DEFAULT_XML_ENCODING));
            InputSource inputSource = new InputSource(br);
            xmlFilter.parse(inputSource);
            bis.close();
            URL coverUrl = xmlFilter.getCoverUrl();
            if (coverUrl != null) {
                con = HTTPTools.getUrlConnection(coverUrl, proxyHost, proxyPort, null, null, httpHeaders);
                is = con.getInputStream();
                bis = new BufferedInputStream(is);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                int c;
                while ((c = bis.read()) != -1) {
                    baos.write(c);
                }
                cover = baos.toByteArray();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
        return cover;
    }

    public String getCoverSOAPRequest(String artist, String album) throws Exception {
        Map params = new HashMap();
        params.put("Operation", "ItemSearch");
        params.put("Artist", artist);
        params.put("Count", "1");
        params.put("ResponseGroup", "Images");
        params.put("SearchIndex", "Music");
        params.put("Title", album);
        return sign(params);
    }

    private String sign(Map params) {
        params.put("AWSAccessKeyId", AWSAccessKeyId);
        params.put("Timestamp", timestamp());
        SortedMap sortedParamMap = new TreeMap(params);
        String canonicalQS = canonicalize(sortedParamMap);
        String toSign = REQUEST_METHOD + "\n" + ENDPOINT + "\n" + REQUEST_URI + "\n" + canonicalQS;
        String hmac = hmac(toSign);
        String sig = percentEncodeRfc3986(hmac);
        String url = "http://" + ENDPOINT + REQUEST_URI + "?" + canonicalQS + "&Signature=" + sig;
        return url;
    }

    private String hmac(String stringToSign) {
        String signature = null;
        byte[] data;
        byte[] rawHmac;
        try {
            data = stringToSign.getBytes(ENCODING);
            rawHmac = mac.doFinal(data);
            BASE64Encoder encoder = new BASE64Encoder();
            signature = new String(encoder.encode(rawHmac));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(ENCODING + " is unsupported!", e);
        }
        return signature;
    }

    private String timestamp() {
        String timestamp = null;
        Calendar cal = Calendar.getInstance();
        DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        dfm.setTimeZone(TimeZone.getTimeZone("GMT"));
        timestamp = dfm.format(cal.getTime());
        return timestamp;
    }

    private String canonicalize(SortedMap sortedParamMap) {
        if (sortedParamMap.isEmpty()) {
            return "";
        }
        StringBuffer buffer = new StringBuffer();
        Iterator iter = sortedParamMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry kvpair = (Map.Entry) iter.next();
            buffer.append(percentEncodeRfc3986((String) kvpair.getKey()));
            buffer.append("=");
            buffer.append(percentEncodeRfc3986((String) kvpair.getValue()));
            if (iter.hasNext()) {
                buffer.append("&");
            }
        }
        String canonical = buffer.toString();
        return canonical;
    }

    private String percentEncodeRfc3986(String s) {
        String out;
        try {
            out = URLEncoder.encode(s, ENCODING).replace("+", "%20").replace("*", "%2A").replace("%7E", "~");
        } catch (UnsupportedEncodingException e) {
            out = s;
        }
        return out;
    }
}
