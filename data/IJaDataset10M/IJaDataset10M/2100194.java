package be.fedict.eid.idp.spi;

public class AttributeConfig {

    private final String name;

    private final String description;

    private final String uri;

    public AttributeConfig(String name, String description, String uri) {
        this.name = name;
        this.description = description;
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getUri() {
        return uri;
    }
}
