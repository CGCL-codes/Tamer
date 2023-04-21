package org.apache.jetspeed.portal.portlets;

import org.apache.jetspeed.capability.CapabilityMap;
import org.apache.jetspeed.capability.CapabilityMapFactory;
import org.apache.jetspeed.om.registry.MediaTypeEntry;
import org.apache.jetspeed.om.registry.PortletEntry;
import org.apache.jetspeed.portal.BasePortletConfig;
import org.apache.jetspeed.portal.expire.Expire;
import org.apache.jetspeed.portal.expire.ExpireFactory;
import org.apache.jetspeed.portal.Portlet;
import org.apache.jetspeed.portal.PortletConfig;
import org.apache.jetspeed.portal.PortletException;
import org.apache.jetspeed.portal.PortletState;
import org.apache.jetspeed.services.persistence.PersistenceManager;
import org.apache.jetspeed.services.persistence.PortalPersistenceException;
import org.apache.jetspeed.portal.PortletInstance;
import org.apache.jetspeed.services.portletcache.Cacheable;
import org.apache.jetspeed.services.Registry;
import org.apache.jetspeed.services.logging.JetspeedLogFactoryService;
import org.apache.jetspeed.services.logging.JetspeedLogger;
import org.apache.jetspeed.util.JetspeedException;
import org.apache.jetspeed.util.MetaData;
import org.apache.jetspeed.util.MimeType;
import org.apache.jetspeed.util.JetspeedClearElement;
import org.apache.ecs.ConcreteElement;
import org.apache.turbine.services.cache.CachedObject;
import org.apache.turbine.services.cache.Refreshable;
import org.apache.turbine.util.RunData;
import java.util.Hashtable;
import java.util.Iterator;

/**
<p>
Should be used by most Portlets that wish to conform to default behavior
</p>

<p>
PERFORMANCE NOTE:

getContent returns a StringElement that was generated on setContent().  This is
used so that performance is increased since ECS does not have to work overtime
to generate output.
</p>

@author <A HREF="mailto:burton@apache.org">Kevin A. Burton</A>
@author <A HREF="mailto:raphael@apache.org">Rapha�l Luta</A>
@author <A HREF="mailto:sgala@apache.org">Santiago Gala</A>
@author <A HREF="mailto:paulsp@apache.org">Paul Spencer</A>
@author <A HREF="mailto:morciuch@apache.org">Mark Orciuch</A>
@version $Id: AbstractPortlet.java,v 1.65 2004/03/29 21:38:42 taylor Exp $
*/
public abstract class AbstractPortlet implements Portlet, PortletState, Cacheable, Refreshable {

    /**
     * Static initialization of the logger for this class
     */
    private static final JetspeedLogger logger = JetspeedLogFactoryService.getLogger(AbstractPortlet.class.getName());

    private boolean cacheable = true;

    private PortletConfig pc = null;

    /**
    Provide a required name for this Portlet
    */
    private String name = null;

    /**
    Provide a Unique Portlet ID
    */
    private String id = null;

    /**
    Cache handle for this object.
    */
    private String handle = "";

    /**
    Expiration time of object in milliseconds since the standard base time
    known as "the epoch", namely January 1, 1970, 00:00:00 GMT.
    */
    private Long expirationMillis = null;

    /**
    Holds instances of ConcreteElements (Portlet output/content)
    based on its current CapabilityMap.
    */
    protected Hashtable content = new Hashtable();

    /**
    The time this portlet was created.
    */
    private long creationTime;

    /**
     * Handle to cached object
     */
    private CachedObject cachedObject = null;

    /**
    */
    protected void clearContent() {
        this.content.clear();
    }

    /**
     */
    protected void setContent(ConcreteElement content) {
        this.setContent(content, CapabilityMapFactory.getDefaultCapabilityMap());
    }

    /**
    */
    protected void setContent(String content) {
        this.setContent(new JetspeedClearElement(content), CapabilityMapFactory.getDefaultCapabilityMap());
    }

