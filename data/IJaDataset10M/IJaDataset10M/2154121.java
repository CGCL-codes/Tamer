package org.nakedobjects.viewer.nuthatch;

import org.nakedobjects.object.NakedCollection;
import org.nakedobjects.object.NakedObject;
import org.nakedobjects.object.NakedObjectField;
import org.nakedobjects.utility.ToString;
import java.util.Enumeration;

public class ObjectContext implements Context {

    private final NakedObject object;

    public ObjectContext(NakedObject object) {
        this.object = object;
    }

    public String debug() {
        String objectTitle = object == null ? "null" : object.titleString();
        return "Object, '" + objectTitle + "'";
    }

    public void display(View view) {
        view.displayAll(object);
    }

    public NakedObject getObject() {
        return object;
    }

    public Context getObject(String lowecaseTitle) {
        String title = object.titleString().toLowerCase();
        if (title != null && title.indexOf(lowecaseTitle) >= 0) {
            return new ObjectContext(object);
        }
        NakedObjectField[] fields = object.getSpecification().getVisibleFields(object);
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].isObject()) {
                NakedObject fld = (NakedObject) fields[i].get(object);
                if (fld != null) {
                    title = fld.titleString();
                    if (title != null && title.toLowerCase().indexOf(lowecaseTitle) >= 0) {
                        return new ObjectContext(fld);
                    }
                }
                if (fields[i].getId().toLowerCase().indexOf(lowecaseTitle) >= 0) {
                    NakedObject field = (NakedObject) fields[i].get(object);
                    return new ObjectContext(field);
                }
            } else if (fields[i].isCollection()) {
                NakedCollection coll = (NakedCollection) fields[i].get(object);
                Enumeration e = coll.elements();
                while (e.hasMoreElements()) {
                    NakedObject element = (NakedObject) e.nextElement();
                    title = element.titleString();
                    if (title != null && title.toLowerCase().indexOf(lowecaseTitle) >= 0) {
                        return new ObjectContext(element);
                    }
                }
                if (fields[i].getName().toLowerCase().indexOf(lowecaseTitle) >= 0) {
                    NakedCollection collection = (NakedCollection) fields[i].get(object);
                    return new CollectionContext(collection, fields[i].getName());
                }
            }
        }
        return null;
    }

    public String getPrompt() {
        return object.titleString() + " (" + object.getSpecification().getSingularName() + ")";
    }

    public void objects(View view) {
        view.display(object);
        NakedObjectField[] fields = object.getSpecification().getVisibleFields(object);
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].isObject()) {
                NakedObject fld = (NakedObject) fields[i].get(object);
                if (fld != null) {
                    view.display(fld);
                }
            }
        }
    }

    public String toString() {
        ToString str = new ToString(this);
        str.append("object", object);
        return str.toString();
    }
}
