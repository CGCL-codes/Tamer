package com.caojin.propertyInjector;

import com.caojin.propertyInjector.locationResolver.ClassPathLocationResolver;
import com.caojin.propertyInjector.locationResolver.FileSyslocationResolver;
import com.caojin.propertyInjector.locationResolver.LocationResolver;

/**
 *  url解析器
 * @author caojin
 */
public abstract class UrlResolver {

    public static final String CLASSPATH_PREFIX = "classpath:";

    public static final String FILESYS_PREFIX = "file:";

    public static final String PROTOCOL_PATH_SPLITOR = ":";

    public static final String RECURSIVE_SYMBOL = "..";

    public static final String PATH_SPLITOR = "/";

    public static final String PACKAGE_SPLITOR = ".";

    public static final String DEFAULT_URL = "classpath:/props.properties";

    protected String url;

    public UrlResolver() {
        this(DEFAULT_URL);
    }

    public UrlResolver(String url) {
        this.url = url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private boolean validateUrl() {
        return this.url.startsWith(CLASSPATH_PREFIX) || this.url.startsWith(FILESYS_PREFIX);
    }

    public boolean isClassPath() {
        if (!validateUrl()) {
            throw new UrlInvaildException("Url Is Not Vaild: " + url);
        }
        return this.url.startsWith(CLASSPATH_PREFIX);
    }

    public boolean isFileSystem() {
        if (!validateUrl()) {
            throw new UrlInvaildException("Url Is Not Vaild: " + url);
        }
        return this.url.startsWith(FILESYS_PREFIX);
    }

    public LocationResolver resolveUrl() {
        int colonIndex = url.indexOf(PROTOCOL_PATH_SPLITOR);
        if (isClassPath()) {
            if (url.contains(RECURSIVE_SYMBOL)) {
                int resursiverIndex = url.indexOf(RECURSIVE_SYMBOL);
                String basePackage = url.substring(colonIndex + 1, resursiverIndex);
                String fileName = url.substring(resursiverIndex + 3);
                return new ClassPathLocationResolver(basePackage, fileName, true);
            } else {
                int lastPathSplitorIndex = url.lastIndexOf(PATH_SPLITOR);
                String basePackage = url.substring(colonIndex + 1, lastPathSplitorIndex);
                String fileName = url.substring(lastPathSplitorIndex + 1);
                return new ClassPathLocationResolver(basePackage, fileName);
            }
        } else if (isFileSystem()) {
            if (url.contains(RECURSIVE_SYMBOL)) {
                int resursiverIndex = url.indexOf(RECURSIVE_SYMBOL);
                String folder = url.substring(colonIndex + 1, resursiverIndex);
                String fileName = url.substring(resursiverIndex + 3);
                return new FileSyslocationResolver(folder, fileName, true);
            } else {
                int lastPathSplitorIndex = url.lastIndexOf(PATH_SPLITOR);
                String folder = url.substring(colonIndex + 1, lastPathSplitorIndex);
                String fileName = url.substring(lastPathSplitorIndex + 1);
                return new FileSyslocationResolver(folder, fileName);
            }
        } else {
            return dealWithOtherUrl();
        }
    }

    public abstract LocationResolver dealWithOtherUrl();

    @SuppressWarnings("serial")
    class UrlInvaildException extends RuntimeException {

        public UrlInvaildException(String msg) {
            super(msg);
        }
    }
}
