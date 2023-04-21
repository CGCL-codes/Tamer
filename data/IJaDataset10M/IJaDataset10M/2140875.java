package org.openliberty.arisid.protocol.ldap;

import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.naming.ldap.Control;
import org.openliberty.arisid.ArisIdService;
import org.openliberty.arisid.ArisIdServiceFactory;
import org.openliberty.arisid.AttrSvcInitializedException;
import org.openliberty.arisid.CarmlDoc;
import org.openliberty.arisid.IGFException;
import org.openliberty.arisid.IInteraction;
import org.openliberty.arisid.log.ILogger;
import org.openliberty.arisid.log.LogHandler;
import org.openliberty.arisid.policy.IPolicy;
import org.openliberty.arisid.policy.PolicyHandler;
import org.w3c.dom.Element;
import com.sun.jndi.ldap.Ber;
import com.sun.jndi.ldap.BerDecoder;
import com.sun.jndi.ldap.BerEncoder;

public class PrivacyControl implements Control, IPrivacyControl {

    /**
	 * 
	 */
    private static final long serialVersionUID = -2668010326604049964L;

    private static final ILogger logger = LogHandler.getLogger(PrivacyControl.class);

    private static PolicyHandler phandler = PolicyHandler.getInstance();

    private boolean critical = false;

    private String _ixnName = "";

    private String _appName = "";

    private String _appUri = "";

    private ArisIdService _asvc = null;

    private CarmlDoc _doc = null;

    private HashMap<String, IPolicy> _polMap = new HashMap<String, IPolicy>();

    public PrivacyControl(IInteraction ixn) throws IGFException {
        super();
        this.critical = false;
        _processCarmlDoc(ixn);
    }

    public PrivacyControl(IInteraction ixn, boolean critical) throws IGFException {
        super();
        this.critical = critical;
        _processCarmlDoc(ixn);
    }

    private void _processCarmlDoc(IInteraction ixn) throws IGFException {
        this._appName = "";
        this._ixnName = "";
        this._appUri = "";
        if (ixn == null) return;
        ArisIdService asvc = ixn.getAttributeService();
        if (asvc == null) return;
        CarmlDoc doc = asvc.getCarmlDoc();
        this._appName = doc.getApplicationNameId();
        if (this._appName == null) this._appName = "";
        this._appUri = doc.getCarmlURI().toString();
        if (this._appUri == null) this._appUri = "";
        this._ixnName = ixn.getNameId();
        if (this._ixnName == null) this._ixnName = "";
    }

    /**
	 * Decodes and constructs a Java object representing the encodedValue
	 * 
	 * @param encodedValue
	 *            ASN.1 encoded value as per Privacy Control Specifiction:
	 *            http://www.openliberty.org/wiki/index.php/Profile_LDAP#
	 *            Extended_PolicySequence_Variation
	 * @throws DecodeException
	 */
    public PrivacyControl(byte[] encodedValue) throws DecodeException {
        _parseAsn1Value(encodedValue);
    }

    private void _parseAsn1Value(byte[] encodedValue) throws DecodeException {
        BerDecoder der = new BerDecoder(encodedValue, 0, encodedValue.length);
        try {
            int[] len = new int[1];
            der.parseSeq(len);
            this._appName = der.parseString(true);
            this._appUri = der.parseString(true);
            this._ixnName = der.parseString(true);
            der.parseSeq(len);
            int pcnt = der.parseInt();
            if (pcnt > 1) {
                for (int i = 0; i < pcnt; i++) {
                    der.parseSeq(null);
                    String pname = der.parseString(true);
                    String pStr = der.parseString(true);
                    Element node = phandler.parseStringToElement(pStr);
                    IPolicy pol = null;
                    try {
                        pol = phandler.parseDomPolicy(node);
                    } catch (Exception e) {
                        logger.error("Error parsing policy: " + e.getMessage(), e);
                    }
                    this._polMap.put(pname, pol);
                }
            }
        } catch (Exception e1) {
            throw new DecodeException("Unable to decode privacy control: " + e1.getMessage(), e1);
        }
    }

