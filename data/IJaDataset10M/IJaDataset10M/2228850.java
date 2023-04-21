package com.guanghua.brick.html;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.guanghua.brick.BrickConstant;
import com.guanghua.brick.biz.content.IContentBiz;
import com.guanghua.brick.db.QueryFilterHelper;

public class SQLContent implements IContent {

    private static final String CONTENT_BIZ_ID = "brick.biz.content";

    private static final String CONTENT_POOL_SQL = "globe.content.pool.sql";

    private static Log logger = LogFactory.getLog(SQLContent.class);

    private String id = null;

    private String sql = null;

    private boolean cache = false;

    public List<Map<String, String>> buildContent(HttpServletRequest request, HttpServletResponse response) {
        Map<String, List<Map<String, String>>> pool = getContentSQLPool(request.getSession().getServletContext());
        if (cache && pool.get(id) != null) return pool.get(id);
        IContentBiz content = (IContentBiz) BrickConstant.getLocator().getBizBean(CONTENT_BIZ_ID);
        Map<String, Object> filter = QueryFilterHelper.newInstanceWithoutPrefix(request.getParameterMap());
        try {
            List<Map<String, String>> list = content.listContent(sql, filter);
            if (cache) pool.put(id, list);
            return list;
        } catch (Exception e) {
            logger.error("wrong when get content list by sqlcontent:" + sql, e);
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public static Map<String, List<Map<String, String>>> getContentSQLPool(ServletContext servletContext) {
        Map<String, List<Map<String, String>>> map = (Map<String, List<Map<String, String>>>) servletContext.getAttribute(SQLContent.CONTENT_POOL_SQL);
        if (map == null) {
            map = new HashMap<String, List<Map<String, String>>>();
            servletContext.setAttribute(SQLContent.CONTENT_POOL_SQL, map);
        }
        logger.trace("sql content cache pool: [" + map + "]");
        return map;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public boolean isCache() {
        return cache;
    }

    public void setCache(boolean cache) {
        this.cache = cache;
    }
}
