package edu.vt.middleware.ldap.pool;

/**
 * Provides an interface for validating objects when they are in the pool.
 *
 * @param  <T>  type of object being pooled
 *
 * @author  Middleware Services
 * @version  $Revision: 2197 $ $Date: 2012-01-01 22:40:30 -0500 (Sun, 01 Jan 2012) $
 */
public interface Validator<T> {

    /**
   * Validate the supplied object.
   *
   * @param  t  object
   *
   * @return  whether validation was successful
   */
    boolean validate(T t);
}
