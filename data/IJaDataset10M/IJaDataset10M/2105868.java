package org.amlfilter.parsers;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.amlfilter.service.GenericService;
import org.amlfilter.service.LoggerService;
import org.amlfilter.util.AlgorithmUtils;
import org.amlfilter.util.DataFileUtils;
import org.amlfilter.util.FileUtils;
import org.amlfilter.util.GeneralConstants;
import org.springframework.beans.factory.InitializingBean;

/**
 * Normalizes the type of an entity. This version only deals with several possible values, 
 * so it does not require an external file.
 * 
 * @author Harish Seshadri
 * @version $Id$
 *
 */
public class EntityTypeProcessor extends GenericService implements InitializingBean {

    private Map<String, String> mEntityTypeMap = new HashMap<String, String>();

    public EntityTypeProcessor() {
        mEntityTypeMap.put("P", GeneralConstants.PERSON);
        mEntityTypeMap.put("PERSON", GeneralConstants.PERSON);
        mEntityTypeMap.put("PERSONA", GeneralConstants.PERSON);
        mEntityTypeMap.put("INDIVIDUAL", GeneralConstants.PERSON);
        mEntityTypeMap.put("E", GeneralConstants.ENTITY);
        mEntityTypeMap.put("ENTITY", GeneralConstants.ENTITY);
        mEntityTypeMap.put("VESSEL", GeneralConstants.ENTITY);
        mEntityTypeMap.put("COMPANY", GeneralConstants.ENTITY);
        mEntityTypeMap.put("ENTIDAD", GeneralConstants.ENTITY);
        mEntityTypeMap.put("ORGANIZATION", GeneralConstants.ENTITY);
    }

    /**
	 * Get the EntityType map
	 * @return The EntityType map
	 */
    public Map<String, String> getEntityTypeMap() {
        return mEntityTypeMap;
    }

    /**
	 * Set the EntityType map
	 * @param pEntityTypeMap The EntityType map
	 */
    public void setEntityTypeMap(Map<String, String> pEntityTypeMap) {
        mEntityTypeMap = pEntityTypeMap;
    }

    /**
	 * Get the adequate EntityType Code
	 * @return The adequate EntityType code
	 */
    public String getEntityTypeCode(String pEntityType) {
        String cleanedEntityType = AlgorithmUtils.cleanString(pEntityType);
        String retVal = getEntityTypeMap().get(cleanedEntityType);
        if (null == retVal) {
            retVal = GeneralConstants.UNDEFINED_TOKEN;
        }
        return retVal;
    }

    public static void main(String[] args) throws Exception {
    }
}
