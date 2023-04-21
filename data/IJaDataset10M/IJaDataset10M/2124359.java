package org.dspace.content.authority;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.XMLReader;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.apache.log4j.Logger;
import org.dspace.core.ConfigurationManager;
import org.dspace.content.DCPersonName;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.util.EncodingUtil;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.HttpException;

/**
 * Sample personal name authority based on Library of Congress Name Authority
 * Also serves as an example of an SRU client as authority.
 *
 * This is tuned for the data in the LC Name Authority test instance, see
 * http://alcme.oclc.org/srw/search/lcnaf
 *
 * WARNING: This is just a proof-of-concept implementation.  It would need
 * WARNING: lots of refinement to be used in production, because it is very
 * WARNING: sloppy about digging through the MARC/XML results.  No doubt
 * WARNING: it is losing a lot of valid results and information.
 * WARNING: Could also do a better job including more info (title, life dates
 * WARNING: etc) in the label instead of just the name.
 *
 * Reads these DSpace Config properties:
 *
 *      lcname.url = http://alcme.oclc.org/srw/search/lcnaf
 *
 *  TODO: make # of results to ask for (and return) configurable.
 *
 * @author Larry Stone
 * @version $Revision $
 */
public class LCNameAuthority implements ChoiceAuthority {

    private static Logger log = Logger.getLogger(LCNameAuthority.class);

    private static String url = null;

    private static final String NS_SRU = "http://www.loc.gov/zing/srw/";

    private static final String NS_MX = "http://www.loc.gov/MARC21/slim";

    public LCNameAuthority() {
        if (url == null) {
            url = ConfigurationManager.getProperty("lcname.url");
            if (url == null) {
                throw new IllegalStateException("Missing DSpace configuration keys for LCName Query");
            }
        }
    }

    public Choices getBestMatch(String field, String text, int collection, String locale) {
        return getMatches(field, text, collection, 0, 2, locale);
    }

    /**
     * Match a proposed value against name authority records
     * Value is assumed to be in "Lastname, Firstname" format.
     */
    public Choices getMatches(String field, String text, int collection, int start, int limit, String locale) {
        Choices result = queryPerson(text, start, limit);
        if (result == null) {
            result = new Choices(true);
        }
        return result;
    }

    public String getLabel(String field, String key, String locale) {
        return key;
    }

    /**
     * Guts of the implementation, returns a complete Choices result, or
     * null for a failure.
     */
    private Choices queryPerson(String text, int start, int limit) {
        if (text == null || text.trim().length() == 0) {
            return new Choices(true);
        }
        DCPersonName pn = new DCPersonName(text);
        StringBuilder query = new StringBuilder();
        query.append("local.FirstName = \"").append(pn.getFirstNames()).append("\" and local.FamilyName = \"").append(pn.getLastName()).append("\"");
        if (limit == 0) {
            limit = 50;
        }
        NameValuePair args[] = new NameValuePair[6];
        args[0] = new NameValuePair("operation", "searchRetrieve");
        args[1] = new NameValuePair("version", "1.1");
        args[2] = new NameValuePair("recordSchema", "info:srw/schema/1/marcxml-v1.1");
        args[3] = new NameValuePair("query", query.toString());
        args[4] = new NameValuePair("maximumRecords", String.valueOf(limit));
        args[5] = new NameValuePair("startRecord", String.valueOf(start + 1));
        HttpClient hc = new HttpClient();
        String srUrl = url + "?" + EncodingUtil.formUrlEncode(args, "UTF8");
        GetMethod get = new GetMethod(srUrl);
        log.debug("Trying SRU query, URL=" + srUrl);
        try {
            int status = hc.executeMethod(get);
            if (status == 200) {
                SAXParserFactory spf = SAXParserFactory.newInstance();
                SAXParser sp = spf.newSAXParser();
                XMLReader xr = sp.getXMLReader();
                SRUHandler handler = new SRUHandler();
                xr.setFeature("http://xml.org/sax/features/namespaces", true);
                xr.setContentHandler(handler);
                xr.setErrorHandler(handler);
                xr.parse(new InputSource(get.getResponseBodyAsStream()));
                if (handler.hits != handler.result.size()) {
                    log.warn("Discrepency in results, result.length=" + handler.result.size() + ", yet expected results=" + handler.hits);
                }
                boolean more = handler.hits > (start + handler.result.size());
                int confidence;
                if (handler.hits == 0) {
                    confidence = Choices.CF_NOTFOUND;
                } else if (handler.hits == 1) {
                    confidence = Choices.CF_UNCERTAIN;
                } else {
                    confidence = Choices.CF_AMBIGUOUS;
                }
                return new Choices(handler.result.toArray(new Choice[handler.result.size()]), start, handler.hits, confidence, more);
            }
        } catch (HttpException e) {
            log.error("SRU query failed: ", e);
            return new Choices(true);
        } catch (IOException e) {
            log.error("SRU query failed: ", e);
            return new Choices(true);
        } catch (ParserConfigurationException e) {
            log.warn("Failed parsing SRU result: ", e);
            return new Choices(true);
        } catch (SAXException e) {
            log.warn("Failed parsing SRU result: ", e);
            return new Choices(true);
        } finally {
            get.releaseConnection();
        }
        return new Choices(true);
    }

