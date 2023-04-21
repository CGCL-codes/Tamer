package org.jxstar.service.util;

import java.util.Map;
import org.jxstar.service.BoException;
import org.jxstar.util.factory.FactoryUtil;
import org.jxstar.util.log.Log;

/**
 * 数据权限解析管理对象，主要管理缓存数据权限解析后的SQL，
 * 修改用户数据权限配置或角色数据权限配置时需要清除缓存中的数据权限解析SQL。
 * 
 * 取数据权限过滤语句的方法中添加了同步处理，只有权限值对象同步，构建过滤语句的方法没有同步，
 * 如果一个用户对同一个功能并发取数据权限时可能会重复构建过滤语句，但不影响使用效果，但不同步对性能帮助很大。
 *
 * @author TonyTan
 * @version 1.0, 2011-3-19
 */
public class SysDataManager {

    private static Log _log = Log.getInstance();

    private static SysDataManager _instance = new SysDataManager();

    private static Map<String, SysDataValue> _mpDataWhere = FactoryUtil.newMap();

    public static SysDataManager getInstance() {
        return _instance;
    }

    /**
	 * 取该用户指定功能的数据权限过滤语句。
	 * 
	 * @param userId -- 用户ID
	 * @param funId -- 功能ID
	 * @return String
	 */
    public String getDataWhere(String userId, String funId) {
        if (userId == null || userId.length() == 0 || funId == null || funId.length() == 0) {
            return "";
        }
        SysDataValue dataValue;
        synchronized (_mpDataWhere) {
            dataValue = _mpDataWhere.get(userId);
            if (dataValue == null) {
                dataValue = new SysDataValue(userId);
                _mpDataWhere.put(userId, dataValue);
            }
        }
        return dataValue.getDataWhere(funId);
    }

    /**
	 * 清除某个用户的数据权限SQL缓存，在修改用户数据权限配置或角色数据权限配置时需要用。
	 * @param userId -- 用户ID
	 */
    public void clearDataWhere(String userId) {
        _mpDataWhere.remove(userId);
    }

    /**
	 * 清除某个用户指定功能的数据权限SQL缓存
	 * @param userId -- 用户ID
	 * @param funId -- 功能ID
	 */
    public void clearDataWhere(String userId, String funId) {
        SysDataValue dataValue = _mpDataWhere.get(userId);
        if (dataValue != null) {
            dataValue.clearDataWhere(funId);
        }
    }

    /**
	 * 负责管理单个用户的所有功能的数据权限SQL。
	 * @author TonyTan
	 * @version 1.0, 2011-3-19
	 */
    private class SysDataValue {

        private String _userId;

        private Map<String, String> _mpWhere = FactoryUtil.newMap();

        public SysDataValue(String userId) {
            _userId = userId;
        }

        /**
		 * 取某一个用户的针对指定功能的数据权限SQL
		 * @param userId -- 用户ID
		 * @param funId -- 功能ID
		 * @return
		 */
        public String getDataWhere(String funId) {
            String where = _mpWhere.get(funId);
            if (where == null) {
                try {
                    where = SysDataUtil.getDataWhere(_userId, funId);
                } catch (BoException e) {
                    _log.showError(e);
                    where = "";
                }
                _mpWhere.put(funId, where);
            }
            return where;
        }

        /**
		 * 清除指定功能的数据权限SQL
		 * @param funId -- 功能ID
		 */
        public void clearDataWhere(String funId) {
            _mpWhere.remove(funId);
        }
    }
}
