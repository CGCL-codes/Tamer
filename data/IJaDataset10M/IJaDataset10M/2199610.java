package com.mapbased.sfw.site;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import com.mapbased.sfw.store.PathData;

public class SiteConfig {

    /**
	 * 为静态资源添加前缀，方便cdn缓存
	 */
    public String resPrefix;

    public ConcurrentMap<String, String> vars = new ConcurrentHashMap<String, String>();

    public void update(PathData pd) {
        String key = pd.path;
        String value = pd.getStrValue();
        if (key.equals("resPrefix")) {
            this.resPrefix = value;
        }
        vars.put(key, value);
    }
}
