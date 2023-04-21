package be.vds.jtbdive.view.panel;

import info.clearthought.layout.TableLayout;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import be.vds.jtbdive.actions.DeleteDiveLocationAction;
import be.vds.jtbdive.actions.EditDiveLocationAction;
import be.vds.jtbdive.actions.NewDiveLocationDialogAction;
import be.vds.jtbdive.model.DiveLocation;
import be.vds.jtbdive.model.DiveLocationManagerFacade;
import be.vds.jtbdive.utils.ResourceManager;
import be.vds.jtbdive.view.component.IconOnlyButton;
import be.vds.jtbdive.view.component.TranslatableJXTable;
import be.vds.jtbdive.view.event.DiveLocationObserverEvent;
import be.vds.jtbdive.view.event.DiverObserverEvent;
import be.vds.jtbdive.view.listener.DiveLocationSelectionListener;
import be.vds.jtbdive.view.tablemodels.DiveLocationTableModel;
import be.vds.jtbdive.view.util.LocaleUpdatable;

public class DiveLocationManagerPanel extends JPanel implements LocaleUpdatable, Observer {

    public static final int ACTION_NEW_DIVELOCATION_DIALOG = 2;

    public static final int ACTION_DELETE_DIVELOCATION = 3;

    public static final int ACTION_EDIT_DIVELOCATION = 4;

    private DiveLocationTableModel diveLocationTableModel;

    private TranslatableJXTable diveLocationTable;

    private DiveLocationManagerFacade diveLocationManagerFacade;

    private Set<DiveLocationSelectionListener> diveLocationSelectionListeners = new HashSet<DiveLocationSelectionListener>();

    private JTextField nameTf;

    private JButton addDiveLocationButton;

    private JButton updateDiveLocationButton;

    private JButton deleteDiveLocationButton;

    private JPanel mainPanel;

    private Window parentWindow;

    private Map<Integer, Action> diveLocationActions = new HashMap<Integer, Action>();

    public DiveLocationManagerPanel(Window parentWindow, DiveLocationManagerFacade diveLocationManagerFacade) {
        this.parentWindow = parentWindow;
        this.diveLocationManagerFacade = diveLocationManagerFacade;
        this.diveLocationManagerFacade.addObserver(this);
        createActions();
        init();
    }

    private void createActions() {
        DeleteDiveLocationAction deleteDiveLoactionAction = new DeleteDiveLocationAction(parentWindow, diveLocationManagerFacade);
        this.addDiveLocationSelectionListener(deleteDiveLoactionAction);
        deleteDiveLoactionAction.setEnabled(false);
        deleteDiveLoactionAction.putValue(Action.SHORT_DESCRIPTION, "delete dive location");
        diveLocationActions.put(ACTION_DELETE_DIVELOCATION, deleteDiveLoactionAction);
        NewDiveLocationDialogAction newDiveLocationDialogAction = new NewDiveLocationDialogAction(parentWindow, diveLocationManagerFacade);
        newDiveLocationDialogAction.putValue(Action.SHORT_DESCRIPTION, "new dive location");
        diveLocationActions.put(ACTION_NEW_DIVELOCATION_DIALOG, newDiveLocationDialogAction);
        EditDiveLocationAction editDiveLocationAction = new EditDiveLocationAction(parentWindow, diveLocationManagerFacade);
        this.addDiveLocationSelectionListener(editDiveLocationAction);
        editDiveLocationAction.setEnabled(false);
        editDiveLocationAction.putValue(Action.SHORT_DESCRIPTION, "edit dive location");
        diveLocationActions.put(ACTION_EDIT_DIVELOCATION, editDiveLocationAction);
    }

    private void init() {
        setMinimumSize(new Dimension(240, 100));
        mainPanel = new JPanel();
        double[] cols = { 5, TableLayout.FILL, TableLayout.PREFERRED, 5, TableLayout.PREFERRED, 5, TableLayout.PREFERRED, 5 };
        double[] rows = { 5, TableLayout.MINIMUM, 5, TableLayout.FILL, 5, TableLayout.PREFERRED, 5 };
        TableLayout tl = new TableLayout(cols, rows);
        mainPanel.setLayout(tl);
        createSelectionPanel();
        createTablePanel();
        createButtonsZone();
        this.setLayout(new BorderLayout());
        this.add(new JScrollPane(mainPanel), BorderLayout.CENTER);
    }

