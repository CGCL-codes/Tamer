package com.googlecode.technorati4j.entity;

import java.util.ArrayList;
import java.util.List;
import org.jdom.Element;
import com.googlecode.technorati4j.util.StringUtils;

/**
 * Response entity for toptags API.
 * 
 * @author Otávio Scherer Garcia
 * @version $Revision$
 * 
 */
public final class TopTagsResponse implements Response {

    /**
     * Value of limit parameter.
     */
    private Integer limit;

    /**
     * List of items found.
     */
    private List<TopTagsResponseItem> items;

    @SuppressWarnings("unchecked")
    public TopTagsResponse(Element element) {
        Element result = element.getChild("result");
        limit = StringUtils.toInteger(result.getChildText("limit"));
        items = new ArrayList<TopTagsResponseItem>();
        List<Element> _items = element.getChildren("item");
        for (Element item : _items) {
            items.add(new TopTagsResponseItem(item));
        }
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public List<TopTagsResponseItem> getItems() {
        return items;
    }

    public void setItems(List<TopTagsResponseItem> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        final StringBuilder str = new StringBuilder();
        str.append("limit=");
        str.append(limit);
        str.append("&items=");
        str.append(items);
        return str.toString();
    }
}
