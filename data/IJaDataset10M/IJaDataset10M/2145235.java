package cw.roommanagementmodul.gui;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.EventObject;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.table.TableModel;
import com.jgoodies.binding.adapter.AbstractTableAdapter;
import com.jgoodies.binding.list.SelectionInList;
import cw.boardingschoolmanagement.app.ButtonEvent;
import cw.boardingschoolmanagement.app.ButtonListener;
import cw.boardingschoolmanagement.app.CWUtils;
import cw.boardingschoolmanagement.gui.CWEditPresentationModel;
import cw.boardingschoolmanagement.gui.CWPresentationModel;
import cw.boardingschoolmanagement.gui.component.CWView.CWHeaderInfo;
import cw.boardingschoolmanagement.manager.GUIManager;
import cw.roommanagementmodul.persistence.Gebuehr;
import cw.roommanagementmodul.persistence.PMGebuehr;
import cw.roommanagementmodul.persistence.PMGebuehrenKat;

/**
 *
 * @author Dominik
 */
public class GebuehrenPresentationModel extends CWPresentationModel {

    private PMGebuehr gebuehrenManager;

    private Action newAction;

    private Action editAction;

    private Action deleteAction;

    private Action kategorieAction;

    private Action tarifAction;

    private SelectionInList<Gebuehr> gebuehrenSelection;

    private CWHeaderInfo headerInfo;

    private SelectionEmptyHandler selectionEmptyHandler;

    private DoubleClickHandler doubleClickHandler;

    public GebuehrenPresentationModel(PMGebuehr gebuehrenManager, CWHeaderInfo header) {
        this.gebuehrenManager = gebuehrenManager;
        this.headerInfo = header;
        initModels();
        initEventHandling();
    }

    private void initModels() {
        newAction = new NewAction();
        editAction = new EditAction();
        deleteAction = new DeleteAction();
        kategorieAction = new KategorieAction();
        tarifAction = new TarifAction();
        gebuehrenSelection = new SelectionInList<Gebuehr>(gebuehrenManager.getAll());
        updateActionEnablement();
    }

    private void initEventHandling() {
        selectionEmptyHandler = new SelectionEmptyHandler();
        gebuehrenSelection.addPropertyChangeListener(SelectionInList.PROPERTYNAME_SELECTION_EMPTY, selectionEmptyHandler);
    }

    public void dispose() {
        gebuehrenSelection.release();
        gebuehrenSelection.removeValueChangeListener(selectionEmptyHandler);
    }

    private void updateActionEnablement() {
        boolean hasSelection = gebuehrenSelection.hasSelection();
        doubleClickHandler = new DoubleClickHandler();
        getEditAction().setEnabled(hasSelection);
        getDeleteAction().setEnabled(hasSelection);
        getTarifAction().setEnabled(hasSelection);
    }

    public Action getNewAction() {
        return newAction;
    }

    public Action getKategorieAction() {
        return kategorieAction;
    }

    public Action getEditAction() {
        return editAction;
    }

    public Action getTarifAction() {
        return tarifAction;
    }

    public Action getDeleteAction() {
        return deleteAction;
    }

    public SelectionInList<Gebuehr> getGebuehrenSelection() {
        return gebuehrenSelection;
    }

    /**
     * @return the headerInfo
     */
    public CWHeaderInfo getHeaderInfo() {
        return headerInfo;
    }

    private class NewAction extends AbstractAction {

        {
            putValue(Action.SMALL_ICON, CWUtils.loadIcon("cw/roommanagementmodul/images/money_add.png"));
        }

        public void actionPerformed(ActionEvent e) {
            final Gebuehr g = new Gebuehr();
            final EditGebuehrenPresentationModel model = new EditGebuehrenPresentationModel(g, new CWHeaderInfo("Gebuehr erstellen", "Hier koennen Sie eine neue Gebuehr erstellen"));
            final EditGebuehrenView editView = new EditGebuehrenView(model);
            model.addButtonListener(new ButtonListener() {

                public void buttonPressed(ButtonEvent evt) {
                    if (evt.getType() == ButtonEvent.SAVE_BUTTON || evt.getType() == ButtonEvent.SAVE_EXIT_BUTTON) {
                        gebuehrenManager.save(g);
                    }
                    if (evt.getType() == ButtonEvent.EXIT_BUTTON || evt.getType() == ButtonEvent.SAVE_EXIT_BUTTON) {
                        model.removeButtonListener(this);
                        getGebuehrenSelection().setList(gebuehrenManager.getAll());
                        GUIManager.changeToPreviousView();
                        GUIManager.getStatusbar().setTextAndFadeOut("Gebuehr wurde erstellt.");
                    }
                }
            });
            GUIManager.changeViewTo(editView, true);
        }
    }

    private class KategorieAction extends AbstractAction {

        {
            putValue(Action.SMALL_ICON, CWUtils.loadIcon("cw/roommanagementmodul/images/category.png"));
        }

