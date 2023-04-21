package org.torweg.pulse.site.content;

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.hibernate.LazyInitializationException;
import org.jdom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.torweg.pulse.bundle.Bundle;
import org.torweg.pulse.bundle.JDOMable;
import org.torweg.pulse.service.PulseException;
import org.torweg.pulse.util.entity.AbstractBasicEntity;
import org.torweg.pulse.util.xml.bind.LocaleXmlAdapter;

/**
 * marks a set of {@code Content}s to be localizations of each other.
 * <p>
 * Within a {@code ContentLocalizationMap} each {@code Locale} and {@code
 * Content} may only appear once. Moreover all {@code Content}s have to be of
 * the exact same type.
 * </p>
 * <p>
 * <em>The {@link ContentLocalizationMap#put(Locale, Content)} method does not comply with the general
 * {@code Map} contract, as it does not support the overwriting of
 * values!</em>
 * </p>
 * 
 * @author Thomas Weber, Daniel Dietz
 * @version $Revision: 1822 $
 */
@Entity
@XmlRootElement(name = "content-localization-map")
@XmlAccessorOrder(XmlAccessOrder.UNDEFINED)
@XmlAccessorType(XmlAccessType.FIELD)
public class ContentLocalizationMap extends AbstractBasicEntity implements JDOMable {

    /**
	 * serialVersionUID.
	 */
    private static final long serialVersionUID = 8867155586696229373L;

    /**
	 * the logger.
	 */
    private static final Logger LOGGER = LoggerFactory.getLogger(ContentLocalizationMap.class);

    /**
	 * the internal map of locales and contents.
	 */
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @MapKeyColumn(name = "locale")
    @XmlTransient
    private final Map<Locale, Content> map = new TreeMap<Locale, Content>(new LocaleStringComparator());

    /**
	 * used by Hibernate<sup>TM</sup>.
	 */
    @Deprecated
    protected ContentLocalizationMap() {
        super();
    }

    /**
	 * creates a new {@code ContentLocalizationMap} for the given {@code
	 * Content} and the given {@code Locale}.
	 * 
	 * @param l
	 *            the locale
	 * @param c
	 *            the content
	 */
    public ContentLocalizationMap(final Locale l, final Content c) {
        super();
        put(l, c);
    }

    /**
	 * removes all mappings from this map.
	 */
    public final void clear() {
        for (Locale l : this.map.keySet()) {
            remove(l);
        }
    }

    public final boolean containsKey(final Object key) throws ClassCastException, NullPointerException {
        return this.map.containsKey((Locale) key);
    }

    public final boolean containsValue(final Object value) throws ClassCastException, NullPointerException {
        return this.map.containsValue((Content) value);
    }

    /**
	 * returns a set view of the mappings contained in this map. Each element in
	 * the returned set is a {@link Entry}. The set is backed by the map, so
	 * changes to the map are reflected in the set, and vice-versa. If the map
	 * is modified while an iteration over the set is in progress (except
	 * through the iterator's own <tt>remove</tt> operation, or through the
	 * <tt>setValue</tt> operation on a map entry returned by the iterator) the
	 * results of the iteration are undefined. The set supports element removal,
	 * which removes the corresponding mapping from the map, via the
	 * <tt>Iterator.remove</tt>, <tt>Set.remove</tt>, <tt>removeAll</tt>,
	 * <tt>retainAll</tt> and <tt>clear</tt> operations. It does not support the
	 * <tt>add</tt> or <tt>addAll</tt> operations.
	 * 
	 * @return a set view of the mappings contained in this map.
	 */
    public final Set<java.util.Map.Entry<Locale, Content>> entrySet() {
        return this.map.entrySet();
    }

    public final Content get(final Object key) throws ClassCastException, NullPointerException {
        return this.map.get((Locale) key);
    }

    /**
	 * returns <tt>true</tt> if this map contains no key-value mappings.
	 * 
	 * @return <tt>true</tt> if this map contains no key-value mappings.
	 */
    public final boolean isEmpty() {
        return this.map.isEmpty();
    }

    /**
	 * returns a set view of the keys contained in this map. The set is backed
	 * by the map, so changes to the map are reflected in the set, and
	 * vice-versa. If the map is modified while an iteration over the set is in
	 * progress (except through the iterator's own <tt>remove</tt> operation),
	 * the results of the iteration are undefined. The set supports element
	 * removal, which removes the corresponding mapping from the map, via the
	 * <tt>Iterator.remove</tt>, <tt>Set.remove</tt>, <tt>removeAll</tt>
	 * <tt>retainAll</tt>, and <tt>clear</tt> operations. It does not support
	 * the add or <tt>addAll</tt> operations.
	 * 
	 * @return a set view of the keys contained in this map.
	 */
    public final Set<Locale> keySet() {
        return this.map.keySet();
    }

