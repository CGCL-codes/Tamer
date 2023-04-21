package org.nakedobjects.viewer.web.task;

import org.nakedobjects.object.Naked;
import org.nakedobjects.object.NakedObject;
import org.nakedobjects.object.NakedObjectField;
import org.nakedobjects.object.NakedObjects;
import org.nakedobjects.object.NakedValue;
import org.nakedobjects.object.OneToOneAssociation;
import org.nakedobjects.object.ValueAssociation;
import org.nakedobjects.object.control.Consent;
import org.nakedobjects.viewer.web.component.Page;
import org.nakedobjects.viewer.web.request.Request;
import org.nakedobjects.viewer.web.request.Task;

public class EditTask extends Task {

    private static int size(NakedObject object) {
        NakedObjectField[] fields = object.getSpecification().getVisibleFields(object);
        int len = 0;
        for (int i = 0; i < fields.length; i++) {
            NakedObjectField fld = fields[i];
            if (skipField(object, fld)) {
                continue;
            }
            len++;
        }
        return len;
    }

    private static boolean skipField(NakedObject object, NakedObjectField fld) {
        return fld.isCollection() || fld.isDerived() || fld.isAvailable(object).isVetoed();
    }

    private NakedObjectField[] fields;

    EditTask(NakedObject object) {
        super("Edit", "", object, size(object));
        NakedObjectField[] allFields = object.getSpecification().getVisibleFields(object);
        fields = new NakedObjectField[names.length];
        for (int i = 0, j = 0; j < allFields.length; j++) {
            NakedObjectField fld = allFields[j];
            if (skipField(object, fld)) {
                continue;
            }
            fields[i] = fld;
            names[i] = fld.getName();
            types[i] = fld.getSpecification();
            initialState[i] = fld.get(object);
            options[i] = fld.getOptions(object);
            required[i] = fld.isMandatory();
            i++;
        }
    }

    public void checkForValidity(Request request) {
        NakedObject target = getTarget();
        Naked[] parameters = getParameters(request);
        int len = fields.length;
        for (int i = 0; i < len; i++) {
            if (errors[i] != null) {
                continue;
            }
            NakedObjectField fld = fields[i];
            if (fld.isValue()) {
                Consent valueValid = ((ValueAssociation) fld).isValueValid(target, (NakedValue) parameters[i]);
                errors[i] = valueValid.isVetoed() ? valueValid.getReason() : null;
            } else if (fld.isObject()) {
                Consent valueValid = ((OneToOneAssociation) fld).isAssociationValid(target, (NakedObject) parameters[i]);
                errors[i] = valueValid.isVetoed() ? valueValid.getReason() : null;
            }
        }
    }

    public Naked completeTask(Request request, Page page) {
        NakedObject target = getTarget();
        Naked[] parameters = getParameters(request);
        int len = fields.length;
        NakedObjects.getObjectPersistor().startTransaction();
        for (int i = 0; i < len; i++) {
            NakedObjectField fld = fields[i];
            if (fld.isValue()) {
                ((ValueAssociation) fld).setValue(target, parameters[i].getObject());
            } else if (fld.isObject()) {
                ((OneToOneAssociation) fld).setAssociation(target, (NakedObject) parameters[i]);
            }
        }
        if (target.getResolveState().isTransient()) {
            NakedObjects.getObjectPersistor().makePersistent(target);
        }
        NakedObjects.getObjectPersistor().endTransaction();
        return target;
    }

    public boolean isEditing() {
        return true;
    }
}
