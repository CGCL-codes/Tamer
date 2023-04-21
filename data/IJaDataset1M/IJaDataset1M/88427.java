package de.fraunhofer.isst.axbench.eastadlinterface.models.adapter;

import org.eclipse.emf.common.notify.Notification;
import de.fraunhofer.isst.axbench.eastadlinterface.Env;
import de.fraunhofer.isst.axbench.eastadlinterface.util.BasicAdapter;
import de.fraunhofer.isst.eastadl.systemmodeling.DesignLevel;
import de.fraunhofer.isst.eastadl.systemmodeling.SystemModel;
import de.fraunhofer.isst.eastadl.systemmodeling.SystemmodelingPackage;

public class SystemModelAdapter extends BasicAdapter {

    public SystemModelAdapter(Env e) {
        super(e);
    }

    @Override
    public boolean equalsImpl(Object obj) {
        if (obj instanceof SystemModelAdapter) {
            SystemModelAdapter that = (SystemModelAdapter) obj;
            return this.env == that.env;
        } else {
            return false;
        }
    }

    @Override
    public boolean isAdapterForTypeImpl(Object type) {
        return type == SystemModelAdapter.class;
    }

    @Override
    public void notifyChangedImpl(Notification notification) {
        int featureID = notification.getFeatureID(SystemModel.class);
        int eventType = notification.getEventType();
        switch(featureID) {
            case SystemmodelingPackage.SYSTEM_MODEL__CATEGORY:
            case SystemmodelingPackage.SYSTEM_MODEL__SHORT_NAME:
            case SystemmodelingPackage.SYSTEM_MODEL__UUID:
            case SystemmodelingPackage.SYSTEM_MODEL__UA_VALUE:
            case SystemmodelingPackage.SYSTEM_MODEL__UA_TYPE:
            case SystemmodelingPackage.SYSTEM_MODEL__OWNED_COMMENT:
            case SystemmodelingPackage.SYSTEM_MODEL__OWNED_RELATIONSHIP:
            case SystemmodelingPackage.SYSTEM_MODEL__TRACEABLE_SPECIFICATION:
                return;
            case SystemmodelingPackage.SYSTEM_MODEL__NAME:
                {
                }
                return;
            case SystemmodelingPackage.SYSTEM_MODEL__DESIGN_LEVEL:
                {
                    if (eventType == Notification.SET) {
                        env.getMultiAdapterFactory().adapt((DesignLevel) notification.getNewValue());
                        env.getSystemModelConverter().patch((DesignLevel) notification.getNewValue());
                        env.refreshAxlEditor();
                    } else if (eventType == Notification.UNSET) {
                        env.getMultiAdapterFactory().remove((DesignLevel) notification.getOldValue());
                        env.getSystemModelConverter().patch((DesignLevel) null);
                        env.refreshAxlEditor();
                    }
                }
                return;
        }
    }
}
