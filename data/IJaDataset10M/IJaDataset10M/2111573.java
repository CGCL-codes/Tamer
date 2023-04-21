package org.apache.http.client;

import java.util.Date;
import java.util.List;
import org.apache.http.cookie.Cookie;

/**
 * This interface represents an abstract store for {@link Cookie}
 * objects.
 * 
 * @since 4.0
 */
public interface CookieStore {

    /**
     * Adds an {@link Cookie}, replacing any existing equivalent cookies.
     * If the given cookie has already expired it will not be added, but existing 
     * values will still be removed.
     * 
     * @param cookie the {@link Cookie cookie} to be added
     */
    void addCookie(Cookie cookie);

    /**
     * Returns all cookies contained in this store.
     * 
     * @return all cookies
     */
    List<Cookie> getCookies();

    /**
     * Removes all of {@link Cookie}s in this store that have expired by 
     * the specified {@link java.util.Date}. 
     * 
     * @return true if any cookies were purged.
     */
    boolean clearExpired(Date date);

    /**
     * Clears all cookies.
     */
    void clear();
}
