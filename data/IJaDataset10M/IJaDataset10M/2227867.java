package com.loribel.java.impl;

import com.loribel.java.abstraction.GB_JavaAttribute;
import com.loribel.java.abstraction.GB_JavaBlock;
import com.loribel.java.abstraction.GB_JavaMethod;
import com.loribel.java.abstraction.GB_JavaMethods;

/**
 * Default implementation of {@link GB_JavaAttribute}.
 */
public class GB_JavaAttributeImpl extends GB_JavaAbstractImpl implements GB_JavaAttribute {

    private String type;

    private boolean flagStatic;

    private boolean flagTransient;

    private boolean flagVolatile;

    private String visibility = "private";

    private String initValue;

    protected GB_JavaMethods methods = new GB_JavaMethodsImpl();

    private GB_JavaBlock initLines = new GB_JavaBlockImpl();

    public GB_JavaAttributeImpl() {
        super();
    }

    public GB_JavaAttributeImpl(String a_type, String a_name) {
        super(a_name);
        type = a_type;
    }

    public void setType(String a_type) {
        type = a_type;
    }

    public String getType() {
        return type;
    }

    public void setVisibility(String a_visibility) {
        visibility = a_visibility;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setInitValue(String a_initValue) {
        initValue = a_initValue;
    }

    public String getInitValue() {
        return initValue;
    }

    public void setStatic(boolean a_flagStatic) {
        flagStatic = a_flagStatic;
    }

    public boolean isStatic() {
        return flagStatic;
    }

    public void setTransient(boolean a_flagTransient) {
        flagTransient = a_flagTransient;
    }

    public boolean isTransient() {
        return flagTransient;
    }

    public void addMethod(GB_JavaMethod a_method) {
        methods.add(a_method);
    }

    public GB_JavaMethods getMethods() {
        return methods;
    }

    public void setInitValueBlock(GB_JavaBlock a_block) {
        initLines = a_block;
    }

    public GB_JavaBlock getInitValueBlock() {
        return initLines;
    }

    public void setVolatile(boolean a_flagVolatile) {
        flagVolatile = a_flagVolatile;
    }

    public boolean isVolatile() {
        return flagVolatile;
    }
}
