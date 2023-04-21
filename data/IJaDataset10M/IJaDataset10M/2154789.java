package com.nexirius.framework.remote;

import com.nexirius.framework.datamodel.*;
import java.io.PushbackInputStream;

/**
 * @author Marcel Baumann
 */
public class DataModelMethodEvent extends DataModelRemoteEvent {

    public DataModelMethodEvent(DataModelEvent event) throws Exception {
        if (event == null) {
            return;
        }
        DataModel transmitter = event.getTransmitter();
        if (transmitter == null) {
            transmitter = event.getDataModel();
        }
        if (transmitter == null || !(transmitter instanceof RemoteDataModel)) {
            return;
        }
        RemoteDataModel remote = (RemoteDataModel) transmitter;
        append(new IntModel(remote.getID(), ID));
        append(new StringModel(event.getChildName(), CHILD_NAME));
        append(new IntModel(event.getSubtype(), SUBTYPE));
        append(new StringModel(event.getData(), VALUE));
    }

    public DataModelMethodEvent(PushbackInputStream in) throws Exception {
        initMembers();
        readDataFrom(in);
    }

    private void initMembers() {
        append(new IntModel(-1, ID));
        append(new StringModel("", CHILD_NAME));
        append(new StringModel("", VALUE));
    }

    public String applyOn(DataModel model) throws Exception {
        DataModel target = model;
        String childName = getChildName();
        int subtype = getSubtype();
        if (childName.length() > 0) {
            target = model.getChild(childName);
            if (target == null) {
                throw new Exception("No child '" + childName + "' on " + model.getClass().getName());
            }
        }
        if (target != null) {
            DataModelCommand meth = target.getMethod(getNewValue());
            if (meth != null) {
                meth.doAction();
                return "OK";
            } else {
                throw new Exception("No method '" + getNewValue() + "' on " + model.getClass().getName());
            }
        }
        return "FAILED";
    }
}
