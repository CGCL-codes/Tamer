package us.wthr.jdem846.shapefile.modeling;

public class FeatureTypeGroup {

    private String name;

    private String id;

    private FeatureTypeStroke defaultStroke;

    public FeatureTypeGroup(String name, String id) {
        this.name = name;
        this.id = id;
        this.defaultStroke = null;
    }

    public FeatureTypeGroup(String name, String id, FeatureTypeStroke defaultStroke) {
        this.name = name;
        this.id = id;
        this.defaultStroke = defaultStroke;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public FeatureTypeStroke getDefaultStroke() {
        return defaultStroke;
    }

    public void setDefaultStroke(FeatureTypeStroke defaultStroke) {
        this.defaultStroke = defaultStroke;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof FeatureTypeGroup)) return false;
        FeatureTypeGroup other = (FeatureTypeGroup) object;
        return this.id.equals(other.id);
    }
}
