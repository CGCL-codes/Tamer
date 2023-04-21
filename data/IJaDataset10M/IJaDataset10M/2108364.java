package fido.db;

/**
 * 
 * 
 * 
 * 
 */
public class ClassLinkType {

    private String linkName;

    private int type;

    private String description;

    /**
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
    public ClassLinkType(String linkName, int type, String description) {
        this.linkName = linkName;
        this.type = type;
        this.description = description;
    }

    /**
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
    public String getLinkName() {
        return linkName;
    }

    /**
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
    public int getType() {
        return type;
    }

    /**
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
    public String getDescription() {
        return description;
    }
}
