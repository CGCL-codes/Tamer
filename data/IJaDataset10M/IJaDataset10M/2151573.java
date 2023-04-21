package org.gvsig.i18n.utils;

import java.util.HashMap;

/**
 * Convenience class to manage the attributes of the project tag from the config.xml
 * file.
 * 
 * 
 * @author cesar
 *
 */
public class Project {

    public String dir;

    public String basename;

    /**
	 * The directory which stores the property files of the project.
	 */
    public String propertyDir;

    /**
	 * Source of the keys: whether they are loaded from the property files of
	 * the project or they are searched inside the sources.
	 * <ul><li>sourceKeys="sources": The keys are searched inside the Java source
	 * files and the config.xml files from the extensions.</li>
	 * <li>sourceKeys="properties": The keys are loaded from the property files
	 * of each project.</li></ul> 
	 */
    public String sourceKeys;

    /**
	 * Stores the associated dictionaries. Each value of the
	 * HashMap is a Properties object, containing the translations for each
	 * language.
	 * 
	 * <p>Example:</p>
	 * Properties dictionary = (Properties)dictionaries.get("es");
	 * 
	 */
    public HashMap dictionaries;

    /**
	 * Stores the subdirectories containing sources. When searching for keys in
	 * the source files, only these subdirectories will be searched.
	 */
    public String[] srcDirs;
}
