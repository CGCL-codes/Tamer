package org.jxstar.service.query;

import java.util.List;
import java.util.Map;
import org.jxstar.dao.DaoParam;
import org.jxstar.service.BusinessObject;
import org.jxstar.service.util.SysUserUtil;
import org.jxstar.util.config.SystemVar;
import org.jxstar.util.factory.FactoryUtil;
import org.jxstar.util.resource.JsMessage;

/**
 * 创建功能模块。
 *
 * @author TonyTan
 * @version 1.0, 2010-3-7
 */
public class MenuQuery extends BusinessObject {

    private static final long serialVersionUID = 1L;

    /**
	 * 返回用户菜单
	 * @param userId -- 用户ID
	 * @return 
	 */
    public String createMainMenu(String userId) {
        if (userId == null || userId.length() == 0) {
            setMessage(JsMessage.getValue("menuquery.useridnull"));
            return _returnFaild;
        }
        String oneWhere = "";
        Map<String, List<Map<String, String>>> moduleFun = FactoryUtil.newMap();
        Map<String, StringBuilder> towModule = FactoryUtil.newMap();
        boolean isAdmin = SysUserUtil.isAdmin(userId);
        _log.showDebug("-----------current user role is admin=" + isAdmin);
        if (!isAdmin) {
            List<Map<String, String>> lsFun = queryAllFun(userId);
            if (lsFun.isEmpty()) {
                setMessage(JsMessage.getValue("menuquery.nofunid"), userId);
                return _returnFaild;
            }
            StringBuilder sbOne = new StringBuilder();
            String oldTwoId = "", oldOneId = "";
            for (int i = 0, n = lsFun.size(); i < n; i++) {
                Map<String, String> mpFun = lsFun.get(i);
                String twoId = mpFun.get("module_id");
                String oneId = twoId.substring(0, 4);
                if (!oldOneId.equals(oneId)) {
                    oldOneId = oneId;
                    sbOne.append("'").append(oneId).append("',");
                    StringBuilder sbTwo = new StringBuilder();
                    towModule.put(oneId, sbTwo);
                }
                if (!oldTwoId.equals(twoId)) {
                    oldTwoId = twoId;
                    towModule.get(oneId).append("'").append(twoId).append("',");
                    List<Map<String, String>> tmpFun = FactoryUtil.newList();
                    moduleFun.put(twoId, tmpFun);
                }
                moduleFun.get(twoId).add(mpFun);
            }
            oneWhere = sbOne.substring(0, sbOne.length() - 1);
            oneWhere = "module_id in (" + oneWhere + ")";
        }
        _log.showDebug("-----------one module id=" + oneWhere);
        List<Map<String, String>> lsModule = queryModule(oneWhere);
        if (lsModule.isEmpty()) {
            setMessage(JsMessage.getValue("menuquery.nomodule"), userId);
            return _returnFaild;
        }
        StringBuilder sbJson = new StringBuilder();
        for (int i = 0, n = lsModule.size(); i < n; i++) {
            Map<String, String> mpModule = lsModule.get(i);
            String moduleId = mpModule.get("module_id");
            String moduleName = mpModule.get("module_name");
            String isExpanded = mpModule.get("is_expanded");
            isExpanded = isExpanded.equals("1") ? "true" : "false";
            String twoWhere = null;
            if (!isAdmin) {
                StringBuilder sbTwo = towModule.get(moduleId);
                twoWhere = sbTwo.substring(0, sbTwo.length() - 1);
                twoWhere = "module_id in (" + twoWhere + ")";
            } else {
                twoWhere = "module_id like '" + moduleId + "%'";
            }
            _log.showDebug("-----------two module id=" + twoWhere);
            List<Map<String, String>> lsTwoModule = queryTwoModule(twoWhere);
            if (lsTwoModule.isEmpty()) continue;
            sbJson.append("{id:'" + moduleId + "', ");
            sbJson.append("text:'" + moduleName + "', ");
            sbJson.append("leaf:false, cls:'one-menu', expanded:" + isExpanded + ", ");
            sbJson.append("children: ");
            sbJson.append(createTwoMenu(lsTwoModule, moduleFun));
            sbJson.append("},");
        }
        String json = "[" + sbJson.substring(0, sbJson.length() - 1) + "]";
        setReturnData(json);
        return _returnSuccess;
    }

