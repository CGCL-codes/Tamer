package com.antlersoft.ilanalyze;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Represents a method signature as read from input, including the return type
 * @author Michael A. MacDonald
 *
 */
public class Signature {

    private ReadType m_return_type;

    /** Collection of ReadArg values representing method arguments */
    private ArrayList m_arguments;

    public Signature() {
        m_arguments = new ArrayList();
    }

    public void addArgument(ReadArg arg) {
        m_arguments.add(arg);
    }

    public void setReturnType(ReadType ret) {
        m_return_type = ret;
    }

    public List getArguments() {
        return Collections.unmodifiableList(m_arguments);
    }

    /**
	 * 
	 * @return Return type of the method for which this is the signature
	 */
    public ReadType getReturnType() {
        return m_return_type;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('(');
        LoggingDBDriver.formatList(sb, m_arguments);
        sb.append(')');
        sb.append(m_return_type.toString());
        return sb.toString();
    }
}
