package genomancer.vine.das2.client.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import genomancer.trellis.das2.model.Das2CapabilityI;
import genomancer.trellis.das2.model.Das2CoordinatesI;
import genomancer.trellis.das2.model.Das2FormatI;
import genomancer.trellis.das2.model.Das2LinkI;
import genomancer.trellis.das2.model.Das2PropertyI;
import genomancer.trellis.das2.model.Das2SourceI;
import genomancer.trellis.das2.model.Das2SourcesResponseI;
import genomancer.trellis.das2.model.Das2VersionI;
import genomancer.vine.das2.client.modelimpl.Das2Coordinates;
import genomancer.vine.das2.client.modelimpl.Das2FeaturesCapability;
import genomancer.vine.das2.client.modelimpl.Das2GenericCapability;
import genomancer.vine.das2.client.modelimpl.Das2SegmentsCapability;
import genomancer.vine.das2.client.modelimpl.Das2Source;
import genomancer.vine.das2.client.modelimpl.Das2SourcesResponse;
import genomancer.vine.das2.client.modelimpl.Das2TypesCapability;
import genomancer.vine.das2.client.modelimpl.Das2Version;
import java.util.Date;
import org.jdom.input.XmlFragmentBuilder;

/**
 *  Assumes that spec has been modified to 
 *  restrict xml:base and xmlns attributes to top-level element (<SOURCES>), 
 *  (unless they're in the "extensions", since any well-formed XML is okay there)
 *
 *  only uses first DAS2_NAMESPACE declaration
 *  Therefore assumes DAS2_NAMESPACE is not declared for both the default namespace "xmlns" 
 *     and additional namespace prefix "xmlns:prefix" -- must be one or the other
 */
public class SourcesXmlReader extends AbstractDas2XmlReader {

    /**
     *  doc_uri SHOULD be an absolute URI
     *  need the doc_uri for determining XmlBase if no "xml:base" attribute, or if "xml:base" is relative
     *  could leave this out if:
     *     a) "xml:base" required
     *     b) "query" required attribute added to SOURCES whose value is original query URL
     */
    public SourcesXmlReader(InputStream istream, URI doc_uri) throws XMLStreamException {
        super(istream, doc_uri);
    }

    public static Das2SourcesResponseI readSourcesDocument(InputStream istream, URI doc_uri) throws XMLStreamException, URISyntaxException {
        SourcesXmlReader sources_reader = new SourcesXmlReader(istream, doc_uri);
        Das2SourcesResponseI response = sources_reader.readSourcesDocument();
        sources_reader.close();
        return response;
    }

    public Das2SourcesResponseI readSourcesDocument() throws XMLStreamException, URISyntaxException {
        List<Das2SourceI> sources = new ArrayList<Das2SourceI>();
        String maintainer_email = null;
        ArrayList<Das2LinkI> links = new ArrayList<Das2LinkI>();
        System.out.println("XMLInputFactory: " + ifactory);
        System.out.println("XMLStreamReader: " + xreader);
        System.out.println("initial xml_base: " + xml_base);
        frag_builder = new XmlFragmentBuilder();
        while (xreader.hasNext()) {
            int eventid = xreader.next();
            if (eventid == XMLStreamConstants.START_ELEMENT) {
                String elname = xreader.getLocalName();
                if (elname.equals("SOURCE")) {
                    Das2SourceI source = parseSourceElement();
                    sources.add(source);
                } else if (elname.equals("SOURCES")) {
                    setNamespaces();
                    setXmlBase();
                } else if (elname.equals("MAINTAINER")) {
                    maintainer_email = xreader.getAttributeValue(ns, "email");
                } else if (elname.equals("LINK")) {
                    Das2LinkI link = parseLinkElement();
                    links.add(link);
                }
            } else if (eventid == XMLStreamConstants.START_DOCUMENT) {
            } else if (eventid == XMLStreamConstants.END_DOCUMENT) {
            }
        }
        Das2SourcesResponseI response = new Das2SourcesResponse(xml_base, sources, maintainer_email, links);
        return response;
    }

    protected Das2SourceI parseSourceElement() throws XMLStreamException {
        String uri = xreader.getAttributeValue(ns, "uri");
        if (DEBUG) {
            System.out.println("SOURCE: " + uri);
        }
        String title = xreader.getAttributeValue(ns, "title");
        String description = xreader.getAttributeValue(ns, "description");
        String info_url = xreader.getAttributeValue(ns, "doc_href");
        Das2Source source = new Das2Source(xml_base, uri, title, description, info_url);
        while (xreader.hasNext()) {
            int eventid = xreader.next();
            if (eventid == XMLStreamConstants.START_ELEMENT) {
                String elname = xreader.getLocalName();
                if (elname.equals("VERSION")) {
                    Das2VersionI version = parseVersionElement(source);
                    source.addVersion(version);
                } else if (elname.equals("MAINTAINER")) {
                    String email = xreader.getAttributeValue(ns, "email");
                    if (email != null) {
                        source.setMaintainerEmail(email);
                    }
                } else {
                    org.jdom.Element xml_fragment = frag_builder.buildXmlFragment(xreader);
                    source.addAdditionalData(xml_fragment);
                }
            } else if (eventid == XMLStreamConstants.END_ELEMENT) {
                String elname = xreader.getLocalName();
                if (elname.equals("SOURCE")) {
                    break;
                }
            }
        }
        return source;
    }

