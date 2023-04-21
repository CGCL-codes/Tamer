package no.uio.ifi.kjetilos.javatraits.parser;

public class ASTFormalParameter extends SimpleNode implements Visitable {

    public ASTFormalParameter(int id) {
        super(id);
    }

    public ASTFormalParameter(JavaTParser p, int id) {
        super(p, id);
    }

    private int modifiers = 0;

    private boolean variableArity;

    public int getModifiers() {
        return modifiers;
    }

    public void setModifiers(int modifiers) {
        this.modifiers = modifiers;
    }

    public String toString() {
        return super.toString() + " modifiers=" + JavaTParser.ModifierSet.toString(modifiers);
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void setVariableArity() {
        variableArity = true;
    }

    public boolean isVariableArity() {
        return variableArity;
    }
}
