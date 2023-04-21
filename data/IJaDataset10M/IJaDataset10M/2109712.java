package com.anothergtdapp.controller;

import com.anothergtdapp.db.Action;
import com.anothergtdapp.db.Project;
import com.anothergtdapp.db.State;
import java.util.List;

/**
 *
 * @author adolfo
 */
public interface ChangeListener {

    public void structureChanged(Project parent);

    public void dataChanged(Project data);

    public void dataChanged(Action act);

    public void selectedProjectChanged(Project oldProj, Project newProj);

    public void selectedActionChanged(Action oldProj, Action newProj);

    public void newInboxAction(Action newAction);

    public void stateChanged(List<State> states);

    public void configChanged();
}
