package com.jacum.cms.session.content;

import com.jacum.cms.security.ProtectedObject;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Map;

/**
 * An external representation of content item.
 * <p/>
 * The content item can exist within the scope of transaction, when
 * active ContentRepositorySession is present. When a repository session is closed
 * the ContentItem can still remain available in 'detached' state.
 * Unattached ContentItem retains its own attributes, although all operations
 * which require active ContentRepositorySession will throw an exception.
 * <p/>
 */
public interface ContentItem extends Serializable, ProtectedObject {

    /**
     * Unique item ID (UUID)
     *
     * @return a string, representing node ID
     */
    String getId();

    /**
     * Get full path to the item
     *
     * @return a string, slash-separated, representing node path
     */
    String getPath();

    /**
     * Item name
     *
     * @return a string, representing node name
     */
    String getName();

    /**
     * Retrieve default template ID for given content item
     *
     * @return a string with template ID
     */
    String getTemplateId();

    void dispose();

    /**
     * Node type
     * @return string, identifying node type
     */
    String getType();

    public void setStringProperty(String propertyName, String[] value);

    public void setStringProperty(String propertyName, String value);

    public void setStringProperty(int index, String propertyName, String value);

    public String getStringProperty(String propertyName);

    public String getStringProperty(int index, String propertyName);

    public void setBinaryProperty(String propertyName, InputStream value);

    public InputStream getBinaryProperty(String propertyName);

    void setBooleanProperty(String propertyName, Boolean value);

    Boolean getBooleanProperty(String propertyName);

    void setDateProperty(String propertyName, Calendar value);

    Calendar getDateProperty(String propertyName);

    void setLongProperty(String propertyName, Long value);

    Long getLongProperty(String propertyName);

    void setDoubleProperty(String propertyName, Double value);

    Double getDoubleProperty(String propertyName);

    void setReferencesProperty(String propertyName, ContentItem[] references);

    ContentItem[] getReferencesProperty(String propertyName);

    ContentItem[] getReferees();

    boolean isImmutable();

    boolean hasProperty(String name);

    Object getProperty(String name);

    Class getPropertyClass(String name);

    public Map<String, Class> getPropertyTypes();
}
