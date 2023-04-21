package org.illico.web.widget.basic;

import org.illico.common.component.Published;
import org.illico.web.widget.common.AbstractText;
import org.illico.web.widget.event.ActionEvent;
import org.illico.web.widget.event.ActionListener;
import org.illico.web.widget.event.ActionManager;
import org.illico.web.widget.event.ActionableWidget;
import org.illico.web.widget.event.EventManager;
import org.illico.web.widget.event.ListenerList;

public class Label<T> extends AbstractText<T> implements ActionableWidget {

    private EventManager<ActionListener, ActionEvent, ActionableWidget> actionManager;

    @Override
    protected void create() {
        super.create();
        actionManager = new ActionManager(this);
    }

    public ListenerList<ActionListener, ActionEvent, ActionableWidget> getActionListeners() {
        return actionManager.getListeners();
    }

    public void addActionListener(ActionListener listener) {
        getActionListeners().add(listener);
    }

    public void removeActionListener(ActionListener listener) {
        getActionListeners().remove(listener);
    }

    @Published
    public void act() {
        actionManager.fireEvent(new ActionEvent(this));
    }

    public boolean isActionListened() {
        return actionManager.isListened();
    }
}