    protected Das2VersionI parseVersionElement(Das2Source source) throws XMLStreamException {
        String uri = xreader.getAttributeValue(ns, "uri");
        if (DEBUG) {
            System.out.println("VERSION: " + uri);
        }
        String title = xreader.getAttributeValue(ns, "title");
        String description = xreader.getAttributeValue(ns, "description");
        String info_url = xreader.getAttributeValue(ns, "doc_href");
        String created = xreader.getAttributeValue(ns, "created");
        String modified = xreader.getAttributeValue(ns, "modified");
        Das2Version version = new Das2Version(source, uri, title, description, info_url, null, null);
        while (xreader.hasNext()) {
            int eventid = xreader.next();
            if (eventid == XMLStreamConstants.START_ELEMENT) {
                String elname = xreader.getLocalName();
                if (elname.equals("CAPABILITY")) {
                    Das2CapabilityI cap = parseCapabilityElement(version);
                    version.addCapability(cap);
                } else if (elname.equals("COORDINATES")) {
                    Das2CoordinatesI coords = parseCoordinatesElement();
                    version.addCoordinates(coords);
                } else if (elname.equals("PROP")) {
                    Das2PropertyI prop = parsePropertyElement();
                    version.addProperty(prop);
                } else if (elname.equals("MAINTAINER")) {
                    String email = xreader.getAttributeValue(ns, "email");
                    if (email != null) {
                        version.setMaintainerEmail(email);
                    }
                } else {
                    org.jdom.Element xml_fragment = frag_builder.buildXmlFragment(xreader);
                    version.addAdditionalData(xml_fragment);
                }
            } else if (eventid == XMLStreamConstants.END_ELEMENT) {
                String elname = xreader.getLocalName();
                if (elname.equals("VERSION")) {
                    break;
                }
            }
        }
        return version;
    }

    protected Das2CapabilityI parseCapabilityElement(Das2Version version) throws XMLStreamException {
        String query_uri = xreader.getAttributeValue(ns, "query_uri");
        if (DEBUG) {
            System.out.println("CAPABILITY: " + query_uri);
        }
        String type = xreader.getAttributeValue(ns, "type");
        if (DEBUG) {
            System.out.println("    type: " + type);
        }
        Das2Coordinates coords = null;
        String coords_local_uri = xreader.getAttributeValue(ns, "coordinates");
        if (coords_local_uri != null) {
            URI coords_uri = xml_base.resolve(coords_local_uri);
            if (coords_uri != null) {
                coords = (Das2Coordinates) version.getCoordinates(coords_uri);
            }
        }
        Das2GenericCapability cap = makeCapability(xml_base, query_uri, type, version, coords);
        if (DEBUG) {
            System.out.println("    cap: " + cap);
        }
        while (xreader.hasNext()) {
            int eventid = xreader.next();
            if (eventid == XMLStreamConstants.START_ELEMENT) {
                String elname = xreader.getLocalName();
                if (elname.equals("FORMAT")) {
                    Das2FormatI format = parseFormatElement();
                    cap.addFormat(format);
                } else if (elname.equals("SUPPORTS")) {
                    String name = xreader.getAttributeValue(ns, "name");
                    cap.addSupportedExtension(name);
                } else {
                    org.jdom.Element xml_fragment = frag_builder.buildXmlFragment(xreader);
                    cap.addAdditionalData(xml_fragment);
                }
            } else if (eventid == XMLStreamConstants.END_ELEMENT) {
                String elname = xreader.getLocalName();
                if (elname.equals("CAPABILITY")) {
                    break;
                }
            }
        }
        return cap;
    }

    protected Das2GenericCapability makeCapability(URI xml_base, String local_query_uri, String type, Das2Version version, Das2Coordinates coords) {
        Das2GenericCapability cap = null;
        if (type.equals("segments")) {
            cap = new Das2SegmentsCapability(xml_base, local_query_uri, version, coords);
        } else if (type.equals("types")) {
            cap = new Das2TypesCapability(xml_base, local_query_uri, version, coords);
        } else if (type.equals("features")) {
            cap = new Das2FeaturesCapability(xml_base, local_query_uri, version, coords);
        } else {
            cap = new Das2GenericCapability(xml_base, local_query_uri, type, version, coords);
        }
        return cap;
    }

    protected Das2CoordinatesI parseCoordinatesElement() {
        String local_uri = xreader.getAttributeValue(ns, "uri");
        String taxid = xreader.getAttributeValue(ns, "taxid");
        String coord_type = xreader.getAttributeValue(ns, "source");
        String authority = xreader.getAttributeValue(ns, "authority");
        String build_version = xreader.getAttributeValue(ns, "version");
        String created = xreader.getAttributeValue(ns, "created");
        String test_range = xreader.getAttributeValue(ns, "test_range");
        Date creation_date = null;
        if (created != null) {
            creation_date = new Date(created);
        }
        Das2CoordinatesI coords = new Das2Coordinates(xml_base, local_uri, taxid, coord_type, authority, build_version, creation_date, test_range);
        return coords;
    }

    public static void main(String[] args) throws XMLStreamException, FileNotFoundException, URISyntaxException {
        String test_file = "./data/das2_registry_sources.mod.xml";
        FileInputStream fis = new FileInputStream(new File(test_file));
        readSourcesDocument(fis, new URI("file:" + test_file));
    }
}
