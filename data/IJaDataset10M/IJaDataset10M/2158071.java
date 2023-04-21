package org.nomadpim.module.schedule.core;

import java.util.ArrayList;
import java.util.List;
import org.nomadpim.core.util.event.DefaultContainerChangeListener;
import org.nomadpim.core.util.event.IExtendedContainerChangeListener;
import org.nomadpim.core.util.event.PropertyChangeEvent;
import org.nomadpim.core.util.test.TestersOnly;

public class DateInformationSourceComposite extends AbstractDateInformationSource {

    public IExtendedContainerChangeListener<IDateInformation> forwarder = new DefaultContainerChangeListener<IDateInformation>() {

        public void objectAdded(IDateInformation o) {
            fireAdd(o);
        }

        public void objectChanged(IDateInformation o, PropertyChangeEvent event) {
            fireChange(o, event);
        }

        public void objectRemoved(IDateInformation o) {
            fireRemove(o);
        }
    };

    private List<IDateInformationSource> children = new ArrayList<IDateInformationSource>();

    public void add(IDateInformationSource source) {
        children.add(source);
        source.addListener(forwarder);
    }

    public List<IDateInformation> get() {
        List<IDateInformation> result = new ArrayList<IDateInformation>();
        for (IDateInformationSource source : children) {
            result.addAll(source.get());
        }
        return result;
    }

    @TestersOnly
    public List<IDateInformationSource> getChildren() {
        return children;
    }
}
