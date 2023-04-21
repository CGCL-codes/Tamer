package edu.vt.middleware.ldap.pool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Contains functionality common to pool implementations.
 *
 * @param  <T>  type of object being pooled
 *
 * @author  Middleware Services
 * @version  $Revision: 2197 $ $Date: 2012-01-01 22:40:30 -0500 (Sun, 01 Jan 2012) $
 */
public abstract class AbstractPool<T> {

    /** Logger for this class. */
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /** Pool config. */
    private PoolConfig poolConfig;

    /** For activating pooled objects. */
    private Activator<T> activator;

    /** For passivating pooled objects. */
    private Passivator<T> passivator;

    /** For validating pooled objects. */
    private Validator<T> validator;

    /**
   * Returns the configuration for this pool.
   *
   * @return  pool config
   */
    public PoolConfig getPoolConfig() {
        return poolConfig;
    }

    /**
   * Sets the configuration for this pool.
   *
   * @param  pc  pool config
   */
    public void setPoolConfig(final PoolConfig pc) {
        poolConfig = pc;
    }

    /**
   * Returns the activator for this pool.
   *
   * @return  activator
   */
    public Activator<T> getActivator() {
        return activator;
    }

    /**
   * Sets the activator for this pool.
   *
   * @param  a  activator
   */
    public void setActivator(final Activator<T> a) {
        activator = a;
    }

    /**
   * Prepare the object to exit the pool for use.
   *
   * @param  t  pooled object
   *
   * @return  whether the object successfully activated
   */
    public boolean activate(final T t) {
        boolean success = false;
        if (activator == null) {
            success = true;
            logger.trace("no activator configured");
        } else {
            success = activator.activate(t);
            logger.trace("activation for {} = {}", t, success);
        }
        return success;
    }

    /**
   * Returns the passivator for this pool.
   *
   * @return  passivator
   */
    public Passivator<T> getPassivator() {
        return passivator;
    }

    /**
   * Sets the passivator for this pool.
   *
   * @param  p  passivator
   */
    public void setPassivator(final Passivator<T> p) {
        passivator = p;
    }

    /**
   * Prepare the object to enter the pool after use.
   *
   * @param  t  pooled object
   *
   * @return  whether the object successfully passivated
   */
    public boolean passivate(final T t) {
        boolean success = false;
        if (passivator == null) {
            success = true;
            logger.trace("no passivator configured");
        } else {
            success = passivator.passivate(t);
            logger.trace("passivation for {} = {}", t, success);
        }
        return success;
    }

    /**
   * Returns the validator for this pool.
   *
   * @return  validator
   */
    public Validator<T> getValidator() {
        return validator;
    }

    /**
   * Sets the validator for this pool.
   *
   * @param  v  validator
   */
    public void setValidator(final Validator<T> v) {
        validator = v;
    }

    /**
   * Verify the object is still viable for use in the pool.
   *
   * @param  t  pooled object
   *
   * @return  whether the object is viable
   */
    public boolean validate(final T t) {
        boolean success = false;
        if (validator == null) {
            success = true;
            logger.warn("validate called, but no validator configured");
        } else {
            success = validator.validate(t);
            logger.trace("validation for {} = {}", t, success);
        }
        return success;
    }
}