    /**
	 * Encode ASN.1 value. Encoding is as per spec:
	 * http://www.openliberty.org/wiki
	 * /index.php/Profile_LDAP#Extended_PolicySequence_Variation
	 */
    public byte[] getEncodedValue() {
        BerEncoder ber = new BerEncoder();
        try {
            ber.beginSeq(Ber.ASN_SEQUENCE | Ber.ASN_CONSTRUCTOR);
            {
                ber.encodeString(this._appName, true);
                ber.encodeString(this._appUri, true);
                ber.encodeString(this._ixnName, true);
                ber.beginSeq(Ber.ASN_SEQUENCE | Ber.ASN_CONSTRUCTOR);
                ber.encodeInt(this._polMap.size());
                Iterator<String> piter = this._polMap.keySet().iterator();
                while (piter.hasNext()) {
                    ber.beginSeq(Ber.ASN_SEQUENCE | Ber.ASN_CONSTRUCTOR);
                    String nameId = piter.next();
                    ber.encodeString(nameId, true);
                    IPolicy pol = this.getTransactionConstraints(nameId);
                    String strPol = phandler.policyToString(pol);
                    ber.encodeString(strPol, true);
                    ber.endSeq();
                }
                ber.endSeq();
            }
            ber.endSeq();
        } catch (Exception e) {
            logger.error("Unknown exception encoding PrivacyControl: " + e.getMessage(), e);
        }
        byte[] in = ber.getTrimmedBuf();
        ber = new BerEncoder(in.length);
        try {
            ber.beginSeq(Ber.ASN_SEQUENCE | Ber.ASN_CONSTRUCTOR);
            {
                ber.encodeString(this._appName, true);
                ber.encodeString(this._appUri, true);
                ber.encodeString(this._ixnName, true);
                ber.beginSeq(Ber.ASN_SEQUENCE | Ber.ASN_CONSTRUCTOR);
                ber.encodeInt(this._polMap.size());
                Iterator<String> piter = this._polMap.keySet().iterator();
                while (piter.hasNext()) {
                    ber.beginSeq(Ber.ASN_SEQUENCE | Ber.ASN_CONSTRUCTOR);
                    String nameId = piter.next();
                    ber.encodeString(nameId, true);
                    IPolicy pol = this.getTransactionConstraints(nameId);
                    String strPol = phandler.policyToString(pol);
                    ber.encodeString(strPol, true);
                    ber.endSeq();
                }
                ber.endSeq();
            }
            ber.endSeq();
        } catch (Exception e) {
            logger.error("Unknown exception encoding PrivacyControl: " + e.getMessage(), e);
        }
        return ber.getTrimmedBuf();
    }

    public String getID() {
        return OID_IGF_CONTROL;
    }

    public boolean isCritical() {
        return this.critical;
    }

    public void setDynamicConstraints(String nameId, IPolicy txnConstraints) {
        this._polMap.put(nameId, txnConstraints);
    }

    public void setDynamicConstraints(Map<String, IPolicy> dynamicConstraints) {
        if (dynamicConstraints != null) this._polMap.putAll(dynamicConstraints);
    }

    public Map<String, IPolicy> getConstraintMap() {
        return this._polMap;
    }

    public IPolicy getDynamicConstraints(String nameId) {
        return this._polMap.get(nameId);
    }

    /**
	 * A convenience method to obtain the CarmlDoc object referenced by this
	 * control. For performance reasons, the CarmlDoc object is not instantiated
	 * unless {@link #loadCarmlDoc(URI)} is called first.
	 * 
	 * @return A CarmlDoc object containing the Controls referenced CarmlDoc or
	 *         null if the document hasn't been loaded.
	 */
    public CarmlDoc getCarmlDoc() {
        return this._doc;
    }

    /**
	 * This is a convenience method intended for servers/proxies that need to
	 * instantiate a CarmlDoc object. The localUri is the URI of a local copy of
	 * the controls referenced Carml Document.
	 * 
	 * @param localUri
	 *            A URI to a copy of the CARML document matching the
	 *            {@link #getAppName()} of this control. If localUri is null,
	 *            the method will use the stored URI to load the document.
	 * @throws URISyntaxException
	 * @throws IllegalAccessException
	 * @throws IGFException
	 * @throws InstantiationException
	 * @throws AttrSvcInitializedException
	 * @throws FileNotFoundException
	 */
    public void loadCarmlDoc(URI localUri) throws URISyntaxException, FileNotFoundException, AttrSvcInitializedException, InstantiationException, IGFException, IllegalAccessException {
        URI loadUri = localUri;
        if (loadUri == null) loadUri = new URI(this._appUri);
        this._asvc = ArisIdServiceFactory.parseCarmlOnly(loadUri);
        if (this._asvc != null) this._doc = this._asvc.getCarmlDoc();
    }

    public IPolicy getTransactionConstraints(String nameId) {
        return this._polMap.get(nameId);
    }

    public String getInteractionName() {
        return this._ixnName;
    }

    public String getAppName() {
        return this._appName;
    }

    /**
	 * @deprecated Use {@link #getCarmlURI()} instead
	 */
    public URI getAppURI() {
        return getCarmlURI();
    }

    public URI getCarmlURI() {
        try {
            return new URI(this._appUri);
        } catch (URISyntaxException e) {
            logger.warn("Invalid CARML URI syntax exception occurred for value: " + this._appUri);
            return null;
        }
    }
}
