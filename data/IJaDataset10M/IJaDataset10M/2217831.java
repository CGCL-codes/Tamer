package com.jacum.cms.source.jcr;

import com.jacum.cms.session.content.ContentItem;
import java.util.Map;
import java.util.Calendar;
import java.util.HashMap;
import java.io.InputStream;

/**
 * @author timur
 */
public class JcrContentItemProxy implements ContentItem {

    private String id;

    private String name;

    private String path;

    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Map<String, Class> getPropertyTypes() {
        return new HashMap<String, Class>();
    }

    public void dispose() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setStringProperty(String propertyName, String value) {
    }

    public void setStringProperty(int index, String propertyName, String value) {
    }

    public String getStringProperty(String propertyName) {
        return null;
    }

    public String getStringProperty(int index, String propertyName) {
        return null;
    }

    public void setBinaryProperty(String propertyName, InputStream value) {
    }

    public InputStream getBinaryProperty(String propertyName) {
        return null;
    }

    public void setBooleanProperty(String propertyName, Boolean value) {
    }

    public Boolean getBooleanProperty(String propertyName) {
        return null;
    }

    public void setDateProperty(String propertyName, Calendar value) {
    }

    public Calendar getDateProperty(String propertyName) {
        return null;
    }

    public void setLongProperty(String propertyName, Long value) {
    }

    public Long getLongProperty(String propertyName) {
        return null;
    }

    public void setDoubleProperty(String propertyName, Double value) {
    }

    public Double getDoubleProperty(String propertyName) {
        return null;
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
        return false;
    }

    public boolean hasProperty(String name) {
        return false;
    }

    public Object getProperty(String name) {
        return null;
    }

    public Class getPropertyClass(String name) {
        return null;
    }

    public String getTemplateId() {
        return null;
    }

    public void setStringProperty(String propertyName, String[] value) {
    }
}
