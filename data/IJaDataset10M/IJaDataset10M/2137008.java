package de.fraunhofer.ipsi.ipsixq.datamodel;

import de.fraunhofer.ipsi.util.IndentPrinter;
import de.fraunhofer.ipsi.xpathDatatypes.XS_UntypedAtomic;
import de.fraunhofer.ipsi.xquery.datamodel.AttributeNode;
import de.fraunhofer.ipsi.xquery.datamodel.Sequence;
import de.fraunhofer.ipsi.xquery.sequencetypes.BuiltinTypeEnum;
import java.io.StringWriter;
import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

public class DM_AttributeNode extends DM_Node implements AttributeNode {

    protected static final QName ID = new QName(XMLConstants.XML_NS_URI, "id", XMLConstants.XML_NS_PREFIX);

    protected static final String kind = "attribute";

    private final QName name;

    private final String value;

    private final QName type;

    private final Boolean is_id;

    private final Boolean is_idrefs;

    /**
	 * Constructor
	 * @deprecated
	 */
    protected DM_AttributeNode(QName name, String value, QName type) {
        this.name = name;
        this.value = value;
        this.type = type;
        if (type.equals(BuiltinTypeEnum.XS_ID.getName()) || name.equals(ID)) {
            is_id = true;
            is_idrefs = false;
        } else if (type.equals(BuiltinTypeEnum.XS_IDREF.getName()) || type.equals(BuiltinTypeEnum.XS_IDREFS.getName())) {
            is_id = true;
            is_idrefs = false;
        } else {
            is_id = false;
            is_idrefs = false;
        }
    }

    /**
	 * Method base_uri
	 *
	 * @return   a DM_SequenceOptional
	 *
	 */
    public String baseUri() {
        if (parent() != null) {
            return parent().baseUri();
        } else {
            return null;
        }
    }

    /**
	 * Method isId
	 *
	 * @return   a XS_Boolean
	 *
	 */
    public Boolean isId() {
        return is_id;
    }

    /**
	 * Method isId
	 *
	 * @return   a XS_Boolean
	 *
	 */
    public Boolean isIdrefs() {
        return is_idrefs;
    }

    /**
	 * Method nodeKind
	 *
	 * @return   a XS_String
	 *
	 */
    public String nodeKind() {
        return kind;
    }

    /**
	 * Method name
	 *
	 * @return   a DM_SequenceOptional
	 *
	 */
    public QName nodeName() {
        return name;
    }

    /**
	 * Method string_value
	 *
	 * @return   a XS_String
	 *
	 */
    public String stringValue() {
        return value;
    }

    /**
	 * Method typed_value
	 *
	 * @return   a DM_Sequence
	 *
	 */
    public Sequence typedValue() {
        Sequence result = manager.newSequence();
        String s = stringValue();
        if (typeName().equals(XS_UntypedAtomic.TYPENAME)) {
            result.add(manager.newAtomicValue(XS_UntypedAtomic.valueOf(s)));
        } else throw new RuntimeException("Unexpected type: " + typeName());
        return result;
    }

    /**
	 * Method type
	 *
	 * @return   a DM_SequenceOptional
	 *
	 */
    public QName typeName() {
        return type;
    }

    /**
	 * Method deepEquals
	 *
	 * @param    other               a  SequenceImpl
	 *
	 * @return   a boolean
	 *
	 */
    public boolean deepEquals(Sequence other) {
        boolean result = false;
        if (other.size() == 1) other.get(0);
        if (other instanceof AttributeNode) {
            AttributeNode node2 = (AttributeNode) other;
            if (this.nodeName().equals(node2.nodeName()) && this.typedValue().deepEquals(node2.typedValue())) {
                result = true;
            }
        }
        return result;
    }
}
