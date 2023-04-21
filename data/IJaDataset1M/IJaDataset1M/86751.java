package util;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import java.security.AccessControlException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.interfaces.DSAPublicKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Collections;
import java.util.List;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.KeyValue;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;

/**
 * This helper class, part of the SAML-based Single Sign-On Reference Tool,
 * serves to digitally sign XML files, given the contents of the XML file, and a
 * pair of public and private keys. The file is signed as per the specifications
 * defined by SAML 2.0.
 * 
 */
public class XmlDigitalSigner {

    private static final String JSR_105_PROVIDER = "org.jcp.xml.dsig.internal.dom.XMLDSigRI";

    private static final String SAML_PROTOCOL_NS_URI_V20 = "urn:oasis:names:tc:SAML:2.0:protocol";

    private static org.w3c.dom.Node getXmlSignatureInsertLocation(org.w3c.dom.Element elem) {
        org.w3c.dom.Node insertLocation = null;
        org.w3c.dom.NodeList nodeList = elem.getElementsByTagNameNS(SAML_PROTOCOL_NS_URI_V20, "Extensions");
        if (nodeList.getLength() != 0) {
            insertLocation = nodeList.item(nodeList.getLength() - 1);
        } else {
            nodeList = elem.getElementsByTagNameNS(SAML_PROTOCOL_NS_URI_V20, "Status");
            insertLocation = nodeList.item(nodeList.getLength() - 1);
        }
        return insertLocation;
    }

    private static Element signSamlElement(Element element, PrivateKey privKey, PublicKey pubKey) throws SamlException {
        try {
            String providerName = System.getProperty("jsr105Provider", JSR_105_PROVIDER);
            XMLSignatureFactory sigFactory = XMLSignatureFactory.getInstance("DOM", (Provider) Class.forName(providerName).newInstance());
            List envelopedTransform = Collections.singletonList(sigFactory.newTransform(Transform.ENVELOPED, (TransformParameterSpec) null));
            Reference ref = sigFactory.newReference("", sigFactory.newDigestMethod(DigestMethod.SHA1, null), envelopedTransform, null, null);
            SignatureMethod signatureMethod;
            if (pubKey instanceof DSAPublicKey) {
                signatureMethod = sigFactory.newSignatureMethod(SignatureMethod.DSA_SHA1, null);
            } else if (pubKey instanceof RSAPublicKey) {
                signatureMethod = sigFactory.newSignatureMethod(SignatureMethod.RSA_SHA1, null);
            } else {
                throw new SamlException("Error signing SAML element: Unsupported type of key");
            }
            CanonicalizationMethod canonicalizationMethod = sigFactory.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE_WITH_COMMENTS, (C14NMethodParameterSpec) null);
            SignedInfo signedInfo = sigFactory.newSignedInfo(canonicalizationMethod, signatureMethod, Collections.singletonList(ref));
            KeyInfoFactory keyInfoFactory = sigFactory.getKeyInfoFactory();
            KeyValue keyValuePair = keyInfoFactory.newKeyValue(pubKey);
            KeyInfo keyInfo = keyInfoFactory.newKeyInfo(Collections.singletonList(keyValuePair));
            org.w3c.dom.Element w3cElement = Util.toDom(element);
            DOMSignContext dsc = new DOMSignContext(privKey, w3cElement);
            org.w3c.dom.Node xmlSigInsertionPoint = getXmlSignatureInsertLocation(w3cElement);
            dsc.setNextSibling(xmlSigInsertionPoint);
            XMLSignature signature = sigFactory.newXMLSignature(signedInfo, keyInfo);
            signature.sign(dsc);
            return Util.toJdom(w3cElement);
        } catch (ClassNotFoundException e) {
            throw new SamlException("Error signing SAML element: " + e.getMessage());
        } catch (InvalidAlgorithmParameterException e) {
            throw new SamlException("Error signing SAML element: " + e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            throw new SamlException("Error signing SAML element: " + e.getMessage());
        } catch (AccessControlException e) {
            throw new SamlException("Error signing SAML element: " + e.getMessage());
        } catch (XMLSignatureException e) {
            throw new SamlException("Error signing SAML element: " + e.getMessage());
        } catch (KeyException e) {
            throw new SamlException("Error signing SAML element: " + e.getMessage());
        } catch (MarshalException e) {
            throw new SamlException("Error signing SAML element: " + e.getMessage());
        } catch (InstantiationException e) {
            throw new SamlException("Error signing SAML element: " + e.getMessage());
        } catch (IllegalAccessException e) {
            throw new SamlException("Error signing SAML element: " + e.getMessage());
        }
    }

    /**
   * Signs the specified xmlString with the pair of provided keys, as per the
   * SAML 2.0 specifications. Returns String format of signed XML if
   * successfully signed, returns null otherwise.
   * 
   * @param samlResponse SAML Response XML file to be signed
   * @param publicKey public key to read the signed XML
   * @param privateKey private key to sign the XML
   * @return String format of signed XML if signed correctly, null otherwise
   */
    public static String signXML(String samlResponse, PublicKey publicKey, PrivateKey privateKey) throws SamlException {
        Document doc = Util.createJdomDoc(samlResponse);
        if (doc != null) {
            Element signedElement = signSamlElement(doc.getRootElement(), privateKey, publicKey);
            doc.setRootElement((Element) signedElement.detach());
            XMLOutputter xmlOutputter = new XMLOutputter();
            return (xmlOutputter.outputString(doc));
        } else {
            throw new SamlException("Error signing SAML Response: Null document");
        }
    }
}
