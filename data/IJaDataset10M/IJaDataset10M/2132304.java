package org.peaseplate;

/**
 * A locator is a reference to a resource. It usually can
 * load the resource and check if it is outdated.
 * 
 * There is no definition for the load method, 
 * since it returns different types of objects with each
 * type of locator.
 */
public interface Locator {

    /**
	 * Returns the key
	 * @return the key
	 */
    public ResourceKey getKey();

    /**
	 * Returns the timestamp of the resource.
	 * Used for checking updates to the source of the resource.
	 * (usually this is the last modified date of the file).
	 * If there is no possibility to support such a timestamp, just return null.
	 * @return the timestamp, or null if not specified
	 */
    public Long getTimestamp();

    /**
	 * Sets the timestamp
	 * @param timestamp the timestamp, may be null
	 */
    public void setTimestamp(Long timestamp);

    /**
	 * Returns the raw size of the resource.
	 * Used for checking updates to the source of the resource.
	 * (usually this is the size of the file).
	 * If there is not possibility to support such a size, just return null.
	 * @return the raw size, or null if not specified
	 */
    public Long getRawSize();

    /**
	 * Sets the raw size
	 * @param rawSize the raw size, may be null
	 */
    public void setRawSize(Long rawSize);

    /**
	 * Returns true if the resource described by the locator
	 * got updated and needs to be reloaded
	 * @return true if the resource should get reloaded
	 */
    public boolean isOutdated();

    /**
	 * A meaningful representation of the locator, mainly used for textual output
	 * e.g. in error messages
	 * @return a meaningful representation of the locator
	 * @see org.peaseplate.ResourceKey#toString()
	 */
    public String toString();
}
