package com.idedeluxe.bc.core.model.attribute;

import java.util.HashMap;
import java.util.Map;
import com.idedeluxe.bc.core.model.struc.*;

/**
 * <pre>
 *  attribute_info {
 *  	u2 attribute_name_index;
 *  	u4 attribute_length;
 *  	u1 info[attribute_length];
 *  }
 * </pre>
 */
public class AttributeInfo extends Attribute {

    public static final Map<String, Structure> ATTRIBUTES_MAP = new HashMap<String, Structure>();

    public static final String DEFAULT = "";

    static {
        for (Attributes a : Attributes.values()) ATTRIBUTES_MAP.put(a.getIdentifier(), new ModelStructure(a.getDescription(), a.getInstanceClass()));
    }

    public static final ArrayStructure INFO = new ArrayElementCountingStructure("bytes", new LeafStructure("byte", 1), ATTRIBUTE_LENGTH, false);

    public static final Structure[] STRUCTURE = new Structure[] { ATTRIBUTE_NAME_INDEX, ATTRIBUTE_LENGTH, INFO };

    public Structure[] getStructure() {
        return STRUCTURE;
    }
}
