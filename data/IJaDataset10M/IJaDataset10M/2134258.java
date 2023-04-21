package com.webobjects.eocontrol;

public interface EORelationshipManipulation {

    public void addObjectToPropertyWithKey(Object eo, String key);

    public void removeObjectFromPropertyWithKey(Object eo, String key);

    public void addObjectToBothSidesOfRelationshipWithKey(EORelationshipManipulation eo, String key);

    public void removeObjectFromBothSidesOfRelationshipWithKey(EORelationshipManipulation eo, String key);
}
