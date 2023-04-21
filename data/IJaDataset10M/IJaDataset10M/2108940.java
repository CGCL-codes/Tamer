package com.ingenico.insider.services.impl;

import java.util.logging.Logger;
import javax.swing.Action;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import com.ingenico.insider.nodes.PMark;
import com.ingenico.insider.swing.MarkCameraAction;
import com.ingenico.insider.swing.MarkGotoAction;
import com.ingenico.insider.swing.SelectionBookmarkAction;
import com.ingenico.insider.swing.SelectionClearAction;
import com.ingenico.insider.swing.MarkDeleteAction;
import com.ingenico.insider.util.Constants;

public final class SelectionControlSupplier {

    private static final Logger _logger = Logger.getLogger(SelectionControlSupplier.class.getCanonicalName());

    /**
	 * Public constant keywords
	 */
    public static final String MARK_KEY = "mark";

    public static final String SELECTION_CLEAR_KEY = SelectionLayerSupplier.SELECTION_KEY + Constants.SEPARATOR + "clear";

    public static final String MARK_CREATE_KEY = SelectionLayerSupplier.MARK_KEY + Constants.SEPARATOR + "create";

    public static final String MARK_DELETE_KEY = SelectionLayerSupplier.MARK_KEY + Constants.SEPARATOR + "delete";

    public static final String MARK_GOTO_KEY = SelectionLayerSupplier.MARK_KEY + Constants.SEPARATOR + "goto";

    public static final String MARK_CAMERA_KEY = SelectionLayerSupplier.MARK_KEY + Constants.SEPARATOR + "camera";

    public static final String TOO_MANY_MARKS_MESSAGE_KEY = "too_many_marks";

    /**
	 * The JList graphical component that will display the curves list
	 */
    private final JList marksList;

    /**
	 * singleton instance
	 */
    private static SelectionControlSupplier instance;

    private final Action selectionClearAction;

    private final Action markCreateAction;

    private final Action markDeleteAction;

    private final Action markGotoAction;

    private final Action markCameraAction;

    private void initMenu() {
        final JMenu editMenu = StandardMenuSupplier.getInstance().getEditMenu();
        final Action[] actions = new Action[] { selectionClearAction, markCreateAction, markDeleteAction, markGotoAction, markCameraAction };
        JMenuItem currentMenuItem;
        if (editMenu.getItemCount() > 0) {
            editMenu.addSeparator();
        }
        for (int i = 0; i < actions.length; i++) {
            currentMenuItem = new JMenuItem();
            currentMenuItem.setAction(actions[i]);
            editMenu.add(currentMenuItem);
        }
    }

    private void initMarksList() {
        marksList.setModel(SelectionLayerSupplier.getInstance().getModel());
        marksList.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                markDeleteAction.setEnabled(marksList.getSelectedValue() != null);
                markGotoAction.setEnabled(marksList.getSelectedValue() != null);
            }
        });
    }

    /**
	 * private constructor of final class to prevent external instantiation
	 */
    private SelectionControlSupplier() {
        markCreateAction = LocalisationSupplier.getInstance().localize(new SelectionBookmarkAction(), MARK_CREATE_KEY);
        markDeleteAction = LocalisationSupplier.getInstance().localize(new MarkDeleteAction(), MARK_DELETE_KEY);
        selectionClearAction = LocalisationSupplier.getInstance().localize(new SelectionClearAction(), SELECTION_CLEAR_KEY);
        markGotoAction = LocalisationSupplier.getInstance().localize(new MarkGotoAction(), MARK_GOTO_KEY);
        markCameraAction = LocalisationSupplier.getInstance().localize(new MarkCameraAction(), MARK_CAMERA_KEY);
        selectionClearAction.setEnabled(false);
        markCreateAction.setEnabled(false);
        markDeleteAction.setEnabled(false);
        markGotoAction.setEnabled(false);
        marksList = new JList();
        initMarksList();
        initMenu();
        _logger.fine("Registering curves list to the dock framework");
        DockableViewsSupplier.getInstance().add(new JScrollPane(marksList), MARK_KEY);
    }

    /**
	 * retrieve the singleton instance
	 *
	 * @return the SelectionControlSupplier singleton
	 */
    public static SelectionControlSupplier getInstance() {
        if (instance == null) {
            _logger.info(SelectionControlSupplier.class.getName() + " instance does not exist... Creating instance.");
            instance = new SelectionControlSupplier();
        }
        return instance;
    }

    public void addSelectionToMarks() {
        final SelectionLayerSupplier selectionSupplier = SelectionLayerSupplier.getInstance();
        final PMark mark = new PMark(selectionSupplier.getSelectionMark());
        mark.setPaint(UserPreferencesSupplier.getInstance().getColor(SelectionLayerSupplier.SELECTION_MARK_PATH));
        selectionSupplier.addMark(mark);
        selectionSupplier.clearSelection();
    }

    public Action getSelectionClearAction() {
        return selectionClearAction;
    }

    public Action getSelectionMarkAction() {
        return markCreateAction;
    }

    public Action getSelectionDeleteAction() {
        return markDeleteAction;
    }

    public Action getMarkCameraAction() {
        return markCameraAction;
    }

    public void gotoMark() {
        Object[] selectedItems = marksList.getSelectedValues();
        if (selectedItems != null) {
            if (selectedItems.length != 1) {
                MessageBoxSupplier.getInstance().showErrorDialog(TOO_MANY_MARKS_MESSAGE_KEY);
            } else {
                final PMark selectedMark = (PMark) selectedItems[0];
                CanvasSupplier.getInstance().getCanvas().getCamera().animateViewToCenterBounds(selectedMark.getBounds(), false, 0);
                selectedMark.getBounds();
            }
        }
    }

    public void addMark(PMark mark) {
        mark.setPaint(UserPreferencesSupplier.getInstance().getColor(SelectionLayerSupplier.SELECTION_MARK_PATH));
        SelectionLayerSupplier.getInstance().addMark(mark);
    }

    public void deleteMark() {
        Object[] selectedItems = marksList.getSelectedValues();
        if (selectedItems != null) {
            for (Object selectedCurve : selectedItems) {
                SelectionLayerSupplier.getInstance().removeMark((PMark) selectedCurve);
            }
        }
    }

    public Object[] getSelectedValues() {
        return marksList.getSelectedValues();
    }
}
