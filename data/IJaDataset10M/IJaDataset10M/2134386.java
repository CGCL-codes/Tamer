package org.libreplan.web.orders;

import org.apache.commons.lang.Validate;
import org.libreplan.business.orders.entities.SchedulingState;
import org.libreplan.business.orders.entities.SchedulingState.ITypeChangedListener;
import org.libreplan.business.orders.entities.SchedulingState.Type;
import org.zkoss.zk.ui.HtmlMacroComponent;

/**
 * @author Óscar González Fernández <ogonzalez@igalia.com>
 */
public class SchedulingStateToggler extends HtmlMacroComponent {

    private final SchedulingState state;

    private boolean readOnly = false;

    public SchedulingStateToggler(SchedulingState state) {
        Validate.notNull(state);
        this.state = state;
        this.state.addTypeChangeListener(new ITypeChangedListener() {

            @Override
            public void typeChanged(Type newType) {
                recreate();
            }
        });
    }

    public boolean isScheduleButtonVisible() {
        return !readOnly && state.canBeScheduled();
    }

    public boolean isUnscheduleButtonVisible() {
        return !readOnly && state.canBeUnscheduled();
    }

    public void schedule() {
        state.schedule();
    }

    public void unschedule() {
        state.unschedule();
    }

    public String getButtonLabel() {
        return state.getStateAbbreviation();
    }

    public String getButtonTextTooltip() {
        return state.getStateName();
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }
}
