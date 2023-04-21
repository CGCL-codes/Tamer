package org.herasaf.xacml.core.function.impl.stringConversionFunctions;

import org.herasaf.xacml.core.function.Function;
import org.herasaf.xacml.core.function.FunctionProcessingException;

/**
 * <p>
 * The implementation of the
 * urn:oasis:names:tc:xacml:1.0:function:string-normalize-space function.
 * </p>
 * <p>
 * See: Apendix A.3 of the <a
 * href="http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20">
 * OASIS eXtensible Access Control Markup Langugage (XACML) 2.0, Errata 29 June
 * 2006</a> page 109, for further information.
 * </p>
 *
 * @author Stefan Oberholzer 
 * @version 1.0
 */
public class StringNormalizeSpaceFunction implements Function {

    private static final long serialVersionUID = 5618331318344051874L;

    private static final String ID = "urn:oasis:names:tc:xacml:1.0:function:string-normalize-space";

    /**
	 * {@inheritDoc} <br>
	 * <br>
	 * Strips off all leading and trailing white spaces.
	 */
    public Object handle(Object... args) throws FunctionProcessingException {
        try {
            if (args.length != 1) {
                throw new FunctionProcessingException("Invalid number of parameters");
            }
            return ((String) args[0]).trim();
        } catch (ClassCastException e) {
            throw new FunctionProcessingException("The arguments were of the wrong datatype.");
        } catch (FunctionProcessingException e) {
            throw e;
        } catch (Exception e) {
            throw new FunctionProcessingException(e);
        }
    }

    @Override
    public String toString() {
        return ID;
    }
}
