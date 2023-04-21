package org.jmlspecs.jml4.fspv.simpl.ast;

public class SimplProofObligation extends SimplDeclaration {

    public final SimplProcedure procedure;

    public final SimplLemma specLemma;

    public String imports;

    public SimplProofObligation(SimplProcedure proc, SimplLemma specLemma) {
        this.procedure = proc;
        this.specLemma = specLemma;
    }

    public String toString() {
        String result = SimplConstants.EMPTY;
        result += this.procedure;
        result += this.specLemma;
        return result;
    }
}
