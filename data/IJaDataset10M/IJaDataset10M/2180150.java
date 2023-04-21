package org.geonetwork.domain.ebrim.informationmodel.event;

import java.util.HashSet;
import java.util.Set;
import org.geonetwork.domain.ebrim.informationmodel.core.Identifiable;
import org.geonetwork.domain.ebrim.informationmodel.core.RegistryObject;
import org.geonetwork.domain.ebrim.informationmodel.core.datatype.URI;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

/**
 * 
 * @author Heikki Doeleman
 * 
 */
@Indexed
public class Notification extends RegistryObject {

    @IndexedEmbedded
    private URI subscription;

    @IndexedEmbedded
    private Set<Identifiable> registryObjectList;

    /**
	 * For Jixb binding.
	 * 
	 * @return
	 */
    @SuppressWarnings("unused")
    private static Set<Identifiable> setFactory() {
        return new HashSet<Identifiable>();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((registryObjectList == null) ? 0 : registryObjectList.hashCode());
        result = prime * result + ((subscription == null) ? 0 : subscription.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        if (getClass() != obj.getClass()) return false;
        Notification other = (Notification) obj;
        if (registryObjectList == null) {
            if (other.registryObjectList != null) return false;
        } else if (!registryObjectList.equals(other.registryObjectList)) return false;
        if (subscription == null) {
            if (other.subscription != null) return false;
        } else if (!subscription.equals(other.subscription)) return false;
        return true;
    }

    public URI getSubscription() {
        return subscription;
    }

    public void setSubscription(URI subscription) {
        this.subscription = subscription;
    }

    public Set<Identifiable> getRegistryObjectList() {
        return registryObjectList;
    }

    public void setRegistryObjectList(Set<Identifiable> registryObjectList) {
        this.registryObjectList = registryObjectList;
    }
}
