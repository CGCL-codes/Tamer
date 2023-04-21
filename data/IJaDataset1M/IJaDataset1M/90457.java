package de.fraunhofer.isst.axbench.axlang.elements.expressions;

import de.fraunhofer.isst.axbench.axlang.utilities.AXLException;
import de.fraunhofer.isst.axbench.axlang.utilities.ReferenceKind;
import de.fraunhofer.isst.axbench.axlang.utilities.Role;
import de.fraunhofer.isst.axbench.axlang.utilities.ValidType;
import de.fraunhofer.isst.axbench.axlang.utilities.attributes.Cardinality;

/**
 * @brief An infix expression is a composed expression that consists of a left and a right expression and an operator
 * 
 * @par Children
 * - <strong>1</strong> left expression (Expression)
 * - <strong>1</strong> right expression (Expression)
 * 
 * @par Attributes
 * - operator (from composed expression)
 * 
 * @author mgrosse
 * @version 0.9.0
 * @since 0.9.0
 */
public class InfixExpression extends ComposedExpression {

    /**
	 * @brief constructor
	 */
    public InfixExpression() {
        super();
        setValidElementTypes(ReferenceKind.CHILD, new ValidType(Role.LEFTEXPRESSION, new Cardinality(1, 1)), new ValidType(Role.RIGHTEXPRESSION, new Cardinality(1, 1)));
    }

    /**
	 * @brief getter method for the left expression
	 * @return the left expression
	 */
    public Expression getLeftExpression() {
        return (Expression) getChild(Role.LEFTEXPRESSION);
    }

    /**
	 * @brief setter method for the left expression
	 * @param newLeftExpression the new left expression 
	 * @throws AXLException if the new left expression cannot be set
	 */
    public void setLeftExpression(Expression newLeftExpression) throws AXLException {
        addChild(newLeftExpression, Role.LEFTEXPRESSION);
    }

    /**
	 * @brief getter method for the right expression
	 * @return the right expression
	 */
    public Expression getRightExpression() {
        return (Expression) getChild(Role.RIGHTEXPRESSION);
    }

    /**
	 * @brief setter method for the right expression
	 * @param newRightExpression the new right expression 
	 * @throws AXLException if the new right expression cannot be set
	 */
    public void setRightExpression(Expression newRightExpression) throws AXLException {
        addChild(newRightExpression, Role.RIGHTEXPRESSION);
    }
}
