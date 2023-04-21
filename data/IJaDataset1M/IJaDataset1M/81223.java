package com.sun.syndication.feed.synd.impl;

import com.sun.syndication.feed.impl.ObjectBean;
import com.sun.syndication.feed.module.*;
import com.sun.syndication.feed.module.impl.ContentModuleImpl;
import com.sun.syndication.feed.module.impl.DCModuleImpl;
import com.sun.syndication.feed.module.impl.ItunesModuleImpl;
import com.sun.syndication.feed.module.impl.ModuleUtils;
import com.sun.syndication.feed.module.impl.SyModuleImpl;
import com.sun.syndication.feed.synd.SyndCategory;
import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndEnclosure;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndLink;
import com.sun.syndication.feed.synd.SyndPerson;
import com.sun.syndication.feed.impl.CopyFromHelper;
import java.util.*;
import java.io.Serializable;

/**
 * Bean for entries of SyndFeedImpl feeds.
 * <p>
 * @author Alejandro Abdelnur
 * @author escape-llc
 */
public class SyndEntryImpl implements Serializable, SyndEntry {

    private static final long serialVersionUID = 1L;

    private ObjectBean _objBean;

    private String _uri;

    private String _link;

    private Date _updatedDate;

    private SyndContent _title;

    private SyndContent _description;

    private List<SyndLink> _links;

    private List<SyndContent> _contents;

    private List<Module> _modules;

    private List<SyndEnclosure> _enclosures;

    private List<SyndPerson> _authors;

    private List<SyndPerson> _contributors;

    private List<SyndCategory> _categories;

    private SyndFeed _source;

    private List<Object> _foreignMarkup;

    private Date _pubDate;

    private Object wireEntry;

    private static final Set<String> IGNORE_PROPERTIES;

    /**
	 * Unmodifiable Set containing the convenience properties of this class.
	 * <p>
	 * Convenience properties are mapped to Modules, for cloning the convenience
	 * properties can be ignored as the will be copied as part of the module
	 * cloning.
	 */
    public static final Set<String> CONVENIENCE_PROPERTIES;

    private final Class<?> beanClass;

    private final Set<String> convenienceProperties;

    private static volatile CopyFromHelper COPY_FROM_HELPER;

    private static final Object cflock;

    static {
        cflock = new Object();
        IGNORE_PROPERTIES = new HashSet<String>();
        CONVENIENCE_PROPERTIES = Collections.unmodifiableSet(IGNORE_PROPERTIES);
    }

    /**
	 * For implementations extending SyndEntryImpl to be able to use the
	 * ObjectBean functionality with extended interfaces.
	 * <p>
	 * @param beanClass
	 * @param convenienceProperties
	 *          set containing the convenience properties of the SyndEntryImpl
	 *          (the are ignored during cloning, check CloneableBean for details).
	 */
    protected SyndEntryImpl(Class<?> beanClass, Set<String> convenienceProperties) {
        this.beanClass = beanClass;
        this.convenienceProperties = convenienceProperties;
    }

    /**
	 * Default constructor. All properties are set to <b>null</b>.
	 * <p>
	 */
    public SyndEntryImpl() {
        this(SyndEntry.class, IGNORE_PROPERTIES);
    }

    private ObjectBean getObjBean() {
        if (_objBean == null) _objBean = new ObjectBean(beanClass, this, convenienceProperties);
        return _objBean;
    }

    /**
	 * Creates a deep 'bean' clone of the object.
	 * <p>
	 * @return a clone of the object.
	 * @throws CloneNotSupportedException
	 *           thrown if an element of the object cannot be cloned.
	 */
    public Object clone() throws CloneNotSupportedException {
        return getObjBean().clone();
    }

