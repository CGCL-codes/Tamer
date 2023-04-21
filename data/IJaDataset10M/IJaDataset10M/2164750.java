package be.vds.jtbdive.core.view.panel;

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
import be.vds.jtbdive.core.actions.DeleteDiverAction;
import be.vds.jtbdive.core.actions.EditDiverAction;
import be.vds.jtbdive.core.actions.NewDiverDialogAction;
import be.vds.jtbdive.core.model.Diver;
import be.vds.jtbdive.core.model.DiverManagerFacade;
import be.vds.jtbdive.core.utils.ResourceManager;
import be.vds.jtbdive.core.view.component.IconOnlyButton;
import be.vds.jtbdive.core.view.component.TranslatableJXTable;
import be.vds.jtbdive.core.view.event.DiverObserverEvent;
import be.vds.jtbdive.core.view.listener.DiverSelectionListener;
import be.vds.jtbdive.core.view.tablemodels.DiverTableModel;
import be.vds.jtbdive.core.view.util.LocaleUpdatable;

public class DiverManagerPanel extends JPanel implements LocaleUpdatable, Observer {

    public static final int ACTION_NEW_DIVER_DIALOG = 2;

    public static final int ACTION_DELETE_DIVER = 3;

    public static final int ACTION_EDIT_DIVER = 4;

    private DiverTableModel diversTableModel;

    private TranslatableJXTable diversTable;

    private DiverManagerFacade diverManagerFacade;

    private Set<DiverSelectionListener> diverSelectionListeners = new HashSet<DiverSelectionListener>();

    private JTextField nameTf;

    private JButton addDiverButton;

    private JButton updateDiverButton;

    private JButton deleteDiverButton;

    private JPanel mainPanel;

    private Map<Integer, Action> diverActions = new HashMap<Integer, Action>();

    private Window parentWindow;

    public DiverManagerPanel(Window parentWindow, DiverManagerFacade logBookApplicationFacade) {
        this.parentWindow = parentWindow;
        this.diverManagerFacade = logBookApplicationFacade;
        this.diverManagerFacade.addObserver(this);
        createActions();
        init();
    }

    private void createActions() {
        NewDiverDialogAction newDiverDialogAction = new NewDiverDialogAction(parentWindow, diverManagerFacade);
        newDiverDialogAction.putValue(Action.LARGE_ICON_KEY, ResourceManager.getImageIcon("diveradd32.png"));
        newDiverDialogAction.putValue(Action.SHORT_DESCRIPTION, "new diver");
        diverActions.put(ACTION_NEW_DIVER_DIALOG, newDiverDialogAction);
        DeleteDiverAction deleteDiverAction = new DeleteDiverAction(parentWindow, diverManagerFacade);
        deleteDiverAction.setEnabled(false);
        this.addDiverSelectionListener(deleteDiverAction);
        deleteDiverAction.putValue(Action.LARGE_ICON_KEY, ResourceManager.getImageIcon("diverdelete32.png"));
        diverActions.put(ACTION_DELETE_DIVER, deleteDiverAction);
        EditDiverAction editDiverAction = new EditDiverAction(parentWindow, diverManagerFacade);
        editDiverAction.setEnabled(false);
        this.addDiverSelectionListener(editDiverAction);
        editDiverAction.putValue(Action.LARGE_ICON_KEY, ResourceManager.getImageIcon("diveredit32.png"));
        editDiverAction.putValue(Action.SHORT_DESCRIPTION, "edit diver");
        diverActions.put(ACTION_EDIT_DIVER, editDiverAction);
    }

    private void init() {
        setMinimumSize(new Dimension(240, 100));
        mainPanel = new JPanel();
        double[] cols = { 5, TableLayout.FILL, TableLayout.PREFERRED, 5, TableLayout.PREFERRED, 2, TableLayout.PREFERRED, 5 };
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
        addDiverButton = new IconOnlyButton(diverActions.get(ACTION_NEW_DIVER_DIALOG));
        updateDiverButton = new IconOnlyButton(diverActions.get(ACTION_EDIT_DIVER));
        deleteDiverButton = new IconOnlyButton(diverActions.get(ACTION_DELETE_DIVER));
        deleteDiverButton.setEnabled(false);
        mainPanel.add(addDiverButton, "6, 5");
        mainPanel.add(updateDiverButton, "4, 5");
        mainPanel.add(deleteDiverButton, "2, 5");
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
        diversTableModel = new DiverTableModel(DiverTableModel.HEADERS_1);
        diversTable = new TranslatableJXTable(diversTableModel);
        diversTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        diversTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    boolean b = diversTable.getSelectedRowCount() > 0;
                    if (b) {
                        int row = diversTable.convertRowIndexToModel(diversTable.getSelectedRow());
                        notifyDiversSelectionListeners(diversTableModel.getDiverAt(row));
                    } else {
                        notifyDiversSelectionListeners(null);
                    }
                    diverActions.get(ACTION_DELETE_DIVER).setEnabled(b);
                    diverActions.get(ACTION_EDIT_DIVER).setEnabled(b);
                }
            }
        });
        JScrollPane scroll = new JScrollPane(diversTable);
        scroll.setPreferredSize(new Dimension(100, 80));
        mainPanel.add(scroll, "1, 3, 6, 3");
    }

    private void lookup() {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                List<Diver> list = diverManagerFacade.findDiversByLastName(nameTf.getText());
                diversTableModel.setDivers(list);
            }
        });
    }

    public void addDiverSelectionListener(DiverSelectionListener diverSelectionListener) {
        diverSelectionListeners.add(diverSelectionListener);
    }

    private void notifyDiversSelectionListeners(Diver diver) {
        for (DiverSelectionListener listener : diverSelectionListeners) {
            listener.diverSelected(diver);
        }
    }

    public void setDiver(Diver diver) {
        List<Diver> l = new ArrayList<Diver>();
        if (diver != null) {
            l.add(diver);
            diversTableModel.setDivers(l);
        } else {
            lookup();
        }
    }

    @Override
    public void updateLocale() {
        diversTable.rebuildColumnNames();
    }

    @Override
    public void update(Observable o, Object arg) {
        DiverObserverEvent diverObserver = (DiverObserverEvent) arg;
        if (diverObserver.getAction() == DiverObserverEvent.SAVE) {
            setDiver(diverObserver.getDivers().get(0));
        } else if (diverObserver.getAction() == DiverObserverEvent.DELETE || diverObserver.getAction() == DiverObserverEvent.UPDATE) {
            lookup();
        }
    }

    public Map<Integer, Action> getDiverActions() {
        return diverActions;
    }
}
