package com.sri.emo.wizard.creation.persistence;

import com.sri.emo.dbobj.WizDefinition;
import com.sri.emo.wizard.creation.model.CreationBeans;

/**
 * Completion DBObject Converter provides an adapter to bridge pure
 * POJO repository classes with Expresso DBObjects.
 *
 * @author Michael Rimov
 */
public interface CreationDBObjConverter {

    /**
     * Converts a Completion Bean to a Wiz Definition bean.  The Wizdefinition
     * bean will have links to navigate to the underlying completion beans.
     *
     * @param source CompletionBean the completion bean.
     * @return WizDefinition constructed wizard definition.
     */
    public WizDefinition convertToDBObject(CreationBeans source);

    /**
     * Converts a wizard definition to a completion bean.
     *
     * @param source WizDefinition
     * @return CompletionBean
     */
    public CreationBeans convertToBean(WizDefinition source);
}
