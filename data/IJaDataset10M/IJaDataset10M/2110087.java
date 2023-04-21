package org.xmlcml.cml;

public interface AttributeConvention {

    /** "convention" attribute name */
    public static final String CONVENTION = "convention";

    public void setConventionName(String conventionName);

    public String getConventionName();

    /** maintenance method */
    public boolean updateDOMHasConvention();

    public boolean processDOMHasConvention();
}
