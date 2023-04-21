package com.jivespaces.domain;

/**
 * @author Shutra
 * 
 */
public class Space extends BaseDomain {

    /**
	 * 
	 */
    private static final long serialVersionUID = 513625230667327985L;

    private User owner;

    private String name;

    private String displayName;

    private String keywords;

    private String description;

    /**
	 * @return the owner
	 */
    public User getOwner() {
        return owner;
    }

    /**
	 * @param owner
	 *            the owner to set
	 */
    public void setOwner(User owner) {
        this.owner = owner;
    }

    /**
	 * @return the name
	 */
    public String getName() {
        return name;
    }

    /**
	 * @param name
	 *            the name to set
	 */
    public void setName(String name) {
        this.name = name;
    }

    /**
	 * @return the displayName
	 */
    public String getDisplayName() {
        return displayName;
    }

    /**
	 * @param displayName
	 *            the displayName to set
	 */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
	 * @return the description
	 */
    public String getDescription() {
        return description;
    }

    /**
	 * @param description
	 *            the description to set
	 */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
	 * @return the keywords
	 */
    public String getKeywords() {
        return keywords;
    }

    /**
	 * @param keywords
	 *            the keywords to set
	 */
    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }
}