    /**
    */
    protected void setContent(ConcreteElement content, CapabilityMap map) throws IllegalArgumentException {
        CapabilityMap mymap = map;
        if (mymap == null) {
            mymap = CapabilityMapFactory.getDefaultCapabilityMap();
        }
        ConcreteElement buffer = new JetspeedClearElement(content.toString());
        this.content.put(mymap.toString(), buffer);
    }

    /**
     * Usually called by caching system when portlet is marked as expired, but
     * has not be idle longer then TimeToLive.
     *
     * This method should be implement in cachable portlets
     */
    public void refresh() {
        logger.debug("AbstractPortlet - Refreshing " + this.getName());
    }

    /**
     * Is this portlet cacheable.  It is the portlet's responsability to
     * cache the content.
     *
     * @return <CODE>true</CODE> Cachable<BR>
     * <CODE>false</CODE> Not cachable
     */
    public boolean isCacheable() {
        return this.cacheable;
    }

    /**
     * Set cachable.  This should only be called in the portlet's init().
     *
     * @param cacheable <CODE>true</CODE> Portlet is cachable<BR>
     * <CODE>false</CODE> Portlet is NOT cachable
     */
    public void setCacheable(boolean cacheable) {
        this.cacheable = cacheable;
    }

    /**
     * Used by a Cacheable object to determine when it should expire itself from the cache.
     *
     * @return Expire
     */
    public Expire getExpire() {
        try {
            return ExpireFactory.getExpire(this, ExpireFactory.NO_EXPIRE);
        } catch (JetspeedException e) {
            logger.error("Exception", e);
            return null;
        }
    }

    /**
     * <p>Used by the cache to get a unique reference on what you want to add
     * and then retrieve in the future from the cache</p>
     *
     * <p>Most implementations should just call the CacheHandleManager with
     * the given params within the implementation and just return this.</p>
     *
     * @return Cache handle (key)
     */
    public final String getHandle() {
        return this.handle;
    }

    /**
     * Used by a Cacheable object to determine when it should
     * expire itself from the cache.
     *
     * @param handle Cache Handle
     *
     * @deprecated cacheable classes should now implement a static getHandle(config) method
     */
    public final void setHandle(String handle) {
        this.handle = handle;
    }

    /**
     * Set the expiration time in milliseconds.
     *
     * @return Expiration time in milliseconds since epoch, or null if the expiration was not set.
     */
    public Long getExpirationMillis() {
        return this.expirationMillis;
    }

    /**
     * Sets the cache expiration time.  When the portlet is stale (expired),
     * the refresh() will be called if the portlet has not been untouched
     * longer then then it's TimeToLive.
     *
     * @param expirationMillis setExpirationMillis Expiration in milliseconds since epoch
     */
    public void setExpirationMillis(long expirationMillis) {
        this.expirationMillis = new Long(expirationMillis);
        if (cachedObject != null) {
            long expirationInterval = this.expirationMillis.longValue() - cachedObject.getCreated();
            if (expirationInterval > 0) {
                cachedObject.setExpires(expirationInterval);
            } else {
                cachedObject.setStale(true);
            }
        }
    }

    /**
     * Builds a new cache handle for this cacheable class with the specified
     * config object.
     *
     * @param config The configuration object to use for building the handle
     *
     * @return A cache handle
     */
    public static Object getHandle(Object config) {
        PortletConfig pc = null;
        if (!(config instanceof PortletConfig)) {
            return null;
        }
        pc = (PortletConfig) config;
        StringBuffer handle = new StringBuffer(256);
        if (pc.getURL() != null && pc.isCachedOnURL()) {
            handle.append(String.valueOf(pc.getURL().hashCode()));
        }
        Iterator i = pc.getInitParameterNames();
        while (i.hasNext()) {
            String name = (String) i.next();
            String value = pc.getInitParameter(name);
            if (value != null) {
                handle.append("|").append(name).append("-").append(value);
            }
        }
        return handle.toString();
    }

