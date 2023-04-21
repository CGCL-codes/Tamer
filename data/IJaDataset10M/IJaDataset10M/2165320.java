package leland.domain.networking;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import leland.domain.base.BaseEntity;

@Entity
@Table(name = "LND_BANDWIDTH_ALLOCATION")
public class BandwidthAllocation extends BaseEntity {

    private int cir;

    private int mir;

    private Set<NetworkAddress> networkAddress = new HashSet<NetworkAddress>();

    private int filterPrio = 50;

    private int classPrio = 50;

    public int getCir() {
        return this.cir;
    }

    public void setCir(int cir) {
        this.cir = cir;
    }

    public int getMir() {
        return this.mir;
    }

    public void setMir(int mir) {
        this.mir = mir;
    }

    @OneToMany(cascade = { CascadeType.REFRESH })
    public Set<NetworkAddress> getNetworkAddress() {
        return this.networkAddress;
    }

    public void setNetworkAddress(Set<NetworkAddress> networkAddress) {
        this.networkAddress = networkAddress;
    }

    public int getFilterPrio() {
        return this.filterPrio;
    }

    public void setFilterPrio(int filterPrio) {
        this.filterPrio = filterPrio;
    }

    public int getClassPrio() {
        return this.classPrio;
    }

    public void setClassPrio(int classPrio) {
        this.classPrio = classPrio;
    }
}
