package net.simpleframework.web.page.component;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.simpleframework.core.AbstractElementBean;
import net.simpleframework.util.StringUtils;
import net.simpleframework.util.XMLUtils;
import net.simpleframework.web.page.IPageResourceProvider;
import net.simpleframework.web.page.PageDocument;
import net.simpleframework.web.page.PageDocumentFactory;
import net.simpleframework.web.page.PageRequestResponse;
import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AbstractComponentBean extends AbstractElementBean {

    public static final String FORM_PREFIX = "form_";

    private final PageDocument pageDocument;

    private final IComponentRegistry componentRegistry;

    protected String name;

    private boolean runImmediately = true;

    private String handleClass;

    private EHandleScope handleScope;

    protected String selector;

    private String includeRequestData;

    private String parameters;

    private boolean effects = true;

    public AbstractComponentBean(final IComponentRegistry componentRegistry, final PageDocument pageDocument, final Element element) {
        super(element);
        this.componentRegistry = componentRegistry;
        this.pageDocument = pageDocument;
    }

    public void saveToFile() throws IOException {
        syncElement();
        XMLUtils.saveToFile(getElement().getDocument(), getPageDocument().getDocumentFile());
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public boolean isRunImmediately() {
        return runImmediately;
    }

    public void setRunImmediately(final boolean runImmediately) {
        this.runImmediately = runImmediately;
    }

    public String getHandleClass() {
        if (!StringUtils.hasText(handleClass)) {
            final Class<?> c = getDefaultHandleClass();
            if (c != null) {
                handleClass = c.getName();
            }
        }
        return handleClass;
    }

    protected Class<? extends IComponentHandle> getDefaultHandleClass() {
        return null;
    }

    public void setHandleClass(final String handleClass) {
        this.handleClass = handleClass;
    }

    public EHandleScope getHandleScope() {
        return handleScope != null ? handleScope : EHandleScope.singleton;
    }

    public void setHandleScope(final EHandleScope handleScope) {
        this.handleScope = handleScope;
    }

    public String getSelector() {
        final StringBuilder sb = new StringBuilder();
        sb.append("#").append(AbstractComponentBean.FORM_PREFIX).append(hashId());
        if (StringUtils.hasText(selector)) {
            sb.append(", ").append(selector);
        }
        return sb.toString();
    }

    public void setSelector(final String selector) {
        this.selector = selector;
    }

    public boolean isEffects() {
        return effects;
    }

    public void setEffects(final boolean effects) {
        this.effects = effects;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(final String parameters) {
        this.parameters = parameters;
    }

    public String getIncludeRequestData() {
        return includeRequestData;
    }

    public void setIncludeRequestData(final String includeRequestData) {
        this.includeRequestData = includeRequestData;
    }

    public String getActionFunction() {
        return "__f_" + hashId();
    }

    public IComponentHandle getComponentHandle(final PageRequestResponse requestResponse) {
        return IComponentHandle.Utils.getComponentHandle(requestResponse, this);
    }

    public PageDocument getPageDocument() {
        return pageDocument;
    }

    public IPageResourceProvider getPageResourceProvider() {
        return getComponentRegistry().getPageResourceProvider();
    }

    public IComponentRegistry getComponentRegistry() {
        return componentRegistry;
    }

    public IComponentRender getComponentRender() {
        return getComponentRegistry().getComponentRender();
    }

    public IComponentResourceProvider getComponentResourceProvider() {
        return getComponentRegistry().getComponentResourceProvider();
    }

    private String _hashId;

    public String hashId() {
        if (_hashId == null) {
            final Element element = getElement();
            final String name = element != null ? element.attributeValue("name") : getName();
            if (StringUtils.hasText(name)) {
                _hashId = getComponentHashByName(pageDocument, name);
            } else {
                _hashId = StringUtils.hash(this);
            }
        }
        return _hashId;
    }

    /**************************** static utils *******************************/
    public static String getComponentHashByName(final PageDocument pageDocument, final String componentName) {
        return StringUtils.hash(pageDocument.hashId() + componentName);
    }

    public static AbstractComponentBean getComponentBeanByName(final PageRequestResponse requestResponse, final String xmlPath, final String componentName) {
        if (StringUtils.hasText(componentName)) {
            final File xFile = new File(requestResponse.getServletContext().getRealPath(xmlPath));
            final PageDocument pageDocument = PageDocumentFactory.getPageDocumentAndCreate(xFile, requestResponse);
            if (pageDocument != null) {
                return ComponentBeanUtils.getComponentBeanByHashId(requestResponse, getComponentHashByName(pageDocument, componentName));
            }
        }
        return null;
    }

    public static AbstractComponentBean getComponentBeanByRequestId(final PageRequestResponse requestResponse, final String requestId) {
        return ComponentBeanUtils.getComponentBeanByHashId(requestResponse, requestResponse.getRequestParameter(requestId));
    }

    public static Map<String, AbstractComponentBean> allComponents = new ConcurrentHashMap<String, AbstractComponentBean>();

    public String getResourceHomePath() {
        return getComponentResourceProvider().getResourceHomePath();
    }

    public String getResourceHomePath(final PageRequestResponse requestResponse) {
        return getComponentResourceProvider().getResourceHomePath(requestResponse);
    }

    public String getCssResourceHomePath(final PageRequestResponse requestResponse) {
        return getComponentResourceProvider().getCssResourceHomePath(requestResponse);
    }
}
