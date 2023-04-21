package pierre.model;

import pedro.util.DisplayNameProvider;
import java.io.Serializable;

public abstract class QueryObject implements DisplayNameProvider, Serializable {

    private String identifier;

    private String name;

    private String description;

    public QueryObject() {
        identifier = "";
        name = "";
        description = "";
    }

    /**
	* Get the value of identifier.
	* @return value of identifier.
	*/
    public String getIdentifier() {
        return identifier;
    }

    /**
	* Get the value of name.
	* @return value of name.
	*/
    public String getName() {
        return name;
    }

    /**
	* Get the value of description.
	* @return value of description.
	*/
    public String getDescription() {
        return description;
    }

    public abstract String[] getFieldNames();

    public void print() {
        System.out.println("Id=" + identifier + "==");
        System.out.println("Name=" + name + "==");
        System.out.println("Description=" + description + "==");
    }

    /**
	* Set the value of identifier.
	* @param name Value to assign to identifier.
	*/
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    /**
	* Set the value of name.
	* @param name Value to assign to name.
	*/
    public void setName(String name) {
        this.name = name;
    }

    /**
	* Set the value of description.
	* @param description Value to assign to description.
	*/
    public void setDescription(String description) {
        this.description = description;
    }

    public void copyAttributeValues(QueryObject queryObject) {
        queryObject.setName(name);
        queryObject.setDescription(description);
        queryObject.setIdentifier(identifier);
    }
}
