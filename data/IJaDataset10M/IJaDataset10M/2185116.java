package com.loribel.commons.exception;

import java.util.Hashtable;
import java.util.Map;
import com.loribel.commons.util.STools;

/**
 * Classe GB_ParameterException.
 *
 * @author Gregory Borelli
 */
public abstract class GB_ParameterException extends GB_RuntimeException {

    /**
     * Class source of the exception.
     */
    protected Class classSource;

    /**
     * Method source of the exception.
     */
    protected String methodSource;

    /**
     * Parameter name that launch this exception.
     */
    protected String parameterName;

    /**
     * Constructor of GB_ParameterException without parameter.
     */
    public GB_ParameterException() {
        super("ParameterException");
    }

    /**
     * Constructor of GB_ParameterException with parameter(s).
     *
     * @param a_classSource Class - class source of the exception
     * @param a_methodSource String - method source of the exception
     * @param a_parameterName String - parameter name that launch this exception
     */
    public GB_ParameterException(Class a_classSource, String a_methodSource, String a_parameterName) {
        this();
        classSource = a_classSource;
        methodSource = a_methodSource;
        parameterName = a_parameterName;
    }

    /**
     * Constructor of GB_ParameterException with parameter(s).
     *
     * @param a_source Exception -
     */
    public GB_ParameterException(Exception a_source) {
        super("ParameterException", a_source);
    }

    /**
     * Constructor of GB_ParameterException with parameter(s).
     *
     * @param a_msg String -
     */
    public GB_ParameterException(String a_msg) {
        super(a_msg);
    }

    /**
     * Constructor of GB_ParameterException with parameter(s).
     *
     * @param a_msg String -
     * @param a_source Exception -
     */
    public GB_ParameterException(String a_msg, Exception a_source) {
        super(a_msg, a_source);
    }

    /**
     * Get Class source of the exception..
     *
     * @return Class -<tt>classSource</tt>
     */
    public Class getClassSource() {
        return classSource;
    }

    /**
     * Return the exception message.
     *
     * @param a_msg String -
     * @return String
     */
    protected String getLocalizedMessage(String a_msg) {
        Map l_findReplace = new Hashtable();
        if (classSource == null) {
            l_findReplace.put("$CLASS$", "??");
            l_findReplace.put("$METHOD$", "??");
            l_findReplace.put("$PARAMETER$", "");
        } else {
            l_findReplace.put("$CLASS$", classSource.getName());
            l_findReplace.put("$METHOD$", methodSource);
            l_findReplace.put("$PARAMETER$", parameterName);
        }
        String retour = STools.replace(a_msg, l_findReplace);
        return retour;
    }

    /**
     * Get Method source of the exception..
     *
     * @return String -<tt>methodSource</tt>
     */
    public String getMethodSource() {
        return methodSource;
    }

    /**
     * Get Parameter name that launch this exception..
     *
     * @return String -<tt>parameterName</tt>
     */
    public String getParameterName() {
        return parameterName;
    }

    /**
     * Set Class source of the exception..
     *
     * @param a_classSource Class -<tt>classSource</tt>
     */
    protected void setClassSource(Class a_classSource) {
        classSource = a_classSource;
    }

    /**
     * Set Method source of the exception..
     *
     * @param a_methodSource String -<tt>methodSource</tt>
     */
    protected void setMethodSource(String a_methodSource) {
        methodSource = a_methodSource;
    }

    /**
     * Set Parameter name that launch this exception..
     *
     * @param a_parameterName String -<tt>parameterName</tt>
     */
    protected void setParameterName(String a_parameterName) {
        parameterName = a_parameterName;
    }
}
