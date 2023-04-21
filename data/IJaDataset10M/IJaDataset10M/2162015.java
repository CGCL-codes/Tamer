package com.ivis.xprocess.framework.xml;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom.Attribute;
import com.ivis.xprocess.framework.Xelement;
import com.ivis.xprocess.util.JdomUtils;

public class XMLCoder {

    private static final Logger logger = Logger.getLogger(XMLCoder.class.getName());

    private static byte[] key;

    private static byte offset = 49;

    private static String obfirmo = "6923:";

    static {
        key = new byte[obfirmo.length()];
        for (int i = 0; i < obfirmo.length(); i++) {
            key[i] = (byte) (obfirmo.charAt(i) - offset);
        }
    }

    /**
     * @return the converted jdom document
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static org.jdom.Document convert2CDATA(org.jdom.Document jdomDocument) {
        org.jdom.Document myConvertedJDomDocument = new org.jdom.Document();
        org.jdom.Element root_src = jdomDocument.getRootElement();
        org.jdom.Element convertedRootElement = cloneHollowElementWithAttributes(root_src);
        myConvertedJDomDocument.setRootElement(convertedRootElement);
        org.jdom.Document ret = myConvertedJDomDocument;
        org.jdom.Element root = jdomDocument.getRootElement();
        List<org.jdom.Element> els_with_uuid = root.getChildren();
        for (org.jdom.Element el : els_with_uuid) {
            Attribute uuid = el.getAttribute(Xelement.UUID);
            if (uuid == null) {
                logger.log(Level.FINE, "Xelement " + el.getName() + " doesn't have a UUID attribute.");
            }
            convertToCDATAjdom(el, myConvertedJDomDocument);
        }
        return ret;
    }

    @SuppressWarnings("unchecked")
    private static void convertToCDATAjdom(org.jdom.Element el, org.jdom.Document myConvertedJDomDocument) {
        String name = el.getName();
        String str = JdomUtils.elementToXML(el);
        str = toBinary(str);
        org.jdom.CDATA cdata = new org.jdom.CDATA(str);
        org.jdom.Element convertedElement = new org.jdom.Element(name);
        convertedElement.addContent(cdata);
        List<Attribute> org_attrs = el.getAttributes();
        for (Attribute src_attr : org_attrs) {
            String attr_name = src_attr.getQualifiedName();
            String attr_value = src_attr.getValue();
            org.jdom.Attribute out_attr = new org.jdom.Attribute(attr_name, attr_value);
            convertedElement.setAttribute(out_attr);
        }
        myConvertedJDomDocument.getRootElement().addContent(convertedElement);
    }

    static org.jdom.Element cloneHollowElementWithAttributes(org.jdom.Element el) {
        String name = el.getQualifiedName();
        org.jdom.Element ret = new org.jdom.Element(name);
        java.util.List<?> attrs = el.getAttributes();
        for (Object object : attrs) {
            if (object instanceof org.jdom.Attribute) {
                org.jdom.Attribute attr = (org.jdom.Attribute) object;
                org.jdom.Attribute out_attr = new org.jdom.Attribute(attr.getName(), attr.getValue());
                ret.setAttribute(out_attr);
            }
        }
        return ret;
    }

    private static String toBinary(String input) {
        byte[] src = input.getBytes();
        byte[] out = shift(src);
        String encoded = Base64.encodeBytes(out);
        return encoded;
    }

    static String fromBinary(String input) throws IOException {
        byte[] dec = Base64.decode(input);
        byte[] ret = shift(dec);
        return new String(ret);
    }

    private static byte[] shift(byte[] src) {
        byte[] out = new byte[src.length];
        for (int i = 0; i < src.length; i++) {
            byte b = src[i];
            byte o = b;
            o = (byte) (b ^ key[i % key.length]);
            out[i] = o;
        }
        return out;
    }
}