    /**
	 * Indicates whether some other object is "equal to" this one as defined by
	 * the Object equals() method.
	 * <p>
	 * @param other
	 *          he reference object with which to compare.
	 * @return <b>true</b> if 'this' object is equal to the 'other' object.
	 */
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (!(other instanceof SyndEntryImpl)) {
            return false;
        }
        boolean ret = getObjBean().equals(other);
        return ret;
    }

    /**
	 * Returns a hashcode value for the object.
	 * <p>
	 * It follows the contract defined by the Object hashCode() method.
	 * <p>
	 * @return the hashcode of the bean object.
	 */
    public int hashCode() {
        return getObjBean().hashCode();
    }

    /**
	 * Returns the String representation for the object.
	 * <p>
	 * @return String representation for the object.
	 */
    public String toString() {
        return getObjBean().toString();
    }

    /**
	 * Returns the entry URI.
	 * <p>
	 * How the entry URI maps to a concrete feed type (RSS or Atom) depends on the
	 * concrete feed type. This is explained in detail in Rome documentation, <a
	 * href="http://wiki.java.net/bin/edit/Javawsxml/Rome04URIMapping">Feed and
	 * entry URI mapping</a>.
	 * <p>
	 * The returned URI is a normalized URI as specified in RFC 2396bis.
	 * <p>
	 * @return the entry URI, <b>null</b> if none.
	 * 
	 */
    public String getUri() {
        return _uri;
    }

    /**
	 * Sets the entry URI.
	 * <p>
	 * How the entry URI maps to a concrete feed type (RSS or Atom) depends on the
	 * concrete feed type. This is explained in detail in Rome documentation, <a
	 * href="http://wiki.java.net/bin/edit/Javawsxml/Rome04URIMapping">Feed and
	 * entry URI mapping</a>.
	 * <p>
	 * @param uri
	 *          the entry URI to set, <b>null</b> if none.
	 */
    public void setUri(String uri) {
        _uri = URINormalizer.normalize(uri);
    }

    /**
	 * Returns the entry title.
	 * <p>
	 * @return the entry title, <b>null</b> if none.
	 */
    public String getTitle() {
        if (_title != null) return _title.getValue();
        return null;
    }

    /**
	 * Sets the entry title. Creates <b>SyndContent</b> if necessary.
	 * <p>
	 * @param title
	 *          the entry title to set, <b>null</b> if none.
	 */
    public void setTitle(String title) {
        if (_title == null) {
            _title = new SyndContentImpl();
            _title.setType("text/*");
        }
        _title.setValue(title);
    }

    /**
	 * Returns the entry title as a text construct.
	 * <p>
	 * @return the entry title, <b>null</b> if none.
	 */
    public SyndContent getTitleEx() {
        return _title;
    }

    /**
	 * Sets the entry title as a text construct.
	 * <p>
	 * @param title
	 *          the entry title to set, <b>null</b> if none.
	 */
    public void setTitleEx(SyndContent title) {
        _title = title;
    }

    /**
	 * Returns the entry link.
	 * <p>
	 * @return the entry link, <b>null</b> if none.
	 */
    public String getLink() {
        return _link;
    }

    /**
	 * Sets the entry link.
	 * <p>
	 * @param link
	 *          the entry link to set, <b>null</b> if none.
	 */
    public void setLink(String link) {
        _link = link;
    }

    /**
	 * Returns the entry description.
	 * <p>
	 * @return the entry description, <b>null</b> if none.
	 */
    public SyndContent getDescription() {
        return _description;
    }

    /**
	 * Sets the entry description.
	 * <p>
	 * @param description
	 *          the entry description to set, <b>null</b> if none.
	 */
    public void setDescription(SyndContent description) {
        _description = description;
    }

    /**
	 * Returns the entry contents.
	 * <p>
	 * @return a list of SyndContentImpl elements with the entry contents, an
	 *         empty list if none.
	 */
    public List<SyndContent> getContents() {
        return (_contents == null) ? (_contents = new ArrayList<SyndContent>(4)) : _contents;
    }

    /**
	 * Sets the entry contents.
	 * <p>
	 * @param contents
	 *          the list of SyndContentImpl elements with the entry contents to
	 *          set, an empty list or <b>null</b> if none.
	 */
    public void setContents(List<SyndContent> contents) {
        _contents = contents;
    }

    /**
	 * Returns the entry enclosures.
	 * <p>
	 * @return a list of SyndEnclosure elements with the entry enclosures, an
	 *         empty list if none.
	 */
    public List<SyndEnclosure> getEnclosures() {
        return (_enclosures == null) ? (_enclosures = new ArrayList<SyndEnclosure>(4)) : _enclosures;
    }

    /**
	 * Sets the entry enclosures.
	 * <p>
	 * @param enclosures
	 *          the list of SyndEnclosure elements with the entry enclosures to
	 *          set, an empty list or <b>null</b> if none.
	 */
    public void setEnclosures(List<SyndEnclosure> enclosures) {
        _enclosures = enclosures;
    }

    /**
	 * Returns the entry published date.
	 * <p>
	 * @return the entry published date, <b>null</b> if none.
	 */
    public Date getPublishedDate() {
        return _pubDate;
    }

    /**
	 * Sets the entry published date.
	 * <p>
	 * @param publishedDate
	 *          the entry published date to set, <b>null</b> if none.
	 */
    public void setPublishedDate(Date publishedDate) {
        _pubDate = publishedDate;
    }

    /**
	 * Returns the entry categories.
	 * <p>
	 * @return a list of SyndCategoryImpl elements with the entry categories, an
	 *         empty list if none.
	 */
    public List<SyndCategory> getCategories() {
        return _categories == null ? (_categories = new ArrayList<SyndCategory>(4)) : _categories;
    }

    /**
	 * Sets the entry categories.
	 * <p>
	 * @param categories
	 *          the list of SyndCategoryImpl elements with the entry categories to
	 *          set, an empty list or <b>null</b> if none.
	 */
    public void setCategories(List<SyndCategory> categories) {
        _categories = categories;
    }

    /**
	 * Returns the entry modules.
	 * <p>
	 * @return a list of ModuleImpl elements with the entry modules, an empty list
	 *         if none.
	 */
    public List<Module> getModules() {
        if (_modules == null) {
            _modules = new ArrayList<Module>(4);
        }
        return _modules;
    }

    /**
	 * Sets the entry modules.
	 * <p>
	 * @param modules
	 *          the list of ModuleImpl elements with the entry modules to set, an
	 *          empty list or <b>null</b> if none.
	 */
    public void setModules(List<Module> modules) {
        _modules = modules;
    }

    /**
	 * Returns the module identified by a given URI.
	 * <p>
	 * @param uri
	 *          the URI of the ModuleImpl.
	 * @return The module with the given URI, <b>null</b> if none.
	 */
    public Module getModule(String uri) {
        return ModuleUtils.getModule(getModules(), uri);
    }

    public Class<?> getInterface() {
        return SyndEntry.class;
    }

    public void copyFrom(Object obj) {
        createCopyFrom().copy(this, obj);
    }

    /**
	 * Returns the links
	 * <p>
	 * @return Returns the links.
	 */
    public List<SyndLink> getLinks() {
        return (_links == null) ? (_links = new ArrayList<SyndLink>(4)) : _links;
    }

    /**
	 * Set the links
	 * <p>
	 * @param links
	 *          The links to set.
	 */
    public void setLinks(List<SyndLink> links) {
        _links = links;
    }

    /**
	 * Returns the updatedDate
	 * <p>
	 * @return Returns the updatedDate.
	 */
    public Date getUpdatedDate() {
        return _updatedDate;
    }

    /**
	 * Set the updatedDate
	 * <p>
	 * @param updatedDate
	 *          The updatedDate to set.
	 */
    public void setUpdatedDate(Date updatedDate) {
        _updatedDate = updatedDate;
    }

    public List<SyndPerson> getAuthors() {
        return (_authors == null) ? (_authors = new ArrayList<SyndPerson>(4)) : _authors;
    }

    public void setAuthors(List<SyndPerson> authors) {
        _authors = authors;
    }

    /**
	 * Returns the entry author.  If multiple, returns first one.
	 * <p>
	 * @return the entry author, <b>null</b> if none.
	 */
    public String getAuthor() {
        return getAuthors().size() == 0 ? null : getAuthors().get(0).getName();
    }

    /**
	 * Sets the entry author.  Creates <b>SyndPerson</b> and adds to list.
	 * <p>
	 * @param author
	 *          the entry author to set, <b>null</b> if none.
	 */
    public void setAuthor(String author) {
        final SyndPersonImpl sp = new SyndPersonImpl();
        sp.setName(author);
        getAuthors().add(sp);
    }

    public List<SyndPerson> getContributors() {
        return (_contributors == null) ? (_contributors = new ArrayList<SyndPerson>(4)) : _contributors;
    }

    public void setContributors(List<SyndPerson> contributors) {
        _contributors = contributors;
    }

    public SyndFeed getSource() {
        return _source;
    }

    public void setSource(SyndFeed source) {
        _source = source;
    }

    /**
	 * Returns foreign markup found at entry level.
	 * <p>
	 * @return list of foreign markup, an empty list if none.
	 */
    public Object getForeignMarkup() {
        return (_foreignMarkup == null) ? (_foreignMarkup = new ArrayList<Object>(4)) : _foreignMarkup;
    }

    /**
	 * Sets foreign markup found at entry level.
	 * <p>
	 * @param foreignMarkup
	 *          list of foreign markup, an empty list if none.
	 */
    @SuppressWarnings("unchecked")
    public void setForeignMarkup(Object foreignMarkup) {
        _foreignMarkup = (List<Object>) foreignMarkup;
    }

    public Object getWireEntry() {
        return wireEntry;
    }

    public void setWireEntry(Object wireEntry) {
        this.wireEntry = wireEntry;
    }

    static CopyFromHelper createCopyFrom() {
        if (COPY_FROM_HELPER == null) {
            synchronized (cflock) {
                if (COPY_FROM_HELPER == null) {
                    final Map<String, Class<?>> basePropInterfaceMap = new HashMap<String, Class<?>>();
                    basePropInterfaceMap.put("uri", String.class);
                    basePropInterfaceMap.put("title", String.class);
                    basePropInterfaceMap.put("link", String.class);
                    basePropInterfaceMap.put("uri", String.class);
                    basePropInterfaceMap.put("description", SyndContent.class);
                    basePropInterfaceMap.put("contents", SyndContent.class);
                    basePropInterfaceMap.put("enclosures", SyndEnclosure.class);
                    basePropInterfaceMap.put("modules", Module.class);
                    basePropInterfaceMap.put("publishedDate", Date.class);
                    basePropInterfaceMap.put("author", String.class);
                    final Map<Class<?>, Class<?>> basePropClassImplMap = new HashMap<Class<?>, Class<?>>();
                    basePropClassImplMap.put(SyndContent.class, SyndContentImpl.class);
                    basePropClassImplMap.put(SyndEnclosure.class, SyndEnclosureImpl.class);
                    basePropClassImplMap.put(DCModule.class, DCModuleImpl.class);
                    basePropClassImplMap.put(SyModule.class, SyModuleImpl.class);
                    basePropClassImplMap.put(ContentModule.class, ContentModuleImpl.class);
                    basePropClassImplMap.put(ItunesModule.class, ItunesModuleImpl.class);
                    COPY_FROM_HELPER = new CopyFromHelper(SyndEntry.class, basePropInterfaceMap, basePropClassImplMap);
                }
            }
        }
        return COPY_FROM_HELPER;
    }
}