    /**
     * Set this portlet's cached object.
     *
     * @param cachedObject Cached Object associated to this portlet
     */
    public void setCachedObject(CachedObject cachedObject) {
        this.cachedObject = cachedObject;
    }

    /**
     * Get the portlet's name
     *
     * @return Name of the portlet
     */
    public String getName() {
        if (name == null) {
            if (getPortletConfig() != null) {
                if (getPortletConfig().getName() != null) {
                    return getPortletConfig().getName();
                } else {
                    return this.getClass().getName();
                }
            }
        }
        return name;
    }

    /**
     * Set the name of the portlet
     *
     * @param name Name of the portlet
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the config of this servlet
     *
     * @return PortletConfig Portlet
     */
    public PortletConfig getPortletConfig() {
        return this.pc;
    }

    /**
     * Set's the configuration of this servlet.
     */
    public void setPortletConfig(PortletConfig pc) {
        this.pc = pc;
    }

    /**
     * @param rundata The RunData object for the current request
     */
    public ConcreteElement getContent(RunData rundata) {
        return getContent(rundata, null, true);
    }

    public ConcreteElement getContent(RunData rundata, CapabilityMap map) {
        CapabilityMap mymap = map;
        if (mymap == null) mymap = CapabilityMapFactory.getCapabilityMap(rundata);
        return (ConcreteElement) content.get(mymap.toString());
    }

    /**
     * @param rundata The RunData object for the current request
     */
    public ConcreteElement getContent(RunData rundata, CapabilityMap map, boolean allowRecurse) {
        CapabilityMap mymap = map;
        if (mymap == null) mymap = CapabilityMapFactory.getCapabilityMap(rundata);
        ConcreteElement element = (ConcreteElement) content.get(mymap.toString());
        if (element == null) {
            if (allowRecurse) {
                try {
                    init();
                    element = getContent(rundata, mymap, false);
                    if (element != null) {
                        this.setContent(element, mymap);
                    }
                } catch (Exception e) {
                    element = new JetspeedClearElement("Error when retrieving Portlet contents");
                    if (logger.isDebugEnabled()) {
                        logger.debug("Error when retrieving Portlet contents", e);
                    }
                }
            } else {
                if (element == null) {
                    mymap = CapabilityMapFactory.getDefaultCapabilityMap();
                    element = (ConcreteElement) content.get(mymap.toString());
                    if (element == null) {
                        element = new JetspeedClearElement("Unknown Problem getting Contents");
                    }
                }
            }
        }
        return element;
    }

    /**
     * Provide a description within PML if the user has specified one.
     *
     * @return a null entry if the user hasn't defined anything
     */
    public String getDescription() {
        if (getPortletConfig() != null) if (getPortletConfig().getMetainfo() != null) return getPortletConfig().getMetainfo().getDescription();
        return null;
    }

    /**
     * Provide a Description within PML if the user has specified one.
     *
     * @return a null if entry AND portlet have not defined a description
     */
    public String getDescription(String instanceDescription) {
        if (instanceDescription != null) return instanceDescription;
        return getDescription();
    }

    /**
     */
    public void setDescription(String description) {
        PortletConfig pc = getPortletConfig();
        if (pc == null) {
            pc = new BasePortletConfig();
            setPortletConfig(pc);
        }
        MetaData meta = pc.getMetainfo();
        if (meta == null) {
            meta = new MetaData();
            pc.setMetainfo(meta);
        }
        meta.setDescription(description);
    }

    /**
     * Provide a title within PML if the user has specified one.
     *
     * @return a null entry if the user hasn't defined anything
     */
    public String getTitle() {
        if (getPortletConfig() != null) if (getPortletConfig().getMetainfo() != null) return getPortletConfig().getMetainfo().getTitle();
        return null;
    }

    /**
     * Provide a title within PML if the user has specified one.
     *
     * @return a null if entry AND portlet have not defined a title
     */
    public String getTitle(String instanceTitle) {
        if (instanceTitle != null) return instanceTitle;
        return getTitle();
    }

