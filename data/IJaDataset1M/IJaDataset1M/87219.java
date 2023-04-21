package genomancer.trellis.das2.xml;

import java.util.Date;
import java.util.List;
import genomancer.trellis.das2.model.Das2CapabilityI;
import genomancer.trellis.das2.model.Das2CoordinatesI;
import genomancer.trellis.das2.model.Das2FormatI;
import genomancer.trellis.das2.model.Das2LinkI;
import genomancer.trellis.das2.model.Das2PropertyI;
import genomancer.trellis.das2.model.Das2SourceI;
import genomancer.trellis.das2.model.Das2SourcesResponseI;
import genomancer.trellis.das2.model.Das2VersionI;
import java.io.OutputStream;
import java.io.Writer;
import java.net.URI;
import java.util.Map;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.codehaus.staxmate.out.SMOutputDocument;
import org.codehaus.staxmate.out.SMOutputElement;
import org.jdom.JDOMException;

public class SourcesXmlWriter extends AbstractDas2XmlWriter {

    Das2SourcesResponseI sources_holder;

    public SourcesXmlWriter(Das2SourcesResponseI sources_holder, XMLStreamWriter xw) throws XMLStreamException {
        super(xw);
        this.sources_holder = sources_holder;
        this.setXmlBase(sources_holder.getBaseURI());
    }

    public SourcesXmlWriter(Das2SourcesResponseI sources_holder, OutputStream ostr) throws XMLStreamException {
        this(sources_holder, getFactory().createXMLStreamWriter(ostr));
    }

    public SourcesXmlWriter(Das2SourcesResponseI sources_holder, Writer writ) throws XMLStreamException {
        this(sources_holder, getFactory().createXMLStreamWriter(writ));
    }

    public void writeSourcesDocument() throws XMLStreamException, JDOMException {
        writeSourcesDocument(null);
    }

    public void writeSourcesDocument(Map<String, List<String>> filter_params) throws XMLStreamException, JDOMException {
        SourcesFilter filter = new SourcesFilter(filter_params);
        List<Das2SourceI> sources = sources_holder.getSources();
        String maintainer = sources_holder.getMaintainerEmail();
        List<Das2LinkI> links = sources_holder.getLinks();
        XMLStreamWriter xw = getXMLStreamWriter();
        SMOutputDocument doc = getSMOutputDocument();
        SMOutputElement sourcesel = doc.addElement("SOURCES");
        xw.writeAttribute("xmlns", DAS2_NAMESPACE);
        if (xml_base != null) {
            xw.writeAttribute("xml:base", xml_base.toString());
        }
        if (links != null) {
            for (Das2LinkI link : links) {
                writeLink(link, sourcesel);
            }
        }
        if (maintainer != null) {
            sourcesel.addElement("MAINTAINER").addAttribute("email", maintainer);
        }
        for (Das2SourceI source : sources) {
            if (filter.passesFilter(source)) {
                writeSource(source, sourcesel, filter);
            }
        }
        doc.closeRoot();
        xw.close();
    }

    public void writeSource(Das2SourceI source, SMOutputElement parent, SourcesFilter filter) throws XMLStreamException, JDOMException {
        SMOutputElement sel = parent.addElement("SOURCE");
        writeCommonAttributes(source, sel);
        String maintainer = source.getMaintainerEmail();
        if (maintainer != null) {
            sel.addElement("MAINTAINER").addAttribute("email", maintainer);
        }
        List<Das2VersionI> versions = source.getVersions();
        for (Das2VersionI version : versions) {
            if (filter.passesFilter(version)) {
                writeVersion(version, sel);
            }
        }
        writeCommonElements(source, sel);
    }

    public void writeVersion(Das2VersionI version, SMOutputElement parent) throws XMLStreamException, JDOMException {
        SMOutputElement vel = parent.addElement("VERSION");
        writeCommonAttributes(version, vel);
        String maintainer = version.getMaintainerEmail();
        if (maintainer != null) {
            vel.addElement("MAINTAINER").addAttribute("email", maintainer);
        }
        List<Das2CoordinatesI> coordinates = version.getCoordinates();
        for (Das2CoordinatesI coords : coordinates) {
            writeCoordinates(coords, vel);
        }
        List<Das2CapabilityI> capabilities = version.getCapabilities();
        for (Das2CapabilityI cap : capabilities) {
            writeCapability(cap, vel);
        }
        writeCommonElements(version, vel);
    }

