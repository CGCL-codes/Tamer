package com.cube42.util.instruction;

import com.cube42.util.exception.Cube42Exception;
import com.cube42.util.exception.Cube42NullParameterException;

/**
 * ParameterDefinition for Strings
 *
 * @author  Matt Paulin
 * @version $Id: StringParameterDef.java,v 1.3 2003/01/15 06:52:12 zer0wing Exp $
 */
public class StringParameterDef extends ParameterDef {

    /**
     * Constructs the StringParameterDef
     *
     * @param   name        The name of the parameter
     * @param   description The description for this type of parameter
     */
    public StringParameterDef(String name, String description) {
        super(name, description);
    }

    /**
     * Forms a parameter with the specified value
     *
     * @param   value   The value of the string to put in the parameter
     */
    public StringParameter formParameter(String value) {
        return new StringParameter(this.getName(), value);
    }

    /**
     * Validates the provided parameter to make sure it satifies
     * parameter definition
     *
     * @param   parameter   The parameter to check
     * @throws  Cube42Exception if the parameter doesn't fit
     */
    public void validate(Parameter parameter) throws Cube42Exception {
        Cube42NullParameterException.checkNull(parameter, "parameter", "validate", this);
        if (parameter instanceof StringParameter) {
            if (parameter.getName() != this.getName()) {
                throw new Cube42Exception(InstructionSystemCodes.WRONG_PARAMETER_NAME, new Object[] { this.getName(), parameter.getName() });
            }
        } else {
            throw new Cube42Exception(InstructionSystemCodes.WRONG_PARAMETER_TYPE, new Object[] { "StringParameter", parameter.getClass().getName() });
        }
    }
}
