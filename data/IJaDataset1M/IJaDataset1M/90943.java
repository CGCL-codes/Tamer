package com.newisys.dv.ifgen.schema;

/**
 * Ifgen schema object for HVL/Java tasks.
 * 
 * @author Trevor Robinson
 */
public final class IfgenHVLTask extends IfgenTask {

    private static final long serialVersionUID = 3689069555951939634L;

    public IfgenHVLTask(IfgenSchema schema, IfgenName name) {
        super(schema, name);
    }

    public void accept(IfgenSchemaMemberVisitor visitor) {
        visitor.visit(this);
    }

    public void accept(IfgenPackageMemberVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void accept(IfgenSchemaObjectVisitor visitor) {
        visitor.visit(this);
    }
}
