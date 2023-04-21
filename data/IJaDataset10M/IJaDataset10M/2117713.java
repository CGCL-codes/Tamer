package com.kwoksys.biz.system.dao;

import com.kwoksys.framework.configs.SystemConfigNames;

/**
 * SystemQueries.
 */
public class SystemQueries {

    public static String selectSystemValues() {
        return "select now() as sysdate, config_value as cache_key from system_config where config_key='" + SystemConfigNames.SYSTEM_CACHE_KEY + "'";
    }

    /**
     * Returns query to select cache keys that need to be flushed from system_cache table
     * @return
     */
    public static String selectFlushSystemCaches() {
        return "select cache_key from system_cache where cache_time >= ?";
    }

    /**
     * Makes sure there is no cache key with a cache_time in the future.
     * @return
     */
    public static String validateSystemCaches() {
        return "{call sp_system_cache_validate(?)}";
    }

    public static String updateSystemCache() {
        return "{call sp_system_cache_add(?, ?)}";
    }

    /**
     * This is for getting all system configuration.
     *
     * @return ..
     */
    public static String selectSystemConfig() {
        return "select config_key, config_value from system_config";
    }

    public static String selectObjectMapCount(String objectTypeIds, String linkedObjectTypeIds) {
        return "select count(object_id) as obj_count from object_map " + "where object_type_id in (" + objectTypeIds + ") " + "and linked_object_id=? " + "and linked_object_type_id in (" + linkedObjectTypeIds + ") ";
    }

    public static String selectLinkedObjectMapCount(String linkedObjectTypeIds) {
        return "select count(linked_object_id) as obj_count from object_map " + "where linked_object_type_id in (" + linkedObjectTypeIds + ") " + "and object_id=? " + "and object_type_id = ?";
    }

    /**
     * Add object mapping.
     */
    public static String insertObjectMapQuery() {
        return "{call sp_object_map_add(?,?,?,?,?,?)}";
    }

    /**
     * Remove object mapping.
     */
    public static String deleteObjectMapQuery() {
        return "{call sp_object_map_delete(?,?,?,?)}";
    }
}
