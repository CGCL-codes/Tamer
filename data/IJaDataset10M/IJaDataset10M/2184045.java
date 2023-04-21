package com.testonica.kickelhahn.core.ui.viewereditor;

import java.awt.Color;
import java.awt.Component;
import java.awt.KeyboardFocusManager;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import com.testonica.kickelhahn.core.KickelhahnInfo;
import com.testonica.kickelhahn.core.ui.viewereditor.group.ViewerEditorGroup;

/**
 * Manages current (active) viewer editor or current group of viewer editors.
 * Handles focus and tab panel events and perform switches between active viewer
 * editors.
 * 
 * @author Sergei Devadze
 */
public class ViewerEditorManager implements ChangeListener, PropertyChangeListener {

    /** Current (active) viewer editor */
    private ViewerEditor currentViewerEditor;

    /** Current (active) viewer editor group */
    private ViewerEditorGroup currentViewerEditorGroup;

    /** List of registered tabbed panes */
    private List<JTabbedPane> tabPanes = new ArrayList<JTabbedPane>();

    /** Color for tab pane */
    public static final Color FGROUND = Color.black;

    /** Color for SELECTED tab pane */
    public static final Color SELECTED_FGROUND = new Color(254, 100, 0);

    /** Color for CHANGED tab pane */
    public static final Color CONTENT_CHANGED_FGROUND = Color.blue;

    /** Color for NOT SAVED tab pane */
    public static final Color NOT_SAVED_FGROUND = new Color(0, 128, 0);

    /** If locked -- then events will left unhandled */
    private boolean isLocked = false;

    private String focusProperty = "focusOwner";

    /** Link to main class */
    private KickelhahnInfo info;

    public ViewerEditorManager(KickelhahnInfo info) {
        this.info = info;
        KeyboardFocusManager focusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        focusManager.addPropertyChangeListener(focusProperty, this);
    }

    /**
     * Registers tab panel that can have child ViewerEditor components
     * 
     * @param tabPane
     *            tab panel to register
     */
    public void registerTabbedPanel(JTabbedPane tabPane) {
        tabPane.addChangeListener(this);
        tabPanes.add(tabPane);
    }

    /** Handles focus events */
    public void propertyChange(PropertyChangeEvent e) {
        if (isLocked) return;
        String prop = e.getPropertyName();
        if (!prop.equals(focusProperty)) return;
        Component component = (Component) e.getNewValue();
        if (component == null) return;
        if (component instanceof JTabbedPane) component = ((JTabbedPane) component).getSelectedComponent();
        while (component != null) {
            if (component instanceof ViewerEditor) {
                setCurrentViewerEditor((ViewerEditor) component);
                break;
            }
            component = component.getParent();
        }
    }

    /** Handles tab pane events */
    public void stateChanged(ChangeEvent e) {
        if (isLocked) return;
        Object o = e.getSource();
        if (o instanceof JTabbedPane) {
            Component c = ((JTabbedPane) o).getSelectedComponent();
            if (c instanceof ViewerEditor) {
                ((JTabbedPane) o).requestFocus();
                c.requestFocusInWindow();
                setCurrentViewerEditor((ViewerEditor) c);
            }
        }
    }

    /** Checks if given viewer editor is active */
    public boolean isCurrentViewerEditor(ViewerEditor viewerEditor) {
        return currentViewerEditor == viewerEditor;
    }

    /** Returns active viewer editor */
    public ViewerEditor getCurrentViewerEditor() {
        return currentViewerEditor;
    }

    /** Returns active viewer editor group */
    public ViewerEditorGroup getCurrentViewerEditorGroup() {
        return currentViewerEditorGroup;
    }

    /** Makes given viewer editor group be active */
    public void setCurrentViewerEditorGroup(ViewerEditorGroup viewerEditorGroup) {
        if (isLocked) return;
        isLocked = true;
        setCurrentViewerEditorGroupInternal(viewerEditorGroup);
        isLocked = false;
    }

