package org.xebra.scp.db.sql;

import java.util.HashMap;
import java.util.Map;

/**
 * This class maps the statements created in the ibatis resource files to
 * their object types and purposes.
 * 
 * @author Rafael Chargel
 * @version $Revision: 1.2 $
 */
public class XMLMethodMap {

    private static final XMLMethodMap MAP = new XMLMethodMap();

    public static final String LIST_ALL = "list_all";

    public static final String LIST_BY_QUERY = "load_by_template";

    public static final String GET_BY_UID = "get_by_uid";

    public static final String GET_BY_CHILD_UID = "get_by_child_uid";

    public static final String LIST_BY_PARENT_UID = "get_by_parent_uid";

    public static final String INSERT = "insert";

    public static final String TEST = "test";

    public static final String PERSIST = "persist";

    public static final String DELETE = "delete";

    public static final String DELETE_CHILD = "delete_child";

    public static final String DELETE_GRAND_CHILD = "delete_grand_child";

    private Map<String, String> methodMap;

    /**
	 * Constructor for the XMLMethodMap class.
	 */
    private XMLMethodMap() {
        loadMaps();
    }

    /**
	 * Gets the singleton instance of the XMLMethodMap.
	 * 
	 * @return Returns the instance of the XML method map.
	 *
	 * @throws RuntimeException
	 */
    public static synchronized XMLMethodMap getInstance() throws RuntimeException {
        return MAP;
    }

    /**
	 * Gets the correct ibatis method ID for the object class
	 * and the method type.
	 * 
	 * @param objectType The class of the object being loaded.
	 * @param methodType The type of method being called 
	 *                   (e.g. XMLMethodMap.LIST_ALL)
	 *                   
	 * @return Returns the correct signature of the method ID.
	 */
    public String getXMLMethodId(Class objectType, String methodType) {
        return this.methodMap.get(objectType.getSimpleName().toLowerCase() + "_" + methodType);
    }

    private void loadMaps() {
        this.methodMap = new HashMap<String, String>();
        this.methodMap.put("study_list_all", "allStudies");
        this.methodMap.put("study_load_by_template", "studySearchByTemplate");
        this.methodMap.put("study_get_by_uid", "studySearchByUID");
        this.methodMap.put("study_get_by_child_uid", "studySearchBySeriesUID");
        this.methodMap.put("study_get_by_parent_uid", "studySearchByPatientMRN");
        this.methodMap.put("study_insert", "insertStudy");
        this.methodMap.put("study_test", "testStudy");
        this.methodMap.put("study_persist", "persistStudy");
        this.methodMap.put("study_delete", "deleteStudy");
        this.methodMap.put("study_delete_child", "deleteSeriesByStudy");
        this.methodMap.put("study_delete_grand_child", "deleteInstanceByStudy");
        this.methodMap.put("series_list_all", "allSeries");
        this.methodMap.put("series_load_by_template", "seriesSearchByTemplate");
        this.methodMap.put("series_get_by_uid", "seriesSearchByUID");
        this.methodMap.put("series_get_by_child_uid", "seriesSearchByInstanceUID");
        this.methodMap.put("series_get_by_parent_uid", "seriesSearchByStudyUID");
        this.methodMap.put("series_insert", "insertSeries");
        this.methodMap.put("series_test", "testSeries");
        this.methodMap.put("series_persist", "persistSeries");
        this.methodMap.put("series_delete", "deleteSeries");
        this.methodMap.put("series_delete_child", "deleteInstanceBySeries");
        this.methodMap.put("instance_list_all", "allInstances");
        this.methodMap.put("instance_load_by_template", "instanceSearchByTemplate");
        this.methodMap.put("instance_get_by_uid", "instanceSearchByUID");
        this.methodMap.put("instance_get_by_parent_uid", "instanceSearchBySeriesUID");
        this.methodMap.put("instance_insert", "insertInstance");
        this.methodMap.put("instance_test", "testInstances");
        this.methodMap.put("instance_persist", "persistInstance");
        this.methodMap.put("instance_delete", "deleteInstance");
    }
}
