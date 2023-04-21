package org.atlantal.api.portal.util;

/**
 * @author f.masurel
 */
public interface NamespaceMapper {

    /**
     * @param namespace namespace
     * @param name name
     * @return encoded
     */
    String encode(String namespace, String name);

    /**
     * @param ns1 ns1
     * @param ns2 ns2
     * @param name name
     * @return encoded
     */
    String encode(String ns1, String ns2, String name);

    /**
     * @param ns ns
     * @param name name
     * @return decoded
     */
    String decode(String ns, String name);
}