    public void writeCoordinates(Das2CoordinatesI coords, SMOutputElement parent) throws XMLStreamException {
        String uri = coords.getLocalURIString();
        String type = coords.getCoordinateType();
        String authority = coords.getAuthority();
        String build_version = coords.getBuildVersion();
        String taxid = coords.getTaxonomyID();
        Date created = coords.getCreated();
        String test_range = coords.getTestRange();
        SMOutputElement cel = parent.addElement("COORDINATES");
        cel.addAttribute("uri", uri);
        cel.addAttribute("source", type);
        cel.addAttribute("authority", authority);
        if (build_version != null) {
            cel.addAttribute("version", build_version);
        }
        if (taxid != null) {
            cel.addAttribute("taxid", taxid);
        }
        if (created != null) {
            cel.addAttribute("created", created.toString());
        }
        if (test_range != null) {
            cel.addAttribute("test_range", test_range);
        }
    }

    public void writeCapability(Das2CapabilityI cap, SMOutputElement parent) throws XMLStreamException, JDOMException {
        String type = cap.getType();
        String query_uri = cap.getLocalURIString();
        SMOutputElement capel = parent.addElement("CAPABILITY");
        capel.addAttribute("type", type);
        capel.addAttribute("query_uri", query_uri);
        for (Das2FormatI format : cap.getFormats()) {
            writeFormat(format, capel);
        }
        for (String extension : cap.getSupportedExtensions()) {
            capel.addElement("SUPPORTS").addAttribute("name", extension);
        }
        writeAdditionalData(cap.getAdditionalData(), capel);
    }

    public class SourcesFilter {

        boolean has_params;

        List<String> label_params = null;

        List<String> organism_params = null;

        List<String> authority_params = null;

        List<String> capability_params = null;

        List<String> coord_type_params = null;

        public SourcesFilter(Map<String, List<String>> filter_params) {
            if (filter_params == null || filter_params.size() == 0) {
                has_params = false;
            } else {
                has_params = true;
                capability_params = filter_params.get("capability");
                coord_type_params = filter_params.get("type");
                organism_params = filter_params.get("organism");
                authority_params = filter_params.get("authority");
                label_params = filter_params.get("label");
            }
        }

        /**
	 *  Das2SourceI passes filter if any version in the source passes filter
	 */
        public boolean passesFilter(Das2SourceI source) {
            if (!has_params) {
                return true;
            }
            boolean passes_filter = false;
            for (Das2VersionI version : source.getVersions()) {
                if (passesFilter(version)) {
                    passes_filter = true;
                    break;
                }
            }
            return passes_filter;
        }

        public boolean passesFilter(Das2VersionI version) {
            if (!has_params) {
                return true;
            }
            boolean passes_filter = true;
            if (passes_filter && capability_params != null && capability_params.size() != 0) {
                passes_filter = false;
                CAP_LOOP: for (Das2CapabilityI cap : version.getCapabilities()) {
                    String cap_type = cap.getType();
                    for (String cap_filter : capability_params) {
                        if (cap_filter.equals(cap_type)) {
                            passes_filter = true;
                            break CAP_LOOP;
                        }
                    }
                }
            }
            if (passes_filter && coord_type_params != null && coord_type_params.size() != 0) {
                passes_filter = false;
                CTYPE_LOOP: for (Das2CoordinatesI coord : version.getCoordinates()) {
                    String ctype = coord.getCoordinateType();
                    for (String ctype_filter : coord_type_params) {
                        if (ctype_filter.equals(ctype)) {
                            passes_filter = true;
                            break CTYPE_LOOP;
                        }
                    }
                }
            }
            if (passes_filter && organism_params != null && organism_params.size() != 0) {
                passes_filter = false;
                ORG_LOOP: for (Das2CoordinatesI coord : version.getCoordinates()) {
                    String taxid = coord.getTaxonomyID();
                    for (String org_filter : organism_params) {
                        if (org_filter.equals(taxid)) {
                            passes_filter = true;
                            break ORG_LOOP;
                        }
                    }
                }
            }
            if (passes_filter && authority_params != null && authority_params.size() != 0) {
                passes_filter = false;
                AUTH_LOOP: for (Das2CoordinatesI coord : version.getCoordinates()) {
                    String auth = coord.getAuthority();
                    for (String auth_filter : authority_params) {
                        if (auth_filter.equals(auth)) {
                            passes_filter = true;
                            break AUTH_LOOP;
                        }
                    }
                }
            }
            if (passes_filter && label_params != null && label_params.size() != 0) {
                passes_filter = false;
                LABEL_LOOP: for (Das2PropertyI prop : version.getProperties()) {
                    if (prop.getKey().equals("label")) {
                        for (String label_filter : label_params) {
                            if (label_filter.equals(prop.getValue())) {
                                passes_filter = true;
                                break LABEL_LOOP;
                            }
                        }
                    }
                }
            }
            return passes_filter;
        }
    }
}
