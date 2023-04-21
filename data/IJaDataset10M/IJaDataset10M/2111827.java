package com.google.code.gwt.storage.client;

import com.google.code.gwt.storage.client.impl.StorageImpl;
import com.google.gwt.core.client.GWT;

/**
 * Implements the HTML5 Storage interface.
 * 
 * <p>
 * You can obtain a Storage by either invoking {@link #getLocalStorage()} or
 * {@link #getSessionStorage()}.
 * </p>
 * 
 * <p>
 * If Web Storage is NOT supported in the browser, these methods return
 * <code>null</code>.
 * </p>
 * 
 * @see <a href="http://www.w3.org/TR/webstorage/#storage-0">W3C Web Storage -
 *      Storage</a>
 * @see <a
 *      href="http://devworld.apple.com/safari/library/documentation/iPhone/Conceptual/SafariJSDatabaseGuide/Name-ValueStorage/Name-ValueStorage.html">Safari
 *      Client-Side Storage and Offline Applications Programming Guide -
 *      Key-Value Storage</a>
 * @see <a href="http://quirksmode.org/dom/html5.html#t00">Quirksmode.org -
 *      HTML5 Compatibility - Storage</a>
 * @see <a
 *      href="http://code.google.com/p/gwt-mobile-webkit/wiki/StorageApi">Wiki -
 *      Quickstart Guide</a>
 * @author bguijt
 */
public final class Storage {

    private static final StorageImpl impl = GWT.create(StorageImpl.class);

    private static Storage localStorage;

    private static Storage sessionStorage;

    private final String storage;

    /**
   * This class can never be instantiated externally. Use
   * {@link #getLocalStorage()} or {@link #getSessionStorage()} instead.
   */
    private Storage(String storage) {
        this.storage = storage;
    }

    /**
   * Returns <code>true</code> if the Storage API (both localStorage and
   * sessionStorage) is supported on the running platform.
   */
    public static boolean isSupported() {
        return isLocalStorageSupported() && isSessionStorageSupported();
    }

    /**
   * Returns <code>true</code> if the <code>localStorage</code> part of the
   * Storage API is supported on the running platform.
   */
    public static boolean isLocalStorageSupported() {
        return impl.isLocalStorageSupported();
    }

    /**
   * Returns <code>true</code> if the <code>sessionStorage</code> part of the
   * Storage API is supported on the running platform.
   */
    public static boolean isSessionStorageSupported() {
        return impl.isSessionStorageSupported();
    }

    /**
   * Returns a Local Storage.
   * 
   * <p>
   * The returned storage is associated with the <a
   * href="http://www.w3.org/TR/html5/browsers.html#origin">origin</a> of the
   * Document.
   * </p>
   * 
   * @see <a href="http://www.w3.org/TR/webstorage/#dom-localstorage">W3C Web
   *      Storage - localStorage</a>
   * @return the localStorage instance, or <code>null</code> if Web Storage is
   *         NOT supported.
   */
    public static Storage getLocalStorage() {
        if (isLocalStorageSupported()) {
            if (localStorage == null) {
                localStorage = new Storage(StorageImpl.LOCAL_STORAGE);
            }
            return localStorage;
        }
        return null;
    }

    /**
   * Returns a Session Storage.
   * 
   * <p>
   * The returned storage is associated with the current <a href=
   * "http://www.w3.org/TR/html5/browsers.html#top-level-browsing-context"
   * >top-level browsing context</a>.
   * </p>
   * 
   * @see <a href="http://www.w3.org/TR/webstorage/#dom-sessionstorage">W3C Web
   *      Storage - sessionStorage</a>
   * @return the sessionStorage instance, or <code>null</code> if Web Storage is
   *         NOT supported.
   */
    public static Storage getSessionStorage() {
        if (isSessionStorageSupported()) {
            if (sessionStorage == null) {
                sessionStorage = new Storage(StorageImpl.SESSION_STORAGE);
            }
            return sessionStorage;
        }
        return null;
    }

    /**
   * Registers an event handler for StorageEvents.
   * 
   * @see <a href="http://www.w3.org/TR/webstorage/#the-storage-event">W3C Web
   *      Storage - the storage event</a>
   * @param handler
   */
    public static void addStorageEventHandler(StorageEventHandler handler) {
        impl.addStorageEventHandler(handler);
    }

    /**
   * De-registers an event handler for StorageEvents.
   * 
   * @see <a href="http://www.w3.org/TR/webstorage/#the-storage-event">W3C Web
   *      Storage - the storage event</a>
   * @param handler
   */
    public static void removeStorageEventHandler(StorageEventHandler handler) {
        impl.removeStorageEventHandler(handler);
    }

    /**
   * Returns the number of items in this Storage.
   * 
   * @return number of items in this Storage
   * @see <a href="http://www.w3.org/TR/webstorage/#dom-storage-l">W3C Web
   *      Storage - Storage.length()</a>
   */
    public int getLength() {
        return impl.getLength(storage);
    }

    /**
   * Returns the key at the specified index.
   * 
   * @param index the index of the key
   * @return the key at the specified index in this Storage
   * @see <a href="http://www.w3.org/TR/webstorage/#dom-storage-key">W3C Web
   *      Storage - Storage.key(n)</a>
   */
    public String key(int index) {
        return impl.key(storage, index);
    }

    /**
   * Returns the item in the Storage associated with the specified key.
   * 
   * @param key the key to a value in the Storage
   * @return the value associated with the given key
   * @see <a href="http://www.w3.org/TR/webstorage/#dom-storage-getitem">W3C Web
   *      Storage - Storage.getItem(k)</a>
   */
    public String getItem(String key) {
        return impl.getItem(storage, key);
    }

    /**
   * Sets the value in the Storage associated with the specified key to the
   * specified data.
   * 
   * @param key the key to a value in the Storage
   * @param data the value associated with the key
   * @see <a href="http://www.w3.org/TR/webstorage/#dom-storage-setitem">W3C Web
   *      Storage - Storage.setItem(k,v)</a>
   */
    public void setItem(String key, String data) {
        impl.setItem(storage, key, data);
    }

    /**
   * Removes the item in the Storage associated with the specified key.
   * 
   * @param key the key to a value in the Storage
   * @see <a href="http://www.w3.org/TR/webstorage/#dom-storage-removeitem">W3C
   *      Web Storage - Storage.removeItem(k)</a>
   */
    public void removeItem(String key) {
        impl.removeItem(storage, key);
    }

    ;

    /**
   * Removes all items in the Storage.
   * 
   * @see <a href="http://www.w3.org/TR/webstorage/#dom-storage-clear">W3C Web
   *      Storage - Storage.clear()</a>
   */
    public void clear() {
        impl.clear(storage);
    }
}
