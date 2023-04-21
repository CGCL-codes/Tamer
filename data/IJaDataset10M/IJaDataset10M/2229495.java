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
import javax.swing.ComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.table.TableModel;
import com.jgoodies.binding.adapter.AbstractTableAdapter;
import com.jgoodies.binding.adapter.ComboBoxAdapter;
import com.jgoodies.binding.list.SelectionInList;
import cw.boardingschoolmanagement.app.ButtonEvent;
import cw.boardingschoolmanagement.app.ButtonListener;
import cw.boardingschoolmanagement.app.CWUtils;
import cw.boardingschoolmanagement.gui.CWPresentationModel;
import cw.boardingschoolmanagement.gui.component.CWView.CWHeaderInfo;
import cw.boardingschoolmanagement.manager.GUIManager;
import cw.customermanagementmodul.customer.gui.CustomerManagementPresentationModel;
import cw.customermanagementmodul.customer.gui.EditCustomerPresentationModel;
import cw.roommanagementmodul.persistence.Bewohner;
import cw.roommanagementmodul.persistence.GebuehrZuordnung;
import cw.roommanagementmodul.persistence.PMBewohner;
import cw.roommanagementmodul.persistence.PMBewohnerHistory;
import cw.roommanagementmodul.persistence.PMGebuehrZuordnung;
import cw.roommanagementmodul.persistence.PMKaution;

/**
 *
 * @author Dominik
 */
public class BewohnerPresentationModel extends CWPresentationModel {

    private PMBewohner bewohnerManager;

    private String headerText;

    private PMGebuehrZuordnung gebZuordnungManager;

    private PMBewohnerHistory historyManager;

    private Action gebuehrZuordnungAction;

    private Action gebAction;

    private Action deleteAction;

    private Action detailAction;

    private Action kautionAction;

    private SelectionInList<Bewohner> bewohnerSelection;

    private CWHeaderInfo headerInfo;

    private SelectionEmptyHandler selectionEmptyHandler;

    private EditCustomerPresentationModel editCustomerModel;

    private DoubleClickHandler doubleClickHandler;

    public BewohnerPresentationModel(PMBewohner bewohnerManager) {
        this.bewohnerManager = bewohnerManager;
        historyManager = PMBewohnerHistory.getInstance();
        gebZuordnungManager = PMGebuehrZuordnung.getInstance();
        initModels();
        this.initEventHandling();
    }

    public BewohnerPresentationModel(PMBewohner bewohnerManager, CWHeaderInfo header) {
        this.bewohnerManager = bewohnerManager;
        this.headerInfo = header;
        historyManager = PMBewohnerHistory.getInstance();
        gebZuordnungManager = PMGebuehrZuordnung.getInstance();
        initModels();
        this.initEventHandling();
    }

    private void initModels() {
        doubleClickHandler = new DoubleClickHandler();
        gebAction = new GebAction();
        deleteAction = new DeleteAction();
        gebuehrZuordnungAction = new GebuehrZuordnungAction();
        kautionAction = new KautionAction();
        detailAction = new AbstractAction("Bearbeiten", CWUtils.loadIcon("cw/customermanagementmodul/images/user_edit.png")) {

            public void actionPerformed(ActionEvent e) {
                editCustomerModel = CustomerManagementPresentationModel.editCustomer(bewohnerSelection.getSelection().getCustomer());
                editCustomerModel.addButtonListener(new ButtonListener() {

                    public void buttonPressed(ButtonEvent evt) {
                        if (evt.getType() == ButtonEvent.SAVE_BUTTON || evt.getType() == ButtonEvent.SAVE_EXIT_BUTTON) {
                            bewohnerSelection.setList(bewohnerManager.getBewohner(true));
                        }
                        if (evt.getType() == ButtonEvent.EXIT_BUTTON || evt.getType() == ButtonEvent.SAVE_EXIT_BUTTON) {
                            editCustomerModel.removeButtonListener(this);
                        }
                    }
                });
            }
        };
        bewohnerSelection = new SelectionInList<Bewohner>(getBewohnerManager().getBewohner(true));
    }

    private void initEventHandling() {
        updateActionEnablement();
        selectionEmptyHandler = new SelectionEmptyHandler();
        getBewohnerSelection().addPropertyChangeListener(SelectionInList.PROPERTYNAME_SELECTION_EMPTY, selectionEmptyHandler);
    }

    public void dispose() {
        getBewohnerSelection().removeValueChangeListener(this.selectionEmptyHandler);
    }

