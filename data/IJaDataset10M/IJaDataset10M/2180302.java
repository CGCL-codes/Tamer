package honeycrm.server.test.small.mocks;

import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.event.logical.shared.HasBeforeSelectionHandlers;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public class HasBeforeSelectionHandlersMock<T> implements HasBeforeSelectionHandlers<T> {

    @Override
    public void fireEvent(GwtEvent<?> event) {
    }

    @Override
    public HandlerRegistration addBeforeSelectionHandler(BeforeSelectionHandler<T> handler) {
        return null;
    }
}
