package vlan.webgame.manage.services;

import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.jmantis.core.PageResult;
import org.jmantis.core.dao.SqlQueryDao;
import org.jmantis.core.dao.grid.Column;
import org.jmantis.core.dao.grid.Columns;
import org.jmantis.core.dao.grid.GridPageResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vlan.webgame.manage.report.GridReport.Item;
import com.google.common.collect.Maps;

public class SqlQueryService {

    private static final Logger log = LoggerFactory.getLogger(SqlQueryService.class);

    private SqlQueryDao sqlQueryDao;

    private Map<String, Columns> columnsTemp = Maps.newHashMap();

    public void setSqlQueryDao(SqlQueryDao sqlQueryDao) {
        this.sqlQueryDao = sqlQueryDao;
    }

    public GridPageResult<Map<String, Object>> query(String sql, int page, int pageSize) {
        return sqlQueryDao.query(sql, page, pageSize);
    }

    public PageResult<Map<String, Object>> queryData(Item item, int page, int pageSize) {
        String sql = item.getSql();
        String where = item.getWhere();
        StringBuilder sb = new StringBuilder(String.format(sql, item.getField()));
        if (StringUtils.isNotBlank(where)) {
            sb.append(" WHERE ");
            sb.append(where);
        }
        sql = sb.toString();
        log.debug("执行Grid报表查询[sql:{}]", sql);
        Columns cs = columnsTemp.get(item.getName());
        if (cs == null) {
            return null;
        }
        PageResult<Map<String, Object>> result = sqlQueryDao.queryData(cs, sql, page, pageSize);
        return result;
    }

    public GridPageResult<Map<String, Object>> query(Item item, int page, int pageSize) {
        String sql = item.getSql();
        String where = item.getWhere();
        StringBuilder sb = new StringBuilder(String.format(sql, item.getField()));
        if (StringUtils.isNotBlank(where)) {
            sb.append(" WHERE ");
            sb.append(where);
        }
        sql = sb.toString();
        log.debug("执行Grid报表查询[sql:{}]", sql);
        GridPageResult<Map<String, Object>> result = sqlQueryDao.query(sql, page, pageSize);
        Columns cs = result.getColumns();
        for (Column c : cs) {
            String code = c.getCode();
            String text = item.map(code);
            c.setText(text == null ? code : text);
        }
        columnsTemp.put(item.getName(), cs);
        return result;
    }
}
