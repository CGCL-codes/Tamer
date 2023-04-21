package be.vds.jtbdive.client.view.core.divesite;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import be.smd.i18n.swing.I18nButton;
import be.vds.jtb.swing.layout.GridBagLayoutManager;
import be.vds.jtb.swing.utils.WindowUtils;
import be.vds.jtbdive.client.core.DiveSiteManagerFacade;
import be.vds.jtbdive.client.core.UnitsAgent;
import be.vds.jtbdive.client.core.event.DiveSiteObserverEvent;
import be.vds.jtbdive.client.swing.component.ExceptionDialog;
import be.vds.jtbdive.client.swing.component.SearchBox;
import be.vds.jtbdive.client.view.events.DiveSiteSelectionListener;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.core.DiveSite;
import be.vds.jtbdive.core.exceptions.DataStoreException;
import be.vds.jtbdive.core.exceptions.DiveLocationUsedException;
import be.vds.jtbdive.core.logging.Syslog;

/**
 * 
 * @author Gautier Vanderslyen
 */
public class DiveSiteManagerPanel extends JPanel implements Observer {

    private static final long serialVersionUID = 2330263025597775079L;

    protected static final Syslog LOGGER = Syslog.getLogger(DiveSiteManagerPanel.class);

    private DiveSiteManagerFacade diveLocationManagerFacade;

    private DiveSiteTableModel diveSiteTableModel;

    private Window parentWindow;

    private List<DiveSiteSelectionListener> listeners = new ArrayList<DiveSiteSelectionListener>();

    private JXTable diveSiteTable;

    private Action deleteAction;

    private Action updateAction;

    private SearchBox diveLocationSearchBox;

    public DiveSiteManagerPanel(DiveSiteManagerFacade diveSiteManagerFacade) {
        this.diveLocationManagerFacade = diveSiteManagerFacade;
        diveSiteManagerFacade.addObserver(this);
        init();
    }

    private void init() {
        this.setLayout(new BorderLayout());
        this.add(createControlPanel(), BorderLayout.NORTH);
        this.add(createListPanel(), BorderLayout.CENTER);
    }

    private Component createControlPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(0, 0, 0, 0);
        GridBagLayoutManager.addComponent(panel, createButtonsPanel(), c, 0, 0, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
        GridBagLayoutManager.addComponent(panel, createSearchPanel(), c, 1, 0, 1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.NORTHEAST);
        return panel;
    }

    private Component createSearchPanel() {
        diveLocationSearchBox = new SearchBox(UIAgent.getInstance().getIcon(UIAgent.ICON_MAGNIFYING_GLASS_16), 200) {

            private static final long serialVersionUID = -3300101873615285458L;

            @Override
            protected String formatSelectedObject(Object object) {
                DiveSite dl = (DiveSite) object;
                return dl.getName();
            }

            @Override
            protected void lookup() {
                lookupDiveLocation();
            }
        };
        return diveLocationSearchBox;
    }

