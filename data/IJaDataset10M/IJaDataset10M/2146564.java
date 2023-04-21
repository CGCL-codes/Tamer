package org.mushroomdb.util;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Concentra los nombres de las propiedades globales del sistema.
 * 
 * @author mchiodi
 *
 */
public class PropertiesHelper {

    private static Set propertiesNames;

    public static final String DATA_FILE = "mushroomdb.data";

    public static final String PAGE_SIZE = "mushroomdb.page.size";

    public static final String POOL_SIZE = "mushroomdb.filemanager.pool.size";

    public static final String REPLACEMENT_POLICY = "mushroomdb.filemanager.pool.replacementpolicy";

    public static final String BLOCK_SIZE = "mushroomdb.filemanager.block.size";

    public static final String INTERCEPTOR = "mushroomdb.interceptor";

    public static final String SERVICE = "mushroomdb.service";

    public static final String PARSER = "mushroomdb.parser";

    public static final String ENCODER = "mushroomdb.encoder";

    public static final String OPTIMIZER = "mushroomdb.optimizer";

    /**
	 * Carga las propiedades.
	 */
    static {
        PropertiesHelper.propertiesNames = new LinkedHashSet();
        PropertiesHelper.propertiesNames.add(DATA_FILE);
        PropertiesHelper.propertiesNames.add(PAGE_SIZE);
        PropertiesHelper.propertiesNames.add(POOL_SIZE);
        PropertiesHelper.propertiesNames.add(REPLACEMENT_POLICY);
        PropertiesHelper.propertiesNames.add(BLOCK_SIZE);
        PropertiesHelper.propertiesNames.add(INTERCEPTOR);
        PropertiesHelper.propertiesNames.add(SERVICE);
        PropertiesHelper.propertiesNames.add(PARSER);
        PropertiesHelper.propertiesNames.add(ENCODER);
    }

    /**
	 * Devuelve un iterador con todas las propiedades.
	 * @return
	 */
    public static Iterator getProperties() {
        return PropertiesHelper.propertiesNames.iterator();
    }
}
