package com.aelitis.azureus.ui.skin;

import java.util.ResourceBundle;

/**
 * Interface for reading Skin properties (might be better)
 * 
 * @author TuxPaper
 * @created Jun 26, 2006
 *
 */
public interface SkinProperties {

    /**
	 * Add a property key/value pair to the list
	 * 
	 * @param name Name of Property
	 * @param value Value of Property
	 */
    void addProperty(String name, String value);

    /**
	 * Retrieve a property's int value
	 * 
	 * @param name Name of property
	 * @param def Default value if property not found
	 * @return value
	 */
    int getIntValue(String name, int def);

    /**
	 * Retrieve a string value
	 * 
	 * @param name Name of property
	 * @return the String value, or null if not found
	 */
    String getStringValue(String name);

    String getStringValue(String name, String def);

    String[] getStringArray(String name);

    String getStringValue(String name, String[] params);

    String getStringValue(String name, String[] params, String def);

    String[] getStringArray(String name, String[] params);

    int[] getColorValue(String name);

    boolean getBooleanValue(String name, boolean def);

    /**
	 * 
	 *
	 * @since 3.1.1.1
	 */
    void clearCache();

    /**
	 * @param name
	 * @return
	 *
	 * @since 3.1.1.1
	 */
    boolean hasKey(String name);

    /**
	 * @param name
	 * @return
	 *
	 * @since 3.1.1.1
	 */
    String getReferenceID(String name);

    /**
	 * @param subBundle
	 * @param skinPath TODO
	 *
	 * @since 4.0.0.5
	 */
    void addResourceBundle(ResourceBundle subBundle, String skinPath);

    /**
	 * @param subBundle
	 * @param skinPath
	 * @param loader
	 * @since 4315
	 */
    void addResourceBundle(ResourceBundle subBundle, String skinPath, ClassLoader loader);

    /**
	 * @return
	 *
	 * @since 4.0.0.5
	 */
    ClassLoader getClassLoader();
}
