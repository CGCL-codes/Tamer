package si.mk.k3.model.listeners;

import si.mk.k3.model.ElementID;
import si.mk.k3.model.K3Element;

public interface ModelChangeListener {

    void elementChanged(K3Element elementD);

    void elementRemoved(ElementID elID);

    void elementAdded(K3Element elementD);
}
