package fab.formatic.backend.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import fab.formatic.backend.domain.enumerate.FabTypeCharging;

/**
 *
 * @author Hokcay
 */
@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = "serviceID"), @UniqueConstraint(columnNames = "serviceName") })
public class FabService implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(length = 30, unique = true)
    private String serviceName;

    @Column(unique = true)
    private String serviceID;

    @Column(length = 500)
    private String serviceDesc;

    private String serviceType;

    @Enumerated(EnumType.STRING)
    private FabTypeCharging typeCharging;

    @Column(length = 100)
    private String typeChargingDesc;

    @OneToMany(mappedBy = "fabService", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private List<FabLogin> fabLogins = new ArrayList<FabLogin>();

    @OneToMany(mappedBy = "fabService", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private List<FabPackage> fabPackages = new ArrayList<FabPackage>();

    @OneToMany(mappedBy = "fabService", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private List<FabTariff> fabTariffs = new ArrayList<FabTariff>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getServiceDesc() {
        return serviceDesc;
    }

    public void setServiceDesc(String serviceDesc) {
        this.serviceDesc = serviceDesc;
    }

    public String getServiceID() {
        if (serviceID == null) {
            serviceID = getServiceName().toUpperCase();
        }
        return serviceID;
    }

    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public FabTypeCharging getTypeCharging() {
        return typeCharging;
    }

    public void setTypeCharging(FabTypeCharging typeCharging) {
        this.typeCharging = typeCharging;
    }

    public String getTypeChargingDesc() {
        return typeChargingDesc;
    }

    public void setTypeChargingDesc(String typeChargingDesc) {
        this.typeChargingDesc = typeChargingDesc;
    }

    public List<FabLogin> getFabLogins() {
        return fabLogins;
    }

    public void setFabLogins(List<FabLogin> fabLogins) {
        this.fabLogins = fabLogins;
    }

    public List<FabPackage> getFabPackages() {
        return fabPackages;
    }

    public void setFabPackages(List<FabPackage> fabPackages) {
        this.fabPackages = fabPackages;
    }

    public List<FabTariff> getFabTariffs() {
        return fabTariffs;
    }

    public void setFabTariffs(List<FabTariff> fabTariffs) {
        this.fabTariffs = fabTariffs;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FabService other = (FabService) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if ((this.serviceName == null) ? (other.serviceName != null) : !this.serviceName.equals(other.serviceName)) {
            return false;
        }
        if ((this.serviceID == null) ? (other.serviceID != null) : !this.serviceID.equals(other.serviceID)) {
            return false;
        }
        if ((this.serviceDesc == null) ? (other.serviceDesc != null) : !this.serviceDesc.equals(other.serviceDesc)) {
            return false;
        }
        if ((this.serviceType == null) ? (other.serviceType != null) : !this.serviceType.equals(other.serviceType)) {
            return false;
        }
        if ((this.typeCharging == null) ? (other.typeCharging != null) : !this.typeCharging.equals(other.typeCharging)) {
            return false;
        }
        if ((this.typeChargingDesc == null) ? (other.typeChargingDesc != null) : !this.typeChargingDesc.equals(other.typeChargingDesc)) {
            return false;
        }
        if (this.fabLogins != other.fabLogins && (this.fabLogins == null || !this.fabLogins.equals(other.fabLogins))) {
            return false;
        }
        if (this.fabPackages != other.fabPackages && (this.fabPackages == null || !this.fabPackages.equals(other.fabPackages))) {
            return false;
        }
        if (this.fabTariffs != other.fabTariffs && (this.fabTariffs == null || !this.fabTariffs.equals(other.fabTariffs))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 43 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 43 * hash + (this.serviceName != null ? this.serviceName.hashCode() : 0);
        hash = 43 * hash + (this.serviceID != null ? this.serviceID.hashCode() : 0);
        hash = 43 * hash + (this.serviceDesc != null ? this.serviceDesc.hashCode() : 0);
        hash = 43 * hash + (this.serviceType != null ? this.serviceType.hashCode() : 0);
        hash = 43 * hash + (this.typeCharging != null ? this.typeCharging.hashCode() : 0);
        hash = 43 * hash + (this.typeChargingDesc != null ? this.typeChargingDesc.hashCode() : 0);
        hash = 43 * hash + (this.fabLogins != null ? this.fabLogins.hashCode() : 0);
        hash = 43 * hash + (this.fabPackages != null ? this.fabPackages.hashCode() : 0);
        hash = 43 * hash + (this.fabTariffs != null ? this.fabTariffs.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "FabService{" + "id=" + id + ", serviceName=" + serviceName + ", serviceID=" + serviceID + ", serviceDesc=" + serviceDesc + ", serviceType=" + serviceType + ", typeCharging=" + typeCharging + ", typeChargingDesc=" + typeChargingDesc + ", fabLogins=" + fabLogins + ", fabPackages=" + fabPackages + ", fabTariffs=" + fabTariffs + '}';
    }
}
