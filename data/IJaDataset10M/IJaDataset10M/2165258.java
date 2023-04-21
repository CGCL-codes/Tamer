package org.ucl.xpath.ast;

import org.ucl.xpath.types.*;

/**
 * Support for Schema Element Test.
 */
public class SchemaElemTest extends KindTest {

    private QName _arg;

    /**
	 * Constructor for SchemaElemTest.
	 * @param arg QName argument.
	 */
    public SchemaElemTest(QName arg) {
        _arg = arg;
    }

    /**
	 * Support for Visitor interface.
	 * @return Result of Visitor operation.
	 */
    public Object accept(XPathVisitor v) {
        return v.visit(this);
    }

    /**
	 * Support for QName interface.
 	 * @return Result of QName operation.
	 */
    public QName name() {
        return _arg;
    }
}