    /**
     * Set the title for this Portlet.
     * @param title Portlet title.
     */
    public void setTitle(String title) {
        PortletConfig pc = getPortletConfig();
        if (pc == null) {
            pc = new BasePortletConfig();
            setPortletConfig(pc);
        }
        MetaData meta = pc.getMetainfo();
        if (meta == null) {
            meta = new MetaData();
            pc.setMetainfo(meta);
        }
        meta.setTitle(title);
    }

    /**
     * Getter for property image.
     * @return Name of portlet image, icon.  The name is expected to be in the form of a URL.
     */
    public String getImage() {
        if (getPortletConfig() != null) if (getPortletConfig().getMetainfo() != null) return getPortletConfig().getMetainfo().getImage();
        return null;
    }

    /**
     * Getter for property image.
     * @return a null if entry AND portlet have not defined an icon.
     */
    public String getImage(String instanceImage) {
        if (instanceImage != null) return instanceImage;
        return getImage();
    }

    public void setImage(String image) {
        PortletConfig pc = getPortletConfig();
        if (pc == null) {
            pc = new BasePortletConfig();
            setPortletConfig(pc);
        }
        MetaData meta = pc.getMetainfo();
        if (meta == null) {
            meta = new MetaData();
            pc.setMetainfo(meta);
        }
        meta.setImage(image);
    }

    /**
     * Is the portled editable/customizeable.
     * @param rundata The RunData object for the current request
     * @return <CODE>true</CODE> Editing is allow
     * <CODE>false</CODE> Editing is NOT alowed
     */
    public boolean getAllowEdit(RunData rundata) {
        return allowCustomize(rundata);
    }

    /**
     * Is the portled viewable.
     * @param rundata The RunData object for the current request
     * @return <CODE>true</CODE> Viewing is allow
     * <CODE>false</CODE> Viewing is NOT alowed
     * 
     * Override this method to control your own View behavior
     */
    public boolean getAllowView(RunData rundata) {
        return allowView(rundata);
    }

    /**
     * Can this portlet be maximized
     * @param rundata The RunData object for the current request
     * @return <CODE>true</CODE> Portlet can be maximized<br>
     * <CODE>false</CODE> Portlet can NOT be maximized
     */
    public boolean getAllowMaximize(RunData rundata) {
        return allowMaximize(rundata);
    }

    /**
     * By default don't provide any initialization
     */
    public void init() throws PortletException {
        clearContent();
    }

    /**
     */
    public long getCreationTime() {
        return this.creationTime;
    }

