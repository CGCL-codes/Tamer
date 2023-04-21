package org.deri.iris.builtins.datatype;

import static org.deri.iris.factory.Factory.BASIC;
import static org.deri.iris.factory.Factory.CONCRETE;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.terms.INumericTerm;
import org.deri.iris.api.terms.IStringTerm;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.concrete.INMTOKEN;

/**
 * Represents a data type conversion function, which converts supported data
 * type instances to {@link INMTOKEN} instances. The following data types are
 * supported:
 * <ul>
 * <li>Numeric</li>
 * <li>String</li>
 * </ul>
 */
public class ToNMTokenBuiltin extends ConversionBuiltin {

    private static final IPredicate PREDICATE = BASIC.createPredicate("TO_NMTOKEN", 2);

    /**
	 * Creates a new instance of this builtin.
	 * 
	 * @param terms An array of terms, where first one is the term to convert
	 *            and the last term represents the result of this data type
	 *            conversion.
	 * @throws NullPointerException If <code>terms</code> is <code>null</code>.
	 * @throws NullPointerException If the terms contain a <code>null</code>
	 *             value.
	 * @throws IllegalArgumentException If the length of the terms and the arity
	 *             of the predicate do not match.
	 */
    public ToNMTokenBuiltin(ITerm... terms) {
        super(PREDICATE, terms);
    }

    @Override
    protected INMTOKEN convert(ITerm term) {
        if (term instanceof INMTOKEN) {
            return (INMTOKEN) term;
        } else if (term instanceof INumericTerm) {
            return toNMTOKEN((INumericTerm) term);
        } else if (term instanceof IStringTerm) {
            return toNMTOKEN((IStringTerm) term);
        }
        return null;
    }

    /**
	 * Converts a {@link INumericTerm} term to a {@link INMTOKEN} term.
	 * 
	 * @param term The {@link INumericTerm} term to be converted.
	 * @return A new {@link INMTOKEN} term representing the result of the
	 *         conversion, or <code>null</code> if the conversion fails.
	 */
    public static INMTOKEN toNMTOKEN(INumericTerm term) {
        return CONCRETE.createNMTOKEN(term.toCanonicalString());
    }

    /**
	 * Converts a {@link IStringTerm} term to a {@link INMTOKEN} term.
	 * 
	 * @param term The {@link IStringTerm} term to be converted.
	 * @return A new {@link INMTOKEN} term representing the result of the
	 *         conversion, or <code>null</code> if the conversion fails.
	 */
    public static INMTOKEN toNMTOKEN(IStringTerm term) {
        return CONCRETE.createNMTOKEN(term.toCanonicalString());
    }
}
