package name.levering.ryan.sparql.logic.function;

import name.levering.ryan.sparql.common.SPARQLConstants;
import name.levering.ryan.sparql.model.logic.LogicFactory;
import name.levering.ryan.sparql.common.Literal;
import name.levering.ryan.sparql.common.SPARQLValueFactory;
import name.levering.ryan.sparql.common.URI;
import name.levering.ryan.sparql.common.Value;

/**
 * Represents the logic of the xsd:string column of the cast constructor table
 * in the SPARQL specification. Responsible for casting all known SPARQL types
 * to strings, if possible.
 * 
 * @author Ryan Levering
 * @version 1.1
 */
public class StringCastFunction extends CastFunction {

    /**
     * Creates a string cast function with a particular value factory to use for
     * literal creation.
     * 
     * @param factory the factory to use for literal creation
     */
    public StringCastFunction(SPARQLValueFactory factory) {
        super(factory);
    }

    /**
     * Casts the passed xsd:string value to a string literal.
     * 
     * @param literal the value to cast to a string literal
     * @return a literal of type xsd:string, representing the cast value
     */
    public Value castString(Literal literal) throws IllegalCastException {
        return this.factory.createLiteral(literal.getLabel(), SPARQLConstants.STRING_TYPE);
    }

    /**
     * Casts the passed xsd:float value to a string literal.
     * 
     * @param literal the value to cast to a string literal
     * @return a literal of type xsd:string, representing the cast value
     */
    public Value castFloat(Literal literal) throws IllegalCastException {
        return this.factory.createLiteral(literal.getLabel(), SPARQLConstants.STRING_TYPE);
    }

    /**
     * Casts the passed xsd:double value to a string literal.
     * 
     * @param literal the value to cast to a string literal
     * @return a literal of type xsd:string, representing the cast value
     */
    public Value castDouble(Literal literal) throws IllegalCastException {
        return this.factory.createLiteral(literal.getLabel(), SPARQLConstants.STRING_TYPE);
    }

    /**
     * Casts the passed xsd:decimal value to a string literal.
     * 
     * @param literal the value to cast to a string literal
     * @return a literal of type xsd:string, representing the cast value
     */
    public Value castDecimal(Literal literal) throws IllegalCastException {
        return this.factory.createLiteral(literal.getLabel(), SPARQLConstants.STRING_TYPE);
    }

    /**
     * Casts the passed xsd:integer value to a string literal.
     * 
     * @param literal the value to cast to a string literal
     * @return a literal of type xsd:string, representing the cast value
     */
    public Value castInteger(Literal literal) throws IllegalCastException {
        return this.factory.createLiteral(literal.getLabel(), SPARQLConstants.STRING_TYPE);
    }

    /**
     * Casts the passed xsd:dateTime value to a string literal.
     * 
     * @param literal the value to cast to a string literal
     * @return a literal of type xsd:string, representing the cast value
     */
    public Value castDateTime(Literal literal) throws IllegalCastException {
        return this.factory.createLiteral(literal.getLabel(), SPARQLConstants.STRING_TYPE);
    }

    /**
     * Casts the passed xsd:boolean value to a string literal.
     * 
     * @param literal the value to cast to a string literal
     * @return a literal of type xsd:string, representing the cast value
     */
    public Value castBoolean(Literal literal) throws IllegalCastException {
        return this.factory.createLiteral(literal.getLabel(), SPARQLConstants.STRING_TYPE);
    }

    /**
     * Casts the passed IRI to a string literal.
     * 
     * @param literal the value to cast to a string literal
     * @return a literal of type xsd:string, representing the cast value
     */
    public Value castIRI(URI iri) throws IllegalCastException {
        return this.factory.createLiteral(iri.toString(), SPARQLConstants.STRING_TYPE);
    }

    /**
     * Casts the passed untyped literal to a string literal.
     * 
     * @param literal the value to cast to a string literal
     * @return a literal of type xsd:string, representing the cast value
     */
    public Value castLiteral(Literal literal) throws IllegalCastException {
        return this.factory.createLiteral(literal.getLabel(), SPARQLConstants.STRING_TYPE);
    }

    /**
	 * Helper method to create a new factory that pulls out of the runtime logic
	 * and value what this function needs.
	 * 
	 * @return a factory that creates functions
	 */
    public static ExternalFunctionFactory getFactory() {
        return new ExternalFunctionFactory() {

            public ExternalFunction create(LogicFactory logicFactory, SPARQLValueFactory valueFactory) {
                return new StringCastFunction(valueFactory);
            }
        };
    }
}