    /**
     */
    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }

    /**
     */
    public boolean supportsType(MimeType mimeType) {
        PortletEntry entry = (PortletEntry) Registry.getEntry(Registry.PORTLET, getName());
        String baseType = mimeType.toString();
        if (entry != null) {
            Iterator i = entry.listMediaTypes();
            while (i.hasNext()) {
                String name = (String) i.next();
                MediaTypeEntry media = (MediaTypeEntry) Registry.getEntry(Registry.MEDIA_TYPE, name);
                if (media != null) {
                    if (baseType.equals(media.getMimeType())) return true;
                }
            }
        }
        return MimeType.HTML.equals(mimeType);
    }

    /**
     * Implements the default close behavior:
     * security permissions will be checked.
     *
     * @param rundata The RunData object for the current request
     */
    public boolean allowClose(RunData rundata) {
        return !isClosed(rundata);
    }

    /**
     * Returns true if this portlet is currently closed
     *
     * @param rundata The RunData object for the current request
     */
    public boolean isClosed(RunData rundata) {
        return this.getAttribute("_display", "normal", rundata).equals("closed");
    }

    /**
     * Toggles the portlet state between closed and normal
     *
     * @param minimized the new portlet state
     * @param rundata The RunData object for the current request
     */
    public void setClosed(boolean close, RunData rundata) {
        if (allowClose(rundata)) {
            this.setAttribute("_display", close ? "closed" : "normal", rundata);
        }
    }

    /**
     * Implements the default info behavior:
     * security permissions will be checked.
     *
     * @param rundata The RunData object for the current request
     */
    public boolean allowInfo(RunData rundata) {
        return true;
    }

    /**
     * Implements the default customize behavior:
     * security permissions will be checked.
     *
     * @param rundata The RunData object for the current request
     */
    public boolean allowCustomize(RunData rundata) {
        return true;
    }

    /**
     * Implements the default maximize behavior:
     * security permissions will be checked.
     *
     * @param rundata The RunData object for the current request
     */
    public boolean allowMaximize(RunData rundata) {
        return true;
    }

    /**
     * Implements the default info behavior:
     * security permissions will be checked.
     *
     * @param rundata The RunData object for the current request
     */
    public boolean allowMinimize(RunData rundata) {
        return true;
    }

    /**
     * Implements the default view behavior:
     * security permissions will be checked.
     *
     * @param rundata The RunData object for the current request
     */
    public boolean allowView(RunData rundata) {
        return true;
    }

    /**
     * Implements the default print friendly format behavior:
     * security permissions will be checked.
     *
     * @param rundata The RunData object for the current request
     */
    public boolean allowPrintFriendly(RunData rundata) {
        return true;
    }

    /**
     * Returns true if this portlet is currently minimized
     *
     * @param rundata The RunData object for the current request
     */
    public boolean isMinimized(RunData rundata) {
        return this.getAttribute("_display", "normal", rundata).equals("minimized");
    }

    /**
     * Change the portlet visibility state ( minimized <-> normal )
     *
     * @param minimize True if the portlet change to minimized
     * @param rundata The RunData object for the current request
     */
    public void setMinimized(boolean minimize, RunData rundata) {
        if (allowMinimize(rundata)) {
            this.setAttribute("_display", minimize ? "minimized" : "normal", rundata);
        }
    }

    /**
     * Returns TRUE if the title bar in should be displayed. The title bar includes
     * the portlet title and action buttons.  This
     *
     * @param rundata The RunData object for the current request
     */
    public boolean isShowTitleBar(RunData rundata) {
        if (getPortletConfig() != null) {
            return Boolean.valueOf(getPortletConfig().getInitParameter("_showtitlebar", "true")).booleanValue();
        }
        return this.getAttribute("_showtitlebar", "true", rundata).equals("true");
    }

    /**
     * Retrieve a portlet attribute from persistent storage
     *
     * @param attrName The attribute to retrieve
     * @param attrDefValue The value if the attr doesn't exists
     * @param rundata The RunData object for the current request
     * @return The attribute value
     */
    public String getAttribute(String attrName, String attrDefValue, RunData rundata) {
        String attrValue = null;
        PortletInstance instance = PersistenceManager.getInstance(this, rundata);
        attrValue = instance.getAttribute(attrName, attrDefValue);
        return attrValue;
    }

    /**
     * Stores a portlet attribute in persistent storage
     *
     * @param attrName The attribute to retrieve
     * @paarm attrValue The value to store
     * @param rundata The RunData object for the current request
     */
    public void setAttribute(String attrName, String attrValue, RunData rundata) {
        try {
            PortletInstance instance = PersistenceManager.getInstance(this, rundata);
            instance.setAttribute(attrName, attrValue);
            PersistenceManager.store(instance);
        } catch (PortalPersistenceException e) {
            logger.error("Exception while setting attribute " + attrName + " for portlet " + getName(), e);
        }
    }

    /**
     * Gets the portlet instance associated with this portlet.
     *
     * @param rundata The RunData object for the current request
     * @return PortletInstance
     */
    public PortletInstance getInstance(RunData rundata) {
        return PersistenceManager.getInstance(this, rundata);
    }

    public String getID() {
        return id;
    }

    public void setID(String id) {
        this.id = id;
    }

    /**
    * @return true if the portlet does its own customization
    */
    public boolean providesCustomization() {
        return false;
    }
}
