package com.genia.toolbox.basics.i18n;

/**
 * interface the enumerations should implement to have an i18n description.
 */
public interface EnumDescriptable {

    /**
   * returns the description of the object.
   * 
   * @return the description of the object
   */
    public abstract I18nMessage getDescription();

    /**
   * returns the ordinal of the enumeration.
   * 
   * @return the ordinal of the enumeration
   */
    public abstract int getOrdinal();
}
