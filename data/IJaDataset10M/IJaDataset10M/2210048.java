package by.bsuir.picasso.shared;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import com.google.gwt.user.client.rpc.IsSerializable;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class PolyStorage implements IsSerializable {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;

    @Persistent
    private Long mapId;

    @Persistent
    private String name;

    @Persistent
    private String points;

    @Persistent
    private String levels;

    @Persistent
    private Integer zoomLevel;

    /**
   * @see by.bsuir.picasso.shared.MapTypes
   */
    @Persistent
    private String type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMapId() {
        return mapId;
    }

    public void setMapId(Long mapId) {
        this.mapId = mapId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
   * @see by.bsuir.picasso.shared.MapTypes
   */
    public String getType() {
        return type;
    }

    /**
   * @see by.bsuir.picasso.shared.MapTypes
   */
    public void setType(String type) {
        if (type == MapTypes.POLYGON || type == MapTypes.POLYLINE) {
            this.type = type;
        }
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getLevels() {
        return levels;
    }

    public void setLevels(String levels) {
        this.levels = levels;
    }

    public Integer getZoomLevel() {
        return zoomLevel;
    }

    public void setZoomLevel(Integer zoomLevel) {
        this.zoomLevel = zoomLevel;
    }
}
