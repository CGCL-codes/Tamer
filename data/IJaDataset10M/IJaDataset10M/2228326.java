package org.jgentleframework.context;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import org.aopalliance.intercept.Interceptor;
import org.jgentleframework.configure.AbstractConfig;
import org.jgentleframework.configure.enums.Scope;
import org.jgentleframework.configure.objectmeta.ObjectBindingInterceptor;
import org.jgentleframework.context.injecting.AppropriateScopeNameClass;
import org.jgentleframework.context.injecting.Provider;
import org.jgentleframework.context.injecting.scope.ScopeInstance;
import org.jgentleframework.context.services.ServiceHandler;
import org.jgentleframework.context.support.CoreInstantiationSelector;
import org.jgentleframework.context.support.CoreInstantiationSelectorImpl;
import org.jgentleframework.context.support.InstantiationSelector;
import org.jgentleframework.context.support.InstantiationSelectorImpl;
import org.jgentleframework.core.intercept.InterceptionException;
import org.jgentleframework.core.intercept.support.AbstractMatcher;
import org.jgentleframework.core.intercept.support.Matcher;
import org.jgentleframework.reflection.metadata.Definition;
import org.jgentleframework.utils.Assertor;
import org.jgentleframework.utils.ReflectUtils;
import org.jgentleframework.utils.data.Pair;

/**
 * This abstract class is an extension of {@link Provider}, is responsible for
 * <code>getBeanInstance</code> method overriding. The
 * {@link #getBeanInstance(Class, Class, String, Definition)} method is
 * overrided in order to customize the bean instantiation according as its
 * configuration data. Moreover, this class provides some methods in order to
 * manage customizing {@link Interceptor interceptors}, matcher cache, ...
 * 
 * @author LE QUOC CHUNG - mailto: <a
 *         href="mailto:skydunkpro@yahoo.com">skydunkpro@yahoo.com</a>
 * @date Mar 8, 2008
 * @see Provider
 * @see IAbstractServiceManagement
 */
public abstract class AbstractServiceManagement extends ProviderCoreCreator implements IAbstractServiceManagement {

    /**
	 * Constructor.
	 * 
	 * @param serviceHandler
	 *            the service handler
	 * @param OLArray
	 *            the oL array
	 */
    public AbstractServiceManagement(ServiceHandler serviceHandler, List<Map<String, Object>> OLArray) {
        super(serviceHandler, OLArray);
    }

