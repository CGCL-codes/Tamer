package org.openorb.orb.test.iiop.fragmentedmessage;

import org.omg.CORBA.ORB;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Class responsible for parsing the XML file correctly and
 * adding the newly created objects to a provided collection.
 *
 * @author  Michael Macaluso
 */
public class LocalContentHandler extends DefaultHandler {

    public static final java.util.Map ATTRTYPECODE_2_ATTRENUM_MAP = new java.util.HashMap();

    public static final String STRING_ATTRIBUTE = "As";

    public static final String DIMENSION_ATTRIBUTE = "Ai";

    public static final String DOUBLE_ATTRIBUTE = "Ad";

    public static final String BOOLEAN_ATTRIBUTE = "Ab";

    public static final String LONG_ATTRIBUTE = "Al";

    public static final String DATE_ATTRIBUTE = "Aa";

    public static final String DATETIME_ATTRIBUTE = "At";

    public static final String ENUMERATION_ATTRIBUTE = "Ae";

    public static final String USERLIST_ATTRIBUTE = "Aw";

    public static final String SEQUENCE_ATTRIBUTE = "Aq";

    public static final String STRINGBUFFER_ATTRIBUTE = "Au";

    public static final String STYLEDTEXT_ATTRIBUTE = "YT";

    static {
        ATTRTYPECODE_2_ATTRENUM_MAP.put(STRING_ATTRIBUTE, AttributeEnum.StringAttr);
        ATTRTYPECODE_2_ATTRENUM_MAP.put(DIMENSION_ATTRIBUTE, AttributeEnum.Dimension);
        ATTRTYPECODE_2_ATTRENUM_MAP.put(DOUBLE_ATTRIBUTE, AttributeEnum.DoubleAttr);
        ATTRTYPECODE_2_ATTRENUM_MAP.put(BOOLEAN_ATTRIBUTE, AttributeEnum.BooleanAttr);
        ATTRTYPECODE_2_ATTRENUM_MAP.put(LONG_ATTRIBUTE, AttributeEnum.LongAttr);
        ATTRTYPECODE_2_ATTRENUM_MAP.put(DATE_ATTRIBUTE, AttributeEnum.DateAttr);
        ATTRTYPECODE_2_ATTRENUM_MAP.put(DATETIME_ATTRIBUTE, AttributeEnum.DateTime);
        ATTRTYPECODE_2_ATTRENUM_MAP.put(ENUMERATION_ATTRIBUTE, AttributeEnum.Enumerated);
        ATTRTYPECODE_2_ATTRENUM_MAP.put(USERLIST_ATTRIBUTE, AttributeEnum.UserList);
        ATTRTYPECODE_2_ATTRENUM_MAP.put(SEQUENCE_ATTRIBUTE, AttributeEnum.SequenceAttr);
        ATTRTYPECODE_2_ATTRENUM_MAP.put(STRINGBUFFER_ATTRIBUTE, AttributeEnum.StringBuffer);
        ATTRTYPECODE_2_ATTRENUM_MAP.put(STYLEDTEXT_ATTRIBUTE, AttributeEnum.StyledText);
    }

    private final java.util.Collection m_collectionOfAttributeDefintions;

    private final ORB m_orb;

    private AttributeDefinition m_attributeDefinition;

    private boolean m_inAttributeData = false;

    private boolean m_inAttributeDefinition = false;

    private int m_numberOfAttributeDefintions = 0;

    public LocalContentHandler(java.util.Collection aCollectionOfAttributeDefintions, ORB anORB) {
        m_collectionOfAttributeDefintions = aCollectionOfAttributeDefintions;
        m_orb = anORB;
    }

    public int getNumberOfAttributeDefinitions() {
        return m_numberOfAttributeDefintions;
    }

    public void startElement(String namespaceURI, String localName, String qName, org.xml.sax.Attributes atts) throws org.xml.sax.SAXException {
        if (!m_inAttributeData) {
            if (qName.equalsIgnoreCase("AttributeData")) {
                m_inAttributeData = true;
                return;
            }
        }
        if (m_attributeDefinition == null) {
            if (qName.equalsIgnoreCase("AttributeDefinition")) {
                m_inAttributeDefinition = true;
                m_attributeDefinition = new AttributeDefinition();
                int index;
                index = atts.getIndex("ID");
                m_attributeDefinition.id = new ObjectIdImpl(atts.getValue(index));
                index = atts.getIndex("Name");
                m_attributeDefinition.name = atts.getValue(index);
                index = atts.getIndex("TSCounter");
                try {
                    m_attributeDefinition.tsCounter = Integer.parseInt(atts.getValue(index));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                m_attributeDefinition.moreInfo = m_orb.create_any();
                index = atts.getIndex("Value");
                if (-1 != index) {
                    try {
                        m_attributeDefinition.moreInfo.insert_wstring(atts.getValue(index));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
                m_attributeDefinition.status = ItemStatus.UNMODIFIED;
                m_attributeDefinition.uiDefinition = new AttributeConstraint[0];
                m_collectionOfAttributeDefintions.add(m_attributeDefinition);
                m_numberOfAttributeDefintions++;
            }
        } else {
            AttributeType anAttributeType = new AttributeType();
            int index;
            index = atts.getIndex("ID");
            anAttributeType.id = new ObjectIdImpl(atts.getValue(index));
            index = atts.getIndex("Name");
            anAttributeType.name = atts.getValue(index);
            index = atts.getIndex("Code");
            String anAttributeEnumCode = atts.getValue(index);
            anAttributeType.type = (AttributeEnum) ATTRTYPECODE_2_ATTRENUM_MAP.get(anAttributeEnumCode);
            index = atts.getIndex("TSCounter");
            try {
                anAttributeType.tsCounter = Integer.parseInt(atts.getValue(index));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            anAttributeType.constraints = new AttributeConstraint[0];
            m_attributeDefinition.type = anAttributeType;
            m_attributeDefinition = null;
        }
    }

    public void endDocument() throws SAXException {
    }
}
