package org.xteam.pascal.parser.ast;

import org.xteam.parser.runtime.Span;

public class FunctionParameter extends Parameter {

    public org.xteam.parser.runtime.reflect.AstNodeType getNodeType() {
        return PascalAstPackage.INSTANCE.getFunctionParameterType();
    }

    protected Identifier name;

    protected org.xteam.parser.runtime.AstList<Parameter> parameters;

    protected Identifier returnType;

    public FunctionParameter(Span span, Identifier name, org.xteam.parser.runtime.AstList<Parameter> parameters, Identifier returnType) {
        super(span);
        this.name = name;
        this.parameters = parameters;
        this.returnType = returnType;
    }

    public Identifier getName() {
        return name;
    }

    public void setName(Identifier name) {
        this.name = name;
    }

    public org.xteam.parser.runtime.AstList<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(org.xteam.parser.runtime.AstList<Parameter> parameters) {
        this.parameters = parameters;
    }

    public Identifier getReturnType() {
        return returnType;
    }

    public void setReturnType(Identifier returnType) {
        this.returnType = returnType;
    }

    public void visit(IPascalVisitor visitor) {
        visitor.visitFunctionParameter(this);
    }
}
