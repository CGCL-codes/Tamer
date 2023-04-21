package com.anothergtdapp.controller;

import com.anothergtdapp.db.State;
import com.anothergtdapp.model.ActionTableModel;
import com.anothergtdapp.db.Action;
import com.anothergtdapp.db.DBController;
import com.anothergtdapp.db.Project;
import com.anothergtdapp.view.ActionTableHighlighter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.Highlighter;
import org.jdesktop.swingx.decorator.HighlighterFactory;

/**
 *
 * @author adolfo
 */
public class ActionTableController implements ChangeListener, ListSelectionListener {

    private JXTable table;

    private ActionTableModel model;

    private DBController db;

    private JScrollPane tableScrollPane;

    private ChangeController changeController;

    private Action lastAction = null;

    private Project actualProject;

    private HashMap<Integer, State> states = null;

    public ActionTableController(JXTable table, JScrollPane tableScroll, DBController db, ChangeController cc) {
        this.table = table;
        this.db = db;
        this.changeController = cc;
        this.tableScrollPane = tableScroll;
        this.actualProject = null;
        states = new HashMap<Integer, State>();
        states.put(State.WORKING.getId(), State.WORKING);
        changeController.registerSelectedProjectChanged(this);
        changeController.registerDataActionChanged(this);
        changeController.registerStateChangedListener(this);
        changeController.registerConfigListener(this);
        model = new ActionTableModel();
        table.setModel(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(this);
        new ActionTablePopup(table, this, changeController);
        new ActionTableDragAndDrop(table, this);
        setHightLigther();
    }

    /**
     * Set a highlighter in the table.
     *
     */
    public void setHightLigther() {
        Highlighter[] highlighters = new Highlighter[] { HighlighterFactory.createAlternateStriping(), HighlighterFactory.createSimpleStriping() };
        highlighters = new Highlighter[] { new ActionTableHighlighter(table, changeController) };
        table.setHighlighters(highlighters);
    }

    public void structureChanged(Project parent) {
        model.setActions(filter(db.getActionByProject(actualProject)));
        table.tableChanged(new TableModelEvent(table.getModel()));
    }

    public void dataChanged(Project data) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void selectedProjectChanged(Project oldProj, Project newProj) {
        this.actualProject = newProj;
        model.setActions(filter(db.getActionByProject(newProj)));
        table.resetSortOrder();
        table.getSelectionModel().clearSelection();
        table.tableChanged(new TableModelEvent(table.getModel()));
        changeController.selectedActionChanged(lastAction, null);
        lastAction = null;
    }

    public void dataChanged(Action act) {
        if (act != null) {
            if (states.get(act.getState().getId()) == null) {
                selectedProjectChanged(actualProject, actualProject);
                return;
            }
        }
        int row = table.getSelectedRow();
        if ((row >= model.getRowCount()) || (row < 0)) table.tableChanged(new TableModelEvent(table.getModel())); else {
            row = table.convertRowIndexToModel(row);
            table.tableChanged(new TableModelEvent(table.getModel(), row));
        }
    }

    public void selectedActionChanged(Action oldProj, Action newProj) {
    }

    public void newInboxAction(Action newAction) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) {
            return;
        }
        int row = table.getSelectedRow();
        if ((row >= model.getRowCount()) || (row < 0)) return;
        row = table.convertRowIndexToModel(row);
        Action action = model.getAction(row);
        changeController.selectedActionChanged(lastAction, action);
        lastAction = action;
    }

    /**
     * @return the actualProject
     */
    public Project getActualProject() {
        return actualProject;
    }

    public void stateChanged(List<State> states) {
        this.states = new HashMap<Integer, State>();
        for (State s : states) this.states.put(s.getId(), s);
        selectedProjectChanged(actualProject, actualProject);
    }

    private List<Action> filter(List<Action> actions) {
        List<Action> result = new LinkedList<Action>();
        for (Action a : actions) {
            if (states.get(a.getState().getId()) != null) {
                result.add(a);
            }
        }
        return result;
    }

    public void configChanged() {
        table.tableChanged(new TableModelEvent(table.getModel()));
        table.repaint();
    }
}
