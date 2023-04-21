package bgl.compiler.types;

public class Antecedent extends CompilerType {

    private Proposition proposition = null;

    public Antecedent(Proposition proposition) {
        this.proposition = proposition;
    }

    public Proposition getProposition() {
        return this.proposition;
    }
}
