package ru.adv.mozart.framework;

import java.util.Map;
import ru.adv.cache.Cache;
import ru.adv.cache.CacheObject;
import ru.adv.io.InputOutputException;
import ru.adv.util.InputOutput;
import ru.adv.util.image.AutoImageInfoReader;
import ru.adv.util.image.ImageInfo;
import ru.adv.util.image.ImageInfoReadException;

/**
 * @version $Revision: 1.8 $
 */
public class ImageInfoReader extends AutoImageInfoReader {

    private Cache cache2;

    private String contextPath;

    public ImageInfoReader(String contextPath, Map<String, String> aliases, InputOutput base) {
        this(contextPath, aliases, base, null);
    }

    public ImageInfoReader(String contextPath, Map<String, String> aliases, InputOutput base, Cache cache) {
        super();
        this.cache2 = cache;
        this.contextPath = contextPath;
        setURLResolver(new AliasURLResolver(contextPath, aliases, base));
    }

    public ImageInfo doReadInfo(String url) throws ImageInfoReadException {
        if (!url.startsWith("/")) {
            url = this.contextPath + "/" + url;
        }
        ImageInfo result = null;
        if (cache2 != null) {
            CacheObject data;
            try {
                String key = resolveUrl(url);
                data = cache2.get(key);
                if (null == data) {
                    result = super.doReadInfo(url);
                    data = cache2.createCacheObject(key, result, getURLResolver().getInclude(url), Cache.NEVER_EXPIRES);
                    cache2.put(data);
                } else {
                    try {
                        result = (ImageInfo) data.getData();
                    } finally {
                        data.release();
                    }
                }
            } catch (InputOutputException e) {
                throw new ImageInfoReadException(e, url);
            }
        } else {
            result = super.doReadInfo(url);
        }
        return result;
    }

    private String resolveUrl(String url) throws InputOutputException {
        return getURLResolver().resolve(url);
    }
}
