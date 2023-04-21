package org.fao.fenix.domain.map.external;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import org.fao.fenix.domain.map.layer.ExternalWMSLayer;

/**
 *
 * @author etj
 */
@Entity
public class WMSServer {

    @Id
    @GeneratedValue
    private Long id;

    private String updateSeq = "dummyValue";

    private String wmsver;

    private String title;

    private String getCapaURL;

    @OneToMany(cascade = CascadeType.ALL)
    private List<ExternalWMSLayer> layerList = new ArrayList<ExternalWMSLayer>();

    public WMSServer() {
    }

    public WMSServer(String title, String getCapaURL) {
        this.title = title;
        this.getCapaURL = getCapaURL;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWmsver() {
        return wmsver;
    }

    public void setWmsver(String wmsver) {
        this.wmsver = wmsver;
    }

    public String getGetCapaURL() {
        return getCapaURL;
    }

    public void setGetCapaURL(String getCapaURL) {
        this.getCapaURL = getCapaURL;
    }

    public List<ExternalWMSLayer> getLayerList() {
        return layerList;
    }

    public void setLayerList(List<ExternalWMSLayer> layerList) {
        this.layerList = layerList;
    }

    public void addLayer(ExternalWMSLayer layer) {
        layerList.add(layer);
    }
}
