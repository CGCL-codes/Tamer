package org.fpse.parser;

import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.XNIException;
import org.cyberneko.html.filters.DefaultFilter;
import org.jdom.Verifier;
import com.sun.mail.iap.Argument;

public class DocumentFilter extends DefaultFilter {

    private boolean m_first = true;

    public void processingInstruction(String arg0, XMLString arg1, Augmentations arg2) throws XNIException {
    }

    public void doctypeDecl(String root, String publicId, String systemId, Augmentations augs) throws XNIException {
    }

    public void comment(XMLString value, Augmentations augs) throws XNIException {
        if (value != null) {
            String string = new String(value.ch);
            if (string.indexOf("--") >= 0) {
                string = string.replaceAll("(-){2,}", "++").trim();
                value.setValues(string.toCharArray(), 0, string.length());
            }
        }
        super.comment(value, augs);
    }

    public void startElement(QName element, XMLAttributes attributes, Augmentations augs) throws XNIException {
        element.uri = null;
        element.prefix = null;
        if (isValidName(element.rawname)) {
            if (m_first) {
                attributes.removeAllAttributes();
                m_first = false;
            }
            for (int i = 0; i < attributes.getLength(); ) {
                QName a = new QName();
                a.localpart = attributes.getLocalName(i);
                a.rawname = a.localpart;
                attributes.setName(i, a);
                String name = attributes.getLocalName(i);
                if (Verifier.checkAttributeName(name) != null) {
                    String modifiedName = cleanupName(name);
                    if (modifiedName == null || modifiedName.length() == 0) {
                        attributes.removeAttributeAt(i);
                        continue;
                    } else {
                        QName x = new QName();
                        attributes.getName(i, x);
                        x.rawname = x.localpart = modifiedName;
                        attributes.setName(i, x);
                    }
                }
                i++;
            }
            super.startElement(element, attributes, augs);
        }
    }

    private String cleanupName(String name) {
        if (name == null || name.length() == 0) {
            return null;
        } else {
            StringBuffer modified = new StringBuffer(name.length());
            for (int i = 0; i < name.length(); i++) {
                char c = name.charAt(i);
                if (modified.length() == 0 ? Verifier.isXMLNameStartCharacter(c) : Verifier.isXMLNameCharacter(c)) modified.append(c);
            }
            return modified.toString();
        }
    }

    private boolean isValidName(String name) {
        if (name == null || name.length() == 0) {
            return false;
        } else {
            boolean valid;
            for (int i = 0; i < name.length(); i++) {
                char c = name.charAt(i);
                valid = i == 0 ? Verifier.isXMLNameStartCharacter(c) : Verifier.isXMLNameCharacter(c);
                if (!valid) return false;
            }
        }
        return true;
    }
}
