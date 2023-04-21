package org.infoeng.ictp.documents;

import org.infoeng.ictp.exceptions.ICTPException;
import org.infoeng.ictp.exceptions.ICTPSignatureException;
import org.infoeng.ictp.utils.ICTPNamespaceContext;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.apache.xml.security.keys.KeyInfo;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.transforms.Transforms;
import org.apache.xml.security.utils.Constants;
import org.apache.xml.security.utils.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.helpers.NamespaceSupport;

public class OfferingOrder {

    private static final String ooURI = "http://infoeng.org/offering-order-2005-02-09";

    private Document ooDoc;

    private XPath xpath;

    public OfferingOrder() throws ICTPException {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            XPathFactory xpf = XPathFactory.newInstance();
            xpath = xpf.newXPath();
            ooDoc = db.newDocument();
            Element root = ooDoc.createElement("OfferingOrder");
            ooDoc.appendChild(root);
            Element oAssetElem = ooDoc.createElement("offeredAsset");
            root.appendChild(oAssetElem);
            Element oTypeElem = ooDoc.createElement("offerType");
            root.appendChild(oTypeElem);
        } catch (javax.xml.parsers.ParserConfigurationException e) {
            throw new ICTPException("ParserConfigurationException: ", e);
        }
    }

    public OfferingOrder(String offeringOrderDoc) throws ICTPException {
        String assetStr = null;
        String typeStr = null;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            ooDoc = db.parse(new ByteArrayInputStream(offeringOrderDoc.getBytes()));
            XPathFactory xpf = XPathFactory.newInstance();
            xpath = xpf.newXPath();
            Element assetElem = (Element) xpath.evaluate("/OfferingOrder/offeredAsset", ooDoc, XPathConstants.NODE);
            if (assetElem != null) assetStr = assetElem.getTextContent().trim();
            Element specElem = (Element) xpath.evaluate("/OfferingOrder/orderType", ooDoc, XPathConstants.NODE);
            if (specElem != null) typeStr = specElem.getTextContent();
            if (typeStr == null || assetStr == null) throw new ICTPException(" OfferingOrder(): uninitialized field(s): typeStr: ==" + typeStr + "== assetString: ==" + assetStr + "==");
        } catch (javax.xml.parsers.ParserConfigurationException e) {
            throw new ICTPException("ParserConfigurationException: ", e);
        } catch (org.xml.sax.SAXException e) {
            throw new ICTPException("SAXException: ", e);
        } catch (javax.xml.xpath.XPathExpressionException e) {
            throw new ICTPException("XPathExpressionException: ", e);
        } catch (java.io.IOException e) {
            throw new ICTPException("IOException: ", e);
        } catch (java.lang.Exception e) {
            throw new ICTPException("Exception: ", e);
        }
    }

    public OfferingOrder(InputStream offeringOrderIS) throws ICTPException {
        String assetStr = null;
        String typeStr = null;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            ooDoc = db.parse(offeringOrderIS);
            XPathFactory xpf = XPathFactory.newInstance();
            xpath = xpf.newXPath();
            Element assetElem = (Element) xpath.evaluate("/OfferingOrder/offeredAsset", ooDoc, XPathConstants.NODE);
            if (assetElem != null) assetStr = assetElem.getTextContent().trim();
            Element specElem = (Element) xpath.evaluate("/OfferingOrder/orderType", ooDoc, XPathConstants.NODE);
            if (specElem != null) typeStr = specElem.getTextContent();
            if (typeStr == null || assetStr == null) throw new ICTPException(" OfferingOrder(): uninitialized field(s): specString: ==" + typeStr + "== assetString: ==" + assetStr + "==");
        } catch (javax.xml.parsers.ParserConfigurationException e) {
            throw new ICTPException("ParserConfigurationException: ", e);
        } catch (org.xml.sax.SAXException e) {
            throw new ICTPException("SAXException: ", e);
        } catch (javax.xml.xpath.XPathExpressionException e) {
            throw new ICTPException("XPathExpressionException: ", e);
        } catch (java.io.IOException e) {
            throw new ICTPException("IOException: ", e);
        } catch (java.lang.Exception e) {
            throw new ICTPException("Exception: ", e);
        }
    }

    public void sign(KeyPair kp) throws ICTPSignatureException {
        try {
            if (kp == null) {
                throw new ICTPSignatureException("KeyPair cannot be null.");
            }
            String algorithmString = new String(XMLSignature.ALGO_ID_SIGNATURE_DSA);
            if (kp.getPublic().getAlgorithm().equals("DSA")) {
                algorithmString = new String(XMLSignature.ALGO_ID_SIGNATURE_DSA);
            } else if (kp.getPublic().getAlgorithm().equals("RSA")) {
                algorithmString = new String(XMLSignature.ALGO_ID_SIGNATURE_RSA);
            }
            XMLSignature sig = new XMLSignature(ooDoc, ooURI, algorithmString);
            Element ooElem = ooDoc.getDocumentElement();
            ooElem.appendChild(sig.getElement());
            Transforms transforms = new Transforms(ooDoc);
            transforms.addTransform(Transforms.TRANSFORM_ENVELOPED_SIGNATURE);
            transforms.addTransform(Transforms.TRANSFORM_C14N_WITH_COMMENTS);
            sig.addDocument("", transforms, Constants.ALGO_ID_DIGEST_SHA1);
            sig.addKeyInfo(kp.getPublic());
            sig.sign(kp.getPrivate());
        } catch (org.apache.xml.security.transforms.TransformationException e) {
            throw new ICTPSignatureException("TransformationException: ", e);
        } catch (org.apache.xml.security.exceptions.XMLSecurityException e) {
            throw new ICTPSignatureException("XMLSecurityException: ", e);
        }
    }

    public PublicKey getPublicKey() throws ICTPException {
        ICTPNamespaceContext nc = new ICTPNamespaceContext("http://www.w3.org/2000/09/xmldsig#", "ds");
        xpath.setNamespaceContext(nc);
        Element dsigElem = (Element) xpath.evaluate("/OfferingOrder/ds:Signature", ooDoc, XPathConstants.NODE);
        XMLSignature xmlSig = new XMLSignature(dsigElem, "");
        KeyInfo ki = xmlSig.getKeyInfo();
        PublicKey pubkey = ki.getPublicKey();
        return pubkey;
    }

    public boolean verify(PublicKey pk) throws ICTPSignatureException {
        ICTPNamespaceContext nc = new ICTPNamespaceContext("http://www.w3.org/2000/09/xmldsig#", "ds");
        try {
            org.apache.xml.security.Init.init();
            xpath.setNamespaceContext(nc);
            Element dsigElem = (Element) xpath.evaluate("/OfferingOrder/ds:Signature", ooDoc, XPathConstants.NODE);
            XMLSignature xmlSig = new XMLSignature(dsigElem, "");
            boolean xmlResult = xmlSig.checkSignatureValue(pk);
            return xmlResult;
        } catch (javax.xml.xpath.XPathExpressionException e) {
            throw new ICTPSignatureException("XPathExpressionException: ", e);
        } catch (org.apache.xml.security.keys.keyresolver.KeyResolverException e) {
            throw new ICTPSignatureException("KeyResolverException: ", e);
        } catch (org.apache.xml.security.exceptions.XMLSecurityException e) {
            throw new ICTPSignatureException("KeyResolverException: ", e);
        }
    }

    public boolean verify() throws ICTPSignatureException {
        ICTPNamespaceContext nc = new ICTPNamespaceContext("http://www.w3.org/2000/09/xmldsig#", "ds");
        try {
            org.apache.xml.security.Init.init();
            xpath.setNamespaceContext(nc);
            Element dsigElem = (Element) xpath.evaluate("/OfferingOrder/ds:Signature", ooDoc, XPathConstants.NODE);
            XMLSignature xmlSig = new XMLSignature(dsigElem, "");
            KeyInfo ki = xmlSig.getKeyInfo();
            PublicKey pubkey = ki.getPublicKey();
            boolean xmlResult = xmlSig.checkSignatureValue(pubkey);
            return xmlResult;
        } catch (javax.xml.xpath.XPathExpressionException e) {
            throw new ICTPSignatureException("XPathExpressionException: ", e);
        } catch (org.apache.xml.security.keys.keyresolver.KeyResolverException e) {
            throw new ICTPSignatureException("KeyResolverException: ", e);
        } catch (org.apache.xml.security.exceptions.XMLSecurityException e) {
            throw new ICTPSignatureException("KeyResolverException: ", e);
        }
    }

    public void setOrder(String orderStr) throws ICTPException {
        try {
            Element typeElem = (Element) xpath.evaluate("/OfferingOrder/orderType", ooDoc, XPathConstants.NODE);
            if (typeElem == null) {
                throw new ICTPException("/OfferingOrder/orderType cannot be null");
            }
            typeElem.setTextContent(orderStr);
        } catch (javax.xml.xpath.XPathExpressionException e) {
            throw new ICTPException("XPathExpressionException: ", e);
        }
    }

    public String getOrder() throws ICTPException {
        try {
            Element typeElem = (Element) xpath.evaluate("/OfferingOrder/orderType", ooDoc, XPathConstants.NODE);
            if (typeElem == null) {
                throw new ICTPException("/OfferingOrder/orderType cannot be null");
            }
            String orderStr = new String(typeElem.getTextContent());
            return orderStr;
        } catch (javax.xml.xpath.XPathExpressionException e) {
            throw new ICTPException("XPathExpressionException: ", e);
        }
    }

    public void setAsset(String assetStr) throws ICTPException {
        try {
            Element assetElem = (Element) xpath.evaluate("/OfferingOrder/offeredAsset", ooDoc, XPathConstants.NODE);
            if (assetElem == null) {
                throw new ICTPException("/OfferingOrder/orderType cannot be null");
            }
            assetElem.setTextContent(assetStr);
        } catch (javax.xml.xpath.XPathExpressionException e) {
            throw new ICTPException("XPathExpressionException: ", e);
        }
    }

    public String getAsset() throws ICTPException {
        try {
            Element assetElem = (Element) xpath.evaluate("/OfferingOrder/offeredAsset", ooDoc, XPathConstants.NODE);
            if (assetElem == null) {
                throw new ICTPException("/OfferingOrder/orderType cannot be null");
            }
            String assetStr = new String(assetElem.getTextContent());
            return assetStr;
        } catch (javax.xml.xpath.XPathExpressionException e) {
            throw new ICTPException("XPathExpressionException: ", e);
        }
    }

    public String toString() {
        org.apache.xml.security.Init.init();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLUtils.outputDOM(ooDoc, baos);
        String domOutput = new String(baos.toString());
        return domOutput;
    }

    public void outputString(OutputStream os) {
        org.apache.xml.security.Init.init();
        XMLUtils.outputDOM(ooDoc, os);
    }
}