    public final Content put(final Locale key, final Content value) throws InconsistentLocalizationException {
        if (this.map.containsKey(key)) {
            throw new InconsistentLocalizationException("The locale " + key + " is already present.");
        } else if (this.map.containsValue(value)) {
            throw new InconsistentLocalizationException("The content " + value.getName() + " [" + value.getId() + "] is already present.");
        } else if (!key.equals(value.getLocale())) {
            throw new InconsistentLocalizationException("The given content and locale do not match.");
        } else if (!this.map.isEmpty() && !value.getClass().equals(this.map.values().iterator().next().getClass())) {
            throw new InconsistentLocalizationException("The given Content '" + value.getClass().getCanonicalName() + "' does not match the Content type of the Map '" + this.map.values().iterator().next().getClass().getCanonicalName() + "'.");
        } else if (value.getLocalizationMap() != null) {
            value.getLocalizationMap().updateRemoveContent(value);
        }
        value.updateLocalizationMap(this);
        return this.map.put(key, value);
    }

    /**
	 * internal method... TODO
	 * 
	 * @param value
	 *            x
	 */
    private void updateRemoveContent(final Content value) {
        for (Map.Entry<Locale, Content> entry : this.map.entrySet()) {
            if (entry.getValue().equals(value)) {
                this.map.remove(entry.getKey());
                break;
            }
        }
    }

