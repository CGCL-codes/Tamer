package org.xmldap.xmldsig;

import java.io.IOException;
import java.security.PrivateKey;
import java.util.List;
import java.util.Vector;
import nu.xom.Attribute;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Nodes;
import nu.xom.ParentNode;
import org.xmldap.crypto.CryptoUtils;
import org.xmldap.exceptions.CryptoException;
import org.xmldap.exceptions.SerializationException;
import org.xmldap.exceptions.SigningException;
import org.xmldap.ws.WSConstants;
import org.xmldap.xml.Canonicalizable;
import org.xmldap.xml.XmlUtils;

public class BaseEnvelopedSignature {

    String mAlgorithm;

    protected PrivateKey privateKey;

    protected KeyInfo keyInfo;

    protected String Id = null;

    public BaseEnvelopedSignature(KeyInfo keyInfo, PrivateKey privateKey, String signingAlgorithm) {
        this.keyInfo = keyInfo;
        this.privateKey = privateKey;
        this.mAlgorithm = signingAlgorithm;
    }

    public BaseEnvelopedSignature(KeyInfo keyInfo, PrivateKey privateKey, String Id, String signingAlgorithm) {
        this.keyInfo = keyInfo;
        this.privateKey = privateKey;
        this.Id = Id;
        this.mAlgorithm = signingAlgorithm;
    }

    protected BaseEnvelopedSignature() {
    }

    /**
   * @param xml
   * @param nodesToReference
   * @throws SigningException
   * @return a signed copy of this element
   */
    public Element sign(Element xml) throws SigningException {
        Element signThisOne = (Element) xml.copy();
        String prefixes = null;
        {
            StringBuilder sb = null;
            for (int i = 0; i < signThisOne.getNamespaceDeclarationCount(); i++) {
                String prefix = signThisOne.getNamespacePrefix(i);
                if ("".equals(prefix)) {
                    prefix = "#default";
                }
                if (sb == null) {
                    sb = new StringBuilder();
                    sb.append(prefix);
                } else {
                    sb.append(" ");
                    sb.append(prefix);
                }
            }
            if (sb != null) {
                prefixes = sb.toString();
            }
        }
        String idVal = signThisOne.getAttributeValue("Id", WSConstants.WSSE_OASIS_10_WSU_NAMESPACE);
        if (idVal == null) {
            Attribute assertionID = signThisOne.getAttribute("AssertionID");
            if (assertionID != null) {
                idVal = assertionID.getValue();
            }
        }
        if (idVal == null) {
            throw new IllegalArgumentException("BaseEnvelopedSignature: Element to sign does not have an id-ttribute");
        }
        Reference reference = new Reference(signThisOne, idVal, prefixes, "SHA1");
        SignedInfo signedInfo = new SignedInfo(reference, mAlgorithm);
        Signature signature = getSignatureValue(signedInfo);
        try {
            signThisOne.appendChild(signature.serialize());
        } catch (SerializationException e) {
            throw new SigningException("Could not create enveloped signature due to serialization error", e);
        }
        return signThisOne;
    }

    /**
   * @param xml
   * @param nodesToReference
   * @return Signture Element
   * @throws SigningException
   */
    public Signature signNodes(Element xml, List references) throws SigningException {
        SignedInfo signedInfo = new SignedInfo(references, mAlgorithm);
        Signature signature = getSignatureValue(signedInfo);
        try {
            xml.appendChild(signature.serialize());
        } catch (SerializationException e) {
            throw new SigningException("Could not create enveloped signature due to serialization error", e);
        }
        return signature;
    }

    /**
   * @param nodesToReference
   * @return
   * @throws SigningException
   */
    private Vector getReferences(Nodes nodesToReference) throws SigningException {
        Vector references = new Vector();
        for (int i = 0; i < nodesToReference.size(); i++) {
            try {
                Element referenceThis = (Element) nodesToReference.get(i);
                boolean isRoot = false;
                Document thisDoc = referenceThis.getDocument();
                if (thisDoc == null) {
                    ParentNode parent = referenceThis.getParent();
                    if (parent == null) isRoot = true;
                } else {
                    Element root = thisDoc.getRootElement();
                    if (root.equals(referenceThis)) isRoot = true;
                }
                String idVal = "";
                if (!isRoot) {
                    Attribute id = referenceThis.getAttribute("id");
                    if (id == null) throw new SigningException("XPath returned Element with no wsu:Id attribute. Id is required");
                    idVal = id.getValue();
                } else {
                    Attribute assertionID = referenceThis.getAttribute("AssertionID");
                    if (assertionID != null) {
                        idVal = assertionID.getValue();
                    }
                }
                Reference referenceElm = new Reference(referenceThis, idVal, null, "SHA");
                references.add(referenceElm);
            } catch (ClassCastException e) {
                throw new SigningException("XPath returned an item which was not an element. Signing only allowed on elements.", e);
            }
        }
        return references;
    }

    /**
   * @param xml
   * @param nodesToReference
   * @throws SigningException
   */
    public void signNodes(Element xml, Nodes nodesToReference) throws SigningException {
        Vector references = getReferences(nodesToReference);
        signNodes(xml, references);
    }

    /**
   * @param signedInfo
   * @return
   */
    protected Signature getSignatureValue(SignedInfo signedInfo) {
        SignatureValue signatureValue = new SignatureValue(signedInfo, privateKey);
        Signature signature = new Signature(signedInfo, signatureValue, keyInfo);
        return signature;
    }

    protected static byte[] getAssertionCanonicalBytes(Element r00t) throws IOException {
        Element root = (Element) r00t.copy();
        Element signature = root.getFirstChildElement("Signature", WSConstants.DSIG_NAMESPACE);
        if (signature != null) {
            root.removeChild(signature);
        }
        return XmlUtils.canonicalize(root, Canonicalizable.EXCLUSIVE_CANONICAL_XML);
    }

    protected static String digestElement(Element root) throws CryptoException {
        return digestElement(root, "SHA");
    }

    /**
   * @param root
   * @return
   * @throws CryptoException
   */
    protected static String digestElement(Element root, String messageDigestAlgorithm) throws CryptoException {
        String b64EncodedDigest = null;
        byte[] assertionCanonicalBytes;
        try {
            assertionCanonicalBytes = getAssertionCanonicalBytes(root);
        } catch (IOException e) {
            throw new CryptoException(e);
        }
        try {
            b64EncodedDigest = CryptoUtils.digest(assertionCanonicalBytes, messageDigestAlgorithm);
        } catch (CryptoException e) {
            throw new CryptoException(e);
        }
        return b64EncodedDigest;
    }
}
