package org.furthurnet.xmlparser;

import java.util.Vector;

public class ValidationList {

    private Vector md5List;

    private ValidationList() {
    }

    public ValidationList(XmlObject obj) throws XmlException {
        if (!obj.getName().equals(XmlTags.BLOCKED_LIST)) throw new XmlException("Could not read element " + XmlTags.BLOCKED_LIST); else md5List = generateMd5List(obj);
    }

    private Vector generateMd5List(XmlObject obj) throws XmlException {
        Vector list = new Vector();
        for (int i = 0; i < obj.numAttributes(); i++) {
            XmlObject next = obj.getAttribute(i);
            if (next.getName().equals(XmlTags.MD5)) list.add(next.getValue());
        }
        return list;
    }

    public boolean findMd5(String md5) {
        for (int i = 0; i < md5List.size(); i++) if (((String) md5List.elementAt(i)).equals(md5)) return true;
        return false;
    }
}
