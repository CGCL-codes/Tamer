package com.jasonwjones.hyperion.jessub;

import com.jasonwjones.hyperion.jessub.formattingvalue.FormattingValueFactory;

/**
 * The main class representing a single replaceable expression in a Jessub
 * input file
 * @author jasonwjones
 *
 */
public class Expression {

    protected String formattingCode;

    protected Object[] formattingValues;

    /**
	 * Construct a new Expression from the given code. Note that the code
	 * does not have the ${ or } characters in it -- it is presumed those have
	 * been stripped out already (generally in Replacer)
	 * @param expressionCode
	 */
    public Expression(String expressionCode) {
        String[] components = expressionCode.split(";");
        formattingCode = components[0];
        int numberOfFormattingParameters = components.length - 1;
        if (numberOfFormattingParameters > 0) {
            formattingValues = new Object[numberOfFormattingParameters];
            FormattingValueFactory factory = new FormattingValueFactory();
            for (int formatValueIndex = 1; formatValueIndex < components.length; formatValueIndex++) {
                formattingValues[formatValueIndex - 1] = factory.create(components[formatValueIndex]).createValue();
            }
        } else {
            String message = "Warning: there are no format parameters for the formatting code %s. Did you add them in with a semi-colon?";
            System.err.println(String.format(message, formattingCode));
        }
    }

    /** 
	 * Formatting code setter
	 * @param formattingCode
	 */
    public void setFormattingCode(String formattingCode) {
        this.formattingCode = formattingCode;
    }

    /**
	 * Formatting code getter 
	 * @return
	 */
    public String getFormattingCode() {
        return formattingCode;
    }

    /**
	 * Generate the code for this expression. If there is an error with the 
	 * syntax of the formatter, it'll be specified and given back as the 
	 * return value (instead of throwing an error).
	 * @return expression or error message if there is a problem
	 */
    public String generate() {
        String returnVal = null;
        try {
            returnVal = String.format(formattingCode, formattingValues);
        } catch (Exception e) {
            String errorTemplate = "${Code %s causes error: %s}";
            System.out.println(e.getMessage());
            e.printStackTrace();
            returnVal = String.format(errorTemplate, formattingCode, e.getMessage());
        }
        return returnVal;
    }

    /**
	 * Simple string implementation
	 */
    @Override
    public String toString() {
        String format = " %s, from code %s";
        return String.format(format, generate(), formattingCode);
    }
}
