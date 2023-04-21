package de.fraunhofer.ipsi.ipsixq.datamodel.copy;

import de.fraunhofer.ipsi.ipsixq.datamodel.DM_AttributeNode;
import de.fraunhofer.ipsi.xquery.datamodel.AttributeNode;
import de.fraunhofer.ipsi.xquery.datamodel.Sequence;
import de.fraunhofer.ipsi.xquery.enums.ConstructionModeEnum;
import de.fraunhofer.ipsi.xquery.sequencetypes.BuiltinTypeEnum;
import javax.xml.namespace.QName;

public class DM_CopiedAttributeNode extends DM_AttributeNode {

    private final AttributeNode original;

    private final ConstructionModeEnum constr;

    /**
	 * Method getType
	 *
	 * @param    original            an ElementNode
	 * @param    constr              a  ConstructionModeEnum
	 *
	 * @return   a QName
	 *
	 */
    private static QName getType(AttributeNode original, ConstructionModeEnum constr) {
        if (constr == ConstructionModeEnum.strip) return BuiltinTypeEnum.XS_UntypedAtomic.getName(); else return original.typeName();
    }

    /**
	 * Constructor
	 *
	 * @param    original            an ElementNode
	 *
	 */
    public DM_CopiedAttributeNode(AttributeNode original, ConstructionModeEnum constr) {
        super(original.nodeName(), original.stringValue(), getType(original, constr));
        this.original = original;
        this.constr = constr;
    }

    /**
	 * Method typedValue
	 *
	 * @return   a Sequence
	 *
	 */
    public Sequence typedValue() {
        if (constr == ConstructionModeEnum.strip) return super.typedValue(); else return original.typedValue();
    }
}
