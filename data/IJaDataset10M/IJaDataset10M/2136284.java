package ru.unislabs.dbtier.samples.events;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import ru.unislabs.dbtier.DataChangeOperation;
import ru.unislabs.dbtier.factory.DAOFactory;
import ru.unislabs.dbtier.notification.DataChangeEvent;
import ru.unislabs.dbtier.notification.DataChangeListener;
import ru.unislabs.dbtier.samples.domain.DomainObject;

public class EventContentProvider implements IStructuredContentProvider {

    private List<DataChangeEvent> eventsList = new ArrayList<DataChangeEvent>();

    private DataChangeListener listener;

    private Viewer viewer;

    public Object[] getElements(Object inputElement) {
        return eventsList.toArray();
    }

    public void dispose() {
        if (listener != null) {
            DAOFactory.getInstance().getDataChangeNotificator().removeListener(DataChangeOperation.ALL, listener, new Class[] { DomainObject.class });
        }
    }

    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        eventsList.clear();
        this.viewer = viewer;
    }

    public EventContentProvider() {
        super();
        listener = new DataChangeListener() {

            public void afterDataChange(DataChangeEvent event) {
                eventsList.add(event);
                viewer.refresh();
            }

            public void beforeDataChange(DataChangeEvent event) {
                eventsList.add(event);
                viewer.refresh();
            }

            public void onCancel(DataChangeEvent event) {
                eventsList.add(event);
                viewer.refresh();
            }
        };
        DAOFactory.getInstance().getDataChangeNotificator().addListener(DataChangeOperation.ALL, listener, new Class[] { DomainObject.class });
    }
}
