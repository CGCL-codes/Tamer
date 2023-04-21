package org.vosao.global;

/**
 * 
 * @author Alexander Oleynik
 *
 */
public interface PageCache {

    void put(String url, String language, String content, String contentType);

    PageCacheItem get(String url, String language);

    void remove(String url);

    boolean contains(String url);
}