    private void setCurrentViewerEditorGroupInternal(ViewerEditorGroup viewerEditorGroup) {
        if (currentViewerEditorGroup == viewerEditorGroup) return;
        if (currentViewerEditorGroup != null) for (ViewerEditor v : currentViewerEditorGroup.getViewerEditors()) v.groupChanging();
        Object[] tabPanesArray = tabPanes.toArray();
        if (tabPanesArray.length == 0) return;
        for (int i = 0; i < tabPanesArray.length; i++) ((JTabbedPane) tabPanesArray[i]).removeAll();
        for (int i = 0; i < viewerEditorGroup.getLeftViewerEditors().size(); i++) addViewerEditor(viewerEditorGroup.getLeftViewerEditors().get(i), viewerEditorGroup.getLeftTabbedPane(), viewerEditorGroup);
        for (int i = 0; i < viewerEditorGroup.getBottomViewerEditors().size(); i++) addViewerEditor(viewerEditorGroup.getBottomViewerEditors().get(i), viewerEditorGroup.getBottomTabbedPane(), viewerEditorGroup);
        for (int i = 0; i < viewerEditorGroup.getCenterViewerEditors().size(); i++) addViewerEditor(viewerEditorGroup.getCenterViewerEditors().get(i), viewerEditorGroup.getCenterTabbedPane(), viewerEditorGroup);
        currentViewerEditorGroup = viewerEditorGroup;
        for (ViewerEditor v : currentViewerEditorGroup.getViewerEditors()) v.groupChanged();
    }

    private void addViewerEditor(ViewerEditor viewerEditor, JTabbedPane pane, ViewerEditorGroup viewerEditorGroup) {
        if (!viewerEditor.isVisible()) return;
        viewerEditor.setTabbedPane(pane);
        viewerEditor.setGroup(viewerEditorGroup);
        pane.addTab(viewerEditor.getTitle(), info.getImage(viewerEditor.getIcon()), viewerEditor);
        viewerEditor.updateTitle();
        if ((viewerEditor.getToolTipText() != null) && (!viewerEditor.getToolTipText().equals(""))) pane.setToolTipTextAt(pane.getComponentCount() - 1, viewerEditor.getToolTipText());
    }

    /** Makes given viewer editor be active */
    public void setCurrentViewerEditor(ViewerEditor viewerEditor) {
        if (isLocked) return;
        isLocked = true;
        setCurrentViewerEditorInternal(viewerEditor);
        isLocked = false;
    }

    private void setCurrentViewerEditorInternal(ViewerEditor viewerEditor) {
        if ((currentViewerEditor == viewerEditor) || (!isInCurrentGroup(viewerEditor))) return;
        JTabbedPane parent = null;
        if (viewerEditor.getParent() instanceof JTabbedPane) parent = (JTabbedPane) viewerEditor.getParent();
        if ((parent == null) && (parent.getSelectedComponent() == viewerEditor)) return;
        ViewerEditor oldViewerEditor = currentViewerEditor;
        if (currentViewerEditor != null) {
            if (currentViewerEditor.isContentChanged() & currentViewerEditor.isNotSavedSupported()) setTabColor(currentViewerEditor, NOT_SAVED_FGROUND); else setTabColor(currentViewerEditor, FGROUND);
        }
        currentViewerEditor = viewerEditor;
        if (currentViewerEditor == null) return;
        if (currentViewerEditor.isContentChanged() & currentViewerEditor.isNotSavedSupported()) setTabColor(currentViewerEditor, NOT_SAVED_FGROUND); else setTabColor(currentViewerEditor, SELECTED_FGROUND);
        parent.setSelectedComponent(currentViewerEditor);
        if (oldViewerEditor != null) oldViewerEditor.deactivated();
        currentViewerEditor.activated();
        info.updateCurrentViewerEditor();
    }

    private boolean isInCurrentGroup(ViewerEditor viewerEditor) {
        if (currentViewerEditorGroup == null) return true;
        return currentViewerEditorGroup.getLeftViewerEditors().contains(viewerEditor) || currentViewerEditorGroup.getBottomViewerEditors().contains(viewerEditor) || currentViewerEditorGroup.getCenterViewerEditors().contains(viewerEditor);
    }

    /**
     * Sets color of possible parent tab of given viewer editor
     */
    private void setTabColor(ViewerEditor ve, Color color) {
        if (!(ve.getParent() instanceof JTabbedPane)) return;
        JTabbedPane tabbedPane = (JTabbedPane) ve.getParent();
        for (int i = 0; i < tabbedPane.getTabCount(); i++) if (tabbedPane.getComponent(i) == ve) {
            tabbedPane.setForegroundAt(i, color);
            break;
        }
    }
}