    private Component createListPanel() {
        JPanel listPanel = new JPanel(new BorderLayout());
        diveSiteTableModel = new DiveSiteTableModel();
        diveSiteTable = new JXTable(diveSiteTableModel);
        diveSiteTable.addHighlighter(HighlighterFactory.createAlternateStriping());
        diveSiteTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    deleteAction.setEnabled(diveSiteTable.getSelectedRowCount() > 0);
                    updateAction.setEnabled(diveSiteTable.getSelectedRowCount() > 0);
                }
            }
        });
        JScrollPane scroll = new JScrollPane(diveSiteTable);
        listPanel.add(scroll, BorderLayout.CENTER);
        return listPanel;
    }

    private Component createButtonsPanel() {
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JButton deleteButton = getButton(createDeleteAction(), UIAgent.getInstance().getIcon(UIAgent.ICON_DIVE_SITE_DELETE_16), "delete");
        JButton updateButton = getButton(createUpdateAction(), UIAgent.getInstance().getIcon(UIAgent.ICON_DIVE_SITE_EDIT_16), "update");
        JButton newButton = getButton(createNewAction(), UIAgent.getInstance().getIcon(UIAgent.ICON_DIVE_SITE_ADD_16), "new");
        JButton mergeButton = getButton(createMergeAction(), UIAgent.getInstance().getIcon(UIAgent.ICON_DIVE_SITE_MERGE_16), "merge");
        newButton.setMinimumSize(UIAgent.DIMENSION_20_20);
        newButton.setPreferredSize(UIAgent.DIMENSION_20_20);
        deleteButton.setMinimumSize(UIAgent.DIMENSION_20_20);
        deleteButton.setPreferredSize(UIAgent.DIMENSION_20_20);
        updateButton.setMinimumSize(UIAgent.DIMENSION_20_20);
        updateButton.setPreferredSize(UIAgent.DIMENSION_20_20);
        mergeButton.setMinimumSize(UIAgent.DIMENSION_20_20);
        mergeButton.setPreferredSize(UIAgent.DIMENSION_20_20);
        buttonsPanel.add(newButton);
        buttonsPanel.add(updateButton);
        buttonsPanel.add(deleteButton);
        buttonsPanel.add(mergeButton);
        return buttonsPanel;
    }

    private Action createMergeAction() {
        Action mergeAction = new AbstractAction() {

            private static final long serialVersionUID = 8611186921543882068L;

            @Override
            public void actionPerformed(ActionEvent e) {
                MergeDiveSiteDialog dlg = new MergeDiveSiteDialog(diveLocationManagerFacade, (JFrame) parentWindow);
                WindowUtils.centerWindow(dlg);
                if (MergeDiveSiteDialog.OPTION_OK == dlg.showDialog()) {
                    DiveSite keep = dlg.getDiveLocationToKeep();
                    DiveSite delete = dlg.getDiveLocationToDelete();
                    try {
                        diveLocationManagerFacade.mergeDiveLocation(keep, delete);
                    } catch (DataStoreException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        };
        return mergeAction;
    }

    private Action createNewAction() {
        Action newAction = new AbstractAction() {

            private static final long serialVersionUID = 8611186921543882068L;

            @Override
            public void actionPerformed(ActionEvent e) {
                DiveSiteEditionDialog dlg = new DiveSiteEditionDialog(null, diveLocationManagerFacade, DiveSiteEditionDialog.MODE_SAVE);
                dlg.setSize(500, 400);
                WindowUtils.centerWindow(dlg);
                dlg.setVisible(true);
            }
        };
        return newAction;
    }

    private Action createUpdateAction() {
        updateAction = new AbstractAction() {

            private static final long serialVersionUID = 4473768673583852509L;

            @Override
            public void actionPerformed(ActionEvent e) {
                DiveSite dl = getSelectedDiveLocation();
                DiveSiteEditionDialog dlg = new DiveSiteEditionDialog(null, diveLocationManagerFacade, DiveSiteEditionDialog.MODE_EDIT);
                try {
                    dl = diveLocationManagerFacade.findDiveLocationsById(dl.getId(), DiveSite.LOAD_FULL);
                    dlg.setValue(dl);
                    dlg.setSize(500, 400);
                    WindowUtils.centerWindow(dlg);
                    dlg.setVisible(true);
                } catch (DataStoreException e1) {
                    LOGGER.error(e1.getMessage());
                    ExceptionDialog.showDialog(e1, DiveSiteManagerPanel.this);
                }
            }
        };
        updateAction.setEnabled(false);
        return updateAction;
    }

    private Action createDeleteAction() {
        deleteAction = new AbstractAction() {

            private static final long serialVersionUID = 7418511119638493460L;

            @Override
            public void actionPerformed(ActionEvent e) {
                for (DiveSite dl : getSelectedDiveLocations()) {
                    try {
                        diveLocationManagerFacade.deleteDiveLocation(dl);
                    } catch (DiveLocationUsedException ex) {
                        LOGGER.error(ex.getMessage());
                        JOptionPane.showMessageDialog(parentWindow, "Dive " + dl.toString() + " is still used... You can't delete it.", "Error", JOptionPane.ERROR_MESSAGE);
                    } catch (DataStoreException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        };
        deleteAction.setEnabled(false);
        return deleteAction;
    }

    private List<DiveSite> getSelectedDiveLocations() {
        List<DiveSite> dls = new ArrayList<DiveSite>();
        int[] rows = diveSiteTable.getSelectedRows();
        for (int i = 0; i < rows.length; i++) {
            int row = diveSiteTable.convertRowIndexToModel(rows[i]);
            dls.add(diveSiteTableModel.getDiveSiteAt(row));
        }
        return dls;
    }

    private DiveSite getSelectedDiveLocation() {
        if (diveSiteTable.getSelectedRowCount() > 0) {
            return diveSiteTableModel.getDiveSiteAt(diveSiteTable.convertRowIndexToModel(diveSiteTable.getSelectedRow()));
        }
        return null;
    }

    private void lookupDiveLocation() {
        String searchText = diveLocationSearchBox.getSearchText();
        if (null != searchText && searchText.trim().length() > 0) {
            LOGGER.debug("searching for : " + searchText);
            List<DiveSite> dls;
            try {
                dls = diveLocationManagerFacade.findDiveSitesByName(searchText);
                setSearchResult(dls);
            } catch (DataStoreException e) {
                ExceptionDialog.showDialog(e, this);
            }
        }
    }

    private void setSearchResult(List<DiveSite> diveLocations) {
        diveSiteTableModel.setData(diveLocations);
    }

    public void addDiveLocationSelectionListener(DiveSiteSelectionListener diveLocationSelectionListener) {
        listeners.add(diveLocationSelectionListener);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o.equals(UnitsAgent.getInstance()) && arg.equals(UnitsAgent.UNITS_CHANGED)) {
            updateUnitsLabels();
        } else if (arg instanceof DiveSiteObserverEvent) {
            DiveSiteObserverEvent event = (DiveSiteObserverEvent) arg;
            if (event.getAction() == DiveSiteObserverEvent.DELETE) {
                diveSiteTableModel.removeDiveLocations(event.getDiveSites());
            } else if (event.getAction() == DiveSiteObserverEvent.UPDATE) {
                diveSiteTableModel.addDiveSites(event.getDiveSites());
            } else if (event.getAction() == DiveSiteObserverEvent.SAVE) {
                diveSiteTableModel.addDiveSites(event.getDiveSites());
            } else if (event.getAction() == DiveSiteObserverEvent.MERGE) {
                diveSiteTableModel.removeDiveLocations(event.getDiveSites());
            }
        }
    }

    @Override
    public String toString() {
        return "Dive location Manager panel";
    }

    private JButton getButton(Action action, Icon image, String tooltip) {
        JButton button = new I18nButton(action);
        button.setIcon(image);
        button.setToolTipText(tooltip);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusable(false);
        return button;
    }

    @Override
    public void updateUI() {
        super.updateUI();
        if (diveSiteTable != null) {
            repaintTableHeaders();
            diveSiteTableModel.fireTableDataChanged();
        }
    }

    private void repaintTableHeaders() {
        for (int i = 0; i < diveSiteTable.getColumnCount(); i++) {
            diveSiteTable.getColumn(i).setHeaderValue(diveSiteTableModel.getColumnName(i));
        }
    }

    public void updateUnitsLabels() {
        updateUI();
    }
}
