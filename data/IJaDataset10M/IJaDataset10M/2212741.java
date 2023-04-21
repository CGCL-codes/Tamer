package net.sf.mxlosgi.disco;

/**
 * @author noah
 * 
 */
public class DiscoInfoIdentity {

    private String node;

    private String category;

    private String type;

    private String name;

    /**
	 * @param node
	 * @param category
	 * @param type
	 * @param name
	 */
    public DiscoInfoIdentity(String node, String category, String type, String name) {
        this.node = node;
        this.category = category;
        this.type = type;
        this.name = name;
    }

    public String getNode() {
        return node;
    }

    public String getCategory() {
        return category;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }
}
