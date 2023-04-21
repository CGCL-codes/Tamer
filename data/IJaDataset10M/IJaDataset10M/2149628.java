package com.ss4o.core.security;

/**
 * @author kakusuke
 *
 */
public interface UserAttributeLoader {

    String getKey();

    Object load(String userId);
}
