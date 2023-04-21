package de.schlund.pfixcore.webservice.beans.metadata;

/**
 * @author mleidig@schlund.de
 */
public class Property {

    String name;

    String alias;

    boolean exclude;

    boolean include;

    public Property(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getAlias() {
        return alias;
    }

    public boolean isExcluded() {
        return exclude;
    }

    public void exclude() {
        exclude = true;
    }

    public boolean isIncluded() {
        return include;
    }

    public void include() {
        include = true;
    }
}
