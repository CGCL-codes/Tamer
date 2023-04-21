package org.openremote.beehive.domain;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * The Class GCDeviceModel.
 */
@Entity
@SuppressWarnings("serial")
@Table(name = "gc_device_model")
public class GCDeviceModel extends BusinessEntity {

    private String name;

    private String type;

    private GCVendor gcVendor;

    private List<GCRemoteModel> gcRemoteModels;

    @Column(nullable = false)
    public String getName() {
        return name;
    }

    @Column(nullable = false)
    public String getType() {
        return type;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gc_vendor_oid", nullable = false)
    public GCVendor getGcVendor() {
        return gcVendor;
    }

    @ManyToMany
    @JoinTable(name = "gc_device_remote", joinColumns = { @JoinColumn(name = "gc_device_model_oid") }, inverseJoinColumns = { @JoinColumn(name = "gc_remote_model_oid") })
    public List<GCRemoteModel> getGcRemoteModels() {
        return gcRemoteModels;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setGcVendor(GCVendor gcVendor) {
        this.gcVendor = gcVendor;
    }

    public void setGcRemoteModels(List<GCRemoteModel> gcRemoteModels) {
        this.gcRemoteModels = gcRemoteModels;
    }
}
