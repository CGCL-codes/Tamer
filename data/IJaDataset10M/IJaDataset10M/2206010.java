package ink.engine;

/**
 * This class represents a Location in the project.
 * It contains the important information every Location has.
 * 
 * @author Ole Schwiegert
 */
public class Location {

    private String name, shortd;

    /**
     * Creates a location with its name and shortdescription
     * 
     * @param name Name of the new location
     * @param shortd Shortdescription of the new location
     */
    public Location(String name, String shortd) {
        this.name = name;
        this.shortd = shortd;
    }

    /**
     * Getter fot the attributes
     * 
     * @param index Two possibilities: name,shortd
     * @return Value of chosen attribute
     */
    public String getAtt(String index) {
        String output = null;
        if (index.equals("name")) {
            output = this.name;
        }
        if (index.equals("shortd")) {
            output = this.shortd;
        }
        return output;
    }
}
