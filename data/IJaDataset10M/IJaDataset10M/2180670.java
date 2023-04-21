package net.sf.brightside.luxurycruise.metamodel.beans;

import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import net.sf.brightside.luxurycruise.core.beans.BaseBean;
import net.sf.brightside.luxurycruise.metamodel.Cruise;
import net.sf.brightside.luxurycruise.metamodel.Location;
import org.apache.tapestry.beaneditor.Validate;
import org.hibernate.annotations.Cascade;

@Entity
public class LocationBean extends BaseBean implements Location {

    private String name;

    private List<Cruise> cruises;

    public LocationBean() {
    }

    public LocationBean(String name) {
        this.name = name;
    }

    @Validate("required")
    @Override
    @Basic
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    @ManyToMany(mappedBy = "itinerary", targetEntity = CruiseBean.class, cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH }, fetch = FetchType.EAGER)
    @Cascade({ org.hibernate.annotations.CascadeType.MERGE, org.hibernate.annotations.CascadeType.PERSIST, org.hibernate.annotations.CascadeType.REFRESH })
    public List<Cruise> getCruises() {
        return cruises;
    }

    @Override
    public void setCruises(List<Cruise> cruises) {
        this.cruises = cruises;
    }

    @Override
    public String toString() {
        return getName();
    }
}