    /**
     * XXX FIXME TODO: Very sloppy MARC/XML parser.
     * This only reads subfields 010.a (for LCCN, to use as key)
     * and 100.a (for "established personal name")
     * Maybe look at Indicator on 100 too.
     * Should probably read other 100 subfields to build a more detailed label.
     */
    private static class SRUHandler extends DefaultHandler {

        private List<Choice> result = new ArrayList<Choice>();

        private int hits = -1;

        private String textValue = null;

        private String name = null;

        private String lccn = null;

        private String lastTag = null;

        private String lastCode = null;

        public void characters(char[] ch, int start, int length) throws SAXException {
            String newValue = new String(ch, start, length);
            if (newValue.length() > 0) {
                if (textValue == null) {
                    textValue = newValue;
                } else {
                    textValue += newValue;
                }
            }
        }

        public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
            if (localName.equals("numberOfRecords") && namespaceURI.equals(NS_SRU)) {
                hits = Integer.parseInt(textValue.trim());
                if (hits > 0) {
                    name = null;
                    lccn = null;
                    log.debug("Expecting " + hits + " records in results.");
                }
            } else if (localName.equals("record") && namespaceURI.equals(NS_SRU)) {
                if (name != null && lccn != null) {
                    if (name.endsWith(",")) {
                        name = name.substring(0, name.length() - 1);
                    }
                    result.add(new Choice(lccn, name, name));
                } else {
                    log.warn("Got anomalous result, at least one of these null: lccn=" + lccn + ", name=" + name);
                }
                name = null;
                lccn = null;
            } else if (localName.equals("subfield") && namespaceURI.equals(NS_MX)) {
                if (lastTag != null && lastCode != null) {
                    if (lastTag.equals("010") && lastCode.equals("a")) {
                        lccn = textValue;
                    } else if (lastTag.equals("100") && lastCode.equals("a")) {
                        name = textValue;
                    }
                    if (lastTag.equals("100") && lastCode.equals("d") && (name != null)) {
                        name = name + "  " + textValue;
                    }
                }
            }
        }

        public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
            textValue = null;
            if (localName.equals("datafield") && namespaceURI.equals(NS_MX)) {
                lastTag = atts.getValue("tag");
                if (lastTag == null) {
                    log.warn("MARC datafield without tag attribute!");
                }
            } else if (localName.equals("subfield") && namespaceURI.equals(NS_MX)) {
                lastCode = atts.getValue("code");
                if (lastCode == null) {
                    log.warn("MARC subfield without code attribute!");
                }
            }
        }

        public void error(SAXParseException exception) throws SAXException {
            throw new SAXException(exception);
        }

        public void fatalError(SAXParseException exception) throws SAXException {
            throw new SAXException(exception);
        }
    }
}
