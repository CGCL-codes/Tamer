package de.sicari.authentication.xml;

import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import codec.util.CertificateChainVerifier;
import de.fhg.igd.logging.Logger;
import de.fhg.igd.logging.LoggerFactory;
import de.sicari.authentication.SecurityTokenException;
import de.sicari.kernel.Lookup;
import de.sicari.kernel.security.KeyMaster;
import de.sicari.kernel.security.SecurityToken;

/**
 * Represents an XMLSecurityToken of an authentication token.</n>
 * 
 * The <code>XMLSecurityToken</code> consists of the following entries:
 * 
 * <ul>
 * <li>The <code>version</code> shows the actual used version.</li>
 * <li>The <code>tokenID</code> is the ID of this token.</li>
 * <li>The <code>creationTime</code> is the time of creating of the 
 *   <code>XMLSecurityToken</code>.</li>
 * <li>The <code>notBefeforeTime</code> is the beginning time of validity
 *    of the <code>XMLSecurityToken</code>.</li>
 * <li>The <code>validUntil</code> is the expiry time of validity of the 
 *    <code>XMLSecurityToken</code>.</li>
 * <li>The <code>attributes</code> is the set of various optional 
 *    attribute-value pairs.</li>
 * </ul>
 * 
 * <code>XMLSecurityToken</code> contains all required information
 * about the principal. This information has to be signed before sending.
 * 
 * If the <code>XMLSecurityToken</code> is created and signed by the trusted 
 * party the reciever can identify the issuer, verify the signature and token
 * validity and use this authentication token for authorisation decision.
 * 
 * 
 * @author Dimitri Idessis
 * @version $Id: XMLSecurityToken.java 210 2007-07-13 17:24:50Z jpeters $
 */
public class XMLSecurityToken extends Object implements SecurityToken {

    /**
     * The Logger instance from de.fhg.igd.logging.Logger.
     */
    private static Logger log_ = LoggerFactory.getLogger("security/core");

    /**
     * A flag denoting whether or not {@link #destroy()} has been called
     */
    private boolean destroyed_ = false;

    /**
     * The type of this security token class.
     */
    public static String TYPE = "xml";

    /**
     * The <code>name_</code> of the element contains the token.
     */
    protected static final String name_ = "XMLSecurityToken";

    /**
     * The certificates of the validated <code>issuers_</code>. 
     */
    private X509Certificate[] trustedIssuers_;

    /**
     * The represantation of the token as <code>org.w3c.dom.Document</code>.
     */
    protected Document doc_;

    /**
     * The represantation of the token as <code>org.w3c.dom.Element</code>.
     */
    protected Element element_;

    /**
     * The <code>Map</code> of all attributes.
     */
    private Map attrMap_;

    public XMLSecurityToken(SecurityToken source) {
        this(source.tokenID(), source.validFrom(), source.validUntil(), source.attributes());
        element_.setAttribute("creationTime", String.valueOf(source.creationTime()));
    }

    /**
     * Creates a new <code>XMLSecurityToken</code> from given parameter.
     * 
     * @param tokenID The <code>tokenID</code> is the ID of this token.
     * @param validFrom The <code>notBefeforeTime</code> is the beginning
     *   time of validity of the <code>XMLSecurityToken</code>.
     * @param validUntil The <code>validUntil</code> is the expiry time of 
     *   validity of the <code>XMLSecurityToken</code>.
     * @param attributes The <code>attributes</code> is the set of various
     *   optional attribute-value pairs.
     */
    public XMLSecurityToken(String tokenID, Date validFrom, Date validUntil, Map attributes) {
        DocumentBuilderFactory factory;
        DocumentBuilder docBuilder;
        Object[] a;
        Set set;
        int i;
        attrMap_ = attributes;
        factory = DocumentBuilderFactory.newInstance();
        try {
            docBuilder = factory.newDocumentBuilder();
            doc_ = docBuilder.newDocument();
            element_ = doc_.createElement(name_);
            element_.setAttribute("version", SecurityToken.VERSION);
            element_.setAttribute("tokenID", tokenID);
            element_.setAttribute("creationTime", String.valueOf(new Date().getTime()));
            element_.setAttribute("validFrom", String.valueOf(validFrom.getTime()));
            element_.setAttribute("validUntil", String.valueOf(validUntil.getTime()));
            if (attributes != null) {
                set = attributes.keySet();
                a = set.toArray();
                element_.setAttribute("attributesCount", String.valueOf(a.length));
                for (i = 0; i < a.length; i++) {
                    element_.setAttribute((String) a[i], (String) attributes.get(a[i]));
                }
            }
            doc_.appendChild(element_);
        } catch (ParserConfigurationException e) {
            log_.error(e.getMessage());
        }
    }

