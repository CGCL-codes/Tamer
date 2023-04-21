package org.apache.ws.security;

/**
 * Interface used for generating unique ID's for elements that need
 * to be addressed by their wsu:Id attribute
 */
public interface WsuIdAllocator {

    String createId(String prefix, Object o);

    String createSecureId(String prefix, Object o);
}
