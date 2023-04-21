package com.entelience.probe.patch;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.xerces.util.XMLStringBuffer;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XMLLocator;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.XNIException;
import com.entelience.objects.vuln.AdvisoryCertIst;
import com.entelience.probe.ProbeXmlParser;
import com.entelience.util.DateHelper;
import com.entelience.probe.asset.Vendors;

/**
 * XML basic parser for the CertIst advisories which are <i>supposed</i> to
 * follow the format of the <a href='www.eispp.org'>EISPP</a>.  
 * 
 * @see CertIstImport
 * @todo This should really use a FSM an not 'in_whatever' booleans
 */
public class CertIstXmlParser extends ProbeXmlParser {

    public static final String EISPP_VERSION = "1.2";

    private CertIstImport imp = null;

    private static final String[] _rating = { "not_rated", "not rated", "very_low", "low", "medium", "high", "very_high" };

    private AdvisoryCertIst _advisory = new AdvisoryCertIst();

    private String _lang;

    private Vendors _vendors;

    /** 
     * Returns the rating on 4 level scale, or 0 
     * if the rating is not compliant or not rated.
     */
    private int getRating(String rating) {
        for (int i = 0; i < _rating.length; ++i) {
            if (rating.equalsIgnoreCase(_rating[i])) {
                return i;
            }
        }
        _logger.warn("Unknown rating: " + rating + ", not compliant with EISPP " + EISPP_VERSION);
        return 0;
    }

    /**
     * Document starts
     */
    public void startDocument(XMLLocator locator, String encoding, NamespaceContext namespaceContext, Augmentations augs) throws XNIException {
        _logger.debug("Starting parsing of XML document");
    }

    /** 
     * Document ends
     */
    public void endDocument(Augmentations augs) throws XNIException {
        _logger.debug("End of XML document reached");
    }

    /** Empty element. */
    public void emptyElement(QName element, XMLAttributes attrs, Augmentations augs) throws XNIException {
        _logger.debug("Found an empty element " + element.localpart);
    }

    /** Comment */
    public void comment(XMLString text, Augmentations augs) {
        _logger.debug("Found a comment " + text);
    }

    /**
     * dumpAttributes
     * dump attributes in to a Map (data)
     */
    private void dumpAttributes(XMLAttributes attrs) {
        att_data = new HashMap<String, String>();
        for (int i = 0; i < attrs.getLength(); ++i) {
            String qname = attrs.getQName(i).trim();
            String value = attrs.getValue(i).trim();
            att_data.put(qname, value);
        }
    }

    private Object getAttribute(String key) {
        return (null == att_data.get(key) ? "" : att_data.get(key));
    }

    /**
     * startBuffer - activate buffer to append xml content
     */
    private void startBuffer() {
        if (buffer_active) _logger.debug("OUPS!");
        buffer = new XMLStringBuffer();
        buffer_active = true;
    }

    /**
     * endBuffer - unactivate buffer
     */
    private void endBuffer() {
        buffer_active = false;
    }

    public void characters(XMLString xtext, Augmentations augs) throws XNIException {
        if (buffer_active) {
            buffer.append(xtext);
        }
    }

    /**
     * getBuffer - return a string with xml content
     */
    private String getBuffer() {
        return buffer.toString().trim();
    }

    /**
     * startVuln 
     */
    private void startVuln() throws Exception {
        if (in_advisory) throw new Exception("EISPP-Advisory elements can not be nested");
        if (!EISPP_VERSION.equals((String) getAttribute("version"))) _logger.warn("Unsupported version of the EISSP format!. Expected " + EISPP_VERSION + ", Got " + getAttribute("version"));
        if (!"CERT-IST".equalsIgnoreCase((String) getAttribute("issuer"))) _logger.warn("This probe only support the CERT-IST");
        _advisory = new AdvisoryCertIst();
        _advisory.setIssuer((String) getAttribute("issuer"));
        _advisory.setVersion((String) getAttribute("version"));
        in_advisory = true;
    }

    /**
     * startRef - activate vuln_ref
     */
    private String ref_desc;

    private String ref_url;

    private String revisionVersion;

