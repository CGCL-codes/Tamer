package com.volantis.mcs.build.marlin;

import com.volantis.mcs.build.parser.AttributeGroupDefinition;
import com.volantis.mcs.build.parser.AttributesStructure;
import com.volantis.mcs.build.parser.Scope;

/**
 * This class adds extra information to the
 * <code>AttributeGroupDefinition</code> which is needed to automatically
 * generate code.
 */
public class AttributeGroupInfo extends AttributeGroupDefinition {

    /**
     * Create a new <code>AttributeGroupInfo</code>.
     *
     * @param scope The scope within which this object belongs.
     * @param name  The name of the attribute group.
     */
    public AttributeGroupInfo(Scope scope, String name) {
        super(name, scope);
    }

    /**
     * Get the extended attributes structure.
     *
     * @return The extended attributes structure.
     */
    public AttributesStructureInfo getAttributesStructureInfo() {
        return (AttributesStructureInfo) getAttributesStructure();
    }

    protected AttributesStructure createAttributesStructure() {
        AttributesStructureInfo info = new AttributesStructureInfo(this);
        info.setAbstractAPIAttributesClass(true);
        return info;
    }

    /**
     * Set the name of the API attribute group class.
     *
     * @param apiElementClass The name of the API attribute group class.
     */
    public void setAPIElementClass(String apiElementClass) {
    }
}
