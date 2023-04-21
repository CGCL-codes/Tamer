package net.sf.refactorit.query.usage;

import net.sf.refactorit.classmodel.BinField;
import net.sf.refactorit.classmodel.BinSourceConstruct;
import net.sf.refactorit.parser.ASTImpl;

/**
 *
 * @author  tanel
 */
public final class EncapsulationInvocationData extends InvocationData {

    private final boolean encapsulationPossible;

    public EncapsulationInvocationData(final BinField field, final Object location, final ASTImpl ast, final BinSourceConstruct sourceConstruct, final boolean encapsulationPossible) {
        super(field, location, ast, sourceConstruct);
        this.encapsulationPossible = encapsulationPossible;
    }

    public final boolean isEncapsulationPossible() {
        return this.encapsulationPossible;
    }

    public final String toString() {
        return "EncapsulationInvocationData [encapsulation possible: " + encapsulationPossible + "]";
    }
}
