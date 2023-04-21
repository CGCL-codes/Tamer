package edu.uiuc.ncsa.security.config;

/**
 * Interface fronting properties. All a component needs to do is query based on
 * <p>Created by Jeff Gaynor<br>
 * on 11/29/11 at  2:38 PM
 * @deprecated
 */
public interface OldNSPropertyInterface {

    public String get(String ns, String key);

    public void put(String ns, String key, Object value);
}
