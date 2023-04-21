package com.tridion.extensions.dynamicdelivery.foundation.contentmodel;

/**
 * Interface for Keyword item
 * 
 * @author Quirijn Slings
 * 
 */
public interface Keyword extends Item {

    public String getPath();

    public void setPath(String path);

    public String getTaxonomyId();

    public void setTaxonomyId(String taxonomyId);
}
