package com.teknokala.xtempore.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.teknokala.xtempore.NoSuchTemplateException;
import com.teknokala.xtempore.Template;
import com.teknokala.xtempore.TemplateCache;
import com.teknokala.xtempore.TemplateFactory;

public class MapCache implements TemplateCache {

    private final Map<String, Template> map;

    public MapCache() {
        this(new ConcurrentHashMap<String, Template>());
    }

    public MapCache(Map<String, Template> map) {
        super();
        this.map = map;
    }

    public Template getTemplate(TemplateFactory tf, String id) throws NoSuchTemplateException {
        Template ret = map.get(id);
        if (ret == null) {
            try {
                ret = tf.loadTemplate(id);
            } catch (Exception e) {
                throw new NoSuchTemplateException("Template couldn't be loaded: " + id, e);
            }
            map.put(id, ret);
        }
        return ret;
    }
}