    @Override
    public Object getBeanInstance(AppropriateScopeNameClass asc) {
        Class<?> type = asc.clazz;
        Class<?> targetClass = asc.targetClass;
        Definition definition = asc.definition;
        String mappingName = asc.ref;
        CoreInstantiationSelector coreSelector = new CoreInstantiationSelectorImpl(type, targetClass, asc.ref, definition);
        ScopeInstance scope = null;
        synchronized (scopeList) {
            scope = scopeList.get(asc.scopeName);
        }
        try {
            if (scope != null && !scope.equals(Scope.SINGLETON)) {
                Object result = returnsCachingResult(coreSelector);
                if (result != NULL_SHAREDOBJECT) {
                    return result;
                }
            }
        } catch (Throwable e) {
            if (log.isFatalEnabled()) {
                log.fatal("Could not instantiate bean instance!", e);
            }
        }
        synchronized (this) {
            matcherCache = this.matcherCache == null ? new ConcurrentHashMap<Definition, Matcher<Definition>>() : matcherCache;
            interceptorList = this.interceptorList == null ? new HashMap<Matcher<Definition>, List<Object>>() : interceptorList;
        }
        Matcher<Definition> matcher = getCachedMatcherOf(definition);
        if (matcher == null || (matcher != null && !matcher.matches(definition))) {
            refreshMatcherCache(definition);
            matcher = getCachedMatcherOf(definition);
        }
        Object result = null;
        HashMap<Interceptor, Matcher<Definition>> mapMatcherInterceptor;
        mapMatcherInterceptor = new HashMap<Interceptor, Matcher<Definition>>();
        if (matcher != null && matcher.matches(definition)) {
            List<Matcher<Definition>> mList = new ArrayList<Matcher<Definition>>();
            AbstractMatcher.getSuperMatcher(matcher, mList);
            for (Matcher<Definition> obj : mList) {
                List<Interceptor> icptList = new ArrayList<Interceptor>();
                getInterceptorFromMatcher(obj, icptList);
                for (Interceptor inter : icptList) {
                    mapMatcherInterceptor.put(inter, obj);
                }
            }
            if (mapMatcherInterceptor.size() != 0) {
                Pair<Class<?>[], Object[]> pairCons = findArgsOfDefaultConstructor(definition);
                Class<?>[] argTypes = pairCons.getKeyPair();
                Object[] args = pairCons.getValuePair();
                InstantiationSelector selector = new InstantiationSelectorImpl(type, targetClass, mappingName, definition, argTypes, args, mapMatcherInterceptor);
                selector.setCachingList(cachingList);
                try {
                    return super.getBeanInstance(selector);
                } catch (Throwable e) {
                    if (log.isFatalEnabled()) {
                        log.fatal("Could not instantiate bean instance!", e);
                    }
                }
            } else {
                result = super.getBeanInstance(type, targetClass, mappingName, definition);
            }
        } else {
            InstantiationSelector selector = new InstantiationSelectorImpl(type, targetClass, mappingName, definition, null, null, mapMatcherInterceptor);
            selector.setCachingList(cachingList);
            try {
                return super.getBeanInstance(selector);
            } catch (Throwable e) {
                if (log.isFatalEnabled()) {
                    log.fatal("Could not instantiate bean instance!", e);
                }
            }
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void init(List<Map<String, Object>> OLArray) {
        List<ObjectBindingInterceptor> obiList = new ArrayList<ObjectBindingInterceptor>();
        for (Map<String, Object> optionsList : OLArray) {
            obiList.addAll((Collection<? extends ObjectBindingInterceptor>) optionsList.get(AbstractConfig.OBJECT_BINDING_INTERCEPTOR_LIST));
        }
        for (int i = 0; i < obiList.size(); i++) {
            ObjectBindingInterceptor obi = obiList.get(i);
            registers(obi.getMatchers(), obi.getInterceptor());
        }
        super.init(OLArray);
    }

    @Override
    public synchronized Matcher<Definition> getCachedMatcherOf(Definition definition) {
        return this.matcherCache.get(definition);
    }

    @Override
    public synchronized void getInterceptorFromMatcher(Matcher<Definition> matcher, List<Interceptor> result) {
        Assertor.notNull(result);
        Assertor.notNull(matcher);
        if (isRegisteredMatcher(matcher)) {
            List<Object> list = this.interceptorList.get(matcher);
            if (list != null) {
                for (Object icpt : list) {
                    Interceptor interceptor = null;
                    if (ReflectUtils.isCast(String.class, icpt)) {
                        Object obj = getBean((String) icpt);
                        if (obj != null && ReflectUtils.isCast(Interceptor.class, obj)) interceptor = (Interceptor) obj; else {
                            throw new InterceptionException("The registered object is not an instance of '" + Interceptor.class + "'!");
                        }
                    } else {
                        interceptor = (Interceptor) icpt;
                    }
                    if (interceptor != null && !result.contains(interceptor)) {
                        result.add(interceptor);
                    } else throw new InterceptionException("The registered interceptor must not be null !");
                }
            }
        }
    }

    @Override
    public Interceptor[] getInterceptorsFromMatcher(List<Matcher<Definition>> matchers) {
        Assertor.notNull(matchers);
        List<Interceptor> result = new ArrayList<Interceptor>();
        for (Matcher<Definition> matcher : matchers) {
            getInterceptorFromMatcher(matcher, result);
        }
        return result.toArray(new Interceptor[result.size()]);
    }

    @Override
    public Map<Definition, Matcher<Definition>> getMatcherCache() {
        return this.matcherCache;
    }

    @Override
    public synchronized List<Matcher<Definition>> getMatcherOf(Object interceptor) {
        List<Matcher<Definition>> list = new ArrayList<Matcher<Definition>>();
        for (Entry<Matcher<Definition>, List<Object>> entry : this.interceptorList.entrySet()) {
            if (entry.getValue().contains(interceptor)) {
                list.add(entry.getKey());
            }
        }
        return list;
    }

    @Override
    public synchronized boolean isRegistered(Matcher<Definition> matcher, Object interceptor) {
        Assertor.notNull(interceptor);
        Assertor.notNull(matcher);
        if (!this.interceptorList.containsKey(matcher)) {
            return false;
        }
        List<Object> list = this.interceptorList.get(matcher);
        if (list == null) {
            this.interceptorList.remove(matcher);
            return false;
        } else {
            if (list.contains(interceptor)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public synchronized boolean isRegisteredMatcher(Matcher<Definition> matcher) {
        Assertor.notNull(matcher);
        if (this.interceptorList.containsKey(matcher)) {
            return true;
        }
        return false;
    }

    @Override
    public synchronized void refreshMatcherCache(Definition definition) {
        Assertor.notNull(definition);
        this.matcherCache.remove(definition);
        for (Matcher<Definition> imatcher : this.interceptorList.keySet()) {
            if (imatcher != null && imatcher.matches(definition)) {
                if (this.matcherCache.containsKey(definition)) {
                    Matcher<Definition> current = this.matcherCache.get(definition);
                    this.matcherCache.put(definition, current.and(imatcher));
                } else {
                    this.matcherCache.put(definition, imatcher);
                }
            }
        }
    }

    @Override
    public synchronized void registers(Matcher<Definition>[] matchers, Object interceptor) {
        Assertor.notNull(interceptor);
        Assertor.notNull(matchers);
        for (int i = 0; i < matchers.length; i++) {
            Matcher<Definition> matcher = matchers[i];
            if (isRegisteredMatcher(matcher)) {
                if (isRegistered(matcher, interceptor)) {
                    throw new InterceptionException("This instantiation interceptor is registered !");
                } else {
                    this.interceptorList.get(matcher).add(interceptor);
                }
            } else {
                List<Object> list = new ArrayList<Object>();
                list.add(interceptor);
                this.interceptorList.put(matcher, list);
            }
        }
    }

    @Override
    public synchronized void unregisters(Matcher<Definition> matcher) {
        Assertor.notNull(matcher);
        this.interceptorList.remove(matcher);
    }

    @Override
    public synchronized void unregisters(Object interceptor) {
        Assertor.notNull(interceptor);
        for (Entry<Matcher<Definition>, List<Object>> entry : this.interceptorList.entrySet()) {
            if (entry.getValue().contains(interceptor)) {
                entry.getValue().remove(interceptor);
                if (entry.getValue().isEmpty()) {
                    this.interceptorList.remove(entry.getKey());
                }
            } else continue;
        }
    }
}
