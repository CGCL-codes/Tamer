package com.byjyate.rssdreamwork;

import org.w3c.dom.*;

public class ItemFilterTransformer extends RssItemTransformer {

    private String keyword;

    public ItemFilterTransformer(String keyword) {
        if (keyword == null || keyword.isEmpty()) throw new NullPointerException();
        this.keyword = keyword.toLowerCase();
    }

    @Override
    protected void processItem(Node item) {
        Node property = RssTransformerHelper.FindProperty(item, "title");
        if (property.getTextContent().toLowerCase().contains(keyword)) item.getParentNode().removeChild(item);
    }
}
