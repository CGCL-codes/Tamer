package org.starobjects.tested.fitnesse.internal.fixtures.perform.checkthat;

public abstract class ThatValidityAbstract extends ThatSubcommandAbstract {

    private final ValidityAssertion assertion;

    public ThatValidityAbstract(final ValidityAssertion assertion) {
        super(assertion.getKeys());
        this.assertion = assertion;
    }

    public ValidityAssertion getAssertion() {
        return assertion;
    }
}
