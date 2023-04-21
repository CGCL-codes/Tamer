package org.ogre4j;

import org.xbig.base.*;

public interface IAnimableObject extends INativeObject {

    public interface IAnimableDictionaryMap extends INativeObject, org.std.Imap<String, org.ogre4j.IStringVector> {

        /** **/
        public void clear();

        /** **/
        public int count(String key);

        /** **/
        public boolean empty();

        /** **/
        public int erase(String key);

        /** **/
        public int max_size();

        /** **/
        public int size();

        /** **/
        public org.ogre4j.IStringVector get(String key);

        /** **/
        public void insert(String key, org.ogre4j.IStringVector value);
    }

    /** 
    Gets a list of animable value names for this object. **/
    public org.ogre4j.IStringVector getAnimableValueNames();

    /** 
    Create a reference-counted AnimableValuePtr for the named value. **/
    public void createAnimableValue(org.ogre4j.IAnimableValuePtr returnValue, String valueName);
}
