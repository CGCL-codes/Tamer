package com.we4tech.openSearch.object;

/**
 * URL object for open search.
 * @author <a href="mailto:hasan@somewherein.net">nhm tanveer hossain khan (hasan)</a>
 */
public class OpenSearchUrl {

    private String type;

    private String method;

    private String template;

    private Integer indexOffset;

    private Integer pageOffset;

    public String getType() {
        return type;
    }

    public void setType(final String pType) {
        type = pType;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(final String pMethod) {
        method = pMethod;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(final String pTemplate) {
        template = pTemplate;
    }

    public Integer getIndexOffset() {
        return indexOffset;
    }

    public void setIndexOffset(final Integer pIndexOffset) {
        indexOffset = pIndexOffset;
    }

    public Integer getPageOffset() {
        return pageOffset;
    }

    public void setPageOffset(final Integer pPageOffset) {
        pageOffset = pPageOffset;
    }
}
