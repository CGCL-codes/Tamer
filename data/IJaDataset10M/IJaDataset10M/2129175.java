package org.jhotdraw.samples.svg.gui;

import java.awt.Dimension;
import java.beans.*;
import javax.swing.*;
import org.jhotdraw.draw.*;

/**
 * Calls setVisible(true/false) on components, which show attributes of the 
 * drawing editor and of its views based on the current selection.
 *
 * @author Werner Randelshofer
 * @version 1.0 23.05.2008 Created.
 */
public class SelectionComponentDisplayer implements PropertyChangeListener, FigureSelectionListener {

    protected DrawingEditor editor;

    protected JComponent component;

    protected int minSelectionCount = 1;

    protected boolean isVisibleIfCreationTool = true;

    public SelectionComponentDisplayer(DrawingEditor editor, JComponent component) {
        this.editor = editor;
        this.component = component;
        if (editor.getActiveView() != null) {
            DrawingView view = editor.getActiveView();
            view.addPropertyChangeListener(this);
            view.addFigureSelectionListener(this);
        }
        editor.addPropertyChangeListener(this);
        updateVisibility();
    }

    public void propertyChange(PropertyChangeEvent evt) {
        String name = evt.getPropertyName();
        if (name == DrawingEditor.ACTIVE_VIEW_PROPERTY) {
            DrawingView view = (DrawingView) evt.getOldValue();
            if (view != null) {
                view.removePropertyChangeListener(this);
                view.removeFigureSelectionListener(this);
            }
            view = (DrawingView) evt.getNewValue();
            if (view != null) {
                view.addPropertyChangeListener(this);
                view.addFigureSelectionListener(this);
            }
            updateVisibility();
        } else if (name == DrawingEditor.TOOL_PROPERTY) {
            updateVisibility();
        }
    }

    public void selectionChanged(FigureSelectionEvent evt) {
        updateVisibility();
    }

    public void updateVisibility() {
        boolean newValue = editor != null && editor.getActiveView() != null && (isVisibleIfCreationTool && editor.getTool() != null && !(editor.getTool() instanceof SelectionTool) || editor.getActiveView().getSelectionCount() >= minSelectionCount);
        component.setVisible(newValue);
        if (newValue) {
            component.setPreferredSize(null);
        } else {
            component.setPreferredSize(new Dimension(0, 0));
        }
        component.revalidate();
    }

    public void dispose() {
        if (editor != null) {
            if (editor.getActiveView() != null) {
                DrawingView view = editor.getActiveView();
                view.removePropertyChangeListener(this);
                view.removeFigureSelectionListener(this);
            }
            editor.removePropertyChangeListener(this);
            editor = null;
        }
        component = null;
    }

    public void setMinSelectionCount(int newValue) {
        minSelectionCount = newValue;
        updateVisibility();
    }

    public void setVisibleIfCreationTool(boolean newValue) {
        isVisibleIfCreationTool = newValue;
    }
}
