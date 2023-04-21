package org.apache.myfaces.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.el.CompositeELResolver;
import javax.el.ELResolver;
import javax.el.ExpressionFactory;
import javax.faces.context.ExternalContext;
import javax.faces.el.PropertyResolver;
import javax.faces.el.VariableResolver;
import org.apache.myfaces.config.element.ManagedBean;
import org.apache.myfaces.config.element.NavigationRule;
import org.apache.myfaces.config.impl.digester.elements.ResourceBundle;

/**
 * Holds all configuration information (from the faces-config xml files) that is needed later during runtime. The config
 * information in this class is only available to the MyFaces core implementation classes (i.e. the myfaces source
 * tree). See MyfacesConfig for config parameters that can be used for shared or component classes.
 * 
 * @author Manfred Geiler (latest modification by $Author: lu4242 $)
 * @version $Revision: 824859 $ $Date: 2009-10-13 12:42:36 -0500 (Tue, 13 Oct 2009) $
 */
@SuppressWarnings("deprecation")
public class RuntimeConfig {

    private static final Logger log = Logger.getLogger(RuntimeConfig.class.getName());

    private static final String APPLICATION_MAP_PARAM_NAME = RuntimeConfig.class.getName();

    private final Collection<NavigationRule> _navigationRules = new ArrayList<NavigationRule>();

    private final Map<String, ManagedBean> _managedBeans = new HashMap<String, ManagedBean>();

    private boolean _navigationRulesChanged = false;

    private final Map<String, ResourceBundle> _resourceBundles = new HashMap<String, ResourceBundle>();

    private final Map<String, ManagedBean> _oldManagedBeans = new HashMap<String, ManagedBean>();

    private String _facesVersion;

    private CompositeELResolver facesConfigElResolvers;

    private CompositeELResolver applicationElResolvers;

    private VariableResolver _variableResolver;

    private PropertyResolver _propertyResolver;

    private ExpressionFactory _expressionFactory;

    private PropertyResolver _propertyResolverChainHead;

    private VariableResolver _variableResolverChainHead;

    public static RuntimeConfig getCurrentInstance(ExternalContext externalContext) {
        RuntimeConfig runtimeConfig = (RuntimeConfig) externalContext.getApplicationMap().get(APPLICATION_MAP_PARAM_NAME);
        if (runtimeConfig == null) {
            runtimeConfig = new RuntimeConfig();
            externalContext.getApplicationMap().put(APPLICATION_MAP_PARAM_NAME, runtimeConfig);
        }
        return runtimeConfig;
    }

    public void purge() {
        _navigationRules.clear();
        _oldManagedBeans.clear();
        _oldManagedBeans.putAll(_managedBeans);
        _managedBeans.clear();
        _navigationRulesChanged = false;
    }

    /**
     * Return the navigation rules that can be used by the NavigationHandler implementation.
     * 
     * @return a Collection of {@link org.apache.myfaces.config.element.NavigationRule NavigationRule}s
     */
    public Collection<NavigationRule> getNavigationRules() {
        return Collections.unmodifiableCollection(_navigationRules);
    }

    public void addNavigationRule(NavigationRule navigationRule) {
        _navigationRules.add(navigationRule);
        _navigationRulesChanged = true;
    }

    public boolean isNavigationRulesChanged() {
        return _navigationRulesChanged;
    }

    public void setNavigationRulesChanged(boolean navigationRulesChanged) {
        _navigationRulesChanged = navigationRulesChanged;
    }

    /**
     * Return the managed bean info that can be used by the VariableResolver implementation.
     * 
     * @return a {@link org.apache.myfaces.config.element.ManagedBean ManagedBean}
     */
    public ManagedBean getManagedBean(String name) {
        return _managedBeans.get(name);
    }

    public Map<String, ManagedBean> getManagedBeans() {
        return Collections.unmodifiableMap(_managedBeans);
    }

    public void addManagedBean(String name, ManagedBean managedBean) {
        _managedBeans.put(name, managedBean);
        if (_oldManagedBeans != null) _oldManagedBeans.remove(name);
    }

    /**
     * Return the resourcebundle which was configured in faces config by var name
     * 
     * @param name
     *            the name of the resource bundle (content of var)
     * @return the resource bundle or null if not found
     */
    public ResourceBundle getResourceBundle(String name) {
        return _resourceBundles.get(name);
    }

    /**
     * @return the resourceBundles
     */
    public Map<String, ResourceBundle> getResourceBundles() {
        return _resourceBundles;
    }

    public void addResourceBundle(ResourceBundle bundle) {
        if (bundle == null) {
            throw new IllegalArgumentException("bundle must not be null");
        }
        String var = bundle.getVar();
        if (_resourceBundles.containsKey(var) && log.isLoggable(Level.WARNING)) {
            log.warning("Another resource bundle for var '" + var + "' with base name '" + _resourceBundles.get(var).getBaseName() + "' is already registered. '" + _resourceBundles.get(var).getBaseName() + "' will be replaced with '" + bundle.getBaseName() + "'.");
        }
        _resourceBundles.put(var, bundle);
    }

    public void addFacesConfigElResolver(ELResolver resolver) {
        if (facesConfigElResolvers == null) {
            facesConfigElResolvers = new org.apache.myfaces.el.CompositeELResolver();
        }
        facesConfigElResolvers.add(resolver);
    }

    public ELResolver getFacesConfigElResolvers() {
        return facesConfigElResolvers;
    }

    public void addApplicationElResolver(ELResolver resolver) {
        if (applicationElResolvers == null) {
            applicationElResolvers = new org.apache.myfaces.el.CompositeELResolver();
        }
        applicationElResolvers.add(resolver);
    }

    public ELResolver getApplicationElResolvers() {
        return applicationElResolvers;
    }

    public void setVariableResolver(VariableResolver variableResolver) {
        _variableResolver = variableResolver;
    }

    public VariableResolver getVariableResolver() {
        return _variableResolver;
    }

    public void setPropertyResolver(PropertyResolver propertyResolver) {
        _propertyResolver = propertyResolver;
    }

    public PropertyResolver getPropertyResolver() {
        return _propertyResolver;
    }

    public ExpressionFactory getExpressionFactory() {
        return _expressionFactory;
    }

    public void setExpressionFactory(ExpressionFactory expressionFactory) {
        _expressionFactory = expressionFactory;
    }

    public void setPropertyResolverChainHead(PropertyResolver resolver) {
        _propertyResolverChainHead = resolver;
    }

    public PropertyResolver getPropertyResolverChainHead() {
        return _propertyResolverChainHead;
    }

    public void setVariableResolverChainHead(VariableResolver resolver) {
        _variableResolverChainHead = resolver;
    }

    public VariableResolver getVariableResolverChainHead() {
        return _variableResolverChainHead;
    }

    public Map<String, ManagedBean> getManagedBeansNotReaddedAfterPurge() {
        return _oldManagedBeans;
    }

    public void resetManagedBeansNotReaddedAfterPurge() {
        _oldManagedBeans.clear();
    }

    public String getFacesVersion() {
        return _facesVersion;
    }

    void setFacesVersion(String facesVersion) {
        _facesVersion = facesVersion;
    }
}
