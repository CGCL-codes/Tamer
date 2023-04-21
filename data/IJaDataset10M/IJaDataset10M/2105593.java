package com.macvu.tiles;

import com.macvu.tiles.xmlDefinition.XmlCacheAttribute;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CacheInformation {

    boolean cacheEnabled;

    List cacheAttributes;

    List possibleCacheAttributes;

    String repositoryName;

    String repositoryFactory;

    String keyFactory;

    CacheComponentDefinition parent;

    public static final String DEFAULT_REPOSITORY_NAME = "DEFAULT_CACHE_TILE_REPOSITORY";

    public static final String DEFAULT_REPOSITORY_FACTORY = "com.macvu.tiles.cache.SingleLRUCacheServiceFactory";

    public static final String DEFAULT_KEY_FACTORY = "com.macvu.tiles.cache.SimpleCacheKeyFactory";

    public CacheInformation() {
        init();
    }

    public CacheInformation(CacheInformation info) {
        init();
        if (info != null) {
            cacheEnabled = info.getCacheEnabled();
            Iterator itr = info.getCacheAttributes().iterator();
            while (itr.hasNext()) {
                XmlCacheAttribute xmlAtt = (XmlCacheAttribute) itr.next();
                CacheAttribute attr = new CacheAttribute(xmlAtt);
                cacheAttributes.add(attr);
            }
            repositoryFactory = info.getRepositoryFactory();
            keyFactory = info.getKeyFactory();
        }
    }

    public CacheInformation(List cacheAttributes, boolean cacheEnabled) {
        init();
        this.cacheAttributes = cacheAttributes;
        this.cacheEnabled = cacheEnabled;
    }

    protected void init() {
        cacheEnabled = false;
        cacheAttributes = new ArrayList();
        possibleCacheAttributes = new ArrayList();
        repositoryFactory = DEFAULT_REPOSITORY_FACTORY;
        keyFactory = DEFAULT_KEY_FACTORY;
    }

    public boolean isCacheEnabled() {
        return cacheEnabled;
    }

    public void setCacheEnabled(boolean cacheEnabled) {
        this.cacheEnabled = cacheEnabled;
    }

    public boolean getCacheEnabled() {
        return cacheEnabled;
    }

    public List getCacheAttributes() {
        return cacheAttributes;
    }

    public void setCacheAttributes(List cacheAttributes) {
        this.cacheAttributes = cacheAttributes;
    }

    public boolean addCacheAttribute(CacheAttribute attribute) {
        if (isInCacheAttributes(attribute.getScope(), attribute.getName())) {
            return false;
        }
        return cacheAttributes.add(attribute);
    }

    public boolean removeCacheAttribute(CacheAttribute attribute) {
        return cacheAttributes.remove(attribute);
    }

    public void clearCacheAttribute() {
        cacheAttributes.clear();
    }

    public List getPossibleCacheAttributes() {
        return possibleCacheAttributes;
    }

    public void setPossibleCacheAttributes(List possibleCacheAttributes) {
        this.possibleCacheAttributes = possibleCacheAttributes;
    }

    public boolean addPossibleCacheAttribute(CacheAttribute attribute) {
        if (isInPossibleCacheAttributes(attribute.getScope(), attribute.getName())) {
            return false;
        }
        return possibleCacheAttributes.add(attribute);
    }

    public boolean removePossibleCacheAttribute(CacheAttribute attribute) {
        return possibleCacheAttributes.remove(attribute);
    }

    public void clearPossibleCacheAttribute() {
        possibleCacheAttributes.clear();
    }

    public String getRepositoryName() {
        if (repositoryName == null) {
            if (parent != null) {
                repositoryName = parent.getName();
            } else {
                repositoryName = DEFAULT_REPOSITORY_NAME;
            }
        }
        return repositoryName;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    public String getRepositoryFactory() {
        return repositoryFactory;
    }

    public void setRepositoryFactory(String repositoryFactory) {
        this.repositoryFactory = repositoryFactory;
    }

    public String getKeyFactory() {
        return keyFactory;
    }

    public void setKeyFactory(String keyFactory) {
        this.keyFactory = keyFactory;
    }

    public boolean isInCacheAttributes(String scope, String name) {
        boolean found = false;
        Iterator itr = cacheAttributes.iterator();
        while (!found && itr.hasNext()) {
            CacheAttribute attr = (CacheAttribute) itr.next();
            if (attr.getName().equals(name) && attr.getScope().equals(scope)) {
                found = true;
            }
        }
        return found;
    }

    public boolean isInPossibleCacheAttributes(String scope, String name) {
        boolean found = false;
        Iterator itr = possibleCacheAttributes.iterator();
        while (!found && itr.hasNext()) {
            CacheAttribute attr = (CacheAttribute) itr.next();
            if (attr.getName().equals(name) && attr.getScope().equals(scope)) {
                found = true;
            }
        }
        return found;
    }

    public boolean isDefault() {
        if (isCacheEnabled() == false && cacheAttributes.isEmpty() && DEFAULT_REPOSITORY_NAME.equals(repositoryName) && DEFAULT_REPOSITORY_FACTORY.equals(repositoryFactory) && DEFAULT_KEY_FACTORY.equals(keyFactory)) {
            return true;
        }
        return false;
    }

    public String toString() {
        return "{name=" + repositoryName + ", cacheFactory=" + repositoryFactory + ", keyFactory=" + keyFactory + ", enabled=" + cacheEnabled + ", cacheAttributes=" + cacheAttributes + ", possibleCacheAttributes=" + possibleCacheAttributes + "}\n";
    }

    public CacheComponentDefinition getParent() {
        return parent;
    }

    public void setParent(CacheComponentDefinition parent) {
        this.parent = parent;
    }
}