    private String revisionDescription;

    private Date revisionDate;

    /**
     * Main functions for the parser, triggered on the start of each
     * element.
     * Currently this processes the &lt;Bullentin&gt; tag.
     */
    private Map<String, String> att_data;

    private XMLStringBuffer buffer;

    private boolean in_advisory = false;

    private boolean buffer_active = false;

    private boolean in_id = false;

    private boolean in_history = false;

    private boolean in_class = false;

    private boolean in_system = false;

    private boolean in_desc = false;

    private boolean in_sol = false;

    private boolean in_vuln = false;

    private boolean in_adds = false;

    private StringBuffer solution;

    private boolean read_text = false;

    public void startElt(QName element, XMLAttributes attrs, Augmentations augs) throws XNIException {
        dumpAttributes(attrs);
        try {
            if ("EISPP-Advisory".equals(element.localpart) && in_advisory) throw new Exception("Element outside of a main advisory tag");
            if ("EISPP-Advisory".equals(element.localpart)) {
                startVuln();
            } else if ("Id_Data".equals(element.localpart)) {
                in_id = true;
            } else if ("ref_num".equals(element.localpart)) {
                if (in_id) {
                    startBuffer();
                } else throw new Exception("refnum element outside of Id_Data");
            } else if ("title".equals(element.localpart)) {
                if (in_id) read_text = true;
            } else if ("History_Data".equals(element.localpart)) {
                in_history = true;
            } else if ("change_descr".equals(element.localpart)) {
                if (in_history) {
                    _advisory.setVersion((String) getAttribute("version"));
                    if ("1.0".equals((String) getAttribute("version"))) {
                        _advisory.setPublish_date((String) getAttribute("date"));
                    } else {
                        _advisory.setChange_date((String) getAttribute("date"));
                    }
                    revisionDate = DateHelper.YYYYMMDD_slashOrNull((String) getAttribute("date"));
                    revisionVersion = (String) getAttribute("version");
                    read_text = true;
                } else {
                    throw new Exception("change_desc element outside of History_Data");
                }
            } else if ("Vulnerability_Class".equals(element.localpart)) {
                in_class = true;
            } else if ("risk".equals(element.localpart)) {
                if (in_class) {
                    startBuffer();
                }
            } else if ("System_Information".equals(element.localpart)) {
                in_system = true;
            } else if ("affected_platform".equals(element.localpart) || "affected_software".equals(element.localpart) || "remarks".equals(element.localpart)) {
                if (in_system) {
                    read_text = true;
                }
            } else if ("system_id".equals(element.localpart)) {
                if (in_system) {
                    startBuffer();
                }
            } else if ("Description".equals(element.localpart)) {
                in_desc = true;
            } else if ("technical_context".equals(element.localpart) || "description".equals(element.localpart) || "technical_information".equals(element.localpart)) {
                if (in_desc) {
                    read_text = true;
                }
            } else if ("Solution".equals(element.localpart)) {
                in_sol = true;
                solution = new StringBuffer();
            } else if ("sol_title".equals(element.localpart)) {
                if (in_sol) {
                    read_text = true;
                }
            } else if ("sol_descr".equals(element.localpart)) {
                if (in_sol) {
                    read_text = true;
                }
            } else if ("reference".equals(element.localpart)) {
                if (in_adds || in_vuln || in_sol) {
                    reference();
                }
            } else if ("Vulnerability_ID".equals(element.localpart)) {
                in_vuln = true;
            } else if ("Additional_Resources".equals(element.localpart)) {
                in_adds = true;
            } else if ("ref_title".equals(element.localpart)) {
                if (in_vuln || in_adds || in_sol) {
                    read_text = true;
                }
            } else if ("uri".equals(element.localpart)) {
                if ((in_vuln || in_adds || in_sol) && _lang.equalsIgnoreCase((String) getAttribute("xml:lang"))) {
                    startBuffer();
                }
            } else if ("FreeText".equals(element.localpart) || "FormattedText".equals(element.localpart)) {
                if (read_text && _lang.equalsIgnoreCase((String) getAttribute("xml:lang"))) startBuffer();
            } else if ("cvss_base_score".equals(element.localpart) || "cvss_base_vector".equals(element.localpart) || "cvss_temporal_score".equals(element.localpart) || "cvss_temporal_vector".equals(element.localpart)) {
                startBuffer();
            } else {
                addUnknownTag(element);
            }
        } catch (Exception e) {
            _logger.error("Fatal error with xml parser declared.", e);
            XNIException ex = new XNIException(e.getMessage());
            ex.initCause(e);
            throw ex;
        }
    }