    public TableModel createBewohnerTableModel(ListModel listModel) {
        return new BewohnerTableModel(listModel);
    }

    public ComboBoxModel createComboModel(SelectionInList list) {
        return new ComboBoxAdapter(list);
    }

    private void updateActionEnablement() {
        boolean hasSelection = getBewohnerSelection().hasSelection();
        getDeleteAction().setEnabled(hasSelection);
        gebAction.setEnabled(hasSelection);
        getGebuehrZuordnungAction().setEnabled(hasSelection);
        detailAction.setEnabled(hasSelection);
    }

    public SelectionInList<Bewohner> getBewohnerSelection() {
        return bewohnerSelection;
    }

    public Action getGebAction() {
        return gebAction;
    }

    public Action getDeleteAction() {
        return deleteAction;
    }

    public String getHeaderText() {
        return headerText;
    }

    /**
     * @return the bewohnerManager
     */
    public PMBewohner getBewohnerManager() {
        return bewohnerManager;
    }

    /**
     * @param bewohnerManager the bewohnerManager to set
     */
    public void setBewohnerManager(PMBewohner bewohnerManager) {
        this.bewohnerManager = bewohnerManager;
    }

    /**
     * @return the gebuehrZuordnungAction
     */
    public Action getGebuehrZuordnungAction() {
        return gebuehrZuordnungAction;
    }

    /**
     * @return the detailAction
     */
    public Action getDetailAction() {
        return detailAction;
    }

    /**
     * @return the kautionAction
     */
    public Action getKautionAction() {
        return kautionAction;
    }

    /**
     * @return the headerInfo
     */
    public CWHeaderInfo getHeaderInfo() {
        return headerInfo;
    }

    private class DeleteAction extends AbstractAction {

        {
            putValue(Action.SMALL_ICON, CWUtils.loadIcon("cw/roommanagementmodul/images/delete.png"));
        }

        public void actionPerformed(ActionEvent e) {
            Bewohner b = getBewohnerSelection().getSelection();
            int k = JOptionPane.showConfirmDialog(null, "Bewohner: " + b.getCustomer().getSurname() + " " + b.getCustomer().getForename() + " wirklich loeschen?", "LOeSCHEN", JOptionPane.OK_CANCEL_OPTION);
            if (k == JOptionPane.OK_OPTION) {
                boolean checkKaution = true;
                if (b.getKautionStatus() == Bewohner.EINGEZAHLT) {
                    JOptionPane.showMessageDialog(null, "Bewohner kann nicht geloescht werden, da der Status der Kaution EINGEZAHLT ist!", "Kaution", JOptionPane.OK_OPTION);
                    checkKaution = false;
                }
                if (b.getKautionStatus() == Bewohner.NICHT_EINGEZAHLT) {
                    JOptionPane.showMessageDialog(null, "Bewohner kann nicht geloescht werden, da der Status der Kaution Nicht Eingezahlt ist!", "Kaution", JOptionPane.OK_OPTION);
                    checkKaution = false;
                }
                if (checkKaution) {
                    b.setCustomer(null);
                    bewohnerManager.delete(b);
                    bewohnerSelection.setList(bewohnerManager.getBewohner(true));
                }
            }
        }
    }

    private class GebAction extends AbstractAction {

        {
            putValue(Action.SMALL_ICON, CWUtils.loadIcon("cw/roommanagementmodul/images/money_add.png"));
        }

        public void actionPerformed(ActionEvent e) {
            gebSelectedItem(e);
        }
    }

    private class GebuehrZuordnungAction extends AbstractAction {

        {
            putValue(Action.SMALL_ICON, CWUtils.loadIcon("cw/roommanagementmodul/images/money.png"));
        }

        public void actionPerformed(ActionEvent e) {
            gebZuordnungSelectedItem(e);
        }
    }

    private class KautionAction extends AbstractAction {

        {
            putValue(Action.SMALL_ICON, CWUtils.loadIcon("cw/roommanagementmodul/images/money_dollar.png"));
        }

