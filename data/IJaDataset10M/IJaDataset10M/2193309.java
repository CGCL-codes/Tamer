package net.sf.jelly.apt.strategies;

import net.sf.jelly.apt.TemplateException;

/**
 * Thrown if a parameter is missing for a strategy.
 * 
 * @author Ryan Heaton
 */
public class MissingParameterException extends TemplateException {

    private String parameter;

    /**
   * @param parameter The parameter that was missing.
   */
    public MissingParameterException(String parameter) {
        super("The '" + parameter + "' parameter must be specified.");
        this.parameter = parameter;
    }

    /**
   * @param message The message.
   * @param parameter The parameter that was missing.
   */
    public MissingParameterException(String message, String parameter) {
        super(message);
        this.parameter = parameter;
    }

    /**
   * The parameter that was missing.
   *
   * @return The parameter that was missing.
   */
    public String getParameter() {
        return parameter;
    }
}
