package xades4j.xml.marshalling;

import org.w3c.dom.Document;
import xades4j.properties.data.PropertyDataObject;
import xades4j.properties.data.SigningCertificateData;
import xades4j.xml.bind.xades.XmlCertIDListType;
import xades4j.xml.bind.xades.XmlSignedPropertiesType;

/**
 *
 * @author Luís
 */
class ToXmlSigningCertificateConverter implements SignedPropertyDataToXmlConverter {

    @Override
    public void convertIntoObjectTree(PropertyDataObject propData, XmlSignedPropertiesType xmlProps, Document doc) {
        SigningCertificateData signCertData = (SigningCertificateData) propData;
        XmlCertIDListType xmlSigningCertificateProp = ToXmlUtils.getXmlCertRefList(signCertData);
        xmlProps.getSignedSignatureProperties().setSigningCertificate(xmlSigningCertificateProp);
    }
}
