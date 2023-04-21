package org.nakedobjects.viewer.lightweight.view;

import org.nakedobjects.object.NakedClass;
import org.nakedobjects.object.NakedError;
import org.nakedobjects.object.NakedObject;
import org.nakedobjects.object.NakedObjectManager;
import org.nakedobjects.object.NotPersistableException;
import org.nakedobjects.object.collection.ArbitraryCollection;
import org.nakedobjects.viewer.lightweight.ObjectDrag;
import org.nakedobjects.viewer.lightweight.RootView;
import org.nakedobjects.viewer.lightweight.util.ViewFactory;

public class ArbitraryList extends StandardList implements RootView {

    public ArbitraryList() {
        setBorder(new RootBorder());
    }

    public NakedClass forNakedClass() {
        return null;
    }

    public String getName() {
        return "ArbitraryList";
    }

    public void dropObject(ObjectDrag drag) {
        NakedObject source = drag.getSourceObject();
        if (canAdd(source).isAllowed()) {
            if (source instanceof NakedClass) {
                source = (NakedObject) ((NakedClass) source).acquireInstance();
                try {
                    NakedObjectManager.getInstance().makePersistent(source);
                } catch (NotPersistableException e) {
                    source = new NakedError("Failed to create object", e);
                    RootView view = ViewFactory.getViewFactory().createRootView(source);
                    view.setLocation(drag.getRelativeLocation());
                    getWorkspace().addRootView(view);
                    return;
                }
                source.created();
            }
            if (canAdd(source).isAllowed()) {
                ((ArbitraryCollection) getObject()).add(source);
                invalidateLayout();
                validateLayout();
            }
        }
    }
}
