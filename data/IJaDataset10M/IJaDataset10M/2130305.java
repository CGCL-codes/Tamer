package org.adore.didl.content;

/**
 * Helper class that represents an element of the Premis namespace.
 * 
 * @author Kjell Lotigiers <kjell.lotigiers@ugent.be>
 * @author Patrick Hochstenbach <patrick.hochstenbach@ugent.be>
 * @author Xiaoming Liu <liu_x@lanl.gov>
 */
public class Premis {

    private String objectIdentifierType;

    private String objectIdentifierValue;

    private String objectCategory;

    private String formatName;

    private String formatKey;

    private String creatingApplication;

    private long size;

    private String storage;

    private int compositionLevel;

    /** Creates a new Premis instance */
    public Premis() {
        size = -1;
        compositionLevel = -1;
    }

    /**
     * Sets the objectIdentifier element of the premis object.
     * @param type The type of the object identifier
     * @param value The value of the object identifier
     */
    public void setObjectIdentifier(String type, String value) {
        objectIdentifierType = type;
        objectIdentifierValue = value;
    }

    /**
     * Gets the value of the objectIdentifierType element of the Premis object
     * @return The value of the objectIdentifierType element
     */
    public String getObjectIdentifierType() {
        return objectIdentifierType;
    }

    /**
     * Gets the value of the objectIdentifierValue element of the Premis object
     * @return The value of the objectIdentifierValue element
     */
    public String getObjectIdentifierValue() {
        return objectIdentifierValue;
    }

    /**
     * Sets the category element of the premis object
     * @param categorie The value of the category element
     */
    public void setObjectCategory(String categorie) {
        this.objectCategory = categorie;
    }

    /**
     * Gets the value of the objectCategory element of the Premis object
     * @return The value of the objectCategory element
     */
    public String getObjectCategory() {
        return objectCategory;
    }

    /**
     * Sets the format element of the Premis object
     * @param name The value of the formatRegistryName element
     * @param key The value of the formatRegistryKey element
     */
    public void setFormat(String name, String key) {
        formatName = name;
        formatKey = key;
    }

    /**
     * Gets the value of the formatRegistryName element of the Premis object
     * @return The value of the formatRegistryName element
     */
    public String getFormatName() {
        return formatName;
    }

    /**
     * Gets the value of the formatRegistryKey element of the Premis object
     * @return The value of the formatRegistryKey element
     */
    public String getFormatKey() {
        return formatKey;
    }

    /**
     * Sets the creatingApplication element of the premis object
     * @param createApp The value of the dateCreatedByApplication element
     */
    public void setCreatingApplication(String createApp) {
        creatingApplication = createApp;
    }

    /**
     * Gets the value of the dateCreatedByApplication element of the Premis object
     * @return The value of the dateCreatedByApplication element
     */
    public String getCreatingApplication() {
        return creatingApplication;
    }

    /**
     * Sets the size element of the Premis object
     * @param size The value of the size element
     */
    public void setSize(long size) {
        this.size = size;
    }

    /**
     * Gets the value of the size element of the Premis object
     * @return The value of the size element
     */
    public long getSize() {
        return size;
    }

    /** 
     * Sets the Composition Level element of the Premis object 
     * @param level The value of the compositionLevel element 
     */
    public void setCompositionLevel(int level) {
        this.compositionLevel = level;
    }

    /**
     * Gets the Composition Level element of the Premis object 
     * @return The value of the compositionLevel element
     */
    public int getCompositionLevel() {
        return this.compositionLevel;
    }

    /**
     * Sets the Storage element of the Premis object 
     * @param storage The value of the Storage element
     */
    public void setStorageMedium(String storage) {
        this.storage = storage;
    }

    /**
     * Gets the Storage element of the Premis object 
     * @return The value of the Storage element
     */
    public String getStorageMedium() {
        return this.storage;
    }
}
