package net.paoding.rose.web.impl.module;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.paoding.rose.web.ControllerErrorHandler;
import net.paoding.rose.web.InterceptorDelegate;
import net.paoding.rose.web.ParamValidator;
import net.paoding.rose.web.paramresolver.ParamResolver;
import org.springframework.web.context.WebApplicationContext;

/**
 * {@link Module}的实现
 * 
 * @author 王志亮 [qieqie.wang@gmail.com]
 */
public class ModuleImpl implements Module {

    private String mappingPath;

    private URL url;

    private String relativePath;

    private Module parent;

    private WebApplicationContext applicationContext;

    private List<ControllerRef> controllers = new ArrayList<ControllerRef>();

    private List<ParamResolver> customerResolvers = Collections.emptyList();

    private List<InterceptorDelegate> interceptors = Collections.emptyList();

    private List<ParamValidator> validators = Collections.emptyList();

    private ControllerErrorHandler errorHandler;

    public ModuleImpl(Module parent, URL url, String mappingPath, String relativePath, WebApplicationContext context) {
        this.parent = parent;
        this.url = url;
        this.mappingPath = mappingPath;
        this.relativePath = relativePath;
        this.applicationContext = context;
    }

    @Override
    public Module getParent() {
        return parent;
    }

    @Override
    public String getMappingPath() {
        return mappingPath;
    }

    @Override
    public URL getUrl() {
        return url;
    }

    @Override
    public String getRelativePath() {
        return relativePath;
    }

    @Override
    public WebApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public List<ControllerRef> getControllers() {
        return Collections.unmodifiableList(controllers);
    }

    public ModuleImpl addController(String[] mappingPaths, Class<?> controllerClass, String controllerName, Object controllerObject) {
        ControllerRef controller = new ControllerRef(mappingPaths, controllerName, controllerObject, controllerClass);
        this.controllers.add(controller);
        return this;
    }

    public void setCustomerResolvers(List<ParamResolver> resolvers) {
        this.customerResolvers = resolvers;
    }

    public List<ParamResolver> getCustomerResolvers() {
        return Collections.unmodifiableList(customerResolvers);
    }

    public void setControllerInterceptors(List<InterceptorDelegate> interceptors) {
        this.interceptors = interceptors;
    }

    @Override
    public List<InterceptorDelegate> getInterceptors() {
        return interceptors;
    }

    public void setValidators(List<ParamValidator> validators) {
        this.validators = validators;
    }

    @Override
    public List<ParamValidator> getValidators() {
        return validators;
    }

    @Override
    public ControllerErrorHandler getErrorHandler() {
        return errorHandler;
    }

    public void setErrorHandler(ControllerErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }
}
