package org.oclc.da.ndiipp.struts.entity.util;

import org.oclc.da.ndiipp.struts.core.util.NDIIPPUIBean;

/**
 * This is the basic object for entities.
 * <P>
 * Created on Apr 6th, 2005
 * @author nelsonj
 */
public class NameQualifierBean extends NDIIPPUIBean {

    /**
     * value set when there is no qualifier value
     */
    public static String noQualifier = "::";

    /**
     * The name name
     */
    private String name = "";

    /**
     * The content standard
     */
    private String qualifier = "";

    /**
     * This is the empty constructor for the bean.
     */
    public NameQualifierBean() {
        name = "";
        qualifier = "";
    }

    /**
     * This is the full constructor for the bean.
     * <P>
     * @param name The object name
     * @param qualifier The object qualifier
     */
    public NameQualifierBean(String name, String qualifier) {
        this.name = name;
        this.qualifier = qualifier;
    }

    /**
     * Get the name
     * <P>
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the qualifier
     * <P>
     * @return Returns the qualifier.
     */
    public String getQualifier() {
        return qualifier;
    }

    /**
     * Set the name
     * <P>
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param qualifier The qualifier to set.
     */
    public void setQualifier(String qualifier) {
        this.qualifier = qualifier;
    }

    /**
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        String nqString = super.toString() + "Name = " + name + ", Qualifier = " + qualifier;
        return (nqString);
    }
}
