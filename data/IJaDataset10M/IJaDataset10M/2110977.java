package mwt.xml.xdbforms.schemalayer.types;

import java.sql.Timestamp;
import javax.xml.XMLConstants;
import mwt.xml.xdbforms.dblayer.metadata.impl.ColumnMetaData;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;

public class XmlDatetime extends XmlDate {

    public XmlDatetime() {
    }

    public XmlDatetime(Document doc) {
        super(doc);
    }

    private void buildFragment(ColumnMetaData cmd) {
        Element xtype, restriction, pattern;
        fragment = doc.createDocumentFragment();
        typeName = cmd.getTypeName();
        xtype = doc.createElementNS(XMLConstants.W3C_XML_SCHEMA_NS_URI, "xs:simpleType");
        xtype.setAttribute("name", typeName);
        cmd.setXschemaTypeName(typeName);
        restriction = doc.createElementNS(XMLConstants.W3C_XML_SCHEMA_NS_URI, "xs:restriction");
        restriction.setAttribute("base", "xs:dateTime");
        pattern = doc.createElementNS(XMLConstants.W3C_XML_SCHEMA_NS_URI, "xs:pattern");
        pattern.setAttribute("value", "\\p{Nd}{4}-\\p{Nd}{2}-\\p{Nd}{2}T\\p{Nd}{2}:\\p{Nd}{2}:\\p{Nd}{2}");
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
        value = value.replace("T", " ");
        Timestamp timestamp = Timestamp.valueOf(value + ".000000000");
        return timestamp;
    }
}