    /**
     * Creates a new <code>XMLSecurityToken</code> from given 
     * <code>org.w3c.dom.Document</code>.
     * 
     * @param el The given <code>org.w3c.dom.Element</code> which contains
     *   all the information for creating the token.
     */
    public XMLSecurityToken(Element el) {
        DocumentBuilderFactory factory;
        DocumentBuilder docBuilder;
        NamedNodeMap nnm;
        String value;
        String name;
        Node n;
        int a;
        int i;
        factory = DocumentBuilderFactory.newInstance();
        try {
            docBuilder = factory.newDocumentBuilder();
            doc_ = docBuilder.newDocument();
            element_ = doc_.createElement(name_);
            nnm = el.getAttributes();
            for (i = 0; i < nnm.getLength(); i++) {
                n = nnm.item(i);
                name = n.getNodeName();
                value = n.getTextContent();
                element_.setAttribute(name, value);
            }
            element_.removeAttribute("actor");
            element_.removeAttribute("mustUnderstand");
            doc_.appendChild(element_);
            String anzahl = element_.getAttribute("attributesCount");
            nnm = element_.getAttributes();
            a = 0;
            if (anzahl.compareTo("") != 0) {
                a = Integer.valueOf(anzahl).intValue();
            }
            attrMap_ = new HashMap();
            for (i = 1; i <= a; i++) {
                n = nnm.item(i);
                attrMap_.put(n.getNodeName(), n.getTextContent());
            }
        } catch (ParserConfigurationException e) {
            log_.error(e.getMessage());
        }
    }

    /**
     * Returns the string representation of this ASN1SecurityToken.
     * 
     * @return The string representation.
     */
    public String toString() {
        X509Certificate[] certs;
        StringBuffer buf;
        Iterator it;
        String key;
        Map attributes;
        buf = new StringBuffer();
        buf.append("XMLSecurityToken");
        buf.append(" {\n");
        buf.append("  version          : ");
        buf.append(SecurityToken.VERSION);
        buf.append("\n");
        buf.append("  tokenID          : ");
        buf.append(tokenID());
        buf.append("\n");
        buf.append("  creationTime     : ");
        buf.append(creationTime());
        buf.append("\n");
        buf.append("  validFrom        : ");
        buf.append(validFrom());
        buf.append("\n");
        buf.append("  validUntil       : ");
        buf.append(validUntil());
        buf.append("\n");
        buf.append("  isValid          : ");
        buf.append(isValid());
        buf.append("\n");
        buf.append("  isSigned         : ");
        buf.append(isSigned());
        buf.append("\n");
        try {
            if (getIssuerCerts() != null) {
                certs = getIssuerCerts();
                for (int i = 0; i < certs.length; i++) {
                    buf.append("  issuer           : ");
                    buf.append(certs[i].getSubjectDN());
                    buf.append("\n");
                }
            } else {
                buf.append("  issuer           : <no issuer certificates>\n");
            }
        } catch (Exception e) {
            buf.append("  issuer           : <exception: ");
            buf.append(e.getMessage());
            buf.append(">\n");
        }
        try {
            buf.append("  verify           : ");
            buf.append(verify());
            buf.append("\n");
        } catch (Exception e) {
            buf.append("  verify           : <exception: ");
            buf.append(e.getMessage());
            buf.append(">\n");
        }
        try {
            buf.append("  validate         : ");
            buf.append(validate());
            buf.append("\n");
        } catch (Exception e) {
            buf.append("  validate         : <exception: ");
            buf.append(e.getMessage());
            buf.append(">\n");
        }
        attributes = attributes();
        if (attributes != null) {
            buf.append("  attributes\n");
            for (it = attributes.keySet().iterator(); it.hasNext(); ) {
                key = (String) it.next();
                buf.append("  > " + key + " = " + attributes.get(key) + "\n");
            }
        }
        buf.append("}");
        return buf.toString();
    }

    /** 
     * Returns the version of the security token.
     * 
     * @return the version of the security token.
     */
    public String getVersion() {
        return element_.getAttribute("version");
    }

    /**
     * Returns the unique identifier (ID) of the security token.<br>
     * 
     * This ID contains the user and session ID and is formatted
     * as follows:<br>
     * 
     * <code>&lt;user_id&gt;-&lt;session_id&gt;</code>
     * 
     * @return the token ID.
     */
    public String tokenID() {
        return element_.getAttribute("tokenID");
    }

    /**
     * Returns the creation time of the security token.
     * 
     * @return the creation time of the security token.
     */
    public Date creationTime() {
        String str;
        long date;
        str = element_.getAttribute("creationTime");
        date = Long.getLong(str).longValue();
        return new Date(date);
    }

