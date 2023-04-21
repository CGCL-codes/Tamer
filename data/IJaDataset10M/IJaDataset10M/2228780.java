package com.loribel.commons.demo.bo.generated;

import java.util.Collection;
import com.loribel.commons.business.GB_BusinessObjectDefault;
import com.loribel.commons.business.abstraction.GB_BOPrototype;
import com.loribel.commons.business.abstraction.GB_SimpleBusinessObject;
import com.loribel.commons.demo.bo.GB_PersonGroupBO;
import com.loribel.commons.util.CTools;

/**
 * Generated class for PersonGroup.
 */
public abstract class GB_PersonGroupBOGen extends GB_BusinessObjectDefault {

    public static final String BO_NAME = "PersonGroup";

    protected GB_PersonGroupBOGen() {
        super(BO_NAME);
    }

    public static final GB_BOPrototype newBOPrototype() {
        GB_BOPrototype retour = new GB_BOPrototype() {

            public String getName() {
                return GB_PersonGroupBO.BO_NAME;
            }

            public GB_SimpleBusinessObject newBusinessObject() {
                return new GB_PersonGroupBO();
            }
        };
        return retour;
    }

    /**
     * Getter for name.
     */
    public final String getName() {
        String retour = (String) getPropertyValue(BO_PROPERTY.NAME);
        return retour;
    }

    /**
     * Setter for name.
     */
    public final void setName(String a_name) {
        setPropertyValue(BO_PROPERTY.NAME, a_name);
    }

    /**
     * Getter for comment.
     */
    public final String getComment() {
        String retour = (String) getPropertyValue(BO_PROPERTY.COMMENT);
        return retour;
    }

    /**
     * Setter for comment.
     */
    public final void setComment(String a_comment) {
        setPropertyValue(BO_PROPERTY.COMMENT, a_comment);
    }

    public void addPerson(com.loribel.commons.demo.bo.GB_PersonBO a_person) {
        addPropertyValue(BO_PROPERTY.PERSON, a_person);
    }

    public final void removePerson(com.loribel.commons.demo.bo.GB_PersonBO a_person) {
        removePropertyValue(BO_PROPERTY.PERSON, a_person);
    }

    public final com.loribel.commons.demo.bo.GB_PersonBO[] getPersonArray() {
        Collection l_list = getPropertyValueList(BO_PROPERTY.PERSON);
        if (l_list == null) {
            return null;
        }
        int len = l_list.size();
        com.loribel.commons.demo.bo.GB_PersonBO[] retour = new com.loribel.commons.demo.bo.GB_PersonBO[len];
        return (com.loribel.commons.demo.bo.GB_PersonBO[]) l_list.toArray(retour);
    }

    public final com.loribel.commons.demo.bo.GB_PersonBO getPerson(int a_index) {
        return getPersonArray()[a_index];
    }

    public final int getPersonCount() {
        return CTools.getSize(getPropertyValueList(BO_PROPERTY.PERSON));
    }

    /**
     * Method used for ant task.
     */
    public final void addConfiguredPerson(com.loribel.commons.demo.bo.GB_PersonBO a_person) {
        this.addPerson(a_person);
    }

    /**
     * Property names of this Business Object.
     */
    public static final class BO_PROPERTY {

        public static final String NAME = "name";

        public static final String COMMENT = "comment";

        public static final String PERSON = "person";
    }
}
