package com.tridion.extensions.dynamicdelivery.foundation.contentmodel.impl;

import org.simpleframework.xml.Attribute;
import com.tridion.extensions.dynamicdelivery.foundation.contentmodel.Keyword;

public class KeywordImpl extends BaseItem implements Keyword {

    @Attribute(name = "taxonomyId")
    private String taxonomyId;

    @Attribute(name = "path")
    private String path;

    @Override
    public String getTaxonomyId() {
        return taxonomyId;
    }

    @Override
    public void setTaxonomyId(String taxonomyId) {
        this.taxonomyId = taxonomyId;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public void setPath(String path) {
        this.path = path;
    }
}
