package net.sf.clairv.index.resource;

import java.io.Serializable;

/**
 * @author qiuyin
 *
 */
public class ResourceMetaData implements Serializable {

    private static final long serialVersionUID = -6809862102583881753L;

    private float documentBoost;

    private String resourceName;

    private String resourceDescription;

    private String hitTextPattern;

    private String[] searchableFields;

    public float getDocumentBoost() {
        return documentBoost;
    }

    public void setDocumentBoost(float documentBoost) {
        this.documentBoost = documentBoost;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getHitTextPattern() {
        return hitTextPattern;
    }

    public void setHitTextPattern(String resultPattern) {
        this.hitTextPattern = resultPattern;
    }

    public String getResourceDescription() {
        return resourceDescription;
    }

    public void setResourceDescription(String resourceDescription) {
        this.resourceDescription = resourceDescription;
    }

    public String[] getSearchableFields() {
        return searchableFields;
    }

    public void setSearchableFields(String[] searchableFields) {
        this.searchableFields = searchableFields;
    }
}
