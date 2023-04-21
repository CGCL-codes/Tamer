package com.ohioedge.j2ee.api.person.ejb;

/**
 * 
 * @(#)NameSuffixEJB.java 1.350 01/12/03
 * 
 * @author Sandeep Dixit
 * 
 * @version 1.350, 01/12/03
 * 
 * @see org.j2eebuilder.view.ValueObjectFactory#getDataVO(java.lang.Object,
 *      java.lang.Class)
 * 
 * @since OEC1.2
 */
public class NameSuffixBean extends org.j2eebuilder.model.ejb.SignatureImpl {

    public NameSuffixBean() {
    }

    public Integer nameSuffixID;

    public String name;

    public String description;

    public Integer getNameSuffixID() {
        return nameSuffixID;
    }

    public void setNameSuffixID(Integer nameSuffixID) {
        this.nameSuffixID = nameSuffixID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