    /**
     * Sanitize the product line : 
     * ignores <i>toutes</i>, <i>anterieures</i> and <i>1.1.x</i>
     * fix the <i>Noyau Linux</i>
     *
     * @return null if this line should be ignored.
     */
    private String sanitizeProductLine(String line) {
        String out = line.trim();
        if (out.length() < 4 || out.endsWith(".x") || out.indexOf("Toutes") != -1 || out.endsWith("rieures")) {
            _logger.warn("Ignoring abnormal system_id " + out + " for advisory " + _advisory.getRef_num());
            return null;
        }
        out = out.replaceAll("Red Hat", "RedHat");
        if (out.startsWith("Noyau Linux")) {
            _logger.debug("Fixing Noyau Linux");
            out = "Linux kernel " + out.substring(11);
        }
        return out;
    }

    public void endElement(QName element, Augmentations augs) throws XNIException {
        try {
            if ("EISPP-Advisory".equals(element.localpart)) {
                in_advisory = false;
                imp.setAdvisory(_advisory);
                imp.storeAdvisory();
            } else if ("Id_Data".equals(element.localpart)) {
                in_id = false;
            } else if ("ref_num".equals(element.localpart)) {
                endBuffer();
                _advisory.setRef_num(getBuffer());
            } else if ("title".equals(element.localpart)) {
                endBuffer();
                _advisory.setTitle(getBuffer());
            } else if ("History_Data".equals(element.localpart)) {
                in_history = false;
            } else if ("change_descr".equals(element.localpart)) {
                revisionDescription = getBuffer();
                _advisory.addHistory(revisionVersion, revisionDate, revisionDescription);
            } else if ("Vulnerability_Class".equals(element.localpart)) {
                in_class = false;
            } else if ("risk".equals(element.localpart)) {
                endBuffer();
                _advisory.setRisk(getRating(getBuffer()));
            } else if ("System_Information".equals(element.localpart)) {
                in_system = false;
            } else if ("affected_platform".equals(element.localpart)) {
                _advisory.setComments(_advisory.getComments() + (("en".equals(_lang) ? "Affected platform :\n" : "Plateforme impactées :\n") + getBuffer() + "\n\n"));
            } else if ("affected_software".equals(element.localpart)) {
                _advisory.setComments(_advisory.getComments() + (("en".equals(_lang) ? "Affected software :\n" : "Logiciels impactés :\n") + getBuffer() + "\n\n"));
            } else if ("remarks".equals(element.localpart)) {
                _advisory.setComments(_advisory.getComments() + (getBuffer() + "\n\n"));
            } else if ("system_id_list".equals(element.localpart)) {
                _logger.debug("Found " + _advisory.getProdList().size());
            } else if ("system_id".equals(element.localpart)) {
                endBuffer();
                String vpv = sanitizeProductLine(getBuffer());
                if (vpv != null) _advisory.getProdList().add(_vendors.productLine(vpv));
            } else if ("Description".equals(element.localpart)) {
                in_desc = false;
                endBuffer();
            } else if ("technical_context".equals(element.localpart)) {
                _advisory.setContext(getBuffer());
            } else if ("description".equals(element.localpart)) {
                _advisory.setDescription(getBuffer());
            } else if ("technical_information".equals(element.localpart)) {
                _advisory.setTechnical(getBuffer());
            } else if ("Solution".equals(element.localpart)) {
                in_sol = false;
                _advisory.setSolution(solution.toString());
            } else if ("sol_title".equals(element.localpart)) {
                solution.append(getBuffer()).append('\n');
            } else if ("sol_descr".equals(element.localpart)) {
                solution.append(getBuffer()).append('\n');
            } else if ("reference".equals(element.localpart)) {
                if (in_vuln || in_adds || in_sol) {
                    url();
                }
            } else if ("Vulnerability_ID".equals(element.localpart)) {
                in_vuln = false;
                endBuffer();
            } else if ("Additional_Resources".equals(element.localpart)) {
                in_adds = false;
                endBuffer();
            } else if ("ref_title".equals(element.localpart)) {
                if (in_vuln || in_adds || in_sol) {
                    ref_desc = getBuffer();
                }
            } else if ("uri".equals(element.localpart)) {
                if (in_vuln || in_adds || in_sol) {
                    endBuffer();
                    ref_url = getBuffer();
                }
            } else if ("FreeText".equals(element.localpart) || "FormattedText".equals(element.localpart)) {
                if (buffer_active) {
                    read_text = false;
                    endBuffer();
                }
            } else if ("cvss_base_score".equals(element.localpart)) {
                endBuffer();
                _advisory.setCvssBaseScore(Double.parseDouble(getBuffer()));
            } else if ("cvss_base_vector".equals(element.localpart)) {
                endBuffer();
                _advisory.setCvssVector(getBuffer());
            } else if ("cvss_temporal_score".equals(element.localpart)) {
                endBuffer();
                _advisory.setCvssTemporalScore(Double.parseDouble(getBuffer()));
            } else if ("cvss_temporal_vector".equals(element.localpart)) {
                endBuffer();
                String temporalVector = getBuffer();
                if (temporalVector != null) _advisory.setCvssVector(temporalVector);
            } else {
                addUnknownTag(element);
            }
        } catch (Exception e) {
            _logger.error("Fatal error with xml parser declared.", e);
            XNIException ex = new XNIException(e.getMessage());
            ex.initCause(e);
            throw ex;
        }
    }

