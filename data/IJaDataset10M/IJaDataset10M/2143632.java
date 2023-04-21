package com.sun.org.apache.xalan.internal.xsltc.compiler.util;

import com.sun.org.apache.bcel.internal.generic.ALOAD;
import com.sun.org.apache.bcel.internal.generic.Instruction;
import com.sun.org.apache.xalan.internal.xsltc.compiler.Stylesheet;

/**
 * This class implements auxliary classes needed to compile 
 * filters (predicates). These classes defined a single method
 * of type <tt>TestGenerator</tt>.
 * @author Jacek Ambroziak
 * @author Santiago Pericas-Geertsen
 */
public final class FilterGenerator extends ClassGenerator {

    private static int TRANSLET_INDEX = 5;

    private final Instruction _aloadTranslet;

    public FilterGenerator(String className, String superClassName, String fileName, int accessFlags, String[] interfaces, Stylesheet stylesheet) {
        super(className, superClassName, fileName, accessFlags, interfaces, stylesheet);
        _aloadTranslet = new ALOAD(TRANSLET_INDEX);
    }

    /**
     * The index of the translet pointer within the execution of
     * the test method.
     */
    public final Instruction loadTranslet() {
        return _aloadTranslet;
    }

    /**
     * Returns <tt>true</tt> since this class is external to the
     * translet.
     */
    public boolean isExternal() {
        return true;
    }
}
