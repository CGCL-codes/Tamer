package com.magic.magicstore.core;

import java.io.Serializable;
import java.util.*;
import com.magic.magicstore.core.container.Factory;
import com.magic.magicstore.core.container.MagicStoreSpringFactory;

/**
 * The class <code>ServerContext</code><BR>
 * ʵ��ServerContext����
 * 
 * @author madawei
 * @version 0.1 2011-7-18
 * @see
 * @since Ver0.1
 */
public class ServerContext implements Serializable {

    /** ���Context��Ϣ */
    private static final ThreadLocal<Map<String, Object>> context = new ThreadLocal<Map<String, Object>>() {

        protected synchronized Map<String, Object> initialValue() {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put(SESSION, new Session());
            map.put(APPLICATION, new Application());
            return map;
        }
    };

    private static final Factory factory = MagicStoreSpringFactory.getInstance();

    private static final String CONTEXT_NAME = "com.magic.magicstore.core.ServerContext.name";

    private static final String SESSION = "com.magic.magicstore.core.ServerContext.session";

    private static final String APPLICATION = "com.magic.magicstore.core.ServerContext.application";

    /** ���context���� */
    private Map<String, Object> getContext() {
        return context.get();
    }

    private Object get(String key) {
        return getContext().get(key);
    }

    private void put(String key, Object value) {
        getContext().put(key, value);
    }

    public Application getApplication() {
        return (Application) get(APPLICATION);
    }

    public void setName(String name) {
        put(CONTEXT_NAME, name);
    }

    public String getName() {
        return (String) get(CONTEXT_NAME);
    }

    public Session getSession() {
        return (Session) get(SESSION);
    }

    public static Factory getFactory() {
        return factory;
    }
}