        public void actionPerformed(ActionEvent e) {
            final KautionPresentationModel model = new KautionPresentationModel(PMKaution.getInstance(), new CWHeaderInfo("Kautionen Verwalten", "Uebersicht aller vorhandenen Kautionen"));
            final KautionView kautionView = new KautionView(model);
            GUIManager.changeViewTo(kautionView, true);
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
                CustomerManagementPresentationModel.editCustomer(bewohnerSelection.getSelection().getCustomer());
            }
        }
    }

    private void gebSelectedItem(EventObject e) {
        final GebuehrZuordnung gb = new GebuehrZuordnung();
        CustomerModel c = bewohnerSelection.getSelection().getCustomer();
        gb.setBewohner(bewohnerSelection.getSelection());
        final GebBewohnerPresentationModel model = new GebBewohnerPresentationModel(gb, new CWHeaderInfo("Bewohner: " + c.getSurname() + " " + c.getForename(), "Hier koennen Sie alle Gebuehren verwalten, die zu einem Bewohner zugeordnet sind."));
        final GebBewohnerView gebView = new GebBewohnerView(model);
        model.addButtonListener(new ButtonListener() {

            public void buttonPressed(ButtonEvent evt) {
                if (evt.getType() == ButtonEvent.SAVE_BUTTON || evt.getType() == ButtonEvent.SAVE_EXIT_BUTTON) {
                    gebZuordnungManager.save(gb);
                    GUIManager.getStatusbar().setTextAndFadeOut("Zuordnung wurde aktualisiert.");
                }
                if (evt.getType() == ButtonEvent.EXIT_BUTTON || evt.getType() == ButtonEvent.SAVE_EXIT_BUTTON) {
                    model.removeButtonListener(this);
                    GUIManager.changeToPreviousView();
                }
            }
        });
        GUIManager.changeViewTo(gebView, true);
    }

    private void gebZuordnungSelectedItem(EventObject e) {
        CustomerModel c = bewohnerSelection.getSelection().getCustomer();
        final GebZuordnungBewohnerPresentationModel model = new GebZuordnungBewohnerPresentationModel(this.getBewohnerSelection().getSelection(), new CWHeaderInfo("Gebuehren Uebersicht: " + c.getSurname() + " " + c.getForename(), "Uebersicht aller Gebuehren die diesem Bewohner zugeordnet sind."));
        final GebZuordnungBewohnerView detailView = new GebZuordnungBewohnerView(model);
        model.addButtonListener(new ButtonListener() {

            public void buttonPressed(ButtonEvent evt) {
                if (evt.getType() == ButtonEvent.SAVE_BUTTON || evt.getType() == ButtonEvent.SAVE_EXIT_BUTTON) {
                    GUIManager.getStatusbar().setTextAndFadeOut("Zuordnung wurde aktualisiert.");
                }
                if (evt.getType() == ButtonEvent.EXIT_BUTTON || evt.getType() == ButtonEvent.SAVE_EXIT_BUTTON) {
                    model.removeButtonListener(this);
                    GUIManager.changeToPreviousView();
                }
            }
        });
        GUIManager.changeViewTo(detailView, true);
    }

    private class BewohnerTableModel extends AbstractTableAdapter<Bewohner> {

        private ListModel listModel;

        public BewohnerTableModel(ListModel listModel) {
            super(listModel);
            this.listModel = listModel;
        }

        @Override
        public int getColumnCount() {
            return 8;
        }

        @Override
        public String getColumnName(int column) {
            switch(column) {
                case 0:
                    return "Nachname";
                case 1:
                    return "Vorname";
                case 2:
                    return "Zimmer";
                case 3:
                    return "Bereich";
                case 4:
                    return "Einzugsdatum";
                case 5:
                    return "Auszugsdatum";
                case 6:
                    return "Kaution";
                case 7:
                    return "Kaution Status";
                default:
                    return "";
            }
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            Bewohner b = (Bewohner) listModel.getElementAt(rowIndex);
            switch(columnIndex) {
                case 0:
                    return b.getCustomer().getSurname();
                case 1:
                    return b.getCustomer().getForename();
                case 2:
                    return b.getZimmer();
                case 3:
                    if (b.getZimmer() != null) {
                        return b.getZimmer().getBereich();
                    } else {
                        return "-";
                    }
                case 4:
                    return b.getVon();
                case 5:
                    return b.getBis();
                case 6:
                    if (b.getKaution() != null) {
                        return b.getKaution();
                    } else {
                        return "Keine Kaution";
                    }
                case 7:
                    switch(b.getKautionStatus()) {
                        case Bewohner.NICHT_EINGEZAHLT:
                            return "Nicht eingezahlt";
                        case Bewohner.EINGEZAHLT:
                            return "Eingezahlt";
                        case Bewohner.ZURUECK_GEZAHLT:
                            return "Zurueck Gezahlt";
                        case Bewohner.EINGEZOGEN:
                            return "Eingezogen";
                        default:
                            return "-";
                    }
                default:
                    return "";
            }
        }
    }
}