    /**
	 * 构建二级模块的菜单
	 * @param lsModule -- 二级模块信息
	 * @param moduleFun -- 二级模块ID与对应的功能信息
	 * @return 返回JSON格式：[{module_id:'', text:'', menu:new Ext.menu.Menu}, {}...]
	 */
    private String createTwoMenu(List<Map<String, String>> lsModule, Map<String, List<Map<String, String>>> moduleFun) {
        String retJson = "[]";
        if (lsModule == null || lsModule.isEmpty()) return retJson;
        String verType = SystemVar.getValue("sys.version.type", "SE");
        StringBuilder sbJson = new StringBuilder();
        for (int i = 0, n = lsModule.size(); i < n; i++) {
            Map<String, String> mpModule = lsModule.get(i);
            String moduleId = mpModule.get("module_id");
            if (verType.equals("SE")) {
                if (moduleId.equals("10100003") || moduleId.equals("10100004")) {
                    continue;
                }
            }
            String moduleName = mpModule.get("module_name");
            String isExpanded = mpModule.get("is_expanded");
            isExpanded = isExpanded.equals("1") ? "true" : "false";
            List<Map<String, String>> lsFun = null;
            if (moduleFun.isEmpty()) {
                lsFun = queryFun(moduleId);
            } else {
                lsFun = moduleFun.get(moduleId);
            }
            sbJson.append("{id:'" + moduleId + "', ");
            sbJson.append("text:'" + moduleName + "', ");
            sbJson.append("leaf:false, cls:'two-menu', expanded:" + isExpanded + ", ");
            sbJson.append("children: ");
            if (lsFun.isEmpty()) {
                sbJson.append("[]");
            } else {
                StringBuilder sbFun = new StringBuilder();
                for (int j = 0, m = lsFun.size(); j < m; j++) {
                    Map<String, String> mpFun = lsFun.get(j);
                    sbFun.append("{id:'" + mpFun.get("fun_id") + "', ");
                    sbFun.append("text:'" + mpFun.get("fun_name") + "',");
                    sbFun.append("leaf:true, cls:'three-menu' },");
                }
                String sfun = "[" + sbFun.substring(0, sbFun.length() - 1) + "]";
                sbJson.append(sfun);
            }
            sbJson.append("},");
        }
        if (sbJson.length() == 0) return retJson;
        retJson = "[" + sbJson.substring(0, sbJson.length() - 1) + "]";
        return retJson;
    }

    /**
	 * 取当前用户有权限的所有功能信息
	 * @param userId
	 * @return
	 */
    private List<Map<String, String>> queryAllFun(String userId) {
        StringBuilder sbsql = new StringBuilder();
        sbsql.append("select fun_id, fun_name, module_id from fun_base ");
        sbsql.append("where reg_type in ('main', 'treemain') and fun_id in ");
        sbsql.append("(select distinct sys_role_fun.fun_id from sys_user_role, sys_role_fun ");
        sbsql.append("where sys_user_role.role_id = sys_role_fun.role_id ");
        sbsql.append("and sys_user_role.user_id = ? ) ");
        sbsql.append("order by module_id, fun_index");
        DaoParam param = _dao.createParam(sbsql.toString());
        param.addStringValue(userId);
        return _dao.query(param);
    }

    /**
	 * 取二级模块对应的功能ID
	 * @param moduleId -- 二级模块ID
	 * @return List
	 */
    private List<Map<String, String>> queryFun(String moduleId) {
        StringBuilder sbsql = new StringBuilder();
        sbsql.append("select fun_id, fun_name from fun_base where module_id = ? and reg_type in ('main', 'treemain') order by fun_index");
        DaoParam param = _dao.createParam(sbsql.toString());
        param.addStringValue(moduleId);
        return _dao.query(param);
    }

    /**
	 * 取二级模块信息
	 * @param twoWhere -- 二级模块ID过滤语句
	 * @return List
	 */
    private List<Map<String, String>> queryTwoModule(String twoWhere) {
        StringBuilder sbsql = new StringBuilder();
        sbsql.append("select module_id, module_name, is_expanded from funall_module ");
        sbsql.append("where module_level = 2 and is_show = '1' ");
        if (twoWhere != null && twoWhere.length() > 0) {
            sbsql.append(" and " + twoWhere);
        }
        sbsql.append(" order by module_index");
        DaoParam param = _dao.createParam(sbsql.toString());
        return _dao.query(param);
    }

    /**
	 * 取一级模块信息
	 * @param oneWhere -- 一级模块ID过滤语句
	 * @return List
	 */
    private List<Map<String, String>> queryModule(String oneWhere) {
        StringBuilder sbsql = new StringBuilder();
        sbsql.append("select module_id, module_name, is_expanded from funall_module ");
        sbsql.append("where module_level = 1 and is_show = '1' ");
        if (oneWhere != null && oneWhere.length() > 0) {
            sbsql.append(" and " + oneWhere);
        }
        sbsql.append(" order by module_index");
        DaoParam param = _dao.createParam(sbsql.toString());
        return _dao.query(param);
    }
}
