package id.game;

public class Resource {

    private String name = "";

    public Resource(String resourceName) {
        setName(resourceName);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
