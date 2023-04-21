package javax.util.jcache;

/**
 * This class defines the attributes an object in the cache can have.
 * @deprecated removed with no replacement.
 */
public interface Attributes {

    /** Event wich is fired when invalidation of an cacheobject is fired. */
    public static final int INVALIDATE_EVENT = 0;

    /**
     * Indicates the object is distributed, updates and invalidations are
     * distributed to other processes. Default is to not distribute changes.
     */
    public static final long DISTRIBUTE = 1;

    /**
     * Indicates to not flush the object from the cache if the object is
     * distributed and the cache is isolated from other caches. Default is to
     * flush the object. This flag is ignored if a "time to live" is specified,
     * or the object is local.
     */
    public static final long NOFLUSH = 2;

    /**
     * Indicates a reply should be sent from the remote caches if this object is
     * updated or invalidated. The default is no reply. If the object is local,
     * this flag is ignored.
     */
    public static final long REPLY = 4;

    /**
     * Indicates that the updates to this object should be synchronized. If this
     * flag is set, only the owner of an object can update or invalidate the
     * object. The default is no synchronization.
     */
    public static final long SYNCHRONIZE = 8;

    /**
     * Indicates the object shoulod be spooled to disk when the object is being
     * removed from the memory cache because of space limitations. This flag is
     * only valid for memory objects.
     */
    public static final long SPOOL = 16;

    /**
     * Indicates the group object should be destroyed when the associated time
     * to live expires. In the default case only the child objects are
     * invalidated. The group remains valid.
     */
    public static final long GROUP_TTL_DESTROY = 32;

    /**
     * Indicates the object was created in the cache and can't be recreated if
     * removed from the cache. Original objects don't get removed from the cache
     * even when they are not referenced. Original objects must be invalidated
     * before they get removed fromthe cache.
     */
    public static final long ORIGINAL = 64;

    /**
     * Is used to specify wich attributes should be set in the attributes
     * object. The different attributes wich is valid is defined as public
     * static variables in the {@link Attributes}class.
     * 
     * @param theFlags The attributes to set. the attributes may be OR-ed
     *            together. I.e. Attributes.DISTRIBUTE | Attributes.SYNCHRONIZE
     *            Invalid flags are silently ignored. To reset all flags you use
     *            0 as a parameter. I.e. setFlags(0)
     */
    public abstract void setFlags(final long theFlags);

    /**
     * Will associate a loader object with this object.
     * 
     * @param aLoader The loader to set. This parameter can be null.
     */
    public abstract void setLoader(final CacheLoader aLoader);

    /**
     * Sets the version attribute. Is only maintained for user convenience. It
     * is not used internally by the cache.
     * 
     * @param aVersion the version number to set.
     */
    public abstract void setVersion(final long aVersion);

    /**
     * Will set the maximum time the associated cache object will stay in the
     * cache before it is invalidated. The time starts when the object is loaded
     * into the cache(by the {@link CacheLoader}object or put by the
     * {@link CacheAccess#replace(Object, Object)}) or when the time to live
     * attribute is set by the {@link CacheLoader#setAttributes(Object,
     * Attributes)} method.
     * 
     * @param ttl the time to live in seconds. The {@link #timeToSeconds(int,
     *            int, int, int)} can be used to convert days, hours, minutes to
     *            seconds.
     * 
     * @throws InvalidArgumentException if a negative value for ttl is supplied.
     */
    public abstract void setTimeToLive(final long ttl) throws InvalidArgumentException;

    /**
     * Will set the maximum time the associated cache object will stay in the
     * cache before it is invalidated. For regions and groups, this will
     * establish a default time to live that is applied individually to each
     * member in the group or region. It will not cause the entire group or
     * region to time out as a whole. For individual objects, the default time
     * to live is equivalent with time to live. If both are set the default time
     * to live is ignored. The time starts when the object is loaded into the
     * cache(by the {@link CacheLoader}objector put by the {@link
     * CacheAccess#replace(Object, Object)}) or when the time to live attribute
     * is set by the {@link CacheLoader#setAttributes(Object,Attributes)}
     * method.
     * 
     * @param ttl the time to live in seconds. The {@link #timeToSeconds(int,
     *            int, int, int)} can be used to convert days, hours, minutes to
     *            secounds.
     * 
     * @throws InvalidArgumentException if a negative value for ttl is supplied.
     */
    public abstract void setDefaultTimeToLive(final long ttl) throws InvalidArgumentException;

