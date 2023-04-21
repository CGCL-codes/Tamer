package google.maps;

import java.util.ArrayList;
import java.util.List;
import javax.jdo.annotations.Element;
import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Order;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class Markers implements javax.jdo.listener.StoreCallback {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent(mappedBy = "markers", defaultFetchGroup = "true")
    @Order(extensions = { @Extension(key = "list-ordering", value = "html ASC", vendorName = "datanucleus") })
    @Element(dependent = "true")
    private List<Marker> markers;

    @Persistent
    private String userEmail;

    @Persistent
    private Double mapCenterLat;

    @Persistent
    private Double mapCenterLng;

    @Persistent
    private Integer mapZoom;

    private Markers() {
        markers = new ArrayList();
    }

    public Key getKey() {
        return key;
    }

    public List getMarkers() {
        return markers;
    }

    public void setMarkers(List markers) {
        this.markers = markers;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Double getMapCenterLat() {
        return mapCenterLat;
    }

    public void setMapCenterLat(Double mapCenterLat) {
        this.mapCenterLat = mapCenterLat;
    }

    public Double getMapCenterLng() {
        return mapCenterLng;
    }

    public void setMapCenterLng(Double mapCenterLng) {
        this.mapCenterLng = mapCenterLng;
    }

    public Integer getMapZoom() {
        return mapZoom;
    }

    public void setMapZoom(Integer mapZoom) {
        this.mapZoom = mapZoom;
    }

    public void jdoPreStore() {
        UserService userService = UserServiceFactory.getUserService();
        if (userService.getCurrentUser() != null) {
            userEmail = userService.getCurrentUser().getEmail();
        }
    }
}
