package name.levering.ryan.sparql.logic.function;

import name.levering.ryan.sparql.common.Literal;
import name.levering.ryan.sparql.common.RdfBindingRow;
import name.levering.ryan.sparql.common.Value;
import name.levering.ryan.sparql.model.data.CallExpressionData;
import name.levering.ryan.sparql.model.logic.ExpressionLogic;
import name.levering.ryan.sparql.model.logic.helper.ValueConversionLogic;

/**
 * The logic to evaluate the sop:langMatches SPARQL function, found in section
 * 11.4.11 of the specification. It returns whether or not a literal has a
 * language tag in the language range specified by the second argument.
 * 
 * @author Ryan Levering
 * @version 1.0
 */
public class LangMatchesLogic implements ExpressionLogic {

    /**
	 * The data that holds the arguments to evaluate for language range and
	 * literal to match.
	 */
    private final CallExpressionData data;

    /**
	 * The converter used to return the boolean value.
	 */
    private final ValueConversionLogic converter;

    /**
	 * Creates a new logic object that can check a literal for being in a
	 * certain language range.
	 * 
	 * @param data the data holding the argument for evaluation
	 * @param converter the value conversion logic to convert the boolean to a
	 *            literal
	 */
    public LangMatchesLogic(CallExpressionData data, ValueConversionLogic converter) {
        this.data = data;
        this.converter = converter;
    }

    /**
	 * Returns whether or not a literal falls within a language range.
	 * 
	 * @param bindings the value bindings to use in argument evaluation
	 * @return boolean literal representing the language match outcome
	 */
    public Value evaluate(RdfBindingRow bindings) {
        ExpressionLogic expression = (ExpressionLogic) this.data.getArguments().get(0);
        Literal literal = (Literal) expression.evaluate(bindings);
        String language = literal.getLanguage().toLowerCase();
        expression = (ExpressionLogic) this.data.getArguments().get(1);
        literal = (Literal) expression.evaluate(bindings);
        String toMatch = literal.getLabel();
        return this.converter.convertBoolean(language.startsWith(toMatch.toLowerCase()));
    }
}
