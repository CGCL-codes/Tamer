package com.fdaoud.rayures.test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;

/**
 * Parses a {@code web.xml} file and returns its key elements, making it convenient for using
 * different {@code web.xml} files for testing purposes.
 * 
 * <p>
 * Limits itself to parsing the following elements:
 * <ul>
 *   <li>{@code <context-param>}</li>
 *   <li>{@code <listener>}</li>
 *   <li>{@code <filter>}</li>
 *   <li>{@code <servlet>} (the first one only)</li>
 * </ul>
 * </p>
 *
 * <p>
 * You do not need to use this class directly; please refer to {@link TestWithMockContainer}.
 * </p>
 *
 * @author Frederic Daoud
 */
public class WebXmlParser {

    private WebXmlConfig contextParams = new WebXmlConfig();

    private List<Class<?>> listenerClasses = new ArrayList<Class<?>>();

    private List<WebXmlConfig> filterConfigs = new ArrayList<WebXmlConfig>();

    private WebXmlConfig servletConfig;

    public WebXmlParser(InputStream input) throws Exception {
        SAXBuilder builder = new SAXBuilder();
        Document document = builder.build(input);
        Element root = document.getRootElement();
        setNoNamespace(root);
        processContextParams(root);
        processListeners(root);
        processFilters(root);
        processFirstServlet(root);
    }

    @SuppressWarnings("unchecked")
    private void setNoNamespace(Element element) {
        element.setNamespace(Namespace.NO_NAMESPACE);
        List<Element> children = element.getChildren();
        for (Element child : children) {
            setNoNamespace(child);
        }
    }

    @SuppressWarnings("unchecked")
    public void processContextParams(Element root) throws Exception {
        List<Element> params = root.getChildren("context-param");
        for (Element param : params) {
            String name = param.getChildText("param-name").trim();
            String value = param.getChildText("param-value").trim();
            contextParams.addParam(name, value);
        }
    }

    @SuppressWarnings("unchecked")
    public void processListeners(Element root) throws Exception {
        List<Element> listeners = root.getChildren("listener");
        for (Element listener : listeners) {
            String listenerClassName = listener.getChildText("listener-class").trim();
            listenerClasses.add(Class.forName(listenerClassName));
        }
    }

    @SuppressWarnings("unchecked")
    public void processFilters(Element root) throws Exception {
        List<Element> filters = root.getChildren("filter");
        for (Element filter : filters) {
            String filterName = filter.getChildText("filter-name").trim();
            String filterClassName = filter.getChildText("filter-class").trim();
            WebXmlConfig filterConfig = new WebXmlConfig(filterName, Class.forName(filterClassName));
            addInitParams(filter, filterConfig);
            filterConfigs.add(filterConfig);
        }
    }

    public void processFirstServlet(Element root) throws Exception {
        Element servlet = root.getChild("servlet");
        if (servlet != null) {
            String servletName = servlet.getChildText("servlet-name").trim();
            String servletClassName = servlet.getChildText("servlet-class").trim();
            servletConfig = new WebXmlConfig(servletName, Class.forName(servletClassName));
            addInitParams(servlet, servletConfig);
        }
    }

    @SuppressWarnings("unchecked")
    private void addInitParams(Element element, WebXmlConfig config) {
        List<Element> initParams = element.getChildren("init-param");
        for (Element initParam : initParams) {
            String name = initParam.getChildText("param-name").trim();
            String value = initParam.getChildText("param-value").trim();
            config.addParam(name, value);
        }
    }

    public WebXmlConfig getContextParams() {
        return contextParams;
    }

    public List<Class<?>> getListenerClasses() {
        return listenerClasses;
    }

    public List<WebXmlConfig> getFilterConfigs() {
        return filterConfigs;
    }

    public WebXmlConfig getServletConfig() {
        return servletConfig;
    }
}
