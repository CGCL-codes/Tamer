package org.openuss.system;

/**
 * <p>
 * Spring Service base class for <code>org.openuss.system.SystemService</code>,
 * provides access to all services and entities referenced by this service.
 * </p>
 *
 * @see org.openuss.system.SystemService
 */
public abstract class SystemServiceBase implements org.openuss.system.SystemService {

    private org.openuss.system.SystemPropertyDao systemPropertyDao;

    /**
     * Sets the reference to <code>systemProperty</code>'s DAO.
     */
    public void setSystemPropertyDao(org.openuss.system.SystemPropertyDao systemPropertyDao) {
        this.systemPropertyDao = systemPropertyDao;
    }

    /**
     * Gets the reference to <code>systemProperty</code>'s DAO.
     */
    protected org.openuss.system.SystemPropertyDao getSystemPropertyDao() {
        return this.systemPropertyDao;
    }

    /**
     * @see org.openuss.system.SystemService#persistProperties(java.util.Collection)
     */
    public void persistProperties(java.util.Collection properties) {
        try {
            this.handlePersistProperties(properties);
        } catch (Throwable th) {
            throw new org.openuss.system.SystemServiceException("Error performing 'org.openuss.system.SystemService.persistProperties(java.util.Collection properties)' --> " + th, th);
        }
    }

    /**
      * Performs the core logic for {@link #persistProperties(java.util.Collection)}
      */
    protected abstract void handlePersistProperties(java.util.Collection properties) throws java.lang.Exception;

    /**
     * @see org.openuss.system.SystemService#persistProperty(org.openuss.system.SystemProperty)
     */
    public void persistProperty(org.openuss.system.SystemProperty property) {
        try {
            this.handlePersistProperty(property);
        } catch (Throwable th) {
            throw new org.openuss.system.SystemServiceException("Error performing 'org.openuss.system.SystemService.persistProperty(org.openuss.system.SystemProperty property)' --> " + th, th);
        }
    }

    /**
      * Performs the core logic for {@link #persistProperty(org.openuss.system.SystemProperty)}
      */
    protected abstract void handlePersistProperty(org.openuss.system.SystemProperty property) throws java.lang.Exception;

    /**
     * @see org.openuss.system.SystemService#getProperties()
     */
    public java.util.Collection getProperties() {
        try {
            return this.handleGetProperties();
        } catch (Throwable th) {
            throw new org.openuss.system.SystemServiceException("Error performing 'org.openuss.system.SystemService.getProperties()' --> " + th, th);
        }
    }

    /**
      * Performs the core logic for {@link #getProperties()}
      */
    protected abstract java.util.Collection handleGetProperties() throws java.lang.Exception;

    /**
     * @see org.openuss.system.SystemService#getProperty(java.lang.Long)
     */
    public org.openuss.system.SystemProperty getProperty(java.lang.Long id) {
        try {
            return this.handleGetProperty(id);
        } catch (Throwable th) {
            throw new org.openuss.system.SystemServiceException("Error performing 'org.openuss.system.SystemService.getProperty(java.lang.Long id)' --> " + th, th);
        }
    }

    /**
      * Performs the core logic for {@link #getProperty(java.lang.Long)}
      */
    protected abstract org.openuss.system.SystemProperty handleGetProperty(java.lang.Long id) throws java.lang.Exception;

    /**
     * @see org.openuss.system.SystemService#getProperty(java.lang.String)
     */
    public org.openuss.system.SystemProperty getProperty(java.lang.String name) {
        try {
            return this.handleGetProperty(name);
        } catch (Throwable th) {
            throw new org.openuss.system.SystemServiceException("Error performing 'org.openuss.system.SystemService.getProperty(java.lang.String name)' --> " + th, th);
        }
    }

    /**
      * Performs the core logic for {@link #getProperty(java.lang.String)}
      */
    protected abstract org.openuss.system.SystemProperty handleGetProperty(java.lang.String name) throws java.lang.Exception;

    /**
     * @see org.openuss.system.SystemService#getInstanceIdentity()
     */
    public java.lang.Long getInstanceIdentity() {
        try {
            return this.handleGetInstanceIdentity();
        } catch (Throwable th) {
            throw new org.openuss.system.SystemServiceException("Error performing 'org.openuss.system.SystemService.getInstanceIdentity()' --> " + th, th);
        }
    }

    /**
      * Performs the core logic for {@link #getInstanceIdentity()}
      */
    protected abstract java.lang.Long handleGetInstanceIdentity() throws java.lang.Exception;

    /**
     * @see org.openuss.system.SystemService#setInstanceIdentity(java.lang.Long)
     */
    public void setInstanceIdentity(java.lang.Long identity) {
        try {
            this.handleSetInstanceIdentity(identity);
        } catch (Throwable th) {
            throw new org.openuss.system.SystemServiceException("Error performing 'org.openuss.system.SystemService.setInstanceIdentity(java.lang.Long identity)' --> " + th, th);
        }
    }

    /**
      * Performs the core logic for {@link #setInstanceIdentity(java.lang.Long)}
      */
    protected abstract void handleSetInstanceIdentity(java.lang.Long identity) throws java.lang.Exception;

    /**
     * Gets the current <code>principal</code> if one has been set,
     * otherwise returns <code>null</code>.
     *
     * @return the current principal
     */
    protected java.security.Principal getPrincipal() {
        return org.andromda.spring.PrincipalStore.get();
    }
}
