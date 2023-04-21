package frost.crypt;

import java.util.*;
import java.util.logging.*;
import org.w3c.dom.*;
import org.xml.sax.*;
import frost.*;
import frost.identities.*;
import frost.util.*;

/**
 * @author zlatinb
 *
 * This file represents MetaData that's of a file in Freenet.
 * It has the following format:
 * <FrostMetaData>
 *  <MyIdentity>
 *   <name> unique name of person</name>
 *   <key> public key of person </key>
 *  </MyIdentity>
 *  <sig> signature of file </sig>
 * </FrostMetaData>
 */
public class SignMetaData extends MetaData {

    private static final Logger logger = Logger.getLogger(SignMetaData.class.getName());

    String sig;

    public SignMetaData() {
        person = null;
        sig = null;
    }

    /**
     * Represents a metadata of something about to be sent.
     * Computes signature of plaintext.
     */
    public SignMetaData(final byte[] plaintext, final LocalIdentity myId) {
        this.person = myId;
        sig = Core.getCrypto().detachedSign(plaintext, myId.getPrivateKey());
    }

    /**
     * Metadata of something that was received.
     */
    public SignMetaData(final byte[] metadata) throws Throwable {
        final Document d = XMLTools.parseXmlContent(metadata, false);
        final Element el = d.getDocumentElement();
        if (el.getTagName().equals("FrostMetaData") == false) {
            throw new Exception("This is not FrostMetaData XML file.");
        }
        try {
            loadXMLElement(el);
        } catch (final SAXException e) {
            logger.log(Level.SEVERE, "Exception thrown in constructor", e);
            throw e;
        }
    }

    /**
     * Represents something that was received and needs to be verified.
     * Parses XML and sets person and sig.
     * @param plaintext the plaintext to be verified
     * @param el the xml element to populate from
     */
    public SignMetaData(final Element el) throws SAXException {
        try {
            loadXMLElement(el);
        } catch (final SAXException e) {
            logger.log(Level.SEVERE, "Exception thrown in constructor", e);
            throw e;
        }
    }

    @Override
    public Element getXMLElement(final Document container) {
        final Element el = super.getXMLElement(container);
        final Element _sig = container.createElement("sig");
        final CDATASection cdata = container.createCDATASection(sig);
        _sig.appendChild(cdata);
        el.appendChild(_sig);
        return el;
    }

    public void loadXMLElement(final Element e) throws SAXException {
        List<Element> tags = XMLTools.getChildElementsByTagName(e, "Identity");
        if (tags.size() == 0) {
            tags = XMLTools.getChildElementsByTagName(e, "MyIdentity");
        }
        final Element _person = tags.iterator().next();
        person = Identity.createIdentityFromXmlElement(_person);
        sig = XMLTools.getChildElementsCDATAValue(e, "sig");
        assert person != null && sig != null;
    }

    /**
     * @return
     */
    public String getSig() {
        return sig;
    }

    @Override
    public int getType() {
        return MetaData.SIGN;
    }
}
