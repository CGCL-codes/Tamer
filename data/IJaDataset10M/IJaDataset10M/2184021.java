package de.fraunhofer.isst.axbench.eastadlinterface.operations.adapter;

import org.eclipse.emf.common.notify.Notification;
import de.fraunhofer.isst.axbench.eastadlinterface.Env;
import de.fraunhofer.isst.axbench.eastadlinterface.util.BasicAdapter;
import de.fraunhofer.isst.eastadl.datatypes.DatatypesPackage;
import de.fraunhofer.isst.eastadl.datatypes.EADatatypePrototype;
import de.fraunhofer.isst.eastadl.functionmodeling.FunctionClientServerPort;

public class ParameterAdapter extends BasicAdapter {

    private FunctionClientServerPort eaPort;

    public ParameterAdapter(Env e, FunctionClientServerPort p) {
        super(e);
        eaPort = p;
    }

    @Override
    public boolean equalsImpl(Object obj) {
        if (obj instanceof ParameterAdapter) {
            ParameterAdapter that = (ParameterAdapter) obj;
            return this.env == that.env && this.eaPort == that.eaPort;
        } else {
            return false;
        }
    }

    @Override
    public boolean isAdapterForTypeImpl(Object type) {
        return type == ParameterAdapter.class;
    }

    @Override
    public void notifyChangedImpl(Notification notification) {
        int featureID = notification.getFeatureID(EADatatypePrototype.class);
        int eventType = notification.getEventType();
        switch(featureID) {
            case DatatypesPackage.EA_DATATYPE_PROTOTYPE__CATEGORY:
            case DatatypesPackage.EA_DATATYPE_PROTOTYPE__SHORT_NAME:
            case DatatypesPackage.EA_DATATYPE_PROTOTYPE__UUID:
            case DatatypesPackage.EA_DATATYPE_PROTOTYPE__UA_VALUE:
            case DatatypesPackage.EA_DATATYPE_PROTOTYPE__UA_TYPE:
            case DatatypesPackage.EA_DATATYPE_PROTOTYPE__OWNED_COMMENT:
                return;
            case DatatypesPackage.EA_DATATYPE_PROTOTYPE__NAME:
                {
                    if (eventType == Notification.SET) {
                        env.getFunctionPortConverter().patchType(eaPort);
                        env.refreshAxlEditor();
                    } else if (eventType == Notification.UNSET) {
                        env.getFunctionPortConverter().patchType(eaPort);
                        env.refreshAxlEditor();
                    }
                }
                return;
            case DatatypesPackage.EA_DATATYPE_PROTOTYPE__TYPE:
                {
                    if (eventType == Notification.SET) {
                        env.getFunctionPortConverter().patchType(eaPort);
                        env.refreshAxlEditor();
                    } else if (eventType == Notification.UNSET) {
                        env.getFunctionPortConverter().patchType(eaPort);
                        env.refreshAxlEditor();
                    }
                }
                return;
        }
    }
}
