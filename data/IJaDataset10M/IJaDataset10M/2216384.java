package org.torweg.pulse.component.core.system;

import java.util.HashSet;
import java.util.Set;
import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.stat.SecondLevelCacheStatistics;
import org.hibernate.stat.Statistics;

/**
 * SecondLevelCacheStats.
 * 
 * @author Thomas Weber
 * @version $Revision$
 */
@XmlRootElement(name = "second-level-cache-statistics")
@XmlAccessorOrder(XmlAccessOrder.UNDEFINED)
@XmlAccessorType(XmlAccessType.NONE)
final class SecondLevelCacheStats {

    /**
	 * the individual cache regions.
	 */
    @XmlElementWrapper(name = "regions")
    @XmlElement(name = "region")
    private Set<SecondLevelCacheStats.SecondLevelCacheRegion> regions = new HashSet<SecondLevelCacheStats.SecondLevelCacheRegion>();

    /**
	 * hitCount.
	 */
    @XmlElement(name = "hit-count")
    private long hitCount;

    /**
	 * missCount.
	 */
    @XmlElement(name = "miss-count")
    private long missCount;

    /**
	 * putCount.
	 */
    @XmlElement(name = "put-count")
    private long putCount;

    /**
	 * the query cache stats.
	 */
    @XmlElement(name = "query-cache")
    private QueryCacheStats queryCache;

    /**
	 * for JAXB.
	 */
    @Deprecated
    protected SecondLevelCacheStats() {
        super();
    }

    /**
	 * creates new SecondLevelCacheStats.
	 * 
	 * @param stats
	 *            the Hibernate Statistics
	 */
    public SecondLevelCacheStats(final Statistics stats) {
        super();
        this.hitCount = stats.getSecondLevelCacheHitCount();
        this.missCount = stats.getSecondLevelCacheMissCount();
        this.putCount = stats.getSecondLevelCachePutCount();
        for (String regionName : stats.getSecondLevelCacheRegionNames()) {
            this.regions.add(new SecondLevelCacheRegion(stats.getSecondLevelCacheStatistics(regionName), regionName));
        }
        this.queryCache = new QueryCacheStats(stats);
    }

    /**
	 * @return the regions
	 */
    public Set<SecondLevelCacheStats.SecondLevelCacheRegion> getRegions() {
        return this.regions;
    }

    /**
	 * @return the hitCount
	 */
    public long getHitCount() {
        return this.hitCount;
    }

    /**
	 * @return the missCount
	 */
    public long getMissCount() {
        return this.missCount;
    }

    /**
	 * @return the putCount
	 */
    public long getPutCount() {
        return this.putCount;
    }

    /**
	 * @return the queryCache
	 */
    public QueryCacheStats getQueryCache() {
        return queryCache;
    }

    /**
	 * SecondLevelCacheRegion.
	 */
    static final class SecondLevelCacheRegion {

        /**
		 * elementCountInMemory.
		 */
        @XmlElement(name = "element-count-in-memory")
        private long elementCountInMemory;

        /**
		 * sizeInMemory.
		 */
        @XmlElement(name = "size-in-memory")
        private long sizeInMemory;

        /**
		 * elementCountOnDisk.
		 */
        @XmlElement(name = "element-count-on-disk")
        private long elementCountOnDisk;

        /**
		 * hitCount.
		 */
        @XmlElement(name = "hit-count")
        private long hitCount;

        /**
		 * missCount.
		 */
        @XmlElement(name = "miss-count")
        private long missCount;

        /**
		 * putCount.
		 */
        @XmlElement(name = "put-count")
        private long putCount;

        /**
		 * the name.
		 */
        @XmlAttribute(name = "name")
        private String name;

        /**
		 * for JAXB.
		 */
        @Deprecated
        protected SecondLevelCacheRegion() {
            super();
        }

        /**
		 * creates a new cache region stats.
		 * 
		 * @param stats
		 *            the stats for the region
		 * @param n
		 *            the name
		 */
        public SecondLevelCacheRegion(final SecondLevelCacheStatistics stats, final String n) {
            super();
            this.name = n;
            this.elementCountInMemory = stats.getElementCountInMemory();
            this.elementCountOnDisk = stats.getElementCountOnDisk();
            this.hitCount = stats.getHitCount();
            this.missCount = stats.getMissCount();
            this.putCount = stats.getPutCount();
            this.sizeInMemory = stats.getSizeInMemory();
        }

        /**
		 * @return the name
		 */
        public String getName() {
            return name;
        }

        /**
		 * returns the elementCountInMemory.
		 * 
		 * @return elementCountInMemory
		 */
        public long getElementCountInMemory() {
            return this.elementCountInMemory;
        }

        /**
		 * returns the sizeInMemory.
		 * 
		 * @return sizeInMemory
		 */
        public long getSizeInMemory() {
            return this.sizeInMemory;
        }

        /**
		 * returns the elementCountOnDisk.
		 * 
		 * @return elementCountOnDisk
		 */
        public long getElementCountOnDisk() {
            return this.elementCountOnDisk;
        }

        /**
		 * returns the hitCount.
		 * 
		 * @return hitCount
		 */
        public long getHitCount() {
            return this.hitCount;
        }

        /**
		 * returns the missCount.
		 * 
		 * @return missCount
		 */
        public long getMissCount() {
            return this.missCount;
        }

        /**
		 * returns the putCount.
		 * 
		 * @return putCount
		 */
        public long getPutCount() {
            return this.putCount;
        }
    }

    /**
	 * QueryCacheStats.
	 */
    static final class QueryCacheStats {

        /**
		 * hitCount.
		 */
        @XmlElement(name = "hit-count")
        private long hitCount;

        /**
		 * missCount.
		 */
        @XmlElement(name = "miss-count")
        private long missCount;

        /**
		 * putCount.
		 */
        @XmlElement(name = "put-count")
        private long putCount;

        /**
		 * for JAXB.
		 */
        @Deprecated
        protected QueryCacheStats() {
            super();
        }

        /**
		 * creates new SecondLevelCacheStats.
		 * 
		 * @param stats
		 *            the Hibernate Statistics
		 */
        public QueryCacheStats(final Statistics stats) {
            super();
            this.hitCount = stats.getQueryCacheHitCount();
            this.missCount = stats.getQueryCacheMissCount();
            this.putCount = stats.getQueryCachePutCount();
        }

        /**
		 * @return the hitCount
		 */
        public long getHitCount() {
            return this.hitCount;
        }

        /**
		 * @return the missCount
		 */
        public long getMissCount() {
            return this.missCount;
        }

        /**
		 * @return the putCount
		 */
        public long getPutCount() {
            return this.putCount;
        }
    }
}
