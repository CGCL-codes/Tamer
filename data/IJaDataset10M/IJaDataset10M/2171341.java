package net.zehrer.vse.controller;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.widgets.Event;

public class ActionManager extends Action {

    private List<IAction> listList = null;

    @Override
    public void run() {
        if (listList != null) {
            for (IAction controller : this.listList) {
                controller.run();
            }
        }
    }

    @Override
    public void runWithEvent(Event event) {
        if (listList != null) {
            for (IAction controller : this.listList) {
                controller.runWithEvent(event);
            }
        }
    }

    public void setActions(ArrayList<IAction> listList) {
        this.listList = listList;
    }
}
