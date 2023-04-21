package com.jacum.cms.session.content;

import java.io.InputStream;
import java.util.*;

/**
 * Fail-safe implementation of ContentItem
 */
public class DummyContentItem implements ContentItem {

    private String templateId;

    private String type;

    private static final String DUMMY_NAME = "dummy";

    private static final String DUMMY_VALUE = "?";

    public DummyContentItem() {
    }

    public DummyContentItem(String templateId, String type) {
        this.templateId = templateId;
        this.type = type;
    }

    public String getId() {
        return DUMMY_NAME;
    }

    public String getPath() {
        return "/" + DUMMY_NAME;
    }

    public String getName() {
        return DUMMY_NAME;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void dispose() {
    }

    public String getType() {
        return type;
    }

    public void setStringProperty(String propertyName, String value) {
    }

    public void setStringProperty(int index, String propertyName, String value) {
    }

    public String getStringProperty(String propertyName) {
        return DUMMY_VALUE;
    }

    public String getStringProperty(int index, String propertyName) {
        return DUMMY_VALUE;
    }

    public void setBinaryProperty(String propertyName, InputStream value) {
    }

    public InputStream getBinaryProperty(String propertyName) {
        return null;
    }

    public void setBooleanProperty(String propertyName, Boolean value) {
    }

    public Boolean getBooleanProperty(String propertyName) {
        return Boolean.FALSE;
    }

    public void setDateProperty(String propertyName, Calendar value) {
    }

    public Calendar getDateProperty(String propertyName) {
        return new GregorianCalendar();
    }

    public void setLongProperty(String propertyName, Long value) {
    }

    public Long getLongProperty(String propertyName) {
        return (long) 0;
    }

    public void setDoubleProperty(String propertyName, Double value) {
    }

    public Double getDoubleProperty(String propertyName) {
        return 0.0;
    }

    public void setReferencesProperty(String propertyName, ContentItem[] references) {
    }

    public ContentItem[] getReferencesProperty(String propertyName) {
        return new ContentItem[0];
    }

    public ContentItem[] getReferees() {
        return new ContentItem[0];
    }

    public boolean isImmutable() {
        return true;
    }

    public boolean hasProperty(String name) {
        return true;
    }

    public Class getPropertyClass(String name) {
        return String.class;
    }

    public Map<String, Class> getPropertyTypes() {
        return new HashMap<String, Class>();
    }

    public Object getProperty(String name) {
        return "";
    }

    public void setStringProperty(String propertyName, String[] value) {
    }
}
