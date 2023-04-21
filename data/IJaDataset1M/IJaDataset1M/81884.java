package edu.mit.csail.pag.amock.representation;

public interface DeclarablePrimary extends Primary, OptionallyDeclarable {

    public String getPrimaryVariableName();

    public String getConstructor();

    public String getClassSourceName();
}
