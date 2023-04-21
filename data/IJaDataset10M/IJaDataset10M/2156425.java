package com.loribel.java.abstraction;

/**
 * Abstraction of a Java attribute.
 */
public interface GB_JavaAttribute extends GB_JavaAbstract {

    public void setType(String a_type);

    public String getType();

    public void setVisibility(String a_visibility);

    public String getVisibility();

    public void setStatic(boolean a_flagStatic);

    public boolean isStatic();

    public void setTransient(boolean a_flagTransient);

    public boolean isTransient();

    public void setVolatile(boolean a_flagVolatile);

    public boolean isVolatile();

    public void setInitValue(String a_initValue);

    public String getInitValue();

    public void setInitValueBlock(GB_JavaBlock a_block);

    public GB_JavaBlock getInitValueBlock();

    public void addMethod(GB_JavaMethod a_method);

    public GB_JavaMethods getMethods();
}
