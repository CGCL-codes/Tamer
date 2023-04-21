package au.edu.archer.metadata.mde.schema;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import au.edu.archer.metadata.mde.util.MDERecord;
import au.edu.archer.metadata.mde.util.ResolverException;
import au.edu.archer.metadata.spi.SchemaRepository;
import au.edu.archer.metadata.spi.SchemaRepositoryException;

/**
 * This class performs the task of determining the effective MSS schema for a given
 * XML metadata record.
 *
 * @author scrawley@itee.uq.edu.au
 *
 */
public class XMLNSSchemaResolverEntry extends SchemaResolverEntry {

    private static String XSI_PREFIX = "xsi";

    private static String XSI_URL = "http://www.w3.org/2001/XMLSchema-instance";

    private static String XSI_URL_SLASH = XSI_URL + "/";

    private static String XMLNS = "xmlns";

    private static String XMLNS_COLON = XMLNS + ":";

    private static String SCHEMA_LOCATION = "schemaLocation";

    private static String COLON_SCHEMA_LOCATION = ":" + SCHEMA_LOCATION;

    private Logger logger = Logger.getLogger(XMLNSSchemaResolverEntry.class);

    public XMLNSSchemaResolverEntry() {
    }

    public XMLNSSchemaResolverEntry(SchemaRepository repos) {
        setProvider(repos);
    }

    /**
     * The behavior of the 'matches' method for an XMLNSSchemaResolverEntry is equivalent
     * to the following algorithm:
     * <ol>
     * <li>Check to see if the entry is applicable based on the criteria of
     *     the parent class; i.e. if {@code super.matches(record)} returns
     *     {@code true}.  If not, return {@code false}.
     * <li>If the record already has a schema key (e.g. set by the metadata store
     *     provider when the record was loaded), return {@code true}.
     * <li>See if we can extract a schema key from the record's XML namespace
     *     declarations.  If not, return {@code false}.
     * <li>We extracted a schema key, so set it as the record's 'schemaKey'
     *     attribute and return {@code true}.
     * </ol>
     */
    @Override
    public boolean matches(MDERecord record) {
        if (!super.matches(record)) {
            return false;
        }
        if (record.getSchemaKey() != null) {
            return true;
        }
        String schemaKey = resolveSchema(record);
        record.setSchemaKey(schemaKey);
        return schemaKey != null;
    }

    /**
     * Attempt to identify the "one true schema" for a DOM containing a metadata record.  We
     * iterate over the declared namespaces in the root XML element, attempting to map them
     * to schema locations, and thence to MSS schemas.  The first one that maps is the "one
     * true schema".
     *
     * @param key the record's key ... which is ignored by this resolver.
     * @param doc the record DOM
     * @return the schema URI or {@code null}
     */
    private String resolveSchema(MDERecord record) {
        List<String> nsPrefixList = new ArrayList<String>();
        Map<String, String> namespaceMap = new HashMap<String, String>();
        Map<String, String> schemaLocationMap = new HashMap<String, String>();
        try {
            extractSchemaLocations(record.getDOM(), nsPrefixList, namespaceMap, schemaLocationMap);
        } catch (ResolverException ex) {
            logger.debug(ex.getMessage());
            return null;
        }
        if (nsPrefixList.size() == 0) {
            logger.debug("Metadata record has no 'xmlns' attributes");
            return null;
        }
        if (schemaLocationMap.size() == 0) {
            logger.debug("Metadata record has no useable 'schemaLocation' attribute");
            return null;
        }
        for (String nsPrefix : nsPrefixList) {
            String namespaceURI = namespaceMap.get(nsPrefix);
            String schemaURI = schemaLocationMap.get(namespaceURI);
            try {
                if (schemaURI != null && getProvider().lookup(schemaURI) != null) {
                    return schemaURI;
                }
            } catch (SchemaRepositoryException ex) {
            }
        }
        logger.debug("Metadata record has no recognizable MSS schema");
        return null;
    }

    /**
     * Extract namespace and schema location information from a DOM.
     *
     * @param doc the record DOM
     * @param nsPrefixList <b>out</b> the namespace prefixes in the order they are encountered
     * @param namespaceMap <b>out</b> this will map from namespace prefixes to namespace URIs
     * @param schemaLocationMap <b>out</b> this will map from namespace URIs to schema location URIs
     * @throws ResolverException
     */
    private void extractSchemaLocations(Document doc, List<String> nsPrefixList, Map<String, String> namespaceMap, Map<String, String> schemaLocationMap) throws ResolverException {
        Set<String> xsiPrefixes = new HashSet<String>();
        List<String> candidateSchemaLocationAttrNames = new ArrayList<String>(1);
        Element root = doc.getDocumentElement();
        NamedNodeMap attrs = root.getAttributes();
        for (int i = 0; i < attrs.getLength(); i++) {
            Node attr = attrs.item(i);
            String name = attr.getNodeName();
            if (name.equals(XMLNS)) {
                String schemaUri = attr.getNodeValue();
                nsPrefixList.add("");
                namespaceMap.put("", schemaUri);
            } else if (name.startsWith(XMLNS_COLON)) {
                String prefix = name.substring(6);
                String schemaUri = attr.getNodeValue();
                if (schemaUri.equals(XSI_URL) || schemaUri.equals(XSI_URL_SLASH)) {
                    xsiPrefixes.add(prefix);
                } else {
                    nsPrefixList.add(prefix);
                    namespaceMap.put(prefix, schemaUri);
                }
            } else if (name.endsWith(SCHEMA_LOCATION)) {
                candidateSchemaLocationAttrNames.add(name);
            }
        }
        if (xsiPrefixes.isEmpty()) {
            xsiPrefixes.add(XSI_PREFIX);
        }
        for (String name : candidateSchemaLocationAttrNames) {
            boolean matched = false;
            for (String xsiPrefix : xsiPrefixes) {
                if (name.equals(xsiPrefix + COLON_SCHEMA_LOCATION)) {
                    matched = true;
                    break;
                }
            }
            if (!matched && !name.equals(XSI_URL_SLASH + SCHEMA_LOCATION)) {
                continue;
            }
            if (!schemaLocationMap.isEmpty()) {
                throw new ResolverException("Metadata record has multiple schemaLocation attributes");
            }
            String schemaLocationsAttrib = root.getAttribute(name);
            String locations[] = schemaLocationsAttrib.split("\\s+");
            for (int j = 0; j + 1 < locations.length; j += 2) {
                String schemaUri = locations[j];
                String schemaLoc = locations[j + 1];
                schemaLocationMap.put(schemaUri, schemaLoc);
            }
        }
    }
}
