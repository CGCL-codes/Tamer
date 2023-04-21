package com.cjframework.common.dao;

import java.util.List;
import java.util.Map;
import com.cjframework.common.action.PageObj;

/**
 * MS SQl 2005 数据库访问对象层基类
 * <pre>
 * 本类提供一些共用的分页访问方法，主要包含：
 *   1、生成分页链接字符串（Ajax）
 *   2、生成分页链接字符串
 * </pre>
 * CJFrameWork Version: 1.0
 * @author caojian
 * @see BaseDAO
 * @see BasePageDAO
 * @see MsSqlBaseDAO
 * @see MySqlBaseDAO
 * @see OracleBaseDAO
 */
public class MsSqlAdvancedBaseDAO extends MsSqlBaseDAO {

    /**
	 * Ajax 分页查询数据（用于）
	 * @param sql 查询数据的SQL语句，如：select * from ....
	 * @param params 参数数组，用于传递参数值
	 * @param action PageBaseAction 类或子类
	 * @param dao BaseDAO 类或其子类
	 * @return 分页后得到数据
	 * @throws Exception
	 */
    protected List<Map<String, Object>> createPageForAjax(String sql, String orderByClause, Object[] params, PageObj pageObj) throws Exception {
        int recordTotal = this.findTotalWithParams(sql, params);
        pageObj.setPageStr(this.pageLinkString(recordTotal, pageObj));
        return this.getDbTemplate().findForList(this.wrapSql(sql, orderByClause, pageObj), params);
    }

    /**
	 * 为MS SQl 2005数据库 计算分页信息
	 * @param action
	 * @return
	 * @throws Exception
	 */
    private String wrapSql(String sql, String orderByClause, PageObj pageObj) throws Exception {
        int fromInedx = sql.toLowerCase().indexOf("from");
        sql = sql.substring(0, fromInedx) + ", row_number() over (order by " + orderByClause + ") as rowNumber " + sql.substring(fromInedx, sql.length());
        return " with tableAliasName as ( " + sql + " ) " + " select * from tableAliasName where rowNumber between " + (pageObj.getPageCount() * (pageObj.getPageIndex() - 1) + 1) + " and " + pageObj.getPageCount() * (pageObj.getPageIndex());
    }
}
