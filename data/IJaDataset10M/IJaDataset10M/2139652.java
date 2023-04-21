package com.cirnoworks.cis.impl.monkey3;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import com.cirnoworks.common.ResourceFetcher;
import com.jme3.asset.AssetInfo;
import com.jme3.asset.AssetKey;
import com.jme3.asset.AssetLocator;
import com.jme3.asset.AssetManager;

/**
 * @author Cloudee
 * 
 */
public class ResourceFetcherLocater implements AssetLocator {

    private String prefix;

    private static ResourceFetcher rf;

    public static void setResourceFetcher(ResourceFetcher rf) {
        ResourceFetcherLocater.rf = rf;
    }

    /**
	 * @param rf
	 */
    public ResourceFetcherLocater() {
        super();
        if (rf == null) {
            throw new NullPointerException("Must run ResourceFetcherLocater.setResourceFetcher first!");
        }
    }

    @Override
    public void setRootPath(String rootPath) {
        if (rootPath == null || "".equals(rootPath)) {
            prefix = "/";
        } else {
            if (!rootPath.endsWith("/")) {
                prefix = rootPath + "/";
            } else {
                prefix = rootPath;
            }
        }
    }

    @Override
    public AssetInfo locate(AssetManager manager, AssetKey key) {
        String name = key.getName();
        URL url = rf.getResource(prefix + name);
        if (url == null) {
            return null;
        } else {
            return new RFAssetInfo(manager, key, url);
        }
    }

    class RFAssetInfo extends AssetInfo {

        private final URL url;

        /**
		 * @param manager
		 * @param key
		 */
        public RFAssetInfo(AssetManager manager, AssetKey key, URL url) {
            super(manager, key);
            this.url = url;
        }

        @Override
        public InputStream openStream() {
            try {
                return url.openStream();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
