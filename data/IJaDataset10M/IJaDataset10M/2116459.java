package org.bing.engine.core.domain;

public class Application extends AbstractEntity {

    private static final long serialVersionUID = 1978L;

    private String name;

    private String location;

    private String description;

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Application)) {
            return false;
        }
        Application app = (Application) obj;
        if (app.getId() == 0 && this.getId() == 0) {
            return name == null ? app.getName() == null : name.equals(app.getName());
        }
        return app.getId() == this.getId();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
