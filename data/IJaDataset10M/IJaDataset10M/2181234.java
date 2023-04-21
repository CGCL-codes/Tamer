package org.herasaf.xacml.core.function.impl.logicalFunctions;

import org.herasaf.xacml.core.function.Function;
import org.herasaf.xacml.core.function.FunctionProcessingException;

/**
 * The implementation of the urn:oasis:names:tc:xacml:1.0:function:not.
 * See: Apendix A.3 of the <a
 * href="http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20">
 * OASIS eXtensible Access Control Markup Langugage (XACML) 2.0, Errata 29 June
 * 2006</a> page 109, for further information.
 *
 * @author Sacha Dolski (sdolski@solnet.ch)
 * @version 1.0
 */
public class NotFunction implements Function {

    /**
	 *
	 */
    private static final long serialVersionUID = -8657888777873703249L;

    private static final String ID = "urn:oasis:names:tc:xacml:1.0:function:not";

    /**
	 * Takes at one {@link Boolean} value as parameter. And returns the inverted
	 * value of the {@link Boolean}.
	 */
    public Object handle(Object... args) throws FunctionProcessingException {
        try {
            if (args.length != 1) {
                throw new FunctionProcessingException("Invalid number of parameters");
            }
            return !(Boolean) args[0];
        } catch (ClassCastException e) {
            throw new FunctionProcessingException(e);
        } catch (Exception e) {
            throw new FunctionProcessingException(e);
        }
    }

    @Override
    public String toString() {
        return ID;
    }
}
