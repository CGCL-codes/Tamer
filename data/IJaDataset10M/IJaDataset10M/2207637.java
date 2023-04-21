package net.sf.depcon.event.ui.model;

import java.io.IOException;
import java.util.Collections;
import net.sf.depcon.Util;
import net.sf.depcon.model.event.Event;
import net.sf.depcon.ui.model.IModel;
import org.eclipse.core.databinding.observable.list.IListChangeListener;
import org.eclipse.core.databinding.observable.list.ListChangeEvent;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.emf.common.ui.URIEditorInput;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.ui.IEditorInput;

public class EventModel implements IModel {

    private static EventModel instance = null;

    private WritableList list = null;

    private Resource resource;

    private IListChangeListener listener = new IListChangeListener() {

        public void handleListChange(ListChangeEvent event) {
            try {
                resource.save(Collections.EMPTY_MAP);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    public static EventModel getInstance() {
        if (instance == null) {
            instance = new EventModel();
        }
        return instance;
    }

    private EventModel() {
        resource = Util.getInstance().getResource("FROM Event");
        WritableList writableList = new WritableList(resource.getContents(), Event.class);
        writableList.addListChangeListener(listener);
        list = writableList;
    }

    public WritableList getInput() {
        return list;
    }

    public IEditorInput createEditorInputFromObject(Object object) {
        if (object instanceof EObject) {
            return new URIEditorInput(EcoreUtil.getURI((EObject) object));
        }
        return null;
    }

    public Object getObjectFromEditorInput(IEditorInput input) {
        if (input instanceof URIEditorInput) {
            String fragment = ((URIEditorInput) input).getURI().fragment();
            return resource.getEObject(fragment);
        }
        return null;
    }

    public void save(Object object) {
        try {
            resource.save(Collections.EMPTY_MAP);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void delete(Object object) {
    }
}
