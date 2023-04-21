package pcgen.gui.tabs;

import javax.swing.event.ListDataEvent;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.TransferHandler;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.undo.StateEditable;
import pcgen.gui.facade.AbilityCatagoryFacade;
import pcgen.gui.facade.AbilityFacade;
import pcgen.gui.facade.CharacterFacade;
import pcgen.gui.tools.ChooserPane;
import pcgen.gui.filter.FilterableTreeViewModel;
import pcgen.gui.filter.FilteredTreeViewPanel;
import pcgen.gui.util.DefaultGenericListModel;
import pcgen.gui.util.GenericListModel;
import pcgen.gui.util.GenericListModelWrapper;
import pcgen.gui.util.event.AbstractGenericListDataWrapper;
import pcgen.gui.util.event.GenericListDataListener;
import pcgen.gui.tools.FlippingSplitPane;
import pcgen.gui.util.treeview.*;

/**
 *
 * @author Connor Petty <cpmeister@users.sourceforge.net>
 */
public class AbilityChooserTab extends ChooserPane implements StateEditable {

    private static final DataView<AbilityFacade> abilityDataView = new DataView<AbilityFacade>() {

        private final List<? extends DataViewColumn> dataColumns = Arrays.asList(new DefaultDataViewColumn("Type", String.class), new DefaultDataViewColumn("Mult", Boolean.class), new DefaultDataViewColumn("Stack", Boolean.class), new DefaultDataViewColumn("Description", String.class), new DefaultDataViewColumn("Source", String.class));

        public List<? extends DataViewColumn> getDataColumns() {
            return dataColumns;
        }

        public List<?> getData(AbilityFacade obj) {
            return Arrays.asList(getTypes(obj.getTypes()), obj.isMult(), obj.isStackable(), obj.getDescription(), obj.getSource());
        }

        private String getTypes(List<String> types) {
            String ret = types.get(0);
            for (int x = 1; x < types.size(); x++) {
                ret += ", " + types.get(x);
            }
            return ret;
        }
    };

    private final FilteredTreeViewPanel availableTreeViewPanel;

    private final FilteredTreeViewPanel selectedTreeViewPanel;

    private final JTable catagoryTable;

    private AvailableAbilityTreeViewModel availableModel;

    private SelectedAbilityTreeViewModel selectedModel;

    private CatagoryTableModel catagoryModel;

    private AbilityCatagoryFacade selectedCatagory;

    private AbilityFacade selectedAbility;

    public AbilityChooserTab() {
        this.availableTreeViewPanel = new FilteredTreeViewPanel();
        this.selectedTreeViewPanel = new FilteredTreeViewPanel();
        this.catagoryTable = new JTable();
        initComponents();
    }