    /**
	 * copies all of the mappings from the specified map to this map. The effect
	 * of this call is equivalent to that of calling
	 * {@link ContentLocalizationMap#put(Locale, Content)} on this map once for
	 * each mapping from key <tt>k</tt> to value <tt>v</tt> in the specified
	 * map. The behaviour of this operation is unspecified if the specified map
	 * is modified while the operation is in progress.
	 * 
	 * @param t
	 *            Mappings to be stored in this map.
	 */
    public final void putAll(final Map<? extends Locale, ? extends Content> t) {
        for (Map.Entry<? extends Locale, ? extends Content> entry : t.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    public final Content remove(final Object key) throws ClassCastException, NullPointerException {
        Content c = this.map.remove((Locale) key);
        if (c != null) {
            c.updateLocalizationMap(new ContentLocalizationMap((Locale) key, c));
        }
        return c;
    }

    /**
	 * returns the number of key-value mappings in this map. If the map contains
	 * more than <tt>Integer.MAX_VALUE</tt> elements, returns
	 * <tt>Integer.MAX_VALUE</tt>.
	 * 
	 * @return the number of key-value mappings in this map.
	 */
    public final int size() {
        return this.map.size();
    }

    /**
	 * returns a collection view of the values contained in this map. The
	 * collection is backed by the map, so changes to the map are reflected in
	 * the collection, and vice-versa. If the map is modified while an iteration
	 * over the collection is in progress (except through the iterator's own
	 * <tt>remove</tt> operation), the results of the iteration are undefined.
	 * The collection supports element removal, which removes the corresponding
	 * mapping from the map, via the <tt>Iterator.remove</tt>,
	 * <tt>Collection.remove</tt>, <tt>removeAll</tt>, <tt>retainAll</tt> and
	 * <tt>clear</tt> operations. It does not support the add or <tt>addAll</tt>
	 * operations.
	 * 
	 * @return a collection view of the values contained in this map.
	 */
    public final Collection<Content> values() {
        return this.map.values();
    }

    /**
	 * For JAXB only.
	 * 
	 * @return this.values()
	 */
    @XmlElementWrapper(name = "contents")
    @XmlElement(name = "content")
    @SuppressWarnings("unused")
    @Deprecated
    private HashSet<ContentInfo> getMap() {
        try {
            return new HashSet<ContentInfo>(ContentInfo.getContentInfos(values()));
        } catch (LazyInitializationException e) {
            LOGGER.debug("ignored: {}", e.getLocalizedMessage());
            return null;
        }
    }

    /**
	 * Always throws {@code PulseException}.
	 * <p>
	 * <strong>ONLY here to satisfy:</strong> {@code ContentLocalizationMap}
	 * extends {@code AbstractBasicEntity}.
	 * </p>
	 * 
	 * 
	 * @return always throws {@code PulseException}
	 */
    public final Element deserializeToJDOM() {
        throw new PulseException("Method not supported!");
    }

    /**
	 * Utility-Object that represents a content of this {@code
	 * ContentLoaclizationMap} for JAXB.
	 * 
	 * @author Daniel Dietz
	 * @version $Revision: 1822 $
	 * 
	 */
    @XmlRootElement(name = "content-info")
    @XmlAccessorOrder(XmlAccessOrder.UNDEFINED)
    @XmlAccessorType(XmlAccessType.FIELD)
    private static class ContentInfo implements Serializable {

        /**
		 * The serialVersionUID.
		 */
        private static final long serialVersionUID = -7775381150954577599L;

        /**
		 * The {@code Content} of the {@code ContentInfo}.
		 */
        @XmlTransient
        private Content content;

        /**
		 * Default constructor for JAXB.
		 */
        @Deprecated
        @SuppressWarnings("unused")
        protected ContentInfo() {
            super();
        }

        /**
		 * Creates a new {@code ContentInfo} for the given content.
		 * 
		 * @param c
		 *            the {@code Content}
		 */
        protected ContentInfo(final Content c) {
            super();
            setContent(c);
        }

        /**
		 * Sets the {@code Content}.
		 * 
		 * @param c
		 *            the {@code Content}
		 */
        protected final void setContent(final Content c) {
            this.content = c;
        }

        /**
		 * Returns the {@code Content}.
		 * 
		 * @return the {@code Content}
		 */
        protected final Content getContent() {
            return this.content;
        }

        /**
		 * For JAXB only.
		 * 
		 * @return getContent().getId()
		 */
        @XmlAttribute(name = "id")
        @SuppressWarnings("unused")
        @Deprecated
        private Long getIdJAXB() {
            try {
                return getContent().getId();
            } catch (LazyInitializationException e) {
                LOGGER.debug("ignored: {}", e.getLocalizedMessage());
                return null;
            }
        }

        /**
		 * For JAXB only.
		 * 
		 * @return getContent().getClass().getCanonicalName()
		 */
        @XmlAttribute(name = "class")
        @SuppressWarnings("unused")
        @Deprecated
        private String getFQClassNameJAXB() {
            try {
                return getContent().getClass().getCanonicalName();
            } catch (LazyInitializationException e) {
                LOGGER.debug("ignored: {}", e.getLocalizedMessage());
                return null;
            }
        }

        /**
		 * For JAXB only.
		 * 
		 * @return getContent().getName()
		 */
        @XmlElement(name = "name")
        @SuppressWarnings("unused")
        @Deprecated
        private String getNameJAXB() {
            try {
                return getContent().getName();
            } catch (LazyInitializationException e) {
                LOGGER.debug("ignored: {}", e.getLocalizedMessage());
                return null;
            }
        }

        /**
		 * For JAXB only.
		 * 
		 * @return getContent().getBundle()
		 */
        @XmlElement(name = "bundle")
        @SuppressWarnings("unused")
        @Deprecated
        private Bundle getBundleJAXB() {
            try {
                return getContent().getBundle();
            } catch (LazyInitializationException e) {
                LOGGER.debug("ignored: {}", e.getLocalizedMessage());
                return null;
            }
        }

        /**
		 * For JAXB only.
		 * 
		 * @return getContent().getLocale()
		 */
        @XmlElement(name = "locale")
        @XmlJavaTypeAdapter(value = LocaleXmlAdapter.class)
        @SuppressWarnings("unused")
        @Deprecated
        private Locale getLocaleJAXB() {
            try {
                return getContent().getLocale();
            } catch (LazyInitializationException e) {
                LOGGER.debug("ignored: {}", e.getLocalizedMessage());
                return null;
            }
        }

        /**
		 * Utility method to build a {@code Set&lt;ContentInfo&gt;} from a given
		 * {@code Collection&lt;Content&gt;}.
		 * 
		 * @param contents
		 *            the {@code Collection&lt;Content&gt;}
		 * 
		 * @return a {@code Set&lt;ContentInfo&gt;}
		 */
        protected static final Set<ContentInfo> getContentInfos(final Collection<Content> contents) {
            Set<ContentInfo> contentInfos = new HashSet<ContentInfo>();
            for (Content c : contents) {
                contentInfos.add(new ContentInfo(c));
            }
            return contentInfos;
        }
    }

    /**
	 * compares two locales string-wise.
	 */
    private static class LocaleStringComparator implements Comparator<Locale>, Serializable {

        /**
		 * serialVersionUID.
		 */
        private static final long serialVersionUID = -8798128443586879106L;

        /**
		 * compares the two locales by their string values.
		 * 
		 * @param o1
		 *            the first locale
		 * @param o2
		 *            the second locale
		 * @see Comparator#compare(Object, Object)
		 * @return the result of the comparison
		 */
        public final int compare(final Locale o1, final Locale o2) {
            return o1.toString().compareTo(o2.toString());
        }
    }
}
