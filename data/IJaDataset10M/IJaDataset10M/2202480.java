package com.icesoft.util.pooling;

import java.util.Map;
import java.util.Collections;
import java.util.LinkedHashMap;
import javax.faces.context.FacesContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class StringInternMapLRU {

    private static final Log log = LogFactory.getLog(StringInternMapLRU.class);

    private static final int DEFAULT_MAX_SIZE = 95000;

    private Map map;

    private int defaultSize;

    private String contextParam;

    private boolean disabled;

    public StringInternMapLRU() {
        this(DEFAULT_MAX_SIZE);
    }

    public StringInternMapLRU(int size) {
        this(size, "");
    }

    public StringInternMapLRU(String contextParam) {
        this(DEFAULT_MAX_SIZE, contextParam);
    }

    public StringInternMapLRU(int defaultSize, String contextParam) {
        this.defaultSize = defaultSize;
        this.contextParam = contextParam;
        this.disabled = false;
    }

    private void createMap() {
        int maxSize = defaultSize;
        if (contextParam != null) {
            String maxSizeParam = FacesContext.getCurrentInstance().getExternalContext().getInitParameter(contextParam);
            if (maxSizeParam != null && maxSizeParam.length() > 0) {
                int configuredMaxSize = 0;
                try {
                    configuredMaxSize = Integer.parseInt(maxSizeParam);
                } catch (Exception e) {
                    log.error("Couldn't parse context-param: " + contextParam + ".", e);
                }
                if (configuredMaxSize > 0) {
                    maxSize = configuredMaxSize;
                } else {
                    disabled = true;
                    return;
                }
            }
        }
        int capacity = ((maxSize * 4) / 3) + 10;
        final int finalSize = maxSize;
        map = Collections.synchronizedMap(new LinkedHashMap(capacity, 0.75f, true) {

            protected boolean removeEldestEntry(Map.Entry eldest) {
                return size() > finalSize;
            }
        });
    }

    public Object get(Object value) {
        if (map == null && !disabled) {
            synchronized (this) {
                if (map == null && !disabled) {
                    createMap();
                }
            }
        }
        if (disabled) {
            return value;
        }
        if (value == null) {
            return null;
        }
        Object pooledValue = map.get(value);
        if (pooledValue != null) {
            return pooledValue;
        } else {
            map.put(value, value);
            return value;
        }
    }

    public int getSize() {
        if (map == null || disabled) {
            return 0;
        } else {
            return map.size();
        }
    }
}
