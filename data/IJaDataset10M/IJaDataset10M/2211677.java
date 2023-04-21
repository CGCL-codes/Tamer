package gr.demokritos.iit.LTagging.storage;

import gr.demokritos.iit.jinsect.documentModel.representations.DocumentNGramSymWinGraph;
import gr.demokritos.iit.jinsect.storage.INSECTFileDB;
import java.io.Serializable;
import java.lang.String;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

/**
 *
 * @author panagiotis
 * 
 * Contains the override methods of INSECTFileDB
 * 
 */
public class INSECTDBWithDir extends INSECTFileDB {

    public INSECTDBWithDir(String sPrefix, String sBaseDir) {
        super(sPrefix, sBaseDir);
    }

    public INSECTDBWithDir() {
        super();
    }

    public static final String ListCategoryName = "nameList";

    /**
     * returns a String table with all names for the category that asked
     * @param sObjectCategory the category name
     * @return a String table with all names 
     */
    @Override
    public String[] getObjectList(String sObjectCategory) {
        if ((super.getObjectList(sObjectCategory).length == 0) || super.getObjectList(sObjectCategory).length == 1) {
            String[] tableList = new String[0];
            return tableList;
        } else {
            ArrayList<String> nlist = (ArrayList<String>) loadObject(sObjectCategory, ListCategoryName);
            String[] tableList = new String[nlist.size()];
            return nlist.toArray(tableList);
        }
    }

    /**
     * save object with a given name
     * @param oObj the save object
     * @param sObjectName the object name 
     * @param sObjectCategory  the category name
     */
    @Override
    public void saveObject(Serializable oObj, String sObjectName, String sObjectCategory) {
        super.saveObject(oObj, sObjectName, sObjectCategory);
        if (existsObject(sObjectCategory, ListCategoryName)) {
            ArrayList<String> nlist = (ArrayList<String>) loadObject(sObjectCategory, ListCategoryName);
            nlist.add(sObjectName);
            super.saveObject(nlist, sObjectCategory, ListCategoryName);
        } else {
            ArrayList<String> nlist = new ArrayList<String>();
            nlist.add(sObjectName);
            super.saveObject(nlist, sObjectCategory, ListCategoryName);
        }
    }

    /**
     * deletes the object
     * @param sObjectName the object name
     * @param sObjectCategory  the category name
     */
    @Override
    public void deleteObject(String sObjectName, String sObjectCategory) {
        int index;
        super.deleteObject(sObjectName, sObjectCategory);
        ArrayList<String> nlist = (ArrayList<String>) loadObject(sObjectCategory, ListCategoryName);
        index = nlist.indexOf(sObjectName);
        nlist.remove(index);
        super.saveObject(nlist, sObjectCategory, ListCategoryName);
    }
}
