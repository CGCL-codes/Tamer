package org.jdiameter.client.impl.annotation.internal;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * @author erick.svenson@yahoo.com
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class Storage {

    private Map<Class<?>, ClassInfo> annotations = new ConcurrentHashMap<Class<?>, ClassInfo>();

    public final synchronized ClassInfo getClassInfo(Class<?> _class) {
        ClassInfo info = annotations.get(_class);
        if (info == null) {
            info = new ClassInfo(this, _class);
            annotations.put(_class, info);
        }
        return info;
    }

    public final synchronized void clear() {
        annotations.clear();
    }
}
