package genomancer.vine.das2.client.modelimpl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLStreamException;
import genomancer.trellis.das2.model.Das2FeaturesQueryI;
import genomancer.trellis.das2.model.Das2FeaturesResponseI;
import genomancer.trellis.das2.model.Das2FeaturesCapabilityI;
import genomancer.trellis.das2.model.Das2LocationRefI;
import genomancer.trellis.das2.model.Das2FeatureI;
import genomancer.trellis.das2.model.Das2TypeI;
import genomancer.trellis.das2.model.Strand;
import genomancer.trellis.das2.server.ServerUtils;
import genomancer.vine.das2.client.xml.FeaturesXmlReader;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Das2FeaturesCapability extends Das2GenericCapability implements Das2FeaturesCapabilityI {

    public Das2FeaturesCapability(URI base_uri, String query_uri, Das2Version version, Das2Coordinates coordinates) {
        super(base_uri, query_uri, "features", version, coordinates);
    }

    public long getLastModified(Das2FeaturesQueryI query) {
        return -1L;
    }

    public Das2FeaturesResponseI getFeatures(Das2FeaturesQueryI query) {
        String query_string = getQueryString(query);
        Das2FeaturesResponseI features = null;
        InputStream istr = null;
        boolean success = false;
        try {
            String features_query_string = this.getAbsoluteURI().toString() + "?" + query_string;
            System.out.println("FEATURES_QUERY: " + features_query_string);
            System.out.println("   URL-decoded: " + URLDecoder.decode(features_query_string));
            URL features_query = new URL(features_query_string);
            URLConnection conn = features_query.openConnection();
            istr = conn.getInputStream();
            features = FeaturesXmlReader.readFeaturesDocument(istr, this.getAbsoluteURI());
            success = true;
        } catch (IOException ex) {
            Logger.getLogger(Das2FeaturesCapability.class.getName()).log(Level.SEVERE, null, ex);
        } catch (XMLStreamException ex) {
            Logger.getLogger(Das2FeaturesCapability.class.getName()).log(Level.SEVERE, null, ex);
        } catch (URISyntaxException ex) {
            Logger.getLogger(Das2FeaturesCapability.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                istr.close();
            } catch (IOException ex) {
                success = false;
                Logger.getLogger(Das2FeaturesCapability.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (!success) {
        }
        return features;
    }

    public InputStream getFeaturesAlternateFormat(Das2FeaturesQueryI query) {
        String query_string = getQueryString(query);
        String format = query.getFormat();
        InputStream istr = null;
        boolean success = false;
        try {
            String features_query_string = this.getAbsoluteURI().toString() + "?" + query_string;
            System.out.println("ALTERNATE FORMAT FEATURES_QUERY: " + features_query_string);
            System.out.println("   URL-decoded: " + URLDecoder.decode(features_query_string));
            URL features_query = new URL(features_query_string);
            URLConnection conn = features_query.openConnection();
            istr = conn.getInputStream();
            success = true;
        } catch (IOException ex) {
            Logger.getLogger(Das2FeaturesCapability.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (!success) {
        }
        return istr;
    }

    public Das2FeaturesResponseI getFeatures() {
        Das2FeaturesResponseI features = null;
        InputStream istr = null;
        boolean success = false;
        try {
            URL features_query = this.getAbsoluteURI().toURL();
            URLConnection conn = features_query.openConnection();
            istr = conn.getInputStream();
            features = FeaturesXmlReader.readFeaturesDocument(istr, this.getAbsoluteURI());
            success = true;
        } catch (IOException ex) {
            Logger.getLogger(Das2FeaturesCapability.class.getName()).log(Level.SEVERE, null, ex);
        } catch (XMLStreamException ex) {
            Logger.getLogger(Das2FeaturesCapability.class.getName()).log(Level.SEVERE, null, ex);
        } catch (URISyntaxException ex) {
            Logger.getLogger(Das2FeaturesCapability.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                istr.close();
            } catch (IOException ex) {
                success = false;
                Logger.getLogger(Das2FeaturesCapability.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (!success) {
        }
        return features;
    }

    public boolean supportsFullQueryFilters() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean supportsCountFormat() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean supportsUriFormat() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getFeaturesCount(Das2FeaturesQueryI query) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<String> getFeaturesURI(Das2FeaturesQueryI query) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     *  Note that returned range string is NOT URL-encoded, if needed apply URLEncoder:
     *      URLEncoder.encode( getRangeQueryString(loc) )
     */
    public static String getRangeQueryString(Das2LocationRefI loc) {
        StringBuffer buf = new StringBuffer();
        String delimiter = ServerUtils.DAS2_RANGE_DELIMITER;
        buf.append(Integer.toString(loc.getMin()));
        buf.append(delimiter);
        buf.append(Integer.toString(loc.getMax()));
        Strand strand = loc.getStrand();
        if (strand == Strand.FORWARD) {
            buf.append(delimiter);
            buf.append("1");
        } else if (strand == Strand.REVERSE) {
            buf.append(delimiter);
            buf.append("-1");
        } else {
        }
        return buf.toString();
    }

    public Class getFeatureClassForType(Das2TypeI type) {
        return Das2FeatureI.class;
    }

    public int getMaxHierarchyDepth(Das2TypeI type) {
        return Das2FeaturesCapabilityI.UNKNOWN;
    }

    /**
     *  URL-encodes both the parameter name and the parameter value
     *  does not include the "?" delimiter that indicates start of URL query section
     */
    class QueryStringBuilder {

        String delimiter = ";";

        int param_count = 0;

        StringBuffer qbuf = new StringBuffer();

        void addParameter(String param_name, String param_val) {
            System.out.println("QueryStringBuilder.addParameter(), " + "name = " + param_name + ", val = " + param_val);
            if (param_count > 0) {
                qbuf.append(delimiter);
            }
            qbuf.append(URLEncoder.encode(param_name));
            qbuf.append("=");
            qbuf.append(URLEncoder.encode(param_val));
            param_count++;
        }

        String getQueryString() {
            return qbuf.toString();
        }
    }

    /**
     *  Given a Das2FeaturesQueryI, 
     *  construct the corresponding query string for making a features request to a DAS/2 server
     *
     *  all param names and values are URL-encoded
     *  currently all values that are URIs are absolute
     *
     *  basically the mirror of genomancer.trellis.das2.server.Das2Servlet.parseFeatureQueryFilter()
     */
    public String getQueryString(Das2FeaturesQueryI query) {
        QueryStringBuilder qsb = new QueryStringBuilder();
        String format = query.getFormat();
        List<Das2LocationRefI> overlaps = query.getOverlaps();
        List<Das2LocationRefI> insides = query.getInsides();
        List<Das2LocationRefI> excludes = query.getExcludes();
        List<URI> types = query.getTypes();
        List<URI> coords = query.getCoordinates();
        List<URI> links = query.getLinks();
        List<String> names = query.getNames();
        List<String> notes = query.getNotes();
        Map<String, List<String>> non_standard_params = query.getNonStandardParams();
        Set<URI> segment_uris = new HashSet<URI>();
        boolean prefix_separator = false;
        if (overlaps != null || insides != null || excludes != null) {
            for (Das2LocationRefI loc : overlaps) {
                URI seguri = loc.getSegmentURI();
                if (!segment_uris.contains(seguri)) {
                    qsb.addParameter("segment", seguri.toString());
                    segment_uris.add(seguri);
                }
                qsb.addParameter("overlaps", getRangeQueryString(loc));
            }
            for (Das2LocationRefI loc : insides) {
                URI seguri = loc.getSegmentURI();
                if (!segment_uris.contains(seguri)) {
                    qsb.addParameter("segment", seguri.toString());
                    segment_uris.add(seguri);
                }
                qsb.addParameter("inside", getRangeQueryString(loc));
            }
            for (Das2LocationRefI loc : excludes) {
                URI seguri = loc.getSegmentURI();
                if (!segment_uris.contains(seguri)) {
                    qsb.addParameter("segment", seguri.toString());
                    segment_uris.add(seguri);
                }
                qsb.addParameter("excludes", getRangeQueryString(loc));
            }
        }
        if (types != null) {
            for (URI typeuri : types) {
                qsb.addParameter("type", typeuri.toString());
            }
        }
        if (coords != null) {
            for (URI coorduri : coords) {
                qsb.addParameter("coordinates", coorduri.toString());
            }
        }
        if (links != null) {
            for (URI linkuri : links) {
                qsb.addParameter("link", linkuri.toString());
            }
        }
        if (names != null) {
            for (String name : names) {
                qsb.addParameter("name", name);
            }
        }
        if (notes != null) {
            for (String note : notes) {
                qsb.addParameter("note", note);
            }
        }
        if (non_standard_params != null) {
            for (String param_name : non_standard_params.keySet()) {
                List<String> param_values = non_standard_params.get(param_name);
                for (String param_val : param_values) {
                    qsb.addParameter(param_name, param_val);
                }
            }
        }
        if (format != null) {
            qsb.addParameter("format", format);
        }
        String query_string = qsb.getQueryString();
        return query_string;
    }

    public static void main(String[] args) throws URISyntaxException {
        String features_url = "file:./data/netaffx_das2_features.xml";
        Das2FeaturesCapability cap = new Das2FeaturesCapability(new URI(features_url), features_url, null, null);
        Das2FeaturesResponseI features_response = cap.getFeatures();
    }
}
