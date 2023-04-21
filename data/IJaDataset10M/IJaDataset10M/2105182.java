package com.reserveamerica.elastica.cluster;

/**
 * The TransientValue is a value that is valid only up until its expiration time, after which the value is null.
 * 
 * @author BStasyszyn
 *
 */
public interface TransientValue<T> {

    /**
   * The value. Null is returned after the expiration time.
   * 
   * @return - The value or null.
   */
    T getValue();

    /**
   * The time that the value expires.
   * 
   * @return - The number of milliseconds since January 1, 1970, 00:00:00 GMT
   */
    long getExpirationTime();
}