    /**
     * Adds a reference. We should avoid adding a reference
     * if it will be autogenerated through one of the vuln
     * ref_num.
     */
    private void url() {
        boolean ok = true;
        if (ref_url != null && ref_desc != null && ref_url.length() > 0 && ref_desc.length() > 0) {
            try {
                new URL(ref_url);
            } catch (MalformedURLException e) {
                ok = false;
                _logger.info("Found a malformed URL as a reference [" + ref_url + "]");
            }
            if (ok) _advisory.getRefList().put(ref_url, ref_desc);
        }
    }

    /**
     * Process references
     */
    private void reference() {
        String ref_type = ((String) getAttribute("ref_type")).trim();
        String ref_num = ((String) getAttribute("ref_num")).trim();
        if ("vuln_id".equals(ref_type)) {
            if (null == ref_num || ref_num.length() == 0) {
                _logger.info("Found a null or empty ref_num in a reference vuln_id.");
            } else if (Issuer.isAValidVulnRef(ref_num)) {
                _advisory.getAlias().add(ref_num);
            } else {
                _logger.info("Found an unknown type of ref_num in a reference vuln_id [" + ref_num + "]");
            }
        } else if ("vendor_advisory".equals(ref_type)) {
            if (ref_num != null && ref_num.length() > 0) {
                if (Issuer.isAValidVulnRef(ref_num)) {
                    _advisory.getAlias().add(ref_num);
                } else {
                    _logger.info("Found an unknown type of ref_num in a reference vendor_advisory [" + ref_num + "]");
                }
            }
        }
    }

    /** Default constructor. */
    public CertIstXmlParser() {
        super();
    }

    public static CertIstXmlParser newParser() {
        _logger.debug("CertIstXmlParser - new parser.");
        return new CertIstXmlParser();
    }

    public AdvisoryCertIst getParsedAdvisory() {
        return _advisory;
    }

    public String getMainXmlTag() {
        return "EISPP-Advisory";
    }

    protected String[] getIgnoredTags() {
        return null;
    }

    public String[] getKeyTags() {
        return null;
    }

    public void configure(String lang, Vendors v, CertIstImport certIstImport) {
        this._lang = lang;
        this._vendors = v;
        this.imp = certIstImport;
    }
}
