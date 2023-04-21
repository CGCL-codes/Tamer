package net.entelijan.cobean.table;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.AbstractButton;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.JTextComponent;
import net.entelijan.cobean.core.ColumnDesc;
import net.entelijan.cobean.core.IInitializer;
import net.entelijan.cobean.core.ISingleSelectionListCobean;
import net.entelijan.cobean.core.SelectionMode;
import net.entelijan.cobean.core.TextComponentActionMode;
import net.entelijan.cobean.core.impl.AbstractComponentBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DefaultSingleSelectionTableInitializer<T> implements IInitializer<ISingleSelectionListCobean<T>> {

    private static Log log = LogFactory.getLog(DefaultSingleSelectionTableInitializer.class);

    protected List<ColumnDesc> columnDescs;

    protected TextComponentActionMode filterActionMode = TextComponentActionMode.ON_KEY_STROKE;

    protected JTextComponent coFilterInput;

    protected AbstractButton coFilterButton;

    protected JTable coTable;

    private IBeanTreater beanTreater = BeanTreatingFactory.createTreater();

    public DefaultSingleSelectionTableInitializer() {
        super();
    }

    public ISingleSelectionListCobean<T> initialize() {
        final TableRowSorter<AbstractBeansTableModel<T>> sorter = new TableRowSorter<AbstractBeansTableModel<T>>();
        final int[] filterIndizes = createFilterIndizes();
        if (coFilterInput != null) {
            if (this.filterActionMode == TextComponentActionMode.ON_ACTION && coFilterButton == null) {
                throw new IllegalStateException("You must define a filter button if you want to use the filtre method " + TextComponentActionMode.ON_ACTION);
            }
            if (this.filterActionMode == TextComponentActionMode.ON_ACTION) {
                coFilterButton.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        sorter.setRowFilter(RowFilter.regexFilter(coFilterInput.getText(), filterIndizes));
                    }
                });
            } else {
                if (filterIndizes.length == 0) {
                    log.warn("No filterable column was defined. Filter is disabled");
                    coFilterInput.setEnabled(false);
                } else {
                    switch(this.filterActionMode) {
                        case ON_FOCUS_LOST:
                            coFilterInput.addFocusListener(new FocusAdapter() {

                                @Override
                                public void focusLost(FocusEvent e) {
                                    sorter.setRowFilter(RowFilter.regexFilter(coFilterInput.getText(), filterIndizes));
                                }
                            });
                            break;
                        case ON_KEY_STROKE:
                            coFilterInput.addKeyListener(new KeyAdapter() {

                                @Override
                                public void keyReleased(KeyEvent e) {
                                    sorter.setRowFilter(RowFilter.regexFilter(coFilterInput.getText(), filterIndizes));
                                }
                            });
                            break;
                        default:
                    }
                }
            }
        }
        ArrayList<T> emptyList = new ArrayList<T>();
        initTableInternal(sorter, emptyList);
        final ComponentBean re = new ComponentBean(sorter, emptyList);
        DefaultListSelectionModel selModel = new DefaultListSelectionModel() {

            private static final long serialVersionUID = 1L;

            @Override
            public void setSelectionInterval(int from, int to) {
                T oldValue = re.getSelectedValue();
                super.setSelectionInterval(from, to);
                T newValue = getSelection();
                re.fireSelectionChanged(oldValue, newValue);
            }
        };
        selModel.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (coTable.getSelectedRow() < 0 && selectedValueNoLongerInValues(re)) {
                    re.clearSelection();
                }
            }
        });
        coTable.setSelectionModel(selModel);
        return re;
    }

    protected boolean selectedValueNoLongerInValues(ComponentBean bean) {
        boolean re = true;
        T sv = bean.getSelectedValue();
        if (sv != null && bean.getValue() != null) {
            for (T v : bean.getValue()) {
                if (v.equals(sv)) {
                    re = false;
                }
            }
        }
        return re;
    }

    @SuppressWarnings("unchecked")
    private T getSelection() {
        ISelectionProvider<T> selProv = (ISelectionProvider<T>) coTable.getModel();
        List<T> selection = selProv.getSelection();
        T oldValue;
        if (selection == null || selection.size() == 0) {
            oldValue = null;
        } else {
            oldValue = selection.get(0);
        }
        return oldValue;
    }

    private int[] createFilterIndizes() {
        ArrayList<Integer> reli = new ArrayList<Integer>();
        int i = 0;
        if (columnDescs == null || columnDescs.size() == 0) {
            throw new IllegalStateException("You must define at least one column description to initialize a table");
        } else {
            for (ColumnDesc columnDesc : columnDescs) {
                if (columnDesc.isFilterable()) {
                    reli.add(i);
                }
                i++;
            }
        }
        int j = 0;
        int[] re = new int[reli.size()];
        for (Integer index : reli) {
            re[j++] = index;
        }
        return re;
    }

    private void initTableInternal(TableRowSorter<AbstractBeansTableModel<T>> sorter, Collection<T> data) {
        coTable.setRowSelectionAllowed(true);
        coTable.setSelectionMode(SelectionMode.SINGLE.getSwingSelectionMode());
        AbstractBeansTableModel<T> beansTableModel = createBeansTableModel(data, coTable, sorter);
        sorter.setModel(beansTableModel);
        initSorter(sorter);
        coTable.setModel(beansTableModel);
        coTable.setRowSorter(sorter);
        initTableColumnModel(coTable);
    }

    private void initSorter(TableRowSorter<AbstractBeansTableModel<T>> sorter) {
        if (columnDescs != null) {
            int i = 0;
            for (ColumnDesc cd : columnDescs) {
                sorter.setSortable(i, cd.isSortable());
                if (cd.getComparator() != null) {
                    sorter.setComparator(i, cd.getComparator());
                }
                i++;
            }
        }
    }

    private void initTableColumnModel(JTable table) {
        final TableColumnModel columnModel = new SwtWorkaroundTableColumnModel();
        if (columnDescs != null && columnDescs.size() > 0) {
            int i = 0;
            for (ColumnDesc desc : columnDescs) {
                columnModel.addColumn(createTableColumn(desc, i++));
            }
        } else {
            throw new IllegalStateException("No columns defined");
        }
        table.setColumnModel(columnModel);
    }

    private TableColumn createTableColumn(final ColumnDesc desc, int index) {
        final TableColumn re = new TableColumn(index);
        if (desc.getWidth() >= 0) {
            re.setPreferredWidth(desc.getWidth());
        }
        re.setHeaderValue(desc.getHeaderName());
        if (desc.getConverter() != null) {
            re.setCellRenderer(new DefaultTableCellRenderer() {

                private static final long serialVersionUID = 1L;

                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    final Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    if (comp instanceof JLabel) {
                        ((JLabel) comp).setText(desc.getConverter().objectToString(value));
                    }
                    return comp;
                }
            });
        }
        return re;
    }

    private AbstractBeansTableModel<T> createBeansTableModel(Collection<T> values, final JTable table, final TableRowSorter<AbstractBeansTableModel<T>> sorter) {
        return new AbstractBeansTableModel<T>(values, columnDescs, table) {

            private static final long serialVersionUID = 1L;

            public List<T> getSelection() {
                ArrayList<T> re = new ArrayList<T>();
                int[] selectedRowIndices = table.getSelectedRows();
                for (int index : selectedRowIndices) {
                    int mindex = sorter.convertRowIndexToModel(index);
                    re.add(getBean(mindex));
                }
                return re;
            }
        };
    }

    public class ComponentBean extends AbstractComponentBean implements ISingleSelectionListCobean<T> {

        private final TableRowSorter<AbstractBeansTableModel<T>> sorter;

        private List<T> values;

        private T selectedValue;

        private ComponentBean(TableRowSorter<AbstractBeansTableModel<T>> sorter, List<T> values) {
            this.sorter = sorter;
            this.values = values;
        }

        public void fireSelectionChanged(T oldValue, T newValue) {
            log.debug("[fireSelectionChanged] " + oldValue + "->" + newValue);
            if (!beanTreater.equals(oldValue, newValue)) {
                this.propertyChangeSupport.firePropertyChange("selectedValue", oldValue, newValue);
                selectedValue = newValue;
            }
        }

        public List<T> getValue() {
            List<T> re = this.values;
            if (this.values != null && this.values.size() == 0) {
                re = null;
            }
            return re;
        }

        public void setValue(List<T> value) {
            List<T> oldValue = getValue();
            this.values = value;
            propertyChangeSupport.firePropertyChange("value", oldValue, value);
            initTableInternal(sorter, (Collection<T>) value);
            initSorter(sorter);
            coTable.repaint();
        }

        public T getSelectedValue() {
            return this.selectedValue;
        }

        public void setSelectedValue(T value) {
            int oldIndex = -1;
            int[] selectedRows = coTable.getSelectedRows();
            if (selectedRows.length > 0) {
                oldIndex = selectedRows[0];
            }
            int modelSelIndex = getIndexOf(values, value);
            int viewSelIndex;
            if (modelSelIndex >= 0) {
                viewSelIndex = coTable.getRowSorter().convertRowIndexToView(modelSelIndex);
            } else {
                viewSelIndex = -1;
            }
            log.debug("[setSelectedValue] (" + oldIndex + ")->(" + viewSelIndex + ")");
            ListSelectionModel selectionModel = coTable.getSelectionModel();
            if (viewSelIndex != oldIndex) {
                if (viewSelIndex < 0) {
                    clearSelection();
                } else {
                    selectionModel.setSelectionInterval(viewSelIndex, viewSelIndex);
                    coTable.getColumnModel().getSelectionModel().setSelectionInterval(0, 0);
                }
            }
        }

        private void clearSelection() {
            clearSelectionModel(coTable.getSelectionModel());
            clearSelectionModel(coTable.getColumnModel().getSelectionModel());
            fireSelectionChanged(this.selectedValue, null);
        }

        /**
		 * @param selectionModel
		 */
        private void clearSelectionModel(final ListSelectionModel selectionModel) {
            selectionModel.clearSelection();
            selectionModel.setLeadSelectionIndex(-1);
        }
    }

    /**
	 * Returns the index of the bean in the list of beans.
	 */
    public int getIndexOf(List<T> beans, T bean) {
        int re = -1;
        int idx = 0;
        if (bean != null) {
            for (T b : beans) {
                if (beanTreater.equals(bean, b)) {
                    re = idx;
                    break;
                }
                idx++;
            }
        }
        return re;
    }

    public List<ColumnDesc> getColumnDescs() {
        return columnDescs;
    }

    public void setColumnDescs(List<ColumnDesc> columnDescs) {
        this.columnDescs = columnDescs;
    }

    public TextComponentActionMode getFilterActionMode() {
        return filterActionMode;
    }

    public void setFilterActionMode(TextComponentActionMode filterActionMode) {
        this.filterActionMode = filterActionMode;
    }

    public JTextComponent getCoFilterInput() {
        return coFilterInput;
    }

    public void setCoFilterInput(JTextComponent coFilterInput) {
        this.coFilterInput = coFilterInput;
    }

    public JTable getCoTable() {
        return coTable;
    }

    public void setCoTable(JTable coTable) {
        this.coTable = coTable;
    }

    public AbstractButton getCoFilterButton() {
        return coFilterButton;
    }

    public void setCoFilterButton(AbstractButton coFilterButton) {
        this.coFilterButton = coFilterButton;
    }
}