    public abstract long getDefaultTimeToLive();

    /**
     * sets the maximum time the associated cache object will remain in the
     * cache without being referenced before it is invalidated.
     * 
     * @param idle is in seconds. The {@link #timeToSeconds(int, int, int,int)}
     *            can be used to convert days, hours, minutes to secounds.
     * 
     * @throws InvalidArgumentException if a negative value for idle is
     *             supplied.
     */
    public abstract void setIdleTime(final long idle) throws InvalidArgumentException;

    /**
     * Register an event listener object to be executed when the specified event
     * occurs with relationship to the associated object. Currently the only
     * invalidate event being monitored is Attributes.INVALIDATE_EVENT. If
     * invalid parameters are passed such as invalid events, or null as
     * listener, this method silently returns without doing any changes to this
     * object.
     * 
     * @param event The event to listen for.
     * @param aListener the listener to fire when the event occurs.
     * 
     *@todo Should these Attributes only have one Listener, or should several be
     * supported?
     */
    public abstract void setListener(final int event, final CacheEventListener aListener);

    /**
     * Is used to specify the size in bytes of the object being cached. This is
     * used to determine when the cache capacity is reached. If the cache is not
     * using object size to determine the capacity (It can also use object
     * counts) this value is ignored.
     * 
     * @param aSize the size to be set. if this parameter is smaller than zero,
     *            this method silently returns.
     */
    public abstract void setSize(final int aSize);

    /**
     * returns the size of the object. this size is set by the {@link
     * #setSize(int)} method, or in the case of StreamAccess objects, the size
     * is calculated by the cache.
     * @todo create and implement an online size calculator.
     * @return the size of the object, or 0 if the size has not been set.
     */
    public abstract int getSize();

    /**
     * Checks wether the flags are set or not.
     * 
     * @param theFlags the flags to be checked. may be OR-ed together, for wich
     *            this method will return true only if all flags are set.
     * 
     * @return true if the specified attribute is set, false otherwise.
     */
    public abstract boolean isSet(final long theFlags);

    /**
     * returns the time the object was loaded into the cache. The time is the
     * number of milliseconds since midnight, January 1, 1970 (UTC).
     * 
     * @return the time the object was loaded into the cache. The time is the
     *         number of milliseconds since midnight, January 1, 1970 (UTC).
     */
    public abstract long getCreateTime();

    /**
     * returns the {@link CacheLoader}attribute.
     * 
     * @return the {@link CacheLoader}attribute.
     */
    public abstract CacheLoader getLoader();

    /**
     * returns the current value of version.
     * 
     * @return the current value of version.
     */
    public abstract long getVersion();

    /**
     * returns the current value for the idle time interval.
     * 
     * @return the current value for the idle time interval.
     */
    public abstract long getIdleTime();

    /**
     * returns the current value for the time to live interval.
     * 
     * @return the current value for the time to live interval.
     */
    public abstract long getTimeToLive();

    /**
     * resets the Attributes to its default values. The attributes wich are
     * reset are expiration time attributes, time to live, default time to
     * live, idle time and event handlers.
     *
     * @todo This method should be package private,  thus this class should be
     *       moved to org.fjank.jcache, an interface should be extracted, and
     *       that should be public,  and in this package.
     */
    public abstract void reset();

    /**
     * Sets the createTime.
     * 
     * @param aCreateTime The createTime to set
     */
    public abstract void setCreateTime(final long aCreateTime);

    /**
     * Will convert the time specified into seconds.
     * 
     * @param days number of days.
     * @param hours number of hours.
     * @param minutes number of minutes.
     * @param seconds number of seconds.
     * 
     * @return the converted time in seconds.
     * 
     * @throws InvalidArgumentException if any of the parameters are negative
     *             values.
     */
    public long timeToSeconds(final int days, final int hours, final int minutes, final int seconds) throws InvalidArgumentException;

    /**
     * Gets the CacheEventListener.
     * @author Philippe Fajeau
     * @return The CacheEventListener.
     */
    public abstract CacheEventListener getListener();

    public void applyAttributes(Attributes attributes);
}
