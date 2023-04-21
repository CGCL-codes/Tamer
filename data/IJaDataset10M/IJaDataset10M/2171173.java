package com.newisys.dv.ifgen.schema;

import com.newisys.langschema.NamedObject;

/**
 * Ifgen schema object representing enumerated types.
 * 
 * @author Trevor Robinson
 * @author Jon Nall
 */
public final class IfgenEnumType extends IfgenType implements NamedObject {

    private final IfgenName name;

    private IfgenEnum enumeration;

    public IfgenEnumType(IfgenSchema schema, IfgenName name) {
        super(schema);
        this.name = name;
    }

    public String toDebugString() {
        return "enum " + name;
    }

    public String toSourceString() {
        return name.getIdentifier();
    }

    public IfgenName getName() {
        return name;
    }

    public void setEnumeration(IfgenEnum enumeration) {
        this.enumeration = enumeration;
    }

    public IfgenEnum getEnumeration() {
        return enumeration;
    }
}
