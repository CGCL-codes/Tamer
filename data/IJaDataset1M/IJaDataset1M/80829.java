package net.sf.brightside.simpark.metamodel.beans;

import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import net.sf.brightside.simpark.core.beans.BaseBean;
import net.sf.brightside.simpark.metamodel.Owner;
import net.sf.brightside.simpark.metamodel.Parcel;

@Entity
public class OwnerBean extends BaseBean implements Owner {

    private String name;

    private List<Parcel> parcels;

    public OwnerBean() {
    }

    @Basic
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    @ManyToMany(targetEntity = ParcelBean.class)
    public List<Parcel> getParcels() {
        return parcels;
    }

    @Override
    public void setParcels(List<Parcel> parcels) {
        this.parcels = parcels;
    }
}
