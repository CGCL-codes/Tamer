package sdloader.javaee;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletException;
import sdloader.constants.JavaEEConstants;
import sdloader.javaee.impl.FilterConfigImpl;
import sdloader.javaee.impl.ServletConfigImpl;
import sdloader.javaee.impl.ServletContextImpl;
import sdloader.javaee.webxml.ContextParamTag;
import sdloader.javaee.webxml.FilterMappingTag;
import sdloader.javaee.webxml.FilterTag;
import sdloader.javaee.webxml.InitParamTag;
import sdloader.javaee.webxml.ListenerTag;
import sdloader.javaee.webxml.ServletMappingTag;
import sdloader.javaee.webxml.ServletTag;
import sdloader.javaee.webxml.WebXml;
import sdloader.util.ClassUtil;
import sdloader.util.CollectionsUtil;
import sdloader.util.StringUtil;
import sdloader.util.WebUtil;

/**
 * WebAppクラス.
 * 
 * <pre>
 * Webアプリケーションに属するサーブレットコンテキスト、 サーブレット、フィルター、webxml、クラスローダーが
 * 集約されています。 WebAppManagerにより管理され、コンテキストパスをキーにして取り出します。
 * </pre>
 * 
 * @author c9katayama
 */
public class InternalWebApplication {

    /** web.xml定義 */
    private WebXml webXml;

    private WebAppContext webAppContext;

    /** WebApplicationクラスローダー */
    private ClassLoader webAppClassLoader;

    /** WebAppManager */
    private WebAppManager manager;

    /** ServletContext */
    private ServletContextImpl servletContext;

    /** Servlet Map */
    private Map<String, Servlet> servletMap;

    /** Filter Map */
    private Map<String, Filter> filterMap;

    /** ListenerEventDispatcher */
    private ListenerEventDispatcher listenerEventDispatcher;

    /**
	 * WebAppクラス
	 */
    public InternalWebApplication(WebAppContext webAppContext, ClassLoader webAppClassLoader, WebAppManager manager) {
        this.webXml = webAppContext.getWebXml();
        this.webAppContext = webAppContext;
        this.webAppClassLoader = webAppClassLoader;
        this.manager = manager;
        init();
    }

    public WebAppManager getWebApplicationManager() {
        return manager;
    }

    public WebAppContext getWebAppContext() {
        return webAppContext;
    }

    public URL[] getDocBase() {
        return getWebAppContext().getDocBase();
    }

    public String getContextPath() {
        return getWebAppContext().getContextPath();
    }

    public ClassLoader getWebAppClassLoader() {
        return webAppClassLoader;
    }

    public WebXml getWebXml() {
        return webXml;
    }

    public ServletContextImpl getServletContext() {
        return servletContext;
    }

    private void init() {
        ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(webAppClassLoader);
        try {
            initListener();
            initServletContext();
            initFilter();
            initServlet();
        } finally {
            Thread.currentThread().setContextClassLoader(oldClassLoader);
        }
    }

    private void initServletContext() {
        servletContext = new ServletContextImpl(this);
        servletContext.setDocBase(getDocBase());
        servletContext.setServletContextName(getContextPath());
        for (ContextParamTag param : webXml.getWebApp().getContextParam()) {
            servletContext.addInitParameter(param.getParamName(), param.getParamValue());
        }
        ServletContextEvent contextEvent = new ServletContextEvent(this.servletContext);
        listenerEventDispatcher.dispatchServletContextListener_contextInitialized(contextEvent);
    }

    private void initFilter() {
        List<FilterTag> filterList = webXml.getWebApp().getFilter();
        for (FilterTag filterTag : filterList) {
            if (filterMap == null) {
                filterMap = CollectionsUtil.newHashMap();
            }
            Filter filterInstance = (Filter) ClassUtil.newInstance(filterTag.getFilterClass());
            FilterConfig config = createFilterConfig(filterTag);
            try {
                filterInstance.init(config);
            } catch (ServletException e) {
                throw new RuntimeException(e);
            }
            filterMap.put(filterTag.getFilterName(), filterInstance);
        }
    }

    private void initListener() {
        listenerEventDispatcher = new ListenerEventDispatcher();
        for (ListenerTag listenerTag : webXml.getWebApp().getListener()) {
            Object listenerImp = ClassUtil.newInstance(listenerTag.getListenerClass());
            listenerEventDispatcher.addListener(listenerImp);
        }
    }

    private void initServlet() {
        List<ServletTag> servletList = webXml.getWebApp().getServlet();
        final List<ServletTag> loadOnStartUpList = CollectionsUtil.newLinkedList();
        final List<ServletTag> noStartUpList = CollectionsUtil.newLinkedList();
        sort: for (ServletTag servletTag : servletList) {
            Integer loadOnStartUp = servletTag.getLoadOnStartup();
            if (loadOnStartUp == null || loadOnStartUp < 0) {
                noStartUpList.add(servletTag);
            } else {
                for (int i = 0; i < loadOnStartUpList.size(); i++) {
                    int n = loadOnStartUpList.get(i).getLoadOnStartup();
                    if (loadOnStartUp < n) {
                        loadOnStartUpList.add(i, servletTag);
                        continue sort;
                    }
                }
                loadOnStartUpList.add(servletTag);
            }
        }
        servletList = loadOnStartUpList;
        servletList.addAll(noStartUpList);
        for (ServletTag servletTag : servletList) {
            if (servletMap == null) {
                servletMap = CollectionsUtil.newHashMap();
            }
            Servlet servletInstance = (Servlet) ClassUtil.newInstance(servletTag.getServletClass());
            ServletConfig config = createServletConfig(servletTag);
            try {
                servletInstance.init(config);
            } catch (ServletException e) {
                throw new RuntimeException(e);
            }
            servletMap.put(servletTag.getServletName(), servletInstance);
        }
        servletContext.setServletMap(servletMap);
    }

