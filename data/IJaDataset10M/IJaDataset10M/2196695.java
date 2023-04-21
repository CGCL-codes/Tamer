package cz.fi.muni.xkremser.editor.client.metadata;

import cz.fi.muni.xkremser.editor.client.mods.ModsTypeClient;

/**
 * The Interface RelatedItemHolder.
 */
public interface RelatedItemHolder {

    /**
     * Gets the mods.
     * 
     * @return the mods
     */
    ModsTypeClient getMods();

    /**
     * Gets the related item attribute holder.
     * 
     * @return the related item attribute holder
     */
    ListOfListOfSimpleValuesHolder getRelatedItemAttributeHolder();
}
