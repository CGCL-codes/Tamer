package scap.check;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class CheckContentRef {

    private final String href;

    private final String name;

    private Integer hashCache;

    public CheckContentRef(String href, String name) {
        if (href == null) {
            throw new NullPointerException("href");
        }
        this.href = href;
        this.name = name;
    }

    /**
	 * @return the href
	 */
    public String getHref() {
        return href;
    }

    /**
	 * @return the name
	 */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    @Override
    public boolean equals(Object other) {
        boolean result = false;
        if (this == other) {
            result = true;
        } else if (other instanceof CheckContentRef) {
            CheckContentRef that = (CheckContentRef) other;
            result = (that.canEqual(this) && this.getHref().equals(that.getHref()) && ((this.getName() == null && that.getName() == null) || (this.getName() != null && this.getName().equals(that.getName()))));
        }
        return result;
    }

    /**
	 * @return the hashCache
	 */
    protected final Integer getHashCache() {
        return hashCache;
    }

    @Override
    public int hashCode() {
        if (hashCache == null) {
            int hash = 15;
            hash = hash * 31 + getHref().hashCode();
            String name = getName();
            if (name != null) {
                hash = hash * 31 + name.hashCode();
            }
            hashCache = hash;
        }
        return hashCache;
    }

    public boolean canEqual(Object other) {
        return (other instanceof CheckContentRef);
    }
}
