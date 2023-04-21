package com.skruk.elvis.admin;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Description of the Class
 *
 * @author     skruk
 * @created    1 grudzień 2003
 */
public class ResourceSchemaManager extends ConfigManager {

    /** Description of the Field */
    private static final ResourceSchemaManager RSM_INSTANCE = new ResourceSchemaManager();

    /** Description of the Field */
    private static String RSM_SCHEMA_FILE = null;

    /** Constructor for the ResourceSchemaManager object */
    protected ResourceSchemaManager() {
    }

    /**
	 * Gets the instance attribute of the RdfSchemaManager class
	 *
	 * @return    The instance value
	 */
    public static ConfigManager getInstance() {
        synchronized (ResourceSchemaManager.class) {
            if (RSM_SCHEMA_FILE == null) {
                if (com.skruk.elvis.beans.ContextKeeper.isContext()) {
                    RSM_SCHEMA_FILE = com.skruk.elvis.beans.ContextKeeper.getInstallDir() + "/xml/dtd/book.xsd";
                    RSM_ELVIS_NS = com.skruk.elvis.beans.ContextKeeper.getElvisNamespace();
                } else {
                    RSM_ELVIS_NS = "http://wbss.pg.gda.pl/";
                    RSM_SCHEMA_FILE = "/www/webapps/ELVIS/xml/dtd/book.xsd";
                }
            }
        }
        return RSM_INSTANCE;
    }

    /**
	 * Gets the version attribute of the RdfSchemaManager object
	 *
	 * @return                                            The version value
	 * @exception  org.xmlpull.v1.XmlPullParserException  Description of the Exception
	 * @exception  java.io.FileNotFoundException          Description of the Exception
	 * @exception  java.io.IOException                    Description of the Exception
	 */
    public String getVersion() throws org.xmlpull.v1.XmlPullParserException, java.io.FileNotFoundException, java.io.IOException {
        return super.getVersion(RSM_SCHEMA_FILE, "schema", "xs");
    }

    /**
	 * Description of the Method
	 *
	 * @return                            Description of the Return Value
	 * @exception  FileNotFoundException  Description of the Exception
	 * @exception  IOException            Description of the Exception
	 */
    public String loadFile() throws FileNotFoundException, IOException {
        return loadFile(RSM_SCHEMA_FILE);
    }

    /**
	 * Description of the Method
	 *
	 * @param  file                       Description of the Parameter
	 * @exception  FileNotFoundException  Description of the Exception
	 * @exception  IOException            Description of the Exception
	 */
    public void storeFile(String file) throws FileNotFoundException, IOException {
        storeFile(file, RSM_SCHEMA_FILE);
    }
}
