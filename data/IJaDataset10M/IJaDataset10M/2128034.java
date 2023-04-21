package diuf.diva.hephaistk;

public class VariableAssociation {

    private String identifier = null;

    private Class<?> type = null;

    public VariableAssociation(String identifier, Class<?> type) {
        super();
        this.identifier = identifier;
        this.type = type;
    }

    /**
	 * @return the identifier
	 */
    public String getIdentifier() {
        return identifier;
    }

    /**
	 * @param identifier the identifier to set
	 */
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    /**
	 * @return the type
	 */
    public Class<?> getType() {
        return type;
    }

    /**
	 * @param type the type to set
	 */
    public void setType(Class<?> type) {
        this.type = type;
    }

    public String toString() {
        return ("[" + this.getClass().getName() + "|identifier:" + identifier + "|type:" + type.toString() + "]");
    }
}
