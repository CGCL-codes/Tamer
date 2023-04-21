package org.nakedobjects.example.expenses.recordedAction.impl;

import org.nakedobjects.applib.AbstractFactoryAndRepository;
import org.nakedobjects.applib.annotation.Hidden;
import org.nakedobjects.example.expenses.recordedAction.Actor;
import org.nakedobjects.example.expenses.recordedAction.RecordActionService;
import org.nakedobjects.example.expenses.recordedAction.RecordedActionContext;
import org.nakedobjects.example.expenses.services.UserFinder;
import java.util.Date;

public class RecordActionServiceImpl extends AbstractFactoryAndRepository implements RecordActionService {

    private UserFinder userFinder;

    /**
     * This property is not persisted, nor displayed to the user.
     */
    protected UserFinder getUserFinder() {
        return this.userFinder;
    }

    /**
     * Injected by the application container.
     */
    public void setUserFinder(final UserFinder userFinder) {
        this.userFinder = userFinder;
    }

    private void recordAction(final RecordedActionContext context, final String type, final String action, final String details) {
        final RecordedAction ra = newTransientInstance(RecordedAction.class);
        ra.setContext(context);
        ra.setType(type);
        ra.setName(action);
        ra.setDetails(details);
        ra.setActor((Actor) getUserFinder().currentUserAsObject());
        ra.setDate(new Date());
        persist(ra);
    }

    @Hidden
    public void recordMenuAction(final RecordedActionContext context, final String action, final String details) {
        recordAction(context, RecordedAction.ACTION, action, details);
    }

    @Hidden
    public void recordFieldChange(final RecordedActionContext context, final String fieldName, final Object previousContents, final Object newContents) {
        String fromValue;
        if (previousContents == null) {
            fromValue = "null";
        } else {
            fromValue = previousContents.toString();
        }
        String toValue;
        if (newContents == null) {
            toValue = "null";
        } else {
            toValue = newContents.toString();
        }
        if (fromValue.equals(toValue)) {
            return;
        }
        final String details = "From: " + fromValue + " to: " + toValue;
        recordAction(context, RecordedAction.CHANGE, fieldName, details);
    }
}
