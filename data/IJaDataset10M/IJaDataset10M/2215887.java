package org.rapla.gui.internal.action;

import java.awt.event.ActionEvent;
import org.rapla.framework.RaplaContext;
import org.rapla.framework.RaplaException;
import org.rapla.gui.RaplaAction;
import org.rapla.gui.internal.common.CalendarSelectionModel;

public class OnlyMyAction extends RaplaAction {

    CalendarSelectionModel model;

    public OnlyMyAction(RaplaContext sm, CalendarSelectionModel model) throws RaplaException {
        super(sm);
        this.model = model;
        this.setEnabled(true);
        putValue(NAME, getString("only_own_reservations"));
    }

    public CalendarSelectionModel getModel() {
        return model;
    }

    public void setModel(CalendarSelectionModel model) {
    }

    public void actionPerformed(ActionEvent evt) {
        try {
            boolean isSelected = model.isOnlyCurrentUserSelected();
            if (!isSelected) {
                model.selectUser(getUser());
            } else {
                model.selectUser(null);
            }
            firePropertyChange("model", new Object(), model);
        } catch (RaplaException ex) {
        }
    }
}
