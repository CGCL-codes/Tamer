package org.osmius.model;

public class OsmUserscriptTypplatform implements java.io.Serializable {

    private OsmUserscriptTypplatformId id;

    private OsmUserscripts osmUserscripts;

    private OsmTypplatform osmTypplatform;

    private static final long serialVersionUID = 8731764791438951142L;

    public OsmUserscriptTypplatform() {
    }

    public OsmUserscriptTypplatform(OsmUserscriptTypplatformId id) {
        this.id = id;
    }

    public OsmUserscriptTypplatform(OsmUserscriptTypplatformId id, OsmUserscripts osmUserscripts, OsmTypplatform osmTypplatform) {
        this.id = id;
        this.osmUserscripts = osmUserscripts;
        this.osmTypplatform = osmTypplatform;
    }

    public OsmUserscriptTypplatformId getId() {
        return this.id;
    }

    public void setId(OsmUserscriptTypplatformId id) {
        this.id = id;
    }

    public OsmUserscripts getOsmUserscripts() {
        return osmUserscripts;
    }

    public void setOsmUserscripts(OsmUserscripts osmUserscripts) {
        this.osmUserscripts = osmUserscripts;
    }

    public OsmTypplatform getOsmTypplatform() {
        return osmTypplatform;
    }

    public void setOsmTypplatform(OsmTypplatform osmTypplatform) {
        this.osmTypplatform = osmTypplatform;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OsmUserscriptTypplatform that = (OsmUserscriptTypplatform) o;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (osmTypplatform != null ? !osmTypplatform.equals(that.osmTypplatform) : that.osmTypplatform != null) return false;
        if (osmUserscripts != null ? !osmUserscripts.equals(that.osmUserscripts) : that.osmUserscripts != null) return false;
        return true;
    }

    public int hashCode() {
        int result;
        result = (id != null ? id.hashCode() : 0);
        result = 31 * result + (osmUserscripts != null ? osmUserscripts.hashCode() : 0);
        result = 31 * result + (osmTypplatform != null ? osmTypplatform.hashCode() : 0);
        return result;
    }

    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("OsmUserscriptTypplatform");
        sb.append("{id=").append(id);
        sb.append(", osmUserscripts=").append(osmUserscripts);
        sb.append(", osmTypplatform=").append(osmTypplatform);
        sb.append('}');
        return sb.toString();
    }
}
