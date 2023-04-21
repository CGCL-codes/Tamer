package org.jxstar.service.studio;

import java.util.Map;
import org.jxstar.dao.DaoParam;
import org.jxstar.service.BusinessObject;
import org.jxstar.util.DateUtil;
import org.jxstar.util.key.KeyCreator;

/**
 * 批量添加用户数据权限处理类。
 *
 * @author TonyTan
 * @version 1.0, 2012-3-2
 */
public class UserDataBO extends BusinessObject {

    private static final long serialVersionUID = 1L;

    private String _selectsql = "";

    /**
	 * 批量添加数据权限数据
	 * @param dataIds -- 选择的数据ID
	 * @param userIds -- 选择的用户ID
	 * @param typeId -- 数据权限类型
	 * @param curUserId -- 当前用户ID
	 * @return
	 */
    public String addRightData(String[] dataIds, String userIds, String typeId, String curUserId) {
        if (dataIds == null || dataIds.length == 0) {
            setMessage("没有选择需要导入的数据！");
            return _returnFaild;
        }
        if (userIds == null || userIds.length() == 0 || typeId == null || typeId.length() == 0) {
            setMessage("选择的用户ID为空，或者数据类别ID为空！");
            return _returnFaild;
        }
        _selectsql = getSelectSql(typeId);
        if (_selectsql.length() == 0) {
            setMessage("构建查询数据的SQL失败！");
            return _returnFaild;
        }
        String[] selUserIds = userIds.split(";");
        for (int i = 0, n = selUserIds.length; i < n; i++) {
            String selUserId = selUserIds[i];
            _log.showDebug("...............insert into userid:" + selUserId);
            for (int j = 0, m = dataIds.length; j < m; j++) {
                boolean ret = insertData(dataIds[j], selUserId, typeId, curUserId);
                if (!ret) {
                    setMessage("新增数据权限记录失败！");
                    return _returnFaild;
                }
            }
        }
        return _returnSuccess;
    }

    /**
	 * 新增一条数据权限记录
	 * @param dataId
	 * @param selUserId
	 * @param typeId
	 * @param curUserId
	 * @return
	 */
    private boolean insertData(String dataId, String selUserId, String typeId, String curUserId) {
        DaoParam param = _dao.createParam(_selectsql);
        param.addStringValue(dataId);
        Map<String, String> mpData = _dao.queryMap(param);
        if (mpData.isEmpty()) {
            _log.showWarn("............select data is null!");
            return false;
        }
        String dataid = mpData.get("dataid");
        String dataname = mpData.get("dataname");
        String isql = "insert into sys_user_data(user_data_id, user_id, dtype_id, " + "dtype_data, has_sub, display, add_userid, add_date) " + "values(?, ?, ?, ?, ?, ?, ?, ?)";
        String keyId = KeyCreator.getInstance().createKey("sys_user_data");
        DaoParam iparam = _dao.createParam(isql);
        iparam.addStringValue(keyId);
        iparam.addStringValue(selUserId);
        iparam.addStringValue(typeId);
        iparam.addStringValue(dataid);
        iparam.addStringValue("1");
        iparam.addStringValue(dataname);
        iparam.addStringValue(curUserId);
        iparam.addDateValue(DateUtil.getTodaySec());
        return _dao.update(iparam);
    }

    /**
	 * 构建数据查询语句
	 * @param typeId
	 * @return
	 */
    private String getSelectSql(String typeId) {
        String ret = "";
        Map<String, String> mpType = getType(typeId);
        if (mpType.isEmpty()) {
            _log.showWarn("............type data is null!");
            return ret;
        }
        String funId = mpType.get("funid");
        Map<String, String> mpFun = getFun(funId);
        if (mpFun.isEmpty()) {
            _log.showWarn("............fun data is null!");
            return ret;
        }
        String pk = mpFun.get("pk_col");
        String table = mpFun.get("table_name");
        String dataId = mpType.get("fun_field");
        String dataName = mpType.get("fun_vfield");
        StringBuilder sb = new StringBuilder();
        sb.append("select ").append(dataId).append(" as dataid, ");
        sb.append(dataName).append(" as dataname from ").append(table);
        sb.append(" where ").append(pk).append(" = ?");
        _log.showDebug("............select sql=" + sb.toString());
        return sb.toString();
    }

    /**
	 * 取功能表名与主键
	 * @param funId
	 * @return
	 */
    private Map<String, String> getFun(String funId) {
        String sql = "select pk_col, table_name from fun_base where fun_id = ?";
        DaoParam param = _dao.createParam(sql);
        param.addStringValue(funId);
        return _dao.queryMap(param);
    }

    /**
	 * 取数据类别信息
	 * @param typeId
	 * @return
	 */
    private Map<String, String> getType(String typeId) {
        String sql = "select dtype_name, dtype_field, funid, fun_field, fun_vfield from sys_datatype where dtype_id = ?";
        DaoParam param = _dao.createParam(sql);
        param.addStringValue(typeId);
        return _dao.queryMap(param);
    }
}
