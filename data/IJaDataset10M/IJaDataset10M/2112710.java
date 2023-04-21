package com.sworddance.beans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import com.sworddance.util.ApplicationIllegalArgumentException;

/**
 * TODO: believe we need HibernateClass resolving here via proxyLoader.
 * @author patmoore
 * @param <I>
 * @param <O>
 */
public class RootProxyMapper<I, O extends I> extends ProxyMapperImpl<I, O> {

    private ProxyBehavior proxyBehavior;

    private Map<String, Object> originalValues;

    private Map<String, Object> newValues;

    private ConcurrentMap<String, ProxyMapperImplementor<?, ?>> childProxies = new ConcurrentHashMap<String, ProxyMapperImplementor<?, ?>>();

    /**
     * @param realObject
     * @param realClass
     * @param proxyClass TODO
     * @param proxyBehavior
     * @param proxyLoader TODO
     * @param propertyChains first property is used to determine equality
     * @param originalValues TODO
     * @param newValues TODO
     */
    public RootProxyMapper(O realObject, Class<? extends O> realClass, Class<? extends I> proxyClass, ProxyBehavior proxyBehavior, ProxyLoader proxyLoader, List<String> propertyChains, Map<String, Object> originalValues, Map<String, Object> newValues) {
        super(null, realObject, realClass, proxyClass, proxyLoader, propertyChains);
        this.setProxyBehavior(proxyBehavior);
        this.newValues = newValues;
        this.originalValues = originalValues;
        initValuesMap(propertyChains);
    }

    public void initValuesMap(List<String> propertyChains) {
        originalValues = new ConcurrentHashMap<String, Object>();
        newValues = new ConcurrentHashMap<String, Object>();
        if (this.getRealClass() != null) {
            for (String property : propertyChains) {
                initValue(property);
            }
        }
    }

    @Override
    public boolean containsKey(Object propertyName) {
        return this.getNewValues().containsKey(propertyName) || this.getOriginalValues().containsKey(propertyName) || this.childProxies.containsKey(propertyName);
    }

    /**
     * @param propertyName
     * @param result
     */
    @Override
    protected void putOriginalValues(String propertyName, Object result) {
        if (propertyName == null) {
            throw new ApplicationIllegalArgumentException("propertyName cannot be null");
        }
        this.getOriginalValuesMap().put(propertyName, result == null ? NullObject : result);
    }

    @Override
    protected void putNewValues(String propertyName, Object result) {
        if (propertyName == null) {
            throw new ApplicationIllegalArgumentException("propertyName cannot be null");
        }
        this.getNewValues().put(propertyName, result == null ? NullObject : result);
    }

    @Override
    public Object getCachedValue(String propertyName) {
        Object o;
        ProxyMapper<?, ?> childProxy;
        if (this.getNewValues().containsKey(propertyName)) {
            o = this.getNewValues().get(propertyName);
        } else if (this.getOriginalValues().containsKey(propertyName)) {
            o = this.getOriginalValues().get(propertyName);
        } else if ((childProxy = this.childProxies.get(propertyName)) != null) {
            o = childProxy.getExternalFacingProxy();
        } else {
            o = null;
        }
        if (o == NullObject) {
            o = null;
        }
        return o;
    }

    @Override
    public void clear() {
        super.clear();
        for (ProxyMapper<?, ?> proxyMapper : this.childProxies.values()) {
            proxyMapper.clear();
        }
    }

    /**
     * Child proxies are used when 'this' has been asked for a property that is partial path to leaf properties.
     * <p>For example, a ProxyMapper is managing properties:
     * <ul><li>foo.bar</li>
     * <li>foo.goo</li>
     * <li>bee</li>
     * </ul>
     * The ProxyMapper is asked for the "foo" property. The ProxyMapper will return a child ProxyMapper "foo" that has properties:
     * <ul><li>bar (mapped to parent "foo.bar")</li>
     * <li>goo (mapped to parent "foo.goo")</li>
     * </ul>
     * This allows the ProxyMapper usage to be less visible to called utility code.</p>
     * @param <CI>
     * @param <CO>
     * @param propertyName
     * @return existing child proxy
     */
    @SuppressWarnings("unchecked")
    public <CI, CO extends CI> ProxyMapperImplementor<CI, CO> getExistingChildProxy(String propertyName) {
        if (this.childProxies != null) {
            return (ProxyMapperImplementor<CI, CO>) this.childProxies.get(propertyName);
        } else {
            return null;
        }
    }

    /**
     * @param propertyName
     * @param proxy
     */
    private void setChildProxy(String propertyName, ProxyMapperImplementor<?, ?> proxy) {
        this.childProxies.putIfAbsent(propertyName, proxy);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected <CI, CO extends CI> ProxyMapperImplementor<CI, CO> getChildProxyMapper(String propertyName, PropertyAdaptor propertyAdaptor, Object base, ProxyMapperImplementor<?, ?> baseProxyMapper) {
        ProxyMapperImplementor<CI, CO> childProxy = getExistingChildProxy(propertyName);
        CO propValue;
        if (base != null && (childProxy == null || !childProxy.isRealObjectSet())) {
            propValue = (CO) propertyAdaptor.read(base);
        } else {
            propValue = null;
        }
        if (childProxy != null) {
            if (!childProxy.isRealObjectSet()) {
                childProxy.setRealObject(propValue);
            }
            return childProxy;
        } else if (propValue != null) {
            childProxy = new ChildProxyMapper<CI, CO>(propertyName, baseProxyMapper, propValue, propertyAdaptor, new ArrayList<String>());
            setChildProxy(propertyName, childProxy);
            childProxy = getExistingChildProxy(propertyName);
        }
        return childProxy;
    }

    @Override
    public RootProxyMapper<I, O> getRootProxyMapper() {
        return this;
    }

    /**
     * @param proxyBehavior the proxyBehavior to set
     */
    public void setProxyBehavior(ProxyBehavior proxyBehavior) {
        this.proxyBehavior = proxyBehavior;
    }

    /**
     * @return the proxyBehavior
     */
    @Override
    public ProxyBehavior getProxyBehavior() {
        return proxyBehavior;
    }

    /**
     * @see com.sworddance.beans.ProxyMapper#getNewValues()
     */
    @Override
    public Map<String, Object> getNewValues() {
        return this.newValues;
    }

    @Override
    public Map<String, Object> getOriginalValues() {
        return Collections.unmodifiableMap(this.getOriginalValuesMap());
    }

    protected Map<String, Object> getOriginalValuesMap() {
        return this.originalValues;
    }

    /**
     * @param basePropertyPath
     * @return the map of new values assigned to this ProxyMapper
     */
    public Map<String, Object> getNewValues(String basePropertyPath) {
        return getSubvalues(basePropertyPath, getNewValues());
    }

    /**
     * @param basePropertyPath
     * @return the original values
     */
    public Map<String, Object> getOriginalValues(String basePropertyPath) {
        return getSubvalues(basePropertyPath, getOriginalValues());
    }

    private Map<String, Object> getSubvalues(String basePropertyPath, Map<String, Object> rootMap) {
        String searchString = basePropertyPath + ".";
        Map<String, Object> result = new HashMap<String, Object>();
        for (Map.Entry<String, Object> entry : rootMap.entrySet()) {
            if (entry.getKey().startsWith(searchString)) {
                result.put(entry.getKey(), entry.getValue());
            }
        }
        return result;
    }

    /**
     *
     * @see com.sworddance.beans.ProxyMapperImplementor#getBaseProxyMapper()
     */
    @Override
    public ProxyMapper<?, ?> getBaseProxyMapper() {
        return null;
    }
}