        public void actionPerformed(ActionEvent e) {
            final PMGebuehrenKat gebKatManager = PMGebuehrenKat.getInstance();
            final GebuehrenKategoriePresentationModel model = new GebuehrenKategoriePresentationModel(gebKatManager, new CWHeaderInfo("Kategorien verwalten", "Uebersicht aller Gebuehren Kategorien"));
            final GebuehrenKategorieView editView = new GebuehrenKategorieView(model);
            model.addButtonListener(new ButtonListener() {

                public void buttonPressed(ButtonEvent evt) {
                    if (evt.getType() == ButtonEvent.SAVE_BUTTON || evt.getType() == ButtonEvent.SAVE_EXIT_BUTTON) {
                    }
                    if (evt.getType() == ButtonEvent.EXIT_BUTTON || evt.getType() == ButtonEvent.SAVE_EXIT_BUTTON) {
                    }
                }
            });
            GUIManager.changeViewTo(editView, true);
        }
    }

    private class TarifAction extends AbstractAction {

        {
            putValue(Action.SMALL_ICON, CWUtils.loadIcon("cw/roommanagementmodul/images/coins.png"));
        }

        public void actionPerformed(ActionEvent e) {
            showTarif();
        }
    }

    private void showTarif() {
        Gebuehr g = gebuehrenSelection.getSelection();
        final TarifPresentationModel model = new TarifPresentationModel(g, new CWHeaderInfo("Tarif Uebersicht: " + g.getName(), "Uebersicht aller Tarife fuer eine bestimmte Gebuehr."));
        final TarifView editView = new TarifView(model);
        model.addButtonListener(new ButtonListener() {

            public void buttonPressed(ButtonEvent evt) {
                if (evt.getType() == ButtonEvent.SAVE_BUTTON || evt.getType() == ButtonEvent.SAVE_EXIT_BUTTON) {
                }
                if (evt.getType() == ButtonEvent.EXIT_BUTTON || evt.getType() == ButtonEvent.SAVE_EXIT_BUTTON) {
                    model.removeButtonListener(this);
                }
            }
        });
        GUIManager.changeViewTo(editView, true);
    }

    private class EditAction extends AbstractAction {

        {
            putValue(Action.SMALL_ICON, CWUtils.loadIcon("cw/roommanagementmodul/images/money_edit.png"));
        }

        public void actionPerformed(ActionEvent e) {
            editSelectedItem(e);
        }
    }

    private class DeleteAction extends AbstractAction {

        {
            putValue(Action.SMALL_ICON, CWUtils.loadIcon("cw/roommanagementmodul/images/money_delete.png"));
        }

        public void actionPerformed(ActionEvent e) {
            Gebuehr g = getGebuehrenSelection().getSelection();
            int i = JOptionPane.showConfirmDialog(null, "Gebuehr loeschen?", "Loeschen", JOptionPane.OK_CANCEL_OPTION);
            if (i == JOptionPane.OK_OPTION) {
                gebuehrenManager.delete(g);
                gebuehrenSelection.setList(gebuehrenManager.getAll());
            }
        }
    }

    private final class SelectionEmptyHandler implements PropertyChangeListener {

        public void propertyChange(PropertyChangeEvent evt) {
            updateActionEnablement();
        }
    }

    public MouseListener getDoubleClickHandler() {
        return doubleClickHandler;
    }

    private final class DoubleClickHandler extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2) {
                showTarif();
            }
        }
    }

    private void editSelectedItem(EventObject e) {
        final Gebuehr g = getGebuehrenSelection().getSelection();
        final EditGebuehrenPresentationModel model = new EditGebuehrenPresentationModel(g, new CWHeaderInfo("Gebuehr bearbeiten", "Hier koennen Sie eine vorhandene Gebuehr bearbeiten"));
        final EditGebuehrenView editView = new EditGebuehrenView(model);
        model.addButtonListener(new ButtonListener() {

            public void buttonPressed(ButtonEvent evt) {
                if (evt.getType() == ButtonEvent.SAVE_BUTTON || evt.getType() == ButtonEvent.SAVE_EXIT_BUTTON) {
                    gebuehrenManager.save(g);
                    GUIManager.getStatusbar().setTextAndFadeOut("Zimmer wurde aktualisiert.");
                }
                if (evt.getType() == ButtonEvent.EXIT_BUTTON || evt.getType() == ButtonEvent.SAVE_EXIT_BUTTON) {
                    model.removeButtonListener(this);
                    GUIManager.changeToPreviousView();
                }
            }
        });
        GUIManager.changeViewTo(editView, true);
    }

    public TableModel createGebuehrenTableModel(ListModel listModel) {
        return new GebuehrenTableModel(listModel);
    }

    private class GebuehrenTableModel extends AbstractTableAdapter<Gebuehr> {

        private ListModel listModel;

        public GebuehrenTableModel(ListModel listModel) {
            super(listModel);
            this.listModel = listModel;
        }

        @Override
        public int getColumnCount() {
            return 2;
        }

        @Override
        public String getColumnName(int column) {
            switch(column) {
                case 0:
                    return "Name";
                case 1:
                    return "Kategorie";
                default:
                    return "";
            }
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            Gebuehr g = (Gebuehr) listModel.getElementAt(rowIndex);
            switch(columnIndex) {
                case 0:
                    return g.getName();
                case 1:
                    if (g.getGebKat() != null) {
                        return g.getGebKat().getName();
                    }
                default:
                    return "";
            }
        }
    }
}