    private ServletConfig createServletConfig(ServletTag servletTag) {
        ServletConfigImpl config = new ServletConfigImpl();
        config.setServletContext(servletContext);
        for (InitParamTag initParam : servletTag.getInitParamList()) {
            String paramName = StringUtil.emptyIfNull(initParam.getParamName());
            String paramValue = StringUtil.emptyIfNull(initParam.getParamValue());
            config.addInitParameter(paramName, paramValue);
        }
        config.setServletName(servletTag.getServletName());
        return config;
    }

    private FilterConfig createFilterConfig(FilterTag filterTag) {
        FilterConfigImpl config = new FilterConfigImpl();
        config.setServletContext(servletContext);
        for (InitParamTag initParam : filterTag.getInitParamList()) {
            String paramName = StringUtil.emptyIfNull(initParam.getParamName());
            String paramValue = StringUtil.emptyIfNull(initParam.getParamValue());
            config.addInitParameter(paramName, paramValue);
        }
        config.setFilterName(filterTag.getFilterName());
        return config;
    }

    /**
	 * リクエストを処理するフィルターのリストを返します。
	 * 
	 * @param resourcePath
	 * @param servletName
	 * @param dispatcherType
	 *            REQUEST,FORWARD,INCLUDE,ERROR
	 * @return List フィルターのリスト フィルターがない場合、空のリストを返します。
	 * @throws ServletException
	 */
    public List<Filter> findFilters(String resourcePath, String servletName, String dispatcherType) {
        List<Filter> filterList = CollectionsUtil.newArrayList();
        if (resourcePath != null && filterMap != null) {
            List<FilterMappingTag> mappingList = webXml.getWebApp().getFilterMapping();
            search: for (FilterMappingTag mapping : mappingList) {
                Set<String> dispatchers = mapping.getDispatchers();
                if (dispatchers.isEmpty()) {
                    if (!dispatcherType.equals(JavaEEConstants.DISPATCHER_TYPE_REQUEST)) {
                        continue search;
                    }
                } else {
                    if (!dispatchers.contains(dispatcherType)) {
                        continue search;
                    }
                }
                String patternText = mapping.getUrlPattern();
                if (patternText != null) {
                    if (WebUtil.matchPattern(patternText, resourcePath) != WebUtil.PATTERN_NOMATCH) {
                        String filterName = mapping.getFilterName();
                        Filter filter = (Filter) filterMap.get(filterName);
                        if (filter == null) {
                            throw new RuntimeException("Filter not found. filterName=" + filterName);
                        }
                        filterList.add(filter);
                        continue search;
                    }
                }
                String nameTest = mapping.getServletName();
                if (nameTest != null && nameTest.equals(servletName)) {
                    String filterName = mapping.getFilterName();
                    Filter filter = (Filter) filterMap.get(filterName);
                    if (filter == null) {
                        throw new RuntimeException("Filter not found. filterName=" + filterName);
                    }
                    filterList.add(filter);
                }
            }
        }
        return filterList;
    }

    /**
	 * リクエストを処理するサーブレットマッピング情報を返します。
	 * 
	 * @param uri
	 * @return servletMapping 見つからなかった場合、nullを返します。
	 * @throws ServletException
	 */
    public ServletMapping findServletMapping(String uri) {
        if (uri != null && servletMap != null) {
            uri = WebUtil.stripQueryPart(uri);
            ServletMapping targetServletMapping = null;
            int currentMatchType = WebUtil.PATTERN_NOMATCH;
            int currentPathMatchLength = 0;
            List<ServletMappingTag> mappingList = webXml.getWebApp().getServletMapping();
            for (ServletMappingTag mapping : mappingList) {
                String patternText = mapping.getUrlPattern();
                int matchType = WebUtil.matchPattern(patternText, uri);
                if (matchType != WebUtil.PATTERN_NOMATCH && matchType >= currentMatchType) {
                    if (matchType == WebUtil.PATTERN_EXACT_MATCH) {
                        ServletMapping servletMapping = new ServletMapping(mapping.getServletName(), patternText);
                        return servletMapping;
                    }
                    if (matchType == WebUtil.PATTERN_PATH_MATCH && currentMatchType == WebUtil.PATTERN_PATH_MATCH) {
                        if (patternText.length() <= currentPathMatchLength) {
                            continue;
                        }
                    }
                    if (matchType == WebUtil.PATTERN_DEFAULT_MATCH && currentMatchType == WebUtil.PATTERN_DEFAULT_MATCH) {
                        continue;
                    }
                    currentMatchType = matchType;
                    currentPathMatchLength = patternText.length();
                    targetServletMapping = new ServletMapping(mapping.getServletName(), patternText);
                }
            }
            return targetServletMapping;
        }
        return null;
    }

    /**
	 * サーブレット名に該当するサーブレットを返します。
	 * 
	 * @param servletName
	 * @return servlet 見つからなかった場合、RuntimeExceptionをスローします。
	 */
    public Servlet findServlet(String servletName) {
        Servlet servlet = servletMap.get(servletName);
        if (servlet == null) {
            throw new RuntimeException("Servlet not found. servletName=" + servletName);
        }
        return servlet;
    }

    public List<Servlet> getServletList() {
        if (servletMap != null) {
            return CollectionsUtil.newArrayList(servletMap.values());
        } else {
            return null;
        }
    }

    public ListenerEventDispatcher getListenerEventDispatcher() {
        return listenerEventDispatcher;
    }

    public List<Filter> getFilterList() {
        if (filterMap != null) {
            return CollectionsUtil.newArrayList(filterMap.values());
        } else {
            return null;
        }
    }
}