    /**
     * Returns the time of the security token from it begins to be 
     * valid.
     * 
     * @return the not valid before  time of the security token.
     */
    public Date validFrom() {
        String str;
        long date;
        str = element_.getAttribute("validFrom");
        date = Long.getLong(str).longValue();
        return new Date(date);
    }

    /**
     * Returns the expiry time of the security token.
     * 
     * @return the expiry time of the security token.
     */
    public Date validUntil() {
        String str;
        long date;
        str = element_.getAttribute("validUntil");
        date = Long.getLong(str).longValue();
        return new Date(date);
    }

    /**
     * Returns the <code>Map</code> of attributes defined
     * for the security token.
     * 
     * @return the security token attributes. 
     */
    public Map attributes() {
        Map attributes;
        attributes = new HashMap();
        return attributes;
    }

    /**
     * Checks, if the security token is valid using the current
     * system time.
     * 
     * The current system time has to lie between the time given
     * by <code>validFrom()</code> and the time given by 
     * <code>validUntil()</code>, including the lower and upper
     * range.
     * 
     * If a time stamp given by <code>validFrom()</code> or 
     * <code>validUntil()</code> is <code>null</code>, the 
     * corresponding lower resp. upper range is ignored.
     * 
     * @return <code>true</code> if the token is valid.
     */
    public boolean isValid() {
        boolean valid;
        Date current;
        Date check;
        current = new Date();
        valid = true;
        check = validFrom();
        if (check != null && current.before(check)) {
            valid = false;
        }
        check = validUntil();
        if (check != null && current.after(check)) {
            valid = false;
        }
        return valid;
    }

    public X509Certificate[] validate(X509Certificate cert) throws SecurityTokenException {
        X509Certificate[] validated;
        X509Certificate[] certs;
        certs = new X509Certificate[] { cert };
        validated = validate(certs);
        return validated;
    }

    public X509Certificate[] validate(X509Certificate[] certs) throws SecurityTokenException {
        CertificateChainVerifier ccv;
        KeyMaster km;
        List trusted;
        int i;
        km = Lookup.environmentWhatIs(KeyMaster.WHATIS, KeyMaster.class);
        if (km == null) {
            throw new SecurityTokenException("KeyMaster not available");
        }
        ccv = new CertificateChainVerifier(km);
        trusted = new ArrayList();
        for (i = 0; i < certs.length; i++) {
            try {
                ccv.verify(certs[i]);
                trusted.add(certs[i]);
            } catch (GeneralSecurityException e) {
                log_.error(e.getMessage());
            }
        }
        trustedIssuers_ = new X509Certificate[trusted.size()];
        for (i = 0; i < trusted.size(); i++) {
            trustedIssuers_[i] = (X509Certificate) trusted.get(i);
        }
        return trustedIssuers_;
    }

    /**
     * Embeds the security token in the given <code>Document</code>
     * 
     * @param doc The <code>Document</code> where to embed the security
     * token
     * @throws SecurityTokenException if embedment fails.
     */
    public void embed(Document doc) throws SecurityTokenException {
    }

    /**
     * @param token This <code>SAMLSecurityToken</code> has to be
     *  compared.
     * 
     * @return Returns <code>true</code> if all fields of the security
     *   token are equal (including the issuer principal, if present), 
     *   and the signature can successfully be verified, if given.
     */
    public boolean equals(SecurityToken token) {
        if (!(token instanceof XMLSecurityToken)) {
            return false;
        }
        return super.equals(token);
    }

    public X509Certificate[] getIssuerCerts() {
        return null;
    }

    public void sign(PrivateKey privateKey, X509Certificate issuer) throws SecurityTokenException {
    }

    public boolean verify() throws SecurityTokenException {
        return false;
    }

    public X509Certificate[] validate() throws SecurityException {
        return null;
    }

    public byte[] encode() {
        return null;
    }

    /**
     * Returns <code>true</code> if the SecurityToken is already signed 
     * otherwise <code>false</code> 
     */
    public boolean isSigned() {
        return false;
    }

    /**
     * Destroy this <code>Object</code>.
     *
     * <p> Sensitive information associated with this <code>Object</code>
     * is destroyed or cleared. 
     */
    public void destroy() {
        if (!destroyed_) {
            destroyed_ = true;
        }
    }

    /**
     * Determine if this <code>Object</code> has been destroyed.
     *
     * <p>
     *
     * @return true if this <code>Object</code> has been destroyed,
     *      false otherwise.
     */
    public boolean isDestroyed() {
        return destroyed_;
    }

    /**
     * Returns the Type of the SecurityToken 
     */
    public String getType() {
        return TYPE;
    }
}
