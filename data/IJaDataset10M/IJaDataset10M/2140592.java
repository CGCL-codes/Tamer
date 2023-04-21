package net.sourceforge.iwii.db.dev.bo;

/**
 * This interface represents any convertable (to entity) object in application 
 * 
 * @author Grzegorz 'Gregor736' Wolszczak
 * @version 1.00
 * @param KeyClass represents class of id from entity
 */
public interface IConvertableBO<KeyClass> extends IBusinessObject {

    /**
     * Gets database id from entity object corresponding to this one
     * @return entity database id
     */
    public KeyClass getDatabaseId();
}