    private void createButtonsZone() {
        addDiveLocationButton = new IconOnlyButton();
        addDiveLocationButton.setAction(diveLocationActions.get(ACTION_NEW_DIVELOCATION_DIALOG));
        addDiveLocationButton.setIcon(ResourceManager.getImageIcon("globeadd32.png"));
        updateDiveLocationButton = new IconOnlyButton();
        updateDiveLocationButton.setAction(diveLocationActions.get(ACTION_EDIT_DIVELOCATION));
        updateDiveLocationButton.setIcon(ResourceManager.getImageIcon("globeedit32.png"));
        deleteDiveLocationButton = new IconOnlyButton();
        deleteDiveLocationButton.setAction(diveLocationActions.get(ACTION_DELETE_DIVELOCATION));
        deleteDiveLocationButton.setIcon(ResourceManager.getImageIcon("globedelete32.png"));
        mainPanel.add(addDiveLocationButton, "6, 5");
        mainPanel.add(updateDiveLocationButton, "4, 5");
        mainPanel.add(deleteDiveLocationButton, "2, 5");
    }

    private void createSelectionPanel() {
        nameTf = new JTextField(15);
        nameTf.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    lookup();
                }
            }
        });
        Action action = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                lookup();
            }
        };
        action.putValue(Action.LARGE_ICON_KEY, ResourceManager.getImageIcon("magnifyglass16.png"));
        JButton searchButton = new IconOnlyButton(action);
        mainPanel.add(nameTf, "1, 1, 4, 1");
        mainPanel.add(searchButton, "6, 1");
    }

    private void createTablePanel() {
        diveLocationTableModel = new DiveLocationTableModel(DiveLocationTableModel.HEADERS_1);
        diveLocationTable = new TranslatableJXTable(diveLocationTableModel);
        diveLocationTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        diveLocationTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    boolean b = diveLocationTable.getSelectedRowCount() > 0;
                    if (b) {
                        int row = diveLocationTable.convertRowIndexToModel(diveLocationTable.getSelectedRow());
                        notifyDiveLocationSelectionListeners(diveLocationTableModel.getDiveLocationAt(row));
                    } else {
                        notifyDiveLocationSelectionListeners(null);
                    }
                    diveLocationActions.get(ACTION_DELETE_DIVELOCATION).setEnabled(b);
                    diveLocationActions.get(ACTION_EDIT_DIVELOCATION).setEnabled(b);
                }
            }
        });
        JScrollPane scroll = new JScrollPane(diveLocationTable);
        scroll.setPreferredSize(new Dimension(100, 80));
        mainPanel.add(scroll, "1, 3, 6, 3");
    }

    private void lookup() {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                List<DiveLocation> list = diveLocationManagerFacade.findDiveLocationsByName(nameTf.getText());
                diveLocationTableModel.setDiveLocations(list);
            }
        });
    }

    public void addDiveLocationSelectionListener(DiveLocationSelectionListener diveLocationSelectionListener) {
        diveLocationSelectionListeners.add(diveLocationSelectionListener);
    }

    private void notifyDiveLocationSelectionListeners(DiveLocation diveLocation) {
        for (DiveLocationSelectionListener listener : diveLocationSelectionListeners) {
            listener.diveLocationSelected(diveLocation);
        }
    }

    public void setDiveLocation(DiveLocation diveLocation) {
        List<DiveLocation> l = new ArrayList<DiveLocation>();
        if (diveLocation != null) {
            l.add(diveLocation);
            diveLocationTableModel.setDiveLocations(l);
        } else {
            lookup();
        }
    }

    @Override
    public void updateLocale() {
        diveLocationTable.rebuildColumnNames();
    }

    @Override
    public void update(Observable o, Object arg) {
        DiveLocationObserverEvent diverObserver = (DiveLocationObserverEvent) arg;
        if (diverObserver.getAction() == DiverObserverEvent.SAVE) {
            setDiveLocation(diverObserver.getDiveLocations().get(0));
        } else if (diverObserver.getAction() == DiverObserverEvent.DELETE || diverObserver.getAction() == DiverObserverEvent.UPDATE) {
            lookup();
        }
    }

    public Map<Integer, Action> getDiveLocationActions() {
        return diveLocationActions;
    }
}
