package org.gomba;

import java.util.Enumeration;
import java.util.Vector;
import org.gomba.utils.convert.ConvertUtilsWrapper;

/**
 * An Expression is created from a string containing ${} parameters. The
 * expression is evaluated by the <code>replaceParameters</code> method.
 * 
 * <p>
 * A ${} parameter has the following syntax:
 * <code>domain.paramName JavaType defaultValue</code>. JavaType and
 * defaultValue are optional.
 * </p>
 * 
 * <p>
 * Examples:
 * 
 * <pre>
 *   ${param.myParam}
 *   ${param.myParam java.lang.Integer}
 *   ${param.myParam java.lang.Integer 10}
 *   ${path.1}
 *   ${column.myColumn java.util.Date}
 * </pre>
 * 
 * </p>
 * 
 * @author Flavio Tordini
 * @version $Id: Expression.java,v 1.5 2007/05/16 12:44:22 flaviotordini Exp $
 */
public final class Expression {

    /**
     * A String representing a null default value.
     * Usage: ${param.myParam java.util.Date null}
     */
    private static final String DEFAULT_NULL_VALUE = "null";

    /**
     * Vector of <code>String</code> with null elements corresponding to
     * parameters.
     */
    private final Vector fragments;

    /**
     * Vector of <code>ParameterDefinition</code> with null elements
     * corresponding to fragments.
     */
    private final Vector parameters;

    /**
     * Constructor
     */
    public Expression(String expression) throws Exception {
        if (expression == null) {
            throw new IllegalArgumentException("Expression cannot be null.");
        }
        this.fragments = new Vector();
        this.parameters = new Vector();
        Expression.parseParameters(expression, this.fragments, this.parameters);
    }

    /**
     * Replaces <code>${domain.paramName}</code> expressions in the given
     * query definition with the actual values.
     * 
     * @param parameterResolver
     *                   The object used to resolve parameters
     * 
     * @exception Exception
     *                        if the string contains an opening <code>${</code>
     *                        without a closing <code>}</code>
     * @return the original string with the properties replaced by question
     *               marks. If the original string contains only a ${} parameter, the
     *               typed value of that parameter is returned, otherwise a
     *               <code>String</code> is returned.
     */
    public Object replaceParameters(ParameterResolver parameterResolver) throws Exception {
        if (this.parameters.size() == 1 && this.fragments.size() == 1 && this.fragments.get(0) == null) {
            ParameterDefinition parameterDefinition = (ParameterDefinition) this.parameters.get(0);
            Object parameterValue = parameterResolver.getParameterValue(parameterDefinition);
            return parameterValue;
        }
        final StringBuffer sb = new StringBuffer();
        final Enumeration i = this.fragments.elements();
        final Enumeration j = this.parameters.elements();
        while (i.hasMoreElements()) {
            String fragment = (String) i.nextElement();
            if (fragment == null) {
                ParameterDefinition parameterDefinition = (ParameterDefinition) j.nextElement();
                Object parameterValue = parameterResolver.getParameterValue(parameterDefinition);
                if (parameterValue == null) {
                    throw new MissingParameterException("Missing parameter: " + parameterDefinition);
                }
                sb.append(parameterValue);
            } else {
                sb.append(fragment);
            }
        }
        return sb.toString();
    }

    /**
     * Parse parameters. Fill the fragments with string literals and parameters
     * with <code>ParameterDefinition</code> objects. For each parameter a
     * null value is placed is the fragments vector and viceversa.
     */
    public static void parseParameters(String expression, Vector fragments, Vector parameters) throws Exception {
        int prev = 0;
        int pos;
        while ((pos = expression.indexOf("$", prev)) >= 0) {
            if (pos > 0) {
                fragments.addElement(expression.substring(prev, pos));
            }
            if (pos == (expression.length() - 1)) {
                fragments.addElement("$");
                prev = pos + 1;
            } else if (expression.charAt(pos + 1) != '{') {
                if (expression.charAt(pos + 1) == '$') {
                    fragments.addElement("$");
                    prev = pos + 2;
                } else {
                    fragments.addElement(expression.substring(pos, pos + 2));
                    prev = pos + 2;
                }
            } else {
                int endName = expression.indexOf('}', pos);
                if (endName < 0) {
                    throw new Exception("Syntax error in parameter: " + expression);
                }
                String propertyDef = expression.substring(pos + 2, endName);
                ParameterDefinition parameterDefinition = parseParameterDefinition(propertyDef);
                fragments.addElement(null);
                parameters.addElement(parameterDefinition);
                prev = endName + 1;
            }
        }
        if (prev < expression.length()) {
            fragments.addElement(expression.substring(prev));
        }
    }

    /**
     * Parse a parameter definition.
     * 
     * @param parameter
     *                   A string with the following syntax:
     *                   <code>domain.param JavaType defaultValue</code>. JavaType
     *                   and defaultValue are optional.
     */
    private static final ParameterDefinition parseParameterDefinition(String parameter) throws Exception {
        boolean escape = false;
        boolean quote = false;
        int tokenCount = 0;
        String[] tokens = new String[3];
        StringBuffer token = new StringBuffer();
        for (int i = 0; i < parameter.length(); i++) {
            final char c = parameter.charAt(i);
            switch(c) {
                case '\\':
                    if (escape) {
                        token.append(c);
                        escape = false;
                    } else {
                        escape = true;
                    }
                    break;
                case '"':
                    if (escape) {
                        token.append(c);
                    } else if (quote) {
                        quote = false;
                    } else {
                        quote = true;
                    }
                    escape = false;
                    break;
                case ' ':
                    if (quote) {
                        token.append(c);
                    } else if (escape) {
                        token.append(c);
                    } else {
                        tokens[tokenCount] = token.toString();
                        token = new StringBuffer();
                        tokenCount++;
                    }
                    escape = false;
                    break;
                default:
                    token.append(c);
                    escape = false;
            }
        }
        if (token.length() > 0) {
            tokens[tokenCount] = token.toString();
        }
        if (tokens[0] == null) {
            throw new Exception("Missing domain.name in parameter: " + parameter);
        }
        final String domainAndName = tokens[0];
        final int domainSeparatorIndex = domainAndName.indexOf('.');
        if (domainSeparatorIndex < 0 || domainSeparatorIndex == domainAndName.length() - 1) {
            throw new Exception("Syntax error in parameter: " + parameter);
        }
        final String domain = domainAndName.substring(0, domainSeparatorIndex);
        final String name = domainAndName.substring(domainSeparatorIndex + 1);
        final Class type;
        Object defaultValue = null;
        boolean nullable = false;
        if (tokens[1] != null) {
            type = Class.forName(tokens[1]);
            if (tokens[2] != null) {
                if (DEFAULT_NULL_VALUE.equals(tokens[2])) {
                    nullable = true;
                } else {
                    defaultValue = ConvertUtilsWrapper.convert(tokens[2], type);
                }
            }
        } else {
            type = null;
        }
        ParameterDefinition parameterDefinition = new ParameterDefinition(domain, name, type, defaultValue, nullable);
        return parameterDefinition;
    }
}
