package com.tridion.extensions.dynamicdelivery.foundation.contentmodel.impl;

import org.simpleframework.xml.Element;
import com.tridion.extensions.dynamicdelivery.foundation.contentmodel.Schema;

public class BaseComponent extends BasePublishedItem {

    @Element(name = "schema")
    private Schema schema;

    private String resolvedUrl;

    public Schema getSchema() {
        return schema;
    }

    public void setSchema(Schema schema) {
        this.schema = schema;
    }

    public String getResolvedUrl() {
        return resolvedUrl;
    }

    public void setResolvedUrl(String resolvedUrl) {
        this.resolvedUrl = resolvedUrl;
    }
}