    private void initComponents() {
        ListSelectionModel selectionModel;
        selectionModel = selectedTreeViewPanel.getSelectionModel();
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        selectionModel.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    List<Object> data = selectedTreeViewPanel.getSelectedData();
                    AbilityFacade ability = null;
                    if (!data.isEmpty()) {
                        Object obj = data.get(0);
                        AbilityCatagoryFacade catagory = selectedCatagory;
                        if (obj instanceof AbilityFacade) {
                            ability = (AbilityFacade) obj;
                            catagory = selectedModel.getCatagoryForAbility(ability);
                        } else if (obj instanceof AbilityCatagoryFacade) {
                            catagory = (AbilityCatagoryFacade) obj;
                        }
                        setSelectedCatagory(catagory);
                    }
                    setSelectedAbility(ability);
                }
            }
        });
        selectionModel = availableTreeViewPanel.getSelectionModel();
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        selectionModel.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    List<Object> data = availableTreeViewPanel.getSelectedData();
                    if (!data.isEmpty() && data.get(0) instanceof AbilityFacade) {
                        setSelectedAbility((AbilityFacade) data.get(0));
                    } else {
                        setSelectedAbility(null);
                    }
                }
            }
        });
        FlippingSplitPane pane = new FlippingSplitPane(JSplitPane.VERTICAL_SPLIT, true, selectedTreeViewPanel, availableTreeViewPanel);
        pane.setOneTouchExpandable(true);
        pane.setDividerSize(7);
        setPrimaryChooserComponent(pane);
        selectionModel = catagoryTable.getSelectionModel();
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        selectionModel.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    setSelectedCatagory(catagoryModel.getCatagory(e.getFirstIndex()));
                }
            }
        });
        setSecondaryChooserComponent(new JScrollPane(catagoryTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
    }

    private void setSelectedAbility(AbilityFacade ability) {
        this.selectedAbility = ability;
        setInfoPaneText(ability.getInfo());
    }

    private void setSelectedCatagory(AbilityCatagoryFacade catagory) {
        this.selectedCatagory = catagory;
        availableModel.setAbilityCatagory(catagory);
        setInfoPaneTitle(catagory.getName() + " Info");
    }

    private static final class SelectedAbilityTreeViewModel extends AbstractGenericListDataWrapper<AbilityCatagoryFacade> implements FilterableTreeViewModel<AbilityFacade> {

        private final List<? extends TreeView<AbilityFacade>> treeViews = null;

        private final Map<AbilityFacade, AbilityCatagoryFacade> catagoryMap;

        private final Map<GenericListModel<AbilityFacade>, GenericListDataListener> listenerMap;

        private final DefaultGenericListModel<AbilityFacade> model;

        private final CharacterFacade character;

        public SelectedAbilityTreeViewModel(CharacterFacade character, DefaultGenericListModel<AbilityCatagoryFacade> catagories) {
            super(catagories);
            this.catagoryMap = new HashMap<AbilityFacade, AbilityCatagoryFacade>();
            this.listenerMap = new HashMap<GenericListModel<AbilityFacade>, GenericListDataListener>();
            this.model = new DefaultGenericListModel<AbilityFacade>();
            this.character = character;
            setModel(catagories);
        }

        protected void addData(Collection<? extends AbilityCatagoryFacade> catagories) {
            AbilityCatagoryFacade[] catagoryArray = catagories.toArray(new AbilityCatagoryFacade[0]);
            for (final AbilityCatagoryFacade catagory : catagoryArray) {
                GenericListModel<AbilityFacade> abilityList = character.getAbilities(catagory);
                GenericListDataListener<AbilityFacade> listener = new AbilityModelListener(abilityList) {

                    @Override
                    protected void addData(Collection<? extends AbilityFacade> data) {
                        for (AbilityFacade ability : data) {
                            catagoryMap.put(ability, catagory);
                        }
                        model.addAll(data);
                    }
                };
                listenerMap.put(abilityList, listener);
            }
        }

        protected void removeData(Collection<? extends AbilityCatagoryFacade> catagories) {
            for (AbilityCatagoryFacade catagory : catagories) {
                GenericListModel<AbilityFacade> abilityList = character.getAbilities(catagory);
                abilityList.removeGenericListDataListener(listenerMap.get(abilityList));
                listenerMap.remove(abilityList);
                final List<AbilityFacade> listWrapper = new GenericListModelWrapper<AbilityFacade>(abilityList);
                model.removeAll(listWrapper);
                catagoryMap.keySet().removeAll(listWrapper);
            }
        }

        public AbilityCatagoryFacade getCatagoryForAbility(AbilityFacade ability) {
            return catagoryMap.get(ability);
        }

        public List<? extends TreeView<AbilityFacade>> getTreeViews() {
            return treeViews;
        }

        public int getDefaultTreeViewIndex() {
            return 0;
        }

        public DataView<AbilityFacade> getDataView() {
            return abilityDataView;
        }

        public DefaultGenericListModel<AbilityFacade> getDataModel() {
            return model;
        }

        private abstract class AbilityModelListener extends AbstractGenericListDataWrapper<AbilityFacade> {

            public AbilityModelListener(GenericListModel<AbilityFacade> model) {
                super(model);
            }

            @Override
            protected void removeData(Collection<? extends AbilityFacade> data) {
                model.removeAll(data);
                catagoryMap.keySet().removeAll(data);
            }
        }

        public Class<AbilityFacade> getFilterClass() {
            return AbilityFacade.class;
        }
    }

    private static final class AvailableAbilityTreeViewModel extends AbstractGenericListDataWrapper<AbilityFacade> implements FilterableTreeViewModel<AbilityFacade> {

        private final DefaultGenericListModel<AbilityFacade> dataModel;

        private final CharacterFacade character;

        private AbilityCatagoryFacade catagory;

        public AvailableAbilityTreeViewModel(CharacterFacade character) {
            this.dataModel = new DefaultGenericListModel<AbilityFacade>();
            this.character = character;
        }

        public List<? extends TreeView<AbilityFacade>> getTreeViews() {
            return null;
        }

        public int getDefaultTreeViewIndex() {
            return 0;
        }

        public DataView<AbilityFacade> getDataView() {
            return abilityDataView;
        }

        public DefaultGenericListModel<AbilityFacade> getDataModel() {
            return dataModel;
        }

        public void setAbilityCatagory(AbilityCatagoryFacade catagory) {
            this.catagory = catagory;
            GenericListModel<AbilityFacade> abilities = character.getDataSet().getAbilities(catagory);
            dataModel.clear();
            setModel(abilities);
        }

        protected void addData(Collection<? extends AbilityFacade> abilities) {
            HashSet<AbilityFacade> abilitySet = new HashSet<AbilityFacade>(abilities);
            Iterator<AbilityFacade> it = abilitySet.iterator();
            while (it.hasNext()) {
                AbilityFacade ability = it.next();
                if (!ability.isMult() && character.hasAbility(catagory, ability)) {
                    it.remove();
                }
            }
            dataModel.addAll(abilitySet);
        }

        @Override
        protected void removeData(Collection<? extends AbilityFacade> data) {
            dataModel.removeAll(data);
        }

        public Class<AbilityFacade> getFilterClass() {
            return AbilityFacade.class;
        }
    }

    private static final class CatagoryTableModel extends AbstractTableModel implements ListDataListener {

        private CharacterFacade character;

        private DefaultGenericListModel<AbilityCatagoryFacade> catagories;

        public CatagoryTableModel(CharacterFacade character, DefaultGenericListModel<AbilityCatagoryFacade> catagories) {
            this.character = character;
            this.catagories = catagories;
            catagories.addListDataListener(this);
        }

        public AbilityCatagoryFacade getCatagory(int index) {
            return catagories.getElementAt(index);
        }

        public int getRowCount() {
            return catagories.getSize();
        }

        public int getColumnCount() {
            return 2;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            if (columnIndex == 1) {
                return true;
            }
            return false;
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            AbilityCatagoryFacade catagory = catagories.getElementAt(rowIndex);
            switch(columnIndex) {
                case 0:
                    return catagory;
                case 1:
                    return character.getRemainingSelections(catagory);
                default:
                    throw new IndexOutOfBoundsException();
            }
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            character.setRemainingSelection(catagories.getElementAt(rowIndex), (Integer) aValue);
        }

        @Override
        public String getColumnName(int column) {
            if (column == 0) {
                return "Catagory";
            }
            return "Remaining";
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 1) {
                return Integer.class;
            }
            return Object.class;
        }

        public void intervalAdded(ListDataEvent e) {
            fireTableRowsInserted(e.getIndex0(), e.getIndex1());
        }

        public void intervalRemoved(ListDataEvent e) {
            fireTableRowsDeleted(e.getIndex0(), e.getIndex1());
        }

        public void contentsChanged(ListDataEvent e) {
            fireTableRowsUpdated(e.getIndex0(), e.getIndex1());
        }
    }

    private final class AbilityTransferHandler extends TransferHandler {

        private CharacterFacade character;

        public AbilityTransferHandler(CharacterFacade character) {
            this.character = character;
        }

        private final DataFlavor abilityFlavor = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType + ";class=" + AbilityFacade.class.getName(), null);

        @Override
        public int getSourceActions(JComponent c) {
            if (selectedAbility != null) {
                if (selectedTreeViewPanel.isAncestorOf(c)) {
                    return MOVE;
                }
                if (selectedAbility.isMult()) {
                    return COPY;
                }
                if (!character.hasAbility(selectedCatagory, selectedAbility)) {
                    return MOVE;
                }
            }
            return NONE;
        }

        @Override
        protected Transferable createTransferable(JComponent c) {
            final AbilityFacade transferAbility = selectedAbility;
            return new Transferable() {

                public DataFlavor[] getTransferDataFlavors() {
                    return new DataFlavor[] { abilityFlavor };
                }

                public boolean isDataFlavorSupported(DataFlavor flavor) {
                    return abilityFlavor == flavor;
                }

                public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
                    if (!isDataFlavorSupported(flavor)) {
                        throw new UnsupportedFlavorException(flavor);
                    }
                    return transferAbility;
                }
            };
        }

        @Override
        public boolean canImport(JComponent comp, DataFlavor[] transferFlavors) {
            return transferFlavors[0] == abilityFlavor;
        }

        @Override
        public boolean importData(JComponent comp, Transferable t) {
            if (selectedTreeViewPanel.isAncestorOf(comp)) {
                try {
                    AbilityFacade ability = (AbilityFacade) t.getTransferData(abilityFlavor);
                    character.addAbility(selectedCatagory, ability);
                    return true;
                } catch (UnsupportedFlavorException ex) {
                    Logger.getLogger(AbilityChooserTab.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(AbilityChooserTab.class.getName()).log(Level.SEVERE, null, ex);
                }
                return false;
            }
            return true;
        }

        @Override
        protected void exportDone(JComponent source, Transferable data, int action) {
            if (action == COPY) {
                return;
            }
        }
    }

    private static final String SELECTED_TREEVIEW_PANEL_STATE = "SelectedTreeViewPanelState";

    private static final String AVAILABLE_TREEVIEW_PANEL_STATE = "AvailableTreeViewPanelState";

    public Hashtable<Object, Object> createState(CharacterFacade character, DefaultGenericListModel<AbilityCatagoryFacade> catagories) {
        Hashtable<Object, Object> state = new Hashtable<Object, Object>();
        state.put(CatagoryTableModel.class, new CatagoryTableModel(character, catagories));
        SelectedAbilityTreeViewModel selectedAbilityModel = new SelectedAbilityTreeViewModel(character, catagories);
        AvailableAbilityTreeViewModel availableAbilityModel = new AvailableAbilityTreeViewModel(character);
        state.put(SelectedAbilityTreeViewModel.class, selectedAbilityModel);
        state.put(AvailableAbilityTreeViewModel.class, availableAbilityModel);
        state.put(SELECTED_TREEVIEW_PANEL_STATE, selectedTreeViewPanel.createState(character, selectedAbilityModel));
        state.put(AVAILABLE_TREEVIEW_PANEL_STATE, availableTreeViewPanel.createState(character, availableAbilityModel));
        state.put(AbilityTransferHandler.class, new AbilityTransferHandler(character));
        return state;
    }

    public void storeState(Hashtable<Object, Object> state) {
    }

    public void restoreState(Hashtable<?, ?> state) {
        catagoryModel = (CatagoryTableModel) state.get(CatagoryTableModel.class);
        selectedModel = (SelectedAbilityTreeViewModel) state.get(SelectedAbilityTreeViewModel.class);
        availableModel = (AvailableAbilityTreeViewModel) state.get(AvailableAbilityTreeViewModel.class);
        AbilityTransferHandler handler = (AbilityTransferHandler) state.get(AbilityTransferHandler.class);
        catagoryTable.setModel(catagoryModel);
        selectedTreeViewPanel.setTransferHandler(handler);
        availableTreeViewPanel.setTransferHandler(handler);
        selectedTreeViewPanel.restoreState((Hashtable<?, ?>) state.get(SELECTED_TREEVIEW_PANEL_STATE));
        availableTreeViewPanel.restoreState((Hashtable<?, ?>) state.get(AVAILABLE_TREEVIEW_PANEL_STATE));
    }
}
