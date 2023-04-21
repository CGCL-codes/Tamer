package org.xteam.pascal.parser.ast;

import org.xteam.parser.runtime.Span;

public class SetType extends Type {

    public org.xteam.parser.runtime.reflect.AstNodeType getNodeType() {
        return PascalAstPackage.INSTANCE.getSetTypeType();
    }

    protected PackedFlag flag;

    protected Type type;

    public SetType(Span span, PackedFlag flag, Type type) {
        super(span);
        this.flag = flag;
        this.type = type;
    }

    public PackedFlag getFlag() {
        return flag;
    }

    public void setFlag(PackedFlag flag) {
        this.flag = flag;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void visit(IPascalVisitor visitor) {
        visitor.visitSetType(this);
    }
}
