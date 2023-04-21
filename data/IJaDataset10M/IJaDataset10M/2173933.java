package com.farukcankaya.simplemodel.ast;

public class SMReferenceDeclaration extends SMFeatureDeclaration {

    private boolean multiple;

    private SMOppositeStatement oppositeStatement;

    public SMReferenceDeclaration(String name, int nameStart, int nameEnd, boolean multiple, int declStart, int declEnd) {
        super(name, nameStart, nameEnd, declStart, declEnd);
        this.multiple = multiple;
    }

    public SMOppositeStatement getOppositeStatement() {
        return oppositeStatement;
    }

    public boolean isMultiple() {
        return multiple;
    }

    public void setMultiple(boolean multiple) {
        this.multiple = multiple;
    }

    public void setOppositeStatement(SMOppositeStatement oppositeStatement) {
        this.oppositeStatement = oppositeStatement;
    }
}
