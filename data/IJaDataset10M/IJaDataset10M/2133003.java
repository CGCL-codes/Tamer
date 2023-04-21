package org.emftext.language.OCL.resource.OCL.grammar;

public class OCLWhiteSpace extends org.emftext.language.OCL.resource.OCL.grammar.OCLFormattingElement {

    private final int amount;

    public OCLWhiteSpace(int amount, org.emftext.language.OCL.resource.OCL.grammar.OCLCardinality cardinality) {
        super(cardinality);
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public String toString() {
        return "#" + getAmount();
    }
}
