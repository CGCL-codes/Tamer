package com.faceye.core.util.helper;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author：宋海鹏
 * @Connection:E_mail:ecsun@sohu.com/myecsun@hotmail.com QQ:82676683
 * @Copy Right:www.faceye.com
 * @System:www.faceye.com网络支持系统
 * @Create Time:2007-9-22
 * @Package com.faceye.core.util.helper.StringPool.java
 * @Description:常量
 */
public class StringPool {

    public static final String FORWARD = "forward";

    public static final String MESSAGE = "message";

    public static final String METHOD = "method";

    public static final String QUERY_TYPE = "queryType";

    public static final String QUERY_TYPE_BY_NAMED_HQL = "byNamedHQL";

    public static final String QUERY_TYPE_BY_HQL = "byHQL";

    public static final String QUERY_TYPE_BY_SQL = "bySQL";

    public static final String QUERY_TYPE_BY_NAMED_SQL = "byNamedSQL";

    public static final String QUERY_TYPE_BY_DetachedCriteria = "byDetachedCriteria";

    public static final String QUERY_NAMED_HQL = "namedHql";

    public static final String QUERY_HQL = "hql";

    public static final String QUERY_SQL = "sql";

    public static final String QUERY_NAMED_SQL = "namedSql";

    public static final String QUERY_DetachedCriteria = "detachedCriteria";

    public static final String QUERY_PARAMS = "params";

    public static final String QUERY_VALUES = "values";

    public static final String QUERY_DISTRIBUTE_CHARACTER = "@";

    public static final String QUERY_PARAMS_IDENTIFER = "search_";

    public static final String CURRENT_INDEX = "currentIndex";

    public static final String CURRENT_PAGE_SIZE = "pageSize";

    public static final String PAGINATION_SUPPORT = "page";

    public static final String LIMIT = "limit";

    public static final String START = "start";

    public static final String SECURITY_RESOURCE_TYPE_URL = "URL";

    public static final String SECURITY_RESOURCE_TYPE_FUNCTION = "FUNCTION";

    public static final String SECURITY_RESOURCE_TYPE_COMPONENTS = "COMPONENTS";

    public static final Map SECURITY_RESOURCE_TYPE = new HashMap();

    public static Map getSecurityResourceType() {
        if (SECURITY_RESOURCE_TYPE.isEmpty()) {
            SECURITY_RESOURCE_TYPE.put(SECURITY_RESOURCE_TYPE_URL, "URL");
            SECURITY_RESOURCE_TYPE.put(SECURITY_RESOURCE_TYPE_COMPONENTS, "���");
            SECURITY_RESOURCE_TYPE.put(SECURITY_RESOURCE_TYPE_FUNCTION, "����");
        }
        return SECURITY_RESOURCE_TYPE;
    }

    /**
	 * 实体相关参数
	 */
    public static final String ENTITY_ID = "id";

    public static final String ENTITY_CLASS = "entityClass";

    public static final String ENTITY_DETAIL = "detail";

    public static final String ENTITY_SAVE = "save";

    public static final String ENTITY_LIST = "list";

    public static final String ENTITY_IDS = "ids";

    public static final String ENTITY_IDS_SPLIT_WITH = "_";

    /**
	 * 树开结构相关
	 */
    public static final String TREE_ID = "id";

    public static final String TREE_NODE = "node";

    public static final String TREE_NAME = "name";

    public static final String TREE_ACTION = "action";

    public static final String TREE_PARENTID = "parentid";

    public static final String TREE_HREF_TARGET = "hrefTarget";

    public static final String TREE_ROOT_ID = "source";

    public static final String TREE_URL = "url";

    public static final String TREE_DOMAIN = "domain";

    public static final String TREE_CLS = "cls";

    public static final String TREE_ICON_Cls = "iconCls";

    public static final String TREE_ORDER = "order";

    public static final String CHARACTER_COMMA = ",";

    public static final String CHARACTER_AND = "&";

    public static final String CHARACTER_SAME = "=";

    public static final String CHARACTER_MIDDLE_LEFT = "[";

    public static final String CHARACTER_MIDDLE_RIGHT = "]";

    public static final String CHARACTER_LARGE_LFET = "{";

    public static final String CHARACTER_LARGE_RIGHT = "}";

    public static final String CHARACTER_COLON = ":";

    /**
	 * 在实体中用于生成json格式数据的方法名，本方法存在于实体中，不存在于实体的超类中，同时，本方法没有参数。
	 */
    public static final String REFLECTION_METHOD_JSON = "json";

    public static final String REFLECTION_METHOD_MAP = "map";

    public static final String SECURITY_REGISTER_ROLE_ID = "1";

    public static final String SECURITY_ADMINISTRATOR_ROLE_ID = "2";

    public static final String USER_IN_SESSION = "user";

    public static final String USER_GUEST_ID = "999999999";

    public static final String SECURITY_AUTH_PERMISSION = "AUTH_";

    /**
	 * Feed 相关常量
	 */
    public static final String FEED_URL = "url";

    public static final String FEED_FEED = "feed";

    public static final String COLUMN_FEED_ID = "";

    public static final String USER_RESORUCE_CATEGORY_ROOT_ID = "0";

    public static final String USER_RESORUCE_CATEGORY_ROOT_NAME = "Root";

    /**
	 * portal
	 */
    public static final String PORTAL_DEFAULT_PORTAL_NAME = "首页";

    public static final String PORTAL_DEFAULT_USER_DEFINE_PORTAL_NAME = "default new page";

    /**
	 * Feed Tradition
	 */
    public static final String CATEGORY_FEED_ID = "402881ce17a971ca0117a97476c30002";

    public static final String CATEGORY_TRADITION_ID = "402881ce17a971ca0117a9743df20001";

    /**
	 * Blog常量
	 */
    public static final String BLOG_ARTICLE_DEFAULT_USER_CATEGORY_NAME = "Default Category";

    public static final String BLOG_PERIOD_LIST_ORDER_WEEK = "week";

    public static final String BLOG_PERIOD_LIST_ORDER_MONTH = "month";

    public static final String BLOG_PERIOD_LIST_ORDER_QUARTER = "quarter";

    public static final String BLOG_PERIOD_LIST_ORER_YEAR = "year";

    public static final String BLOG_PERIOD_LIST_ORDER_ALL = "all";

    public static final String BLOG_USER = "blog_user";

    public static final String BLOG_SEARCH_ALL = "all";

    public static final String BLOG_SEARCH_TITLE_ONLY = "title_only";
}
