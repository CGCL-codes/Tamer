package gov.nist.scap.xccdf.document;

import gov.nist.scap.xccdf.PropertyNotFoundException;
import gov.nist.scap.xccdf.XCCDFVisitorHandler;

public interface Value extends Item {

    public enum Operator {

        EQUALS("equals"), NOT_EQUAL("not equal"), GREATER_THAN("greater than"), LESS_THAN("less than"), GREATER_THAN_OR_EQUAL("greater than or equal"), LESS_THAN_OR_EQUAL("less than or equal"), PATTERN_MATCH("pattern match");

        private Operator(String name) {
        }
    }

    public enum Type {

        NUMBER, STRING, BOOLEAN
    }

    Operator getOperator();

    void setOperator(Operator operator);

    void visit(XCCDFVisitorHandler handler, Group parent);

    /**
	 * 
	 * @param selector
	 * @return <code>true</code> if the selector affected a change in the value
	 */
    boolean applySelector(String selector);

    LiteralValue getLiteralValue();

    void setLiteralValue(LiteralValue literal) throws PropertyNotFoundException;

    Type getType();
}
