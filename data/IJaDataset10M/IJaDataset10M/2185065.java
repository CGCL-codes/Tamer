package edu.kit.cm.kitcampusguide.model;

import java.util.ArrayList;
import java.util.Collection;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * @author Kateryna Yurchenko
 * @author Tobias Zündorf
 * @author Roland Steinegger, Karlsruhe Institute of Technology
 */
public class POICategory extends AbstractEntity implements Entity {

    private String name;

    private String icon;

    private String description;

    private boolean visible;

    private Collection<POI> pois;

    public POICategory() {
        this(null, null, null, null);
    }

    public POICategory(String name, Integer id, String icon, String description) {
        this(name, id, icon, description, new ArrayList<POI>());
    }

    public POICategory(String name, Integer id, String icon, String description, Collection<POI> pois) {
        this.name = name;
        this.icon = icon;
        this.description = description;
        this.pois = new ArrayList<POI>(pois);
        this.visible = false;
        setId(id);
    }

    public void add(POI poi) {
        if (!this.pois.contains(poi)) {
            this.pois.add(poi);
        }
        if (poi != null && (poi.getCategory() == null || !poi.getCategory().equals(this))) {
            poi.setCategory(this);
        }
    }

    public void remove(POI poi) {
        if (this.pois.contains(poi)) {
            this.pois.remove(poi);
        }
        if (this.equals(poi.getCategory())) {
            poi.setCategory(null);
        }
    }

    public Collection<POI> getPois() {
        return this.pois;
    }

    public void setPois(Collection<POI> pois) {
        if (this.pois != null) {
            for (POI poi : new ArrayList<POI>(this.pois)) {
                this.remove(poi);
            }
        }
        if (pois != null) {
            for (POI poi : new ArrayList<POI>(pois)) {
                this.add(poi);
            }
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIcon() {
        return this.icon;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return this.visible;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((icon == null) ? 0 : icon.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + (visible ? 1231 : 1237);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        POICategory other = (POICategory) obj;
        if (description == null) {
            if (other.description != null) return false;
        } else if (!description.equals(other.description)) return false;
        if (icon == null) {
            if (other.icon != null) return false;
        } else if (!icon.equals(other.icon)) return false;
        if (name == null) {
            if (other.name != null) return false;
        } else if (!name.equals(other.name)) return false;
        if (visible != other.visible) return false;
        return true;
    }

    @Override
    public POICategory clone() {
        POICategory clone = new POICategory();
        clone.description = this.description;
        clone.icon = this.icon;
        clone.name = this.name;
        clone.visible = this.visible;
        clone.pois = this.pois;
        return clone;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
