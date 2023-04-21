package mwt.xml.xdbforms.schemalayer.types;

import javax.xml.XMLConstants;
import mwt.xml.xdbforms.dblayer.metadata.impl.ColumnMetaData;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;

/**
 * Costruire una facelet per il tipo TinyInt
 * @author Gianfranco Murador, Cristian Castiglia, Matteo Ferri
 */
public class XmlTinyInt extends XmlInteger {

    public XmlTinyInt(Document doc) {
        super(doc);
    }

    XmlTinyInt() {
    }

    public void buildFragment(ColumnMetaData cmd) {
        Element xtype, restriction, maxInclusive, minInclusive;
        typeName = cmd.getTypeName();
        fragment = doc.createDocumentFragment();
        xtype = doc.createElementNS(XMLConstants.W3C_XML_SCHEMA_NS_URI, "xs:simpleType");
        xtype.setAttribute("name", typeName);
        cmd.setXschemaTypeName(typeName);
        restriction = doc.createElementNS(XMLConstants.W3C_XML_SCHEMA_NS_URI, "xs:restriction");
        restriction.setAttribute("base", "xs:integer");
        maxInclusive = doc.createElementNS(XMLConstants.W3C_XML_SCHEMA_NS_URI, "xs:maxInclusive");
        maxInclusive.setAttribute("value", Byte.toString(Byte.MAX_VALUE));
        minInclusive = doc.createElementNS(XMLConstants.W3C_XML_SCHEMA_NS_URI, "xs:minInclusive");
        minInclusive.setAttribute("value", Byte.toString(Byte.MIN_VALUE));
        restriction.appendChild(maxInclusive);
        restriction.appendChild(minInclusive);
        xtype.appendChild(restriction);
        xtype.appendChild(restriction);
        fragment.appendChild(xtype);
    }

    @Override
    public DocumentFragment getSchemaType(ColumnMetaData cmd) {
        buildFragment(cmd);
        return fragment;
    }

    @Override
    public Object getXSchemaToJavaType(String baseType, String value) {
        Short s = Short.parseShort(value);
        return s;
    }
}
