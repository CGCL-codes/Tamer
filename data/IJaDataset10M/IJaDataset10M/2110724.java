package de.fraunhofer.isst.axbench.eastadlinterface.models.adapter;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import de.fraunhofer.isst.axbench.eastadlinterface.Env;

public class ResourceSetAdapter extends AdapterImpl {

    private Env env;

    public ResourceSetAdapter(Env e) {
        env = e;
    }

    public boolean isAdapterForType(Object type) {
        return type == ResourceSetAdapter.class;
    }

    public void notifyChanged(Notification notification) {
        if (env.areAdaptersOff()) return;
        if (!(notification.getNotifier() instanceof ResourceSet)) {
            System.err.println("ResourceSetAdapter: notifier is no ResourceSet!");
            return;
        }
        int featureID = notification.getFeatureID(ResourceSet.class);
        if (featureID != ResourceSet.RESOURCE_SET__RESOURCES) return;
        int eventType = notification.getEventType();
        switch(eventType) {
            case Notification.ADD:
                env.getAdapterFactory().adapt((Resource) notification.getNewValue());
                displayWarning();
                return;
            case Notification.ADD_MANY:
                for (Object newValue : (EList<?>) notification.getNewValue()) env.getAdapterFactory().adapt((Resource) newValue);
                displayWarning();
                return;
            case Notification.REMOVE:
                env.getAdapterFactory().remove((Resource) notification.getOldValue());
                displayWarning();
                return;
            case Notification.REMOVE_MANY:
                for (Object oldValue : (EList<?>) notification.getOldValue()) env.getAdapterFactory().remove((Resource) oldValue);
                displayWarning();
                return;
        }
    }

    public void displayWarning() {
        System.err.println("please do not change the resource set while models are synchronized.");
        System.err.println("this operation is not supported yet, please undo or");
        System.err.println("synchronization might be broken.");
    }
}
